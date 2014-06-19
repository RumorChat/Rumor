package com.timvisee.rumor.client;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.Profiler;

public class CoreClient extends Core {

    /**
     * Constructor
     */
    public CoreClient() {
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
        CoreClient.getLogger().info("Initializing " + Defaults.APP_CLIENT_NAME + " v" + Defaults.APP_VERSION_NAME + " (" + Defaults.APP_VERSION_CODE + ")...");

        // TODO: Initialization...

        // Stop the initialization profiler
        initProf.stop();

        // Initialization finished, show a message
        CoreClient.getLogger().info("Successfully initialized, took " + initProf.getDurationString() + "! Cave Johnson here!");

        // TODO: Execute a test client
        Connector c = new Connector("localhost");
        c.connect();

        // The initialization seems to be fine, return true
        return true;
    }
}
