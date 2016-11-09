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
package org.xapagy.agents;

/**
 * The objective of this class is to collect the type of values which are
 * supposed to be achieved in various components of the system.
 * 
 * These are really numbers without too much meaning, but it is better for them
 * to be here and explained.
 * 
 * 
 * @author Ladislau Boloni
 * Created on: Mar 17, 2013
 */
public class Calibration {

    /**
     * This is a value used by Incompatibility.decideIncompatibility to decide
     * whether two overlays are incompatible or not.
     */
    public static final double decideIncompatibility = 0.05; // was 0.5

    /**
     * Used by relation helper to decide whether if a relation verb is
     * sufficiently present in order to be considered...
     */
    public static final double decideRelationPresent = 0.5;
    /**
     * This is a value used by Similarity.decideSimilarity to decide whether two
     * overlays are similar enough to be considered the same. This is set up in
     * such a way that two action verbs are not similar just because of the
     * verbs....
     */
    public static final double decideSimilarity = 0.7;
}
