/*
   This file is part of the Xapagy project
   Created on: Feb 28, 2012
 
   org.xapagy.xapi.XapiL2Parser
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.LoopItem;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.language.parser.ParseException;
import org.xapagy.xapi.language.parser.XapiLang;

/**
 * Parses macro expansions from Xapi
 * 
 * @author Ladislau Boloni
 * 
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
            LoopItem loopItem = agent.getLoop().getInExecution();
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
            LoopItem loopItem = agent.getLoop().getInExecution();
            if (loopItem != null) {
                description += "While executing loop item: " + loopItem.getFileName() + ":" + loopItem.getFileLineNo() + "\n";
            }
            description+= "Exception at line:\n" + macroStatement + "\n";
            description+= e.toString();
            TextUi.println(description);
            throw new Error("Exception while performing command, cannot continue");            
        } /* catch(Error e) {
            String description = "";
            LoopItem loopItem = agent.getLoop().getInExecution();
            if (loopItem != null) {
                description += "While executing loop item: " + loopItem.getFileName() + ":" + loopItem.getFileLineNo() + "\n";
            }
            description+= "Exception at line:\n" + macroStatement + "\n";
            description+= e.getMessage();
            TextUi.println(description);
            throw new Error("Exception while performing command, cannot continue");            
        } */
    }    
    
    

}
