package com.timvisee.rumor.client;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.Profiler;
import com.timvisee.rumor.client.connector.ServerConnector;

public class CoreClient extends Core {

    private ServerConnector con;

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

        // TODO: Perform a demo connection for test purposes
        this.con = new ServerConnector("localhost");
        this.con.connect();

        // The initialization seems to be fine, return true
        return true;
    }

    /**
     * Get the server connector instance.
     * @return Server connector instance, or null if the server connector isn't available yet.
     */
    public ServerConnector getServerConnector() {
        return this.con;
    }
}
