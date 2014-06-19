package com.timvisee.rumor;

public class Profiler {

    long start = -1;
    long duration = 0;

    /**
     * Constructor
     */
    public Profiler() { }

    /**
     * Constructor
     * @param start True to start the profiler, false to keep it inactive.
     */
    public Profiler(boolean start) {
        // Check whether we should start the profiler
        if(start)
            start();
    }

    /**
     * Start the profiler
     * @return Profiler instance, for method chaining
     */
    public Profiler start() {
        this.start = System.currentTimeMillis();
        return this;
    }

    /**
     * Stop the profiler
     * @return Profiler instance, for method chaining
     */
    public Profiler stop() {
        // Make sure the profiler is active
        if(isActive())
            this.duration += System.currentTimeMillis() - start;
        return this;
    }

    /**
     * Return the duration in milliseconds..
     * @param stop True to stop the profiler if it's active.
     * @return Duration in milliseconds.
     */
    public long getDuration(boolean stop) {
        // Stop the profiler if it's currently active
        if(isActive()) {
            stop();

            // Check whether we should restart the profiler
            if(!stop)
                start();
        }

        // Return the duration
        return this.duration;
    }

    /**
     * Return the duration in milliseconds. Calling this method doesn't stop the profiler if it's active.
     * @return Duration in milliseconds
     */
    public long getDuration() {
        return getDuration(false);
    }

    /**
     * Return the duration as readable text.
     * @param stop True to stop the profiler if it's active.
     * @return Duration as readable text.
     */
    public String getDurationString(boolean stop) {
        long duration = getDuration(stop);

        // Return durations which are 0 or shorter
        if(duration <= 0)
            return "<1 ms";

        // Return milliseconds
        if(duration < 1000)
            return duration + " ms";

        // Convert and return the duration as seconds
        return (duration / 1000) + " s";
    }

    /**
     * Return the duration as readable text. Calling this method doesn't stop the profiler if it's active.
     * @return Duration as readable text.
     */
    public String getDurationString() {
        return getDurationString(false);
    }

    /**
     * Check whether the profiler is active
     * @return True if the profiler is active, false otherwise
     */
    public boolean isActive() {
        return this.start >= 0;
    }

    /**
     * Check whether the profiler has started, it doesn't have to be active.
     * @return True if the profiler has been started, false otherwise.
     */
    public boolean isStarted() {
        return isActive() || (this.duration > 0);
    }

    /**
     * Reset the profiler
     * @return Profiler instance, for method chaining
     */
    public Profiler reset() {
        this.start = -1;
        this.duration = 0;
        return this;
    }
}
