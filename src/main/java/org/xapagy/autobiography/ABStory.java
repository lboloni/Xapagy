/*
   This file is part of the Xapagy project
   Created on: Sep 16, 2012
 
   org.xapagy.autobiography.ABStory
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.autobiography;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.util.FileWritingUtil;

/**
 * Small utility class to create a story.
 * 
 * @author Ladislau Boloni
 * 
 */
public class ABStory {

    /**
     * Replacement of indexed values
     * 
     * Replaces the occurrence of prefix1, prefix2... with the terms specified
     * as a list
     * 
     * Change Aug 10, 2014 - replacement should happen on full words.
     * 
     * @param statement
     * @param prefix
     * @param terms
     * @return
     */
    private static String subs(String statement, String prefix,
            List<String> terms) {
        String retval = statement;
        for (int i = 0; i != terms.size(); i++) {
            String toReplace = prefix + (i + 1);
            String replacement = terms.get(i);
            // retval = retval.replaceAll(toReplace, replacement);
            retval = retval.replaceAll("\\b" + toReplace + "\\b", replacement);
        }
        return retval;
    }

    /**
     * Replacement of indexed values
     * 
     * Replaces the occurrence of prefix1, prefix2... with the terms
     * 
     * Change Aug 10, 2014 - replacement should happen on full words.
     * 
     * @param statement
     * @param prefix
     * @param terms
     * @return
     */
    private static String subs(String statement, String prefix,
            String... terms) {
        String retval = statement;
        for (int i = 0; i != terms.length; i++) {
            String toReplace = prefix + (i + 1);
            String replacement = terms[i];
            // retval = retval.replaceAll(toReplace, replacement);
            // this makes the replacement happen at a word boundary
            retval = retval.replaceAll("\\b" + toReplace + "\\b", replacement);
            // retval = retval.replaceAll(" " + toReplace + " ", " " +
            // replacement + " ");
        }
        return retval;
    }

    private boolean isolated = false;
    private List<String> lines = new ArrayList<>();

    /**
     * Constructor which creates an empty story
     */
    public ABStory() {
        // nothing here
    }

    /**
     * Copy constructor, deep copy
     * 
     * @param abs
     */
    public ABStory(ABStory abs) {
        this.lines.addAll(abs.lines);
    }

    /**
     * Constructor for creating an ABStory object from an in-memory 
     * multiline text
     * @param text
     */
    public ABStory(String text) {
        //String[] separated = text.replaceAll("\\r", "\\n")
        //        .replaceAll("\\n{2,}", "\\n").split("\\n");
        String[] separated = text.split("\\n");
        for(String line: separated) {
            this.lines.add(line);
        }
    }

    /**
     * Creates an ABStory from a file
     * 
     * @param file
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public ABStory(File file) throws FileNotFoundException, IOException {
        try (FileReader fr = new FileReader(file);
                LineNumberReader in = new LineNumberReader(fr);) {
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                add(line);
            }
        }
    }

    /**
     * 
     * Constructor the creates the story based on a line-by-line list of stories
     * 
     * @param lines
     */
    public ABStory(List<String> lines) {
        this.lines.addAll(lines);
    }

    /**
     * Adds another story
     * 
     * @param story
     */
    public void add(ABStory story) {
        lines.addAll(story.lines);
    }

    /**
     * Adds a line of text
     * 
     * @param text
     */
    public void add(String text) {
        lines.add(text);
    }

    /**
     * Deletes the specified line - if it is positive, start from beginning, if
     * negative, from the end
     */
    public void deleteLine(int lineToDelete) {
        if (lineToDelete >= 0) {
            lines.remove(lineToDelete);
        } else {
            lines.remove(lines.size() + lineToDelete);
        }
    }

    /**
     * Deletes the line specified in the search string
     */
    public void deleteLine(String findText) {
        int lineToDelete = find(findText);
        if (lineToDelete == -1) {
            throw new Error("Cannot find what line to delete!");
        }
        deleteLine(lineToDelete);
    }

