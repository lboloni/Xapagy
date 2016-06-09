/*
   This file is part of the Xapagy project
   Created on: Sep 25, 2012
 
   org.xapagy.algorithm.IProbabilityProportionalToSize
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.algorithm;

import java.util.List;
import java.util.Random;

/**
 * An interface for algorithms which implement the probability proportional to
 * size sampling algorithm
 * 
 * Probability-proportional-to-size sampling (PPS sampling)
 * 
 * A naive implementation is in PpsNaive
 * 
 * Doing it the right way and fast is a non-trivial challenge
 * 
 * <ul>
 * <li>
 * http://publib.boulder.ibm.com/infocenter/spssstat/v20r0m0/index.jsp?topic=%2F
 * com.ibm.spss.statistics.help%2Falg_csselect_ppswor.htm
 * <li>
 * JVM based interpreter for R
 * https://code.google.com/p/renjin/source/browse/trunk/core/src/test/
 * resources/survey/DESCRIPTION?r=424
 * <li>
 * http://cran.r-project.org/web/packages/survey/index.html
 * </ul>
 * 
 * @author Ladislau Boloni
 * 
 */
public interface IProbabilityProportionalToSize<T extends ISized> {

    public List<T> choose(List<T> list, int n, Random r);

}
