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
package org.xapagy.debug.storygenerator;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.autobiography.ABStory;
import org.xapagy.instances.XapagyComponent;

/**
 * Captures the result of one line in a recorded story
 * 
 * @author Ladislau Boloni
 * Created on: Apr 2, 2013
 */
public class RsOneLine implements XapagyComponent {

    private ABStory abStory;
    private String identifier = null;
    private int lineNo;
    private List<AbstractLoopItem> loopItems;
    private RecordedStory recordedStory;

    /**
     * @param recordedStory
     * @param abStory
     * @param lineNo
     * @param loopItems
     */
    public RsOneLine(Agent agent, RecordedStory recordedStory, ABStory abStory,
            int lineNo, List<AbstractLoopItem> loopItems) {
        super();
        this.identifier =
                agent.getIdentifierGenerator().getRsOneLineIdentifier();
        this.recordedStory = recordedStory;
        this.abStory = abStory;
        this.lineNo = lineNo;
        this.loopItems = loopItems;
    }

    public ABStory getAbStory() {
        return abStory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.instances.XapagyComponent#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    public int getLineNo() {
        return lineNo;
    }

    public List<AbstractLoopItem> getLoopItems() {
        return loopItems;
    }

    public RecordedStory getRecordedStory() {
        return recordedStory;
    }

}
