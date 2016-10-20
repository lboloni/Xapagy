/*
   This file is part of the Xapagy project
   Created on: Jun 25, 2014
 
   org.xapagy.debug.ViMatch
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;

/**
 * This class is used for situations in debugging when we need to look up a
 * specific VI based on matches
 * 
 * @author Ladislau Boloni
 * 
 */
public class ViMatch implements Serializable {

    private static final long serialVersionUID = -2573569136620080201L;
    /**
     * Prefix denoting that we are looking for a missing part
     */
    private static final String MATCH_MISSING = "MISSING";
    /**
     * Prefix denoting that we are looking for a "new" part. The remainder of
     * the string is interpreted as the attributes
     */
    private static final String MATCH_NEW = "NEW:";
    /**
     * Prefix denoting that we are accepting any value (including missing or
     * new)
     */
    public static final String MATCH_ANY = "*";
    /**
     * Most of these matches require the use of the parser, so we keep a link to
     * it
     */
    private XapiParser xp;

    /**
     * Constructor. The agent is passed to get the parser.
     * 
     * @param agent
     */
    public ViMatch(Agent agent) {
        xp = agent.getXapiParser();
    }

    /**
     * Selects a set of VIs from a given list which matches the specified
     * parameters
     * 
     * @param list
     * @param type
     * @param params
     * @return
     */
    public List<VerbInstance> select(List<VerbInstance> list, ViType type,
            String... params) {
        List<VerbInstance> retval = new ArrayList<>();
        for (VerbInstance vi : list) {
            if (match(vi, type, params)) {
                retval.add(vi);
            }
        }
        return retval;
    }

    /**
     * Selects a set of VIs from a given list which matches the passed
     * ViMatchFilter
     * 
     * @param list
     * @param type
     * @param params
     * @return
     */
    public List<VerbInstance> select(List<VerbInstance> list,
            ViMatchFilter vmf) {
        List<VerbInstance> retval = new ArrayList<>();
        for (VerbInstance vi : list) {
            if (match(vi, vmf)) {
                retval.add(vi);
            }
        }
        return retval;
    }

    /**
     * Matching a vi based on the ViMatchFilter
     * 
     * @param vi
     * @param vmf
     * @return
     * @throws XapiParserException
     */
    public boolean match(VerbInstance vi, ViMatchFilter vmf) {
        return match(vi, vmf.getSceneAttributes(), vmf.getType(),
                vmf.getParams());
    }

    /**
     * Matching a BI based on the type and the specified params - it assumes
     * that we are not dealing
     * 
     * @param vi
     * @param type
     * @param params
     * @return
     * @throws XapiParserException
     */
    public boolean match(VerbInstance vi, ViType type, String... params) {
        return match(vi, null, type, params);
    }

    /**
     * Matching a VI based on the type and specific params. This function only
     * spans things out
     * 
     * @param vi
     * @param type
     * @param params
     * @return
     * @throws XapiParserException
     */
    public boolean match(VerbInstance vi, String sceneAttributes, ViType type,
            String... params) {
        try {
            if (type != null) {
                if (!vi.getViType().equals(type)) {
                    return false;
                }
            }
            if (!matchScene(vi, sceneAttributes)) {
                return false;
            }
            switch (vi.getViType()) {
            case S_V_O:
                return matchSVO(vi, params);
            case S_V:
                return matchSV(vi, params);
            case S_ADJ:
                return matchSAdj(vi, params);
            case QUOTE:
                return matchQuote(vi, params);
            }
        } catch (XapiParserException e) {
            e.printStackTrace();
            TextUi.abort("Should not happen here " + e.toString());
        }
        // should not get here
        return false;
    }

    /**
     * Checks whether the _subject_ of the VI is in the specified scene
     * 
     * @param vi
     * @param vi
     * @return
     */
    private boolean matchScene(VerbInstance vi, String sceneAttributes) {
        if (sceneAttributes == null) {
            return true;
        }
        ConceptOverlay co = null;
        try {
            co = xp.parseCo(sceneAttributes);
        } catch (XapiParserException e) {
            e.printStackTrace();
            TextUi.abort("Should not happen here " + e.toString());
        }
        Instance instSubject = vi.getSubject();
        Instance scene = instSubject.getScene();
        if (!scene.getConcepts().coversWithLabels(co)) {
            return false;
        }
        return true;
    }

