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
package org.xapagy.headless_shadows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.activity.hlsmaintenance.CharacterizationScore;
import org.xapagy.activity.hlsmaintenance.ChoiceScore;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.SceneHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * A choice encapsulates a potential verb instance which can be instantiated or
 * considered by the system. Choices can come from several sources
 * (continuation, missing relation, missing action, summarization, new instance
 * creation).
 * 
 * Choices are either not selected for instantiation, instantiated, or in course
 * of instantiation. The latter applies in situations where the course have
 * dependencies.
 * 
 * @author Ladislau Boloni
 * Created on: Sep 6, 2011
 */
public class Choice implements XapagyComponent, Serializable {

    public enum ChoiceStatus {
        FULLY_INSTANTIATED, NOT_CHOSEN, PARTIALLY_INSTANTIATED
    }

    public enum ChoiceType {
        CHARACTERIZATION, CONTINUATION, MISSING_ACTION, MISSING_RELATION,
        STATIC
    }

    private static final long serialVersionUID = -2993886162310388201L;
    private CharacterizationScore characterizationScore;
    private ChoiceScore choiceScore;
    private ChoiceStatus choiceStatus;
    private ChoiceType choiceType;
    /**
     * the currently resolved HLSNI - it is set in the partially instantiated
     * state
     */
    private HlsNewInstance currentHlsni;
    private Hls hls;
    /**
     * The HLS for the characterization type choices
     */
    private HlsCharacterization hlsCharacterization;
    /**
     * The StaticHls for STATIC type choices
     */
    private StaticHls staticHls;

    /**
     * @return the staticHls
     */
    public StaticHls getStaticHls() {
        if (choiceType != ChoiceType.STATIC) {
            TextUi.abort("getStaticHls called, but this is not of type STATIC");
        }
        return staticHls;
    }

    private String identifier;
    private List<VerbInstance> instantiatedVis = new ArrayList<>();

    /**
     * Create a choice of type anything but CHARACTERIZATION or STATIC
     */
    public Choice(Agent agent, Hls hls, ChoiceType choiceType) {
        choiceStatus = ChoiceStatus.NOT_CHOSEN;
        if (choiceType.equals(ChoiceType.CHARACTERIZATION)
                || choiceType.equals(ChoiceType.STATIC)) {
            TextUi.abort("This constructor cannot be used for choiceType "
                    + choiceType);
        }
        this.choiceType = choiceType;
        this.hls = hls;
        this.identifier = agent.getIdentifierGenerator().getChoiceIdentifier();
        this.choiceScore = new ChoiceScore(this, agent);
    }

    /**
     * Constructor for the STATIC type of choice
     * 
     * @param agent
     *            - used to get the identifier
     * @param staticHls
     *            - the static hls on which the choice depends
     */
    public Choice(Agent agent, StaticHls shls) {
        this.choiceType = ChoiceType.STATIC;
        this.choiceStatus = ChoiceStatus.NOT_CHOSEN;
        this.staticHls = shls;
        this.identifier = agent.getIdentifierGenerator().getChoiceIdentifier();
        this.choiceScore = new ChoiceScore(this, agent);
    }

    /**
     * Constructor for the characterization choice
     * 
     * @param hlsCharacterization
     */
    public Choice(Agent agent, HlsCharacterization hlsCharacterization,
            CharacterizationScore characterizationScore) {
        this.choiceType = ChoiceType.CHARACTERIZATION;
        this.choiceStatus = ChoiceStatus.NOT_CHOSEN;
        this.hlsCharacterization = hlsCharacterization;
        this.characterizationScore = characterizationScore;
        this.choiceScore =
                new ChoiceScore(this, agent, characterizationScore.getScore());
        this.identifier = agent.getIdentifierGenerator().getChoiceIdentifier();
    }

    public CharacterizationScore getCharacterizationScore() {
        return characterizationScore;
    }

    /**
     * @return the choiceScore
     */
    public ChoiceScore getChoiceScore() {
        return choiceScore;
    }

