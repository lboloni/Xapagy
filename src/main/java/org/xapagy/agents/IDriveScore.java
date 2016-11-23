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

import org.xapagy.instances.VerbInstance;

/**
 * @author lboloni Created on: Nov 23, 2016
 */
public interface IDriveScore {

	/**
	 * Returns the score for a verb instance to be executed after a delay of "delayTime" from now, from the
	 * perspective and with the drives of the self specified in the drives
	 * 
	 * We assume that scores calculated from VIs in this way are additive. 
	 * 
	 * @param agent - the agent in which this is considered
	 * @param drives - the drives considered - it also specifies the self from whose perspective 
	 * the drives are evaluated. This might not necessarily be the agent 
	 * @param delayTime - the score is evaluated at a certain amount of time from now
	 * @param vi - the VI whose score we are contributing
	 * 
	 * @author lboloni
	 * @return
	 */
	double score(Agent agent, Drives drives, double delayTime, VerbInstance vi);

}
