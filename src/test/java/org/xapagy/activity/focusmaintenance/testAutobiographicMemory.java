/*
   This file is part of the Xapagy project
   Created on: Jun 9, 2011
 
   org.xapagy.agents.testEpisodicMemory
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import java.util.AbstractMap.SimpleEntry;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.latex.LatexFormatter;
import org.xapagy.ui.prettyprint.FormatTable;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * Testing the autobiographical memory
 * 
 * 
 * @author Ladislau Boloni
 * 
 */
@SuppressWarnings("unused")
public class testAutobiographicMemory {

    public boolean doTest = true;

    /**
     * for the incompatibility between concepts
     */
    public SimpleEntry<String, String> autobiographicalMemoryExperiments() {
        Runner r = ArtificialDomain.createAabConcepts();
        Agent agent = r.agent;
        LatexFormatter lf = new LatexFormatter();
        FormatTable ft = new FormatTable(40, 20, 40);
        ft.header("instance/VI", "salience", "comment");
        lf.beginTabular("|p{5cm}|p{1.5cm}|p{5cm}");
        lf.add("\\hline");
        lf.addTableLine("instance/VI", "salience", "comment");
        lf.add("\\hline");
        //
        // Action and non-action verbs
        //
        r.exec("$CreateScene #Reality CloseOthers With Instances 'Hector', 'Achilles', w_c_bai20, w_c_bai21");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("The  w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        //
        // is-a and relation verbs, at the beginning
        //
        VerbInstance viIsA1 = r.exac("'Achilles' / is-a / w_c_bai22.");
        r.exec("'Achilles' / CreateRelation wv_vr_rel40 / 'Hector'.");
        VerbInstance viRel1 = r.agent.getLastVerbInstance();
        // TextUi.println(PrettyPrint.ppDetailed(viRel1, agent));

        VerbInstance vi1 = r.exac("'Achilles' / wa_v_av40 / 'Hector'.");
        for (int i = 0; i != 5; i++) {
            r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        }
        VerbInstance vi2 = r.exac("'Achilles' / wa_v_av40 / 'Hector'.");
        VerbInstance vi5 = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        // now do the tests
        tst(ft, lf, agent, vi1, -1.0, "Action VI - default");

        //
        // Test the salience of a fast-followed action VI.
        //
        String description = "Test the salience of a fast-followed action VI.";
        r.exec("$CreateScene #R2 CloseOthers With Instances 'Hector', 'Achilles', w_c_bai20, w_c_bai21");
        vi1 = r.exac("'Hector' / wa_v_av40 / 'Achilles';");
        vi2 = r.exac("'Achilles' / wa_v_av40 / 'Hector';");
        VerbInstance vi3 = r.exac("'Hector' / wa_v_av40 / 'Achilles';");
        VerbInstance vi4 = r.exac("'Achilles' / wa_v_av40 / 'Hector';");
        for (int i = 0; i != 10; i++) {
            r.exac("'Hector' / wa_v_av40 / 'Achilles';");
            r.exac("'Achilles' / wa_v_av40 / 'Hector';");
        }

        tst(ft, lf, agent, vi1, -1.0, "Fast followed Action VI - 1");
        tst(ft, lf, agent, vi2, -1.0, "Fast followed Action VI - 2");
        tst(ft, lf, agent, vi3, -1.0, "Fast followed Action VI - 3");
        tst(ft, lf, agent, vi4, -1.0, "Fast followed Action VI - 4");

        //
        // End of tests, closing the tables
        //
        ft.endTable();
        lf.add("\\hline");
        lf.endTabular();

        return new SimpleEntry<>(ft.toString(), lf.toString());
    }

    /**
     * Runs
     */
    @Test
    public void test() {
        SimpleEntry<String, String> val = autobiographicalMemoryExperiments();
        TextUi.println(val);
    }

    /**
     * Calculated the memory values
     * 
     * @param ft
     *            the formatted table
     * @param expected
     * @param concepts
     */
    public void tst(FormatTable ft, LatexFormatter lf, Agent agent,
            XapagyComponent component, double expectedScore, String comment) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        double salience = 0;
        String label = null;
        if (component instanceof Instance) {
            Instance instance = (Instance) component;
            salience = am.getSalience(instance, EnergyColors.AM_INSTANCE);
            label = PrettyPrint.ppConcise(instance, agent);
        } else {
            VerbInstance vi = (VerbInstance) component;
            salience = am.getSalience(vi, EnergyColors.AM_VI);
            label = PrettyPrint.ppConcise(vi, agent);
        }
        ft.wrappedRow(label, salience, comment);
        lf.addTableLine(label, salience, comment);
        if (expectedScore >= 0) {
            Assert.assertEquals("Salience not as expected", expectedScore,
                    salience, expectedScore * 0.03);
        }
    }

}
