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
package org.xapagy.ui.smartprint;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Oct 16, 2011
 */
public class testSpFocus {

    @Test
    public void testSpFocusSimple() {
        String description = "SpFocus-simple";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        // r.tso.trace();
        // r.tso.setCompactTraceIncludingVerbalization(true);
        r.exec("$CreateScene #Scene CloseOthers With Instances w_c_bai20 'Jenny', w_c_bai21 'Billy', w_c_bai22");
        r.exec("The w_c_bai21 / CreateRelation wv_vr_rel1 / the w_c_bai20.");
        r.exec("The w_c_bai21 / wa_v_av40 / the w_c_bai20.");
        r.exec("The w_c_bai20 / wa_v_av41 / the w_c_bai21.");
        r.exec("The w_c_bai20 + the w_c_bai21 / exists.");
        TextUi.println(SpFocus.spFocus(r.agent.getFocus(), r.agent));
        r.exec("$CreateScene #View RelatedAs view With Instances w_c_bai20 -> w_c_bai20, w_c_bai21 -> w_c_bai21");
        // r.exec("The w_c_bai20 -- in -- #View / exists.");
        // r.exec("The w_c_bai21 -- in -- #View / exists.");
        r.exec("The w_c_bai20 -- in -- #View / wa_v_av40 / the w_c_bai21 -- in -- #View.");
        TextUi.println(SpFocus.spFocus(r.agent.getFocus(), r.agent));
        TestHelper.testIncomplete();
    }

    @Test
    public void testSpFocusSummary() {
        String description = "SpFocus - printing a summary";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        // r.tso.trace();
        // r.tso.setCompactTraceIncludingVerbalization(true);
        r.exec("$CreateScene #Scene CloseOthers With Instances w_c_bai20 'Jenny', w_c_bai21 'Billy', w_c_bai22");
        r.exec("Scene / clone-scene / scene #Summarization.");
        r.exec("Scene / has-view / scene #Summarization.");
        r.exec("The w_c_bai21 / CreateRelation wv_vr_rel1 / the w_c_bai20.");
        r.exec("The w_c_bai21 / wa_v_av40 / the w_c_bai20.");
        r.exec("The w_c_bai20 / wa_v_av41 / the w_c_bai21.");
        r.exec("The w_c_bai20 + the w_c_bai21 / wa_v_av42.");
        TextUi.println(SpFocus.spFocus(r.agent.getFocus(), r.agent));
        //TextUi.println(SpFocus.spFocus(r.agent.getFocus(), r.agent));
        TestHelper.testIncomplete();
    }

}
