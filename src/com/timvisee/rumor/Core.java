package com.timvisee.rumor;

import com.timvisee.rumor.log.Logger;

public abstract class Core {

    protected static Logger log;

    /**
     * Constructor
     */
    public Core() {
        // Set the logger instance
        Core.log = new Logger(true, Defaults.APP_DEBUG);

        // Initialize
        init();
    }

    /**
     * Initialize
     * @return True on success, false on failure.
     */
    protected abstract boolean init();

    /**
     * Get the main logger instance
     * @return Main logger instance
     */
    public static Logger getLogger() {
        return Core.log;
    }

    /**
     * Set the main logger instance
     * @param log Main logger instance
     */
    public static void setLogger(Logger log) {
        Core.log = log;
    }
}
