package com.timvisee.rumor.server.connection.newclient;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.server.CoreServer;
import com.timvisee.rumor.server.DisconnectReason;
import com.timvisee.rumor.server.connection.Connection;
import com.timvisee.rumor.server.connection.newclient.NewClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewClientManager {

    private List<NewClient> newClients = new ArrayList<NewClient>();

    private Thread manThread;

    /**
     * Constructor
     */
    public NewClientManager() {
        this.manThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // New client manager thread stopped, show a status message
                Core.getLogger().debug("New client manager thread started!");

                try {
                    while(!Thread.currentThread().isInterrupted()) {
                        for(NewClient client : newClients) {

                            // TODO: Proper error messages and handling for everything bellow
                            if(!client.hasHandshaked()) {
                                long diff = System.currentTimeMillis() - client.getConnectedAt();

                                if(diff >= 5000) {
                                    client.disconnect(DisconnectReason.CONNECTION_TIMEOUT);
                                    Core.getLogger().debug("Kicked non-responding client after 5 seconds!");
                                }

                            } else if(!client.hasClientInfoReceived()) {
                                long diff = System.currentTimeMillis() - client.getHandshakedAt();

                                if(diff >= 5000) {
                                    client.disconnect(DisconnectReason.CONNECTION_TIMEOUT);
                                    Core.getLogger().debug("Kicked non-responding client after 5 seconds!");
                                }

                            } else if(!client.hasAuthenticated()) {
                                long diff = System.currentTimeMillis() - client.getClientInfoReceivedAt();

                                if(diff >= 5000) {
                                    client.disconnect(DisconnectReason.CONNECTION_TIMEOUT);
                                    Core.getLogger().debug("Kicked non-responding client after 5 seconds!");
                                }

                            } else {
                                // TODO: Client already authenticated, why is it still a new client?
                            }
                        }

                        // Wait for a second
                        Thread.sleep(500);
                    }
                } catch(InterruptedException e) {
                } catch(Exception e) {
                    e.printStackTrace();
                }

                // New client manager thread stopped, show a status message
                Core.getLogger().debug("New client manager thread stopped!");
            }
        });
        this.manThread.start();
    }

    /**
     * Add a new client by it's socket instance.
     *
     * @param con The connection instance of the client.
     *
     * @return The new client instance, or null on failure.
     */
    public NewClient addNewClient(Connection con) {
        // Create the new client instance
        NewClient newClient = new NewClient(con);

        // Add the new client, return null on failure
        if(!this.addNewClient(newClient))
            return null;

        // Return the new client instance
        return newClient;
    }

    /**
     * Add a new client instance.
     *
     * @param newClient The new client instance to add.
     *
     * @return True on success, false on failure.
     */
    public boolean addNewClient(NewClient newClient) {
        return this.newClients.add(newClient);
    }

    /**
     * Get the list of new clients
     *
     * @return List of new clients
     */
    public List<NewClient> getNewClients() {
        return this.newClients;
    }

    /**
     * Get the number of new clients.
     *
     * @return Number of new clients.
     */
    public int getNewClientsCount() {
        return this.newClients.size();
    }

    /**
     * Stop the new client manager
     *
     * @param wait True to wait until the new client manager thread is stopped, false otherwise.
     *
     * @return True on success, false on failure.
     */
    public boolean stop(boolean wait) {
        // TODO: Rewrite this method, due to thread interruptions

        // Stopping new client manager, show a status message
        Core.getLogger().debug("Stopping new client manager...");

        try {
            // Interrupt the thread
            this.manThread.interrupt();

            // Wait for the thread to stop
            while(wait && this.manThread.isAlive()) {
                try {
                    Thread.sleep(1);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch(Exception e) {
            CoreServer.getLogger().error("An error occurred while stopping the new client thread!");
            CoreServer.getLogger().error("ERROR: " + e.getMessage());
        }

        // Stopping new client manager, show a status message
        Core.getLogger().debug("New client manager stopped!");

        // The new client manager was stopped successfully, return the result
        return true;
    }

    /**
     * Remove a new client by it's connection.
     *
     * @param con Connection of the new client to remove
     *
     * @return True if a new client was removed, false otherwise.
     */
    public boolean removeNewClient(Connection con) {
        // Remove the new client
        for(NewClient client : this.newClients)
            if(client.getConnection().equals(con))
                if(removeNewClient(client))
                    return true;
        return false;
    }

    /**
     * Remove a new client.
     *
     * @param client The new client to remove.
     *
     * @return Number of removed new clients
     */
    public boolean removeNewClient(NewClient client) {
        return this.newClients.remove(client);
    }
}
