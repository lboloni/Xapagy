/*
   This file is part of the Xapagy project
   Created on: Aug 5, 2013
 
   org.xapagy.ui.tempdyn.tdDataBase
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.headless_shadows.ChoiceTypeHelper;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViSimilarityHelper;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.smartprint.SpFocus;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.ui.tempdyn.tdComponent.tdComponentType;

/**
 * A database of TD values. It is essentially just a queryable collection of
 * tdValue objects
 * 
 * @author Ladislau Boloni
 * 
 */
public class tdDataBase implements Serializable {
    /**
     * This is the maximum amount of time we look backwards for a recorded value
     * for something which is updated by the DA. Should be a bit larger than the
     * p.timeStepOfDAs
     */
    public static double BACKWINDOW_DA = 0.11;
    /**
     * This is the maximum amount of time we look backwards for a recorded value
     * for something which is updated in single step (eg. Choices, if the
     * p.doSingleStepHLS is set). This is a bit more iffy, but let us assume it
     * is like this.
     */
    public static double BACKWINDOW_STEP = 1.10;
    private static final long serialVersionUID = 6150843279465277529L;

    /**
     * A list of all the choice type components
     */
    private List<tdComponent> choices = new ArrayList<>();
    /**
     * A list of all the instance type components which had been in the focus
     */
    private List<tdComponent> focusInstances = new ArrayList<>();
    /**
     * A list of all the vi type components which had been in the focus
     */
    private List<tdComponent> focusVis = new ArrayList<>();

    /**
     * The ending time (last record)
     */
    private double timeEnd = -1;

    /**
     * The starting time (first record)
     */
    private double timeStart = -1;

    /**
     * A collection of values indexed by the idPrimary of the value
     */
    private Map<String, List<tdValue>> values = new HashMap<>();

    /**
     * Adds a TDV in the specific lists
     * 
     * @param tdv
     */
    private void addTdValue(tdValue tdv) {
        List<tdValue> list = values.get(tdv.getIdPrimary());
        if (list == null) {
            list = new ArrayList<>();
            values.put(tdv.getIdPrimary(), list);
        }
        list.add(tdv);
    }

    /**
     * @return the choices
     */
    public List<tdComponent> getChoices() {
        return choices;
    }

    /**
     * Returns the dependent choice score value. If there is no exact value for
     * the specific one, look for the previous one. If there is no previous one,
     * return zero. FIXME: this is not quite what I want...
     * 
     * @param identifier
     * @param time
     * @return
     */
    public double getChoiceScoreDependent(String identifier, double time) {
        tdValue tdv =
                getTimedValue(identifier, time, tdDataBase.BACKWINDOW_STEP);
        if (tdv != null) {
            return tdv.getValueChoiceScoreDependent();
        } else {
            return 0;
        }
    }

    /**
     * Returns the independent choice score value. If there is no exact value
     * for the specific one, look for the previous one. If there is no previous
     * one, return zero. FIXME: this is not quite what I want...
     * 
     * @param identifier
     * @param time
     * @return
     */
    public double getChoiceScoreIndependent(String identifier, double time) {
        tdValue tdv =
                getTimedValue(identifier, time, tdDataBase.BACKWINDOW_STEP);
        if (tdv != null) {
            return tdv.getValueChoiceScoreIndependent();
        } else {
            return 0;
        }
    }

    /**
     * Returns the mood choice score value. If there is no exact value for the
     * specific one, look for the previous one. If there is no previous one,
     * return zero. FIXME: this is not quite what I want...
     * 
     * @param identifier
     * @param time
     * @return
     */
    public double getChoiceScoreMood(String identifier, double time) {
        tdValue tdv =
                getTimedValue(identifier, time, tdDataBase.BACKWINDOW_STEP);
        if (tdv != null) {
            return tdv.getValueChoiceScoreMood();
        } else {
            return 0;
        }
    }

    /**
     * Returns the focus value of the specific component (instance or VI) at a
     * specific time. If there is no exact value for the specific one, look for
     * the previous one. If there is no previous one, return zero.
     * 
     * @param identifier - the identifier of the component
     * @param time
     * @return
     */
    public double getEnergy(String identifier, String ec, double time) {
        tdValue tdv = getTimedValue(identifier, time, tdDataBase.BACKWINDOW_DA);
        if (tdv != null) {
            return tdv.getValueEnergy(ec);
        } else {
            return 0;
        }
    }

