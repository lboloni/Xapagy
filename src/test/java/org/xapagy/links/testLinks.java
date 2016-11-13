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
package org.xapagy.links;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * Created on: May 19, 2011
 */
public class testLinks {


    /**
     * Testing the ViLinkDB for not-one-way and one-way relations
     * 
     */
    @Test
    public void test() {
        String description = "Test undirected links";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector', 'Ajax', 'Ulysses', 'Patrocles'");
        VerbInstance vi1 = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        VerbInstance vi2 = r.exac("'Achilles' / wa_v_av40 / 'Hector'.");
        Links vir = r.agent.getLinks();
        //
        // behavior of the coincidence link
        //
        double val = 0;
        // add in one direction (COINCIDENCE)
        vir.changeLink(Hardwired.LINK_COINCIDENCE, vi1, vi2, 0.4);
        val = vir.getLinksByLinkName(vi1, Hardwired.LINK_COINCIDENCE).value(vi2);
        Assert.assertEquals(0.4, val, 0.0);
        val = vir.getLinksByLinkName(vi2, Hardwired.LINK_COINCIDENCE).value(vi1);
        Assert.assertEquals(0.0, val, 0.0);
        // add in the other direction as well
        vir.changeLink(Hardwired.LINK_COINCIDENCE, vi2, vi1, 0.6);
        val = vir.getLinksByLinkName(vi1, Hardwired.LINK_COINCIDENCE).value(vi2);
        Assert.assertEquals(0.4, val, 0.0);
        val = vir.getLinksByLinkName(vi2, Hardwired.LINK_COINCIDENCE).value(vi1);
        Assert.assertEquals(0.6, val, 0.0);
        //
        // behavior of a one-way relation (SUCCESSOR)
        //
        vir.changeLink(Hardwired.LINK_QUESTION, vi1, vi2, 0.4);
        val = vir.getLinksByLinkName(vi1, Hardwired.LINK_QUESTION).value(vi2);
        Assert.assertEquals(0.4, val, 0.0);

        val = vir.getLinksByLinkName(vi2, Hardwired.LINK_QUESTION).value(vi2);
        Assert.assertEquals(0.0, val, 0.0);
        // add in the other direction as well
        vir.changeLink(Hardwired.LINK_QUESTION, vi2, vi1, 0.6);
        val = vir.getLinksByLinkName(vi1, Hardwired.LINK_QUESTION).value(vi2);
        Assert.assertEquals(0.0, val, 0.0);
        val = vir.getLinksByLinkName(vi2, Hardwired.LINK_QUESTION).value(vi1);
        Assert.assertEquals(0.2, val, 0.1);
        TestHelper.testDone();
    }
}
