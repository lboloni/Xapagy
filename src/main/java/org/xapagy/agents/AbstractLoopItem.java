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
import java.util.ArrayList;
import java.util.List;

import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * A loop item is the execution unit of the Xapagy agent.
 *
 * @author Ladislau Boloni
 * Created on: Sep 11, 2011
 */
public abstract class AbstractLoopItem implements XapagyComponent, Serializable {

	/**
	 * The state of the loop item: each loop item can be executed at most once.
	 *
	 * @author Ladislau Boloni
	 *
	 */
	public enum LoopItemState {
		EXECUTED, NOT_EXECUTED
	}

	private static final long serialVersionUID = 5644893720655147749L;
	public static String MACRO_PREFIX = "$";
	protected Agent agent;
	/**
	 * Stores the VIs that resulted from the execution of this loop item. They
	 * can be more than one (for instance, when a single Xapi maps to more than
	 * one step), or when intervals
	 */
	protected List<VerbInstance> executionResult = new ArrayList<>();
	protected double executionTime = -1;
	/**
	 * Xapagy identifier
	 */
	protected String identifier;
	protected LoopItemState state = null;


	public AbstractLoopItem(Agent agent) {
		identifier = agent.getIdentifierGenerator().getLoopItemIdentifier();
		agent.getLoop().getAllLoopItems().put(identifier, this);
		this.agent = agent;
		this.state = LoopItemState.NOT_EXECUTED;
	}

	/**
	 * Executes a loop item
	 *
	 * @param notifyObservers
	 *            - if this is set to false, it is called from an observer, so
	 *            we don't put it into the history and do not re-notify
	 *            observers to avoid an infinite loop
	 */
	public void execute(boolean notifyObservers) {
		// this allows recursive execution
		AbstractLoopItem current = agent.getLoop().getInExecution();
		agent.getLoop().setInExecution(this);
		agent.notifyObservers(new DebugEvent(DebugEventType.BEFORE_LOOP_ITEM_EXECUTION));
		// the type specific execute part
		internalExecute();
		executionTime = agent.getTime();
		state = LoopItemState.EXECUTED;
		if (notifyObservers) {
			agent.notifyObservers(new DebugEvent(DebugEventType.AFTER_LOOP_ITEM_EXECUTION));
			agent.getLoop().getHistory().add(this);
		}
		agent.getLoop().setInExecution(current);
	}

	public abstract String formatException(Throwable t, String description);

	/**
	 * @return the executionResult
	 */
	public List<VerbInstance> getExecutionResult() {
		return executionResult;
	}

	/**
	 * @return the executionTime
	 */
	public double getExecutionTime() {
		return executionTime;
	}

	/**
	 * @return the identifier
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return the state
	 */
	public LoopItemState getState() {
		return state;
	}

	/**
	 * The type specific execute part - this should go into the individual
	 * classes
	 */
	protected abstract void internalExecute();

	@Override
	public String toString() {
		return PrettyPrint.ppString(this);
	}

}
