package org.xapagy.shadows;

import java.util.Comparator;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;

/**
 * Comparator which sorts shadows of Instances by ENERGY or SALIENCE of a
 * specific shadow color
 * 
 * 
 * @author Ladislau Boloni
 * Created on: Feb 7, 2012
 */
public class ShadowInstanceComparator implements Comparator<Instance> {

    enum SortedBy {
        ENERGY, SALIENCE
    };

    private Agent agent;
    private String color;
    /**
     * The focus object, we are comparing the other instances based on their
     * values in the shadow of this instance
     */
    private Instance fi;
    private SortedBy sortedBy;

    public ShadowInstanceComparator(SortedBy sortedBy, Agent agent,
            Instance fi, String color) {
        super();
        this.sortedBy = sortedBy;
        this.agent = agent;
        this.fi = fi;
        this.color = color;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Instance o1, Instance o2) {
        Shadows sf = agent.getShadows();
        switch (sortedBy) {
        case ENERGY:
            return Double.compare(sf.getEnergy(fi, o1, color),
                    sf.getEnergy(fi, o2, color));
        case SALIENCE:
            return Double.compare(sf.getSalience(fi, o1, color),
                    sf.getSalience(fi, o2, color));
        }
        // should not happen
        throw new Error("Should not happen");
    }

}
