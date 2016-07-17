/*
   This file is part of the Xapagy project
   Created on: Nov 21, 2012
 
   org.xapagy.links.testSaMvTransforms
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
 */
public class testSaMvTransforms {
    /**
     * Test the SaMvTransforms metaverb.
     * 
     * The transformed instance must have a zero focus, it must be connected
     * with identity to the old instance, the attributes and relations are not
     * transferred
     * 
     */
    @Test
    public void test() {
        String description = "SaMvTransforms";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Troy CloseOthers With Instances 'Achilles' w_c_bai20, 'Hector' w_c_bai20 w_c_bai21");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Instance instHector = vi.getSubject();
        Instance instAchilles = vi.getObject();
        r.exec("'Achilles' / CreateRelation wv_vr_rel1 / 'Hector'.");
        Assert.assertTrue(RelationHelper.decideRelation(true, r.agent,
                "vr_rel1", instAchilles, instHector));
        r.exec("'Achilles' / wa_v_av42 / 'Hector'.");
        r.exec("'Hector' / transforms / w_c_bai22.");
        vi = r.exac("'Achilles' / wa_v_av41 / the w_c_bai22.");
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
        // the dead Hector is still trojan
        r.ah.coDoesntContain(instHectorDead.getConcepts(), "c_bai21");
        // Achilles still hates the dead Hector as well - SaMvChanger transfers
        // the relations
        Assert.assertFalse(RelationHelper.decideRelation(true, r.agent,
                "vr_rel1", instAchilles, instHectorDead));
        TestHelper.testDone();
    }

}
