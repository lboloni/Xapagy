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
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * Created on: Nov 4, 2012
 */
public class testSaMvCreateSuccessionIdentity {
    /**
     * Test for the creation of succession identity
     */
    @Test
    public void test() {
        String description = "SaMvCreateSuccessionIdentity";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // r.tso.setTrace();
        r.exec("$CreateScene #Iliad CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Instance instHector = vi.getSubject();
        Instance instAchilles = vi.getObject();
        r.exec("$CreateScene #Iliad2 Current RelatedAs successor With Instances w_c_bai20 'Ulysses', w_c_bai20 'Ajax'");
        r.exec("'Ajax' / wa_v_av41 / 'Ulysses'.");
        r.exec("$CreateScene #Iliad3 Current RelatedAs successor With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Achilles' / create-succession-identity / 'Achilles'.");
        r.exec("'Hector' / create-succession-identity / 'Hector'.");
        vi = r.exac("'Achilles' / wa_v_av41 / 'Hector'.");
        Instance instHector2 = vi.getObject();
        Instance instAchilles2 = vi.getSubject();

        Assert.assertTrue(IdentityHelper.isIdentityRelated(instHector2,
                instHector, r.agent));
        Assert.assertTrue(IdentityHelper.isIdentityRelated(instAchilles2,
                instAchilles, r.agent));
        TestHelper.testDone();
    }

}
