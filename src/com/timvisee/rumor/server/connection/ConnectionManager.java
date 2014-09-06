package com.timvisee.rumor.server.connection;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {

    /** List of connections */
    List<Connection> cons = new ArrayList<Connection>();

    /**
     * Constructor
     */
    public ConnectionManager() { }

    /**
     * Add a new connection based on the client socket.
     *
     * @param sock Socket of the client to add as connection.
     *
     * @return Connection instance of the client.
     */
    public Connection addConnection(Socket sock) {
        // Create a connection instance
        Connection c = new Connection(sock);

        // Add the connection instance to the list
        if(!this.addConnection(c))
            return null;

        // Return the connection instance
        return c;
    }

    /**
     * Add a new connection.
     *
     * @param c The connection to add.
     *
     * @return True on success, false on failure.
     */
    public boolean addConnection(Connection c) {
        return this.cons.add(c);
    }

    /**
     * Get the list of connections.
     *
     * @return List of connections.
     */
    public List<Connection> getConnections() {
        return this.cons;
    }

    /**
     * Get the number of connections.
     *
     * @return The number of connections.
     */
    public int getConnectionCount() {
        return this.cons.size();
    }
}
