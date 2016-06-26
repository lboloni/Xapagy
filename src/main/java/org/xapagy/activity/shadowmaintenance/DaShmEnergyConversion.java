/*
   This file is part of the Xapagy project
   Created on: May 15, 2014
 
   org.xapagy.activity.shadowmaintenance.DaShmEnergyConversion
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.Overlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.concepts.operations.Coverage;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViSimilarityHelper;
import org.xapagy.parameters.Parameters;
import static org.xapagy.set.EnergyColors.*;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.set.EnergyTransformationHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * @author Ladislau Boloni
 * 
 */
public class DaShmEnergyConversion extends AbstractDaFocusIterator {
    private static final long serialVersionUID = -5491552198659189523L;

    /**
     * Describes the ratio at which the salience of SHI_ATTRIBUTE is converted
     * to energy of SHI_GENERIC
     */
    private double conv_S_SHI_ATTRIBUTE_TO_E_SHI_GENERIC;
    /**
     * Describes the ratio at which the salience of SHI_RELATION is converted to
     * energy of SHI_GENERIC
     */
    private double conv_S_SHI_RELATION_TO_E_SHI_GENERIC;
    /**
     * Describes the ratio at which the salience of SHI_RELATION is converted to
     * energy of SHI_GENERIC
     */
    private double conv_S_SHI_ACTION_TO_E_SHI_GENERIC;
    /**
     * Describes the ratio at which the salience of SHV_TEMPORAL_ORDER is
     * converted to energy of SHV_GENERIC
     */
    private double conv_S_SHV_TEMPORAL_ORDER_TO_E_SHV_GENERIC;
    /**
     * Describes the ratio at which the salience of SHV_ACTION_MATCH is
     * converted to energy of SHV_GENERIC
     */
    private double conv_S_SHV_ACTION_MATCH_TO_E_SHV_GENERIC;

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        conv_S_SHI_ATTRIBUTE_TO_E_SHI_GENERIC =
                getParameterDouble("S_SHI_ATTRIBUTE_TO_E_SHI_GENERIC");
        conv_S_SHI_RELATION_TO_E_SHI_GENERIC =
                getParameterDouble("S_SHI_RELATION_TO_E_SHI_GENERIC");
        conv_S_SHI_ACTION_TO_E_SHI_GENERIC =
                getParameterDouble("S_SHI_ACTION_TO_E_SHI_GENERIC");
        conv_S_SHV_TEMPORAL_ORDER_TO_E_SHV_GENERIC =
                getParameterDouble("S_SHV_TEMPORAL_ORDER_TO_E_SHV_GENERIC");
        conv_S_SHV_ACTION_MATCH_TO_E_SHV_GENERIC =
                getParameterDouble("S_SHV_ACTION_MATCH_TO_E_SHV_GENERIC");
    }

    /**
     * For shadow instance energies, calculates the conversion from one energy
     * salience to another energy for all the shadows of one energy
     * 
     * @param fi
     *            - the focus item who's shadows we are dealing with
     * @param time
     *            - the time over with we are calculating
     * @param ecFrom
     *            - the "from" energy color
     * @param ecTo
     *            - the "to" energy color
     * @param transformationFraction
     *            - the fraction of the "from" energy that it taken away in one
     *            1
     * @param conversionRatio
     *            - the ratio over which the salience if the "from" energy is
     *            converted into the "to" energy
     * @return a list of quantums for the instance shadow energies
     */
    private List<EnergyQuantum<Instance>> s2eForInstance(Instance fi,
            double time, String ecFrom, String ecTo,
            double transformationFraction, double conversionRatio) {
        List<EnergyQuantum<Instance>> eqs = new ArrayList<>();
        for (Instance si : sf.getMembers(fi, ecFrom)) {
            SimpleEntry<EnergyQuantum<Instance>, EnergyQuantum<Instance>> pair =
                    EnergyTransformationHelper.createS2E(agent, fi, si,
                            transformationFraction, conversionRatio, time,
                            ecFrom, ecTo,
                            "DaShmEnergyConversion." + ecFrom + " to " + ecTo);
            eqs.add(pair.getValue());
            // eqs.add(pair.getKey());
        }
        return eqs;
    }

    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double time) {
        List<EnergyQuantum<Instance>> eqs = new ArrayList<>();
        // attribute --> generic
        List<EnergyQuantum<Instance>> eqsPart =
                s2eForInstance(fi, time, SHI_ATTRIBUTE, SHI_GENERIC,
                        EnergyTransformationHelper.STANDARD_RATIO,
                        conv_S_SHI_ATTRIBUTE_TO_E_SHI_GENERIC);
        eqs.addAll(eqsPart);
        // relation --> generic
        eqsPart = s2eForInstance(fi, time, SHI_RELATION, SHI_GENERIC,
                EnergyTransformationHelper.STANDARD_RATIO,
                conv_S_SHI_RELATION_TO_E_SHI_GENERIC);
        eqs.addAll(eqsPart);
        // action --> generic
        eqsPart = s2eForInstance(fi, time, SHI_ACTION, SHI_GENERIC,
                EnergyTransformationHelper.STANDARD_RATIO,
                conv_S_SHI_ACTION_TO_E_SHI_GENERIC);
        eqs.addAll(eqsPart);
        // Now apply all the quantums
        for (EnergyQuantum<Instance> eq : eqs) {
            sf.applyInstanceEnergyQuantum(eq);
        }
    }

    @Override
    protected void applyFocusScene(Instance fi, double time) {
        super.applyFocusScene(fi, time);
    }

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmEnergyConversion(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * The idea here is that for VIs which are similar, the positional energy is
     * converted to generic energy, for those which are not, it is less so. The
     * original version was with 1.0
     * 
     * @param fvi
     * @param svi
     * @return
     */
    double viConversionFactor(VerbInstance fvi, VerbInstance svi) {
        if (!ViSimilarityHelper.isCompatible(agent, fvi, svi)) {
            return 0;
        }
        VerbOverlay scraper = VerbOverlay.createVO(agent,
                AmLookup.getVerbsIgnoredInShadowing());
        VerbOverlay fvo = fvi.getVerbs();
        VerbOverlay fvoScraped = Overlay.scrape(fvo, scraper);
        VerbOverlay svo = svi.getVerbs();
        VerbOverlay svoScraped = Overlay.scrape(svo, scraper);
        double conversionFactor =
                Coverage.scoreCoverage(fvoScraped, svoScraped);

        //
        // Print the conversion factor
        //
        boolean printConversionFactor = false;
        if (printConversionFactor) {
            Formatter fmt = new Formatter();
            fmt.add("Conversion factor:");
            fmt.indent();
            fmt.is("FVI", XapiPrint.ppsViXapiForm(fvi, agent));
            fmt.is("SVI", XapiPrint.ppsViXapiForm(svi, agent));
            fmt.is("Conversion factor", conversionFactor);
            TextUi.println(fmt);
        }
        //
        // End of printing the conversion factor
        //
        return conversionFactor;
    }

    /**
     * Performs conversions between the various energies of the shadows.
     * 
     * <ul>
     * <li>converts the salience of the SHADOW_LINK_STRUCTURE into the energy of
     * SHADOW_GENERIC
     * </ul>
     * 
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
        Parameters p = agent.getParameters();
        List<EnergyQuantum<VerbInstance>> eqs = new ArrayList<>();
        List<EnergyQuantum<Instance>> eqsInstance = new ArrayList<>();
        double ratioInstanceToVi = p.get("A_SHM", "G_GENERAL",
                "N_FRACTION_STRENGTHEN_INSTANCE_PART");
        //
        // Convert the salience of the SHV_TEMPORAL_ORDER energy into
        // SHV_GENERIC
        // as a composite to the VI and its instances as well.
        //
        for (VerbInstance svi : sf.getMembers(fvi,
                SHV_TEMPORAL_ORDER)) {
            double salience =
                    sf.getSalience(fvi, svi, SHV_TEMPORAL_ORDER);
            double conversionFactor = viConversionFactor(fvi, svi);
            if (conversionFactor == 0)
                continue;
            double additiveChange = conversionFactor * salience
                    * conv_S_SHV_TEMPORAL_ORDER_TO_E_SHV_GENERIC;
            SimpleEntry<EnergyQuantum<VerbInstance>, List<EnergyQuantum<Instance>>> comp =
                    EnergyQuantum.createCompositeAdd(fvi, svi, additiveChange,
                            timeSlice, ratioInstanceToVi,
                            "DaShmEnergyConversion.SHV_TEMPORAL_ORDER to SHV_GENERIC and SHI_ACTION",
                            SHV_GENERIC, SHI_ACTION);
            eqs.add(comp.getKey());
            eqsInstance.addAll(comp.getValue());
        }
        //
        // Convert the salience of the SHV_ACTION_MATCH energy into
        // SHV_GENERIC
        //
        for (VerbInstance svi : sf.getMembers(fvi,
                SHV_ACTION_MATCH)) {
            double salience =
                    sf.getSalience(fvi, svi, SHV_ACTION_MATCH);
            double additiveChange =
                    salience * conv_S_SHV_ACTION_MATCH_TO_E_SHV_GENERIC;
            EnergyQuantum<VerbInstance> eq = EnergyQuantum.createAdd(fvi, svi,
                    additiveChange, timeSlice, SHV_GENERIC,
                    "DaShmEnergyConversion.SHV_LINK_STRUCTURE to SHV_GENERIC and SHI_ACTION");
            eqs.add(eq);
        }

        //
        // Apply all the collected energy quantums
        //
        for (EnergyQuantum<VerbInstance> eq : eqs) {
            sf.applyViEnergyQuantum(eq);
        }
        for (EnergyQuantum<Instance> eq : eqsInstance) {
            sf.applyInstanceEnergyQuantum(eq);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters
     * .IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaShmEnergyConversion");
        fmt.indent();
        // conv_S_SHI_ATTRIBUTE_TO_E_SHI_GENERIC
        fmt.is("conv_S_SHI_ATTRIBUTE_TO_E_SHI_GENERIC",
                conv_S_SHI_ATTRIBUTE_TO_E_SHI_GENERIC);
        fmt.explanatoryNote(
                "Describes the ratio at which the salience of SHI_ATTRIBUTE is "
                        + "converted to energy of SHI_GENERIC");
        // conv_S_SHI_RELATION_TO_E_SHI_GENERIC
        fmt.is("conv_S_SHI_RELATION_TO_E_SHI_GENERIC",
                conv_S_SHI_RELATION_TO_E_SHI_GENERIC);
        fmt.explanatoryNote(
                "Describes the ratio at which the salience of SHI_RELATION is "
                        + "converted to energy of SHI_GENERIC");
        // conv_S_SHI_ACTION_TO_E_SHI_GENERIC
        fmt.is("conv_S_SHI_ACTION_TO_E_SHI_GENERIC",
                conv_S_SHI_ACTION_TO_E_SHI_GENERIC);
        fmt.explanatoryNote(
                "Describes the ratio at which the salience of SHI_ACTION is "
                        + "converted to energy of SHI_GENERIC");
        // conv_S_SHV_TEMPORAL_ORDER_TO_E_SHV_GENERIC
        fmt.is("conv_S_SHV_TEMPORAL_ORDER_TO_E_SHV_GENERIC",
                conv_S_SHV_TEMPORAL_ORDER_TO_E_SHV_GENERIC);
        fmt.explanatoryNote(
                "Describes the ratio at which the salience of SHV_TEMPORAL_ORDER is "
                        + "converted to energy of SHV_GENERIC");
        // conv_S_SHV_ACTION_MATCH_TO_E_SHV_GENERIC
        fmt.is("conv_S_SHV_ACTION_MATCH_TO_E_SHV_GENERIC",
                conv_S_SHV_ACTION_MATCH_TO_E_SHV_GENERIC);
        fmt.explanatoryNote(
                "Describes the ratio at which the salience of SHV_ACTION_MATCH is "
                        + "converted to energy of SHV_GENERIC");
        fmt.deindent();
    }

}
