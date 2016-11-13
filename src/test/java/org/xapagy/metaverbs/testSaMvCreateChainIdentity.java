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
 * Tests whether the is-the-same-as function creates an identity
 * 
 * @author Ladislau Boloni
 * Created on: Nov 11, 2011
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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
