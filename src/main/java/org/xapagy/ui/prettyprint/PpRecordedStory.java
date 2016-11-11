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
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsScene;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * @author Ladislau Boloni
 * Created on: Feb 17, 2013
 */
public class PpRecordedStory {
    /**
     * Concise printing: fall back on the toString
     * 
     * @param rs
     * @param agent
     * @return
     */
    public static String ppConcise(RecordedStory rs, Agent agent) {
        return rs.toString();
    }

    public static String ppDetailed(RecordedStory rs, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("RecordedStory");
        fmt.indent();
        fmt.add("Stories");
        fmt.indent();
        fmt.add("Initialization");
        fmt.addIndented(rs.getStoryInitialization());
        fmt.add("Post-initialization");
        fmt.addIndented(rs.getStoryPostInitialization());
        for (int i = 0; i != rs.getStories().size(); i++) {
            fmt.add("Story " + (i + 1));
            fmt.addIndented(rs.getStories().get(i));
        }
        fmt.deindent();
        //
        // The scenes - it will also print the instances here
        //
        fmt.add("Scenes:");
        fmt.indent();
        for (RsScene rsScene : rs.getRsScenes()) {
            fmt.add(rsScene.getName());
            fmt.addIndented(PrettyPrint.ppDetailed(rsScene, agent));
        }
        fmt.deindent();
        //
        // The recorded verb instances
        //
        fmt.add("VIs recorded from the execution");
        fmt.indent();
        for (VerbInstance vi : rs.getRecordedVis()) {
            fmt.add(XapiPrint.ppsViXapiForm(vi, agent));
        }
        fmt.deindent();
        return fmt.toString();
    }
}
