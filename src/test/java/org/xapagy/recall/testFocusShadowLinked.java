/*
   This file is part of the Xapagy project
   Created on: Jun 11, 2011
 
   org.xapagy.recall.testFocusShadowLinked
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.recall;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.activity.hlsmaintenance.FslGenerator;
import org.xapagy.debug.Runner;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * 
 */
public class testFocusShadowLinked {

    /**
     * Tests whether the FSL for a specific creation is correctly created.
     * 
     */
    @Test
    public void testCreateFslForPrediction() {
        String description =
                "Test for creation of FocusShadowLinked for prediction.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$Include 'P-Default'");
        r.exec("An w_c_bai20 'Achilles' / exists.");
        r.exec("A w_c_bai20 'Hector' / exists.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        VerbInstance viPredictedRoot =
                r.exac("'Achilles' / wa_v_av41 / 'Hector'.");
        VerbInstance viPredicted = r.exac("'Hector' / wa_v_av42 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av42 / 'Hector'.");
        r.exec("----");
        r.exec("A scene 'Ithaca' / exists.");
        r.exec("'Ithaca' / is-only-scene.");
        r.exec("An w_c_bai20 'Ulysses' / exists.");
        r.exec("A w_c_bai20 'Nestor' / exists.");
        r.exec("'Ulysses' / wa_v_av40 / 'Nestor'.");
        VerbInstance viHead = r.exac("'Nestor' / wa_v_av41 / 'Ulysses'.");
        // at this moment the viHead must normally have in the shadow the
        // shadows of the others...
        List<FocusShadowLinked> list =
                FslGenerator.generateFslsFromViFocus(r.agent, viHead);
        FocusShadowLinked fslPrediction = null;
        for (FocusShadowLinked fsl : list) {
            if (!fsl.getFslType().equals(FslType.SUCCESSOR)) {
                continue;
            }
            if (!fsl.getViLinked().equals(viPredicted)) {
                continue;
            }
            if (!fsl.getViShadow().equals(viPredictedRoot)) {
                continue;
            }
            fslPrediction = fsl;
        }
        Assert.assertTrue(fslPrediction != null);
        TestHelper.testDone();
    }

}
