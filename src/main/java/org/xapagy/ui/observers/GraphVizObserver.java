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
package org.xapagy.ui.observers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettygraphviz.GraphVizHelper;
import org.xapagy.ui.prettygraphviz.pgvFocusInstances;
import org.xapagy.ui.prettygraphviz.pgvFocusVis;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.util.FileWritingUtil;

/**
 * @author Ladislau Boloni
 * Created on: Apr 8, 2012
 */
public class GraphVizObserver extends AbstractAgentObserver {

    /**
     * 
     */
    private static final long serialVersionUID = 183215714748683346L;

    /**
     * For each DebugEventType shows what graphs will be generated at specific
     * event types
     */
    private Map<DebugEventType, Set<String>> generateWhat = new HashMap<>();
    /**
     * The root directory in which the files will be written
     */
    private File rootDir = new File("output");

    /**
     * Adds a different type of thing to print at the given event
     * 
     * @param eventType
     * @param p
     */
    public void addGenerateWhat(DebugEventType eventType, String p) {
        Set<String> set = generateWhat.get(eventType);
        if (set == null) {
            set = new HashSet<>();
            generateWhat.put(eventType, set);
            observeWhat.add(eventType);
        }
        set.add(p);
    }

    /**
     * @param p
     * @param eventType
     * @throws IOException
     * @throws InterruptedException
     */
    private void generate(String p, DebugEventType eventType, File target)
            throws IOException, InterruptedException {
        switch (p) {
        case "ALL_FOCUS_INSTANCES": {
            String dot = pgvFocusInstances.generate(agent);
            FileWritingUtil.writeToTextFile(
                    new File(target.toString() + ".dot"), dot);
            GraphVizHelper.dotToCompile(target.getParent(), target.getName(),
                    "jpg");
            return;
        }
        case "ALL_FOCUS_VERBINSTANCES": {
            String dot = pgvFocusVis.generate(agent);
            FileWritingUtil.writeToTextFile(
                    new File(target.toString() + ".dot"), dot);
            GraphVizHelper.dotToCompile(target.getParent(), target.getName(),
                    "jpg");
            return;
        }
        default: {
            TextUi.println("Not implemented yet");
            throw new Error(
                    "GraphVizObserver: calling for an unimplemented printwhat "
                            + p);
        }
        }

    }

    /**
     * Generate the file name into which the outputs will be written.
     * 
     * @param p
     * @param eventType
     * @param time
     * @return
     */
    private File
            generateFile(String p, DebugEventType eventType, double time) {
        String temp =
                p.toString() + "_"
                        + Formatter.padTo(Formatter.fmt(time), 10, '_') + "_"
                        + eventType.toString();
        return new File(rootDir, temp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.AbstractAgentObserver#observe2(org.xapagy.debug
     * .DebugEvent)
     */
    @Override
    public void observeInner(DebugEvent event) throws IOException,
            InterruptedException {
        Set<String> generate = generateWhat.get(event.getEventType());
        for (String p : generate) {
            File f = generateFile(p, event.getEventType(), agent.getTime());
            generate(p, event.getEventType(), f);
        }
    }

}
