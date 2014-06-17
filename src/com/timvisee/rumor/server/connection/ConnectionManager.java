package com.timvisee.rumor.server.connection;

import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {

    private List<Connection> cons = new ArrayList<Connection>();

    private int acceptedCount = 0;

    /**
     * Constructor
     */
    public ConnectionManager() { }

    /**
     * Return the list of active connections
     * @return List of active connections
     */
    public List<Connection> getConnections() {
        return this.cons;
    }

    /**
     * Get the count of active connections
     * @return Count of active connections
     */
    public int getConnectionCount() {
        return this.cons.size();
    }

    /**
     * Register a new connection
     * @param con Connector instance
     * @return True if succeed, false if failed.
     */
    public boolean registerConnection(Connection con) {
        // Register the connection
        this.cons.add(con);

        // Increase the accepted count
        this.acceptedCount++;

        // Everything seems to be fine, return true
        return true;
    }

    /**
     * Get total count of accepted connections
     * @return Total count of accepted connections
     */
    public int getAcceptedCount() {
        return this.acceptedCount;
    }
}
