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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.xapagy.activity.DaComposite;
import org.xapagy.activity.DiffusionActivity;

/**
 * The performance meter used to measure and store the time spent in various DAs. A single 
 * performance meter is attached to an agent. The DAs automatically register 
 * 
 * @author Ladislau Boloni
 * Created on: Jul 14, 2011
 */
public class PerformanceMeter implements Serializable {

    private static final long serialVersionUID = -4607002341500974686L;
    /**
     * The list of diffusion activities. Every DA at creation time registers into this value.
     */
    private Set<DiffusionActivity> das;
    private long timeAtReset;
    //private boolean stop;

    /**
     * Constructor. Called on demand from agent. Makes an initial reset of the time
     */
    public PerformanceMeter() {
        das = new HashSet<>();
        timeCountReset();
        //stop = false;
    }

    /**
     * @return the das
     */
    public Set<DiffusionActivity> getDas() {
        return das;
    }

    /**
     * Returns the time spent in DAs since the reset (don't count the omnibus DAs)
     * 
     * @return
     */
    public long getDaTimeSinceReset() {
        long retval = 0;
        for (DiffusionActivity da : das) {
            if (da instanceof DaComposite) {
                continue;
            }
            retval = retval + da.getTimeSpentSinceReset();
        }
        return retval;
    }

    /**
     * Returns the total time spent in DAs (don't count the omnibus)
     * 
     * @return
     */
    public long getDaTotalTime() {
        long retval = 0;
        for (DiffusionActivity da : das) {
            if (da instanceof DaComposite) {
                continue;
            }
            retval = retval + da.getTimeSpent();
        }
        return retval;
    }

    /**
     * @return the timeAtReset
     */
    public long getTimeAtReset() {
        return timeAtReset;
    }

    /**
     * Adds a new diffusion activity to the measures ones
     * @param da
     */
    public void registerDA(DiffusionActivity da) {
        das.add(da);
    }

    /**
     * Resets the time count of all the DAs and records the current time
     */
    public void timeCountReset() {
        for (DiffusionActivity da : das) {
            da.timeCountReset();
        }
        timeAtReset = System.currentTimeMillis();
    }

}
