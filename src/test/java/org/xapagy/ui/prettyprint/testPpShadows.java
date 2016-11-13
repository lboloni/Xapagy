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
package org.xapagy.ui.prettyprint;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * Created on: Nov 22, 2011
 */
public class testPpShadows {

    @Test
    public void testShadowInstances() {
        String description = "Printing of the shadow instances";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace();
        // the original observations
        r.printOn = true;
        r.exec("$CreateScene #N0 CloseOthers With Instances 'Billy', 'Johnny'");

        r.exec("'Billy' / wa_v_av40 / 'Johnny'.");
        r.exec("'Johnny' / wa_v_av40 / 'Billy'.");
        // r.tso.ppd(PrintWhat.FOCUS_STRUCTURED);
        r.exec("----");
        // the suggestion
        r.exec("$CreateScene #Conversation CloseOthers With Instances w_c_bai21 'Billy', w_c_bai21 'Johnny'");
        r.exec("$CreateScene #N1 Current With Instances 'Narrator'");

        r.exec("'Narrator' / says in #Conversation // 'Billy' / wa_v_av40 / 'Johnny'.");
        VerbInstance vi =
                r.exac("'Narrator' / says in #Conversation // 'Johnny' / wa_v_av40 / 'Billy'.");
        r.exec("'Narrator' / says in #Conversation // 'Johnny' / wa_v_av41 / 'Billy'.");
        PrettyPrint.ppd(vi, r.agent);
        PrettyPrint.ppd(vi.getQuote(), r.agent);
        // r.tso.ppc(PrintWhat.SHADOW_INSTANCES);
        r.exec("----");
        // the recall
        r.exec("$CreateScene #R1 CloseOthers With Instances w_c_bai21 'Billy', w_c_bai21 'Johnny'");
        r.exec("'Billy' / wa_v_av40 / 'Johnny'.");
        r.tso.ppc("SHADOW_INSTANCES");
        TestHelper.testIncomplete();
    }

}
