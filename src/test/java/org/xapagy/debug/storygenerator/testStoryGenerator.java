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

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * Created on: Jun 18, 2011
 */
@SuppressWarnings("unused")
public class testStoryGenerator {

    @Test
    public void testNameGenerators() {
        String description =
                "The concept name, proper name and verb name generators of the the story generator";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        for (int i = 0; i != 5; i++) {
            String temp = r.properNameGenerator.generateProperName();
            // TextUi.println(temp);
        }
        TestHelper.testDone();
    }

}
