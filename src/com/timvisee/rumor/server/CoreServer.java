package com.timvisee.rumor.server;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.Profiler;

public class CoreServer extends Core {

    private ServerController sc;

    /**
     * Constructor
     */
    public CoreServer() {
        super();
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
