package com.timvisee.rumor.server;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.util.Profiler;

public class CoreServer extends Core {

    /** Server controller instance */
    private ServerController sc;
    /** Static instance */
    public static CoreServer instance;

    /**
     * Constructor
     */
    public CoreServer() {
        // Construct the parent class
        super();

        // Set the instance
        CoreServer.instance = this;
    }

    /**
     * Initialize
     * @return True on success, false on failure.
     */
    @Override
    protected boolean init() {
        // Profile the initialization
        Profiler initProf = new Profiler(true);

        // Show an initialization message
        CoreServer.getLogger().info("Initializing " + Defaults.APP_SERVER_NAME + " v" + Defaults.APP_VERSION_NAME + " (" + Defaults.APP_VERSION_CODE + ")...");

        // TODO: Initialization...

        // Stop the initialization profiler
        initProf.stop();

        // Initialization finished, show a message
        CoreServer.getLogger().info("Successfully initialized, took " + initProf.getDurationString() + "! Cave Johnson here!");

        // Start the server
        this.sc = new ServerController();
        this.sc.start();

        // The initialization seems to be fine, return true
        return true;
    }

    /**
     * Get the server controller instance
     * @return Server controller instance
     */
    public ServerController getServerController() {
        return this.sc;
    }
}
