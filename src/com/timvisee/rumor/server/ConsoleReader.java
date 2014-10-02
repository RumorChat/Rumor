package com.timvisee.rumor.server;

import com.timvisee.rumor.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader {

    private Thread t;

    /**
     * Constructor
     */
    public ConsoleReader() {
        // Initialize
        init();
    }

    public void init() {
        this.t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
                    String in = null;
                    try {
                        in = bf.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(!in.trim().equalsIgnoreCase("stop"))
                        Core.getLogger().debug("Console Input: " + in);

                    else
                        CoreServer.instance.stop();
                }
            }
        });
        this.t.start();
    }
}
