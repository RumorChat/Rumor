package com.timvisee.rumor.server.connection.acceptor;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.server.CoreServer;
import com.timvisee.rumor.server.connection.Connection;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientAcceptor {

    /** Server socket instance, which is used to accept all connections on. */
    private ServerSocket server;

    /** Connection acceptor thread instance. */
    private Thread acceptorThread;

    /** The time in milliseconds the server socket listener times out */
    private final int SERVER_SOCKET_ACCEPTOR_TIMEOUT = 50;

    /**
     * Constructor
     */
    public ClientAcceptor() { }

    /**
     * Start the client acceptor
     *
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
        this.acceptorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Client acceptor thread starting, show a status message!
                Core.getLogger().debug("Client acceptor thread started!");

                // Start the client acceptance
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        // Wait for a client to connect, show a status message
                        CoreServer.getLogger().debug("Waiting for " + Defaults.APP_NAME + " client to connect...");

                        // Create a variable to store the client socket instance in
                        Socket clientSock = null;

                        // Wait for a client to connect, unless the thread is interrupted
                        while(!Thread.currentThread().isInterrupted() && clientSock == null) {
                            try {
                                // Set the server socket timeout, which achieves non-blocking code
                                server.setSoTimeout(SERVER_SOCKET_ACCEPTOR_TIMEOUT);

                                // Wait for a client to connect
                                clientSock = server.accept();
                            } catch(SocketTimeoutException ex) {
                                // The server socket listener timed out, used to get non-blocking code
                            }
                        }

                        // Make sure the thread isn't interrupted and that a valid socket was accepted
                        if(Thread.currentThread().isInterrupted() || clientSock == null)
                            continue;

                        // Show a status message
                        CoreServer.getLogger().debug("Client connecting from " + clientSock.getInetAddress().getHostAddress() + "...");

                        // Add the socket as a connection
                        Connection con = CoreServer.instance.getServerController().getConnectionManager().addConnection(clientSock);

                        // Make sure the connection is valid
                        // TODO: Better client connection validation!
                        if(con == null) {
                            CoreServer.getLogger().error("Client failed to connect from " + clientSock.getInetAddress().getHostAddress() + "!");
                            continue;
                        }

                        // Create a new authenticating client
                        CoreServer.instance.getServerController().getClientAuthenticator().createAuthClient(con);

                    } catch (IOException e) {
                        // Print the stack trace
                        e.printStackTrace();

                        // TODO: Allow a maximum number of errors within a specified time frame, before stopping the thread!

                        // Sleep for a little, to prevent infinite error loops
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                // Acceptance thread stopped, show a status message
                Core.getLogger().debug("Client acceptor thread stopped!");
            }
        });
        this.acceptorThread.start();

        // Everything seems to be fine, return true
        return true;
    }

    /**
     * Check whether the client acceptance thread is active.
     *
     * @return True if the acceptance thread is active, false otherwise.
     */
    public boolean isActive() {
        // TODO: Rewrite this method, due to thread interruptions

        // Make sure a server socket is created
        if(this.server == null)
            return false;

        // Make sure the server socket is open
        if(this.server.isClosed())
            return false;

        // Make sure the acceptance thread is active
        return this.acceptorThread.isAlive();
    }

    /**
     * Stop accepting clients.
     *
     * @param wait True to wait until all threads are fully stopped before returning.
     *
     * @return True if the client acceptor was stopped successfully, false otherwise.
     */
    public boolean stop(boolean wait) {
        // TODO: Rewrite this method, due to thread interruptions

        // Stopping client acceptor, show a status message
        Core.getLogger().debug("Stopping client acceptor...");

        try {
            // Interrupt the thread
            this.acceptorThread.interrupt();

            // Wait for the thread to stop
            while(wait && this.acceptorThread.isAlive()) {
                try {
                    Thread.sleep(1);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch(Exception e) {
            CoreServer.getLogger().error("An error occurred while stopping the client acceptance thread!");
            CoreServer.getLogger().error("ERROR: " + e.getMessage());
        }

        // Close the server socket
        // TODO: Can we close this server socket while clients are connected?
        try {
            this.server.close();
            this.server = null;
        } catch(IOException e) {
            e.printStackTrace();
        }

        // TODO: Is the server closed, show a status message?
        // TODO: Make sure everything stopped successfully!

        // Stopping client acceptor, show a status message
        Core.getLogger().debug("Client acceptor stopped!");

        // The client acceptor was stopped successfully, return the result
        return true;
    }
}