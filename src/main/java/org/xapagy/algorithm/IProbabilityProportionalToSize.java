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
/*
   This file is part of the Xapagy project
   
 
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
 * Created on: Sep 25, 2012
 */
public interface IProbabilityProportionalToSize<T extends ISized> {

    public List<T> choose(List<T> list, int n, Random r);

}
