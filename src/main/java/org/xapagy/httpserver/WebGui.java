package org.xapagy.httpserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xapagy.agents.Agent;
import org.xapagy.ui.TextUi;

public class WebGui {

    private static WebGui theWebGui = null;
    
    /**
     * Utility function: returns true if the UI is started correctly
     * 
     * @param agent
     * @return
     */
    public static WebGui startWebGui(Agent agent) {
        return WebGui.startWebGui(agent, 1500);
    }

    /**
     * Utility function: returns true if the UI is started correctly
     * 
     * @param agent
     * @param port
     * @return
     */
    //@SuppressWarnings("unused")
    public static WebGui startWebGui(Agent agent, int port) {
        if (theWebGui != null) {
            return theWebGui;
        }
        try {
            theWebGui = new WebGui(agent, port);
            return theWebGui;
        } catch (IOException e) {
            e.printStackTrace();
            TextUi.abort("Could not start WebGui, probably the port is occupied");
        } catch(Exception ex) {
            ex.printStackTrace();
            TextUi.abort("startWebGui catched");
        }
        // should not be reached
        return null;
    }

    /**
     * Creates a web based GUI for the specified agent at listening the
     * specified port
     * 
     * @param agent
     * @param port
     * @throws FileNotFoundException
     * @throws IOException
     */
    private WebGui(Agent agent, int port) throws FileNotFoundException,
            IOException {
        File dir = new File("output");
        WgPageGenerator gfs = new WgPageGenerator(agent, dir);
        WgServerThread gs = new WgServerThread(port, gfs);
        gs.start();
        Thread t = new Thread(gs);
        t.start();
    }

}
