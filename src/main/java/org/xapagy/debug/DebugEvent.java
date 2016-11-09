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
package org.xapagy.debug;

/**
 * @author Ladislau Boloni
 * Created on: Sep 23, 2010
 */
public class DebugEvent {
    /**
     * Constants to identify various events for the observers
     * 
     * <ul>
     * <li>AFTER_DA_CHUNK - every second, after the Execute.executeTimePassing -
     * after the (focus and shadow)* and hls maintenance is done
     * <li>BEFORE_DA_STEP - before the timeStep of focus and shadow...
     * <li>AFTER_CHOICE_SELECTED - in recall, after the choice was selected. At
     * this moment, the Loop.getInexecution() should return the current LoopItem
     * which is a recall item. Should be able to print it with
     * PrintWhat.currentLoopItem
     * 
     * </ul>
     * 
     */
    public enum DebugEventType {
        AFTER_DA_CHUNK, AFTER_INSTANCE_RESOLUTION, AFTER_LOOP_ITEM_EXECUTION,
        AFTER_RECALL, BEFORE_DA_STEP, BEFORE_LOOP_ITEM_EXECUTION,
        RESOLVE_SURPRISE
    }

    private DebugEventType eventType;
    private String fileName = null;
    private int lineNo = -1;
    private Object[] objects;

    /**
     * @param eventType
     */
    public DebugEvent(DebugEventType eventType) {
        super();
        this.eventType = eventType;
    }

    /**
     * @param eventType
     */
    public DebugEvent(DebugEventType eventType, String fileName, int lineNo) {
        this(eventType, fileName, lineNo, null, null);
    }

    /**
     * @param eventType
     */
    public DebugEvent(DebugEventType eventType, String fileName, int lineNo,
            Object... objects) {
        super();
        this.eventType = eventType;
        this.fileName = fileName;
        this.lineNo = lineNo;
        this.objects = objects;
    }

    public DebugEventType getEventType() {
        return eventType;
    }

    public Object[] getObjects() {
        return objects;
    }

    @Override
    public String toString() {
        StringBuffer buffer =
                new StringBuffer("DebugEvent (" + eventType + ")");
        if (fileName != null) {
            buffer.append(" at " + fileName + ":" + lineNo);
        }

        return buffer.toString();
    }
}
