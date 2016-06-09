/*
   This file is part of the Xapagy project
   Created on: Nov 14, 2012

   org.xapagy.shadows.ShadowQuantum

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.set;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.TextUi;

/**
 * Represents a particular quantum of change in the energy in an energy set
 *
 * @author Ladislau Boloni
 *
 */
public class EnergyQuantum<T extends XapagyComponent> implements Serializable {

    /**
     * Different types of energy quantums: STANDALONE - the energy is created
     * from outside sources, TRANSFER_FROM - this is a decrease, the energy is
     * used up in a transformation, TRANSFER_TO - this is an increase, the
     * energy is increased by the transformed energy
     *
     */
    public enum EnergyQuantumRelation {
        STANDALONE, TRANSFER_FROM, TRANSFER_TO
    };

    // neutral addition
    public static final double ADDITION_NEUTRAL = 0.0;

    // neutral multiplier
    public static final double MULTIPLIER_NEUTRAL = 1.0;
    private static final long serialVersionUID = -973176541178431877L;
    // full strength
    public static final double STRENGTH_FULL = 1.0;
    // one full timeslice
    public static final double TIMESLICE_ONE = 1.0;

    /**
     * Factory function, creates an addition for a component on focus or memory.
     * Allows the specification of the timeslice, so appropriate for DAs.
     *
     * @param t
     * @param additiveChange
     * @param timeSlice
     * @param ec
     * @param source
     * @return
     */
    public static <T extends XapagyComponent> EnergyQuantum<T> createAdd(T t,
            double additiveChange, double timeSlice, String ec,
            String source) {
        EnergyQuantum<T> eq =
                new EnergyQuantum<>(t, null, additiveChange,
                        EnergyQuantum.MULTIPLIER_NEUTRAL, timeSlice, source, ec);
        return eq;
    }

    /**
     * Factory function, creates an addition for an component on focus or
     * memory. Assumes full timeslice, so it is appropriate for SAs
     *
     * @param instance
     * @param additiveChange
     * @param ec
     * @param source
     * @return
     */
    public static <T extends XapagyComponent> EnergyQuantum<T> createAdd(T t,
            double additiveChange, String ec, String source) {
        EnergyQuantum<T> eq =
                new EnergyQuantum<>(t, null, additiveChange,
                        EnergyQuantum.MULTIPLIER_NEUTRAL,
                        EnergyQuantum.TIMESLICE_ONE, source, ec);
        return eq;
    }

    /**
     * Factory function, creates an addition the fi / si pairing. Allows the
     * specification of the timeslice, so appropriate for DAs.
     *
     * @param fi
     *            - the focus instance
     * @param si
     *            - the shadow instance
     * @param additiveChange
     * @param timeSlice
     * @param ec
     * @param source
     * @return
     */
    public static <T extends XapagyComponent> EnergyQuantum<T> createAdd(T fi,
            T si, double additiveChange, double timeSlice, String ec,
            String source) {
        EnergyQuantum<T> eq =
                new EnergyQuantum<>(fi, si, additiveChange,
                        EnergyQuantum.MULTIPLIER_NEUTRAL, timeSlice, source, ec);
        return eq;
    }

