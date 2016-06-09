/*
   This file is part of the Xapagy project
   Created on: Mar 10, 2015
 
   org.xapagy.httpserver.CursorHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyhtml;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;

/**
 * 
 * 
 * @author Ladislau Boloni
 *
 */
public class QueryHelper implements IQueryAttributes {
    
    /**
     * Creates a query with a copy with an empty command
     * 
     * FIXME: this is a bit too much used --> it is good for cursor
     * 
     * @param gq
     * @return
     */
    public static RESTQuery copyWithEmptyCommand(RESTQuery gq) {
        RESTQuery other = new RESTQuery(gq);
        other.setAttribute(Q_COMMAND_TYPE, "NOTHING");
        return other;
    }

    
    /**
     * Creates a new query, setting the agent, the query type and id
     * @param gq
     * @param agent
     * @param queryType
     * @param id
     * @return
     */
    public static RESTQuery newQuery(Agent agent, String queryType, String id) {
        RESTQuery other = new RESTQuery();
        other.setAttribute(Q_COMMAND_TYPE, "NOTHING");
        other.setAttribute(Q_AGENT_NAME, agent.getName());
        other.setAttribute(Q_QUERY_TYPE, queryType);
        other.setAttribute(Q_RESULT_TYPE, "HTML");
        other.setAttribute(Q_ID, id);
        return other;
    }

    
    /**
     * Creates the cursor neighbors - the previous and the other one, returned
     * as a [previous, next] pair
     * 
     * If we are at the beginning, and can't move backward, the previous will be
     * null.
     * 
     * If we are at the end, and can't move forward, the next will be null
     * 
     * @param gq
     * @return
     */
    public static SimpleEntry<RESTQuery, RESTQuery>
            createCursorNeighbors(RESTQuery gq) {
        RESTQuery gqPrevious = null;
        RESTQuery gqNext = null;
        //
        // create the previous cursor
        //
        int cursorFrom = Integer.parseInt(gq.getAttribute(Q_CURSOR_FROM));
        int cursorSize = Integer.parseInt(gq.getAttribute(Q_CURSOR_SIZE));
        int cursorTotal = Integer.parseInt(gq.getAttribute(Q_CURSOR_TOTAL));
        int cursorTo = Integer.parseInt(gq.getAttribute(Q_CURSOR_TO));
        if (cursorFrom > 0) {
            gqPrevious = copyWithEmptyCommand(gq);
            int previousCursorFrom = cursorFrom - cursorSize;
            if (previousCursorFrom < 0) {
                previousCursorFrom = 0;
            }
            int previousCursorTo = previousCursorFrom + cursorSize;
            if (previousCursorTo > cursorTotal) {
                previousCursorTo = cursorTotal;
            }
            gqPrevious.setAttribute(Q_CURSOR_FROM, ""+previousCursorFrom);
            gqPrevious.setAttribute(Q_CURSOR_TO, "" + previousCursorTo);
        }
        //
        // create the current version
        //
        //
        // link to the next cursor
        //
        if (cursorTo < cursorTotal) {
            gqNext = copyWithEmptyCommand(gq);
            int nextCursorTo = cursorTo + cursorSize;
            if (nextCursorTo > cursorTotal) {
                nextCursorTo = cursorTotal;
            }
            int nextCursorFrom = nextCursorTo - cursorSize;
            if (nextCursorFrom < 0) {
                nextCursorFrom = 0;
            }
            gqNext.setAttribute(Q_CURSOR_FROM, "" + nextCursorFrom);
            gqNext.setAttribute(Q_CURSOR_TO,"" + nextCursorTo);
        }

        return new SimpleEntry<>(gqPrevious, gqNext);
    }
}
