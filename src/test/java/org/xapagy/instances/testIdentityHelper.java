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
package org.xapagy.instances;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * Created on: Oct 21, 2011
 */
public class testIdentityHelper {

    /**
     * Creation of the fictional identity, explicit setting up of the scenes
     * 
     */
    @Test
    public void testFictionalIdentityExplicit() {
        String description = "Fictional identity.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #P CloseOthers With Instances 'Achilles' #Achilles");
        Instance instAchilles =
                r.ref.InstanceByLabel("#Achilles");
        r.exec("$CreateScene #T RelatedAs fictional-future With Instances 'Achilles' #AchillesFictional -> 'Achilles'");
        Instance instAchillesFictional =
                r.ref.InstanceByLabel("#AchillesFictional");
        Assert.assertFalse(instAchilles.equals(instAchillesFictional));
        Assert.assertFalse(IdentityHelper.isSomaticIdentity(instAchilles,
                instAchillesFictional, r.agent));
        Assert.assertTrue(IdentityHelper.isFictionalIdentity(instAchilles,
                instAchillesFictional, r.agent));
        TestHelper.testDone();
    }

    /**
     * Creation of the somatic identity, explicit setting up FIXME: This is
     * using some weird referential creation of the identity, clean it up!!!
     */
    @Test
    public void testSomaticIdentityExplicit() {
        String description = "Somatic identity.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Troy CloseOthers With Instances 'Achilles', 'Hector' #Hector");

        Instance instHector =
                r.ref.InstanceByLabel("#Hector");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Hector' / changes / w_c_bai20.");
        Instance instHectorDead =
                r.exac("'Achilles' / wa_v_av41 / 'Hector'.").getObject();
        // alive and dead Hector are not the same
        Assert.assertFalse(instHector.equals(instHectorDead));
        Assert.assertTrue(IdentityHelper.isSomaticIdentity(instHector,
                instHectorDead, r.agent));
        Assert.assertFalse(IdentityHelper.isFictionalIdentity(instHector,
                instHectorDead, r.agent));
        TestHelper.testDone();
    }

    /**
     * Creation of the succession identity, explicit setting up of the scenes
     * 
     */
    @Test
    public void testSuccessionIdentity() {
        String description = "Succession identity.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #P CloseOthers With Instances 'Achilles' #Achilles");

        Instance instAchilles =
                r.ref.InstanceByLabel("#Achilles");
        r.exec("$CreateScene #T Current RelatedAs successor With Instances 'Achilles' #AchillesFictional -> 'Achilles'");
        Instance instAchillesFictional =
                r.ref.InstanceByLabel("#AchillesFictional");
        Assert.assertFalse(instAchilles.equals(instAchillesFictional));
        Assert.assertFalse(IdentityHelper.isSomaticIdentity(instAchilles,
                instAchillesFictional, r.agent));
        Assert.assertTrue(IdentityHelper.isSuccessionIdentity(instAchilles,
                instAchillesFictional, r.agent));
        TestHelper.testDone();
    }

    /**
     * Creation of the view identity, explicit setting up of the scenes
     * 
     */
    @Test
    public void testViewIdentity() {
        String description = "View identity.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #P CloseOthers With Instances 'Achilles' #Achilles");

        Instance instAchilles =
                r.ref.InstanceByLabel("#Achilles");
        r.exec("$CreateScene #T Current RelatedAs view With Instances 'Achilles' #AchillesFictional -> 'Achilles'");
        Instance instAchillesFictional = r.agent.getReferenceAPI()
                .InstanceByLabel("#AchillesFictional");
        Assert.assertFalse(instAchilles.equals(instAchillesFictional));
        Assert.assertFalse(IdentityHelper.isSomaticIdentity(instAchilles,
                instAchillesFictional, r.agent));
        Assert.assertTrue(IdentityHelper.isViewIdentity(instAchilles,
                instAchillesFictional, r.agent));
        TestHelper.testDone();
    }

}