    /**
     * Matching for a Quote type VI
     * 
     * @param vi
     * @param params
     * @return
     * @throws XapiParserException
     */
    private boolean matchQuote(VerbInstance vi, String[] params)
            throws XapiParserException {
        // if it is not the right one...
        if (vi.getViType() != ViType.QUOTE) {
            return false;
        }
        // if there is no parameter, we assume that we have matching
        if (params.length == 0) {
            TextUi.println(
                    "ViMatch.matchQuote without any parameter! returning true");
            return true;
        }
        // one parameter: assume it is the verb
        if (params.length == 1) {
            return matchesVerb(params[0], vi);
        }
        // two parameters: don't use
        if (params.length == 2) {
            return false;
        }
        // three parameters or more: use the first three to match the inquit and
        // pass
        // the remainder ones to the quote
        if (params.length >= 3) {
            boolean inquitMatch =
                    matchesSubject(params[0], vi) && matchesVerb(params[1], vi)
                            && matchesQuoteScene(params[2], vi);
            if (!inquitMatch) {
                return false;
            }
            // copy the remaining parameters
            String[] newParams = new String[params.length - 3];
            for (int i = 0; i != newParams.length; i++) {
                newParams[i] = params[3];
            }
            return match(vi.getQuote(), null, newParams);
        }
        return false;
    }

    /**
     * Matching for an S_Adj type VI
     * 
     * @param vi
     * @param params
     * @return
     */
    private boolean matchSAdj(VerbInstance vi, String[] params) {
        // if it is not the right one...
        if (vi.getViType() != ViType.S_ADJ) {
            return false;
        }
        // if there is no parameter, we assume that we have matching
        if (params.length == 0) {
            TextUi.println(
                    "ViMatch.matchSAdj without any parameter! returning true");
            return true;
        }
        // one parameter: assume it is the verb
        if (params.length == 1) {
            return matchesVerb(params[0], vi);
        }
        // two parameters: don't use
        if (params.length == 2) {
            TextUi.println("matchSAdj with two parameters, don't use!!!");
            return false;
        }
        // three parameters
        if (params.length == 3) {
            return matchesSubject(params[0], vi) && matchesVerb(params[1], vi)
                    && matchesAdjective(params[2], vi);
        }
        TextUi.println("matchSAdj with more than 3 parameters, don't use!!!");
        return false;
    }

    /**
     * Matching for an SV type VI
     * 
     * @param vi
     * @param params
     * @return
     */
    private boolean matchSV(VerbInstance vi, String[] params) {
        // if it is not the right one...
        if (vi.getViType() != ViType.S_V) {
            return false;
        }
        // if there is no parameter, we assume that we have matching
        if (params.length == 0) {
            TextUi.println(
                    "ViMatch.matchSV without any parameter! returning true");
            return true;
        }
        // one parameter: assume it is the verb
        if (params.length == 1) {
            return matchesVerb(params[0], vi);
        }
        // two parameters
        if (params.length == 2) {
            return matchesSubject(params[0], vi) && matchesVerb(params[1], vi);
        }
        TextUi.println("matchSV with more than 2 parameters, don't use!!!");
        return false;
    }

    /**
     * Matching for an SVO type VI
     * 
     * @param vi
     * @param params
     * @return
     * @throws XapiParserException
     */
    public boolean matchSVO(VerbInstance vi, String[] params)
            throws XapiParserException {
        // if it is not the right one...
        if (vi.getViType() != ViType.S_V_O) {
            return false;
        }
        // if there is no parameter, we assume that we have matching
        if (params.length == 0) {
            TextUi.println(
                    "ViMatch.matchSVO without any parameter! returning true");
            return true;
        }
        // one parameter: assume it is the verb
        if (params.length == 1) {
            return matchesVerb(params[0], vi);
        }
        // two parameters: don't use
        if (params.length == 2) {
            TextUi.println("matchSVO with two parameters, don't use!!!");
            return false;
        }
        // three parameters
        if (params.length == 3) {
            return matchesSubject(params[0], vi) && matchesVerb(params[1], vi)
                    && matchesObject(params[2], vi);
        }
        TextUi.println("matchSVO with more than 3 parameters, don't use!!!");
        return false;
    }

