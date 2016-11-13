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
 * Created on: Jun 16, 2011
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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
