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
package org.xapagy.debug.storygenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xapagy.autobiography.ABStory;
import org.xapagy.ui.TextUi;

/**
 * A factory for frequent narrative structures packaged into RsTestingUnits
 * 
 * This had been created on July 5, 2014. It is supposed to be a relatively
 * complete coverage of the various structures which are possible in a single
 * linear sequence without summaries or multi-scene events
 *
 * <pre>
 * PureRepetition
 *    pure_repetition: identical recall
 *    multiple_pure_repetition: 2, 3, 10, 100
 *    identical features / try it when the instances don't have identical
 *       features
 * DirtyRepetition
 *    dirty_repetition: what if some of those verbs had been used in other stories
 * ApproximateRepetition
 *    extra actions in the memory stories
 *    extra actions in the focus story
 *    flipping actions
 * Forking 
 *    memory stories can fork into two different outcomes...
 * </pre>
 * 
 * @author Ladislau Boloni
 * Created on: Jul 5, 2014
 */
public class RsFrequentNarratives {

    /**
     * A simplified call for the fork, there are two types of stories of
     * Achilles and Hector, each of length 5, with different verbs, they diverge
     * after the 3-rd one.
     * 
     * @param countA
     * @param countB
     * @return
     */
    public static RsTestingUnit createForkSimple(int countA, int countB) {
        List<String> references = Arrays.asList("'Achilles'", "'Hector'");
        List<String> instances =
                Arrays.asList("w_c_bai20 'Achilles'", "w_c_bai21 'Hector'");
        List<String> verbsA = Arrays.asList("wa_v_av10", "wa_v_av11",
                "wa_v_av12", "wa_v_av13", "wa_v_av14");
        List<String> verbsB = Arrays.asList("wa_v_av10", "wa_v_av11",
                "wa_v_av12", "wa_v_av23", "wa_v_av24");
        return createFork(references, instances, countA, verbsA, countB,
                verbsB);
    }

    /**
     * Create a test for forked stories. There are two types of stories A and B.
     * This test is intended for types of stories where they start the same, but
     * then they diverge at some point. By convention we will assume that both
     * the focus and the shadow story are of type A.
     * 
     * @param references
     *            - the way we refer to the two instances in the stories
     * @param instances
     *            - the attributes of the instances
     * @param countA
     *            - the number of stories of type A
     * @param verbsA
     *            - the verbs in the stories of type A
     * @param countB
     *            - the number of stories of type B
     * @param verbsB
     *            - the verbs in the stories of type B
     * 
     * 
     * @return
     */
    public static RsTestingUnit createFork(List<String> references,
            List<String> instances, int countA, List<String> verbsA, int countB,
            List<String> verbsB) {
        // anything but the last previous goes here
        List<RecordedStory> background = new ArrayList<>();
        // Create the background stories
        for (int i = 0; i < countB; i++) {
            RecordedStory rsB = createStory(references, instances, verbsB);
            background.add(rsB);
        }
        // Create the background stories
        for (int i = 0; i < countA - 1; i++) {
            RecordedStory rsA = createStory(references, instances, verbsA);
            background.add(rsA);
        }

        // Create the memory story
        RecordedStory rsMemory = createStory(references, instances, verbsA);
        // Create the focus story - exactly the same
        RecordedStory rsFocus = createStory(references, instances, verbsA);
        // Now assemble the RsTestingUnit
        // the parameter we are going to test
        RsTestingUnit rtu = new RsTestingUnit(
                new ABStory("$Include 'P-FocusAndShadows'"), background,
                rsMemory, new ABStory("$Include 'P-FocusAndShadows'"),
                rsFocus);
        return rtu;
    }

    /**
     * Pure repetition, H and A does not have common features
     * 
     * @param countPrevious
     * @return
     */
    public static RsTestingUnit createPureRepetition(int countPrevious) {
        List<String> references = Arrays.asList("'Achilles'", "'Hector'");
        List<String> instances =
                Arrays.asList("w_c_bai20 'Achilles'", "w_c_bai21 'Hector'");
        List<String> verbs =
                Arrays.asList("wa_v_av10", "wa_v_av11", "wa_v_av12");
        return createRepetition(countPrevious, references, instances, verbs,
                verbs);
    }

    /**
     * Approximate repetition
     * 
     * @param memoryLonger
     *            - if true, the longer one is in the memory
     * @param flipAt
     *            - flip the focus story at this value, -1 for not flipping
     * @return
     */
    public static RsTestingUnit createApproximateRepetition(int memoryLonger,
            int flipAt) {
        List<String> references = Arrays.asList("'Achilles'", "'Hector'");
        List<String> instances =
                Arrays.asList("w_c_bai20 'Achilles'", "w_c_bai21 'Hector'");
        List<String> verbsR1 = Arrays.asList("wa_v_av10", "wa_v_av11");
        List<String> verbsInsert = Arrays.asList("wa_v_av19");
        List<String> verbsR2 =
                Arrays.asList("wa_v_av12", "wa_v_av13", "wa_v_av14");
        // now create the stuff
        // anything but the last previous goes here
        List<RecordedStory> background = new ArrayList<>();
        // Create the memory story
        RecordedStory rsLonger = createStoryWithExtra(references, instances,
                verbsR1, verbsInsert, verbsR2, -1);
        // Create the focus story - exactly the same
        RecordedStory rsShorter = createStoryWithExtra(references, instances,
                verbsR1, new ArrayList<String>(), verbsR2, flipAt);
        // Now assemble the RsTestingUnit
        ABStory p = new ABStory("$Include 'P-FocusAndShadows'");
        RsTestingUnit rtu = null;
        switch (memoryLonger) {
        case 1:
            rtu = new RsTestingUnit(p, background, rsLonger, p, rsShorter);
            break;
        case 0:
            rtu = new RsTestingUnit(p, background, rsShorter, p, rsShorter);
            break;
        case -1:
            rtu = new RsTestingUnit(p, background, rsShorter, p, rsLonger);
            break;
        default:
            TextUi.abort("Unsupported value of memoryLonger " + memoryLonger);
        }
        return rtu;
    }

