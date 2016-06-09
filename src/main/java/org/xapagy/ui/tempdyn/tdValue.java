/*
   This file is part of the Xapagy project
   Created on: Aug 4, 2013
 
   org.xapagy.ui.tempdyn.tdValue
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.agents.Focus;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;

/**
 * An object recording a certain value in the components.
 * 
 * Depending on the type:
 * 
 * FOCUS_MEMORY captures the focus and memory energy and salience
 * 
 * SHADOW captures the shadow values between idPrimary (the focus entity) and
 * idSecondary (the shadow entity)
 * 
 * CHOICE captures the values of the choice described by idPrimary
 * 
 * LINK captures the links between the VIs described by idPrimary and
 * idSecondary
 * 
 * For all of them it always records all the energy colors.
 * 
 * @author Ladislau Boloni
 * 
 */
public class tdValue implements Serializable {

    enum tdValueType {
        CHOICE, FOCUS_MEMORY, LINK, SHADOW
    }

    private static final long serialVersionUID = 1458255341719089135L;;

    /**
     * Records the choice value
     * 
     * @param component
     *            - the component which records these values. It's id will be
     *            the primary id (although the current choice's id might be
     *            different)
     * @param choice
     *            - the choice recorded. We get its score components
     * @param agent
     * @return
     */
    public static tdValue createChoice(tdComponent component, Choice choice,
            Agent agent) {
        tdValue tdv = new tdValue();
        tdv.time = agent.getTime();
        tdv.type = tdValueType.CHOICE;
        tdv.idPrimary = component.getIdentifier();
        tdv.valueChoiceScoreDependent =
                choice.getChoiceScore().getScoreDependent();
        tdv.valueChoiceScoreIndependent =
                choice.getChoiceScore().getScoreIndependent();
        tdv.valueChoiceScoreMood = choice.getChoiceScore().getScoreMood();
        return tdv;
    }

