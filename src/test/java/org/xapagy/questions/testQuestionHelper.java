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
 * Created on: Feb 22, 2011
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
