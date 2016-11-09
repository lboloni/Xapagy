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
package org.xapagy.headless_shadows;

import java.util.Comparator;

import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 * Created on: Jun 28, 2012
 */
public class FslComparator implements Comparator<FocusShadowLinked> {

    private Agent agent;

    public FslComparator(Agent agent) {
        this.agent = agent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(FocusShadowLinked arg0, FocusShadowLinked arg1) {
        return Double.compare(arg0.getTotalSupport(agent),
                arg1.getTotalSupport(agent));
    }

}