    /**
     * Returns the list of all the instances and scenes which appeared in the
     * focus during the time
     * 
     * @return
     */
    public List<tdComponent> getFocusInstances() {
        return focusInstances;
    }

    /**
     * Returns the list of all the instances and scenes which appeared in the
     * focus during the time of the activity of the TDO
     * 
     * @return
     */
    public List<tdComponent> getFocusVis() {
        return focusVis;
    }

    /**
     * Returns the specified link value
     * 
     * @param identifier
     * @param identifier2
     * @param linkName
     * @param time
     * @return
     */
    public double getLinkValue(String fromId, String toId, String linkName,
            Double time) {
        tdValue tdv = getTimedValue(fromId, toId, linkName, time);
        if (tdv != null) {
            return tdv.getValueLink();
        } else {
            return 0;
        }
    }

    /**
     * Returns the  salience value of the specific component (instance or
     * VI) at a specific time. If there is no exact value for the specific one,
     * look for the previous one. If there is no previous one, return zero.
     * 
     * @param component
     * @param time
     * @return
     */
    public double getSalience(String identifier, String ec, double time) {
        tdValue tdv = getTimedValue(identifier, time, Double.MAX_VALUE);
        if (tdv != null) {
            return tdv.getValueSalience(ec);
        } else {
            return 0;
        }
    }

