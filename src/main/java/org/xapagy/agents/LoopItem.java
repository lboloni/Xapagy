/*
   This file is part of the Xapagy project
   Created on: Sep 11, 2011
 
   org.xapagy.agents.LoopItem
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.script.ScriptException;

import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.exceptions.MalformedConceptOrVerbName;
import org.xapagy.exceptions.NoSuchConceptOrVerb;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.ExecuteChoice;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.reference.ReferenceResolution;
import org.xapagy.reference.rrException;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.DecompositionHelper;
import org.xapagy.xapi.MacroParser;
import org.xapagy.xapi.XapiParserException;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiReferenceType;
import org.xapagy.xapi.reference.XrefStatement;
import org.xapagy.xapi.reference.XrefVi;
import org.xapagy.xapi.reference.XrefWait;

/**
 * A loop item is the execution unit of the Xapagy agent. One loop item can
 * correspond to:
 * 
 * <ul>
 * <li>A Xapi sentence describing a VI.
 * <li>A Xapi meta-statement
 * <li>An internally generated choice.
 * <li>A currently pending summarization (FIXME)
 * </ul>
 * 
 * @author Ladislau Boloni
 * 
 */
public class LoopItem implements XapagyComponent, Serializable {

    /**
     * The state of the loop item: each loop item can be executed at most once.
     * 
     * @author Ladislau Boloni
     *
     */
    public enum LoopItemState {
        EXECUTED, NOT_EXECUTED
    }

    /**
     * The type of the loop item:
     * 
     * <ul>
     * <li>EXTERNAL - an external observation (this normally appears as a
     * scheduled loop item when Xapagy is run from the command line.
     * <li>READING - a loop item which is retrieved from the reading list
     * <li>INTERNAL - a loop item which is generated internally by Xapagy
     * (typically, a result of a choice selected for execution)
     * </ul>
     * 
     * @author Ladislau Boloni
     *
     */
    public enum LoopItemType {
        EXTERNAL, INTERNAL, READING
    };

    private static final long serialVersionUID = 5644893720655147749L;

    /**
     * Creates an internal loop item: either a recall or an inference
     * 
     * @param vi
     * @param time
     * @param willingness
     * 
     * @return
     */
    public static LoopItem createInternal(Agent agent, double time,
            Choice choice, double willingness) {
        LoopItem retval = new LoopItem(agent);
        retval.state = LoopItemState.NOT_EXECUTED;
        retval.type = LoopItemType.INTERNAL;
        retval.executionTime = time;
        retval.willingness = willingness;
        retval.choice = choice;
        return retval;
    }

    /**
     * Creates a reading type loop item, hand edited (no source file)
     * 
     * @param text
     * @return
     */
    public static LoopItem createReading(Agent agent, String text) {
        LoopItem retval = new LoopItem(agent);
        retval.state = LoopItemState.NOT_EXECUTED;
        retval.type = LoopItemType.READING;
        retval.text = text;
        return retval;
    }

    /**
     * Creates a reading type loop item (called from Loop.addFileToReading)
     * 
     * @param text
     * @return
     */
    public static LoopItem createReading(Agent agent, String text, File file,
            int fileLineNo) {
        LoopItem retval = new LoopItem(agent);
        retval.state = LoopItemState.NOT_EXECUTED;
        retval.type = LoopItemType.READING;
        retval.text = text;
        retval.fileName = file.getName();
        retval.fileLineNo = fileLineNo;
        return retval;
    }

    /**
     * @param xapiText
     * @param time
     * @return
     */
    public static LoopItem createScheduled(Agent agent, String text,
            double time) {
        LoopItem retval = new LoopItem(agent);
        retval.state = LoopItemState.NOT_EXECUTED;
        retval.type = LoopItemType.EXTERNAL;
        retval.text = text;
        retval.scheduledExecutionTime = time;
        return retval;
    }

    private Agent agent;

    /**
     * For internal recalls, the choice which let to its instantiation
     */
    private Choice choice;
    /**
     * The choices which are active when the loop item goes into execution.
     * These will be recorded only when the parameter is set
     */
    private List<Choice> recordedChoices = null;

