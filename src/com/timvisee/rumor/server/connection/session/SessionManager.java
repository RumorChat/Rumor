package com.timvisee.rumor.server.connection.session;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    /** List of active sessions */
    private List<Session> sessions = new ArrayList<Session>();

    /**
     * Constructor
     */
    public SessionManager() { }

    /**
     * Return the list of connected sessions
     * @return List of connected sessions
     */
    public List<Session> getSessions() {
        return this.sessions;
    }

    /**
     * Get the count of connected sessions
     * @return Count of connected sessions
     */
    public int getSessionCount() {
        return this.sessions.size();
    }

    /**
     * Create a new session.
     * @param sock Client's socket instance
     * @return The session instance if a session was created successfully, null otherwise.
     */
    public Session createSession(Socket sock) {
        // Construct the session connector
        SessionConnector con = new SessionConnector(sock);

        // Construct the session itself
        Session s = new Session(con);

        // Add the session to the list, and return the session instance
        this.sessions.add(s);
        return s;
    }

    /**
     * Remove a session by it's socket
     * @param sock Socket of the session to remove
     * @return True if any session was removed, false if not.
     */
    public boolean removeSession(Socket sock) {
        // Make sure a valid socket instance is specified
        if(sock == null)
            return false;

        // Track whether we removed a session or not
        boolean removed = false;

        // Remove each session with the specified socket
        for(int i = 0; i < this.sessions.size(); i++) {
            // Get the current session entry
            Session s = this.sessions.get(i);

            // Make sure the sockets are equal
            if(s.getConnector().getSocket() != sock)
                continue;

            // Make sure session is disconnected
            s.disconnect();

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
     * @return Count of disconnected sessions.
     */
    public int disconnectAll() {
        // Count the sessions currently available
        int sessionCount = this.sessions.size();

        // Disconnect each session
        for(Session s : this.sessions)
            s.disconnect();

        // Return the count of disconnected sessions
        return sessionCount - this.sessions.size();
    }
}
