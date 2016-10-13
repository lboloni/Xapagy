/*
   This file is part of the Xapagy project
   Created on: Aug 23, 2010
 
   org.xapagy.model.Overlay
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.concepts;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.util.SimpleEntryComparator;

/**
 * 
 * Represents an overlay of concepts.
 * 
 * @author Ladislau Boloni
 * 
 */
public abstract class Overlay<T extends AbstractConcept> extends Observable
        implements Serializable {

    private static final long serialVersionUID = -1998628957350822686L;

    /**
     * Removes all the concepts which are present in scraper from the orig CO.
     * 
     * Helper function, frequently used.
     * 
     * @param <P>
     * 
     * @param orig
     * @param scraper
     * @return
     */
    public static <T extends Overlay<P>, P extends AbstractConcept> T scrape(
            T orig, T scraper) {
        @SuppressWarnings("unchecked")
        T reminder = (T) orig.newOverlay();
        reminder.addOverlay(orig);
        reminder.addOverlay(scraper, -100.0);
        return reminder;
    }

    protected Agent agent;
    protected AbstractConceptDB<T> cdb;
    private Map<T, Double> energy = new HashMap<>();
    protected String identifier = null;

    /**
     * Labels - return zero if none
     */
    private List<String> labels = new ArrayList<>();

    /**
     * Simple constructor.
     * 
     * @param cdb
     */
    protected Overlay(AbstractConceptDB<T> cdb, Agent agent) {
        this.cdb = cdb;
        this.agent = agent;
    }

    /**
     * Basic function for adding energy.
     * 
     * Note: Does not notify the observers, this has to be done from elsewhere
     * 
     * @param concept
     * @param increase
     */
    private void addEnergyBasic(T concept, double increase) {
        if (concept == null) {
            throw new Error("Trying to increase the area of a null concept");
        }
        double oldEnergy = getExplicitEnergy(concept);
        double newEnergy = increase + oldEnergy;
        if (newEnergy > cdb.getArea(concept)) {
            newEnergy = cdb.getArea(concept);
        }
        if (newEnergy <= 0.0) {
            energy.remove(concept);
        } else {
            energy.put(concept, newEnergy);
        }
    }

    /**
     * Basic function for adding energy with impact.
     * 
     * Note: Does not notify the observers, this has to be done from elsewhere
     * 
     * @param concept
     * @param increase
     */
    public void addEnergyImpactedBasic(T concept, double increase) {
        addEnergyBasic(concept, increase);
        // now do the impacts only for increase, not inhibition! ???
        if (increase > 0.0) {
            impact(concept, increase, false, null);
        }
    }

    /**
     * Adds a concept with full energy
     * 
     * @param concept
     * @param increase
     */
    public void addFullEnergy(T concept) {
        double area = 0;
        if (concept instanceof Concept) {
            area = agent.getConceptDB().getArea((Concept) concept);
        } else {
            area = agent.getVerbDB().getArea((Verb) concept);
        }
        addEnergyBasic(concept, area);
        setChanged();
        notifyObservers();
    }

    /**
     * Adds a concept with full energy
     * 
     * @param concept
     * @param increase
     */
    public void addFullEnergyImpacted(T concept) {
        double area = 0;
        if (concept instanceof Concept) {
            area = agent.getConceptDB().getArea((Concept) concept);
        } else {
            area = agent.getVerbDB().getArea((Verb) concept);
        }
        addEnergyImpactedBasic(concept, area);
        setChanged();
        notifyObservers();
    }

    /**
     * Adds a label and create the labels if necessary
     * 
     * @param label
     */
    public void addFullLabel(String label, Agent agent) {
        String fullLabel = agent.getLabelSpaces().fullLabel(label);
        labels.add(fullLabel);
    }

    /**
     * Adds an other overlay with the ratio 1.0. This function is necessary to
     * make the other one a hook for special cases
     * 
     * @param other
     */
    public void addOverlay(Overlay<T> other) {
        addOverlay(other, 1.0);
    }

    /**
     * Adds another overlay with a specified ratio (which can be positive or
     * negative).
     * 
     * @param other
     * @param ratio
     * 
     */
    public void addOverlay(Overlay<T> other, double ratio) {
        for (SimpleEntry<T, Double> entry : other.getList()) {
            addEnergyBasic(entry.getKey(), entry.getValue() * ratio);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Adds another overlay in an impacted way with the ratio 1.0. Extracting
     * this frequence case to make the other one searchable.
     * 
     * @param other
     */
    public void addOverlayImpacted(Overlay<T> other) {
        addOverlayImpacted(other, 1.0);
    }

    /**
     * Adds another overlay with a specified ratio (which can be positive or
     * negative) in an impacted way
     * 
     * @param other
     * @param ratio
     * 
     */
    public void addOverlayImpacted(Overlay<T> other, double ratio) {
        for (SimpleEntry<T, Double> entry : other.getList()) {
            addEnergyImpactedBasic(entry.getKey(), entry.getValue() * ratio);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Adds energy to a concept in the overlay. It is limited by the area of the
     * concept.
     * 
     * For external use: sets changed and notifies the observers
     * 
     * @param concept
     * @param increment
     */
    public void addSpecificEnergy(T concept, double increase) {
        addEnergyBasic(concept, increase);
        setChanged();
        notifyObservers();
    }

    /**
     * Adds a specific energy in an impacted way (for external use)
     * 
     * For external use: sets changed and notifies the observers
     * 
     * @param concept
     * @param increase
     */
    public void addSpecificEnergyImpacted(T concept, double increase) {
        addEnergyImpactedBasic(concept, increase);
        // now do the impacts only for increase, not inhibition! ???
        setChanged();
        notifyObservers();
    }

    /**
     * @return
     */
    public Set<T> getAsSet() {
        Set<T> retval = new HashSet<>(energy.keySet());
        return retval;
    }

    /**
     * @return the cdb
     */
    public AbstractConceptDB<T> getCdb() {
        return cdb;
    }

    /**
     * Returns the most likely value of the energy of concept (the maximum value
     * from the range)
     * 
     * @param concept
     * @return
     */
    public double getEnergy(T concept) {
        double d = getEnergyRange(concept).getValue();
        if (d > cdb.getArea(concept)) {
            d = cdb.getArea(concept);
        }
        return d;
    }

    /**
     * Returns the minimum and maximum values of the possible energy
     * 
     * @return
     */
    private SimpleEntry<Double, Double> getEnergyRange(T concept) {
        Double explicit = getExplicitEnergy(concept);
        double minimum = explicit;
        double maximum = explicit;
        // minimum: the maximum over the implied ones
        // maximum: their sum
        for (T peer : energy.keySet()) {
            if (peer.equals(concept)) {
                continue;
            }
            double impliedValue = cdb.getOverlap(concept, peer);
            minimum = Math.max(minimum, impliedValue);
            maximum = maximum + impliedValue;
        }
        return new SimpleEntry<>(minimum, maximum);
    }

    /**
     * Returns the energy which the concept explicitly have been allocated in
     * the overlay. This does not include the one which it acquired through
     * overlap.
     * 
     * @param concept
     * @param excitation
     */
    private double getExplicitEnergy(T concept) {
        Double x = energy.get(concept);
        if (x == null) {
            return 0.0;
        } else {
            return x;
        }
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns a list of labels (unmodifiable)
     * 
     * @return
     */
    public List<String> getLabels() {
        return Collections.unmodifiableList(labels);
    }

    /**
     * Returns the list of components in an arbitrary order
     * 
     * @return
     */
    public List<SimpleEntry<T, Double>> getList() {
        List<SimpleEntry<T, Double>> list = new ArrayList<>();
        for (T concept : energy.keySet()) {
            double value = energy.get(concept);
            list.add(new SimpleEntry<>(concept, value));
        }
        return list;
    }

    /**
     * Returns a list of the components, sorted by their explicit area, in
     * decreasing order
     * 
     * @return
     */
    public List<SimpleEntry<T, Double>> getSortedByExplicitEnergy() {
        List<SimpleEntry<T, Double>> list = new ArrayList<>();
        for (T concept : energy.keySet()) {
            double value = energy.get(concept);
            list.add(new SimpleEntry<>(concept, value));
        }
        Collections.sort(list,
                Collections.reverseOrder(new SimpleEntryComparator<T>()));
        return list;
    }

    /**
     * Returns the total area of the overlay
     * 
     * @return
     */
    public double getTotalEnergy() {
        double retval = 0;
        for (Double d : energy.values()) {
            retval += d;
        }
        return retval;
    }

    /**
     * Performs the impacts of an overlay over an overlay, by calling the single
     * concept impact function repeatedly.
     * 
     * This is used in Incompatibility (legitly)
     * 
     * and QuestionAnswerPairing from where it should be factored out
     * 
     * @param concept
     * @param increaseRatio
     * @return
     */
    public SimpleEntry<Double, Double> impact(Overlay<T> other, double ratio,
            boolean simulateOnly, Overlay<T> collectOverImpact) {
        double positiveImpactEnergy = 0.0;
        double negativeImpactEnergy = 0.0;
        for (SimpleEntry<T, Double> entry : other.getList()) {
            double impactEnergy =
                    impact(entry.getKey(), entry.getValue() * ratio,
                            simulateOnly, collectOverImpact);
            if (impactEnergy > 0) {
                positiveImpactEnergy += impactEnergy;
            } else {
                negativeImpactEnergy += impactEnergy;
            }
        }
        if (!simulateOnly) {
            setChanged();
            notifyObservers();
        }
        return new SimpleEntry<>(positiveImpactEnergy, negativeImpactEnergy);
    }

    /**
     * Performs the impacts of a concept on an overlay
     * 
     * @param concept
     * @param increaseRatio
     * @param collectOverImpact
     *            - overlay to collect the overimpact
     * @return the total change of energy
     */
    public double impact(T concept, double increase, boolean simulateOnly,
            Overlay<T> collectOverImpact) {
        double energyAdded = 0.0;
        double newEnergyAdded =
                impactImmediate(concept, increase, simulateOnly,
                        collectOverImpact);
        energyAdded += newEnergyAdded;
        if (!simulateOnly && energyAdded != 0.0) {
            setChanged();
            notifyObservers();
        }
        return energyAdded;
    }

    /**
     * Implements the immediate impact
     * 
     * Returns the new energy added to the overlay And the overimpact - the
     * amount of energy which would be wasted by increasing the amount above the
     * maximum
     * 
     * @param concept
     * @param increaseRatio
     * @return the total change of energy + overimpact
     */
    public double impactImmediate(T concept, double increase,
            boolean simulateOnly, Overlay<T> collectOverImpact) {
        Map<T, Double> implications = cdb.getImpacts(concept);
        double energyAdded = 0.0;
        if (implications != null) {
            for (Entry<T, Double> entry : implications.entrySet()) {
                double currentArea = getExplicitEnergy(entry.getKey());
                double areaChange = increase * entry.getValue();
                double newArea = currentArea + areaChange;
                if (newArea <= 0.0) {
                    newArea = 0.0;
                    energyAdded = energyAdded - currentArea;
                    if (!simulateOnly) {
                        energy.remove(entry.getKey());
                    }
                    continue;
                }
                if (newArea > cdb.getArea(entry.getKey())) {
                    // TextUi.println("B1");
                    double newOverImpact =
                            newArea - cdb.getArea(entry.getKey());
                    if (collectOverImpact != null) {
                        collectOverImpact.addSpecificEnergy(entry.getKey(),
                                newOverImpact);
                    }
                    newArea = cdb.getArea(entry.getKey());
                    energyAdded = energyAdded + newArea - currentArea;
                    if (!simulateOnly) {
                        energy.put(entry.getKey(), newArea);
                    }
                    continue;
                }
                if (newArea != currentArea) {
                    // TextUi.println("B2");
                    energyAdded = energyAdded + newArea - currentArea;
                    if (!simulateOnly) {
                        energy.put(entry.getKey(), newArea);
                    }
                    continue;
                }
            }
        }
        return energyAdded;
    }

    /**
     * Function allowing to create new overlays
     * 
     * @return
     */
    public abstract Overlay<T> newOverlay();

    /**
     * Returns the sum of overlaps between this overlay and the other overlay
     * 
     * @param current
     * @param other
     * @param db
     * @return
     */
    public double overlapEnergy(Overlay<T> other) {
        double overlap = 0;
        for (SimpleEntry<T, Double> entry : getList()) {
            double val = other.getEnergy(entry.getKey());
            overlap = overlap + Math.min(entry.getValue(), val);
        }
        return overlap;
    }

    /**
     * Returns true if this overlay covers the same concepts as the other one, AND
     * it has all the labels of other as well
     * 
     * @param other
     * @return
     */
    public boolean coversWithLabels(Overlay<T> other) {
        if (!covers(other)) {
            return false;
        }
        for(String label: other.getLabels()) {
            if (!getLabels().contains(label)) {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Returns true if this overlay covers the same concepts as the other one.
     * It is ok if this overlay is larger
     * 
     * @param other
     * @return
     */
    public boolean covers(Overlay<T> other) {
        return overlapEnergy(other) == other.getTotalEnergy();
    }
    
    /**
     * Returns true if this overlay covers the same concept as the other 
     * overlay... It is the case if their overlap energy are the same as their energies.
     * 
     * @param other
     * @return
     */
    public boolean sameCoverage(Overlay<T> other) {
       double myEnergy = getTotalEnergy();
       if (other.getTotalEnergy() != myEnergy) {
           return false;
       }
       if (overlapEnergy(other) != myEnergy) {
           return false;
       }
       return true; 
    }
    
    
    /**
     * Removes the energy for a specific concept
     * 
     * @param concept
     */
    public void scrapeEnergy(T concept) {
        addEnergyBasic(concept, -100);
        setChanged();
        notifyObservers();
    }

    /**
     * Returns an overlay which removes from the explicit energy in coFrom the
     * energy which is also present in coToSubtract
     * 
     * It is not symmetrical!
     * 
     * @return
     */
    public Overlay<T> subtract(Overlay<T> coToSubtract) {
        Overlay<T> retval = newOverlay();
        for (SimpleEntry<T, Double> entry : getList()) {
            T c = entry.getKey();
            double surplus = getExplicitEnergy(c) - coToSubtract.getEnergy(c);
            if (surplus > 0) {
                retval.addSpecificEnergy(c, surplus);
            }
        }
        return retval;
    }

    /**
     * toString falls back on the consise description, as it does not need
     * anything from the agent
     */
    @Override
    public String toString() {
        return PrettyPrint.ppString(this);
    }
}
