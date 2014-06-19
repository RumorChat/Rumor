package com.timvisee.rumor.server;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.Profiler;
import com.timvisee.rumor.server.connection.ConnectionHandler;
import com.timvisee.rumor.server.connection.ConnectionManager;

public class ServerController {

    private ConnectionManager conMan;
    private ConnectionHandler conList;

    // TODO: Should we do this differently?
    private Thread conListThread;

    /**
     * Constructor
     */
    public ServerController() {
        // Profile the server start
        Profiler serverProf = new Profiler(true);

        // Starting server, show a status message
        ServerCore.getLogger().info("Starting " + Defaults.APP_NAME + " server...");

        // Set up the connection manager
        this.conMan = new ConnectionManager();

        // Set up the connection listener
        // TODO: Should we do this differently?
        conList = new ConnectionHandler(this.conMan);
        this.conListThread = new Thread(conList);
        this.conListThread.start();

        // Stop the server start profiler
        serverProf.stop();

        // Server started, show a status message
        ServerCore.getLogger().info(Defaults.APP_NAME + " server started, took " + serverProf.getDurationString() + "!");
    }

    /**
     * Get the connection manager instance
     * @return Connector manager instance
     */
    public ConnectionManager getConnectionManager() {
        return this.conMan;
    }

    /**
     * Get the connection listener instance
     * @return Connector listener instance
     */
    public ConnectionHandler getConnectionListener() {
        return this.conList;
    }
}
