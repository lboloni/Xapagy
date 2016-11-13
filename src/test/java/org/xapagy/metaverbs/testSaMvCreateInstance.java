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
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * Created on: Jun 15, 2011
 */
public class testSaMvCreateInstance {

    /**
     * 
     * [test] Test for the creation through "a" in the first quote
     * 
     * [test] Test for the creation through "a" in the second quote
     * 
     */
    @Test
    public void testCreateInstanceInQuote() {
        String description = "Create instance in quote";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        r.exec("A scene #Scene1 / exists.");
        r.exec("$ChangeScene #Scene1");
        r.exec("A w_c_bai20 'Jenny' / exists.");
        r.exec("A w_c_bai21 'Fluffy' / exists.");
        Instance scene2 = r.exac("A scene #Scene2 / exists.").getSubject();
        r.exec("$ChangeScene #Scene1");
        Instance instBoy =
                r.eh.getAction(
                        r.exec("'Jenny' / says in #Scene2 // A w_c_bai22 / exists."))
                        .getQuote().getSubject();
        r.ah.isInScene(scene2, instBoy);
        TestHelper.testDone();
    }

    /**
     * Verifies that the labels added to the creation of the instance will end
     * up inside the instance
     */
    @Test
    public void testCreateInstanceLabel() {
        String description = "Create instance in main scene";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Reality CloseOthers With Instances 'Achilles' #A w_c_bai20");
        r.exec("A 'Hector' #H / exists.");
        VerbInstance vi = r.exac("'Achilles' / wa_v_av40 / 'Hector'.");
        Formatter fmt = new Formatter();
        fmt.add(PrettyPrint.ppDetailed(vi.getSubject(), r.agent));
        fmt.add(PrettyPrint.ppDetailed(vi.getObject(), r.agent));
        fmt.add(PrettyPrint.ppDetailed(vi.getSubject().getScene(), r.agent));
        TextUi.println(fmt);
        TestHelper.testDone();
    }

    /**
     * 
     * Tests for the create instance in the main scene (as an object, as a
     * subject).
     * 
     */
    @Test
    public void testCreateInstanceMainScene() {
        String description = "Create instance in main scene";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // create as a subject
        Instance instHector = r.exac("A 'Hector' / exists.").getSubject();
        // create as an object
        Instance instAchilles =
                r.exac("'Hector' / wa_v_av40 / an 'Achilles'.").getObject();
        VerbInstance vi = r.exac("'Achilles' / wa_v_av40 / 'Hector'.");
        Assert.assertTrue(vi.getSubject().equals(instAchilles));
        Assert.assertTrue(vi.getObject().equals(instHector));
        TestHelper.testDone();
    }

}
