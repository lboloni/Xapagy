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
 * Implements a loop item that is based on interpreting a line of Xapi 
 * 
 * @author Ladislau Boloni
 *
 */
public abstract class AbstractXapiLoopItem extends AbstractLoopItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3265034876385485965L;

	public String getXapiText() {
		return xapiText;
	}

	/**
	 * The xapi text which the agent is reading at this point - it 
	 * is strictly one line
	 */
	protected String xapiText;
	
	public AbstractXapiLoopItem(Agent agent, String xapiText) {
		super(agent);
		this.xapiText = xapiText;
	}
	
}
