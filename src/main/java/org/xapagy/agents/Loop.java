/*
   This file is part of the Xapagy project
   Created on: Sep 11, 2011
 
   org.xapagy.agents.Loop
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.headless_shadows.Choice;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.SaveLoadUtil;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * A class which determines the agent's iterative / real time / internal
 * interaction with the world.
 * 
 * It also keeps a log of the events...
 * 
 * @author Ladislau Boloni
 * 
 */
public class Loop implements Serializable {

    private static final long serialVersionUID = 560230003475117529L;
    /**
     * If a checkpoint was requested, for instance, through the checkpoint
     * command, the file where we need to write the checkpoint will be put here.
     * Checkpoints will be written in between loop iterations
     */
    private File requestedCheckPointFile = null;
    /**
     * If this is set to true, incoming data
     */
    private boolean scriptAccumulationMode = false;

    /**
     * @return the scriptAccumulationMode
     */
    public boolean isScriptAccumulationMode() {
        return scriptAccumulationMode;
    }

    /**
     * Accumulated script
     */
    private StringBuffer accumulatedScript = null;

    /**
     * Starts the accumulation of script
     */
    public void startScriptAccumulation() {
        scriptAccumulationMode = true;
        accumulatedScript = new StringBuffer();
    }

    /**
     * Starts the accumulation of script and returns the accumulated script
     */
    public String stopScriptAccumulation() {
        if (!scriptAccumulationMode) {
            throw new Error("Trying to  while not in accumulation mode");
        }
        String retval = accumulatedScript.toString();
        scriptAccumulationMode = false;
        accumulatedScript = null;
        return retval;
    }

    /**
     * Adds a line to the accumulated string
     * 
     * @param line
     */
    public void addToAccumulatedScript(String line) {
        if (!scriptAccumulationMode) {
            throw new Error(
                    "Trying to accumulate while not in accumulation mode");
        }
        accumulatedScript.append(line + "\n");
    }

    /**
     * @param requestedCheckPointFile
     *            the requestedCheckPointFile to set
     */
    public void setRequestedCheckPointFile(File requestedCheckPointFile) {
        this.requestedCheckPointFile = requestedCheckPointFile;
    }

    private Agent agent;
    /**
     * All the loopitems ever created
     */
    private Map<String, LoopItem> allLoopItems = new HashMap<>();

    /**
     * The ChoiceSelector component
     */
    private ChoiceSelector choiceSelector;

    /**
     * Keeps a history of all the executed loop items
     */
    private List<LoopItem> history = new ArrayList<>();
    private LoopItem inExecution;
    /**
     * Keeps the schedule of the future readings: these are the items which the
     * agent will read, when it wants to read something
     */
    private List<LoopItem> readings = new ArrayList<>();
    /**
     * Keeps the schedule of future events scheduled at specific moments in
     * time.
     */
    private List<LoopItem> scheduled = new ArrayList<>();
    /**
     * I think... Keeps a list of currently active summaries, which at some
     * moment will be
     */
    private List<LoopItem> summaries = new ArrayList<>();

    /**
     * Constructor, creates the loop for the specified agent
     * 
     * @param agent
     */
    public Loop(Agent agent) {
        this.agent = agent;
        this.choiceSelector = new ChoiceSelector(agent);
    }

    /**
     * @return the choiceSelector
     */
    public ChoiceSelector getChoiceSelector() {
        return choiceSelector;
    }

    /**
     * Adds the specified list of statements to the immediate readings. This is
     * the entry point of the XapiL2 parser
     * 
     * @param list
     */
    public void addImmediateReading(List<String> list) {
        List<LoopItem> lilist = new ArrayList<>();
        for (String text : list) {
            LoopItem li = LoopItem.createReading(agent, text);
            lilist.add(li);
        }
        readings.addAll(0, lilist);
    }

    /**
     * Adds a single line of Xapi text to the end of the reading
     * 
     * @param xapiText
     */
    public void addReading(String xapiText) {
        LoopItem reading = LoopItem.createReading(agent, xapiText);
        readings.add(reading);
    }

    /**
     * Adds a single line of Xapi text to be scheduled to be executed at a
     * certain time
     * 
     * @param time
     * @param xapiText
     */
    public void addScheduled(double time, String xapiText) {
        LoopItem scheduledItem =
                LoopItem.createScheduled(agent, xapiText, time);
        scheduled.add(scheduledItem);
    }

    /**
     * Adds a summary item to execution. This is called from
     * SaHlsmSummarizationCreation. The summary will be added to the list of
     * things to instantiate, and it will be instantiated at the next available
     * opportunity from processNewSummaries()
     * 
     * 
     * @param loopItem
     */
    public void addSummary(LoopItem loopItem) {
        summaries.add(loopItem);
    }