    /**
     * Create a collection of energy quantums which represent an addition to
     * both a VI as well as to the instance components of the VI. If this
     * applies in a shadowing context, the VIs must have a matching type,
     *
     * @param fvi
     * @param svi
     * @param additiveChange
     * @param timeSlice
     * @param ratioInstanceToVi
     *            - the ratio of the energy given to the instances, compared
     *            with the one given to the VI
     * @param source
     *            - the description of the source generating the quantum
     * @param ecVi
     *            - the energy color added for the VIs
     * @param ecInstance
     *            - the energy color added for the instances
     */
    public static
    SimpleEntry<EnergyQuantum<VerbInstance>, List<EnergyQuantum<Instance>>>
    createCompositeAdd(VerbInstance fvi, VerbInstance svi,
            double additiveChange, double timeSlice,
            double ratioInstanceToVi, String source, String ecVi,
            String ecInstance) {
        //
        // Create the quantum for the VI
        //
        EnergyQuantum<VerbInstance> eq =
                EnergyQuantum.createAdd(fvi, svi, additiveChange, timeSlice,
                        ecVi, source + " + createCompositeAdd-Vi");
        //
        // Create the quantums for the instance members
        //
        double instanceAdditiveChange = additiveChange * ratioInstanceToVi;
        List<EnergyQuantum<Instance>> eqInstances = new ArrayList<>();
        for (ViPart part : ViStructureHelper.getAllowedInstanceParts(fvi
                .getViType())) {
            try {
                EnergyQuantum<Instance> eq2 =
                        EnergyQuantum.createAdd((Instance) fvi.getPart(part),
                                (Instance) svi.getPart(part),
                                instanceAdditiveChange, timeSlice, ecInstance,
                                source + " + createCompositeAdd-Instance");
                eqInstances.add(eq2);
            } catch (NullPointerException npe) {
                TextUi.println("WTF");
            }
        }
        SimpleEntry<EnergyQuantum<VerbInstance>, List<EnergyQuantum<Instance>>> retval =
                new SimpleEntry<>(eq, eqInstances);
        return retval;
    }

    /**
     * Factory function, creates a multiplication for a component on focus or
     * memory. Allows the specification of the timeslice, so suitable for DAs.
     *
     * @param t
     * @param additiveChange
     * @param timeSlice
     * @param ec
     * @param source
     * @return
     */
    public static <T extends XapagyComponent> EnergyQuantum<T> createMult(T t,
            double multiplicativeChange, double timeSlice, String ec,
            String source) {
        EnergyQuantum<T> eq =
                new EnergyQuantum<>(t, null, EnergyQuantum.ADDITION_NEUTRAL,
                        multiplicativeChange, timeSlice, source, ec);
        return eq;
    }

    /**
     * Factory function, creates a multiplication for an component on focus or
     * memory. It assumes a full timeslice, so suitable for SAs.
     *
     * @param t
     * @param additiveChange
     * @param ec
     * @param source
     * @return
     */
    public static <T extends XapagyComponent> EnergyQuantum<T> createMult(T t,
            double multiplicativeChange, String ec, String source) {
        EnergyQuantum<T> eq =
                new EnergyQuantum<>(t, null, EnergyQuantum.ADDITION_NEUTRAL,
                        multiplicativeChange, EnergyQuantum.TIMESLICE_ONE,
                        source, ec);
        return eq;
    }

    /**
     * Factory function, creates a multiplication for an fi/si pair in the
     * shadow. Allows the specification of the timeslice, so suitable for DAs.
     *
     * @param fi
     * @param si
     * @param additiveChange
     * @param timeSlice
     * @param ec
     * @param source
     * @return
     */
    public static <T extends XapagyComponent> EnergyQuantum<T> createMult(T fi,
            T si, double multiplicativeChange, double timeSlice,
            String ec, String source) {
        EnergyQuantum<T> eq =
                new EnergyQuantum<>(fi, si, EnergyQuantum.ADDITION_NEUTRAL,
                        multiplicativeChange, timeSlice, source, ec);
        return eq;
    }

    /**
     * Additive change - the change with which the shadow energy will be added
     * (can be negative)
     */
    private double additiveChange;
    /**
     * The agent time when the EnergyQuantum was applied
     */
    private double agentTimeWhenApplied;
    /**
     * If the shadow quantum had been applied, the i-th sq which had been
     * applied
     */
    private long applicationOrder = -1;
    /**
     * The SC_ALL energy after the quantum had been applied (for display
     * purposes only)
     */
    private double energyAfterQuantum = 0;
    /**
     * The SC_ALL energy before the quantum had been applied (for display
     * purposes only)
     */
    private double energyBeforeQuantum = 0;
    /**
     * The energy color modified by the quantum
     */
    private String energyColor;
    /**
     * The relation into which this energy quantum enters: default is standalone
     */
    private EnergyQuantumRelation energyQuantumRelation =
            EnergyQuantumRelation.STANDALONE;
    /**
     * The focus item of the shadow to which the change is applied
     */
    private T focusComponent;
    /**
     * Multiplicative change - the change with which the shadow energy will be
     * multiplied
     */
    private double multiplicativeChange;
    /**
     * The shadow component of the shadow energy to which the change is applied
     */
    private T shadowComponent;
    /**
     * A string describing the DA which created
     */
    private String source;
    /**
     * The time slice over which the quantum is applied
     */
    private double timeSlice;