    /**
     * Pure repetition, H and A does has some common features
     * 
     * @param countPrevious
     * @return
     */
    public static RsTestingUnit
            createPureRepetitionSharedFeatures(int countPrevious) {
        List<String> references = Arrays.asList("'Achilles'", "'Hector'");
        List<String> instances = Arrays.asList("w_c_bai20 w_c_bai22 'Achilles'",
                "w_c_bai21 w_c_bai22 'Hector'");
        List<String> verbs =
                Arrays.asList("wa_v_av10", "wa_v_av11", "wa_v_av12");
        return createRepetition(countPrevious, references, instances, verbs,
                verbs);
    }

    /**
     * Dirtied up repetion: wa_v_av11 had been in other stories
     * 
     * @param countPrevious
     * @return
     */
    public static RsTestingUnit createDirtyRepetition(int countPrevious) {
        List<String> references = Arrays.asList("'Achilles'", "'Hector'");
        List<String> instances =
                Arrays.asList("w_c_bai20 'Achilles'", "w_c_bai21 'Hector'");
        List<String> verbs =
                Arrays.asList("wa_v_av10", "wa_v_av11", "wa_v_av12");
        RsTestingUnit retval = createRepetition(countPrevious, references,
                instances, verbs, verbs);
        // now dirty it up
        List<String> referencesD = Arrays.asList("'Paris'", "'Menelaos'");
        List<String> instancesD =
                Arrays.asList("w_c_bai20 'Paris'", "w_c_bai21 'Menelaos'");
        List<String> verbsD =
                Arrays.asList("wa_v_av16", "wa_v_av11", "wa_v_av17");
        RecordedStory st = createStory(referencesD, instancesD, verbsD);
        retval.getRsHistory().add(st);
        return retval;
    }

    /**
     * Creates a repetition
     * 
     * @param count
     *            - how many times the memory item is repeated (1 in the memory
     *            story, the others in the background)
     * @param count
     *            -
     * @return
     */
    public static RsTestingUnit createRepetition(int countPrevious,
            List<String> references, List<String> instances, List<String> verbs,
            List<String> verbsFocus) {
        // anything but the last previous goes here
        List<RecordedStory> background = new ArrayList<>();
        // Create the background stories
        for (int i = 0; i < countPrevious - 1; i++) {
            RecordedStory rsBg = createStory(references, instances, verbs);
            background.add(rsBg);
        }
        // Create the memory story
        RecordedStory rsMemory = createStory(references, instances, verbs);
        // Create the focus story - exactly the same
        RecordedStory rsFocus = createStory(references, instances, verbsFocus);
        // Now assemble the RsTestingUnit
        ABStory p = new ABStory("$Include 'P-FocusAndShadows'");
        RsTestingUnit rtu =
                new RsTestingUnit(p, background, rsMemory, p, rsFocus);
        return rtu;
    }

    /**
     * Creates a reciprocal story between 'Achilles' and 'Hector'
     * 
     * @param references
     *            - the way we refer to the instances, usually not with all the
     *            features
     * @param instances
     *            - the attributes of the instances
     * @param verbs
     * @return
     */
    private static RecordedStory createStory(List<String> references,
            List<String> instances, List<String> verbs) {
        RecordedStory rs = new RecordedStory(RsTemplates.DIRECT);
        rs.getRsScene(RsTemplates.DIRECT).setInstanceLabels(instances);
        ABStory storyMemory = RsTemplates.generateReciprocalAction(
                references.get(0), references.get(1), verbs);
        rs.addStory(storyMemory);
        return rs;
    }

    /**
     * Creates a reciprocal story between 'Achilles' and 'Hector'
     * 
     * @param references
     *            - the way we refer to the instances, usually not with all the
     *            features
     * @param instances
     *            - the attributes of the instances
     * @param verbs
     * @param flipAt
     *            - flips two stories at this
     * @return
     */
    private static RecordedStory createStoryWithExtra(List<String> references,
            List<String> instances, List<String> verbsReciprocal1,
            List<String> verbsOneWay, List<String> verbsReciprocal2,
            int flipAt) {
        RecordedStory rs = new RecordedStory(RsTemplates.DIRECT);
        rs.getRsScene(RsTemplates.DIRECT).setInstanceLabels(instances);
        ABStory all = new ABStory();
        ABStory story = RsTemplates.generateReciprocalAction(references.get(0),
                references.get(1), verbsReciprocal1);
        all.add(story);
        story = RsTemplates.generateOneWayAction(references.get(0),
                references.get(1), verbsOneWay);
        all.add(story);
        story = RsTemplates.generateReciprocalAction(references.get(0),
                references.get(1), verbsReciprocal2);
        all.add(story);
        // flip at flipAt
        if (flipAt > 0) {
            ABStory flipped = new ABStory();
            flipped.add(all.substory(0, flipAt - 1));
            flipped.add(all.substory(flipAt + 1, flipAt + 1));
            flipped.add(all.substory(flipAt, flipAt));
            flipped.add(all.substory(flipAt + 2, all.length() - 1));
            all = flipped;
        }
        rs.addStory(all);
        return rs;
    }

}
