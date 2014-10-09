package com.timvisee.rumor.client;

import com.timvisee.rumor.Core;
import com.timvisee.rumor.Defaults;
import com.timvisee.rumor.util.Profiler;
import com.timvisee.rumor.client.connection.server.ServerConnector;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class CoreClient extends Core {

    /** Server connector instance */
    private ServerConnector con;

    /**
     * Constructor
     */
    public CoreClient() {
        super();
    }

    /**
     * Initialize
     * @return True on success, false on failure.
     */
    @Override
    protected boolean init() {
        // Profile the initialization
        Profiler initProf = new Profiler(true);

        // Show an initialization message
        CoreClient.getLogger().info("Initializing " + Defaults.APP_CLIENT_NAME + " v" + Defaults.APP_VERSION_NAME + " (" + Defaults.APP_VERSION_CODE + ")...");

        // TODO: Initialization...

        // Stop the initialization profiler
        initProf.stop();

        // Initialization finished, show a message
        CoreClient.getLogger().info("Successfully initialized, took " + initProf.getDurationString() + "! Cave Johnson here!");

        // TODO: Perform a demo connection for test purposes
        this.con = new ServerConnector("local.timvisee.com");
        this.con.connect();

        // Build a window for test purposes
        buildTestWindow();

        // The initialization seems to be fine, return true
        return true;
    }

    /**
     * Get the server connection instance.
     * @return Server connection instance, or null if the server connection isn't available yet.
     */
    public ServerConnector getServerConnector() {
        return this.con;
    }



    public void buildTestWindow() {
        Random rand = new Random();

        // Create the frame and set it's properties
        JFrame f = new JFrame(Defaults.APP_CLIENT_NAME + " v" + Defaults.APP_VERSION_NAME);
        f.setLayout(new FlowLayout());
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(600, 400));
        f.getContentPane().setBackground(new Color(48, 48, 48));

        // Create a list with some dummy text
        final DefaultListModel dlm = new DefaultListModel();
        /*for(int i3 = 0; i3 < 100; i3++)
            dlm.addElement("%user%: " + rand.nextInt(Integer.MAX_VALUE));*/
        dlm.addElement("[CORE] Started!");
        JList list = new JList(dlm);

        // Create the chat view
        JScrollPane chatView = new JScrollPane(list);
        chatView.setPreferredSize(new Dimension(300, 200));
        f.add(chatView);

        final JTextArea txt = new JTextArea("Msg");
        f.add(txt, BorderLayout.PAGE_END);
        txt.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() != KeyEvent.VK_ENTER)
                    return;

                e.consume();

                String msg = txt.getText();
                txt.setText("");

                msg = msg.trim();

                if(msg.length() <= 0)
                    return;

                dlm.addElement("%user%: " + msg);
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        });

        // Pack all the components in the frame
        f.pack();

        // Set the frame's location
        f.setLocationRelativeTo(null);

        // Show the frame
        f.setVisible(true);
    }
}
