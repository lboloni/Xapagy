/*
   This file is part of the Xapagy project
   Created on: Jun 11, 2011
 
   org.xapagy.hlss.testViShadowRelativeInterpretation
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.recall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.activity.hlsmaintenance.FslAlternative;
import org.xapagy.activity.hlsmaintenance.FslGenerator;
import org.xapagy.activity.hlsmaintenance.FslInterpreter;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * 
 */
@SuppressWarnings("unused")
public class testFslInterpretation {

    /**
     * Test for the pretty printing of the FslInterpretation
     * 
     */
    @Test
    public void testPrettyPrint() {
        String description =
                "Test for the pretty printing of the FslInterpretation.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$Include 'P-Default'");
        Shadows sf = r.agent.getShadows();
        HeadlessComponents hlc = r.agent.getHeadlessComponents();
        r.exec("An w_c_bai20 'Achilles' / exists.");
        r.exec("A w_c_bai20 'Hector' / exists.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        VerbInstance viPredicted =
                r.exec("'Hector' / wa_v_av44 / 'Achilles'.").get(0);
        r.exec("'Achilles' / wa_v_av42 / 'Hector'.");
        r.exec("----");
        r.exec("A scene 'Ithaca' / exists.");
        r.exec("'Ithaca' / is-only-scene.");
        r.exec("An w_c_bai20 'Ulysses' / exists.");
        r.exec("A w_c_bai20 'Nestor' / exists.");
        r.exec("'Ulysses' / wa_v_av40 / 'Nestor'.");
        VerbInstance viHead =
                r.exec("'Nestor' / wa_v_av41 / 'Ulysses'.").get(0);
        // at this moment the viHead must normally have in the shadow the
        // shadows of the others...
        List<FocusShadowLinked> list =
                FslGenerator.generateFslsFromViFocus(r.agent, viHead);
        FocusShadowLinked fslPrediction = null;
        for (FocusShadowLinked fsl : list) {
            if (fsl.getFslType().equals(FslType.SUCCESSOR)
                    && fsl.getViLinked().equals(viPredicted)) {
                fslPrediction = fsl;
            }
        }
        // TextUi.printLabeledSeparator("Prediction");
        String temp = PrettyPrint.ppDetailed(fslPrediction, r.agent);
        // TextUi.println(temp);
        // ok, now explode it
        Map<VerbInstance, Map<VerbInstance, Double>> cacheViInterpretations =
                new HashMap<VerbInstance, Map<VerbInstance, Double>>();
        Map<Instance, FslAlternative> cacheInstanceInterpretations =
                new HashMap<>();
        // boolean interpretAsSummary = false;
        List<FslInterpretation> fslis =
                FslInterpreter.createInterpretations(fslPrediction, r.agent,
                        cacheViInterpretations, cacheInstanceInterpretations);
        for (FslInterpretation fsli : fslis) {
            temp = PrettyPrint.ppDetailed(fsli, r.agent);
            TextUi.println(temp);
        }
        // Assert.assertEquals("Number of fsli-s", 1, fslis.size());
        Assert.assertTrue(fslis.iterator().next().getViInterpretation()
                .getVerbs().getEnergy(r.agent.getVerbDB().getConcept("v_av44")) > 0);

        // print HLSs
        // TextUi.printLabeledSeparator("HLSs");
        for (Hls hls : hlc.getHlss()) {
            temp = PrettyPrint.ppConcise(hls, r.agent);
            // TextUi.println(temp);
        }
        TestHelper.testDone();
    }
}
