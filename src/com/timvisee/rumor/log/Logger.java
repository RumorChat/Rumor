package com.timvisee.rumor.log;

public class Logger {

    private static final String INFO_PREFIX = "[INFO]";
    private static final String WARNING_PREFIX = "[WARNING]";
    private static final String ERROR_PREFIX = "[ERROR]";
    private static final String DEBUG_PREFIX = "[DEBUG]";

    private boolean enabled = true;
    private boolean debug = false;

    /**
     * Constructor
     */
    public Logger() {
        this(true, false);
    }

    /**
     * Constructor
     * @param enabled True to enable the logger, false to disable.
     * @param debug True to enable debug messages, false to disable.
     */
    public Logger(boolean enabled, boolean debug) {
        setEnabled(enabled);
        setDebug(debug);
    }

    /**
     * Log an info message
     * @param msg Info message to log
     */
    public void info(String msg) {
        this.log(INFO_PREFIX, msg);
    }

    /**
     * Log a warning message
     * @param msg Warning message to log
     */
    public void warning(String msg) {
        this.log(WARNING_PREFIX, msg);
    }

    /**
     * Log an error message
     * @param msg Error message to log
     */
    public void error(String msg) {
        this.log(ERROR_PREFIX, msg);
    }

    public void debug(String msg) {
        // Make sure debug messages are enabled
        if(debug)
            this.log(DEBUG_PREFIX, msg);
    }

    /**
     * Log a message
     * @param msg Message to log
     */
    public void log(String msg) {
        // Make sure the logger is enabled
        if(this.enabled)
            // Print the logged message to the console
            System.out.println(msg);

        // TODO: Store the logged message to a log file
    }

    /**
     * Log a message
     * @param prefix Message prefix
     * @param msg Message to log
     */
    public void log(String prefix, String msg) {
        this.log(prefix + " " + msg);
    }

    /**
     * Check whether the logger is enabled or not.
     * @return True if the logger is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Set whether the logger is enabled or not.
     * @param enabled True to enable the logger, false to disable.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Check whether debug messages are enabled
     * @return True if debug messages are enabled, false otherwise.
     */
    public boolean isDebug() {
        return this.debug;
    }

    /**
     * Set whether debug messages are enabled or not.
     * @param debug True to enable debug messages, false to disable.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
