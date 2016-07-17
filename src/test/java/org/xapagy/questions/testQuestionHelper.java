/*
   This file is part of the Xapagy project
   Created on: Feb 22, 2011
 
   org.xapagy.model.testQuestions
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.questions;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * 
 * Tests
 * 
 * @author Ladislau Boloni
 * 
 */
public class testQuestionHelper {

    /**
     * Verifying if something is a question
     */
    @Test
    public void testIsAQuestion() {
        String description = "The verification if something is a question";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        VerbInstance vi = null;
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");

        // a simple S-V-O action - not a question
        vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Assert.assertFalse(QuestionHelper.isAQuestion(vi, r.agent));
        // a S-V action - not a question
        vi = r.exac("'Hector' / wa_v_av41.");
        Assert.assertFalse(QuestionHelper.isAQuestion(vi, r.agent));
        // a "whether" question
        vi = r.exac("'Hector' / whether wa_v_av40 / 'Achilles'?");
        Assert.assertTrue(QuestionHelper.isAQuestion(vi, r.agent));
        // a why question
        vi = r.exac("'Hector' / why wa_v_av40 / 'Achilles'?");
        Assert.assertTrue(QuestionHelper.isAQuestion(vi, r.agent));
        Assert.assertTrue(QuestionHelper.isWhyQuestion(vi, r.agent));
        // a wh-V-O
        vi = r.exac("Wh / wa_v_av40 / 'Achilles'?");
        Assert.assertTrue(QuestionHelper.isAQuestion(vi, r.agent));
        // a (wh attribute)-V-O
        vi = r.exac("Wh w_c_bai20/ wa_v_av40 / 'Achilles'?");
        Assert.assertTrue(QuestionHelper.isAQuestion(vi, r.agent));
        // quote question
        vi = r.exac("'Achilles' / says / what?");
        Assert.assertTrue(QuestionHelper.isAQuestion(vi, r.agent));
        Assert.assertTrue(QuestionHelper
                .isWhatCommunicatesQuestion(vi, r.agent));
        TestHelper.testDone();

    }
}
