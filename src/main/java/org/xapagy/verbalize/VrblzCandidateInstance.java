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
package org.xapagy.verbalize;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.reference.ReferenceResolution;
import org.xapagy.reference.rrContext;
import org.xapagy.reference.rrException;
import org.xapagy.reference.rrState;
import org.xapagy.ui.formatters.Formatter;

/**
 * A candidate for the verbalization of an instance
 * 
 * @author Ladislau Boloni
 * Created on: Oct 6, 2011
 */
public class VrblzCandidateInstance {

    public enum IVCType {
        Group, Pronoun, Relational, Simple
    };

    private ConceptOverlay co;
    private ConceptOverlay coReal;
    private Instance instance;
    // the score for the matching of the word to the co
    private double scoreMatch;
    private IVCType type;
    private String verbalization;
    private VerbInstance vi;
    private ViPart viPart;

    /**
     * Constructor, initialize the fields
     * 
     * FIXME: this only works for single words!!!
     * 
     * @param agent
     * @param type
     * @param instance
     * @param co
     * @param vi
     * @param viPart
     * @param verbalization
     */
    public VrblzCandidateInstance(Agent agent, IVCType type, Instance instance,
            ConceptOverlay co, VerbInstance vi, ViPart viPart,
            String verbalization, double scoreMatch) {
        this.type = type;
        this.instance = instance;
        this.co = co;
        this.vi = vi;
        this.viPart = viPart;
        this.verbalization = verbalization;
        this.coReal = agent.getXapiDictionary().getCoForWord(verbalization);
        for (String label : co.getLabels()) {
            coReal.addFullLabel(label, agent);
        }
        this.scoreMatch = scoreMatch;
    }

    /**
     * @return the co
     */
    public ConceptOverlay getCo() {
        return co;
    }

    /**
     * @return the coReal
     */
    public ConceptOverlay getCoReal() {
        return coReal;
    }

    /**
     * @return the scoreMatch
     */
    public double getScoreMatch() {
        return scoreMatch;
    }

    /**
     * @return the type
     */
    public IVCType getType() {
        return type;
    }

    /**
     * 
     * 
     * Returns the verbalization of this relation
     * 
     * @return
     */
    public String getVerbalization(Agent agent) {
        String retval = verbalization;
        for (String label : coReal.getLabels()) {
            retval += " " + label;
        }
        return retval;
    }

    /**
     * Resolution margin: measuring how clear is the resolution. Current
     * version: very crude: 1.0 if it resolves, 0.0 if it doesn't.
     * 
     * @param agent
     * @param scene
     * @return
     */
    public double resolutionMargin(Agent agent, Instance scene) {
        if (resolves(agent, instance)) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    /**
     * Verifies whether in the agent, and the scene, it resolves correctly to
     * the instance
     * 
     * @return
     */
    public boolean resolves(Agent agent, Instance scene) {
        try {
            rrContext rrc =
                    rrContext.createDirectReference(agent, coReal,
                            vi.getVerbs(), viPart, scene, null);
            SimpleEntry<Instance, rrState> resolvedAs =
                    ReferenceResolution.resolveDirect(rrc, true);
            if (resolvedAs == null) {
                return false;
            }
            return instance.equals(resolvedAs.getKey());
        } catch (rrException rrex) {
            return false;
        }
    }

    /**
     * Quick printing
     */
    @Override
    public String toString() {
        Formatter fmt = new Formatter();
        fmt.add("VrblzCandidateInstance: " + type + " to instance "
                + instance.getIdentifier());
        fmt.indent();
        fmt.is("verbalization", verbalization);
        fmt.is("co", co);
        fmt.is("co", coReal);
        fmt.is("scoreMatch", scoreMatch);
        return fmt.toString();
    }
}
