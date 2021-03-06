package com.timvisee.rumor.server;

public enum DisconnectReason {

    UNKNOWN(0),
    SERVER_SHUTDOWN(10),
    CONNECTION_TIMEOUT(20),
    AUTH_TIMEOUT(30),
    TOO_MANY_MALFORMED_PACKETS(40);

    /** Disconnect reason type ID */
    private final int id;

    /**
     * Constructor.
     *
     * @param id Disconnect reason type ID
     */
    DisconnectReason(int id) {
        this.id = id;
    }

    /**
     * Get the disconnect reason type ID.
     *
     * @return Disconnect reason type ID.
     */
    public int id() {
        return this.id;
    }
}
