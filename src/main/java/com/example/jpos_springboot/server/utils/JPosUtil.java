package com.example.jpos_springboot.server.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JPosUtil {

    private static final Set<Integer> MASK_FIELDS = Set.of(35, 36, 45, 52);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // convert isomsg to json
    public static String isoMsgToJson(ISOMsg isoMsg) throws ISOException {
        try {
            Map<String, String> isoFields = new HashMap<>();

            isoFields.put("MTI", isoMsg.getMTI());

            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    isoFields.put(String.valueOf(i), isoMsg.getString(i));
                }
            }

            return objectMapper.writeValueAsString(isoFields);
        } catch (Exception e) {
            log.error("Error converting ISOMsg to JSON", e);
            throw new ISOException("Error converting ISOMsg to JSON", e);
        }
    }

    // conver json to isomsg
    public static ISOMsg jsonToIsoMsg(String json) throws ISOException {
        try {
            Map<String, String> isoFields = objectMapper.readValue(json, Map.class);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setMTI(isoFields.get("MTI"));

            for (Map.Entry<String, String> entry : isoFields.entrySet()) {
                if (!"MTI".equals(entry.getKey())) {
                    isoMsg.set(Integer.parseInt(entry.getKey()), entry.getValue());
                }
            }

            return isoMsg;
        } catch (Exception e) {
            log.error("Error converting JSON to ISOMsg", e);
            throw new ISOException("Error converting JSON to ISOMsg", e);
        }
    }

    // field 2 keep first 6 number and last 4 number
    // field 35, 36, 45, 52 mask all
    public static String getISOMessage(ISOMsg msg) throws ISOException {
        StringBuilder isoMessage = new StringBuilder();
        ISOMsg isoMsg = (ISOMsg) msg.clone();

        try {
            isoMessage.append("MTI: ").append(isoMsg.getMTI());

            for (int i = 2; i <= isoMsg.getMaxField(); i++) {
                String fieldVal;

                if (!isoMsg.hasField(i)) {
                    continue;
                }

                if (i == 2) {
                    fieldVal = maskSensitive(isoMsg.getString(i));
                }

                else if (MASK_FIELDS.contains(i)) {
                    fieldVal = "[MASKED]";
                }

                else if (i == 14) {
                    fieldVal = maskExpiredDate(isoMsg.getString(i));
                }

                else {
                    fieldVal = isoMsg.getString(i);
                }

                isoMessage.append(", F").append(i).append(": ").append(fieldVal);
            }

        } catch (Exception e) {
            log.error("Error processing ISO 8583 message", e);
        }

        return isoMessage.toString();
    }

    public static String maskSensitive(String sensitiveString) {
        return ISOUtil.protect(sensitiveString, '*');
    }

    public static String maskExpiredDate(String expiredDate) {
        return expiredDate.replaceAll("(?<=\\d{2})\\d{2}", "**");
    }
}
