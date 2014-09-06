package com.timvisee.rumor.protocol.packet;

public enum PacketType {

    UNKNOWN(0),

    HANDSHAKE(10),
    HEARTBEAT(11),

    CLIENT_INFO(20),
    SERVER_INFO(21),

    AUTH(30),
    AUTH_RESULT(31),

    DISCONNECT(100);

    private final int id;

    /**
     * Constructor
     *
     * @param id Packet type ID
     */
    PacketType(int id) {
        this.id = id;
    }

    /**
     * Get the packet type ID
     *
     * @return Packet type ID
     */
    public int id() {
        return this.id;
    }
}
