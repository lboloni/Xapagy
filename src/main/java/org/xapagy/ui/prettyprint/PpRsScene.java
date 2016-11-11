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
package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storygenerator.RsScene;
import org.xapagy.instances.Instance;
import org.xapagy.ui.smartprint.SpInstance;

/**
 * @author Ladislau Boloni
 * Created on: Feb 17, 2013
 */
public class PpRsScene {
    /**
     * Concise printing: fall back on the toString
     * 
     * @param rs
     * @param agent
     * @return
     */
    public static String ppConcise(RsScene rsc, Agent agent) {
        return rsc.toString();
    }

    /**
     * Detailed printout
     * 
     * @param rsc
     * @param agent
     * @return
     */
    public static String ppDetailed(RsScene rsc, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("RsScene");
        fmt.indent();
        fmt.is("Name", rsc.getName());
        fmt.is("LabelScene", rsc.getLabelScene());
        fmt.is("Scene instance", SpInstance.spc(rsc.getSceneInstance(), agent));
        fmt.add("Instance labels");
        fmt.indent();
        for (String label : rsc.getInstanceLabels()) {
            fmt.add(label);
        }
        fmt.deindent();
        fmt.add("Instances");
        fmt.indent();
        for (Instance inst : rsc.getInstances()) {
            fmt.add(SpInstance.spc(inst, agent));
        }
        fmt.deindent();

        fmt.deindent();
        return fmt.toString();
    }
}
