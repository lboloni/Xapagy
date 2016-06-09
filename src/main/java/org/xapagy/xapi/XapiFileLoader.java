/*
   This file is part of the Xapagy project
   Created on: Nov 26, 2015
 
   org.xapagy.xapi.XapiFileLoader
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.LoopItem;
import org.xapagy.ui.TextUi;

/**
 * This file contains functions that load a Xapi file and either add them to the
 * reading or other components of the loop or execute them right away.
 * 
 * @author Ladislau Boloni
 *
 */
public class XapiFileLoader {

    /**
     * Loads the items from the XapiFileLoader to the "reading list" of the
     * agent
     * 
     * @param file
     * @throws IOException
     */
    public static void loadFileToReading(Agent agent, File file, String marker)
            throws IOException {
        List<LoopItem> localReadings = loadFileToLoopItems(agent, file, marker);
        agent.getLoop().getReadings().addAll(0, localReadings);
    }

    /**
     * Takes a file of Xapi statements. Performs a minimal parsing of the
     * statements in the sense that it recognizes Xapagy sentences spread over
     * multiple lines and ignores // comments.
     * 
     * Then for each of the recognized statement, it creates a loop item that
     * refers back to the file name, and adds it to the returned list of
     * LoopItems
     * 
     * @param agent
     * @param file
     *            - the file from where the statements will be read
     * @param marker
     *            - the statements will be ignored until the marker had been
     *            reached
     * @return
     * @throws IOException
     */
    public static List<LoopItem> loadFileToLoopItems(Agent agent, File file,
            String marker) throws IOException {
        boolean ignore = true;
        if (marker == null) {
            ignore = false;
        } else {
            ignore = true;
        }
        List<LoopItem> localReadings = new ArrayList<>();
        try (FileReader fr = new FileReader(file);
                LineNumberReader in = new LineNumberReader(fr);) {
            String statement = "";
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                statement = statement + line;
                statement = statement.trim();
                if (XapiParser.isAComment(statement)) {
                    statement = "";
                    continue;
                }
                // meta statements are on a single line!!!
                if (statement.startsWith("$Marker ")) {
                    String currentMarker = statement.substring("$Marker ".length(), statement.length());
                    if (currentMarker.equals(marker)) {
                        ignore = false;
                        TextUi.println("ignore is false from here");
                    }
                    statement = "";
                    continue;
                }
                if (statement.startsWith("$")
                        || XapiParser.completeStatement(statement)) {
                    LoopItem reading = LoopItem.createReading(agent, statement,
                            file, in.getLineNumber());
                    // add the statements only if we are not in the ignore mode
                    if (!ignore) {
                        localReadings.add(reading);
                    }
                    statement = "";
                }
            }
            in.close();
            fr.close();
        } catch (FileNotFoundException fnfe) {
            TextUi.println("Could not find file:" + file.getAbsolutePath());
            throw new Error("Could not find file:" + file.getAbsolutePath());
        }
        return localReadings;
    }

}
