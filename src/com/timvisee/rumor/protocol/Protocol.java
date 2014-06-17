package com.timvisee.rumor.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Protocol {

    private static char PACKET_HEADER = 1;
    private static char PACKET_FOOTER = 2;
    private static char PACKET_PART_SEPARATOR = 3;
    private static char PACKET_DATA_PART_SEPARATOR= 4;
    private static char PACKET_DATA_PART_ARRAY_SEPARATOR = 5;

    private static int DATA_ARRAY_UNKNOWN = 0;
    private static int DATA_ARRAY_INTEGER = 1;
    private static int DATA_ARRAY_BOOLEAN = 2;
    private static int DATA_ARRAY_STRING = 3;
    private static int DATA_ARRAY_SHORT = 4;

    public static String serialize(Packet p) {
        StringBuilder sb = new StringBuilder();

        // Add the packet header
        sb.append(PACKET_HEADER);

        // Send the target device ID
        sb.append(serializeInt(p.getTargetId()));

        // Separate the packet data
        sb.append(PACKET_PART_SEPARATOR);

        // Send the packet id
        sb.append(serializeInt(p.getPacketId()));

        // Separate the packet data
        sb.append(PACKET_PART_SEPARATOR);

        // Serialize the integer arrays
        if(p.hasIntegers()) {
            // Add the array type
            sb.append(DATA_ARRAY_INTEGER);

            for(int entry : p.getIntegers()) {
                // Separate the array data
                sb.append(PACKET_DATA_PART_ARRAY_SEPARATOR);

                sb.append(serializeInt(entry));
            }

            // Separate the arrays
            sb.append(PACKET_DATA_PART_SEPARATOR);
        }

        // Serialize the string arrays
        if(p.hasBooleans()) {
            // Add the array type
            sb.append(DATA_ARRAY_BOOLEAN);

            for(boolean entry : p.getBooleans()) {
                // Separate the array data
                sb.append(PACKET_DATA_PART_ARRAY_SEPARATOR);

                sb.append(serializeBoolean(entry));
            }

            // Separate the arrays
            sb.append(PACKET_DATA_PART_SEPARATOR);
        }

        // Serialize the string arrays
        if(p.hasStrings()) {
            // Add the array type
            sb.append(DATA_ARRAY_STRING);

            for(String entry : p.getStrings()) {
                // Separate the array data
                sb.append(PACKET_DATA_PART_ARRAY_SEPARATOR);

                sb.append(serializeString(entry));
            }

            // Separate the arrays
            sb.append(PACKET_DATA_PART_SEPARATOR);
        }

        // Serialize the string arrays
        if(p.hasShorts()) {
            // Add the array type
            sb.append(DATA_ARRAY_SHORT);

            for(short entry : p.getShorts()) {
                // Separate the array data
                sb.append(PACKET_DATA_PART_ARRAY_SEPARATOR);

                sb.append(serializeShort(entry));
            }

            // Separate the arrays
            sb.append(PACKET_DATA_PART_SEPARATOR);
        }

        // Add the packet footer
        sb.append(PACKET_FOOTER);

        return sb.toString();
    }

    public static Packet deserialize(String str) {
        // Split the packet
        String parts[] = str.split("(?<!\\\\)" + Pattern.quote(String.valueOf(PACKET_PART_SEPARATOR)));

        // Make sure either two to three parts are available
        if(parts.length < 2 || parts.length > 3) {
            System.out.println("ERROR! INVALID PART COUNT!");
            return null;
        }

        // Get the packet target device ID
        int targetId = deserializeInt(parts[0]);

        // Get the packet ID
        int pId = deserializeInt(parts[1]);

        // Create a new packet
        Packet p = new Packet(targetId, pId);

        // Parse the data if available
        if(parts.length >= 3) {
            String dataStr = parts[2];

            String[] dataParts = dataStr.split("(?<!\\\\)" + Pattern.quote(String.valueOf(PACKET_DATA_PART_SEPARATOR)));

            // Parse each array
            for(String dataArrStr : dataParts) {
                // Make sure this array string contains anything
                if(dataArrStr.trim().length() == 0)
                    continue;

                // Split the array string
                String[] arrParts = dataArrStr.split("(?<!\\\\)" + Pattern.quote(String.valueOf(PACKET_DATA_PART_ARRAY_SEPARATOR)));

                // Get the array type
                String arrTypeStr = arrParts[0];
                int arrType = deserializeInt(arrTypeStr);

                // Parse the array
                if(arrType == DATA_ARRAY_INTEGER) {
                    List<Integer> buff = new ArrayList<Integer>();

                    for(int i = 1; i < arrParts.length; i++)
                        buff.add(deserializeInt(arrParts[i]));

                    p.setIntegers(buff);

                } else if(arrType == DATA_ARRAY_BOOLEAN) {
                    List<Boolean> buff = new ArrayList<Boolean>();

                    for(int i = 1; i < arrParts.length; i++)
                        buff.add(deserializeBoolean(arrParts[i]));

                    p.setBooleans(buff);

                } else if(arrType == DATA_ARRAY_STRING) {
                    List<String> buff = new ArrayList<String>();

                    for(int i = 1; i < arrParts.length; i++)
                        buff.add(deserializeString(arrParts[i]));

                    p.setStrings(buff);

                } else if(arrType == DATA_ARRAY_SHORT) {
                    List<Short> buff = new ArrayList<Short>();

                    for(int i = 1; i < arrParts.length; i++)
                        buff.add(deserializeShort(arrParts[i]));

                    p.setShorts(buff);
                }
            }
        }

        // Return the packet
        return p;
    }

    private static String serializeInt(int i) {
        return ("" + i);
    }

    private static int deserializeInt(String str) {
        return Integer.parseInt(String.valueOf(str));
    }

    private static String serializeShort(short s) {
        return ("" + s);
    }

    private static short deserializeShort(String str) {
        return Short.parseShort(String.valueOf(str));
    }

    private static String serializeBoolean(boolean b) {
        return serializeString(b ? "1" : "0");
    }

    private static boolean deserializeBoolean(String str) {
        return (str.equals("1"));
    }

    private static String serializeString(String str) {
        str = str.replaceAll("\\\\", "\\\\\\\\");

        for(int i = 0; i < 32; i++) {
            char c = (char) i;
            str = str.replaceAll(Pattern.quote(String.valueOf(c)), "\\\\" + String.valueOf(c));
        }

        return str;
    }

    private static String deserializeString(String str) {
        str = str.replaceAll("\\\\\\\\", "\\\\");

        for(int i = 0; i < 32; i++) {
            char c = (char) i;
            str = str.replaceAll("\\\\" + Pattern.quote(String.valueOf(c)), String.valueOf(c));
        }

        return str;
    }
}
