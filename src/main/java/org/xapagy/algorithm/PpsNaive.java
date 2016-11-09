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
import java.util.List;
import java.util.Random;

/**
 * Naive implementation of the PPS algorithm, O(n^2) complexity
 * 
 * @author Ladislau Boloni
 * @param <P>
 * @param <T>
 * 
 * Created on: Sep 25, 2012
 */
public class PpsNaive<T extends ISized> implements
        IProbabilityProportionalToSize<T> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.algorithm.IProbabilityProportionalToSize#choose(java.util.
     * List, int, java.util.Random)
     */
    @Override
    public List<T> choose(List<T> list, int n, Random r) {
        double totalScore = 0;
        for (T t : list) {
            totalScore += t.getSize();
        }
        List<T> retval = new ArrayList<>();
        if (n >= list.size()) {
            retval.addAll(list);
            return retval;
        }
        double localTotal = totalScore;
        List<T> localList = new ArrayList<>(list);
        for (int i = 0; i != n; i++) {
            double val = r.nextDouble() * localTotal;
            double val1 = 0;
            T chosen = null;
            for (T item : localList) {
                val1 += item.getSize();
                if (val1 > val) {
                    chosen = item;
                    break;
                }
            }
            if (chosen == null) {
                // TextUi.println("PpsNaive - choosen null???");
                break;
            }
            retval.add(chosen);
            localTotal -= chosen.getSize();
            localList.remove(chosen);
        }
        return retval;
    }

}