    /**
     * Helper function for matching the verb of a VI
     * 
     * @param text
     * @param vi
     * @return
     */
    private boolean matchesVerb(String text, VerbInstance vi) {
        if (text.equals(MATCH_ANY)) {
            return true;
        }
        VerbOverlay vo = null;
        try {
            vo = xp.parseVo(text);
        } catch (XapiParserException e) {
            e.printStackTrace();
            TextUi.abort(e.toString() + " should not happen here");
        }
        if (!vi.getVerbs().coversWithLabels(vo)) {
            return false;
        }
        return true;
    }

    /**
     * Helper function for matching a part of the VI which is an instance
     * 
     * @param xapiText
     * @param vi
     * @return
     * @throws XapiParserException
     *             - if the matchString cannot be parsed
     */
    private boolean matchesInstancePart(String matchString, VerbInstance vi,
            ViPart part) {
        try {
            String text = matchString;
            if (text.equals(MATCH_ANY)) {
                return true;
            }
            if (text.equals(MATCH_MISSING)) {
                if (vi.getMissingParts().contains(part)) {
                    return true;
                } else {
                    return false;
                }
            }
            // if this matches, it is a new part
            if (text.startsWith(MATCH_NEW)) {
                ConceptOverlay coNew = vi.getNewParts().get(part);
                if (coNew == null) { // it is not a new part
                    return false;
                }
                text = text.substring(MATCH_NEW.length()).trim();
                // if there is nothing here, accept anything
                if (text.isEmpty()) {
                    return true;
                }
                ConceptOverlay co = xp.parseCo(text);
                if (!coNew.coversWithLabels(co)) {
                    return false;
                }
                return true;
            }
            // if we are here, it means that we are not looking for a template
            // with
            // a missing part
            ConceptOverlay co = xp.parseCo(text);
            Object object = vi.getResolvedParts().get(part);
            if (object == null) { // missing or new part
                return false;
            }
            Instance instance = (Instance) object;
            if (!instance.getConcepts().coversWithLabels(co)) {
                return false;
            }
            return true;
        } catch (XapiParserException e) {
            e.printStackTrace();
            TextUi.abort("Should not happen here " + e.toString());
        }
        return false;
    }

    /**
     * Helper function for matching the subject of a VI
     * 
     * @param text
     * @param vi
     * @return
     * @throws XapiParserException
     */
    private boolean matchesSubject(String text, VerbInstance vi) {
        return matchesInstancePart(text, vi, ViPart.Subject);
    }

    /**
     * Helper function for matching the object of a VI
     * 
     * @param text
     * @param vi
     * @return
     * @throws XapiParserException
     */
    private boolean matchesObject(String text, VerbInstance vi) {
        return matchesInstancePart(text, vi, ViPart.Object);
    }

    /**
     * Helper function for matching the adjective of a VI
     * 
     * @param text
     * @param vi
     * @return
     * @throws XapiParserException
     */
    private boolean matchesAdjective(String text, VerbInstance vi) {
        if (text.equals(MATCH_ANY)) {
            return true;
        }
        ConceptOverlay co = null;
        try {
            co = xp.parseCo(text);
        } catch (XapiParserException e) {
            e.printStackTrace();
            TextUi.abort("Should not happen here " + e.toString());
        }
        if (!vi.getAdjective().coversWithLabels(co)) {
            return false;
        }
        return true;
    }

    /**
     * Helper function for matching a quote scene
     * 
     * @param string
     * @param vi
     * @return
     */
    private boolean matchesQuoteScene(String text, VerbInstance vi) {
        return matchesInstancePart(text, vi, ViPart.QuoteScene);
    }

}
