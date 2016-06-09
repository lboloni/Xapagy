/*
   This file is part of the Xapagy project
   Created on: Feb 10, 2012
 
   org.xapagy.activity.shadowmaintenance.SHMConstants
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

/**
 * @author Ladislau Boloni
 * 
 */
public class SHMConstants {

    public static final double instanceMatchBaseLevel_Max = 0.4;
    /**
     * The expected value when the instances match each other at the base level,
     * but no other support.
     */
    public static final double instanceMatchBaseLevel_Min = 0.3;
    public static final double instanceMatchNoProperName_Max = 0.33;
    /**
     * The expected value when the instances match each other at the base level,
     * but have different proper name and no other support
     */
    public static final double instanceMatchNoProperName_Min = 0.28;
    public static final double instanceMatchProperName_Max = 0.5;
    /**
     * The expected value when the instances match each other at at base level
     * and a proper name, but no other support
     */
    public static final double instanceMatchProperName_Min = 0.4;

}
