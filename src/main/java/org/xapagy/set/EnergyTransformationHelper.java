/*
   This file is part of the Xapagy project
   Created on: Nov 5, 2014
 
   org.xapagy.set.EnergyTransformationHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.set;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.set.EnergyQuantum.EnergyQuantumRelation;
import org.xapagy.shadows.Shadows;

/**
 * This class contains helper functions for performing an energy transformation.
 * This is always done through two energy quantums
 * 
 * @author Ladislau Boloni
 *
 */
public class EnergyTransformationHelper {

    /**
     * The standard ratio of the source energy to be transformed. If this is set
     * to 1.0, it will be eaten up immediately in the first iteration... with a
     * resolution of 10 /sec this seems to transfer about 1/2 of the initial
     * salience (see testEnergyTransformationHelper)
     */
    public static double STANDARD_RATIO = 0.9;

    /**
     * Salience to energy conversion for shadow energy
     * 
     * @param agent
     * @param focusT
     * @param shadowT
     * @param fraction
     *            - the fraction of the source energy to be converted (if time
     *            10)
     * @param multiplier
     *            - the multiplier with which the salience will be multiplied to
     *            become the destination energy
     * @param timeSlice
     * @param ecFrom
     * @param ecTo
     * @param documentation
     *            - description of the transfer
     * @return
     */
    public static <T extends XapagyComponent>
            SimpleEntry<EnergyQuantum<T>, EnergyQuantum<T>>
            createS2E(Agent agent, T focusT, T shadowT, double fraction,
                    double multiplier, double timeSlice, String ecFrom,
                    String ecTo, String documentation) {
        Shadows sf = agent.getShadows();
        double salience = 0;
        // double energy = 0;
        if (focusT instanceof Instance) {
            salience = sf.getSalience((Instance) focusT, (Instance) shadowT,
                    ecFrom);
        } else {
            salience = sf.getSalience((VerbInstance) focusT,
                    (VerbInstance) shadowT, ecFrom);
        }
        double addition = salience * fraction * multiplier;
        double multiplicativeChange = 1 - fraction;
        EnergyQuantum<T> eqFrom = EnergyQuantum.createMult(focusT, shadowT,
                multiplicativeChange, timeSlice, ecFrom, documentation + " + From");
        eqFrom.setEnergyQuantumRelation(EnergyQuantumRelation.TRANSFER_FROM);
        EnergyQuantum<T> eqTo = EnergyQuantum.createAdd(focusT, shadowT,
                addition, timeSlice, ecTo, documentation + " + To");
        eqTo.setEnergyQuantumRelation(EnergyQuantumRelation.TRANSFER_TO);
        eqFrom.setTransferPair(eqTo);
        eqTo.setTransferPair(eqFrom);
        return new SimpleEntry<>(eqFrom, eqTo);
    }

}
