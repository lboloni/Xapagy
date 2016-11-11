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
package org.xapagy.xapi;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liXapiReading;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.language.parser.ParseException;
import org.xapagy.xapi.language.parser.XapiLang;

/**
 * Parses macro expansions from Xapi
 * 
 * @author Ladislau Boloni
 * Created on: Feb 28, 2012
 */
public class MacroParser {

    /**
     * 
     */
    private static final String REPEAT_INQUIT = "$..//";


    /**
     * Simplifying statement: repeat the inquit
     */
    private static final void
            macroRepeatInquit(String l2statement, Agent agent) {
        List<String> list = new ArrayList<>();
        String quote = l2statement.substring(REPEAT_INQUIT.length());
        String tmp = "";
        for (String inq : agent.getXapiParser().getLastInquits()) {
            tmp = tmp + inq + " // ";
        }
        tmp = tmp + quote;
        list.add(tmp);
        TextUi.println("Expanded repeat inquit:\n" + tmp);
        agent.getLoop().addImmediateReading(list);
    }

    
    /**
     * Executes a macro expression
     * 
     * @param macroStatement
     * @param agent
     * @throws Exception 
     */
    public static final void executeMacro(String macroStatement, Agent agent) {
        // a special macro...
        if (macroStatement.startsWith(REPEAT_INQUIT)) {
            MacroParser.macroRepeatInquit(macroStatement, agent);
            return;
        }
        // TextUi.println("Executing: " + macroStatement);
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream((macroStatement).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // this should not normally happen
            e.printStackTrace();
        }
        XapiLang xl = new XapiLang(bais);
        xl.setAgent(agent);
        try {
            xl.line();
        } catch (ParseException e) {
            // Pretty write of the current error
            String description = "";
            liXapiReading loopItem = (liXapiReading) agent.getLoop().getInExecution();
            if (loopItem != null) {
                description += "While executing loop item: " + loopItem.getFileName() + ":" + loopItem.getFileLineNo() + "\n";
            }
            description+= "Parse exception at line:\n" + macroStatement + "\n";
            for(int i = 0; i != e.currentToken.beginColumn-1; i++) {
                description += " ";
            }
            for(int i = 0; i != e.currentToken.image.length(); i++) {
                description += "^";
            }
            description += "\n" + e.getMessage();
            TextUi.println(description);
            throw new Error("Parse exception, cannot continue");
        } catch(Exception e) {
            String description = "";
            liXapiReading loopItem = (liXapiReading)agent.getLoop().getInExecution();
            if (loopItem != null) {
                description += "While executing loop item: " + loopItem.getFileName() + ":" + loopItem.getFileLineNo() + "\n";
            }
            description+= "Exception at line:\n" + macroStatement + "\n";
            description+= e.toString();
            TextUi.println(description);
            throw new Error("Exception while performing command, cannot continue");            
        } 
    }    
    
    

}