    /**
     * Creates a focus/memory type tdValue value for an instance
     * 
     * @param instance
     * @return
     */
    public static tdValue createFocusMemory(Instance instance, Agent agent) {
        tdValue tdv = new tdValue();
        tdv.time = agent.getTime();
        tdv.type = tdValueType.FOCUS_MEMORY;
        tdv.idPrimary = instance.getIdentifier();
        // get all the focus energies and saliences
        Focus fc = agent.getFocus();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
            tdv.valueEnergy.put(ec, fc.getEnergy(instance, ec));
            tdv.valueSalience.put(ec, fc.getSalience(instance, ec));
        }
        // get all the autobiographical memory energies and saliences
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.AM_INSTANCE)) {
            tdv.valueEnergy.put(ec, am.getEnergy(instance, ec));
            tdv.valueSalience.put(ec, am.getSalience(instance, ec));
        }
        return tdv;
    }

    /**
     * Creates the focus/memory type tdValue for a verb instance
     * 
     * @param instance
     * @return
     */
    public static tdValue createFocusMemory(VerbInstance vi, Agent agent) {
        tdValue tdv = new tdValue();
        tdv.time = agent.getTime();
        tdv.type = tdValueType.FOCUS_MEMORY;
        tdv.idPrimary = vi.getIdentifier();
        // get all the focus energies and saliences
        Focus fc = agent.getFocus();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
            tdv.valueEnergy.put(ec, fc.getEnergy(vi, ec));
            tdv.valueSalience.put(ec, fc.getSalience(vi, ec));
        }
        // get all the autobiographical memory energies and saliences
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.AM_VI)) {
            tdv.valueEnergy.put(ec, am.getEnergy(vi, ec));
            tdv.valueSalience.put(ec, am.getSalience(vi, ec));
        }
        return tdv;
    }

    /**
     * Creates the link type tdValue
     * 
     * @param fvi
     * @param svi
     * @param agent
     * @return
     */
    public static tdValue createLink(VerbInstance fromvi, VerbInstance tovi,
            Agent agent, String linkName, double linkValue) {
        tdValue tdv = new tdValue();
        tdv.time = agent.getTime();
        tdv.type = tdValueType.LINK;
        tdv.idPrimary = fromvi.getIdentifier();
        tdv.idSecondary = tovi.getIdentifier();
        tdv.linkName = linkName;
        tdv.valueLink = linkValue;
        return tdv;
    }

    /**
     * Creates the shadow type tdValue for an instance
     * 
     * @param fi
     * @param si
     * @param agent
     * @return
     */
    public static tdValue createShadow(Instance fi, Instance si, Agent agent) {
        Shadows sf = agent.getShadows();
        tdValue tdv = new tdValue();
        tdv.time = agent.getTime();
        tdv.type = tdValueType.SHADOW;
        tdv.idPrimary = fi.getIdentifier();
        tdv.idSecondary = si.getIdentifier();
        // get all the shadow energies and saliences
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            tdv.valueEnergy.put(ec, sf.getEnergy(fi, si, ec));
            tdv.valueSalience.put(ec, sf.getSalience(fi, si, ec));
        }
        return tdv;
    }

    /**
     * Creates the shadow type tdValue for a VI
     * 
     * @param fvi
     * @param svi
     * @param agent
     * @return
     */
    public static tdValue createShadow(VerbInstance fvi, VerbInstance svi,
            Agent agent) {
        Shadows sf = agent.getShadows();
        tdValue tdv = new tdValue();
        tdv.time = agent.getTime();
        tdv.type = tdValueType.SHADOW;
        tdv.idPrimary = fvi.getIdentifier();
        tdv.idSecondary = svi.getIdentifier();
        // get all the shadow energies and saliences
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            tdv.valueEnergy.put(ec, sf.getEnergy(fvi, svi, ec));
            tdv.valueSalience.put(ec, sf.getSalience(fvi, svi, ec));
        }
        return tdv;
    }

    /**
     * The id of the primary component in the observation (the focus component
     * or the memory component)
     */
    private String idPrimary;

    /**
     * The id of the second component in the observation (the shadow component)
     * - null otherwise
     */
    private String idSecondary;

    /**
     * The name of the link if this is the link type tdValue
     */
    private String linkName;

    /**
     * The agent time when the observation had been made
     */
    private double time;

    /**
     * The type of the value
     */
    private tdValueType type;

    /**
     * The value of the dependent choice score when recorded
     */
    private double valueChoiceScoreDependent;
    /**
     * The value of the independent choice score when recorded
     */
    private double valueChoiceScoreIndependent;
    /**
     * The value of the mood choice score when recorded
     */
    private double valueChoiceScoreMood;
    /**
     * The energy values, indexed by energy color
     */
    private Map<String, Double> valueEnergy = new HashMap<>();

    /**
     * The salience values, indexed by energy color
     */
    private Map<String, Double> valueSalience = new HashMap<>();

    /**
     * The value of the link if this is the link type tdValue
     */
    private double valueLink;

    /**
     * 
     * @return
     */

    public String getIdPrimary() {
        return idPrimary;
    }

    public String getIdSecondary() {
        return idSecondary;
    }

    /**
     * @return the linkName
     */
    public String getLinkName() {
        return linkName;
    }

    public double getTime() {
        return time;
    }

    public tdValueType getType() {
        return type;
    }

    /**
     * @return the valueChoiceScoreDependent
     */
    public double getValueChoiceScoreDependent() {
        return valueChoiceScoreDependent;
    }

    /**
     * @return the valueChoiceScoreIndependent
     */
    public double getValueChoiceScoreIndependent() {
        return valueChoiceScoreIndependent;
    }

    /**
     * @return the valueChoiceScoreMood
     */
    public double getValueChoiceScoreMood() {
        return valueChoiceScoreMood;
    }

    /**
     * Returns the value of the energy of a certain color.
     * 
     * @param ec
     * @return
     */
    public double getValueEnergy(String ec) {
        try {
            return valueEnergy.get(ec);
        } catch (NullPointerException npe) {
            TextUi.println("what is going on here???");
        }
        return 0;
    }

    public double getValueSalience(String ec) {
        return valueSalience.get(ec);
    }

    /**
     * @return the valueLink
     */
    public double getValueLink() {
        return valueLink;
    }

}