    /**
     * @return the choiceStatus
     */
    public ChoiceStatus getChoiceStatus() {
        return choiceStatus;
    }

    /**
     * @return the choiceType
     */
    public ChoiceType getChoiceType() {
        return choiceType;
    }

    /**
     * @return the hlsSupported
     */
    public Hls getHls() {
        if (choiceType == ChoiceType.CHARACTERIZATION) {
            TextUi.abort("This is a characterization choice, there is no HLS!!!");
        }
        return hls;
    }

    /**
     * @return the hlsCharacterization
     */
    public HlsCharacterization getHlsCharacterization() {
        return hlsCharacterization;
    }

    /**
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return the instantiatedVis
     */
    public List<VerbInstance> getInstantiatedVis() {
        return instantiatedVis;
    }

    /**
     * Returns the dominant scene of this choice (used to maintain the
     * interstitial and continuation energy)
     * 
     */
    public Set<Instance> getReferredScenes() {
        switch (choiceType) {
        case CHARACTERIZATION: {
            Set<Instance> retval = new HashSet<>();
            retval.add(hlsCharacterization.getInstance().getScene());
            return retval;
        }
        case STATIC: {
            //return staticHls.getViTemplate().getReferencedScenes();
        	return SceneHelper.extractScenes(staticHls.getViTemplate(), false);
        }
        default: {
        	return SceneHelper.extractScenes(hls.getViTemplate(), false);
            //return hls.getViTemplate().getReferencedScenes();
        }
        }

    }

    /**
     * Instantiates the main VI of the choice. This should only be called when
     * the dependencies have been fully resolved.
     * 
     * After this call the Choice will be fully instantiated.
     * 
     * @return returns the VI instantiated in this round of characterization
     * @param agent
     */
    public VerbInstance instantiate(Agent agent) {
        VerbInstance vi = null;
        if (choiceStatus == ChoiceStatus.FULLY_INSTANTIATED) {
            TextUi.abort("Called instantiate on an already fully instantiated Choice");
        }
        switch (choiceType) {
        case CHARACTERIZATION: {
            vi = hlsCharacterization.instantiate(agent);
            break;
        }
        case CONTINUATION:
        case MISSING_ACTION:
        case MISSING_RELATION: {
            vi = hls.instantiate(agent);
            break;
        }
        case STATIC: {
            vi = staticHls.instantiate(agent);
        }
        }
        instantiatedVis.add(vi);
        choiceStatus = ChoiceStatus.FULLY_INSTANTIATED;
        return vi;
    }

    /**
     * Instantiate dependencies - that is instances which need to be created
     * before the VI can be instantiated. Each call to this function
     * instantiates one dependency and returns the VI which did so.
     * 
     * It returns null if there was no more dependency to be instantiated.
     * 
     * CHARACTERIZATION type choices do not have dependencies.
     * 
     * 
     * @param agent
     * @return
     */
    public VerbInstance instantiateDependency(Agent agent) {
        if (ChoiceTypeHelper.isHlsBased(this)) {
            // checks if any of the parts involve an instance which need to be
            // created - if there are multiple...
            VerbInstance viTemplate = hls.getViTemplate();
            for (ViPart part : viTemplate.getNewParts().keySet()) {
                choiceStatus = ChoiceStatus.PARTIALLY_INSTANTIATED;
                currentHlsni = hls.getDependencies().get(part);
                VerbInstance vi = currentHlsni.instantiate(agent);
                instantiatedVis.add(vi);
                return vi;
            }
        }
        // the others do not have any dependencies currently
        return null;
    }

    /**
     * To be called when we are resolving the dependency
     * 
     * @param agent
     */
    public void resolveDependency(Instance instance) {
        if (currentHlsni != null) {
            currentHlsni.resolve(instance);
        }
        currentHlsni = null;
    }

    @Override
    public String toString() {
        return PrettyPrint.ppString(this);
    }

}
