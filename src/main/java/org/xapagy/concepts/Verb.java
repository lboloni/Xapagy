package org.xapagy.concepts;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.metaverbs.AbstractSaMetaVerb;

public class Verb extends AbstractConcept implements Serializable {

    private static final long serialVersionUID = 8759727500307496910L;
    private AbstractSaMetaVerb spike;

    /**
     * @param type
     * @param label
     */
    public Verb(String name, String identifier, AbstractSaMetaVerb spike) {
        super(name, identifier);
        this.spike = spike;
    }

    /**
     * @return the spike
     */
    public AbstractSaMetaVerb getSpike() {
        return spike;
    }

    /**
     * If the verb is a meta-verb, execute it
     * 
     * @param vi - the VI on which we apply the meta-verb
     * @param agent
     */
    public void safeExecute(VerbInstance vi, Agent agent) {
        if (spike == null) {
            return;
        }
        if (spike.getViType() != null
                && !vi.getViType().equals(spike.getViType())) {
            throw new Error("Verb " + name + " can only be applied to "
                    + spike.getViType() + " and the current instance is "
                    + vi.getViType());
        }
        spike.apply(vi);
    }

    @Override
    public String toString() {
        return "[" + name + "]";
    }

}
