package com.example.jpos_springboot.server.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JPosUtil {

    private static final Set<Integer> SENSITIVE_FIELDS = Set.of(36, 45, 52, 55, 57, 62, 127);

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

                if (i == 2 || i == 35 || i == 52) {
                    fieldVal = ISOUtil.protect(isoMsg.getString(i), '*');
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

    public static String maskExpiredDate(String expiredDate) {
        return expiredDate.replaceAll("(?<=\\d{2})\\d{2}", "**");
    }

    public static void compareISOMsg(ISOMsg before, ISOMsg after) {
        try {
            Set<Integer> allFields = new HashSet<>();
            for (int i = 0; i <= Math.max(before.getMaxField(), after.getMaxField()); i++) {
                if (before.hasField(i) || after.hasField(i)) {
                    allFields.add(i);
                }
            }

            for (Integer i : allFields) {
                String beforeVal = before.hasField(i) ? before.getString(i) : "<null>";
                String afterVal = after.hasField(i) ? after.getString(i) : "<null>";

                if (!Objects.equals(beforeVal, afterVal)) {
                    log.info("Field %03d changed: '%s' -> '%s'%n", i, beforeVal, afterVal);
                }
            }
        } catch (Exception e) {
            log.error("Error comparing ISO messages: " + e.getMessage());
        }
    }

}
