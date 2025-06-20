package com.example.jpos_springboot.server.utils;

import java.util.Set;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JPosUtil {

    private static final Set<Integer> SENSITIVE_FIELDS = Set.of(35, 36, 45, 52, 55, 127);
    
    public static String getISOKey(ISOMsg isoMsg) throws ISOException {
        return isoMsg.getMTI()
                + getFieldValue(isoMsg, 7, "")
                + getFieldValue(isoMsg, 11, "")
                + getFieldValue(isoMsg, 37, "")
                + getFieldValue(isoMsg, 41, "");
    }

    public static String getFieldValue(ISOMsg isoMsg, int fieldNumber, String defaultValue) throws ISOException {
        return isoMsg.hasField(fieldNumber) ? isoMsg.getString(fieldNumber) : defaultValue;
    }

    // field 2 mask first 6 number and last 4 number
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
                    fieldVal = maskPAN(isoMsg.getString(i));
                }

                else if (i == 14) {
                    fieldVal = maskExpiredDate(isoMsg.getString(i));
                }

                else if (SENSITIVE_FIELDS.contains(i)) {
                    fieldVal = "[PROTECTED]";
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

    public static String maskPAN(String pan) {
        if (pan.length() < 10) return maskAll(pan);
        return pan.replaceAll("(?<=\\d{6})\\d(?=\\d{4})", "*");
    }

    public static String maskExpiredDate(String expiredDate) {
        return expiredDate.replaceAll("(?<=\\d{2})\\d{2}", "**");
    }

    public static String maskAll(String value) {
        return "*".repeat(value.length());
    }
}
