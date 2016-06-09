/*
   This file is part of the Xapagy project
   Created on: Nov 27, 2015
 
   org.xapagy.ui.observers.XapiObserver
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.observers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xapagy.agents.LoopItem;
import org.xapagy.debug.DebugEvent;
import org.xapagy.xapi.XapiFileLoader;

/**
 * An observer that executes a predefines Xapi file every time it is invoked
 * 
 * @author Ladislau Boloni
 *
 */
public class XapiObserver extends AbstractAgentObserver {

    private static final long serialVersionUID = -2974239551949046072L;
    private File xapiFile;
    
    public XapiObserver(File xapiFile) throws IOException {
        this.xapiFile = xapiFile;
    }
    
    
    /* (non-Javadoc)
     * @see org.xapagy.ui.observers.AbstractAgentObserver#observeInner(org.xapagy.debug.DebugEvent)
     */
    @Override
    public void observeInner(DebugEvent event)
            throws IOException, InterruptedException {
        List<LoopItem> loopItems = XapiFileLoader.loadFileToLoopItems(agent, xapiFile, null);
        for(LoopItem li: loopItems) {
            li.execute(false);
        }
    }

}
