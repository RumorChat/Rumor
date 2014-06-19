package com.timvisee.rumor.server.connection;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.server.ServerCore;

import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private ConnectionManager conMan;

    /**
     * Constructor
     * @param conMan Connector manager instance
     */
    public ConnectionHandler(ConnectionManager conMan) {
        this.conMan = conMan;
    }

    /**
     * Get the connection manager instance
     */
    public ConnectionManager getConnectionManager() {
        return this.conMan;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSock = new ServerSocket(Defaults.APP_SERVER_PORT);

            while(true) {
                // Wait for a client to connect, show a status message
                ServerCore.getLogger().debug("Waiting for " + Defaults.APP_NAME + " client to connect...");

                // Accept the next connection
                Socket client = serverSock.accept();

                // TODO: Make sure the connection is valid

                // Create a connection instance, and register the connection
                Connection c = new Connection(client);
                conMan.registerConnection(c);

                // A client has connected, show a status message
                // TODO: Improve this status message!
                ServerCore.getLogger().info("A " + Defaults.APP_NAME + " client has connected successfully (Accepted " + conMan.getAcceptedCount() + " connections)!");
            }

        } catch (Exception e) {
            // TODO: Handle errors properly, such as 'port already in use' exceptions
            e.printStackTrace();
        }
    }
}