    /**
     * The transfer pair of this energy quantum - null by default, for standalone
     */
    private EnergyQuantum<T> transferPair = null;

    /**
     * Create a shadow quantum with the specific parameters
     *
     * @param focusComponent
     * @param shadowComponent
     * @param additiveChange
     * @param multiplicativeChange
     * @param timeSlice
     * @param source
     *            - a string descriptor, which allows us to describe where the
     *            quantum is coming from
     * @param color
     *            - the energy color
     */
    private EnergyQuantum(T focusComponent, T shadowComponent,
            double additiveChange, double multiplicativeChange,
            double timeSlice, String source, String energyColor) {
        super();
        this.focusComponent = focusComponent;
        this.shadowComponent = shadowComponent;
        this.additiveChange = additiveChange;
        this.multiplicativeChange = multiplicativeChange;
        this.timeSlice = timeSlice;
        this.source = source;
        // there is not supposed to be negative additive change
        if (additiveChange < 0) {
            throw new Error("EnergyQuantum with negative additive change!!!");
        }
        this.energyColor = energyColor;
    }

    /**
     * @return the additiveChange
     */
    public double getAdditiveChange() {
        return additiveChange;
    }

    /**
     * @return the agentTimeWhenApplied
     */
    public double getAgentTimeWhenApplied() {
        return agentTimeWhenApplied;
    }

    /**
     * @return the applicationCount
     */
    public long getApplicationOrder() {
        return applicationOrder;
    }

    /**
     * @return the energyAfterQuantum
     */
    public double getEnergyAfterQuantum() {
        return energyAfterQuantum;
    }

    /**
     * @return the energyBeforeQuantum
     */
    public double getEnergyBeforeQuantum() {
        return energyBeforeQuantum;
    }

    public String getEnergyColor() {
        return energyColor;
    }

    public EnergyQuantumRelation getEnergyQuantumRelation() {
        return energyQuantumRelation;
    }

    /**
     * @return the focusComponent
     */
    public T getFocusComponent() {
        return focusComponent;
    }

    /**
     * @return the multiplicativeChange
     */
    public double getMultiplicativeChange() {
        return multiplicativeChange;
    }

    /**
     * @return the shadowComponent
     */
    public T getShadowComponent() {
        return shadowComponent;
    }

    /**
     * Returns the source description
     *
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the first component of the source description
     *
     * @return
     */
    public String getSourceOriginator() {
        int end = source.indexOf("+");
        if (end == -1) {
            return source;
        }
        String first = source.substring(0, end - 1);
        first = first.trim();
        return first;
    }

    /**
     * @return the timeSlice
     */
    public double getTimeSlice() {
        return timeSlice;
    }

    public EnergyQuantum<T> getTransferPair() {
        return transferPair;
    }

    /**
     * Marks the application of the EnergyQuantum: the application order and the
     * agent time when it was applied.
     *
     * @param order
     *            the applicationCount to set
     */
    public void markApplication(long order, double agentTime) {
        if (this.applicationOrder != -1) {
            throw new Error(
                    "Attempt to apply an energy quantum more than once!!!");
        }
        this.applicationOrder = order;
        this.agentTimeWhenApplied = agentTime;
    }

    /**
     * @param energyAfterQuantum
     *            the energyAfterQuantum to set
     */
    public void setEnergyAfterQuantum(double energyAfterQuantum) {
        this.energyAfterQuantum = energyAfterQuantum;
    }

    /**
     * @param energyBeforeQuantum
     *            the energyBeforeQuantum to set
     */
    public void setEnergyBeforeQuantum(double energyBeforeQuantum) {
        this.energyBeforeQuantum = energyBeforeQuantum;
    }

    public void setEnergyQuantumRelation(
            EnergyQuantumRelation energyQuantumRelation) {
        this.energyQuantumRelation = energyQuantumRelation;
    }

    public void setTransferPair(EnergyQuantum<T> tranferPair) {
        this.transferPair = tranferPair;
    }

}
