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
 * Created on: Jun 11, 2011
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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
