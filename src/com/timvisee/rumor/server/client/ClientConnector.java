package com.timvisee.rumor.server.client;

import java.net.Socket;

public class ClientConnector {

    private Socket s;

    /**
     * Constructor
     * @param s Client's socket instance
     */
    public ClientConnector(Socket s) {
        this.s = s;
    }

    /**
     * Get the client's socket instance
     * @return Client's socket instance
     */
    public Socket getSocket() {
        return this.s;
    }

    /*out = new PrintWriter(link.getOutputStream(), true);

    PacketHandler ph = new PacketHandler();

    ph.registerListener(new PacketListener() {
        @Override
        public void onPacketReceived(Packet p) {
            packetsReceived++;

            if(p.getPacketId() == PacketType.HEARTHBEAT.getId())
                CoreServer.instance.packetLog.log("Received packet! (HEARTBEAT)");
            else
                CoreServer.instance.packetLog.log("Received packet! (ID: " + p.getPacketId() + ")" +
                        " (Ints: " + p.getIntegers().size() + ", Bools: " + p.getBooleans().size() +
                        ", Strings: " + p.getStrings().size() + ")");

            if(p.getPacketId() == PacketType.MOTOR_STATUS.getId()) {
                MainFrame.r.setTopLeftLegAngle(p.getIntegers().get(0));
                MainFrame.r.setTopRightLegAngle(p.getIntegers().get(1));
                MainFrame.r.setBottomLeftLegAngle(p.getIntegers().get(2));
                MainFrame.r.setBottomRightLegAngle(p.getIntegers().get(3));
            }
        }
    });*/
}
