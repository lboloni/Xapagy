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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.xapagy.agents.Agent;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.HtmlFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.observers.BreakObserver;
import org.xapagy.ui.observers.IAgentObserver;
import org.xapagy.ui.prettygraphviz.GraphVizHelper;
import org.xapagy.ui.prettygraphviz.pgvFocusInstances;
import org.xapagy.ui.prettygraphviz.pgvFocusVis;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwNavigationBar;
import org.xapagy.ui.prettyhtml.QueryHelper;
import org.xapagy.util.FileWritingUtil;

/**
 * 
 * This class is responsible for generating the page served in the web gui
 * (html, jpg or other)
 * 
 * @author Lotzi Boloni
 * 
 */
public class WgPageGenerator implements IQueryAttributes {

    /**
     * Copies the css file from the Xapagy directories to the destination
     * 
     * @param agent
     * @param dir
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void copyCSS(Agent agent, File dir)
            throws FileNotFoundException, IOException {
        URL url = HtmlFormatter.class.getResource("xapagy.css");
        StringBuffer buffer = new StringBuffer();
        String inputLine;
        try (BufferedReader in =
                new BufferedReader(new InputStreamReader(url.openStream()))) {
            while ((inputLine = in.readLine()) != null) {
                buffer.append(inputLine + "\n");
            }
            in.close();
        }
        File targetFile = new File(dir, "xapagy.css");
        FileWritingUtil.writeToTextFile(targetFile, buffer.toString());
    }

    private Agent agent;
    private File dir;
    private Session session = new Session();

    /**
     * @param agent
     * @throws IOException
     * @throws FileNotFoundException
     */
    public WgPageGenerator(Agent agent, File dir)
            throws FileNotFoundException, IOException {
        this.agent = agent;
        this.dir = dir;
        WgPageGenerator.copyCSS(agent, dir);
    }

    /**
     * Returns the graphviz image file for a specific query
     * 
     * @param gq
     * @param format
     *            it can be "jpg","pdf","eps","png" etc
     * @return
     * @throws IOException
     */
    public File createGraphVizImage(RESTQuery gq, String format)
            throws IOException {
        String fileRoot = "temp" + System.currentTimeMillis();
        String dot = null;
        String queryType = gq.getAttribute(Q_QUERY_TYPE);
        switch (queryType) {
        case "ALL_FOCUS_INSTANCES":
            dot = pgvFocusInstances.generate(agent);
            break;
        case "ALL_FOCUS_VERBINSTANCES":
            dot = pgvFocusVis.generate(agent);
            break;
        default: {
            TextUi.errorPrint(
                    "Query type " + queryType + " not supported here.");
            throw new Error("Query type " + queryType + " not supported here.");
        }
        }
        File fileDot = new File(dir, fileRoot + ".dot");
        FileWritingUtil.writeToTextFile(fileDot, dot);
        GraphVizHelper.dotToCompile(dir.toString(), fileRoot, format);
        // delete the unnecessary dot file, comment this out for debugging
        // fileDot.delete();
        return new File(dir, fileRoot + "." + format);
    }

    /**
     * @return the agent
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * @return the dir
     */
    public File getDir() {
        return dir;
    }

    /**
     * Returns the HTML (for a query) and adds a possible notification string
     * 
     * @param query
     * @return
     */
    public String getHtml(RESTQuery originalQuery, String notificationString) {
        // first if this is a command, handle it
        String commandtype = originalQuery.getAttribute(Q_COMMAND_TYPE);
        if ("ADVANCE_STEP".equals(commandtype)) {
            // if there is breakobserver
            TextUi.println("Number of observers:" + agent.getObservers().size());
            for (IAgentObserver iobs : agent.getObservers()) {
                if (iobs instanceof BreakObserver) {
                    synchronized (iobs) {
                        iobs.notify();
                    }
                }
            }
            // now, wait until we reach the new break on any of the break observers
            synchronized (this) {
                TextUi.print("Waiting for a break");
                observer: while (true) {
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TextUi.print(".");
                    for (IAgentObserver iobs : agent.getObservers()) {
                        if (iobs instanceof BreakObserver) {
                            BreakObserver bo = (BreakObserver) iobs;
                            // we reached a break... let us show the GUI
                            if (bo.isInBreak()) {
                                TextUi.println(bo.toString());
                                break observer;
                            }
                        }
                    }
                }
            }
        }

        //
        // create a local copy of the query which does not have the step command
        // if the query is empty, make it HISTORY
        //
        String queryType = originalQuery.getAttribute(Q_QUERY_TYPE);
        RESTQuery query = QueryHelper.copyWithEmptyCommand(originalQuery);
        if (queryType == null || queryType.equals("EMPTY")) {
            query.setAttribute(Q_QUERY_TYPE, "LOOP_HISTORY");
        }
        // update the color codes (it should be ok, repeated code)
        agent.getStoryLineRepository()
                .updateColorCodesWithFocus(session.colorCodeRepository);
        PwFormatter fmt = new PwFormatter("Xapagy: " + queryType);
        fmt.openHtml();
        fmt.openBody("onload=\"JavaScript:timedRefresh(5000);\"");
        PwNavigationBar.navigationBar(fmt, agent, query);
        // open the container for the rest
        fmt.openDiv("class=content");

        // add the message string
        if (notificationString != null) {
            // fmt.addPre(notificationString, "class=notification");
            fmt.addErrorMessage(notificationString);
        }
        String queryHandlerClassName = "org.xapagy.ui.queryhandlers.qh_"
                + query.getAttributes().get(Q_QUERY_TYPE);
        Class<?> ppClass = null;
        try {
            ppClass = Class.forName(queryHandlerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        IQueryHandler qh = null;
        try {
            qh = (IQueryHandler) ppClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        qh.generate(fmt, agent, query, session);
        // close the container for the content
        fmt.closeDiv();

        fmt.closeBody();
        fmt.closeHtml();
        return fmt.toString();
    }

}