    /**
     * Executes the internal inputs - such as recalls or hidden action,
     * relations etc. in between the call to the next reading components
     * 
     * This should not be called directly, instead it must be part of the
     * proceed()
     * 
     * @param agent
     * @return the VIs which have been executed
     * @throws InterruptedException
     * @throws IOException
     */
    private List<VerbInstance> executeInternalInput() throws IOException {
        List<VerbInstance> retval = new ArrayList<>();
        while (true) {
            Choice choice = choiceSelector.selectChoice();
            if (choice == null) {
                break;
            }
            LoopItem item = LoopItem.createInternal(agent, agent.getTime(),
                    choice, choice.getChoiceScore().getScoreMood());
            item.execute(true);
        }
        return retval;
    }

    /**
     * @return the allLoopItems
     */
    public Map<String, LoopItem> getAllLoopItems() {
        return allLoopItems;
    }

    /**
     * @return the history
     */
    public List<LoopItem> getHistory() {
        return history;
    }

    /**
     * @return the inExecution
     */
    public LoopItem getInExecution() {
        return inExecution;
    }

    /**
     * @return the readings
     */
    public List<LoopItem> getReadings() {
        return readings;
    }

    /**
     * @return the scheduled
     */
    public List<LoopItem> getScheduled() {
        return scheduled;
    }

    /**
     * @return the summaries
     */
    public List<LoopItem> getSummaries() {
        return summaries;
    }

    /**
     * Retrieves the next loop external or reading loopitem for execution. If
     * there is a scheduled item which is supposed to be executed now, it
     * returns it. If not, proceeds to read the next line from its book.
     * 
     * If there is no reading either, it returns null.
     * 
     * @return the next loop item
     */
    private LoopItem nextLoopItemExternalOrReading() {
        if (inExecution != null) {
            TextUi.errorPrint("already executing "
                    + PrettyPrint.ppDetailed(inExecution, agent)
                    + " possibly after checkpoint restore?");
            // throw new Error("Already in execution");
        }
        if (!scheduled.isEmpty() && scheduled.get(0)
                .getScheduledExecutionTime() <= agent.getTime()) {
            inExecution = scheduled.get(0);
            scheduled.remove(0);
            return inExecution;
        }
        if (!readings.isEmpty()) {
            inExecution = readings.get(0);
            readings.remove(0);
            return inExecution;
        }
        // nothing to execute
        return null;
    }

    /**
     * Proceed basically forever
     * 
     * @return
     * @throws IOException
     */
    public List<VerbInstance> proceed() throws IOException {
        return proceed(Integer.MAX_VALUE, false);
    }

    /**
     * The main loop of the Xapagy agent. For each loop there is one call to the
     * bringNextToExecution. If there is nothing there, it stops. It also stops
     * after a specific number of VIs.
     * 
     * @param countMax
     *            - the number of maximum verbinstances which will be processed
     * @param onlyReading
     *            - only execute the external or the reading - useful for
     *            parsing commands etc
     * 
     * @return the list of the created VIs
     * @throws IOException
     */
    public List<VerbInstance> proceed(int countMax, boolean onlyReading)
            throws IOException {
        List<VerbInstance> retval = new ArrayList<>();
        while (true) {
            // if a checkpoint was requested, do it here
            if (requestedCheckPointFile != null) {
                SaveLoadUtil<Agent> slu = new SaveLoadUtil<Agent>();
                slu.save(agent, requestedCheckPointFile);
                requestedCheckPointFile = null;
            }
            if (!onlyReading) {
                // execute internal input, if any
                List<VerbInstance> viInternalInput = executeInternalInput();
                retval.addAll(viInternalInput);
                // process the summaries, if any
                List<VerbInstance> viSummaries = processNewSummaries();
                retval.addAll(viSummaries);
                // now proceed with the external or reading lines
            }
            LoopItem item = nextLoopItemExternalOrReading();
            if (item == null) {
                return retval;
            }
            item.execute(true);
            List<VerbInstance> viMainLoopItem = item.getExecutionResult();
            retval.addAll(viMainLoopItem);
            if (retval.size() >= countMax) {
                return retval;
            }
        }
    }

    /**
     * Processes all the pending summaries in the summary list
     * 
     * @return the list of vi's created
     */
    private List<VerbInstance> processNewSummaries() {
        List<VerbInstance> retval = new ArrayList<>();
        // this can be longer than what you have here, because the
        // processing of summaries can create new summaries
        while (!summaries.isEmpty()) {
            LoopItem item = summaries.get(0);
            summaries.remove(0);
            item.execute(true);
            retval.addAll(item.getExecutionResult());
        }
        return retval;
    }

    /**
     * Resets the future reading of the agent starting from this position
     */
    public void resetReading() {
        readings.clear();
    }

    /**
     * @param inExecution
     *            the inExecution to set
     */
    public void setInExecution(LoopItem inExecution) {
        this.inExecution = inExecution;
    }
}
