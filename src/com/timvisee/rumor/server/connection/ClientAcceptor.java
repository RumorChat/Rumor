package com.timvisee.rumor.server.connection;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.server.CoreServer;
import com.timvisee.rumor.server.connection.newclient.NewClientManager;
import com.timvisee.rumor.server.connection.session.SessionManager;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientAcceptor {

    /** Session manager instance. */
    private SessionManager sessionMan;

    /** Server socket instance, which is used to accept all connections on. */
    private ServerSocket server;
    /** Connection acceptance thread instance. */
    private Thread acceptanceThread;

    private NewClientManager newClientMan;

    /**
     * Constructor
     * @param conMan Client manager instance
     */
    public ClientAcceptor(SessionManager conMan) {
        this.sessionMan = conMan;

        // Set up the new client manager
        this.newClientMan = new NewClientManager();
    }

    /**
     * Get the session manager instance
     *
     * @return The session manager instance
     */
    public SessionManager getSessionManager() {
        return this.sessionMan;
    }

    /**
     * Get the new client manager instance
     *
     * @return The new client manager instance
     */
    public NewClientManager getNewClientManager() {
        return this.newClientMan;
    }

    /**
     * Start the client acceptor
     * @return True if the acceptor has started, false otherwise. Also returns true if the acceptor was active already.
     */
    public boolean start() {
        // Make sure the acceptor isn't active already
        if(isActive())
            return true;

        // Set up a server socket and start listening for clients to connect
        try {
            this.server = new ServerSocket(Defaults.APP_SERVER_PORT);

        } catch(BindException e) {
            CoreServer.getLogger().error("Failed to start server, port already in use!");
            return false;

        } catch (IOException e) {
            // TODO: Handle errors such as 'port already in use' properly
            // Print the stack trace, return false
            e.printStackTrace();
            return false;
        }

        // Start the session acceptance thread
        this.acceptanceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        // Wait for a client to connect, show a status message
                        CoreServer.getLogger().debug("Waiting for " + Defaults.APP_NAME + " client to connect...");

                        // Accept the next session
                        Socket clientSock = server.accept();

                        CoreServer.getLogger().debug("Client connecting...");

                        // Add the connection
                        Connection con = CoreServer.instance.getConnectionManager().addConnection(clientSock);

                        // Make sure the connection is valid
                        if(con == null) {
                            CoreServer.getLogger().error("Client failed to connect from " + clientSock.getInetAddress().getHostAddress() + "!");
                            continue;
                        }

                        // Add the new client
                        // TODO: Make sure the new client was addess successfully!
                        newClientMan.addNewClient(con);

                        // TODO: Make sure the session is valid
                        // TODO: Authenticate

                        /* // Create a session for the client
                        sessionMan.createSession(client);

                        // A session has connected, show a status message
                        // TODO: Improve this status message!
                        CoreServer.getLogger().info("A " + Defaults.APP_NAME + " client has successfully connected!");*/

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
        if(this.server == null)
            return false;

        // Make sure the server socket is open
        if(this.server.isClosed())
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
            this.server.close();
            this.server = null;
        } catch(IOException e) {
            e.printStackTrace();
        }

        // TODO: Is the server closed, show a status message?

        // Check whether the acceptance thread stopped successfully, return the result
        return isActive();
    }
}