package com.timvisee.rumor.server.client;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.server.CoreServer;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientAccepter {

    private ClientManager conMan;

    private ServerSocket sock;
    private Thread acceptanceThread;

    /**
     * Constructor
     * @param conMan Client manager instance
     */
    public ClientAccepter(ClientManager conMan) {
        this.conMan = conMan;
    }

    /**
     * Get the client manager instance
     */
    public ClientManager getConnectionManager() {
        return this.conMan;
    }

    /**
     * Start the client accepter
     * @return True if the accepter has started, false otherwise. Also returns true if the accepter was active already.
     */
    public boolean start() {
        // Make sure the accepter isn't active already
        if(isActive())
            return true;

        // Set up a server socket and start listening for clients to connect
        try {
            this.sock = new ServerSocket(Defaults.APP_SERVER_PORT);

        } catch(BindException e) {
            CoreServer.getLogger().error("Failed to start server, port already in use!");
            return false;

        } catch (IOException e) {
            // TODO: Handle errors such as 'port already in use' properly
            // Print the stack trace, return false
            e.printStackTrace();
            return false;
        }

        // Start the client acceptance thread
        this.acceptanceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        // Wait for a client to connect, show a status message
                        CoreServer.getLogger().debug("Waiting for " + Defaults.APP_NAME + " client to connect...");

                        // Accept the next client
                        Socket client = null;
                        client = sock.accept();

                        // TODO: Make sure the client is valid

                        // Create a client instance, and register the client
                        ClientConnector c = new ClientConnector(client);
                        conMan.registerClient(c);

                        // A client has connected, show a status message
                        // TODO: Improve this status message!
                        CoreServer.getLogger().info("A " + Defaults.APP_NAME + " client has successfully connected!");

                    } catch (IOException e) {
                        e.printStackTrace();

                        // Sleep the thread for a little while to prevent infinite error loops
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        this.acceptanceThread.start();

        // Everything seems to be fine, return true
        return true;
    }

    /**
     * Check whether the client acceptance thread is active.
     * @return True if the acceptance thread is active, false otherwise.
     */
    public boolean isActive() {
        // Make sure a server socket is created
        if(this.sock == null)
            return false;

        // Make sure the server socket is open
        if(this.sock.isClosed())
            return false;

        // Make sure an acceptance thread is created
        if(this.acceptanceThread == null)
            return false;

        // Make sure the acceptance thread is active
        return this.acceptanceThread.isAlive();
    }

    /**
     * Stop accepting clients
     * @return True if
     */
    public boolean stop() {
        // Stop the acceptance thread
        try {
            this.acceptanceThread.stop();
            this.acceptanceThread = null;
        } catch(Exception e) {
            CoreServer.getLogger().debug("Suppressed an exception which occurred while stopping the client acceptance thread!");
            CoreServer.getLogger().debug("ERROR: " + e.getMessage());
        }

        // Close the server socket
        // TODO: Can we close this socket while clients are connected?
        try {
            this.sock.close();
            this.sock = null;
        } catch(IOException e) {
            e.printStackTrace();
        }

        // TODO: Is the server closed, show a status message?

        // Check whether the acceptance thread stopped successfully, return the result
        return isActive();
    }
}