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
package org.xapagy.ui.prettygraphviz;

import java.io.File;
import java.io.IOException;

import org.xapagy.ui.TextUiHelper;

/**
 * 
 * <code>golyoscsapagy.ui.format.graphviz.GraphVizHelper</code>
 * 
 * variety of simple stuff for graphviz handler
 * 
 * @author Ladislau Boloni (lotzi.boloni@gmail.com)
 * Created on: Nov 27, 2008
 */

public class GraphVizHelper {

    /**
     * 
     */
    private static final String DEFAULT_LOCATION_DOT_LINUX = "/usr/bin/dot";
    /**
     * 
     */
    private static final String DEFAULT_LOCATION_DOT_WIN32 =
            "/program files/graphviz2.26.3/bin/dot.exe";
    /**
     * 
     */
    // private static final String DEFAULT_LOCATION_DOT_WIN64 =
    // "/program files (x86)/graphviz2.26.3/bin/dot.exe";
    // private static final String DEFAULT_LOCATION_DOT_WIN64 =
    // "/program files (x86)/graphviz 2.28/bin/dot.exe";
    private static final String DEFAULT_LOCATION_DOT_WIN64 =
            "/program files (x86)/graphviz/bin/dot.exe";

    /**
     * Adds a specific link to a StringBuffer. It happens betweeen two nodes
     * which are already recorded in the labelhandler
     * 
     * @param buffer
     * @param from
     * @param to
     * @param labelHandler
     * @param label
     * @param fontSize
     * @param otherParams
     */
    public static void addLink(StringBuffer buffer, Object from, Object to,
            LabelHandler labelHandler, String label, int fontSize,
            String otherParams) {
        String link =
                labelHandler.getLabel(from) + " -> "
                        + labelHandler.getLabel(to) + "[";
        if (label != null) {
            link = link + "label=" + label + " ";
        }
        link = link + " fontsize=\"" + fontSize + "\" ";
        if (otherParams != null) {
            link = link + otherParams;
        }
        link = link + " ];\n";
        buffer.append(link);
    }

    /**
     * Converts a dot file to an image file
     * 
     * @param fileName
     * @throws IOException
     */
    public static void
            dotToCompile(String dirName, String fileName, String type)
                    throws IOException {
        String params[] =
                { "temp", dirName + "/" + fileName + ".dot", "-o",
                        dirName + "/" + fileName + "." + type, "-T" + type };
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        // TextUi.println("os.name = " + osName);
        // TextUi.println("os.arch = " + System.getProperty("os.arch"));
        // TextUi.println("os.version = " + System.getProperty("os.version"));
        if (osName.equals("Linux")) {
            params[0] = GraphVizHelper.DEFAULT_LOCATION_DOT_LINUX;
        } else if (System.getProperty("os.name").contains("Windows")) {
            if (osArch.equals("x86")) {
                params[0] = GraphVizHelper.DEFAULT_LOCATION_DOT_WIN32;
            } else {
                params[0] = GraphVizHelper.DEFAULT_LOCATION_DOT_WIN64;
            }
        }
        File test = new File(params[0]);
        if (!test.exists()) {
            throw new Error("graphviz executable does not exist:" + params[0]);
        }
        Process p = Runtime.getRuntime().exec(params);
        synchronized (p) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                // interrupted exception: too bad
                e.printStackTrace();
            }
        }
        p.exitValue();
    }

    /**
     * Returns the passed string formatted appropriately for an identifier in
     * GraphViz
     * 
     * @param id
     * @return
     */
    public static String formatIdentifier(String id) {
        String tmp = id;
        tmp = tmp.replaceAll("\\.|-|\"", "_");
        return tmp;
    }

    /**
     * @param value
     * @return
     */
    public static String formatLabel(String value) {
        String tmp = value;
        // strip end of the line
        tmp = tmp.replaceAll("\n", "");
        // escape the quotation marks
        tmp = tmp.replaceAll("\"", "\\\\\"");
        return tmp;
    }

    /**
     * Creates a wrapped label for GraphViz nodes. After it performs the
     * wrapping, it replaces the newline with the appropriate escaped stuff such
     * that it will appear fine when set for the label
     * 
     * @param originalLabel
     * @param wrapWidth
     * @return
     */
    public static String wrapLabel(String originalLabel, int wrapWidth) {
        String label = TextUiHelper.wrap(originalLabel, 20);
        label = label.replaceAll("\n", "\\\\n");
        return label;
    }

}
