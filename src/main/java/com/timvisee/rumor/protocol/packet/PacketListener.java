package com.timvisee.rumor.protocol.packet;

public abstract class PacketListener {

    /**
     * Called once a packet has been received.
     *
     * @param p Instance of the packet that has been received.
     */
    public abstract void onPacketReceived(Packet p);

    /**
     * Called once a malformed packet has been received.
     *
     * @param data The malformed data as a string.
     */
    public abstract void onMalformedPacketReceived(String data);
}