    private List<VerbInstance> executionResult = new ArrayList<>();
    private double executionTime = -1;
    private int fileLineNo = -1;
    private String fileName = null;
    private String identifier;
    private double scheduledExecutionTime = -1;
    private LoopItemState state = null;
    private String text;
    private LoopItemType type = null;

    /**
     * For internal events, the willingness of the choice with which it was
     * instantiated
     */
    private double willingness = 0;

    public static String MACRO_PREFIX = "$";

    public LoopItem(Agent agent) {
        identifier = agent.getIdentifierGenerator().getLoopItemIdentifier();
        agent.getLoop().getAllLoopItems().put(identifier, this);
        this.agent = agent;
    }

    /**
     * Returns the recorded choices...
     * 
     * @return
     */
    public List<Choice> getRecordedChoices() {
        return recordedChoices;
    }

    /**
     * Executes a loop item
     * 
     * @param notifyObservers
     *            - if this is set to false, it is called from an observer, so
     *            we don't put it into the history and do not re-notify
     *            observers to avoid an infinite loop
     */
    public void execute(boolean notifyObservers) {
        agent.getLoop().setInExecution(this);
        //
        // if this parameter is turned on, the choices which had been active at
        // the moment the loopitem had been executed will be recorded into the
        // LoopItem - this would allow us to debug why certain choices had been
        // chosen at certain moments.
        //
        boolean debugRecordChoices = agent.getParameters().getBoolean("A_DEBUG",
                "G_GENERAL", "N_RECORD_CHOICES_INTO_LOOPITEM");
        if (debugRecordChoices) {
            recordedChoices = agent.getHeadlessComponents()
                    .getChoices(HeadlessComponents.comparatorMoodScore);
        }
        agent.notifyObservers(
                new DebugEvent(DebugEventType.BEFORE_LOOP_ITEM_EXECUTION));
        switch (type) {
        case EXTERNAL:
        case READING:
            executeExternalOrReading();
            break;
        case INTERNAL: {
            executionResult = ExecuteChoice.executeChoice(agent, choice);
            double pause = getPauseAfterInternalLoopItem();
            Execute.executeDiffusionActivities(agent, pause);
            break;
        }
        }
        executionTime = agent.getTime();
        state = LoopItemState.EXECUTED;
        if (notifyObservers) {
            agent.notifyObservers(
                    new DebugEvent(DebugEventType.AFTER_LOOP_ITEM_EXECUTION));
            agent.getLoop().getHistory().add(this);
        }
        agent.getLoop().setInExecution(null);

    }

    /**
     * Formats a specific throwable (Error or Exception) by indicating where it
     * was coming from
     * 
     * @param t
     * @param text
     * @return
     */
    private String formatException(Throwable t, String description) {
        if (fileName != null) {
            return "At " + fileName + ":" + fileLineNo + " = " + text
                    + "\nError was found: " + t.getClass().getCanonicalName()
                    + " " + description;
        } else {
            return "At generated Xapi = " + text + "\nError was found: "
                    + t.getClass().getCanonicalName() + " " + description;
        }
    }

