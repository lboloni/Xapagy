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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * Created on: Jun 9, 2011
 */
@SuppressWarnings("unused")
public class testSuccession {

    /**
     * Helper function, retrieves the succession relation between two VIs in a
     * list
     * 
     * @param from
     * @param to
     * @param listVi
     * @param agent
     * @return
     */
    private static double getSucc(int from, int to, List<VerbInstance> listVi,
            Agent agent) {
        VerbInstance viFrom = listVi.get(from);
        VerbInstance viTo = listVi.get(to);
        double retval =
                agent.getLinks().getLinksByLinkName(viFrom, Hardwired.LINK_SUCCESSOR)
                        .value(viTo);
        // TextUi.println(Formatter.fmt(retval));
        return retval;
    }

    /**
     * Helper function, prints the succession between two VIs in a list
     * 
     * @param from
     * @param to
     * @param listVi
     * @param agent
     */
    private static void printSucc(Formatter fmt, int from, int to,
            List<VerbInstance> listVi, Agent agent) {
        String temp =
                ""
                        + from
                        + "--> "
                        + to
                        + " = "
                        + Formatter.fmt(testSuccession.getSucc(from, to,
                                listVi, agent));
        fmt.add(temp);
    }

    /**
     * Tests if the groups are added to the succesion
     */
    @Test
    public void testGroupSuccession() {
        String description = "Test .";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector', 'Ajax', 'Ulysses', 'Patrocles'");
        VerbInstance viSingle = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        VerbInstance viGroup =
                r.exac("'Hector' + 'Achilles' / wa_v_av40 / 'Ajax'.");
        VerbInstance viNonSucc = r.exac("'Ulysses' / wa_v_av40 / 'Patrocles'.");
        VerbInstance viGroup2 =
                r.exac("'Ulysses' + 'Achilles' / wa_v_av40 / 'Patrocles'.");
        r.ah.isSuccessorOf(viSingle, viGroup);
        TestHelper.testDone();
    }

    /**
     * Tests the establishment of the succession links for a series of linear,
     * dependent VIs but with inserted non-action VIs in the chain of the action
     * 
     */
    @Test
    public void testInsertedDescription() {
        String description =
                "Succession links for action VIs interspersed with isA VIs.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector'");
        List<VerbInstance> listVi = new ArrayList<VerbInstance>();
        // 0,1,2,3,4
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        // 5
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        // 6, 7
        listVi.add(r.exac("'Hector' / is-a / w_c_bai20."));
        listVi.add(r.exac("'Achilles' / is-a / w_c_bai21."));
        // 8, 9, 10, 11, 12
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        listVi.add(r.exac("'Hector' / wa_v_av40 / 'Achilles'."));
        Formatter fmt = new Formatter();
        for (int i = 0; i <= 5; i++) {
            testSuccession.printSucc(fmt, i, 5, listVi, r.agent);
        }
        for (int i = 5; i <= 12; i++) {
            testSuccession.printSucc(fmt, 5, i, listVi, r.agent);
        }
        TextUi.println(fmt);
        Assert.assertTrue(testSuccession.getSucc(0, 5, listVi, r.agent) < 0.01);
        Assert.assertTrue(testSuccession.getSucc(4, 5, listVi, r.agent) > 0.2);
        // no succession to non-action verbs
        Assert.assertTrue(testSuccession.getSucc(5, 6, listVi, r.agent) == 0.0);
        Assert.assertTrue(testSuccession.getSucc(5, 8, listVi, r.agent) > 0.2);
        Assert.assertTrue(testSuccession.getSucc(5, 12, listVi, r.agent) < 0.01);
        TestHelper.testDone();
    }

    /**
     * Tests the creation of a succession for a linear, dependent sequence of
     * action VIs
     */
    @Test
    public void testLinearSuccession() {
        String description =
                "Creation of succession for a linear sequence of action VIs.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector'");

        List<VerbInstance> listVi = new ArrayList<VerbInstance>();
        for (int i = 0; i <= 10; i++) {
            // TextUi.printLabeledSeparator("Iteration " + i);
            VerbInstance vi =
                    r.exec("'Hector' / wa_v_av40 / 'Achilles'.").get(0);
            listVi.add(vi);
            for (int j = 0; j <= i; j++) {
                String temp = PrettyPrint.ppDetailed(listVi.get(j), r.agent);
                // TextUi.println(temp);
            }
        }
        Formatter fmt = new Formatter();
        for (int i = 0; i <= 5; i++) {
            testSuccession.printSucc(fmt, i, 5, listVi, r.agent);
        }
        for (int i = 5; i <= 10; i++) {
            testSuccession.printSucc(fmt, 5, i, listVi, r.agent);
        }
        // TextUi.println(fmt);
        Assert.assertTrue(testSuccession.getSucc(0, 5, listVi, r.agent) < 0.01);
        Assert.assertTrue(testSuccession.getSucc(4, 5, listVi, r.agent) > 0.2);
        Assert.assertTrue(testSuccession.getSucc(5, 6, listVi, r.agent) > 0.2);
        Assert.assertTrue(testSuccession.getSucc(5, 10, listVi, r.agent) <= 0.01);
        TestHelper.testDone();
    }

    /**
     * Tests if succession works accross change -it is not working since the
     * changes of April 2014
     */
    // @Test
    public void testSuccessionChange() {
        String description = "Test succession over change.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector', 'Ajax', 'Ulysses', 'Patrocles'");
        VerbInstance viHectorOld = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        PrettyPrint.ppd(viHectorOld, r.agent);
        VerbInstance viChange = r.exac("'Hector' / changes / w_c_bai20.");
        VerbInstance viHectorNew =
                r.exac("'Hector' / wa_v_av40 / 'Patrocles'.");
        PrettyPrint.ppd(viHectorNew, r.agent);
        r.ah.isSuccessorOf(viHectorOld, viHectorNew);
        // r.ah.isSuccessorOf(viHectorNew, viHectorOld);
        TestHelper.testDone();
    }

}
