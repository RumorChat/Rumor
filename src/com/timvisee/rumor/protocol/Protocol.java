package com.timvisee.rumor.protocol;

import com.timvisee.rumor.protocol.packet.Packet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
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
        // Create a packet object
        JSONObject packet = new JSONObject();

        // Set the packet ID
        packet.put("packet_id", p.getPacketId());

        // Create a data instance to hold all packet data
        JSONObject packetData = new JSONObject();

        // Serialize the integer array
        if(p.hasIntegers())
            packetData.put("ints", p.getIntegers());

        // Serialize the boolean array
        if(p.hasBooleans())
            packetData.put("bools", p.getBooleans());

        // Serialize the string array
        if(p.hasStrings())
            packetData.put("strs", p.getStrings());

        // Add the serialized data to the main packet
        packet.put("data", packetData);

        // Return the packet object
        return packet.toJSONString() + "\n";
    }

    public static Packet deserialize(String str) {
        // Instantiate the parser
        JSONParser parser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) parser.parse(str);

            // Parse the packet ID
            int packet_id = (int) ((long) ((Long) obj.get("packet_id")));

            // Create a new packet
            Packet p = new Packet(packet_id);

            // Parse the packet data
            JSONObject objData = (JSONObject) obj.get("data");

            // Parse integers
            if(objData.containsKey("ints")) {
                JSONArray intsArr = (JSONArray) objData.get("ints");
                List<Long> longs = Arrays.asList((Long[]) intsArr.toArray(new Long[]{}));

                List<Integer> ints = new ArrayList<Integer>();
                for(long l : longs)
                    ints.add((int) l);

                p.setIntegers(ints);
            }

            // Parse booleans
            if(objData.containsKey("bools")) {
                JSONArray boolsArr = (JSONArray) objData.get("bools");
                List<Boolean> bools = Arrays.asList((Boolean[]) boolsArr.toArray(new Boolean[]{}));
                p.setBooleans(bools);
            }

            // Parse strings
            if(objData.containsKey("strs")) {
                JSONArray strsArr = (JSONArray) objData.get("strs");
                List<String> strs = Arrays.asList((String[]) strsArr.toArray(new String[]{}));
                p.setStrings(strs);
            }

            // Return the packet
            return p;

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
