package com.timvisee.rumor.server.client;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {

    private List<ClientConnector> cons = new ArrayList<ClientConnector>();

    /**
     * Constructor
     */
    public ClientManager() { }

    /**
     * Return the list of connected clients
     * @return List of connected clients
     */
    public List<ClientConnector> getClients() {
        return this.cons;
    }

    /**
     * Get the count of connected clients
     * @return Count of connected clients
     */
    public int getClientCount() {
        return this.cons.size();
    }

    /**
     * Register a new client
     * @param c Client connector instance
     * @return True if the client was registered successfully, false if failed.
     */
    public boolean registerClient(ClientConnector c) {
        // Register the client, return the result
        this.cons.add(c);
        return true;
    }
}
