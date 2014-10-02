package com.timvisee.rumor.client.connection.server;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.protocol.packet.Packet;
import com.timvisee.rumor.protocol.packet.PacketFactory;
import com.timvisee.rumor.protocol.packet.PacketHandler;
import com.timvisee.rumor.protocol.packet.PacketListener;
import com.timvisee.rumor.protocol.packet.exception.MalformedPacketException;
import com.timvisee.rumor.util.Profiler;
import com.timvisee.rumor.client.CoreClient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnector {

    /** Server host to connect to */
    private String host;
    /** Server port to connect to */
    private int port;

    /** Variable tracking whether there's an active connection or not */
    private boolean connected = false;

    /**
     * Constructor
     * @param host Server host
     */
    public ServerConnector(String host) {
        this(host, Defaults.APP_SERVER_PORT);
    }

    /**
     * Constructor
     * @param host Server host
     * @param port Server port
     */
    public ServerConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connect to the server
     * @return True if a connection was made successfully, false otherwise.
     * Also returns true if there already was an active connection.
     */
    public boolean connect() {
        // Make sure we aren't connected already
        if(isConnected()) {
            CoreClient.getLogger().debug("Cancelled server connection, already connected!");
            return true;
        }

        // Profile the connection process
        Profiler conProf = new Profiler(true);

        // Setting up a connection to the server, show a status message
        CoreClient.getLogger().info("Connecting to " + this.host + ":" + this.port + "...");

        StringBuffer instr = new StringBuffer();

        try {
            // TODO: Validate the host
            InetAddress address = InetAddress.getByName(this.host);
            // TODO: Validate the port
            final Socket connection = new Socket(address, this.port);

            this.connected = true;

            final BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");

            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");

            // Stop the connection profiler
            conProf.stop();

            // Connected successfully, show a status message
            CoreClient.getLogger().info("Connected to server, took " + conProf.getDurationString() + "!");

            // TODO: Authenticate, validate the session...

            /*Packet p = new Packet(1, 2);
            p.appendString("test" + ((char) 2));
            p.appendInteger(2);
            p.appendInteger(189753);
            p.appendBoolean(true);
            p.appendBoolean(false);
            p.appendString("te\\s\\\\ta");
            p.appendShort((short) 2);
            p.appendShort((short) 4672);
            PacketHandler.send(osw, p);*/

            osw.flush();

            final PacketHandler ph = new PacketHandler();
            ph.addPacketListener(new PacketListener() {
                @Override
                public void onPacketReceived(Packet p) {
                    //CoreClient.getLogger().info(p.getStrings().get(0));
                }

                @Override
                public void onMalformedPacketReceived(String data) { }
            });

            // Send a handshake packet
            PacketHandler.send(osw, PacketFactory.createHandshakePacket());
            PacketHandler.send(osw, PacketFactory.createClientInfoPacket());
            PacketHandler.send(osw, PacketFactory.createAuthenticationPacket("SomeUser"));
            Core.getLogger().debug("Send handshake packet to server!");

            this.connected = false;

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    CoreClient.getLogger().debug("Started packet listener thread.");
                    while(true) {
                        // TODO: Wait for packets, instead of infinite loops!

                        try {
                            while(bis.available() > 0)
                                ph.received(bis.read());

                        } catch(MalformedPacketException ex) {
                            ex.printStackTrace();

                        } catch(IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            t.start();

            //Thread.sleep(5000);

            //connection.close();
            //System.out.println(instr);

        } catch(Exception e) {
            // Show a status message
            CoreClient.getLogger().error("An error occurred while connecting to the server!");
            CoreClient.getLogger().error("ERROR: " + e.getMessage());

            this.connected = false;

            // An error occurred, return false
            return false;
        }

        // Return true if a connection was made successfully
        return isConnected();
    }

    /**
     * Check whether there is an active connection to the server.
     * @return True if there's an active connection to the server, false otherwise.
     */
    public boolean isConnected() {
        return this.connected;
    }

    /**
     * Disconnect from the server
     * @return True if
     */
    public boolean disconnect() {
        // Show a status message
        CoreClient.getLogger().info("Disconnecting from server...");

        // TODO: Disconnect logic here...

        // Make sure we're disconnected, if not, show an error message
        if(isConnected()) {
            // Show a status message
            CoreClient.getLogger().error("Failed to disconnect from the server!");
            return false;
        }

        // Show a status message
        CoreClient.getLogger().info("Disconnected from server!");
        return false;
    }
}
