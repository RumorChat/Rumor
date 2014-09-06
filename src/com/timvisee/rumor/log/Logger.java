package com.timvisee.rumor.log;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    /** Info message prefix */
    private static final String INFO_PREFIX = "[INFO]";
    /** Warning message prefix */
    private static final String WARNING_PREFIX = "[WARNING]";
    /** Error message prefix */
    private static final String ERROR_PREFIX = "[ERROR]";
    /** Debug message prefix */
    private static final String DEBUG_PREFIX = "[DEBUG]";

    /** Defines whether the logger is enabled or not */
    private boolean enabled = true;
    /** Defines whether the debugging mode is active */
    private boolean debug = false;

    /** True to hide all info messages, false to unhide them. */
    private boolean hideInfo = true;
    /** True to hide all warning messages, false to unhide them */
    private boolean hideWarn = true;
    /** True to hide all error messages, false to unhide them */
    private boolean hideErr = true;
    /** True to hide all debug messages, false to unhide them */
    private boolean hideDebug = true;
    /** True to show all message types, even if hiders are enabled. */
    private boolean showAll = true;

    public List<String> history = new ArrayList<String>();

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
        if(!this.hideInfo || this.showAll)
            this.log(INFO_PREFIX, msg);
    }

    /**
     * Log an info message
     * @param msg Info message to log as a string
     */
    public void info(Object msg) {
        // Make sure the object is set
        if(msg == null)
            info("null");
        else
            info(msg.toString());
    }

    /**
     * Log a warning message
     * @param msg Warning message to log
     */
    public void warning(String msg) {
        if(!this.hideWarn || this.showAll)
            this.log(WARNING_PREFIX, msg);
    }

    /**
     * Log an warning message
     * @param msg Warning message to log as a string
     */
    public void warning(Object msg) {
        // Make sure the object is set
        if(msg == null)
            warning("null");
        else
            warning(msg.toString());
    }

    /**
     * Log an error message
     * @param msg Error message to log
     */
    public void error(String msg) {
        if(!this.hideErr || this.showAll)
            this.log(ERROR_PREFIX, msg);
    }

    /**
     * Log an error message
     * @param msg Error message to log as a string
     */
    public void error(Object msg) {
        // Make sure the object is set
        if(msg == null)
            error("null");
        else
            error(msg.toString());
    }

    /**
     * Log a debug message
     * @param msg Debug message to log
     */
    public void debug(String msg) {
        // Make sure debug messages are enabled
        if(debug && (!this.hideDebug || this.showAll))
            this.log(DEBUG_PREFIX, msg);
    }

    /**
     * Log a debug message
     * @param msg Debug message to log as a string
     */
    public void debug(Object msg) {
        // Make sure the object is set
        if(msg == null)
            debug("null");
        else
            debug(msg.toString());
    }

    /**
     * Log a message
     * @param msg Message to log
     */
    public void log(String msg) {
        // Make sure the logger is enabled
        if(this.enabled) {
            // Print the logged message to the console
            System.out.println(msg);

            history.add(msg);
        }

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

    /**
     * Check whether info messages are hidden or not.
     * @return True if info messages are hidden, false if not.
     */
    public boolean getHideInfo() {
        return this.hideInfo;
    }

    /**
     * Set whether info messages should be hidden or not.
     * @param hideInfo True to hide info messages, false to unhide them.
     */
    public void setHideInfo(boolean hideInfo) {
        this.hideInfo = hideInfo;
    }

    /**
     * Check whether warning messages are hidden or not.
     * @return True if warning messages are hidden, false if not.
     */
    public boolean getHideWarnings() {
        return this.hideWarn;
    }

    /**
     * Set whether warning messages should be hidden or not.
     * @param hideWarn True to hide warning messages, false to unhide them.
     */
    public void setHideWarnings(boolean hideWarn) {
        this.hideWarn = hideWarn;
    }

    /**
     * Check whether error messages are hidden or not.
     * @return True if error messages are hidden, false if not.
     */
    public boolean getHideErrors() {
        return this.hideErr;
    }

    /**
     * Set whether error messages should be hidden or not.
     * @param hideErr True to hide error messages, false to unhide them.
     */
    public void setHideErrors(boolean hideErr) {
        this.hideErr = hideErr;
    }

    /**
     * Check whether debug messages are hidden or not.
     * @return True if debug messages are hidden, false if not.
     */
    public boolean getHideDebug() {
        return this.hideDebug;
    }

    /**
     * Set whether debug messages should be hidden or not.
     * @param hideDebug True to hide debug messages, false to unhide them.
     */
    public void setHideDebug(boolean hideDebug) {
        this.hideDebug = hideDebug;
    }

    /**
     * Check whether all kind of messages are being shown, even if some hiders are enabled.
     * The logger has to be enabled, or else all messages are suppressed anyway.
     * @return True if all kind of messages are shown.
     */
    public boolean getShowAll() {
        return this.showAll;
    }

    /**
     * Set whether all kind of messages should be shown, even if some hiders are enabled.
     * The logger has to be enabled, or else all messages are suppressed anyway.
     * @param showAll True to show all kind of messages even if hiders are enabled.
     */
    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }
}
