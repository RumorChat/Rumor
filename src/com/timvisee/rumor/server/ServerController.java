package com.timvisee.rumor.server;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.server.connection.ClientAcceptor;
import com.timvisee.rumor.server.connection.session.SessionManager;
import com.timvisee.rumor.util.Profiler;

public class ServerController {

    /** Session manager instance. */
    private SessionManager sessionManager;
    /** Client acceptor instance */
    private ClientAcceptor clientAcceptor;

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

        // Set up the session manager
        this.sessionManager = new SessionManager();

        // Set up the session acceptor and start accepting clients
        this.clientAcceptor = new ClientAcceptor(this.sessionManager);
        if(!this.clientAcceptor.start()) {
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
        // Make sure the session acceptor is available
        if(this.clientAcceptor == null)
            return false;

        // Check whether the session acceptor is active,
        return this.clientAcceptor.isActive();
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

        // Stop the session acceptor
        this.clientAcceptor.stop();

        // TODO: Disconnect all connected clients
        // TODO: Stop all server (socket) listener threads

        // Return true if the server is successfully stopped
        return !isStarted();
    }

    /**
     * Get the session manager instance
     * @return Session manager instance
     */
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    /**
     * Get the session acceptor instance
     * @return Client acceptor instance
     */
    public ClientAcceptor getClientAcceptor() {
        return this.clientAcceptor;
    }
}
