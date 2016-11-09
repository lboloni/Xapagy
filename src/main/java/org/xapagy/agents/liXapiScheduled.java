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
 * A loopitem based on a Xapi string that was scheduled ahead of time to execute
 * at a certain time.
 * 
 * @author lboloni Created on November 1, 2016
 */
public class liXapiScheduled extends AbstractXapiLoopItem {

	private static final long serialVersionUID = -4790922158730493388L;
	/**
	 * The time at which it was scheduled to execute
	 */
	private double scheduledExecutionTime = -1;

	/**
	 * 
	 * @param agent
	 * @param xapiText
	 * @param time
	 */
	public liXapiScheduled(Agent agent, String xapiText, double time) {
		super(agent, xapiText);
		this.scheduledExecutionTime = time;
	}

	@Override
	public String formatException(Throwable t, String description) {
		return "liScheduled: At generated Xapi = " + xapiText + "\nError was found: " + t.getClass().getCanonicalName()
				+ " " + description;
	}

	/**
	 * @return the scheduledExecutionTime
	 */
	public double getScheduledExecutionTime() {
		return scheduledExecutionTime;
	}

	/**
	 * Executes a LoopItem of type "Forced". This assumes that we have the VI.
	 */
	@Override
	protected void internalExecute() {
		Execute.executeXapiText(agent, this);
	}

}