    /**
     * Executes an internal or reading type LoopItem. This is whatever comes out
     * of a single line of Xapi
     * 
     */
    private void executeExternalOrReading() {
        String line = getText();
        //
        // if we are in script accumulation mode
        //
        if (agent.getLoop().isScriptAccumulationMode()) {
            if (line.startsWith("$EndScript") || line.startsWith("$}}")) {
                String script = agent.getLoop().stopScriptAccumulation();
                try {
                    agent.getScriptEngine().eval(script);
                } catch (ScriptException e) {
                    e.printStackTrace();
                    throw new Error("Script exception!");
                }
                return;
            }
            // so it is not the end of the script           
            agent.getLoop().addToAccumulatedScript(line);
            return;
        }
        //
        // if  it is a one-line script
        //
        if (line.startsWith("!!")) {
            String scriptText = line.substring("!!".length());
            try {
                agent.getScriptEngine().eval(scriptText);
            } catch (ScriptException e) {
                e.printStackTrace();
                throw new Error("Script exception!");
            }
            return;
        }
        if (line.startsWith("$BeginScript") || line.startsWith("${{")) {
            agent.getLoop().startScriptAccumulation();
            return;
        }

        //
        // if  it is a macro
        //
        if (line.startsWith(LoopItem.MACRO_PREFIX)) {
            MacroParser.executeMacro(line, agent);
            return;
        }
        // 
        // if we got here, it is a regular sentence
        //
        XapiReference xst = null;
        try {
            xst = agent.getXapiParser().parseLine(line);
        } catch (XapiParserException e) {
            TextUi.println(line);
            throw new Error(formatException(e, e.getDiagnosis()));
        } catch (MalformedConceptOrVerbName e) {
            throw new Error(formatException(e, e.toString()));
        } catch (NoSuchConceptOrVerb e) {
            throw new Error(formatException(e, e.toString()));
        }
        // if it is a macro
        List<XapiReference> atomicStatements;
        // the scenes referenced by this loopitem
        Set<Instance> referencedScenes = new HashSet<>();
        if (xst.getType().equals(XapiReferenceType.WAIT)) {
            // if it is a wait statement, add directly
            atomicStatements = new ArrayList<>();
            atomicStatements.add(xst);
        } else {
            // if it is a statement, decompose
            atomicStatements =
                    DecompositionHelper.decomposeStatementIntoVisAndWait(agent,
                            (XrefStatement) xst);
        }
        List<VerbInstance> retval = new ArrayList<>();
        for (XapiReference statement : atomicStatements) {
            if (statement.getType().equals(XapiReferenceType.VI)) {
                VerbInstance vi = null;
                try {
                    vi = ReferenceResolution.resolveFullVi(agent,
                            (XrefVi) statement, null);
                } catch (rrException e) {
                    throw new Error("Reference resolution exception at line:\n"
                            + line + "\n" + e.toString());
                }
                referencedScenes.addAll(vi.getReferencedScenes());
                Execute.executeSpikeActivities(agent, vi, null);
                retval.add(vi);
                continue;
            }
            if (statement.getType().equals(XapiReferenceType.WAIT)) {
                Execute.executeDiffusionActivities(agent,
                        ((XrefWait) statement).getTimeWait());
                continue;
            }
            throw new Error(
                    "The only ones allowed are XapiVi and XapiWait, this one is "
                            + xst.getType());
        }
        //
        // Sets the interstitial energy for all the referenced scenes
        //
        for (Instance scene : referencedScenes) {
            scene.getSceneParameters().resetInterstitialEnergy();
        }
        executionResult.addAll(retval);
    }

    /**
     * @return the choice
     */
    public Choice getChoice() {
        return choice;
    }

    /**
     * @return the executionResult
     */
    public List<VerbInstance> getExecutionResult() {
        return executionResult;
    }

    /**
     * @return the executionTime
     */
    public double getExecutionTime() {
        return executionTime;
    }

    /**
     * @return the fileLineNo
     */
    public int getFileLineNo() {
        return fileLineNo;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns how much pause are we going to have after after a specific
     * choice. Currently, all of them imply a pause of 1.0 second.
     * 
     * Essentially, this is the time we have to run the Da's before the next
     * loop, so if we return 0 here, we won't take into account the newly
     * created VI.
     * 
     * @return
     */
    public double getPauseAfterInternalLoopItem() {
        switch (choice.getChoiceType()) {
        case CHARACTERIZATION:
            return 1.0;
        case CONTINUATION:
            return 1.0;
        case MISSING_ACTION:
            return 1.0;
        case MISSING_RELATION:
            return 1.0;
        case STATIC:
            return 1.0;
        }
        throw new Error("unknown choice type");
    }

    /**
     * @return the scheduledExecutionTime
     */
    public double getScheduledExecutionTime() {
        return scheduledExecutionTime;
    }

    /**
     * @return the state
     */
    public LoopItemState getState() {
        return state;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @return the type
     */
    public LoopItemType getType() {
        return type;
    }

    /**
     * @return the willingness
     */
    public double getWillingness() {
        return willingness;
    }

    @Override
    public String toString() {
        return PrettyPrint.ppString(this);
    }

}
