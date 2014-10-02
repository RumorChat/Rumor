package com.timvisee.rumor.server.connection.session;

import com.timvisee.rumor.server.DisconnectReason;
import com.timvisee.rumor.server.connection.Connection;
import com.timvisee.rumor.server.connection.authenticator.AuthenticatingClient;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    /**
     * List of active sessions
     */
    private List<Session> sessions = new ArrayList<Session>();

    /**
     * Constructor
     */
    public SessionManager() { }

    /**
     * Return the list of connected sessions
     *
     * @return List of connected sessions
     */
    public List<Session> getSessions() {
        return this.sessions;
    }

    /**
     * Get the count of connected sessions
     *
     * @return Count of connected sessions
     */
    public int getSessionCount() {
        return this.sessions.size();
    }

    /**
     * Create a new session.
     *
     * @param con Client connection.
     *
     * @return The session instance if a session was created successfully, null otherwise.
     */
    public Session createSession(Connection con) {
        // Construct the session connector
        //SessionConnector sesCon = new SessionConnector(con);

        // Construct the session itself
        Session s = new Session(con);

        // Add the session to the list, and return the session instance
        this.sessions.add(s);
        return s;
    }

    /**
     * Create a new session.
     *
     * @param client New client instance
     *
     * @return The session instance if a session was created successfully, null otherwise.
     */
    public Session createSession(AuthenticatingClient client) {
        return this.createSession(client.getConnection());
    }

    /**
     * Remove a session by it's socket
     *
     * @param con Socket of the session to remove
     *
     * @return True if any session was removed, false if not.
     */
    public boolean removeSession(Connection con) {
        // Make sure a valid socket instance is specified
        if(con == null)
            return false;

        // Track whether we removed a session or not
        boolean removed = false;

        // Remove each session with the specified socket
        for(int i = 0; i < this.sessions.size(); i++) {
            // Get the current session entry
            Session s = this.sessions.get(i);

            // Make sure the sockets are equal
            if(!s.getConnection().equals(con))
                continue;

            // Make sure session is disconnected
            s.disconnect(DisconnectReason.UNKNOWN);

            // Remove the session
            this.sessions.remove(i);
            removed = true;
            i--;
        }

        // Return the result
        return removed;
    }

    /**
     * Disconnect all sessions.
     * Warning: Some sessions might fail disconnecting.
     *
     * @param reason Disconnection reason.
     *
     * @return Count of disconnected sessions.
     */
    public int disconnectAll(DisconnectReason reason) {
        // Count the sessions currently available
        int sessionCount = this.sessions.size();

        // Disconnect each session
        for(Session s : this.sessions)
            s.disconnect(reason);

        // Return the count of disconnected sessions
        return sessionCount - this.sessions.size();
    }
}
