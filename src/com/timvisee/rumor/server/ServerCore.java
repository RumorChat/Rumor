package com.timvisee.rumor.server;

import com.timvisee.rumor.Profiler;
import com.timvisee.rumor.log.Logger;

public class ServerCore {

    private static Logger log = new Logger(true, RumorServer.APP_DEBUG);

    private ServerController sc;

    /**
     * Constructor
     */
    public ServerCore() {
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
        ServerCore.getLogger().info("Initializing " + RumorServer.APP_SERVER_NAME + " v" + RumorServer.APP_VERSION_NAME + " (" + RumorServer.APP_VERSION_CODE + ")...");

        // TODO: Initialization...

        // Stop the initialization profiler
        initProf.stop();

        // Initialization finished, show a message
        ServerCore.getLogger().info("Successfully initialized, took " + initProf.getDurationString() + "! Cave Johnson here!");

        // Start the server
        this.sc = new ServerController();

        // The initialization seems to be fine, return true
        return true;
    }

    /**
     * Get the main logger instance
     * @return Main logger instance
     */
    public static Logger getLogger() {
        return ServerCore.log;
    }

    /**
     * Set the main logger instance
     * @param log Main logger instance
     */
    public static void setLogger(Logger log) {
        ServerCore.log = log;
    }

    /**
     * Get the server controller instance
     * @return Server controller instance
     */
    public ServerController getServerController() {
        return this.sc;
    }
}
