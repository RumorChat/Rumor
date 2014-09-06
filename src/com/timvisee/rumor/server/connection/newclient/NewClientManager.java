package com.timvisee.rumor.server.connection.newclient;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.server.connection.Connection;
import com.timvisee.rumor.server.connection.newclient.NewClient;

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
                try {
                    while(true) {
                        for(NewClient client : newClients) {

                            // TODO: Proper error messages and handling for everything bellow
                            if(!client.hasHandshaked()) {
                                long diff = System.currentTimeMillis() - client.getConnectedAt();

                                if(diff >= 5000)
                                    Core.getLogger().error("New client inactive for longer than 5 secs!");

                            } else if(!client.hasClientInfoReceived()) {
                                long diff = System.currentTimeMillis() - client.getHandshakedAt();

                                if(diff >= 5000)
                                    Core.getLogger().error("New client inactive for longer than 5 secs after handshake!");

                            } else if(!client.hasAuthenticated()) {
                                long diff = System.currentTimeMillis() - client.getClientInfoReceivedAt();

                                if(diff >= 5000)
                                    Core.getLogger().error("New client inactive for longer than 5 secs after client info!");

                            } else {
                                // TODO: Client already authenticated, why is it still a new client?
                            }
                        }

                        // Wait for a second
                        Thread.sleep(1000);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
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
}
