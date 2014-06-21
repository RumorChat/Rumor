package com.timvisee.rumor.client.connection.server;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.util.Profiler;
import com.timvisee.rumor.client.CoreClient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnector {

    private String host;
    private int port;

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
            Socket connection = new Socket(address, this.port);

            this.connected = true;

            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
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

            Thread.sleep(2500);

            connection.close();
            //System.out.println(instr);

            this.connected = false;

        } catch(Exception e) {
            // Show a status message
            CoreClient.getLogger().error("An error occurred while connecting to the server!");
            CoreClient.getLogger().error("ERROR: " + e.getMessage());

            this.connected = false;

            // An error occurred, return false
            return false;
        }

        CoreClient.getLogger().info("Disconnected from server!");

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
}
