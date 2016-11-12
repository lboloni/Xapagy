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
package org.xapagy.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.ui.formatters.Formatter;

/**
 * Test for the naive implementation of the probability-proportional-to-size
 * sampling - correctness and performance
 * 
 * @author Ladislau Boloni
 * Created on: Sep 27, 2012
 */
public class testPpsNaive {

    class LocalSized implements ISized {
        int id;
        double size;

        public LocalSized(int id, double size) {
            this.id = id;
            this.size = size;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.xapagy.algorithm.ISized#getSize()
         */
        @Override
        public double getSize() {
            return size;
        }

    }

    /**
     * Tests for the correctness of the algorithm
     */
    @Test
    public void testCorrectness() {
        String description =
                "Tests the correctness of the results of the PpsNaive algorithm";
        TestHelper.testStart(description);
        Random r = new Random();
        List<LocalSized> list = new ArrayList<>();
        int numberOfItems = 100; // was 100
        int chooseNumer = 20;
        int numberOfIterations = 1000;
        // double totalSize = 0;
        for (int i = 0; i != numberOfItems; i++) {
            // make the distribution quadratic
            double size = r.nextDouble() * r.nextDouble();
            LocalSized ls = new LocalSized(i, size);
            list.add(ls);
        }

        // counters
        int countSelected[] = new int[numberOfItems];
        Arrays.fill(countSelected, 0);
        for (int it = 0; it != numberOfIterations; it++) {
            PpsNaive<LocalSized> alg = new PpsNaive<>();
            List<LocalSized> chosen = alg.choose(list, chooseNumer, r);
            // update the count selected
            for (LocalSized lsx : chosen) {
                countSelected[lsx.id]++;
            }
        }
        // print the results
        Formatter fmt = new Formatter();
        for (int i = 0; i != numberOfItems; i++) {
            double countWeight = (double) countSelected[i] / numberOfIterations;
            fmt.add(Formatter.padTo(i, 5)
                    + Formatter.padTo(Formatter.fmt(list.get(i).getSize()), 10)
                    + "S:" + Formatter.padTo(Formatter.fmt(countWeight), 10));
        }
        // TextUi.println(fmt);
        TestHelper.testIncomplete();
    }

    /**
     * On the office machine, it takes 1 second to choose 20 out of 20e6, it is
     * unlikely to be the bottleneck currently
     * 
     */
    @Test
    public void testPpsNaivePerformance() {
        String description = "Tests the performance of the PpsNaive algorithm";
        TestHelper.testStart(description);
        Random r = new Random();
        List<LocalSized> list = new ArrayList<>();
        // int numberOfItems = 84 * 1000 * 1000; // full set of VIs of 4 year
        // old
        // int numberOfItems = 20 * 1000 * 1000; // largest number stored
        // without increasing heap
        int numberOfItems = 20 * 1000;
        //Parameters p = new Parameters();
        //InitParameters.init(p);
        int chooseNumber = 10;
        //        p.getInteger("A_SHM", "G_GENERAL",
        //                "N_SHADOW_ITEMS_PER_ITERATION");
        int numberOfIterations = 1000;
        // double totalSize = 0;
        for (int i = 0; i != numberOfItems; i++) {
            // make the distribution quadratic
            double size = r.nextDouble() * r.nextDouble();
            LocalSized ls = new LocalSized(i, size);
            list.add(ls);
        }
        long timeStart = System.currentTimeMillis();
        // counters
        int countSelected[] = new int[numberOfItems];
        Arrays.fill(countSelected, 0);
        for (int it = 0; it != numberOfIterations; it++) {
            PpsNaive<LocalSized> alg = new PpsNaive<>();
            List<LocalSized> chosen = alg.choose(list, chooseNumber, r);
            // update the count selected
            for (LocalSized lsx : chosen) {
                countSelected[lsx.id]++;
            }
        }
        long timeEnd = System.currentTimeMillis();
        long time = timeEnd - timeStart;
        //String text =
        //        "For " + numberOfItems + " selecting " + chooseNumber
        //                + " repeated " + numberOfIterations + " times took "
        //                + TextUiHelper.formatTimeInterval(timeEnd - timeStart);
        // TextUi.println(text);
        // what this thing tests is that for the specific number of matches
        // the algorithm takes less than a millisecond
        Assert.assertTrue(time < 10 * numberOfIterations);
        TestHelper.testDone();
    }

}
