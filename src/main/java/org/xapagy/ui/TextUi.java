/*
   This file is part of the Golyoscsapagy project
   Created on: Nov 25, 2008
 
   golyoscsapagy.ui.format.graphviz.IToGraphViz
 
   Copyright (c) 2008 Ladislau Boloni
 */
package org.xapagy.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xapagy.util.Glob;

public class TextUi {
    private static final String ADD_ALL = "ADD ALL";
    public static final int CHOICE_ABORT = 3;
    public static final int CHOICE_NO = 2;
    public static final int CHOICE_YES = 1;
    private static final String DONE = "DONE";
    private static final String EMPTY = "Empty";
    public static PrintStream errorWriter = System.err;
    static InputStreamReader isr = new InputStreamReader(System.in);
    static BufferedReader reader = new BufferedReader(TextUi.isr);
    private static final String RESTART = "RESTART";
    public static TimeCounter timeCounter = new TimeCounter();
    private static boolean writeDebugMessage = true;
    public static PrintStream writer = System.out;

    /**
     * A function for aborting the program and returning the error. It will also
     * print the passed parameter on the error stream
     * 
     * @param string
     */
    public static void abort(String string) {
        String header = TextUiHelper.createHeader(string);
        TextUi.errorPrint(header);
        Error e = new Error();
        e.printStackTrace();
        TextUi.errorPrint(header);
        System.exit(1);

    }

    /**
     * Simple query for a yes/no confirmation
     * 
     * @param string
     * @return
     */
    public static boolean
            confirm(String initprompt, final boolean defaultChoice) {
        String prompt = TextUi.processQuestionPrompt(initprompt);
        while (true) {
            if (defaultChoice) {
                TextUi.writer.print(prompt + " ([y]/n): ");
            } else {
                TextUi.writer.print(prompt + " (y/[n]): ");
            }
            try {
                final String value = TextUi.reader.readLine();
                if (value.equals("")) {
                    return defaultChoice;
                }
                if (value.equals("y") || value.equals("Y")) {
                    return true;
                }
                if (value.equals("n") || value.equals("N")) {
                    return false;
                }
                TextUi.println("Incorrect value entered, use y or n!");
            } catch (final Exception ex) {
                // should never happen
                TextUi.errorPrint(ex.toString());
                return false;
            }
        }
    }

    /**
     * Writes a debug message to the error output. It can be turned on and off
     * with setDebugMessages
     * 
     * @param message
     */
    public static void debug(final String message) {
        if (TextUi.writeDebugMessage) {
            TextUi.errorPrint("DEBUG:" + message);
        }
    }

