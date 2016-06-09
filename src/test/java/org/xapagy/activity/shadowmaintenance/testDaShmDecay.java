/*
   This file is part of the Xapagy project
   Created on: Jan 30, 2012
 
   org.xapagy.activity.shadowmaintenance.testDaShmDecay
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;


import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsGenerator;
import org.xapagy.debug.storygenerator.RsTemplates;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.parameters.Parameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class testDaShmDecay {

    /**
     * Measures the instance decay. Uses only the instanceMatchingHead to add a
     * specific instance to the shadow
     * 
     * @param instanceRemainingEnergyRatio
     *            - the parameter to be set
     * @return
     */
    public SimpleEntry<Double, Double> measureInstanceDecay(
            double instanceRemainingEnergyRatio) {
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$Include 'P-FocusAndShadows'");
        Parameters p = r.agent.getParameters();
        Shadows sf = r.agent.getShadows();
        r.printOn = false; // was true
        RecordedStory storyInit =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av41", "wa_v_av42", "wa_v_av43"));
        storyInit.runAll(r);
        Instance si =
                storyInit.getRsScene(RsTemplates.DIRECT).getInstances().get(0);
        // second story
        RecordedStory storySecond =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av41", "wa_v_av42", "wa_v_av43"));
        p.set("A_SHM", "G_INSTANCE_MATCHING_HEAD",
                "N_ENABLE", true);
        p.set("A_SHM", "G_DECAY",
                "N_ENABLE", true);
        p.set("A_SHM", "G_DECAY",
                "N_INSTANCE_SHADOW_ENERGY_DECAY_MULTIPLIER" + EnergyColors.SHI_GENERIC,
                instanceRemainingEnergyRatio);
        storySecond.runAll(r);
        Instance fi =
                storySecond.getRsScene(RsTemplates.DIRECT).getInstances()
                        .get(0);
        // value of shadow after the story
        double afterStory = sf.getSalience(fi, si, EnergyColors.SHI_GENERIC);
        p.set("A_SHM", "G_INSTANCE_MATCHING_HEAD",
                "N_ENABLE", false);
        for (int i = 0; i != 10; i++) {
            // r.tso.ppd(PrintWhat.SHADOW_INSTANCES);
            r.exec("-");
        }
        // value of shadow after 10 seconds
        double after10 = sf.getSalience(fi, si, EnergyColors.SHI_GENERIC);
        return new SimpleEntry<>(afterStory, after10);
    }

    /**
     * Measures the instance decay. Uses only the instanceMatchingHead to add a
     * specific instance to the shadow
     * 
     * @param instanceRemainingEnergyRatio
     *            - the parameter to be set
     * @return
     */
    public SimpleEntry<Double, Double> measureViDecay(
            double viRemainingEnergyRatio) {
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$Include 'P-FocusAndShadows'");
        Parameters p = r.agent.getParameters();
        Shadows sf = r.agent.getShadows();
        r.printOn = false; // was true
        RecordedStory storyInit =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av41", "wa_v_av42", "wa_v_av43"));
        storyInit.runAll(r);
        VerbInstance svi = storyInit.getRecordedVis().get(1);
        // second story
        RecordedStory storySecond =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av41", "wa_v_av42", "wa_v_av43"));
        p.set("A_SHM", "G_INSTANCE_MATCHING_HEAD",
                "N_ENABLE", true);
        p.set("A_SHM", "G_VI_MATCHING_HEAD",
                "N_ENABLE", true);
        p.set("A_SHM", "G_DECAY",
                "N_ENABLE", true);
        p.set("A_SHM", "G_DECAY",
                "N_VI_SHADOW_ENERGY_DECAY_MULTIPLIER" + EnergyColors.SHV_GENERIC,
                viRemainingEnergyRatio);
        storySecond.runAll(r);
        VerbInstance fvi = storySecond.getRecordedVis().get(1);
        // value of shadow after the story
        double afterStory =
                sf.getSalience(fvi, svi, EnergyColors.SHV_GENERIC);
        p.set("A_SHM", "G_INSTANCE_MATCHING_HEAD",
                "N_ENABLE", false);
        p.set("A_SHM", "G_VI_MATCHING_HEAD",
                "N_ENABLE", false);
        for (int i = 0; i != 10; i++) {
            // r.tso.ppd(PrintWhat.SHADOW_VIS);
            r.exec("-");
        }
        // value of shadow after 10 seconds
        double after10 = sf.getSalience(fvi, svi, EnergyColors.SHV_GENERIC);
        return new SimpleEntry<>(afterStory, after10);
    }

    /**
     * Tests the decay of the instances for various values of the
     * 
     */
    // @Test
    public void testDecayInstances() {
        String description = "daShmDecay - decay along with the instances";
        TestHelper.testStart(description);
        // various values of the irer
        TextUi.println("instance remaining energy ratio / after story / 10 seconds later");
        for (double irer = 0.0; irer <= 1.0; irer = irer + 0.1) {
            SimpleEntry<Double, Double> entry = measureInstanceDecay(irer);
            TextUi.println(Formatter.fmt(irer) + "   "
                    + Formatter.fmt(entry.getKey()) + "   "
                    + Formatter.fmt(entry.getValue()));
        }

        Parameters p = new Parameters();
        double irer =
                p.get("A_SHM",
                        "G_DECAY",
                        "N_INSTANCE_SHADOW_ENERGY_DECAY_MULTIPLIER"
                                + EnergyColors.SHI_GENERIC);
        SimpleEntry<Double, Double> entry = measureInstanceDecay(irer);
        TextUi.println("Default value setting");
        TextUi.println(Formatter.fmt(irer) + "   "
                + Formatter.fmt(entry.getKey()) + "   "
                + Formatter.fmt(entry.getValue()));
        // now the default value
        TestHelper.testDone();
    }

    /**
     * Tests the decay of the instances for various values of the
     * 
     */
    // @Test
    public void testDecayVis() {
        String description = "daShmDecay - decay of the vis";
        TestHelper.testStart(description);
        // various values of the irer
        TextUi.println("vi remaining energy ratio / after story / 10 seconds later");
        for (double vrer = 0.0; vrer <= 1.0; vrer = vrer + 0.1) {
            SimpleEntry<Double, Double> entry = measureViDecay(vrer);
            TextUi.println(Formatter.fmt(vrer) + "   "
                    + Formatter.fmt(entry.getKey()) + "   "
                    + Formatter.fmt(entry.getValue()));
        }
        Parameters p = new Parameters();
        // InitParameters.init(p);
        double vrer =
                p.get("A_SHM", "G_DECAY",
                        "N_VI_SHADOW_ENERGY_DECAY_MULTIPLIER"
                                + EnergyColors.SHV_GENERIC);
        SimpleEntry<Double, Double> entry = measureViDecay(vrer);
        TextUi.println("Default value setting");
        TextUi.println(Formatter.fmt(vrer) + "   "
                + Formatter.fmt(entry.getKey()) + "   "
                + Formatter.fmt(entry.getValue()));
        // now the default value
        TestHelper.testDone();
    }

}
