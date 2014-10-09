package com.timvisee.rumor.protocol.packet.exception;

public class MalformedPacketException extends Exception {

    private String data;

    public MalformedPacketException(String packetData) {
        this.data = packetData;
    }

    public String getPacketData() {
        return this.data;
    }
}
