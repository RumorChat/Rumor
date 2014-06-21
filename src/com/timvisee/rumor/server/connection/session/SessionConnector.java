package com.timvisee.rumor.server.connection.session;

import com.timvisee.rumor.server.CoreServer;

import java.io.IOException;
import java.net.Socket;

public class SessionConnector {

    private Socket s;

    /**
     * Spawn a new session connector
     * @param s Client's socket instance to use for this session
     */
    public SessionConnector(Socket s) {
        this.s = s;
    }

    /**
     * Check whether the session is connected.
     * @return True if the session is connected, false if not.
     */
    public boolean isConnected() {
        // Make sure a socket instance is available
        if(this.s == null)
            return false;

        // TODO: Use proper connection detection here, methods used bellow aren't reliable!

        // Check whether the socket is closed
        return (this.s.isClosed() || this.s.isConnected());
    }

    /**
     * Disconnect the session connector from the client
     * @return True if the session connector disconnected successfully.
     * Also returns true if there wasn't an active connection.
     */
    public boolean disconnect() {
        // Make sure we are connected
        if(!isConnected()) {
            CoreServer.getLogger().debug("Session connector already disconnected!");
            return true;
        }

        // Close the socket
        try {
            this.s.close();
        } catch(IOException e) {
            CoreServer.getLogger().error("An error occurred while disconnecting a session!");
            CoreServer.getLogger().error("ERROR: " + e.getMessage());
        }

        // TODO: Properly disconnect here!
        // TODO: Make sure to disconnect the corresponding Session instance

        // Return false if we're still connected (which means disconnecting failed)
        if(isConnected())
            return false;

        // Remove the session from the session manager, return true
        CoreServer.instance.getServerController().getSessionManager().removeSession(getSocket());
        return true;
    }

    /**
     * Get the session's socket instance
     * @return Session's socket instance
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
