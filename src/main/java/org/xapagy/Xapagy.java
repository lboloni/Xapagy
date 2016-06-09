/*
   This file is part of the Xapagy project
   Created on: Mar 12, 2012
 
   org.xapagy.Main
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.debug.Runner;
import org.xapagy.ui.SaveLoadUtil;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;
import org.xapagy.ui.observers.ToStringObserver;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class Xapagy {

    private static File inputAgent = null;
    private static File log = null;
    private static File outputAgent = null;
    private static boolean cache = false;
    /**
     * Flush all the events
     */
    private static boolean resetReadings = false;
    public static final String P_HELP = "--help";
    public static final String P_CACHE = "--cache";
    public static final String P_RESET_READINGS = "--reset-readings";
    public static final String P_INPUT_AGENT = "--input-agent";
    public static final String P_INPUT_AGENT_SHORT = "-i";
    public static final String P_OUTPUT_AGENT = "--output-agent";
    public static final String P_OUTPUT_AGENT_SHORT = "-o";
    public static List<String> stories = new ArrayList<>();

    /**
     * Error message: prints the message, the help, and exits with 01
     * 
     * @param error
     */
    public static void error(String errorMessage) {
        TextUi.printLabeledSeparator("Error!!!");
        TextUi.println(errorMessage);
        TextUi.printLabeledSeparator("-");
        Xapagy.help();
        System.exit(1);
    }

    /**
     * The help for the Xapagy main file
     */
    private static void help() {
        Formatter fmt = new Formatter();
        fmt.add("Format: Xapagy [parameters] stories");
        fmt.add("Parameters:");
        fmt.indent();
        fmt.add("--input-agent / -i path-to-input-agent");
        fmt.indent();
        fmt.add("specifies a saved agent that serves as the starting point.");
        fmt.add("By default, start with an empty agent.");
        fmt.deindent();
        fmt.add("--output-agent / -o path-to-output-agent");
        fmt.indent();
        fmt.add("specifies a file where the agent obtained as the result of the run");
        fmt.add("should be saved. By default, do not save.");
        fmt.deindent();
        fmt.add("--cache");
        fmt.indent();
        fmt.add("If the output file exists, return immediately");
        fmt.deindent();
        fmt.add("--reset-readings");
        fmt.indent();
        fmt.add("Remove the saved readings from the input agent - useful for restarting from checkpoint");
        fmt.deindent();
        fmt.add("--help");
        fmt.deindent();
        fmt.add("Stories:");
        fmt.indent();
        fmt.add("story - refers to a story named story.xapi in one of the domain directories");
        fmt.add("file:dir/dir/story - refers to a story named story.xapi in the specified directory");
        fmt.add("story::marker - include the story starting from the specified marker");
        fmt.add("if no story is specified but there is an input, enter in debug mode"); 
        fmt.deindent();
        TextUi.println(fmt);
    }

    /**
     * @param args
     */
    public static void main(String... args) {
        // initialization - for the event that this is called internally
        stories = new ArrayList<>();
        inputAgent = null;
        outputAgent = null;
        cache = false;
        TextUi.println(Version.versionString());
        //
        Xapagy.parseArguments(args);
        int retval = Xapagy.run();
        if (retval != 0) {
            System.exit(retval);
        }
        // System.exit(retval);
    }

    /**
     * Parse the arguments into the parameter values
     * 
     * @param args
     */
    private static void parseArguments(String args[]) {
        // end of debugging
        // start from 0...
        int i = 0;
        while (i < args.length) {
            String paramName = args[i];
            i++;
            // interpret those which do not have a parameter
            switch (paramName) {
            case P_HELP: {
                Xapagy.help();
                System.exit(0);
                break;
            }
            case P_CACHE: {
                cache = true;
                continue;
            }
            case P_RESET_READINGS: {
                resetReadings = true;
                continue;
            }            }
            // if a parameter does not start with "-", interpret it as story
            // name
            if (!paramName.startsWith("-")) {
                stories.add(paramName);
                continue;
            }
            // from here we have parameters with values
            // handle the case when there is no value passed
            if (i == args.length) {
                throw new Error("missing value for parameter: " + paramName);
            }
            String paramValue = args[i];
            i++;
            // now, interpret the options with a parameter
            switch (paramName) {
            case P_INPUT_AGENT:
            case P_INPUT_AGENT_SHORT: {
                Xapagy.inputAgent = new File(paramValue);
                break;
            }
            case P_OUTPUT_AGENT: 
            case P_OUTPUT_AGENT_SHORT: {
                Xapagy.outputAgent = new File(paramValue);
                break;
            }
            default: {
                TextUi.println("Can not understand parameter: " + paramName);
            }
            }
        }
    }

    /**
     * Run the whole thing
     * 
     * @throws IOException
     */
    public static int run() {
        // check whether there is anything to run
        if (stories.size() == 0 && (Xapagy.inputAgent == null)) {
            TextUi.println(
                    "No stories specified, no input agent, there is nothing to run. ");
            TextUi.println("Type: xapagy --help");
            System.exit(1);
        }

        if (cache) {
            if (Xapagy.outputAgent.exists()) {
                TextUi.println("output exists, cache on, returning");
                return 0;
            }
        }

        Runner r = null;
        SaveLoadUtil<Agent> slo = new SaveLoadUtil<>();
        //
        //  Create the new agent
        //
        if (Xapagy.inputAgent != null) {
            Agent a = slo.load(Xapagy.inputAgent);
            if (a == null) {
                TextUi.println("Could not load input agent from " + Xapagy.inputAgent);
                System.exit(1);
            }
            r = new Runner(a);
            // observers are transient, so here we will add a compact tostring
            // observer such that we know what is going on
            ToStringObserver tso = new ToStringObserver();
            r.agent.addObserver("ToStringObserver", tso);
            tso.setTrace(TraceWhat.COMPACT);
            TextUi.println("Existing agent successfully loaded from: "
                    + Xapagy.inputAgent);
        } else {
            // create the new agent
            r = new Runner("Core");
            TextUi.println("New agent created");
        }
        if (resetReadings) {
            r.agent.getLoop().resetReading();
        }
        // turn on the tracer
        r.tso.setTrace();
        if (Xapagy.log != null) {
            r.tso.setPrintToFile(true);
            r.tso.setOutput(Xapagy.log);
        }
        if (stories.isEmpty()) {
            // this is the particular case when there is no story 
            // r.exec("-");
            r.exec("$DebugHere");
        } else {
            for (String story : stories) {
                // if a story is marked with story::xxx, include from marker xxx
                int separator = story.indexOf("::");
                if (separator != -1) {
                    String marker = story.substring(separator + "::".length(),
                            story.length());
                    TextUi.println(marker);
                    String storypart = story.substring(0, separator);
                    TextUi.println(storypart);
                    r.exec("$Include From '" + marker + "' '" + storypart + "'");
                } else {
                    r.exec("$Include '" + story + "'");
                }
            }
        }
        if (Xapagy.outputAgent != null) {
            boolean res = slo.save(r.agent, Xapagy.outputAgent);
            if (res) {
                TextUi.println(
                        "Successfully saved agent to: " + Xapagy.outputAgent);
                return 0;
            } else {
                Xapagy.error("Could not save agent to:" + Xapagy.outputAgent);
                return 1; // unsuccessful save
            }
        } else {
            TextUi.println("No output specified, agent was NOT saved.");
        }
        return 0;
    }

}