    /**
     * Returns the index of the line which contains the specified string
     */
    public int find(String findText) {
        for (int i = 0; i != lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains(findText)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a specific line of the story
     * 
     * @param i
     * @return
     */
    public String getLine(int i) {
        return lines.get(i);
    }

    /**
     * Returns all the lines of the story in a List. This should not be normally
     * used, but it is useful during the process of translating the existing
     * code to the ones which use ABStory for everything.
     * 
     * @return
     */
    public List<String> getLines() {
        List<String> retval = new ArrayList<>();
        retval.addAll(lines);
        return retval;
    }

    /**
     * Inserts a new line after the specified one. Valid ranges include -1 to
     * lines.size()-1
     * 
     * @param insertPoint
     * @param line
     */
    public void insertAfter(int insertPoint, String line) {
        lines.add(insertPoint + 1, line);
    }

    /**
     * Inserts a line after the one specified in the search string
     * 
     * @param insertPoint
     * @param line
     */
    public void insertAfter(String findText, String line) {
        int insertPoint = find(findText);
        if (insertPoint == -1) {
            throw new Error("Cannot find where to insert!");
        }
        insertAfter(insertPoint, line);
    }

    /**
     * @return the isolated
     */
    public boolean isIsolated() {
        return isolated;
    }

    /**
     * Isolates the story by generating a new scene at the beginning, and adding
     * a long delay at the end
     */
    public void isolate() {
        if (isolated) {
            return;
        }
        //String startLine = "NewSceneOnly #isolated"
        //        + System.currentTimeMillis() + ", none";
        String isolationSceneLabel = "#isolated" + System.currentTimeMillis() ;
        String startLine = "$CreateScene " + isolationSceneLabel + " CloseOthers";
        String endLine = "----";
        lines.add(0, startLine);
        lines.add(endLine);
        isolated = true;
    }

    /**
     * Returns the length of the story
     * 
     * @return
     */
    public int length() {
        return lines.size();
    }

    /**
     * Moves a line from the "from" location to the "to" location
     * 
     * @param from
     *            - which line to move
     * @param toAfter
     *            - after which line to move it
     */
    public void move(int from, int toAfter) {
        if (from == toAfter) {
            return;
        }
        String lineFlip = getLine(from);
        deleteLine(from);
        if (toAfter > from) {
            insertAfter(toAfter - 1, lineFlip);
        } else {
            insertAfter(toAfter, lineFlip);
        }
    }


    /**
     * Replaces the occurrence of prefix1, prefix2... with the terms for all the
     * lines of the story
     * 
     * @param statement
     * @param prefix
     * @param terms
     */
    public void subs(String prefix, List<String> terms) {
        List<String> newStory = new ArrayList<>();
        for (String statement : lines) {
            newStory.add(ABStory.subs(statement, prefix, terms));
        }
        lines = newStory;
    }

    /**
     * Replaces the occurrence of prefix1, prefix2... with the terms for all the
     * lines of the story
     * 
     * @param statement
     * @param prefix
     * @param terms
     */
    public void subs(String prefix, String... terms) {
        List<String> newStory = new ArrayList<>();
        for (String statement : lines) {
            newStory.add(ABStory.subs(statement, prefix, terms));
        }
        lines = newStory;
    }

    /**
     * Creates a new substory based on a subset of the existing story.
     * 
     * @param from
     * @param to
     * @return
     */
    public ABStory substory(int from, int to) {
        List<String> sst = lines.subList(from, to + 1);
        return new ABStory(sst);
    }

    /**
     * Converts the story to a text
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (String line : lines) {
            buffer.append(line + "\n");
        }
        return buffer.toString();
    }

    /**
     * Creates a new substory by cutting off the last count items from the end
     * of the story
     * 
     * @param from
     * @param to
     * @return
     */
    public ABStory withoutEnd(int count) {
        List<String> sst = lines.subList(0, lines.size() - count);
        return new ABStory(sst);
    }

    /**
     * Saves the story to a file 
     * @param filename
     * @throws IOException
     */
    public void saveTo(String filename) throws IOException {
        File file = new File(filename);
        FileWritingUtil.writeToTextFile(file, toString());
    }
    
}
