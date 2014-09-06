package com.timvisee.rumor.protocol.packet;

import java.util.ArrayList;
import java.util.List;

public class Packet {

    private int packetId = 0;

    private List<Integer> ints = new ArrayList<Integer>();
    private List<Boolean> bools = new ArrayList<Boolean>();
    private List<String> strs = new ArrayList<String>();

    public Packet(int packetId) {
        this.packetId = packetId;
    }

    public Packet(PacketType packetType) {
        this.packetId = packetType.id();
    }

    public int getPacketId() {
        return this.packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public boolean isPacketType(PacketType type) {
        return (this.packetId == type.id());
    }

    public List<Integer> getIntegers() {
        return this.ints;
    }

    public void appendInteger(Integer i) {
        ints.add(i);
    }

    public boolean hasIntegers() {
        if(ints == null)
            return false;

        return (ints.size() > 0);
    }

    public void setIntegers(List<Integer> intList) {
        this.ints = intList;
    }

    public void clearIntegers() {
        this.ints.clear();
    }

    public List<Boolean> getBooleans() {
        return this.bools;
    }

    public void appendBoolean(Boolean b) {
        bools.add(b);
    }

    public boolean hasBooleans() {
        if(bools == null)
            return false;

        return (bools.size() > 0);
    }

    public void setBooleans(List<Boolean> boolList) {
        this.bools = boolList;
    }

    public void clearBooleans() {
        this.bools.clear();
    }

    public List<String> getStrings() {
        return this.strs;
    }

    public void appendString(String str) {
        strs.add(str);
    }

    public boolean hasStrings() {
        if(strs == null)
            return false;

        return (strs.size() > 0);
    }

    public void setStrings(List<String> strList) {
        this.strs = strList;
    }

    public void clearStrings() {
        this.strs.clear();
    }
}
