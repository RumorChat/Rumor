package com.timvisee.rumor.server;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.Profiler;
import com.timvisee.rumor.server.connection.ClientAccepter;
import com.timvisee.rumor.server.connection.ClientManager;

public class ServerController {

    private ClientManager conMan;
    private ClientAccepter clientAccepter;

    /**
     * Constructor
     */
    public ServerController() {
        // Profile the server start
        Profiler serverProf = new Profiler(true);

        // Starting server, show a status message
        ServerCore.getLogger().info("Starting " + Defaults.APP_NAME + " server...");

        // Set up the connection manager
        this.conMan = new ClientManager();

        // Set up the client accepter
        this.clientAccepter = new ClientAccepter(this.conMan);
        this.clientAccepter.start();

        // Stop the server start profiler
        serverProf.stop();

        // Server started, show a status message
        ServerCore.getLogger().info(Defaults.APP_NAME + " server started, took " + serverProf.getDurationString() + "!");
    }

    /**
     * Get the connection manager instance
     * @return Connector manager instance
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
