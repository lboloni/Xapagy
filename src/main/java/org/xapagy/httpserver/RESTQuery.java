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
