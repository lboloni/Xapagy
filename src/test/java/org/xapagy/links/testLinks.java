/*
   This file is part of the Xapagy project
   Created on: May 19, 2011
 
   org.xapagy.links.testViLinkDB
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
