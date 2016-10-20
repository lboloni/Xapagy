/*
   This file is part of the Xapagy project
   Created on: Sep 11, 2011
 
   org.xapagy.agents.LoopItem
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * A loop item is the execution unit of the Xapagy agent. One loop item can
 * correspond to:
 * 
 * <ul>
 * <li>A Xapi sentence describing a VI.
 * <li>A Xapi meta-statement
 * <li>An internally generated choice.
 * <li>A currently pending summarization (FIXME)
 * <li>A forced external VI
 * </ul>
 * 
 * @author Ladislau Boloni
 * 
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

	/**
	 * The type of the loop item:
	 * 
	 * <ul>
	 * <li>EXTERNAL - an external observation (this normally appears as a
	 * scheduled loop item when Xapagy is run from the command line.
	 * <li>READING - a loop item which is retrieved from the reading list
	 * <li>INTERNAL - a loop item which is generated internally by Xapagy
	 * (typically, a result of a choice selected for execution)
	 * <li>FORCED - a forced, externally generated VI - for instance as coming
	 * from the story line reasoning
	 * </ul>
	 * 
	 * @author Ladislau Boloni
	 *
	 */
	public enum LoopItemType {
		XAPI_SCHEDULED, HLS_CHOICE_BASED, XAPI_READING, VI_BASED
	};

	private static final long serialVersionUID = 5644893720655147749L;




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
	/**
	 * For external or internal loopitems, the Xapi text that generates this
	 * loopitem
	 */
	protected String xapiText;
	/**
	 * READING, INTERNAL, EXTERNAL or FORCED
	 */
	protected LoopItemType type = null;

	public static String MACRO_PREFIX = "$";

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


	/**
	 * The type specific execute part - this should go into the individual classes
	 */
	protected abstract void internalExecute();

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
	 * @return the text
	 */
	public String getXapiText() {
		return xapiText;
	}

	/**
	 * @return the type
	 */
	public LoopItemType getType() {
		return type;
	}


	@Override
	public String toString() {
		return PrettyPrint.ppString(this);
	}

}