    /**
     * Returns a list of the id's of the shadow components, in the order in the
     * largest shadow energy value
     * 
     * @param focusId
     * @param maxNumber
     *            - the maximum number of values to return (-1 for all)
     * @return
     */
    public List<String> getShadowComponents(String focusId, String ec,
            int maxNumber) {
        // collect the shadow id's and their maximum shadow energy
        Map<String, Double> collection = new HashMap<>();
        List<tdValue> list = values.get(focusId);
        for (tdValue val : list) {
            if (val.getType() != tdValue.tdValueType.SHADOW) {
                continue;
            }
            Double maxVal = collection.get(val.getIdSecondary());
            if (maxVal == null) {
                collection
                        .put(val.getIdSecondary(), val.getValueEnergy(ec));
            } else {
                double maxEnergy = Math.max(maxVal, val.getValueEnergy(ec));
                collection.put(val.getIdSecondary(), maxEnergy);
            }
        }
        List<SimpleEntry<String, Double>> tosort = new ArrayList<>();
        for (String key : collection.keySet()) {
            SimpleEntry<String, Double> entry =
                    new SimpleEntry<>(key, collection.get(key));
            tosort.add(entry);
        }
        Collections.sort(tosort, new Comparator<SimpleEntry<String, Double>>() {

            @Override
            public int compare(SimpleEntry<String, Double> o1,
                    SimpleEntry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });
        Collections.reverse(tosort);
        // ok, now create the retval
        List<String> retval = new ArrayList<>();
        for (int i = 0; i != tosort.size(); i++) {
            retval.add(tosort.get(i).getKey());
            if (i == maxNumber - 1) {
                break;
            }
        }
        return retval;
    }


    /**
     * Returns the appropriate FOCUS_MEMORY or CHOICE type td value for a
     * specified time. If there is no exact value for the specific one, look for
     * the previous one. If there is no previous one, return null.
     * 
     * 
     * @param idPrimary
     * @param time
     * @param backWindow
     *            - the amount of time it reports the previous one if not found
     * @return
     */
    private tdValue getTimedValue(String idPrimary, double time,
            double backWindow) {
        List<tdValue> list = values.get(idPrimary);
        if (list == null) {
            return null;
        }
        tdValue lastFitting = null;
        for (tdValue tdv : list) {
            if (tdv.getType() != tdValue.tdValueType.FOCUS_MEMORY
                    && tdv.getType() != tdValue.tdValueType.CHOICE) {
                continue;
            }
            if (tdv.getTime() == time) {
                return tdv;
            }
            if (tdv.getTime() > time) {
                break;
            }
            lastFitting = tdv;
        }
        // if didn't find exact, return lastFitting
        if (lastFitting != null && time <= lastFitting.getTime() + backWindow) {
            return lastFitting;
        } else {
            return null;
        }
    }

    /**
     * Returns the appropriate SHADOW td value for a specified time. If there is
     * no exact value for the specific one, look for the previous one. If there
     * is no previous one, return null
     * 
     * 
     * @param idPrimary
     * @param idSecondary
     * @param time
     * @param backWindow
     *            - the amount of time for which we report the previous one if
     *            not found
     * 
     * @return
     */
    private tdValue getTimedValue(String idPrimary, String idSecondary,
            double time, double backWindow) {
        List<tdValue> list = values.get(idPrimary);
        if (list == null) {
            return null;
        }
        tdValue lastFitting = null;
        for (tdValue tdv : list) {
            if (tdv.getType() != tdValue.tdValueType.SHADOW) {
                continue;
            }
            if (!tdv.getIdSecondary().equals(idSecondary)) {
                continue;
            }
            if (tdv.getTime() == time) {
                return tdv;
            }
            if (tdv.getTime() > time) {
                break;
            }
            lastFitting = tdv;
        }
        // if didn't find exact, return lastFitting
        if (lastFitting != null && time <= lastFitting.getTime() + backWindow) {
            return lastFitting;
        } else {
            return null;
        }
        // return lastFitting;
    }

    /**
     * Returns the appropriate LINK td value for a specified time. Because links
     * only increase in time, the previous one is always appropriate!
     * 
     * 
     * @param idPrimary
     * @param idSecondary
     * @param time
     * @param backWindow
     *            - the amount of time for which we report the previous one if
     *            not found
     * 
     * @return
     */
    private tdValue getTimedValue(String idPrimary, String idSecondary,
            String linkName, double time) {
        List<tdValue> list = values.get(idPrimary);
        if (list == null) {
            return null;
        }
        tdValue lastFitting = null;
        for (tdValue tdv : list) {
            if (tdv.getType() != tdValue.tdValueType.LINK) {
                continue;
            }
            if (!tdv.getIdSecondary().equals(idSecondary)) {
                continue;
            }
            if (!tdv.getLinkName().equals(linkName)) {
                continue;
            }
            if (tdv.getTime() == time) {
                return tdv;
            }
            if (tdv.getTime() > time) {
                break;
            }
            lastFitting = tdv;
        }
        // if couldn't find exact, return lastFitting (possibly null)
        return lastFitting;
    }

    /**
     * Returns the earliest time in the database
     * 
     * @return
     */
    public double getTimeEnd() {
        return timeEnd;
    }

    /**
     * Returns the earliest time in the database
     * 
     * @return
     */
    public double getTimeStart() {
        return timeStart;
    }

    /**
     * Records everything about the current focus at the current time
     * 
     * @param agent
     */
    public void record(Agent agent) {
        if (timeStart == -1) {
            timeStart = agent.getTime();
        }
        timeEnd = agent.getTime();
        for (Instance scene : agent.getFocus().getSceneListAllEnergies()) {
            record(scene, agent);
        }
        for (Instance instance : agent.getFocus().getInstanceListAllEnergies()) {
            record(instance, agent);
        }
        for (VerbInstance vi : agent.getFocus().getViListAllEnergies()) {
            record(vi, agent);
        }
        for (Choice choice : agent.getHeadlessComponents().getChoices(
                HeadlessComponents.comparatorDependentScore)) {
            record(choice, agent);
        }
    }

    /**
     * Records a choice
     * 
     * @param choice
     * @param agent
     */
    private void record(Choice choice, Agent agent) {
        // ignore the ones which are not HLS based, for the time being
        if (!ChoiceTypeHelper.isHlsBased(choice)) {
            return;
        }
        // if it is already present, find it, othervise create a tdComponent
        // and add it to the list of choices
        
        VerbInstance template = choice.getHls().getViTemplate();
        tdComponent tdcExisting = null;
        for (tdComponent tdcChoice : choices) {
            VerbInstance model = tdcChoice.getChoice().getHls().getViTemplate();
            ChoiceType type = tdcChoice.getChoice().getChoiceType();
            if (type == choice.getChoiceType()
                    && ViSimilarityHelper.decideSimilarityVi(template, model,
                            agent, true)) {
                tdcExisting = tdcChoice;
                break;
            }
        }
        if (tdcExisting == null) {
            tdcExisting = new tdComponent(choice);
            choices.add(tdcExisting);
        }
        tdValue tdv = tdValue.createChoice(tdcExisting, choice, agent);
        addTdValue(tdv);
    }

    /**
     * Records everything about an instance in the current moment
     * 
     * @param instance
     * @param agent
     */
    private void record(Instance instance, Agent agent) {
        recordFocusInstance(instance, agent);
        // focus and memory
        tdValue tdv = tdValue.createFocusMemory(instance, agent);
        addTdValue(tdv);
        // all the shadows
        Shadows sh = agent.getShadows();
        List<Instance> members = sh.getMembersAnyEnergy(instance);
        for (Instance si : members) {
            tdv = tdValue.createShadow(instance, si, agent);
            addTdValue(tdv);
        }
    }

    /**
     * Records everything about a VI in the current moment
     * 
     * @param vi
     * @param agent
     */
    private void record(VerbInstance vi, Agent agent) {
        recordFocusVi(vi, agent);
        // focus and memory
        tdValue tdv = tdValue.createFocusMemory(vi, agent);
        addTdValue(tdv);
        // all the shadows
        Shadows sh = agent.getShadows();
        List<VerbInstance> members = sh.getMembersAnyEnergy(vi);
        for (VerbInstance svi : members) {
            tdv = tdValue.createShadow(vi, svi, agent);
            addTdValue(tdv);
        }
        // all the links: only add to Vis which are currently in the focus,
        // because the other ones won't change
        for (String linkName : agent.getLinks().getLinkTypeNames()) {
            ViSet link = agent.getLinks().getLinksByLinkName(vi, linkName);
            for (VerbInstance tovi : agent.getFocus().getViList(EnergyColors.FOCUS_VI)) {
                // don't add it if it is not in the focus, because we will fall
                // back to it
                double linkValue = link.value(tovi);
                if (linkValue == 0.0) {
                    continue;
                }
                tdv = tdValue.createLink(vi, tovi, agent, linkName, linkValue);
                addTdValue(tdv);
            }
        }
    }

    /**
     * Records the focus instance in the list of focus instances
     * 
     * 
     * @param instance
     * @param agent
     */
    private void recordFocusInstance(Instance instance, Agent agent) {
        tdComponent tdcThis = null;
        for (tdComponent tdc : focusInstances) {
            if (tdc.getIdentifier().equals(instance.getIdentifier())) {
                tdcThis = tdc;
                break;
            }
        }
        // if it is not there yet, create it
        if (tdcThis == null) {
            tdcThis =
                    new tdComponent(tdComponentType.INSTANCE,
                            instance.getIdentifier());
            focusInstances.add(tdcThis);
        }
        String pp = SpInstance.spc(instance, agent);
        tdcThis.recordPrettyPrint(pp);
    }

    /**
     * Records the focus VI in the list of focus VIs
     * 
     * @param instance
     */
    private void recordFocusVi(VerbInstance vi, Agent agent) {
        tdComponent tdcThis = null;
        for (tdComponent tdc : focusVis) {
            if (tdc.getIdentifier().equals(vi.getIdentifier())) {
                tdcThis = tdc;
                break;
            }
        }
        // if it is not there yet, create it
        if (tdcThis == null) {
            tdcThis = new tdComponent(tdComponentType.VI, vi.getIdentifier());
            focusVis.add(tdcThis);
        }
        String pp = SpFocus.ppsViXapiForm(vi, agent);
        tdcThis.recordPrettyPrint(pp);
    }

    /**
     * Gets the salience of a shadow value
     * 
     * @param identifier
     * @param sh
     * @param ec - the energy color
     * @param time
     * @return
     */
    public double getSalience(String identifier, String sh,
            String ec, Double time) {
        tdValue tdv = getTimedValue(identifier, sh, time, Double.MAX_VALUE);
        if (tdv != null) {
            return tdv.getValueSalience(ec);
        } else {
            return 0;
        }
    }

    /**
     * Gets the energy of a shadow for a certain value
     * 
     * @param identifier
     * @param sh
     * @param ec - the energy color of interest
     * @param time
     * @return
     */
    public double getEnergy(String identifier, String sh,
            String ec, Double time) {
        tdValue tdv = getTimedValue(identifier, sh, time, Double.MAX_VALUE);
        if (tdv != null) {
            return tdv.getValueEnergy(ec);
        } else {
            return 0;
        }
    }

}
