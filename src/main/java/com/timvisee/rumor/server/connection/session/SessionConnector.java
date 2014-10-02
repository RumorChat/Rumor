package com.timvisee.rumor.server.connection.session;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.protocol.packet.Packet;
import com.timvisee.rumor.protocol.Protocol;
import com.timvisee.rumor.protocol.packet.PacketFactory;
import com.timvisee.rumor.protocol.packet.PacketHandler;
import com.timvisee.rumor.protocol.packet.PacketListener;
import com.timvisee.rumor.server.CoreServer;
import com.timvisee.rumor.server.connection.Connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SessionConnector {

    /*
    /** Client's socket instance * /
     */
    private Connection con;

    /*private BufferedOutputStream bos = null;
    private OutputStreamWriter osw = null;* /

    private PrintWriter out;

    private PacketHandler ph;

    private SessionConnectorPacketListener listener;

    /**
     * Spawn a new session connector
     *
     * @param con Client connection instance.
     * /
    public SessionConnector(Connection con) {
        // Set the connection instance
        this.con = con;

        // Set up the packet handler
        this.ph = new PacketHandler();
        // TODO: Route all received data to the packet handler!

        // Create a packet listener
        this.listener = new SessionConnectorPacketListener();
        this.ph.addPacketListener(this.listener);

        // Construct a print writer
        try {
            out = new PrintWriter(s.getOutputStream(), true);
        } catch(IOException e) {
            e.printStackTrace();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(5000);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Send a test packet
                    Packet p = new Packet(0);
                    p.appendString("Welcome client!");
                    p.appendInteger(3);
                    p.appendInteger(5);
                    p.appendInteger(7);
                    p.appendString("Another string!");
                    p.appendBoolean(true);
                    p.appendBoolean(false);
                    p.appendBoolean(true);

                    send(p);
                }
            }
        });
        t.start();
    }

    public boolean send(Packet p) {
        return send(Protocol.serialize(p));
    }

    public boolean send(String data) {
        Core.getLogger().debug("[Send Packet] " + data);

        out.write(data);
        out.flush();

        // TODO: Make sure the results are good, before returning true
        return true;
    }

    /**
     * Check whether the session is connected.
     * @return True if the session is connected, false if not.
     * /
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
     * /
    public boolean disconnect() {
        // Make sure we are connected
        if(!isConnected()) {
            CoreServer.getLogger().debug("Session connector already disconnected!");
            return true;
        }

        try {
            // Send a close packet
            send(PacketFactory.createDisconnectPacket());

            // Close the socket
            this.s.close();

        } catch(IOException e) {
            // An error occurred, show an error message
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
     * /
    public Socket getSocket() {
        return this.s;
    }

    /**
     * Get the packet handler instance
     * @return Packet handler instance
     * /
    public PacketHandler getPacketHandler() {
        return this.ph;
    }

    public class SessionConnectorPacketListener extends PacketListener {
        @Override
        public void onPacketReceived(Packet p) {
            Core.getLogger().debug("[Test Listener] Received something!");
        }
    }

    /*out = new PrintWriter(link.getOutputStream(), true);

    PacketHandler ph = new PacketHandler();

    ph.addPacketListener(new PacketListener() {
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
    });* /
    */
}
