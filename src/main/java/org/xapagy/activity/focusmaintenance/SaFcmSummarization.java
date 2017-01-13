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
package org.xapagy.activity.focusmaintenance;

import java.util.List;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.summarization.SummarizationHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * This class implements the actions related to summarization for the 
 * newly created VI. 
 * 
 * @author Ladislau Boloni
 * Created on Jan 13, 2017
 */
public class SaFcmSummarization extends SpikeActivity {

	private static final long serialVersionUID = 2592801386676711424L;

	public enum ViSuviRelation {Indifferent, Joins, Extends, Closes, Terminates, Opens};
	
	/**
	 * 
	 * @param agent
	 * @param name
	 * @param vi
	 */
	public SaFcmSummarization(Agent agent, String name) {
		super(agent, name);
	}

	
	@Override
	public void formatTo(IXwFormatter fmt, int detailLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyInner(VerbInstance vi) {
		// identify all the VIs that might possibly be suvi's for this one
		int sumlevel = vi.getSummarizationLevel();
		String ec = SummarizationHelper.getFocusEnergyColor(vi.getSummarizationLevel()+1);
		List<VerbInstance> suvis = agent.getFocus().getViList(ec);
		// this number will count how many summarizations will the vi be part of
		int partof = 0;
		for(VerbInstance suvi: suvis) {
			ViSuviRelation action = visuviAction(vi, suvi);
		}
	}
	
	/**
	 * The function enacts the relationship between the vi and the suvi
	 * @param vi
	 * @param suvi
	 */
	public ViSuviRelation visuviAction(VerbInstance vi, VerbInstance suvi) {
		ViSuviRelation rel = identifyViSuviRelation(vi, suvi);
		switch(rel) {
		case Indifferent:
			// the vi and the suvi are indifferent to each other, do nothing
			return rel;
		case Joins:
			// they are compatible and they refer the the same things
			// The vi is added as part of the body of the suvi
			throw new Error("Not implemented yet");
		case Extends: 
			// It is going to get the link as elaboration
			//It will also extend the suvi with a new coincidence group
			throw new Error("Not implemented yet");
		case Closes:
			// It adds the closed link, itself becoming part of the suvi
			throw new Error("Not implemented yet");
		case Terminates:
			// After this vi, the suvi will not be an open summary any more 
			// but this vi will not be part of the suvi
			throw new Error("Not implemented yet");
		}
		// should not get here
		return rel;
	}
	
	
	public ViSuviRelation identifyViSuviRelation(VerbInstance vi, VerbInstance suvi) {
		TextUi.println("visuviAction not implemented");
		return ViSuviRelation.Indifferent;
	}
	

	@Override
	public void extractParameters() {
		// TODO Auto-generated method stub
		
	}

}
