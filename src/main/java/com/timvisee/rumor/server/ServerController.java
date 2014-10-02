package com.timvisee.rumor.server;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.server.connection.acceptor.ClientAcceptor;
import com.timvisee.rumor.server.connection.ConnectionManager;
import com.timvisee.rumor.server.connection.authenticator.ClientAuthenticator;
import com.timvisee.rumor.server.connection.session.SessionManager;
import com.timvisee.rumor.util.Profiler;

public class ServerController {

    /** Connection manager instance */
    public ConnectionManager conMan;
    /** Client acceptor instance */
    private ClientAcceptor clientAcceptor;
    /** Client authenticator instance */
    private ClientAuthenticator clientAuth;
    /** Session manager instance. */
    private SessionManager sessionManager;

    /**
     * Constructor.
     * Server doesn't start automatically while constructing.
     */
    public ServerController() {
        this(false);
    }

    /**
     * Constructor.
     *
     * @param start True to start the server immediately, false otherwise.
     */
    public ServerController(boolean start) {
        // Set up the connection manager
        this.conMan = new ConnectionManager();

        // Start the server
        if(start)
            start();
    }

    /**
     * Start the server.
     *
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

        // Set up the client authenticator
        this.clientAuth = new ClientAuthenticator();

        // Set up the session manager
        this.sessionManager = new SessionManager();

        // Set up the session acceptor and start accepting clients
        this.clientAcceptor = new ClientAcceptor();
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
     * Check whether the server is started.
     *
     * @return True if the server is started, false otherwise.
     */
    public boolean isStarted() {
        // TODO: Improve this method!

        // Make sure the session acceptor is available
        if(this.clientAcceptor == null)
            return false;

        // Check whether the session acceptor is active,
        return this.clientAcceptor.isActive();
    }

    /**
     * Stop the server.
     *
     * @param wait True to wait until all threads are stopped before returning.
     *
     * @return True if the server was stopped, false if failed. Also returns true if.
     */
    public boolean stop(boolean wait) {
        // Make sure the server is started
        if(!isStarted()) {
            CoreServer.getLogger().debug("Server already stopped!");
            return true;
        }

        // Disconnect all clients
        this.conMan.disconnectAll(DisconnectReason.SERVER_SHUTDOWN);

        // Stop the client acceptor
        this.clientAcceptor.stop(wait);

        // Stop the client authenticator
        this.clientAuth.stop(wait);

        // TODO: Disconnect all connected clients
        // TODO: Stop all server (socket) listener threads

        // Return true if the server is successfully stopped
        return !isStarted();
    }

    /**
     * Get the session acceptor instance.
     *
     * @return Client acceptor instance.
     */
    public ClientAcceptor getClientAcceptor() {
        return this.clientAcceptor;
    }

    /**
     * Get the connection manager instance.
     *
     * @return Connection manager instance.
     */
    public ConnectionManager getConnectionManager() {
        return this.conMan;
    }

    /**
     * Get the client authenticator instance.
     *
     * @return The client authenticator instance.
     */
    public ClientAuthenticator getClientAuthenticator() {
        return this.clientAuth;
    }

    /**
     * Get the session manager instance.
     *
     * @return Session manager instance.
     */
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }
}
