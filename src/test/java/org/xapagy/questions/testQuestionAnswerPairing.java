/*
   This file is part of the Xapagy project
   Created on: Nov 5, 2012
 
   org.xapagy.questions.testSyntacticPairing
 
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
 * @author Ladislau Boloni
 * 
 */
public class testQuestionAnswerPairing {

    /**
     * Verifying if something is a question
     */
    @Test
    public void testIsSyntacticPair() {
        String description =
                "Checks whether a question and a proposed answer is a syntactic pair";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        VerbInstance viQuestion = null;
        VerbInstance viAnswer = null;
        
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");

        r.exec("$CreateScene #Legend With Instances w_c_bai20 'Ulysses', w_c_bai20 'Ajax'");

        // a simple S-V-O action - not a question
        viQuestion = r.exac("Wh / wa_v_av40 / 'Achilles'?");
        viAnswer = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        boolean matchSimple =
                QuestionAnswerPairing.isSyntacticPair(viQuestion, viAnswer,
                        r.agent);
        // a (wh attribute)-V-O - FIXME: this fails!!!
        viQuestion = r.exac("Wh w_c_bai20 / wa_v_av40 / 'Achilles'?");
        boolean matchFiltered =
                QuestionAnswerPairing.isSyntacticPair(viQuestion, viAnswer,
                        r.agent);
        // a "whether" question
        viQuestion = r.exac("'Hector' / whether wa_v_av40 / 'Achilles'?");
        boolean matchWhether =
                QuestionAnswerPairing.isSyntacticPair(viQuestion, viAnswer,
                        r.agent);
        // a why question
        viQuestion = r.exac("'Hector' / why wa_v_av40 / 'Achilles'?");
        // DEBUG: this get SaMvIsA: adding incompatible concepts, why?
        // VerbInstance viAny = r.exac("A dog / is-a / red.");
        VerbInstance viAny = r.exac("'Hector' / wa_v_av41 / 'Achilles'.");
        boolean matchWhy =
                QuestionAnswerPairing.isSyntacticPair(viQuestion, viAny,
                        r.agent);
        // quote question
        viQuestion = r.exac("'Achilles' / says / what?");
        VerbInstance viQuote =
                r.exac("'Achilles' / says in #Legend // 'Ulysses' / wa_v_av40 / 'Ajax'.");
        boolean matchWhatSays =
                QuestionAnswerPairing.isSyntacticPair(viQuestion, viQuote,
                        r.agent);
        // now let us do the assignments
        Assert.assertTrue(matchSimple);
        Assert.assertTrue(matchFiltered);
        Assert.assertTrue(matchWhether);
        Assert.assertTrue(matchWhy);
        Assert.assertTrue(matchWhatSays);
        TestHelper.testDone();

    }

}
