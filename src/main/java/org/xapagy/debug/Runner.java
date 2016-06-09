/*
   This file is part of the Xapagy project
   Created on: Jan 4, 2011

   org.xapagy.debug.Runner

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.ReferenceAPI;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.storygenerator.ProperNameGenerator;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.SaveLoadUtil;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;
import org.xapagy.ui.observers.ToStringObserver;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.ui.prettyprint.PrintDetail;
import org.xapagy.util.ClassResourceHelper;
import org.xapagy.xapi.XapiFileLoader;
import org.xapagy.xapi.XapiParser;

/**
 * Utility class which helps run debug and test runs
 *
 * @author Ladislau Boloni
 *
 */
public class Runner implements Serializable {

    private static final long serialVersionUID = 2676120083211218743L;
    public Agent agent;
    // assertion helper for testing assertions and throwing errors
    public AssertionHelper ah;
    // assertion helper for checking things
    public AssertionHelper aht;
    // choice access
    public ChoiceAccess choiceAccess;
    /**
     * if set, the specified choices are recorded after every execution step.
     * This is not done for the execution from file.
     */
    private ChoiceEvolutionMatrix choiceEvolutionMatrix = null;
    // exec helper
    public ExecHelper eh;
    public ReferenceAPI ref;

    /**
     * if set, the specified shadows are recorded after every execution step.
     * This is not done for the execution from file.
     */
    private InstanceShadowEvolutionMatrix instanceShadowEvolutionMatrix = null;

    public boolean printOn = false;

    public ProperNameGenerator properNameGenerator = new ProperNameGenerator();
    int round = 0;
    public ToStringObserver tso;
    public ViMatch viMatch;
    /**
     * if set, the specified shadows are recorded after every execution step.
     * This is not done for the execution from file.
     */
    private ViShadowEvolutionMatrix viShadowEvolutionMatrix = null;

    
    /**
     * Constructor for a runner with an agent
     * @param agent
     */
    public Runner(Agent agent) {
        this.agent = agent;
        PrettyPrint.lastAgent = agent;
        initComponents();
    }

    /**
     * Constructor which loads the agent from a specific file
     *
     * @param agentFile
     */
    public Runner(File agentFile) {
        SaveLoadUtil<Agent> slu = new SaveLoadUtil<>();
        agent = slu.load(agentFile);
        initComponents();
    }

    /**
     * Constructor which creates a new agent for a specific domain
     */
    public Runner(String domain) {
        agent = new Agent("unnamed");
        exec("$Include '" + domain + "'");
        initComponents();
    }

    /**
     * Syntactic sugar, but simplifies a lot. Executes the string and returns
     * the action component
     *
     * @param statement
     * @return
     */
    public VerbInstance exac(String statement) {
        return eh.getAction(exec(statement));
    }

    /**
     * Executes an ABStory
     *
     * @param statements
     * @return
     */
    public List<VerbInstance> exec(ABStory abs) {
        List<VerbInstance> retval = new ArrayList<>();
        for (int i = 0; i != abs.length(); i++) {
            String statement = abs.getLine(i);
            retval.addAll(exec(statement));
        }
        return retval;
    }

    /**
     * Executes a list of statements
     *
     * @param statements
     * @return
     */
    public List<VerbInstance> exec(List<String> statements) {
        List<VerbInstance> retval = new ArrayList<>();
        for (String statement : statements) {
            retval.addAll(exec(statement));
        }
        return retval;
    }

    /**
     * Executes a statement and returns the list of VIs that have been created 
     * as a result of the statement.
     * 
     * Immediately returns with an empty list for comments
     *
     * @param object
     * @param file
     * @return
     */
    public List<VerbInstance> exec(String statement) {
        if (XapiParser.isAComment(statement)) {
            return new ArrayList<>(); 
        }
        List<VerbInstance> retval = null;
        try {
            agent.getLoop().addReading(statement);
            retval = agent.getLoop().proceed();
        } catch (Exception ex) {
            TextUi.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
        if (choiceEvolutionMatrix != null) {
            choiceEvolutionMatrix.record();
        }
        if (instanceShadowEvolutionMatrix != null) {
            instanceShadowEvolutionMatrix.record();
        }
        if (viShadowEvolutionMatrix != null) {
            viShadowEvolutionMatrix.record();
        }
        return retval;
    }

    /**
     * Executes a file in the current directory
     *
     * @param object
     * @param file
     * @return
     */
    public List<VerbInstance> execFile(Object object, String fileName, String fromMarker) {
        String fullPath = null;
        try {
            File file = ClassResourceHelper.getResourceFile(object, fileName);
            fullPath = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return execFile(fullPath, fromMarker);
    }

    /**
     * Executes a certain file (directly) and returns the list of verb instances
     *
     * @param file
     * @return
     */
    public List<VerbInstance> execFile(String fileName, String fromMarker) {
        List<VerbInstance> retval = new ArrayList<>();
        try {
            File f = new File(fileName);
            XapiFileLoader.loadFileToReading(agent, f, fromMarker);
            retval.addAll(agent.getLoop().proceed());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retval;
    }
    
    
    /**
     * Executes a certain file (directly) and returns the list of verb instances
     *
     * @param file
     * @return
     */
    public List<VerbInstance> execFile(File file, String fromMarker) {
        List<VerbInstance> retval = new ArrayList<>();
        try {
            XapiFileLoader.loadFileToReading(agent, file, fromMarker);
            retval.addAll(agent.getLoop().proceed());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retval;
    }


    public ChoiceEvolutionMatrix getChoiceEvolutionMatrix() {
        return choiceEvolutionMatrix;
    }

    /**
     * Initializes the components. Called from the constructor. We assume that
     * by this time we have the agent.
     */
    private void initComponents() {
        ah = new AssertionHelper(agent, true);
        aht = new AssertionHelper(agent, false);
        eh = new ExecHelper(agent);
        viMatch = new ViMatch(agent);
        choiceAccess =
                new ChoiceAccess(agent,
                        HeadlessComponents.comparatorIndependentScore);
        ref = agent.getReferenceAPI();
        tso = new ToStringObserver();
        agent.addObserver("ToStringObserver", tso);
        tso.setTrace(TraceWhat.COMPACT);
    }

    public void print(Object object) {
        if (printOn) {
            String text = PrettyPrint.pp(object, agent, PrintDetail.DTL_DETAIL);
            TextUi.println(text);
        }
    }

    public void println(String string) {
        if (printOn) {
            TextUi.println(string);
        }
    }

    public void setChoiceEvolutionMatrix(
            ChoiceEvolutionMatrix choiceEvolutionMatrix) {
        this.choiceEvolutionMatrix = choiceEvolutionMatrix;
    }

    public void setInstanceShadowEvolutionMatrix(
            InstanceShadowEvolutionMatrix instanceShadowEvolutionMatrix) {
        this.instanceShadowEvolutionMatrix = instanceShadowEvolutionMatrix;
    }

    public void setViShadowEvolutionMatrix(
            ViShadowEvolutionMatrix viShadowEvolutionMatrix) {
        this.viShadowEvolutionMatrix = viShadowEvolutionMatrix;
    }

}
