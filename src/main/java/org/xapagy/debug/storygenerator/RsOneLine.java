/*
   This file is part of the Xapagy project
   Created on: Apr 2, 2013
 
   org.xapagy.debug.storygenerator.RsOneLine
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug.storygenerator;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.LoopItem;
import org.xapagy.autobiography.ABStory;
import org.xapagy.instances.XapagyComponent;

/**
 * Captures the result of one line in a recorded story
 * 
 * @author Ladislau Boloni
 * 
 */
public class RsOneLine implements XapagyComponent {

    private ABStory abStory;
    private String identifier = null;
    private int lineNo;
    private List<LoopItem> loopItems;
    private RecordedStory recordedStory;

    /**
     * @param recordedStory
     * @param abStory
     * @param lineNo
     * @param loopItems
     */
    public RsOneLine(Agent agent, RecordedStory recordedStory, ABStory abStory,
            int lineNo, List<LoopItem> loopItems) {
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

    public List<LoopItem> getLoopItems() {
        return loopItems;
    }

    public RecordedStory getRecordedStory() {
        return recordedStory;
    }

}
