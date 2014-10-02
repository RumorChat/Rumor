package com.timvisee.rumor.server.connection.authenticator;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.server.CoreServer;
import com.timvisee.rumor.server.DisconnectReason;
import com.timvisee.rumor.server.connection.Connection;

import java.util.ArrayList;
import java.util.List;

public class ClientAuthenticator {

    /** List holding all authenticating clients */
    private List<AuthenticatingClient> authClients = new ArrayList<AuthenticatingClient>();

    /** The management thread, which manages all authenticating clients */
    private Thread manThread;

    /**
     * Constructor
     */
    public ClientAuthenticator() {
        // Create and start the manager thread
        this.manThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // New client manager thread stopped, show a status message
                Core.getLogger().debug("Client authenticator thread started!");

                try {
                    while(!Thread.currentThread().isInterrupted()) {
                        for(int i = 0; i < authClients.size(); i++) {
                            AuthenticatingClient client = authClients.get(i);

                            // TODO: Proper error messages and handling for everything bellow
                            if(!client.hasHandshaked()) {
                                long diff = System.currentTimeMillis() - client.getConnectedAt();

                                if(diff >= 5000) {
                                    Core.getLogger().warning("Kicked client, not responding at authentication for 5 seconds!");
                                    client.disconnect(DisconnectReason.AUTH_TIMEOUT);
                                }

                            } else if(!client.hasClientInfoReceived()) {
                                long diff = System.currentTimeMillis() - client.getHandshakedAt();

                                if(diff >= 5000) {
                                    Core.getLogger().warning("Kicked client, not responding at authentication for 5 seconds!");
                                    client.disconnect(DisconnectReason.AUTH_TIMEOUT);
                                }

                            } else if(!client.hasAuthenticated()) {
                                long diff = System.currentTimeMillis() - client.getClientInfoReceivedAt();

                                if(diff >= 5000) {
                                    Core.getLogger().warning("Kicked client, not responding at authentication for 5 seconds!");
                                    client.disconnect(DisconnectReason.AUTH_TIMEOUT);
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
                Core.getLogger().debug("Client authenticator thread stopped!");
            }
        });
        this.manThread.start();
    }

    /**
     * Create and add a new authenticating client, referenced by it's connection instance.
     *
     * @param con The connection instance of the client to create and add as authenticating client.
     *
     * @return The created authenticating client instance.
     */
    public AuthenticatingClient createAuthClient(Connection con) {
        // Create the authenticating client
        AuthenticatingClient authClient = new AuthenticatingClient(con);

        // Add the authenticating client, return null on failure
        if(!this.addAuthClient(authClient))
            return null;

        // Return the authenticating client instance
        return authClient;
    }

    /**
     * Add a new authenticating client.
     *
     * @param authClient The authenticating client to add.
     *
     * @return True on success, false on failure.
     */
    public boolean addAuthClient(AuthenticatingClient authClient) {
        return this.authClients.add(authClient);
    }

    /**
     * Remove an authenticating client, referenced by it's connection instance.
     *
     * @param con Connection instance of the client to remove.
     *
     * @return True on success, false on failure.
     */
    public boolean removeAuthClient(Connection con) {
        // Remove the new client
        for(AuthenticatingClient client : this.authClients)
            if(client.getConnection().equals(con))
                if(removeAuthClient(client))
                    return true;
        return false;
    }

    /**
     * Remove an authenticating client.
     *
     * @param client The authenticating client to remove.
     *
     * @return True on success, false on failure.
     */
    public boolean removeAuthClient(AuthenticatingClient client) {
        // Destroy the authenticating client before removing it from the list
        client.destroy();

        // Remove the client from the list, return the result
        return this.authClients.remove(client);
    }

    /**
     * Get a list of authenticating clients.
     *
     * @return List of authenticating clients.
     */
    public List<AuthenticatingClient> getAuthClients() {
        return this.authClients;
    }

    /**
     * Get the number of authenticating clients.
     *
     * @return Number of authenticating clients.
     */
    public int getAuthClientsCount() {
        return this.authClients.size();
    }

    /**
     * Stop the client authenticator.
     *
     * @param wait True to wait until client authenticator thread is stopped, false otherwise.
     *
     * @return True on success, false on failure.
     */
    public boolean stop(boolean wait) {
        // TODO: Rewrite this method, due to thread interruptions

        // Stopping client authenticator, show a status message
        Core.getLogger().debug("Stopping client authenticator...");

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
            CoreServer.getLogger().error("An error occurred while stopping the client authenticator thread!");
            CoreServer.getLogger().error("ERROR: " + e.getMessage());
        }

        // Stopped the client authenticator, show a status message
        Core.getLogger().debug("Client authenticator stopped!");

        // The client authenticator was stopped successfully, return the result
        return true;
    }
}
