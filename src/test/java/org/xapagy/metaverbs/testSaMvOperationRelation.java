/*
   This file is part of the Xapagy project
   Created on: Jun 16, 2011
 
   org.xapagy.metaverbs.testSaMvCreateRelation
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;

/**
 * A series of tests, on the artificial domain, for the verbs which create or
 * remove relations
 * 
 * @author Ladislau Boloni
 * 
 */
public class testSaMvOperationRelation {

    /**
     * Tests for the fact that the creation of an incompatible relation kicks
     * out the other relation...
     */
    @Test
    public void testCategoryRelation() {
        String description = "creating and removing an independent relation";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$CreateScene #reality CloseOthers With Instances w_c_bai1, w_c_bai2");
        VerbInstance vi =
                r.exac("The w_c_bai1 / CreateRelation wv_vr_rcat1_rmi0 / the w_c_bai2.");
        Instance instBai1 = vi.getSubject();
        Instance instBai2 = vi.getObject();
        r.ah.inRelation("vr_rcat1_rmi0", instBai1, instBai2);
        r.ah.notInRelation("vr_rcat1_rmi1", instBai1, instBai2);
        // create an incompatible relation
        r.exec("The w_c_bai1 / CreateRelation wv_vr_rcat1_rmi1 / the w_c_bai2.");
        r.ah.notInRelation("vr_rcat1_rmi0", instBai1, instBai2);
        r.ah.inRelation("vr_rcat1_rmi1", instBai1, instBai2);
        TestHelper.testDone();
    }

    /**
     * Tests the creation and removal of an independent relation
     */
    @Test
    public void testIndependentRelation() {
        String description = "creating and removing an independent relation";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$CreateScene #reality CloseOthers With Instances w_c_bai1, w_c_bai2");
        VerbInstance vi =
                r.exac("The w_c_bai1 / CreateRelation wv_vr_rel0 / the w_c_bai2.");
        Instance instBai1 = vi.getSubject();
        Instance instBai2 = vi.getObject();
        r.ah.inRelation("vr_rel0", instBai1, instBai2);
        r.ah.notInRelation("vr_rel0", instBai2, instBai1);
        r.exec("The w_c_bai1 / RemoveRelation wv_vr_rel0 / the w_c_bai2.");
        r.ah.notInRelation("vr_rel0", instBai1, instBai2);
        r.ah.notInRelation("vr_rel0", instBai2, instBai1);
        TestHelper.testDone();
    }

}