    /**
     * Often, the program is finished but it needs to be kept around such that
     * it can be debugged. This function waits for a keypress and then
     * terminates the program.
     */
    public static void enterToTerminate() {
        TextUi.println("Active part done, press <Enter> to terminate.");
        try {
            TextUi.reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextUi.println("Ok, program terminated.");
        System.exit(0);
    }

    /**
     * @param string
     */
    public static void errorPrint(final String string) {
        TextUi.errorWriter.println(string);
    }

    /**
     * @param string
     * @return
     */
    public static boolean inputBoolean(String prompt) {
        return TextUi.inputBoolean(prompt, false);
    }

    /**
     * @param string
     * @return
     */
    public static boolean inputBoolean(String initprompt, boolean oldValue) {
        String prompt =
                TextUi.processPrompt(initprompt + " [" + oldValue + "]");
        TextUi.writer.print(prompt);
        try {
            final String value = TextUi.reader.readLine();
            if (value.equals("y") || value.equals("Y")) {
                return true;
            }
            if (value.equals("")) {
                return oldValue;
            }
            return false;
        } catch (final Exception ex) {
            // should never happen
            TextUi.errorPrint(ex.toString());
            return false;
        }
    }

    /**
     * @param string
     * @param oldValue
     */
    public static Date inputDate(final String prompt) {
        return TextUi.inputDate(prompt, null);
    }

    /**
     * Inputs the date
     * 
     * @param prompt
     * @param oldValue
     * @return
     */
    public static Date inputDate(String initprompt, final Date oldValue) {
        String prompt = TextUi.processPrompt(initprompt);
        if (oldValue != null) {
            TextUi.println(prompt + oldValue);
            TextUi.println("<ENTER> to keep the old value");
        }
        while (true) {
            try {
                TextUi.writer.print(prompt);
                final String propertyValue = TextUi.reader.readLine();
                if (propertyValue.equals("") && oldValue != null) {
                    return oldValue;
                }
                // return DataTypeParserHelper.parseStringToDate(propertyValue);
            } catch (final Exception ex) {
                // should never happen
                TextUi.errorPrint(ex.toString());
                return null;
            }
        }
    }

    /**
     * @param string
     * @return
     */
    public static File inputDestinationFile(final String prompt) {
        return TextUi.inputFile(prompt, false);
    }

    /**
     * Inputs a directory through iterative navigation
     * 
     * @param prompt
     * @return
     */
    public static File inputDirectory(final String prompt) {
        File currentDir = new File(".");
        currentDir = currentDir.getAbsoluteFile();
        while (true) {
            final List<String> menuItems = new ArrayList<>();
            final File dirs[] = currentDir.listFiles();
            for (int i = 0; i != dirs.length; i++) {
                if (dirs[i].isDirectory()) {
                    menuItems.add(dirs[i].getName());
                }
            }
            menuItems.add("-");
            menuItems.add("UP");
            menuItems.add("HOME");
            menuItems.add("CANCEL");
            menuItems.add("DIRECT ENTRY");
            menuItems.add("ACCEPT");
            final String value =
                    TextUi.menu(menuItems, null, prompt + "\nCurrent dir is:"
                            + currentDir + "\nNavigate directory: ");
            if (value.equals("UP")) {
                currentDir = currentDir.getParentFile();
                continue;
            }
            if (value.equals("HOME")) {
                currentDir =
                        new File(java.lang.System.getProperty("user.home"));
                continue;
            }
            if (value.equals("ACCEPT")) {
                return currentDir;
            }
            if (value.equals("CANCEL")) {
                return null;
            }
            if (value.equals("DIRECT ENTRY")) {
                try {
                    TextUi.writer.print("Type in directory: ");
                    final String propertyValue = TextUi.reader.readLine();
                    final File newDir = new File(propertyValue);
                    if (newDir.isDirectory()) {
                        currentDir = newDir;
                    }
                    TextUi.println("Not a directory: " + newDir);
                } catch (final IOException ex) {
                    // should never happen
                    TextUi.errorPrint(ex.toString());
                    return null;
                }
            }
            currentDir = new File(currentDir, value);
        }
    }

    /**
     * Syntactic sugar, no default
     * 
     * @param string
     * @param oldValue
     */
    public static Double inputDouble(final String prompt) {
        return TextUi.inputDouble(prompt, null);
    }

    /**
     * Inputs a double value
     * 
     * @param prompt
     * @param oldValue
     * @return
     */
    public static Double inputDouble(String initprompt, final Double oldValue) {
        String prompt = TextUi.processPrompt(initprompt);
        if (oldValue != null) {
            TextUi.println(prompt + oldValue);
            TextUi.println("<ENTER> to keep the old value");
        }
        while (true) {
            try {
                TextUi.print(prompt);
                final String value = TextUi.reader.readLine();
                if (value.equals("") && oldValue != null) {
                    return oldValue;
                }
                return Double.valueOf(value);
            } catch (final NumberFormatException nfex) {
                TextUi.println("Invalid value, enter an integer!");
            } catch (final Exception ex) {
                // should never happen
                TextUi.errorPrint(ex.toString());
                return null;
            }
        }
    }

    /**
     * Customized for source files - files from which you want to read
     * 
     * @param prompt
     * @return
     */
    public static File inputExistingFile(final String prompt) {
        return TextUi.inputFile(prompt, true);
    }

    /**
     * Allows to input a file name, doing various checks
     * 
     * @param prompt
     * @param checkForExistence
     *            - if true, it checks for the existence of the file
     * @return
     */
    public static File inputFile(String initprompt,
            final boolean checkForExistence) {
        String prompt = TextUi.processPrompt(initprompt);
        while (true) {
            try {
                TextUi.writer.print(prompt);
                final String propertyValue = TextUi.reader.readLine();
                if (propertyValue.equals("")) {
                    return null;
                }
                final File theFile = new File(propertyValue);
                if (!checkForExistence || theFile.exists()) {
                    return theFile;
                }
                TextUi.println("Unexistent file, check the spelling!");
            } catch (final IOException ex) {
                // should never happen
                TextUi.errorPrint(ex.toString());
                return null;
            }
        }
    }

    /**
     * 
     * Inputs a file with navigation
     * 
     * @return
     */
    public static File inputFileWithNavigation(final String startDir,
            final String pattern) {
        File currentDir = new File(startDir);
        if (!currentDir.exists()) {
            TextUi.errorPrint("The specified directory " + startDir
                    + " does not exist, starting in current dir");
            currentDir = new File(".");
        }
        currentDir = currentDir.getAbsoluteFile();
        while (true) {
            final List<String> menuItems = new ArrayList<>();
            final File dirs[] = currentDir.listFiles();
            for (int i = 0; i != dirs.length; i++) {
                if (dirs[i].isDirectory()) {
                    menuItems.add(dirs[i].getName());
                }
            }
            menuItems.add("-");
            for (int i = 0; i != dirs.length; i++) {
                if (!dirs[i].isDirectory()
                        & Glob.match(pattern, dirs[i].getName())) {
                    menuItems.add(dirs[i].getName());
                }
            }
            menuItems.add("-");
            menuItems.add("UP");
            menuItems.add("HOME");
            menuItems.add("CANCEL");
            final String value =
                    TextUi.menu(menuItems, null, "Current dir is:" + currentDir
                            + "\nNavigate directory or select the file: ");
            if (value.equals("HOME")) {
                currentDir =
                        new File(java.lang.System.getProperty("user.home"));
                continue;
            }
            if (value.equals("UP")) {
                currentDir = currentDir.getParentFile();
                continue;
            }
            if (value.equals("CANCEL")) {
                return null;
            }
            currentDir = new File(currentDir, value);
            if (currentDir.isFile()) {
                return currentDir;
            }
        }
    }

    /**
     * Syntactic sugar, no default
     * 
     * @param string
     * @param oldValue
     */
    public static Integer inputInteger(final String prompt) {
        return TextUi.inputInteger(prompt, null);
    }

    /**
     * Inputs an integer value
     * 
     * @param prompt
     * @param oldValue
     * @return
     */
    public static Integer
            inputInteger(String initprompt, final Integer oldValue) {
        String prompt = TextUi.processPrompt(initprompt);
        if (oldValue != null) {
            TextUi.println(prompt + oldValue);
            TextUi.println("<ENTER> to keep the old value");
        }
        while (true) {
            try {
                TextUi.print(prompt);
                final String value = TextUi.reader.readLine();
                if (value.equals("") && oldValue != null) {
                    return oldValue;
                }
                return Integer.valueOf(value);
            } catch (final NumberFormatException nfex) {
                TextUi.writer.print("Invalid value, enter an integer number");
            } catch (final Exception ex) {
                // should never happen
                TextUi.errorPrint(ex.toString());
                return null;
            }
        }
    }

    /**
     * @param string
     * @param oldValue
     */
    public static List<String> inputList(final String prompt) {
        return TextUi.inputList(prompt, null);
    }

    /**
     * Dynamically edits a list of strings
     * 
     * @param prompt
     * @param oldValue
     * @return
     */
    public static List<String> inputList(String initprompt,
            List<String> oldValueInit) {
        String prompt = TextUi.processPrompt(initprompt);
        List<String> oldValue;
        if (oldValueInit == null) {
            oldValue = new ArrayList<>();
        } else {
            oldValue = oldValueInit;
        }
        while (true) {
            try {
                TextUi.printListWithPrompt(prompt, oldValue);
                final String propertyValue = TextUi.reader.readLine();
                if (propertyValue.equals("")) {
                    return oldValue;
                }
                if (propertyValue.startsWith("+")) {
                    oldValue.add(propertyValue.substring(1));
                    continue;
                }
                if (propertyValue.startsWith("-")) {
                    final String number = propertyValue.substring(1);
                    final int num = Integer.parseInt(number);
                    oldValue.remove(num);
                    continue;
                }
                // parse a new one
                // oldValue =
                // DataTypeParserHelper.parseStringToList(propertyValue);
            } catch (final Exception ex) {
                // should never happen
                TextUi.errorPrint(ex.toString());
                return null;
            }
        }
    }

    /**
     * @param parentDirectory
     * @param oldValues
     * @return
     */
    public static List<String> inputListOfFileNames(final File parentDirectory,
            final List<String> oldValues) {
        List<String> newValues = new ArrayList<>(oldValues);
        final List<String> possibleValues = new ArrayList<>();
        if (!parentDirectory.isDirectory()) {
            throw new Error("Call this method with an existing directory!");
        }
        // fill the Vector with the (top level) files
        final File content[] = parentDirectory.listFiles();
        for (int i = 0; i != content.length; i++) {
            if (content[i].isDirectory()) {
                continue;
            }
            possibleValues.add(content[i].getName());
        }
        // create a menu with the possible values
        while (true) {
            ArrayList<String> menuItems = new ArrayList<>();
            menuItems.addAll(possibleValues);
            // remove the ones already in the collection
            for (final String string : newValues) {
                if (menuItems.contains(string)) {
                    menuItems.remove(string);
                }
            }
            menuItems.add("-");
            menuItems.add(TextUi.RESTART);
            menuItems.add(TextUi.EMPTY);
            menuItems.add(TextUi.DONE);
            final String result = TextUi.menu(menuItems, null, null);
            if (result.equals(TextUi.RESTART)) {
                newValues = new ArrayList<>(oldValues);
            }
            if (result.equals(TextUi.DONE)) {
                return newValues;
            }
            if (result.equals(TextUi.EMPTY)) {
                newValues = new ArrayList<>();
            }
        }
    }

    /**
     * @param string
     * @return
     */
    public static String inputPassword(final String string) {
        return TextUi.inputString(string);
    }

    /**
     * <p>
     * Inputs a string from the command line, without a default value
     * 
     * @param string
     * @param oldValue
     */
    public static String inputString(final String prompt) {
        return TextUi.inputString(prompt, null);
    }

    /**
     * <p>
     * Inputs a string from the command line, prompting for the default. ENTER
     * maintains the default
     * 
     * @param prompt
     * @param oldValue
     * @return
     */
    public static String
            inputString(String promptPassed, final String oldValue) {
        String prompt = TextUi.processPrompt(promptPassed);
        if (oldValue != null) {
            TextUi.println(prompt + oldValue);
            TextUi.println("Press <ENTER> to keep");
        }
        TextUi.writer.print(prompt);
        try {
            String value = TextUi.reader.readLine();
            if (value.equals("") && oldValue != null) {
                value = oldValue;
            }
            if (value.startsWith("+")) {
                value = oldValue + value.substring(1);
            }
            return value;
        } catch (final Exception ex) {
            // should never happen
            TextUi.errorPrint(ex.toString());
            return null;
        }
    }

    /**
     * Creates an interactive menu with the specified menu items and prompt.
     * Returns the index in the menuitems.
     * 
     * It can select by numbers, by substring etc.
     * 
     * @param menuitems
     * @param defaultItem
     *            the default item or -1 if it is not one of them
     * @param prompt
     * @return
     */
    public static int menu(final List<String> menuitems, int defaultItem,
            final String prompt) {
        String thePrompt = null;
        String value = null;
        // final HashMap<Integer, String> map = new HashMap<Integer, String>();
        int i = 0;
        if (prompt != null) {
            thePrompt = prompt;
        } else {
            thePrompt = "Enter your choice: ";
        }
        while (true) {
            i = 0;
            TextUi.printLabeledSeparator("-");
            for (final String item : menuitems) {
                if (item.startsWith("-")) {
                    TextUi.printLabeledSeparator(item);
                } else {
                    i++;
                    if (i == defaultItem + 1) {
                        TextUi.println("[" + i + "]\t" + item);
                    } else {
                        TextUi.println(" " + i + " \t" + item);
                    }
                }
            }
            TextUi.printLabeledSeparator("-");
            try {
                TextUi.print(thePrompt);
                value = TextUi.reader.readLine();
                if (value.equals("")) {
                    if (defaultItem != -1) {
                        return defaultItem;
                    } else {
                        continue;
                    }
                }
                final Integer val = Integer.valueOf(value);
                if (val < 1 || val > menuitems.size()) {
                    TextUi.writer.println("Invalid value, try again!");
                    continue;
                }
                return val - 1;
            } catch (final IOException ioex) {
                throw new Error("Input output exception!");
            } catch (final NumberFormatException nfex) {
                int selectedItem = -1;
                int countmatch = 0;
                for (int k = 0; k != menuitems.size(); k++) {
                    String menuitem = menuitems.get(k);
                    if (menuitem.toLowerCase().indexOf(value.toLowerCase()) != -1) {
                        selectedItem = k;
                        countmatch++;
                    }
                }
                if (countmatch == 1) {
                    return selectedItem;
                }
                if (countmatch > 1) {
                    TextUi.println(value + " matches more than one menu items!");
                    continue;
                }
                if (countmatch == 0) {
                    TextUi.println(value
                            + " does not match any of the menu items!");
                    continue;
                }
            }
        }
    }

    /**
     * Syntactic sugar: the old, string based version of menu item
     * 
     * @param menuitems
     * @param defaultItem
     * @param prompt
     * @return
     */
    public static String menu(final List<String> menuitems, String defaultItem,
            final String prompt) {
        int defaultNumber = -1;
        if (defaultItem != null) {
            for (int i = 0; i != menuitems.size(); i++) {
                if (menuitems.get(i).equals(defaultItem)) {
                    defaultNumber = i;
                }
            }
            if (defaultNumber == -1) {
                TextUi.errorPrint("Default item: " + defaultItem
                        + " does not exist in the list!");
            }
        }
        int retval = TextUi.menu(menuitems, defaultNumber, prompt);
        if (retval == -1) {
            return null;
        } else {
            return menuitems.get(retval);
        }
    }

    private static List<String> prepareChoices(final List<String> currentList,
            final List<String> choices) {
        final List<String> currentChoices = new ArrayList<>();
        for (final String string : choices) {
            final String item = string;
            if (!currentList.contains(item)) {
                currentChoices.add(item);
            }
        }
        currentChoices.add("-");
        currentChoices.add(TextUi.RESTART);
        currentChoices.add(TextUi.ADD_ALL);
        currentChoices.add(TextUi.DONE);
        return currentChoices;
    }

    public static void print(final String text) {
        TextUi.writer.print(text);
    }

    /**
     * Prints a header
     * 
     * @param text
     */
    public static void printHeader(final String text) {
        TextUi.print(TextUiHelper.createHeader(text));
    }

    /**
     * Prints a labeled separator
     * 
     * @param label
     */
    public static void printLabeledSeparator(final String label) {
        TextUi.print(TextUiHelper.createLabeledSeparator(label));
    }

    /**
     * Helper function for inputList
     * 
     * @param prompt
     * @param value
     */
    private static void printListWithPrompt(final String prompt,
            final List<String> value) {
        TextUi.println(prompt);
        int i = 0;
        for (final String element : value) {
            TextUi.println("" + i + "\t" + element);
            i++;
        }
        TextUi.println("<ENTER> to keep the old value, \n+newvalue to add new value, \n-number to remove one of the elements\nor comma separated list for direct entry");
    }

    public static void println(final Object o) {
        TextUi.writer.println(o.toString());
    }

    public static void println(final String text) {
        TextUi.writer.println(text);
    }

    /**
     * Utility function to make the prompts look better even if the user did not
     * pass something pretty.
     * 
     * @param prompt
     * @return
     */
    private static String processPrompt(final String prompt) {
        if (prompt.endsWith("=") || prompt.endsWith(":")
                || prompt.endsWith("= ")) {
            return prompt;
        }
        return prompt + " = ";
    }

    /**
     * Utility function to make the question look better even if the user did
     * not pass something pretty.
     * 
     * @param prompt
     * @return
     */
    private static String processQuestionPrompt(final String prompt) {
        if (prompt.endsWith("?") || prompt.endsWith("? ")) {
            return prompt;
        }
        return prompt + "?";
    }

    public static List<String> selectFromChoices(final List<String> oldList,
            final List<String> choices) {
        List<String> currentList = new ArrayList<>(oldList);
        while (true) {
            final List<String> currentChoices =
                    TextUi.prepareChoices(currentList, choices);
            final String prompt =
                    "Current elements: " + currentList + "\nChoice: ";
            final String choice =
                    TextUi.menu(currentChoices, TextUi.DONE, prompt);
            if (choice == TextUi.DONE) {
                return currentList;
            }
            if (choice == TextUi.RESTART) {
                currentList = new ArrayList<>();
                continue;
            }
            if (choice == TextUi.ADD_ALL) {
                currentList = new ArrayList<>(choices);
                continue;
            }
            currentList.add(choice);
        }
    }

    /**
     * Toggles the writing of the debug messages.
     * 
     * @param wdm
     */
    public static void setDebugMessages(final boolean wdm) {
        TextUi.writeDebugMessage = wdm;
    }

    /**
     * Query for a three choice question (typically, YES / NO / ABORT)
     * 
     * @param prompt
     * @param defaultChoice
     * @return
     */
    public static int tripleChoice(String promptPassed,
            final boolean defaultChoice) {
        String prompt = TextUi.processQuestionPrompt(promptPassed);
        while (true) {
            if (defaultChoice) {
                TextUi.writer.print(prompt + " ([y]/n/a): ");
            } else {
                TextUi.writer.print(prompt + " (y/[n]/a): ");
            }
            try {
                final String value = TextUi.reader.readLine();
                if (value.equals("")) {
                    if (defaultChoice) {
                        return TextUi.CHOICE_YES;
                    } else {
                        return TextUi.CHOICE_NO;
                    }
                }
                if (value.equals("y") || value.equals("Y")) {
                    return TextUi.CHOICE_YES;
                }
                if (value.equals("n") || value.equals("N")) {
                    return TextUi.CHOICE_NO;
                }
                if (value.equals("a") || value.equals("A")) {
                    return TextUi.CHOICE_ABORT;
                }
                TextUi.println("Incorrect value entered, use y, n or a!");
            } catch (final Exception ex) {
                // should never happen
                TextUi.errorPrint(ex.toString());
                return TextUi.CHOICE_NO;
            }
        }
    }
}