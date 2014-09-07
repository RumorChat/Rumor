package com.timvisee.rumor.server.connection;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.protocol.packet.Packet;
import com.timvisee.rumor.protocol.packet.PacketFactory;
import com.timvisee.rumor.protocol.packet.PacketListener;
import com.timvisee.rumor.protocol.packet.ServerPacketHandler;
import com.timvisee.rumor.server.CoreServer;
import com.timvisee.rumor.server.DisconnectReason;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection {

    /** Socket instance */
    private Socket s;

    /** Buffered input stream instance */
    private BufferedInputStream bis;
    /** Input stream reader instance */
    private InputStreamReader isr;

    /** Buffered output stream instance */
    private BufferedOutputStream bos;
    /** Output stream writer instance */
    private OutputStreamWriter osw;

    /** Packet handler */
    private ServerPacketHandler ph;

    /** Socket thread */
    private Thread sockThread;

    private List<PacketListener> listeners = new ArrayList<PacketListener>();

    private boolean disconnected = false;

    /**
     * Constructor
     *
     * @param sock Socket of the client
     */
    public Connection(Socket sock) {
        // Set the socket instance
        this.s = sock;

        // Get the input and output stream instances
        try {
            bis = new BufferedInputStream(this.s.getInputStream());
            isr = new InputStreamReader(bis, "US-ASCII");

            bos = new BufferedOutputStream(this.s.getOutputStream());
            osw = new OutputStreamWriter(bos, "US-ASCII");

        } catch(IOException e) {
            e.printStackTrace();
        }

        // Set up the packet handler
        this.ph = new ServerPacketHandler(this);

        // Create and start a socket thread
        this.sockThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        // Store any received packet
                        Packet p = null;

                        // Read new data from the stream, and parse it as a packet when possible
                        try {
                            while(getBufferedInputStream().available() > 0 && p == null)
                                p = getPacketHandler().received(getBufferedInputStream().read());

                        } catch(IOException e) {
                            e.printStackTrace();
                        }

                        // Make sure the packet isn't null
                        if(p == null)
                            continue;

                        // Call all packet listeners
                        for(PacketListener pl : getPacketListeners())
                            pl.onPacketReceived(p);
                    }

                } catch(Exception ex) {
                    // TODO: Max 10 errors in a specified time, before auto cancelling the thread!
                    ex.printStackTrace();
                }
            }
        });
        this.sockThread.start();
    }

    /**
     * Get the socket instance of this connection.
     *
     * @return Socket instance
     */
    public Socket getSocket() {
        return this.s;
    }

    /**
     * Get the buffered input stream instance.
     *
     * @return Buffered input stream instance
     */
    public BufferedInputStream getBufferedInputStream() {
        return this.bis;
    }

    /**
     * Get the input stream reader instance
     *
     * @return Input stream reader instance
     */
    public InputStreamReader getInputStreamReader() {
        return this.isr;
    }

    /**
     * Get the buffered output stream instance
     *
     * @return Buffered output stream instance
     */
    public BufferedOutputStream getBufferedOutputStream() {
        return this.bos;
    }

    /**
     * Get the output stream writer instance
     *
     * @return Output stream writer instance
     */
    public OutputStreamWriter getOutputStreamWriter() {
        return this.osw;
    }

    /**
     * Get the packet handler instance.
     *
     * @return Packet handler instance.
     */
    public ServerPacketHandler getPacketHandler() {
        return this.ph;
    }

    /**
     * Get the socket thread instance.
     *
     * @return Socket thread instance.
     */
    public Thread getSocketThread() {
        return this.sockThread;
    }

    public boolean isConnected() {
        // TODO: Put proper code here!
        return !this.disconnected;
    }

    /**
     * Properly disconnect the connection.
     *
     * @param reason The reason of disconnection.
     *
     * @return True on success, false on failure. False will be returned if the connection was already disconnected.
     */
    public boolean disconnect(DisconnectReason reason) {
        // Make sure the connection isn't disconnected already
        if(disconnected)
            return false;

        // Send a disconnect packet
        sendPacket(PacketFactory.createDisconnectPacket(reason));
        disconnected = true;

        // Remove the connection from the connection manager
        CoreServer.instance.getServerController().getConnectionManager().disconnect(this, reason);

        // Show a status message, return the result
        Core.getLogger().info("Client disconnected!");
        return true;
    }

    /**
     * Add a packet listener.
     *
     * @param pl Packet listener to add.
     *
     * @return True on success, false on failure.
     */
    public boolean addPacketListener(PacketListener pl) {
        return this.listeners.add(pl);
    }

    /**
     * Remove a packet listener.
     *
     * @param pl Packet listener to remove.
     *
     * @return True if any item was removed, false otherwise.
     */
    public boolean removePacketListener(PacketListener pl) {
        return this.listeners.remove(pl);
    }

    /**
     * Get the list of registered packet listeners.
     *
     * @return List of packet listeners.
     */
    public List<PacketListener> getPacketListeners() {
        return this.listeners;
    }

    /**
     * Send a packet.
     *
     * @param p Packet to send.
     *
     * @return True on success, false on failure.
     */
    public boolean sendPacket(Packet p) {
        this.ph.send(p);

        // TODO: Return some status
        return true;
    }
}
