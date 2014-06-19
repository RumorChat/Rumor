package com.timvisee.rumor.client;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.Profiler;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Connector {

    private String host;
    private int port;

    /**
     * Constructor
     * @param host Server host
     */
    public Connector(String host) {
        this(host, Defaults.APP_SERVER_PORT);
    }

    /**
     * Constructor
     * @param host Server host
     * @param port Server port
     */
    public Connector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
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

            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");

            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");

            // Stop the connection profiler
            conProf.stop();

            // Connected successfully, show a status message
            CoreClient.getLogger().info("Connected to server, took " + conProf.getDurationString() + "!");

            // TODO: Authenticate, validate the client...

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

        } catch (IOException e) {
            // Print the error
            e.printStackTrace();

            // An error occurred, return false
            return false;

        } catch (Exception e) {
            // Print the error
            e.printStackTrace();

            // An error occurred, return false
            return false;
        }

        CoreClient.getLogger().info("Disconnected from server!");

        // Everything seems to be fine, return true
        return true;
    }
}
