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
package org.xapagy.instances;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * @author Ladislau Boloni
 * Created on: Oct 2, 2012
 */
public class testViSimilarityHelper {

    
    
    /**
     * Tests the ViSimilarityHelper.decideSimilarityVi function
     */
    @Test
    public void testDecideSimilarityVi() {
        String description = "Matching.decideSimilarityVi function.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("A w_c_bai20 'Achilles' / exists.");
        r.exec("A w_c_bai20 'Hector' / exists.");
        VerbInstance vi1 = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Instance instAchilles = vi1.getObject();
        Instance instHector = vi1.getSubject();
        VerbOverlay voHits = vi1.getVerbs();
        VerbInstance vi2 = r.exac("'Achilles' / wa_v_av41 / 'Hector'.");
        VerbOverlay voStrikes = vi2.getVerbs();
        VerbInstance vi3 = r.exac("'Hector' / is-a / w_c_bai20.");
        // ConceptOverlay coMan = vi3.getAdjective();
        VerbOverlay voIsA = vi3.getVerbs();
        VerbInstance vi4 = r.exac("'Hector' / is-a / w_c_bai21.");
        ConceptOverlay coWarrior = vi4.getAdjective();
        //
        // Checking for S-V-O types
        //
        VerbInstance vit1 = VerbInstance.createViTemplate(r.agent, ViType.S_V_O, voHits);
        VerbInstance vit2 =
                VerbInstance.createViTemplate(r.agent, ViType.S_V_O, voStrikes);
        VerbInstance vit3 = VerbInstance.createViTemplate(r.agent, ViType.S_V_O, voHits);
        // vi1 and vi2 are not compatible, because of the
        // hits vs strikes
        Assert.assertTrue(!ViSimilarityHelper.decideSimilarityVi(vit1, vit2,
                r.agent, true));
        Assert.assertTrue(ViSimilarityHelper.decideSimilarityVi(vit1, vit3,
                r.agent, true));
        vit1.setResolvedPart(ViPart.Subject, instAchilles);
        vit3.setResolvedPart(ViPart.Subject, instAchilles);
        Assert.assertTrue(ViSimilarityHelper.decideSimilarityVi(vit1, vit3,
                r.agent, true));
        vit1.setResolvedPart(ViPart.Object, instAchilles);
        vit3.setResolvedPart(ViPart.Object, instHector);
        Assert.assertTrue(!ViSimilarityHelper.decideSimilarityVi(vit1, vit3,
                r.agent, true));        
        
        //
        // Checking for S-ADJ types
        //
        // These fail, because for the two IsA verbs it returns
        // incompatibility!!!
        VerbInstance vit4 = VerbInstance.createViTemplate(r.agent, ViType.S_ADJ, voIsA);
        vit4.setResolvedPart(ViPart.Adjective, coWarrior);
        VerbInstance vit5 = VerbInstance.createViTemplate(r.agent, ViType.S_ADJ, voIsA);
        vit5.setResolvedPart(ViPart.Adjective, coWarrior);
        Assert.assertTrue(ViSimilarityHelper.decideSimilarityVi(vit4, vit5,
                r.agent, true));
        // Checking for QOUTE
        VerbInstance vi5 =
                r.exac("'Hector' / says / scene // 'Achilles' / is-a / w_c_bai21.");
        VerbInstance vit6 =
                VerbInstance.createViTemplate(r.agent, ViType.QUOTE, vi5.getVerbs());
        vit6.setResolvedPart(ViPart.Subject, instHector);
        vit6.setResolvedPart(ViPart.QuoteScene, vi5.getQuoteScene());
        // checking compatibility with the original, at this point
        Assert.assertFalse(ViSimilarityHelper.decideSimilarityVi(vit6, vi5,
                r.agent, false));
        VerbInstance vit7 = VerbInstance.createViTemplate(r.agent, ViType.S_ADJ, voIsA);
        vit7.setResolvedPart(ViPart.Subject, instAchilles);
        vit7.setResolvedPart(ViPart.Adjective, coWarrior);
        vit6.setResolvedPart(ViPart.Quote, vit7);
        Assert.assertTrue(ViSimilarityHelper.decideSimilarityVi(vit6, vi5,
                r.agent, false));
    }

}
