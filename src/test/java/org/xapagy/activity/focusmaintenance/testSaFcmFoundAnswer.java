/*
   This file is part of the Xapagy project
   Created on: Oct 13, 2011
 
   org.xapagy.activity.testSaFoundAnswer
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.questions.QuestionHelper;

/**
 * @author Ladislau Boloni
 * 
 */
public class testSaFcmFoundAnswer {

    @Test
    public void testIdentifyAnswer() {
        String description = "Checks whether matched answers are linked.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Reality CloseOthers With Instances w_c_bai20, w_c_bai21");

        VerbInstance viAnswer =
                r.exac("The w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        VerbInstance viNotAnswer =
                r.exac("The w_c_bai20 / wa_v_av41 / the w_c_bai21.");
        VerbInstance viQuestion = r.exac("Wh / wa_v_av40 / the w_c_bai21?");
        Assert.assertTrue(QuestionHelper.decideQuestionAnswerLinkExists(
                r.agent, viQuestion, viAnswer));
        Assert.assertTrue(!QuestionHelper.decideQuestionAnswerLinkExists(
                r.agent, viQuestion, viNotAnswer));
        TestHelper.testDone();
    }

}
