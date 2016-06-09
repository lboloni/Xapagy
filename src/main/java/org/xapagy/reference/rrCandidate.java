/*
   This file is part of the Xapagy project
   Created on: Feb 21, 2014
 
   org.xapagy.reference.rrCandidate
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
                xwRrCandidate.pwDetailed(twf, this, PrettyPrint.lastAgent);
        return retval;
    }

}
