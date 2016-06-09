package org.xapagy.ui.observers;

import java.io.IOException;
import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;

/**
 * The interface which needs to be implemented by the observers
 * 
 * @author Lotzi Boloni
 * 
 */
public interface IAgentObserver extends Serializable {
    void addObserveWhat(DebugEventType eventType);

    void observe(DebugEvent event) throws IOException, InterruptedException;

    void removeObserveWhat(DebugEventType eventType);

    void setAgent(Agent agent);

    void setEnabled(boolean enabled);
}
