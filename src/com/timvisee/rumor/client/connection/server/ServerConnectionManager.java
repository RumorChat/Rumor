package com.timvisee.rumor.client.connection.server;

import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.client.CoreClient;

public class ServerConnectionManager {

    private ServerConnector con;

    /**
     * Constructor
     */
    public ServerConnectionManager() { }

    /**
     * Connect to the server, on the default port.
     * @param host Server host
     * @return True if we successfully connected to the server, false otherwise.
     * Also returns true if there already was an active connection.
     */
    public boolean connect(String host) {
        return connect(host, Defaults.APP_SERVER_PORT);
    }

    /**
     * Connect to the server, on the default port.
     * @param host Server host
     * @param port Server port
     * @return True if we successfully connected to the server, false otherwise.
     * Also returns true if there already was an active connection.
     */
    public boolean connect(String host, int port) {
        // Make sure we aren't connected already
        if(isConnected()) {
            CoreClient.getLogger().debug("Cancelled connection to the server, already connected!");
            return true;
        }

        // Set up the server connection
        this.con = new ServerConnector(host, port);

        // Return true if we successfully connected to the server
        return isConnected();
    }

    /**
     * Check whether there's an active connection to the server.
     * @return True if there's an active connection to the server, false otherwise
     */
    public boolean isConnected() {
        // Make sure a server connection is available
        if(this.con == null)
            return false;

        // Check if there's an active connection
        return this.con.isConnected();
    }
}
