package com.timvisee.rumor.server.connection.session;

import com.timvisee.rumor.server.DisconnectReason;
import com.timvisee.rumor.server.connection.Connection;

public class Session {

    private Connection con;

    /**
     * Create a new session.
     *
     * @param con Client connection
     */
    public Session(Connection con) {
        this.con = con;
    }

    /**
     * Check whether this session is connected
     *
     * @return True if connected, false otherwise
     */
    public boolean isConnected() {
        return this.con.isConnected();
    }

    /**
     * Disconnect the session
     *
     * @param reason Disconnection reason.
     *
     * @return True if the session was disconnected successfully, false otherwise.
     * Also returns true if the session was disconnected already.
     */
    public boolean disconnect(DisconnectReason reason) {
        // Disconnect the connector
        if(this.con.isConnected())
            this.con.disconnect(reason);

        // TODO: Make sure to remove the session from the session manager!

        // Return true if we're disconnected
        return !isConnected();
    }

    /**
     * Get the connection instance
     *
     * @return Connection instance
     */
    public Connection getConnection() {
        return this.con;
    }
}
