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
package org.xapagy.httpserver;

import java.util.HashMap;
import java.util.Scanner;

import org.xapagy.ui.prettyprint.Formatter;

/**
 * 
 * @author Lotzi Boloni
 * 
 */
public class RESTQuery {
    
    /**
     * The storage for all the attributes (as String name / value pairs)
     */
    private HashMap<String, String> attributes = new HashMap<>();

    public HashMap<String, String> getAttributes() {
        return attributes;
    }
    
    public String getAttribute(String attribute) {
        return attributes.get(attribute);
    }

    public String setAttribute(String attribute, String value) {
        return attributes.put(attribute, value);
    }
    
    


    /**
     * Parses a query string as arriving from a web server
     * 
     * It is the format ?attr=name&...
     * 
     * The recognized name / attribute pairs are
     * 
     * @param query
     *            - the query string arriving from the client
     * @return the newly created GsQuery object
     * 
     */
    public static RESTQuery parseQueryString(String query) {
        if (!query.startsWith("?")) {
            throw new Error("Query should start with ?:" + query);
        }
        RESTQuery gsQuery = new RESTQuery();
        String temp = query.substring(1);
        try (Scanner scanner = new Scanner(temp)) {
            scanner.useDelimiter("&");
            while (scanner.hasNext()) {
                String item = scanner.next();
                int eqloc = item.indexOf("=");
                if (eqloc == -1) {
                    throw new Error("invalid item:" + item);
                }
                String name = item.substring(0, eqloc);
                String value = item.substring(eqloc + 1, item.length());
                gsQuery.attributes.put(name, value);
            }
            scanner.close();
        }
        return gsQuery;
    }


    public RESTQuery() {
    }

    /**
     * Copy constructor Resets the command-type to NOTHING
     * 
     * @param agent
     */
    public RESTQuery(RESTQuery other) {
        this.attributes = new HashMap<>(other.attributes);
    }

    /**
     * Formats the GsQuery back to the format of the query
     * 
     * @return
     */
    public String toQuery() {
        StringBuffer buf = new StringBuffer("?");
        for (String attribute : attributes.keySet()) {
            String value = attributes.get(attribute);
            buf.append(attribute + "=" + value + "&");
        }
        return buf.toString();
    }

    /**
     * Formats the query for human reading
     */
    @Override
    public String toString() {
        Formatter fmt = new Formatter();
        fmt.add("GsQuery");
        fmt.indent();
        for (String attribute : attributes.keySet()) {
            String value = attributes.get(attribute);
            fmt.is(attribute, value);
        }
        return fmt.toString();
    }

}
