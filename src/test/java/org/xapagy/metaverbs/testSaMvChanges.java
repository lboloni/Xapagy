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
package org.xapagy.metaverbs;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.IdentityHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * Created on: Jun 17, 2011
 */
public class testSaMvChanges {

    /**
     * Test the SaMvChanges metaverb.
     * 
     * Changes the changed instance must have a zero focus, it must be connected
     * with identity to the old instance, the attributes and relations are
     * transfered
     * 
     */
    @Test
    public void test() {
        String description = "SaMvChanges";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Troy CloseOthers With Instances 'Achilles' w_c_bai20, 'Hector' w_c_bai20 w_c_bai21");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Instance instHector = vi.getSubject();
        Instance instAchilles = vi.getObject();
        r.exec("'Achilles' / CreateRelation wv_vr_rel1 / 'Hector'.");
        Assert.assertTrue(RelationHelper.decideRelation(true, r.agent,
                "vr_rel1", instAchilles, instHector));
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        // let us say w_c_bai22 is the "dead"
        r.exec("'Hector' / changes / w_c_bai22.");
        vi = r.exac("'Achilles' / wa_v_av42 / 'Hector'.");
        Instance instHectorDead = vi.getObject();
        // alive and dead Hector are not the same
        Assert.assertFalse(instHector.equals(instHectorDead));
        // alive Hector is not part of the focus, nor the current scene
        r.ah.isNotInFocus(instHector);
        // dead Hector is part of the focus and the current scene
        r.ah.isStrongInFocus(instHectorDead);
        r.ah.isInScene(r.agent.getFocus().getCurrentScene(), instHectorDead);
        // they are somatic identity connected
        Assert.assertTrue(IdentityHelper.isSomaticIdentity(instHector,
                instHectorDead, r.agent));
        // the dead Hector is still c_bai21
        r.ah.coContains(instHectorDead.getConcepts(), "c_bai21");
        // Achilles still hates the dead Hector as well - SaMvChanger transfers
        // the relations
        Assert.assertTrue(RelationHelper.decideRelation(true, r.agent,
                "vr_rel1", instAchilles, instHectorDead));
        TestHelper.testDone();
    }

}
