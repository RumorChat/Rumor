package com.timvisee.rumor.server.connection.newclient;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.protocol.packet.*;
import com.timvisee.rumor.server.connection.Connection;

public class NewClient {

    private Connection con;

    // TODO: Use a time class with better support!
    private long connectedAt;
    private long handshakeAt = -1;
    private long clientInfoAt = -1;
    private long authAt = -1;

    /**
     * Constructor
     *
     * @param con Client connection
     */
    public NewClient(Connection con) {
        this.con = con;

        // Set the time the new client was constructed
        this.connectedAt = System.currentTimeMillis();

        PacketListener l = new PacketListener() {
            @Override
            public void onPacketReceived(Packet p) {
                if(p.isPacketType(PacketType.HANDSHAKE)) {
                    setHandshaked(true);

                    Core.getLogger().debug("Handshake packet received from client!");

                    getConnection().sendPacket(PacketFactory.createHandshakePacket());

                } else if(p.isPacketType(PacketType.CLIENT_INFO)) {
                    setClientInfoReceived(true);

                    Core.getLogger().debug("Client info packet received from client!");

                    getConnection().sendPacket(PacketFactory.createServerInfoPacket());

                } else if(p.isPacketType(PacketType.AUTH)) {
                    //setAuthenticated(true);

                    Core.getLogger().debug("Auth packet received from client!");
                    Core.getLogger().debug("Authenticated with: " + p.getStrings().get(0));
                    Core.getLogger().debug("Authentication rejected!");

                    getConnection().sendPacket(PacketFactory.createAuthenticationResultPacket(false));
                }

                Core.getLogger().debug("[CLIENT STATUS] Handshaked: " + Boolean.toString(hasHandshaked()));
                Core.getLogger().debug("[CLIENT STATUS] Client Info: " + Boolean.toString(hasClientInfoReceived()));
                Core.getLogger().debug("[CLIENT STATUS] Authenticated: " + Boolean.toString(hasAuthenticated()));
            }
        };
        this.con.addPacketListener(l);
    }

    /**
     * Get the connection instance
     *
     * @return Connection instance
     */
    public Connection getConnection() {
        return this.con;
    }

    /**
     * Get the time in milliseconds the client connected at.
     *
     * @return The time in milliseconds the client connected at.
     */
    public long getConnectedAt() {
        return this.connectedAt;
    }

    /**
     * Get the time in milliseconds the client send a handshake.
     *
     * @return Time in milliseconds of the client handshake, -1 if no handshake was made yet.
     */
    public long getHandshakedAt() {
        return this.handshakeAt;
    }

    /**
     * Set whether the client made a handshake.
     *
     * @param madeHandshake True if the client made a handshake, false otherwise.
     */
    private void setHandshaked(boolean madeHandshake) {
        if(madeHandshake)
            this.handshakeAt = System.currentTimeMillis();
        else
            this.handshakeAt = -1;
    }

    /**
     * Check whether the client made a handshake.
     *
     * @return True if the client made a handshake, false otherwise.
     */
    public boolean hasHandshaked() {
        return this.handshakeAt != -1;
    }

    /**
     * Get the time in milliseconds the client information was received.
     *
     * @return Time in milliseconds the client information was received, -1 if no information was received yet.
     */
    public long getClientInfoReceivedAt() {
        return this.clientInfoAt;
    }

    /**
     * Set whether the client information was received.
     *
     * @param clientInfoReceived True if the client information was received, false otherwise.
     */
    private void setClientInfoReceived(boolean clientInfoReceived) {
        if(clientInfoReceived)
            this.clientInfoAt = System.currentTimeMillis();
        else
            this.clientInfoAt = -1;
    }

    /**
     * Check whether the client information was received.
     *
     * @return True if the client information was received, false otherwise.
     */
    public boolean hasClientInfoReceived() {
        // Make sure a handshake has been made
        if(!this.hasHandshaked())
            return false;

        // Check whether client info has been received, return the result
        return this.clientInfoAt != -1;
    }

    /**
     * Get the time in milliseconds the client was authenticated at.
     *
     * @return The time in milliseconds the client was authenticated at, -1 if the client wasn'manThread authenticated yet.
     */
    public long getAuthenticatedAt() {
        return this.authAt;
    }

    /**
     * Set whether the client has been authenticated.
     *
     * @param authenticated True if the client has been authenticated, false otherwise.
     */
    private void setAuthenticated(boolean authenticated) {
        if(authenticated)
            this.authAt = System.currentTimeMillis();
        else
            this.authAt = -1;
    }

    /**
     * Check whether the client has been authenticated.
     *
     * @return True if the client has been authenticated, false otherwise.
     */
    public boolean hasAuthenticated() {
        // Make sure the client info was received
        if(!hasClientInfoReceived())
            return false;

        // Check whether the client was authenticated, return the result
        return this.authAt != -1;
    }
}
