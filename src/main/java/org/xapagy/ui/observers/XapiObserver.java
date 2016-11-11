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
package org.xapagy.ui.observers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.debug.DebugEvent;
import org.xapagy.xapi.XapiFileLoader;

/**
 * An observer that executes a predefines Xapi file every time it is invoked
 * 
 * @author Ladislau Boloni
 * Created on: Nov 27, 2015
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
        List<AbstractLoopItem> loopItems = XapiFileLoader.loadFileToLoopItems(agent, xapiFile, null);
        for(AbstractLoopItem li: loopItems) {
            li.execute(false);
        }
    }

}
