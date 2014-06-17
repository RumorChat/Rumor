package com.timvisee.rumor.client;

public class RumorClient {

    public static final String APP_NAME = "Rumor";
    public static final String APP_CLIENT_NAME = "Rumor Client";
    public static final String APP_SERVER_NAME = "Rumor Server";
    public static final int APP_VERSION_CODE = 1;
    public static final String APP_VERSION_NAME = "0.1-Pre-Alpha";

    public static final int APP_SERVER_PORT = 1234;
    public static final boolean APP_DEBUG = true;

    /**
     * Main method, to start the application
     * @param args
     */
    public static void main(String[] args) {
        // Start the core
        new ClientCore();
    }
}
