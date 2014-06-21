package com.timvisee.rumor.server.connection.session;

public class Session {

    private SessionConnector con;

    /**
     * Create a new session.
     * @param con Session connector instance
     */
    public Session(SessionConnector con) {
        this.con = con;
    }

    /**
     * Check whether this session is connected
     * @return True if connected, false otherwi se
     */
    public boolean isConnected() {
        return this.con.isConnected();
    }

    /**
     * Disconnect the session
     * @return True if the session was disconnected successfully, false otherwise.
     * Also returns true if the session was disconnected already.
     */
    public boolean disconnect() {
        // Disconnect the connector
        this.con.disconnect();

        // TODO: Make sure to remove the session from the session manager!

        // Return true if we're disconnected
        return !isConnected();
    }

    /**
     * Get the session connector instance
     * @return Session connector instance
     */
    public SessionConnector getConnector() {
        return this.con;
    }
}
