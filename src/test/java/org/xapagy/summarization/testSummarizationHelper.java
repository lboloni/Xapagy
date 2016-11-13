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
package org.xapagy.summarization;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * Created on: Dec 27, 2014
 */
public class testSummarizationHelper {


    @Test
    public void test() {
        String description =
                "Test the functions of the summarization helper";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #one CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");

        VerbInstance vi = r.exac("'Achilles'/ wa_v_av40 / 'Hector'.");
        // this is not summarization
        boolean value = SummarizationHelper.isSummarization(r.agent, vi);
        assertFalse(value);
        
        vi = r.exac("'Achilles' / ##Summarization wv_v_av43 / 'Hector'.");
        // this is not yet a summarization, it becomes when it has the link
        value = SummarizationHelper.isSummarization(r.agent, vi);
        assertFalse(value);
        
        r.exec("$CreateLink 'Summarization_Begin' { 'wa_v_av40' } ==> { 'wv_v_av43' }");

        // it is now an open, not closed summarization
        value = SummarizationHelper.isSummarization(r.agent, vi);
        assertTrue(value);
        value = SummarizationHelper.isOpenSummarization(r.agent, vi);
        assertTrue(value);
        value = SummarizationHelper.isClosedSummarization(r.agent, vi);
        assertFalse(value);
        
        
        r.exac("'Hector'/ #act wa_v_av41 / 'Achilles'.");
        r.exec("$CreateLink 'Summarization_Body' { 'wa_v_av41' } ==> { 'wv_v_av43' }");
        r.exac("'Achilles'/ #act wa_v_av42 / 'Hector'.");
        r.exec("$CreateLink 'Summarization_Close' { 'wa_v_av42' } ==> { 'wv_v_av43' }");

        // it is now a closed, not open summarization
        value = SummarizationHelper.isSummarization(r.agent, vi);
        assertTrue(value);
        value = SummarizationHelper.isOpenSummarization(r.agent, vi);
        assertFalse(value);
        value = SummarizationHelper.isClosedSummarization(r.agent, vi);
        assertTrue(value);
        
        TestHelper.testDone();

    }    

    
}
