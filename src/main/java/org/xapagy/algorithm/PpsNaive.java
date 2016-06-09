/*
   This file is part of the Xapagy project
   Created on: Sep 25, 2012
 
   org.xapagy.algorithm.PpsNaive
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
