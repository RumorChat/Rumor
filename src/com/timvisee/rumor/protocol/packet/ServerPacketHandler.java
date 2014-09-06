package com.timvisee.rumor.protocol.packet;

import com.timvisee.rumor.server.connection.Connection;

public class ServerPacketHandler extends PacketHandler {

    private Connection con;

    public ServerPacketHandler(Connection con) {
        // Set the connection instance
        this.con = con;
    }

    /**
     * Get the connection instance.
     *
     * @return Connection instance.
     */
    public Connection getConnection() {
        return this.con;
    }

    /**
     * Send a packet.
     *
     * @param p Packet to send.
     */
    public void send(Packet p) {
        PacketHandler.send(this.con.getOutputStreamWriter(), p);
    }
}
