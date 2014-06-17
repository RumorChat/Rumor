package com.timvisee.rumor.client;

import com.timvisee.rumor.Profiler;
import com.timvisee.rumor.log.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientCore {

     private static Logger log = new Logger(true, RumorClient.APP_DEBUG);

     /**
     * Constructor
     */
    public ClientCore() {
        // Initialize
        init();
    }

    /**
     * Initialize
     * @return True on success, false on failure.
     */
    private boolean init() {
        // Profile the initialization
        Profiler initProf = new Profiler(true);

        // Show an initialization message
        ClientCore.getLogger().info("Initializing " + RumorClient.APP_CLIENT_NAME + " v" + RumorClient.APP_VERSION_NAME + " (" + RumorClient.APP_VERSION_CODE + ")...");

        // TODO: Initialization...

        // Stop the initialization profiler
        initProf.stop();

        // Initialization finished, show a message
        ClientCore.getLogger().info("Successfully initialized, took " + initProf.getDurationString() + "! Cave Johnson here!");

        // TODO: Execute a test connection
        Connector c = new Connector("localhost");
        c.connect();

        // The initialization seems to be fine, return true
        return true;
    }

    /**
     * Get the main logger instance
     * @return Main logger instance
     */
    public static Logger getLogger() {
        return ClientCore.log;
    }

    /**
     * Set the main logger instance
     * @param log Main logger instance
     */
    public static void setLogger(Logger log) {
        ClientCore.log = log;
    }
}
