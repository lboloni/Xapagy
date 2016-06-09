/*
   This file is part of the Xapagy project
   Created on: Jul 13, 2014
 
   org.xapagy.debug.AbstractEvolutionMatrix
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.XapiParserException;

/**
 * The common part of the classes which are used to capture the evolution of
 * shadows / choices.
 * 
 * This is mostly the access components and the functions which access the rows.
 * 
 * @author Ladislau Boloni
 *
 */
public abstract class AbstractEvolutionMatrix implements Serializable {

    private static final long serialVersionUID = -436918845768222007L;
    /**
     * The rows of the matrix: it is the last executed VI, plus the current
     * time.
     */
    protected List<SimpleEntry<VerbInstance, Double>> rows = new ArrayList<>();
    /**
     * The agent
     */
    protected Agent agent;
    /**
     * ViMatch object: used internally for convenient querying
     */
    protected ViMatch viMatch;

    /**
     * Contstructor: initialize the components
     * 
     * @param agent
     */
    public AbstractEvolutionMatrix(Agent agent) {
        this.agent = agent;
        this.viMatch = new ViMatch(agent);
    }

    /**
     * Finds a row based on the parameters like for ViMatch
     * 
     * @return
     * @throws XapiParserException
     */
    public int findRow(ViType viType, String... params) {
        for (int i = 0; i != rows.size(); i++) {
            VerbInstance vi = rows.get(i).getKey();
            if (viMatch.match(vi, viType, params)) {
                return i;
            }
        }
        TextUi.abort("ChoiceEvolutionMatrix.query: could not identify row "
                + viType + " - " + params);
        return -1;
    }

    /**
     * Adds a new row
     */
    protected Double addRow() {
        // add the new row
        Double time = agent.getTime();
        VerbInstance vi = agent.getLastVerbInstance();
        rows.add(new SimpleEntry<>(vi, time));
        return time;
    }
}
