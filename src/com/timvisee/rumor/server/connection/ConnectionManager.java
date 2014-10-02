package com.timvisee.rumor.server.connection;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.server.CoreServer;
import com.timvisee.rumor.server.DisconnectReason;

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
        // Client connected, show a status message
        CoreServer.getLogger().info("Client connected from " + c.getSocket().getInetAddress().getHostAddress() + "!");

        // Add the connection, return the result
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

    /**
     * Disconnect and remove a connection.
     *
     * @param con The connection to disconnect.
     *
     * @return True on success, false on failure. False wills be returned if the connection was unknown.
     */
    public boolean disconnect(Connection con, DisconnectReason reason) {
        // Make sure the connection isn't null
        if(con == null)
            return false;

        // Disconnect the connection
        if(con.isConnected())
            con.disconnect(reason);

        // Remove the connection from the list
        this.cons.remove(con);

        // Remove this connection from the new client and session manager
        CoreServer.instance.getServerController().getClientAuthenticator().removeAuthClient(con);
        CoreServer.instance.getServerController().getSessionManager().removeSession(con);
        return true;
    }

    /**
     * Disconnect all clients
     *
     * @param reason Disconnect reason
     */
    public void disconnectAll(DisconnectReason reason) {
        // Show a status message
        Core.getLogger().debug("Disconnecting " + getConnectionCount() + " clients...");

        // Disconnect all clients
        while(this.cons.size() > 0)
            this.cons.get(0).disconnect(reason);

        // Show a status message
        Core.getLogger().debug("All clients disconnected!");
    }
}
