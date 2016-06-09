/*
   This file is part of the Xapagy project
   Created on: Nov 11, 2011
 
   org.xapagy.metaverbs.testSaMvIsTheSameAs
 
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
import org.xapagy.instances.VerbInstance;

/**
 * Tests whether the is-the-same-as function creates an identity
 * 
 * @author Ladislau Boloni
 * 
 */
public class testSaMvCreateChainIdentity {

    /**
     * Test for the create chain identity - the identity is connected to the
     * shadows, so we need to turn on the shadow calculation
     * 
     * FIXME: why is this so slow??? Probably the VI shadows...
     * 
     */
    @Test
    public void test() {
        String description = "SaMvCreateChainIdentity";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$Include 'P-FocusAndShadows'");
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Instance instHector = vi.getSubject();
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        r.exec("'Hector' / wa_v_av42 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av43 / 'Hector'.");
        r.exec("'Hector' / wa_v_av44 / 'Achilles'.");
        r.exec("$CreateScene #Playground CloseOthers With Instances w_c_bai21 'Billy', w_c_bai20 'Jenny'");
        VerbInstance viCreateChainIdentity =
                r.exac("'Jenny' / create-chain-identity / 'Hector'.");
        Instance instJenny = viCreateChainIdentity.getSubject();
        Assert.assertTrue(IdentityHelper.isIdentityRelated(instJenny,
                instHector, r.agent));
        TestHelper.testDone();
    }

}
