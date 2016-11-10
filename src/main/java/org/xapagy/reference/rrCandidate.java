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
package org.xapagy.reference;

import org.xapagy.instances.Instance;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwRrCandidate;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * A class which encapsulates a reference resolution candidate
 * 
 * @author Ladislau Boloni
 * Created on: Feb 21, 2014
 */
public class rrCandidate {

    private double assignedScore;
    private Instance instance;
    private rrContext rrc;
    private rrState state;

    /**
     * @param instance
     * @param rrc
     */
    public rrCandidate(Instance instance, rrContext rrc) {
        super();
        this.instance = instance;
        this.rrc = rrc;
    }

    public double getAssignedScore() {
        return assignedScore;
    }

    public Instance getInstance() {
        return instance;
    }

    public rrContext getRrc() {
        return rrc;
    }

    public rrState getState() {
        return state;
    }

    public void setAssignedScore(double assignedScore) {
        this.assignedScore = assignedScore;
    }

    public void setState(rrState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        TwFormatter twf = new TwFormatter();
        String retval =
                xwRrCandidate.xwDetailed(twf, this, PrettyPrint.lastAgent);
        return retval;
    }

}
