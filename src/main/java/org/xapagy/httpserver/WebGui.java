/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
*/
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
