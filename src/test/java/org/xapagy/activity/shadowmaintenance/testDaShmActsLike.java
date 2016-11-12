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
package org.xapagy.activity.shadowmaintenance;

import org.junit.Assert;
import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;

/**
 * Tests whether the DaActsLike diffusion activity has any effect, ie. it
 * increases the shadow of the given thing
 * 
 * @author Ladislau Boloni
 * Created on: Nov 11, 2011
 */
public class testDaShmActsLike {

    /**
     * Tests impact of the DaActsLike - it favors a previous shadow
     */
    @SuppressWarnings("unused")
    //@Test
    public void test() {
        String description =
                "DaMvActsLike - changing the participation in the shadow";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$Include 'P-FocusAndShadows'");
        r.exec("$DiffusionActivity Append To Composite 'ShadowMaintenance' New DA With Name 'InstanceActsLike' Code 'org.xapagy.activity.shadowmaintenance.DaShmInstanceActsLike'");
        // r.tso.setTrace();
        //
        // a history with troy
        //
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Instance instHector = vi.getSubject();
        Instance instAchilles = vi.getObject();
        r.exec("'Achilles' / wa_v_av41/ 'Hector'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("----");
        //
        // another history with other Troy
        //
        r.exec("$CreateScene #Playground CloseOthers With Instances w_c_bai22 w_c_bai20 'Billy'");
        // r.debugMode();
        Instance instJenny =
                r.exac("A w_c_bai23 w_c_bai20 'Jenny' / exists.").getSubject();
        Shadows sf = r.agent.getShadows();
        double shValueHector =
                sf.getSalience(instJenny, instHector, EnergyColors.SHI_GENERIC);
        double shValueAchilles =
                sf.getSalience(instJenny, instAchilles, EnergyColors.SHI_GENERIC);
        Assert.assertTrue(Math.abs(shValueHector - shValueAchilles) < 0.01);
        VerbInstance viActsLike = r.exac("'Jenny' / acts-like / 'Hector'.");
        // the Da had not acted here yet
        VerbInstance viAction = r.exac("'Jenny' / wa_v_av40 / 'Billy'.");
        // the Da had acted here
        double sh2ValueHector =
                sf.getSalience(instJenny, instHector, EnergyColors.SHI_GENERIC);
        double sh2ValueAchilles =
                sf.getSalience(instJenny, instAchilles, EnergyColors.SHI_GENERIC);
        Assert.assertTrue(sh2ValueHector > 1.5 * sh2ValueAchilles);
        TestHelper.testDone();
    }

}
