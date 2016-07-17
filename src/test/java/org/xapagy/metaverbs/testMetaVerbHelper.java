/*
   This file is part of the Xapagy project
   Created on: Jan 19, 2011
 
   org.xapagy.domain.testMetaVerbHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * Tests the functions in the MetaVerbHelper
 * 
 * @author Ladislau Boloni
 * 
 */
public class testMetaVerbHelper {

    /**
     * This test simply verifies if the removeMetaVerbs removes the successor
     * common metaverb
     */
    @Test
    public void testRemoveMetaVerbs() {
        String description = "Removing meta verbs from a VO";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector', 'Ajax', 'Ulysses', 'Patrocles'");
        r.exec("'Hector' / is-a / w_c_bai20.");
        r.exec("'Achilles' / is-a / w_c_bai21.");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        VerbOverlay original = vi.getVerbs();
        r.ah.voContains(original, Hardwired.VM_SUCCESSOR);
        VerbOverlay removed = MetaVerbHelper.removeMetaVerbs(original, r.agent);
        r.ah.voDoesntContain(removed, Hardwired.VM_SUCCESSOR);
        TestHelper.testDone();
    }

}
