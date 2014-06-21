package com.timvisee.rumor.server;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.Profiler;
import com.timvisee.rumor.server.connection.client.ClientAccepter;
import com.timvisee.rumor.server.connection.client.ClientManager;

public class ServerController {

    private ClientManager conMan;
    private ClientAccepter clientAccepter;

    /**
     * Constructor.
     * Server doesn't start automatically while constructing.
     */
    public ServerController() {
        this(false);
    }

    /**
     * Constructor
     * @param start True to start the server immediately, false otherwise.
     */
    public ServerController(boolean start) {
        // Start the server
        if(start)
            start();
    }

    /**
     * Start the server
     * @return True if the server was started successfully, false otherwise.
     * Also returns true if the server was already running.
     */
    public boolean start() {
        // Make sure the server isn't started already
        if(isStarted()) {
            // Show a status message, return true since the server is running already
            CoreServer.getLogger().debug("Cancelled server start, server already running!");
            return true;
        }

        // Profile the server start
        Profiler serverProf = new Profiler(true);

        // Starting server, show a status message
        CoreServer.getLogger().info("Starting " + Defaults.APP_NAME + " server...");

        // Set up the client manager
        this.conMan = new ClientManager();

        // Set up the client accepter and start accepting clients
        this.clientAccepter = new ClientAccepter(this.conMan);
        if(!this.clientAccepter.start()) {
            // Stop the server start profiler
            serverProf.stop();

            // Server started, show a status message, and return false due to the error that occurred
            CoreServer.getLogger().error(Defaults.APP_NAME + " failed to start after " + serverProf.getDurationString() + "!");
            return false;
        }

        // Stop the server start profiler
        serverProf.stop();

        // Server started, show a status message, and return true if the server started successfully
        CoreServer.getLogger().info(Defaults.APP_NAME + " server started, took " + serverProf.getDurationString() + "!");
        return isStarted();
    }

    /**
     * Check whether the server is started
     * @return True if the server is started, false otherwise.
     */
    public boolean isStarted() {
        // Make sure the client accepter is available
        if(this.clientAccepter == null)
            return false;

        // Check whether the client accepter is active,
        return this.clientAccepter.isActive();
    }

    /**
     * Stop the server
     * @return True if the server was stopped, false if failed. Also returns true if
     */
    public boolean stop() {
        // Make sure the server is started
        if(!isStarted()) {
            CoreServer.getLogger().debug("Didn't stop server, server already stopped!");
            return true;
        }

        // Stop the client accepter
        this.clientAccepter.stop();

        // TODO: Disconnect all connected clients
        // TODO: Stop all server (socket) listener threads

        // Return true if the server is successfully stopped
        return !isStarted();
    }

    /**
     * Get the client manager instance
     * @return Connection manager instance
     */
    public ClientManager getConnectionManager() {
        return this.conMan;
    }

    /**
     * Get the client accepter instance
     * @return Client accepter instance
     */
    public ClientAccepter getClientAccepter() {
        return this.clientAccepter;
    }
}
