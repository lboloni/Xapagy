/*
   This file is part of the Xapagy project
   Created on: Mar 17, 2013
 
   org.xapagy.activity.shadowmaintenance.full.testShm_Identical
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance.full;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.activity.shadowmaintenance.ShadowTableHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsGenerator;
import org.xapagy.debug.storygenerator.RsTemplates;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.LatexFormatter;
import org.xapagy.ui.prettyprint.FormatTable;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * 
 * Tests what kind of shadowing levels will be achieved when we are matching
 * identical stories (with full DaShm).
 * 
 * @author Ladislau Boloni
 * 
 */
public class testShm_Identical {

    public SimpleEntry<String, String> shadowExperiments() {
        SimpleEntry<FormatTable, LatexFormatter> tables =
                ShadowTableHelper.createShadowTables();
        LatexFormatter lf = tables.getValue();
        FormatTable ft = tables.getKey();
        //
        // Instances are identical. Verbs are all different.
        // Different lengths of stories.
        //
        for (int i = 0; i != 9; i++) {
            String instance1 = "w_c_bai21 #instance1";
            String instance2 = "w_c_bai21 #instance2";
            List<String> verblist = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                String verb = "wa_v_av" + (40 + j);
                verblist.add(verb);
            }
            testReciprocal(ft, lf, instance1, instance2, verblist, null, null);
        }
        //
        // Instances are identical. There are specific relations to and from.
        // Verbs are all different.
        // Different lengths of stories.
        //
        for (int i = 0; i != 9; i++) {
            String instance1 = "w_c_bai21 #instance1";
            String instance2 = "w_c_bai21 #instance2";
            List<String> verblist = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                String verb = "wa_v_av" + (40 + j);
                verblist.add(verb);
            }
            List<String> rel12 = Arrays.asList("wv_vr_rel0");
            List<String> rel21 = Arrays.asList("wv_vr_rel1", "wv_vr_rel2");
            testReciprocal(ft, lf, instance1, instance2, verblist, rel12,
                    rel21);
        }
        // end of the table
        lf.add("\\hline");
        ft.endTable();
        lf.endTabular();
        return new SimpleEntry<>(ft.toString(), lf.toString());

    }

    /**
     * Runs
     */
    @Test
    public void test() {
        SimpleEntry<String, String> retval = shadowExperiments();
        TextUi.println(retval.getKey());
    }

    /**
     * Creates two reciprocal stories with two instances
     * 
     * @param ft
     * @param lf
     * @param instance1
     * @param instance2
     * @param verbs
     */
    public Runner testReciprocal(FormatTable ft, LatexFormatter lf,
            String instance1, String instance2, List<String> verbs,
            List<String> rels12, List<String> rels21) {

        //
        // STORY: identical, ACTORS: base level matching
        //
        List<RecordedStory> history = new ArrayList<>();

        RecordedStory st = RsGenerator.generateReciprocalWithRelations(
                instance1, instance2, verbs, rels12, rels21);
        RecordedStory st2 = RsGenerator.generateReciprocalWithRelations(
                instance1, instance2, verbs, rels12, rels21);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        RsTestingUnit stu = new RsTestingUnit(
                new ABStory("$Include 'P-FocusAndShadows'"), history, st,
                new ABStory("$Include 'P-FocusAndShadows'"), st2);
        stu.runAll(r);
        TextUi.printLabeledSeparator("Shadows:");
        TextUi.println(stu);
        //
        // add the results etc onto the tables
        //
        double shInst00 = stu.getInstanceShadowSalience(RsTemplates.DIRECT, 0,
                RsTemplates.DIRECT, 0, EnergyColors.SHI_GENERIC);
        double shInst01 = stu.getInstanceShadowSalience(RsTemplates.DIRECT, 0,
                RsTemplates.DIRECT, 1, EnergyColors.SHI_GENERIC);
        double shInst11 = stu.getInstanceShadowSalience(RsTemplates.DIRECT, 1,
                RsTemplates.DIRECT, 1, EnergyColors.SHI_GENERIC);
        double shInst10 = stu.getInstanceShadowSalience(RsTemplates.DIRECT, 1,
                RsTemplates.DIRECT, 0, EnergyColors.SHI_GENERIC);
        String instanceShadows = "(" + Formatter.fmt(shInst00) + "/"
                + Formatter.fmt(shInst01) + "), (" + Formatter.fmt(shInst11)
                + "/" + Formatter.fmt(shInst10) + ")";
        //
        int vilast = verbs.size() - 1;
        double shVi1 = stu.getViShadowSalience(0, 0, EnergyColors.SHV_GENERIC);
        double shVi2 = stu.getViShadowSalience(vilast, vilast,
                EnergyColors.SHV_GENERIC);
        String viShadows = Formatter.fmt(shVi1) + "..." + Formatter.fmt(shVi2);

        ft.wrappedRow(instance1, instance2, verbs.toString(), instanceShadows,
                viShadows);
        // if (expected != null) {
        // assertEquals(expected, textRow);
        // }
        lf.addTableLine(instance1, instance2, verbs.toString(), instanceShadows,
                viShadows);
        return r;
    }

}
