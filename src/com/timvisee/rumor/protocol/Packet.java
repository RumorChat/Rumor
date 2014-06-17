package com.timvisee.rumor.protocol;

import java.util.ArrayList;
import java.util.List;

public class Packet {

    private int targetId = 0;
    private int packetId = 0;

    private List<Integer> intList = new ArrayList<Integer>();
    private List<Boolean> boolList = new ArrayList<Boolean>();
    private List<String> strList = new ArrayList<String>();
    private List<Short> shortList = new ArrayList<Short>();

    public Packet(int targetId, int packetId) {
        this.targetId = targetId;
        this.packetId = packetId;
    }

    public int getTargetId() {
        return this.targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getPacketId() {
        return this.packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public List<Integer> getIntegers() {
        return this.intList;
    }

    public void appendInteger(Integer i) {
        intList.add(i);
    }

    public boolean hasIntegers() {
        if(intList == null)
            return false;

        return (intList.size() > 0);
    }

    public void setIntegers(List<Integer> intList) {
        this.intList = intList;
    }

    public void clearIntegers() {
        this.intList.clear();
    }

    public List<Boolean> getBooleans() {
        return this.boolList;
    }

    public void appendBoolean(Boolean b) {
        boolList.add(b);
    }

    public boolean hasBooleans() {
        if(boolList == null)
            return false;

        return (boolList.size() > 0);
    }

    public void setBooleans(List<Boolean> boolList) {
        this.boolList = boolList;
    }

    public void clearBooleans() {
        this.boolList.clear();
    }

    public List<String> getStrings() {
        return this.strList;
    }

    public void appendString(String str) {
        strList.add(str);
    }

    public boolean hasStrings() {
        if(strList == null)
            return false;

        return (strList.size() > 0);
    }

    public void setStrings(List<String> strList) {
        this.strList = strList;
    }

    public void clearStrings() {
        this.strList.clear();
    }

    public List<Short> getShorts() {
        return this.shortList;
    }

    public void appendShort(Short s) {
        shortList.add(s);
    }

    public boolean hasShorts() {
        if(shortList == null)
            return false;

        return (shortList.size() > 0);
    }

    public void setShorts(List<Short> shortList) {
        this.shortList = shortList;
    }

    public void clearShorts() {
        this.shortList.clear();
    }
}
