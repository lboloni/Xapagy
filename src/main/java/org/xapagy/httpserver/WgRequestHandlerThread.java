package org.xapagy.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import org.xapagy.agents.Agent;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyhtml.IQueryAttributes;

/**
 * The request handler. Essentially, this will parse the query into a GsQuery,
 * then try to satisfy it.
 * 
 * @author Lotzi Boloni
 * 
 */
public class WgRequestHandlerThread implements Runnable, IQueryAttributes {
    final static String CRLF = "\r\n";

    /**
     * Creates the HTTP content-type parameter based on the extension of the
     * file
     * 
     * @param fileName
     * @return
     */
    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")
                || fileName.endsWith(".txt")) {
            return "text/html";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * Sends the bytes of a given file
     * 
     * @param fis
     * @param os
     * @throws Exception
     */
    private static void sendBytes(FileInputStream fis, OutputStream os)
            throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }

    private BufferedReader br;
    private WgPageGenerator gfs;
    // private InputStream input;
    private OutputStream output;
    private Socket socket;

    public WgRequestHandlerThread(Socket socket, WgPageGenerator gfs)
            throws Exception {
        this.socket = socket;
        // this.input = socket.getInputStream();
        this.output = socket.getOutputStream();
        this.br =
                new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
        this.gfs = gfs;
    }

    /**
     * Processes a request
     * 
     * @throws Exception
     */
    private void processRequest() throws Exception {
        while (true) {
            String headerLine = br.readLine();
            // TextUi.println("HEADERLINE: " + headerLine);
            if (headerLine == null
                    || headerLine.equals(WgRequestHandlerThread.CRLF)
                    || headerLine.equals("")) {
                break;
            }
            StringTokenizer s = new StringTokenizer(headerLine);
            String temp = s.nextToken();
            if (temp.equals("GET")) {
                String query = s.nextToken();
                File theFile = null;
                // is it the css?
                if (query.contains("xapagy.css")) {
                    theFile = new File(gfs.getDir(), "xapagy.css");
                    serveFile(theFile, gfs.getAgent());
                    return;
                }
                RESTQuery gq = null;
                try {
                    query = query.substring(1);
                    gq = RESTQuery.parseQueryString(query);
                } catch (Error er) {
                    // TextUi.println("Error catched here, should answer with error");
                    if (!"".equals(query)) {
                        serveError(gfs.getAgent(),
                                "Error, incorrect query string");
                    } else {
                        // this is the case of the empty
                        gq = new RESTQuery();
                        gq.setAttribute(Q_QUERY_TYPE, "EMPTY");
                        gq.setAttribute(Q_RESULT_TYPE, "HTML");
                        gq.setAttribute(Q_AGENT_NAME, gfs.getAgent().getName());
                        String html = gfs.getHtml(gq, null);
                        serveHtml(html);                        
                    }
                    continue;
                }
                switch (gq.getAttribute(Q_RESULT_TYPE)) {
                case "JPG": {
                    theFile = gfs.createGraphVizImage(gq, "jpg");
                    serveFile(theFile, gfs.getAgent());
                    // delete the file - this
                    theFile.delete();
                    break;
                }
                case "HTML": {
                    String html = gfs.getHtml(gq, null);
                    serveHtml(html);
                    break;
                }
                case "PDF": {
                    theFile = gfs.createGraphVizImage(gq, "pdf");
                    serveFile(theFile, gfs.getAgent());
                    // delete the file - this
                    // theFile.delete();
                    break;
                }
                case "EPS": {
                    theFile = gfs.createGraphVizImage(gq, "eps");
                    serveFile(theFile, gfs.getAgent());
                    // delete the file - this
                    // theFile.delete();
                    break;
                }
                default: {
                    TextUi.abort("Don't know how to serve result type");
                }
                }
            }
        }

        try {
            output.close();
            br.close();
            socket.close();
        } catch (Exception e) {
        }
    }

    /**
     * Runs the request handler
     */
    @Override
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an error message, and then serves it
     * 
     * @param errorText
     * @throws IOException
     */
    private void serveError(Agent agent, String errorText) throws IOException {
        RESTQuery gq = new RESTQuery();
        gq.setAttribute(Q_AGENT_NAME, agent.getName());
        gq.setAttribute(Q_QUERY_TYPE, "EMPTY");
        String html = gfs.getHtml(gq, errorText);
        serveHtml(html);
    }

    /**
     * Serves a file from the local file system
     * 
     * --- it is a little bit self contained because of the way it happened due
     * to the refactoring
     * 
     * @throws Exception
     * 
     */
    private void serveFile(File theFile, Agent agent) throws Exception {
        if (theFile == null) {
            serveError(agent, "File passed to serveFile was null");
            return;
        }
        try (FileInputStream fis = new FileInputStream(theFile)) {

            String serverLine = "Server: Xapagy browser";
            String statusLine = null;
            String contentTypeLine = null;
            String contentLengthLine = "error";
            statusLine = "HTTP/1.0 200 OK" + WgRequestHandlerThread.CRLF;
            contentTypeLine =
                    "Content-type: "
                            + WgRequestHandlerThread.contentType(theFile
                                    .getName()) + WgRequestHandlerThread.CRLF;
            contentLengthLine =
                    "Content-Length: "
                            + new Integer(fis.available()).toString()
                            + WgRequestHandlerThread.CRLF;
            output.write(statusLine.getBytes());
            output.write(serverLine.getBytes());
            output.write(contentTypeLine.getBytes());
            output.write(contentLengthLine.getBytes());
            // Send a blank line to indicate the end of the header lines.
            output.write(WgRequestHandlerThread.CRLF.getBytes());
            WgRequestHandlerThread.sendBytes(fis, output);
            fis.close();
        } catch (FileNotFoundException e) {
            serveError(agent, "File not found");
            return;
        }
    }

    /**
     * Serves an error message --- it is a little bit self contained because of
     * the way it happened due to the refactoring
     * 
     * @param string
     * 
     * @throws IOException
     */
    private void serveHtml(String htmlText) throws IOException {
        // TextUi.println("Handling ERROR!!!");
        String serverLine = "Server: Xapagy browser";
        String statusLine = null;
        String contentTypeLine = null;
        String contentLengthLine = "error";
        statusLine = "HTTP/1.0 200 OK" + WgRequestHandlerThread.CRLF;
        // statusLine = "HTTP/1.0 404 Not Found" + CRLF;
        contentTypeLine = "text/html";
        contentLengthLine =
                "Content-Length: " + new Integer(htmlText.length()).toString()
                        + WgRequestHandlerThread.CRLF;
        output.write(statusLine.getBytes());
        output.write(serverLine.getBytes());
        output.write(contentTypeLine.getBytes());
        output.write(contentLengthLine.getBytes());
        // Send a blank line to indicate the end of the header lines.
        output.write(WgRequestHandlerThread.CRLF.getBytes());
        output.write(htmlText.getBytes());
        output.write(WgRequestHandlerThread.CRLF.getBytes());
    }

}
