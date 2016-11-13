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

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrintDetail;

/**
 * @author Ladislau Boloni
 * Created on: Jun 10, 2011
 */
public class testHls {
    /**
     * Test for the presence of the context relations
     */
    // @Test
    public void testHlsContextRelations() {
        String description = "HLS creation.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        HeadlessComponents hlc = r.agent.getHeadlessComponents();
        r.printOn = false;
        r.exec("$CreateScene #Troy CloseOthers");

        r.exec("An w_c_bai20 'Achilles' / exists.");
        r.exec("A w_c_bai20 'Hector' / exists.");
        r.exec("'Hector' / CreateRelation wv_vr_rel1 / 'Achilles'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av42 / 'Hector'.");
        r.exec("'Hector' / wa_v_av41 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av43 / 'Hector'.");
        r.exec("----");
        r.exec("$Include 'P-All'");
        r.exec("$CreateScene #Troy CloseOthers");
        r.exec("An w_c_bai20 'Ulysses' / exists.");
        r.exec("A w_c_bai20 'Nestor' / exists.");
        r.exec("'Ulysses' / wa_v_av40 / 'Nestor'.");
        r.exec("'Nestor' / wa_v_av42 / 'Ulysses'.");
        r.exec("'Ulysses' / wa_v_av41 / 'Nestor'.");
        // at this moment, one of the HLS-s with a positive continuation must be
        // an SaMvCreateRelation vr_rel1
        boolean found = false;
        for (Hls hls : hlc.getHlss()) {
            // PrettyPrint.ppd(hls.getViTemplate().getVerbs(), r.agent);
            boolean ok =
                    r.aht.verbInstanceTemplateIs(hls.getViTemplate(),
                            ViType.S_V_O, "vr_rel1");
            if (ok) {
                found = true;
            }
        }
        Assert.assertTrue(found);
        r.print(r.tso.printNow("HEADLESS_COMPONENTS",
                PrintDetail.DTL_CONCISE));
        r.exec("'Nestor' / wa_v_av43 / 'Ulysses'.");
        // TextUi.println("Continuation HLS size = " +
        // r.agent.getHeadlessShadows().getHlss().size());
        // assertTrue(r.agent.getHeadlessShadows().getHlss().size() == 6);
        TestHelper.testDone();
    }

    /**
     * Test for the presence of the context relations
     */
    // @Test
    public void testHlsSummaryRelations() {
        String description = "HLS creation.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        HeadlessComponents hlc = r.agent.getHeadlessComponents();
        r.printOn = true;
        r.exec("$CreateScene #main CloseOthers AddSummary With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");

        
        //r.exec("An w_c_bai20 'Achilles' / exists.");
        //r.exec("A w_c_bai20 'Hector' / exists.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av42 / 'Hector'.");
        r.exec("'Hector' / wa_v_av41 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av43 / 'Hector'.");
        r.exec("'Achilles' / create-summary wa_v_av44 / 'Hector'.");
        r.exec("----");
        r.exec("$Include 'P-All'");
        r.exec("$CreateScene #Troy CloseOthers");
        r.exec("An w_c_bai20 'Ulysses' / exists.");
        r.exec("A w_c_bai20 'Nestor' / exists.");
        r.exec("'Ulysses' / wa_v_av40 / 'Nestor'.");
        r.exec("'Nestor' / wa_v_av42 / 'Ulysses'.");
        r.exec("'Ulysses' / wa_v_av41 / 'Nestor'.");
        // at this moment, one of the HLS-s with a positive continuation must be
        // an SaMvCreateRelation CreateRelation vr_hates
        boolean found = false;
        for (Hls hls : hlc.getHlss()) {
            boolean ok =
                    r.aht.verbInstanceTemplateIs(hls.getViTemplate(),
                            ViType.S_V_O, "v_av44");
            if (ok) {
                found = true;
            }
        }
        Assert.assertTrue(found);
        r.print(r.tso.printNow("HEADLESS_COMPONENTS",
                PrintDetail.DTL_CONCISE));
        r.exec("'Nestor' / wa_v_av43 / 'Ulysses'.");
        // TextUi.println("Continuation HLS size = " +
        // r.agent.getHeadlessShadows().getHlss().size());
        // assertTrue(r.agent.getHeadlessShadows().getHlss().size() == 5);
        TestHelper.testDone();
    }

    /**
     * Generic shadow structure
     * 
     */
    @Test
    public void testTheHlsSimple() {
        String description = "HLS creation.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = false;
        r.exec("An w_c_bai20 'Achilles' / exists.");
        r.exec("A w_c_bai20 'Hector' / exists.");
        r.exec("'Hector' / CreateRelation wv_vr_rel1 / 'Achilles'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av42 / 'Hector'.");
        r.exec("'Hector' / wa_v_av41 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av43 / 'Hector'.");
        r.exec("----");
        r.exec("$CreateScene #Ithaca CloseOthers");

        r.exec("An w_c_bai20 'Ulysses' / exists.");
        r.exec("A w_c_bai20 'Nestor' / exists.");
        r.exec("'Ulysses' / wa_v_av40 / 'Nestor'.");
        r.exec("'Nestor' / wa_v_av42 / 'Ulysses'.");
        r.exec("'Ulysses' / wa_v_av41 / 'Nestor'.");
        r.print(r.tso.printNow("HEADLESS_COMPONENTS",
                PrintDetail.DTL_CONCISE));
        r.exec("'Nestor' / wa_v_av43 / 'Ulysses'.");
        TextUi.println("Continuation HLS size = "
                + r.agent.getHeadlessComponents().getHlss().size());
        // assertTrue(r.agent.getHeadlessShadows().getHlss().size() == 6);
        TestHelper.testDone();
    }
}
