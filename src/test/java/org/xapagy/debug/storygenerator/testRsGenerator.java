/*
   This file is part of the Xapagy project
   Created on: Feb 8, 2013
 
   org.xapagy.debug.testSGV2
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug.storygenerator;

import java.util.Arrays;

import org.junit.Test;

import org.xapagy.ArtificialDomain;

/**
 * A series of tests for the RsGenerator
 * 
 * @author Ladislau Boloni
 * 
 */
public class testRsGenerator {

    public static boolean printIt = true;
    public static boolean runIt = true;

    /**
     * The RsGenerator is used to generate a setting where a narrator narrates a
     * bare-bones reciprocal story in the quoted scene. There are no identities
     * set up.
     * 
     */
    @Test
    public void testNarratedReciprocal() {
        RecordedStory rs =
                RsGenerator.generateNarratedReciprocal(
                        "w_c_bai3 'propername1'", "w_c_bai1", "w_c_bai2",
                        Arrays.asList("wa_v_av1", "wa_v_av2", "wa_v_av3"));
        ArtificialDomain
                .doIt("Two scenes, narration from the direct scene of a reciprocal story",
                        rs, testRsGenerator.printIt, testRsGenerator.runIt);
    }

    /**
     * The RsGenerator is used to generate a setting where a narrator narrates a
     * bare-bones reciprocal story in the quoted scene. There are no identities
     * set up.
     * 
     * In addition, the system is used to create random proper-names for all
     * instances. This is used in certain older tests.
     * 
     */
    @Test
    public void testNarratedReciprocalWithRandomPropernames() {
        ProperNameGenerator png = new ProperNameGenerator();
        RecordedStory rs =
                RsGenerator.generateNarratedReciprocal(
                        "w_c_bai3 'propername1'", "w_c_bai1", "w_c_bai2",
                        Arrays.asList("wa_v_av1", "wa_v_av2", "wa_v_av3"));
        rs.addRandomPropernames(png);
        ArtificialDomain
                .doIt("Two scenes, narration from the direct scene of a reciprocal story",
                        rs, testRsGenerator.printIt, testRsGenerator.runIt);
    }

    /**
     * The RsGenerator is used to generate a bare-bones reciprocal story in a
     * single scene.
     * 
     */
    @Test
    public void testReciprocal() {
        RecordedStory rs =
                RsGenerator.generateReciprocal("w_c_bai1", "w_c_bai2",
                        Arrays.asList("wa_v_av1", "wa_v_av2", "wa_v_av3"));
        ArtificialDomain.doIt("Only direct scene, simple reciprocal", rs,
                testRsGenerator.printIt, testRsGenerator.runIt);
    }
}
