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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.xapagy.ui.TextUi;

/**
 * Web style server which handles the GUI
 * 
 * @author Lotzi Boloni
 * 
 */
public class WgServerThread implements Runnable {

    /**
     * Start a locking server
     * 
     * @param port
     * @param gfs
     */
    public static void startServer(int port, WgPageGenerator gfs) {
        WgServerThread guiserver = new WgServerThread(port, gfs);
        guiserver.start();
        guiserver.run();
    }

    private WgPageGenerator gfs;
    private int port;

    private ServerSocket serverSocket = null;

    /**
     * Starts the server for a particular agent.
     * 
     * @param port
     * @param agent
     */
    public WgServerThread(int port, WgPageGenerator gfs) {
        this.port = port;
        this.gfs = gfs;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                // TextUi.println("New connection accepted "
                // + socket.getInetAddress() + ":" + socket.getPort());
                // Construct handler to process the HTTP request message.
                try {
                    WgRequestHandlerThread request =
                            new WgRequestHandlerThread(socket, gfs);
                    // Create a new thread to process the request.
                    Thread thread = new Thread(request);
                    // Start the thread.
                    thread.start();
                } catch (Exception e) {
                    TextUi.println("here" + e);
                }
            }

        } catch (IOException iex) {
            TextUi.println("Exception was: " + iex);
        }
    }

    /**
     * Starts the server
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            TextUi.println("GuiServer running on port "
                    + serverSocket.getLocalPort());
        } catch (IOException e) {
            TextUi.println("Could not start the GUI at port " + port + " --- probably another instance of Xapagy is running at this machine!");
            TextUi.abort("Terminating now");
        }
    }
}
