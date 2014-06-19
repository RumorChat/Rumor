package com.timvisee.rumor.server.client;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {

    private List<Client> cons = new ArrayList<Client>();

    /**
     * Constructor
     */
    public ClientManager() { }

    /**
     * Return the list of connected clients
     * @return List of connected clients
     */
    public List<Client> getClients() {
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
     * @param c Client instance
     * @return True if the client was registered successfully, false if failed.
     */
    public boolean registerClient(Client c) {
        // Register the client
        this.cons.add(c);

        // Everything seems to be fine, return true
        return true;
    }
}
