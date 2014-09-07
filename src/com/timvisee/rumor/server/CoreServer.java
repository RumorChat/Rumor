package com.timvisee.rumor.server;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.server.connection.ConnectionManager;
import com.timvisee.rumor.util.Profiler;

public class CoreServer extends Core {

    /** Server controller instance */
    private ServerController serverController;
    /** Static instance */
    public static CoreServer instance;

    public ConsoleReader consoleReader;

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

        // TODO: Initialization code...

        // Stop the initialization profiler
        initProf.stop();

        // Initialization finished, show a message
        CoreServer.getLogger().info("Successfully initialized, took " + initProf.getDurationString() + "! Cave Johnson here!");

        // Create and start the server controller
        this.serverController = new ServerController();
        this.serverController.start();

        // Set up the console reader
        this.consoleReader = new ConsoleReader();

        // The initialization seems to be fine, return true
        return true;
    }

    /**
     * Get the server controller instance
     * @return Server controller instance
     */
    public ServerController getServerController() {
        return this.serverController;
    }

    /**
     * Stop the Rumor server. The application will be terminated on success.
     *
     * @return True on success, false on failure.
     */
    public boolean stop() {
        // Profile the stopping server
        Profiler stopProf = new Profiler(true);

        // Server stopped, show a status message
        Core.getLogger().info("Stopping server...");

        // Stop the server controller, return false on failure
        if(!serverController.stop(true))
            return false;

        // Stop the stopping server profiler
        stopProf.stop();

        // Server stopped, show a status message
        CoreServer.getLogger().info("Server stopped, took " + stopProf.getDurationString() + "!");

        // Quit the application
        System.exit(0);

        // Everything seems to be fine, return true
        return true;
    }
}
