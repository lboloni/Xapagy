/*
   This file is part of the Xapagy project
   Created on: Sep 23, 2010
 
   org.xapagy.debug.DebugEvent
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.debug;

/**
 * @author Ladislau Boloni
 * 
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
