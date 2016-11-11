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
package org.xapagy.ui.prettyhtml;

/**
 * @author Ladislau Boloni
 * Created on: Mar 10, 2015
 */
public interface IQueryAttributes {
    public static final String Q_AGENT_NAME = "agentName";
    public static final String Q_AUTO_REFRESH = "autoRefresh";
    /**
     * The type of command which can be piggybacked on the query
     */
    public static final String Q_COMMAND_TYPE = "commandType";
    public static final String Q_CURSOR_FROM = "cursorFrom";
    public static final String Q_CURSOR_SIZE = "cursorSize";
    public static final String Q_CURSOR_TO = "cursorTo";
    public static final String Q_CURSOR_TOTAL = "cursorTotal";    
    public static final String Q_FILTER_VALUE = "filterValue";
    public static final String Q_FILTERED_BY = "filteredBy";
    /**
     * The identifier of the XapiComponent to which the specific query refers to
     */
    public static final String Q_ID = "id";
    /**
     * In cases where the query refers to two IDs (such as shadows, or links), the second ID
     */
    public static final String Q_SECOND_ID = "secondId";
    
    public static final String Q_QUERY_TYPE = "queryType";
    public static final String Q_RESULT_TYPE = "resultType";
    public static final String Q_SORTED_BY = "sortedBy";
    /**
     * When we are referring to a link, the link type
     */
    public static final String Q_LINK_TYPE = "linkType";
}
