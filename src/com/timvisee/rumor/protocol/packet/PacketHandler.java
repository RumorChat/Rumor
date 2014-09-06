package com.timvisee.rumor.protocol.packet;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.protocol.Protocol;
import com.timvisee.rumor.server.connection.Connection;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class PacketHandler {

    private List<PacketListener> listeners = new ArrayList<PacketListener>();

    private boolean escapeNextChar = false;
    private boolean packetReceived = false;
    private StringBuilder buff = new StringBuilder();

    private Packet lastReceived = null;

    public PacketHandler() { }

    public static void send(OutputStreamWriter osw, Packet p) {
        String data = Protocol.serialize(p);

        Core.getLogger().debug("[SEND PACKET] " + Protocol.serialize(p));

        try {
            osw.write(data);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Packet received(int c) {
        /*if(c == ((char) '\\')) {
            escapeNextChar = true;
            buff.append((char) c);
            return;
        }*/

        if(!escapeNextChar) {
            if(c == '\n') {
                packetReceived = true;
                escapeNextChar = false;
            }
        }

        if(packetReceived) {
            // Parse the received data as a string
            String data = buff.toString();

            // Show a debug message containing the received data
            Core.getLogger().debug("[Received Packet] " + data);

            this.lastReceived = Protocol.deserialize(data);
            buff.setLength(0);
            escapeNextChar = false;
            packetReceived = false;

            for(PacketListener pl : this.listeners)
                pl.onPacketReceived(this.lastReceived);

            return this.lastReceived;
        }

        buff.append((char) c);
        escapeNextChar = false;
        return null;
    }

    /**
     * Get the last successfully received packet.
     *
     * @return Last successfully received packet.
     */
    public Packet getLastReceived() {
        return this.lastReceived;
    }

    /**
     * Add and register a packet listener.
     *
     * @param pl Packet listener to add
     *
     * @deprecated Will be removed soon!
     */
    public void addPacketListener(PacketListener pl) {
        this.listeners.add(pl);
    }

    /**
     * Remove and unregister a packet listener
     *
     * @param pl Packet listener to remove
     *
     * @deprecated Will be removed soon!
     */
    public void removePacketListener(PacketListener pl) {
        this.listeners.remove(pl);
    }
}
