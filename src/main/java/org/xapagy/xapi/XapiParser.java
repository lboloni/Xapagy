/*
   This file is part of the Xapagy project
   Created on: Aug 10, 2010
 
   org.xapagy.storyvisualizer.agent.XEPidginParser
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.parameters.Parameters;
import org.xapagy.questions.QuestionHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;
import org.xapagy.xapi.reference.XrefAdjective;
import org.xapagy.xapi.reference.XrefDirect;
import org.xapagy.xapi.reference.XrefGroup;
import org.xapagy.xapi.reference.XrefNewInstance;
import org.xapagy.xapi.reference.XrefNewRelational;
import org.xapagy.xapi.reference.XrefRelational;
import org.xapagy.xapi.reference.XrefStatement;
import org.xapagy.xapi.reference.XrefToInstance;
import org.xapagy.xapi.reference.XrefToVo;
import org.xapagy.xapi.reference.XrefVerb;
import org.xapagy.xapi.reference.XrefWait;
import org.xapagy.xapi.reference.XrefWhInstance;

/**
 * 
 * Implements the parser for the Xapi language
 * 
 * @author Ladislau Boloni
 * 
 */
public class XapiParser implements Serializable {

    public enum Article {
        A, THE
    }

    public static final String CO_LABEL_PREFIX = "#";

    public static String KEYWORD_IN = "in";
    public static double PAUSE_1_DASH = 1;
    public static double PAUSE_2_DASH = 10;
    public static double PAUSE_3_DASH = 100;
    public static double PAUSE_4_DASH = 1000;
    public static double PAUSE_COMMA = 0.0;
    public static double PAUSE_PERIOD = 1;
    public static double PAUSE_QUESTION_MARK = 1;
    public static double PAUSE_SEMICOLON = 0.2;
    private static final long serialVersionUID = 3810142546003499924L;
    private static final String specificationLevel = "5.0";
    public static final String VO_LABEL_PREFIX = "#";

    /**
     * Returns true if this statement is a comment
     * 
     * @return
     */
    public static boolean isAComment(String statement) {
        if (statement.equals("") || statement.startsWith("#")
                || statement.startsWith("//") || statement.startsWith("%")) {
            return true;
        }
        return false;
    }

    /**
     * @param statement
     * @return
     */
    public static boolean completeStatement(String statement) {
        String terminator = statement.substring(statement.length() - 1);
        if (".?!,;".contains(terminator)) {
            return true;
        }
        if (statement.equals("-") || statement.equals("--")
                || statement.equals("---") || statement.equals("----")) {
            return true;
        }
        return false;
    }

    /**
     * Identifies an article
     * 
     * @param next
     * @return
     */
    private static Article identifyArticle(String next1) {
        String next = next1.toLowerCase();
        if (next.equals("a") || next.equals("an")) {
            return Article.A;
        }
        if (next.equals("the")) {
            return Article.THE;
        }
        return null;
    }

    private Agent agent;

    /**
     * Keeps the previous inquit, if the previous statement was not a quote, it
     * is zero
     */
    private List<String> lastInquits = null;

    public XapiParser(Agent agent, boolean printVersionString) {
        if (printVersionString) {
            TextUi.println("Xapi - the Xapagy Pidgin language (specification level:"
                    + XapiParser.specificationLevel + ")");
        }
        this.agent = agent;
    }

    /**
     * @return the lastInquits
     */
    public List<String> getLastInquits() {
        return lastInquits;
    }

    /**
     * Interpret the time to wait
     * 
     * @param terminator
     * @return
     * @throws XapiParserException 
     */
    public double interpretTimeWait(String terminator) throws XapiParserException {
        Parameters p = agent.getParameters();
        double readingSpeed =
                p.get("A_GENERAL", "G_GENERAL", "N_READING_SPEED");
        if (terminator.equals(",")) {
            return readingSpeed * XapiParser.PAUSE_COMMA;
        }
        if (terminator.equals(";")) {
            return readingSpeed * XapiParser.PAUSE_SEMICOLON;
        }
        if (terminator.equals(".")) {
            return readingSpeed * XapiParser.PAUSE_PERIOD;
        }
        if (terminator.equals("?")) {
            return readingSpeed * XapiParser.PAUSE_QUESTION_MARK;
        }
        throw new XapiParserException("interpretTimeWait - unknown terminator: " + terminator);
    }

    /**
     * Resolves a phrase to a concept overlay (which will appear as an
     * adjective)
     * 
     * @param text
     * @param fromAgent
     * @return
     * @throws XapiParserException - if cannot resolve the text
     */
    private XrefAdjective parseAdjectivePhrase(String text) throws XapiParserException {
        try (Scanner scan = new Scanner(text)) {
            String current;
            String next = scan.next();
            List<String> wordList = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            while (true) {
                current = next;
                if (current.startsWith(XapiParser.CO_LABEL_PREFIX)) {
                    labels.add(current);
                } else {
                    if (!current.startsWith("\"")) {
                        current = current.toLowerCase();
                    }
                    wordList.add(current);
                }
                if (!scan.hasNext()) {
                    break;
                }
                next = scan.next();
            }
            scan.close();
            ConceptOverlay co =
                    WordResolutionHelper.resolveWordsToCo(agent, wordList);
            for (String label : labels) {
                co.addFullLabel(label, agent);
            }
            return new XrefAdjective(null, co, text,
                    XapiPositionInParent.ADJECTIVE);
        }
    }

    /**
     * Scans a CO from the current location in the scan. This is normally a
     * piece of a noun
     * 
     * Ends at the end of the scanner or at -- or +
     * 
     * Returns the parsed CO and the next word (or empty)
     * 
     * @param scan
     * @param next
     * @param agent
     * @return
     * @throws XapiParserException 
     */
    public SimpleEntry<ConceptOverlay, String> parseCo(Scanner scan,
            String initNext) throws XapiParserException {
        String next = initNext;
        // eat up a the article, if necessary
        String current;
        Article article = XapiParser.identifyArticle(next);
        if (article == null) {
            // nothing here
        } else {
            if (article == Article.THE) {
                next = scan.next();
            } else {
                throw new XapiParserException(
                        "parseCo: a/an is not acceptable at this point.");
            }
        }
        List<String> words = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        while (true) {
            current = next;
            if (current.startsWith(XapiParser.CO_LABEL_PREFIX)) {
                labels.add(current);
            } else {
                if (!current.startsWith("\"")) {
                    current = current.toLowerCase();
                }
                words.add(current);
            }
            if (!scan.hasNext()) {
                next = null;
                break;
            }
            next = scan.next();
            if (next.equals("+") || next.equals("--")) {
                break;
            }
        }
        //
        // at this moment we have the words - let us generate the CO.
        ConceptOverlay co = WordResolutionHelper.resolveWordsToCo(agent, words);
        for (String label : labels) {
            co.addFullLabel(label, agent);
        }
        return new SimpleEntry<>(co, next);
    }

    /**
     * Utility function - parses a string as if it would be a CO
     * 
     * @param text
     *            - not used by the parser, but by text programs
     * @return
     * @throws XapiParserException - if it cannot resolve the text
     */
    public ConceptOverlay parseCo(String text) throws XapiParserException {
        return parseAdjectivePhrase(text).getCo();
    }

    /**
     * Parses a group noun phrase into an XrefGroup.
     * 
     * In its current version, it only allows us to parse simple models (no
     * relational models, although the XrefArchitecture allows it)
     * 
     * @param scan
     * @param firstNext
     * @param agent
     * @param text
     * @return
     * @throws XapiParserException 
     */
    private XrefGroup parseGroupNounPhrase(Scanner scan, String firstNext,
            String text, XapiPositionInParent positionInParent) throws XapiParserException {
        List<ConceptOverlay> cos = new ArrayList<>();
        String next = firstNext;
        List<XrefToInstance> list = new ArrayList<>();
        while (true) {
            SimpleEntry<ConceptOverlay, String> entry = parseCo(scan, next);
            ConceptOverlay co = entry.getKey();
            XrefDirect xref =
                    new XrefDirect(null, XapiPositionInParent.GROUP_MEMBER, co,
                            next);
            list.add(xref);
            cos.add(co);
            if (entry.getValue() == null) {
                break;
            }
            if (!entry.getValue().equals("+")) {
                throw new XapiParserException("Expecting + here");
            }
            next = scan.next();
        }
        XrefGroup group = new XrefGroup(null, positionInParent, list, text);
        for (XrefToInstance xref : list) {
            xref.setParent(group);
        }
        return group;
    }

    /**
     * Parses a statement, which can be a line. 
     * 
     * One of the problems with this 
     * function is that if this is a Macro, it will be executed right here, but 
     * if it is a Xapi statement, it will be executed in the LoopItem
     * 
     * @param text
     * @throws XapiParserException
     */
    public XapiReference parseLine(String text) throws XapiParserException {
        String text2 = text.trim();
        // allow ' in the text as a replacement for "
        text2 = text.replaceAll("'", "\"");
        // check if it is a pause
        if (text2.startsWith("-")) {
            return parsePause(text2);
        }
        // the terminator should be usually . but other ones are acceptable as
        // well, eg. ?!
        String terminator = text2.substring(text2.length() - 1);
        if (!".?!,;".contains(terminator)) {
            throw new XapiParserException("Terminator is " + terminator
                    + " should only be .!?,;");
        }
        String text4 = text2.substring(0, text2.length() - 1);
        double timeWait = interpretTimeWait(terminator);
        XrefStatement statement = parseStatement(text4, timeWait);
        return statement;
    }

    /**
     * Resolves a noun phrase reference. The type of the reference might be new
     * instance, existing instance or a wh instance
     * 
     * @param nounPhrase
     * @param positionInParent
     *            - the position in the parent. It is not used here, but it is
     *            parsed later
     * @return
     * @throws XapiParserException 
     */
    public XrefToInstance parseNounPhrase(String nounPhrase,
            XapiPositionInParent positionInParent) throws XapiParserException {
        try (Scanner scan = new Scanner(nounPhrase)) {
            Article article = null;
            String next = scan.next();
            article = XapiParser.identifyArticle(next);
            String npWithoutArticle = null;
            if (article == null) {
                npWithoutArticle = nounPhrase;
                article = Article.THE;
            } else {
                npWithoutArticle = nounPhrase.substring(next.length() + 1);
                next = scan.next();
            }
            if (nounPhrase.contains("--")) {
                return parseRelationalNounPhrase(article, scan, next,
                        npWithoutArticle, positionInParent);
            }
            if (nounPhrase.contains("+")) {
                return parseGroupNounPhrase(scan, next, npWithoutArticle,
                        positionInParent);
            }

            ConceptOverlay co = parseCo(scan, next).getKey();

            // if it is a question, the article does not matter
            if (QuestionHelper.isWhConceptOverlay(co, agent)) {
                return new XrefWhInstance(null, positionInParent, co,
                        npWithoutArticle);
            }
            if (QuestionHelper.isWhatConceptOverlay(co, agent)) {
                return new XrefWhInstance(null, positionInParent, co,
                        npWithoutArticle);
            }

            switch (article) {
            case THE:
                return new XrefDirect(null, positionInParent, co,
                        npWithoutArticle);
            case A:
                return new XrefNewInstance(null, positionInParent, co,
                        npWithoutArticle);
            }
            throw new XapiParserException("resolveNounPhrase: one should never reach here");
        }
    }

    /**
     * Parsing a pause statement
     * 
     * @param text
     * @return
     */
    public XrefWait parsePause(String text) {
        double timeWait = XapiParser.PAUSE_1_DASH;
        if (text.startsWith("--")) {
            timeWait = XapiParser.PAUSE_2_DASH;
        }
        if (text.startsWith("---")) {
            timeWait = XapiParser.PAUSE_3_DASH;
        }
        if (text.startsWith("----")) {
            timeWait = XapiParser.PAUSE_4_DASH;
        }
        XrefWait statement = new XrefWait(timeWait, null);
        return statement;
    }

    /**
     * Parses a relational noun phrase
     * 
     * @param nounPhrase
     * @param agent
     * @return
     * @throws XapiParserException
     */
    private XrefToInstance parseRelationalNounPhrase(Article article,
            Scanner scan, String firstNext, String text,
            XapiPositionInParent positionInParent) throws XapiParserException {
        List<XrefToInstance> cos = new ArrayList<>();
        List<XrefToVo> vos = new ArrayList<>();
        String next = firstNext;
        SimpleEntry<ConceptOverlay, String> entry = parseCo(scan, next);
        ConceptOverlay co = entry.getKey();
        XrefDirect xref =
                new XrefDirect(null, XapiPositionInParent.RELATION_INSTANCE,
                        co, entry.getValue());
        if (!entry.getValue().equals("--")) {
            throw new XapiParserException("Expecting -- here");
        }
        next = scan.next();
        cos.add(xref);
        while (true) {
            SimpleEntry<VerbOverlay, String> entryvo = parseVo(scan, next);
            XrefVerb xrefvo =
                    new XrefVerb(null, entryvo.getKey(), entryvo.getValue(),
                            XapiPositionInParent.RELATION_RELATION);
            vos.add(xrefvo);
            if (!entryvo.getValue().equals("--")) {
                throw new XapiParserException("Expecting -- here");
            }
            next = scan.next();
            entry = parseCo(scan, next);
            co = entry.getKey();
            xref =
                    new XrefDirect(null,
                            XapiPositionInParent.RELATION_INSTANCE, co,
                            entry.getValue());
            cos.add(xref);
            if (entry.getValue() == null) {
                break;
            }
            next = scan.next();
        }
        XrefToInstance retval = null;
        switch (article) {
        case THE:
            retval = new XrefRelational(null, positionInParent, cos, vos, text);
            break;
        case A:
            retval =
                    new XrefNewRelational(null, positionInParent, cos, vos,
                            text);
            break;
        }
        for (XrefToInstance xrefinst : cos) {
            xrefinst.setParent(retval);
        }
        for (XrefToVo xrefvo : vos) {
            xrefvo.setParent(retval);
        }
        return retval;
    }

    /**
     * Parses a string and accepts it: SUBJECT / VERB / OBJECT or SUBJECT / VERB
     * 
     * @param text
     *            the text describing the statement
     * @param label
     *            - the label of the statement, which had been extracted before
     *            and it is passed here to be able to be put in the returned
     *            object
     * @param timeWait
     * @throws XapiParserException 
     * 
     */
    public XrefStatement parseSingleStatement(String text, String label,
            double timeWait) throws XapiParserException {
        //
        // Divide the remainder of the text into segments
        //
        ViType statementType = null;
        String subject = null;
        String object = null;
        String verb = null;
        try (Scanner scan = new Scanner(text)) {
            scan.useDelimiter("/");
            subject = scan.next();
            subject = subject.trim();
            verb = scan.next();
            verb = verb.trim();
            // if there is an "in" at the end of the verb
            int pos = verb.indexOf(" " + XapiParser.KEYWORD_IN + " ");
            if (pos != -1) {
                statementType = ViType.QUOTE;
                object = verb.substring(pos + 4, verb.length());
                verb = verb.substring(0, pos);
            }
            if (object == null && scan.hasNext()) {
                object = scan.next();
                object = object.trim();
            }
            scan.close();
        } catch (NoSuchElementException nsee) {
            throw new XapiParserException("Could not find expected component when parsing Xapagy statement:\n  "
                    + text);
        }
        //
        // Resolve the verb phrase
        //
        XrefVerb verbReference = parseVerbPhrase(verb);
        VerbOverlay verbs = verbReference.getVo();

        // if at this moment the type is not resolved,
        // resolve it based on the verb phrase
        if (statementType == null) {
            statementType =
                    ViTypeFromVoHelper.identifyViType(verbs, agent, object);
        }
        // correction for no object
        if (statementType == ViType.S_V_O && object == null) {
            statementType = ViType.S_V;
        }
        // correction for quote without scene specification
        if (statementType == ViType.QUOTE && object == null) {
            object = "scene";
        }

        //
        // End of resolving the type of the statement
        //
        switch (statementType) {
        case S_V_O: {
            XapiReference subjectReference =
                    parseNounPhrase(subject, XapiPositionInParent.SUBJECT);
            XapiReference objectReference =
                    parseNounPhrase(object, XapiPositionInParent.OBJECT);
            XrefStatement statement =
                    new XrefStatement(text, label, statementType,
                            subjectReference, verbReference, objectReference,
                            timeWait);
            subjectReference.setParent(statement);
            objectReference.setParent(statement);
            return statement;
        }
        case S_V: {
            XapiReference subjectReference =
                    parseNounPhrase(subject, XapiPositionInParent.SUBJECT);
            XrefStatement statement =
                    new XrefStatement(text, label, statementType,
                            subjectReference, verbReference, null, timeWait);
            subjectReference.setParent(statement);
            return statement;
        }
        case S_ADJ: {
            XrefToInstance subjectReference =
                    parseNounPhrase(subject, XapiPositionInParent.SUBJECT);
            XrefAdjective adjectiveReference = parseAdjectivePhrase(object);
            XrefStatement statement =
                    new XrefStatement(text, label, statementType,
                            subjectReference, verbReference,
                            adjectiveReference, timeWait);
            subjectReference.setParent(statement);
            adjectiveReference.setParent(statement);
            return statement;
        }
        case QUOTE: {
            XapiReference subjectReference =
                    parseNounPhrase(subject, XapiPositionInParent.SUBJECT);
            XapiReference quoteSceneReference =
                    parseNounPhrase(object, XapiPositionInParent.QUOTE);
            XrefStatement statement =
                    new XrefStatement(text, label, statementType,
                            subjectReference, verbReference,
                            quoteSceneReference, timeWait);
            subjectReference.setParent(statement);
            quoteSceneReference.setParent(statement);
            return statement;
        }
        default: {
            throw new Error("Unknown statement type: " + statementType);
        }
        }
    }

    /**
     * Parses a potentially quoted statement. Chops it down, parses the
     * statements independently and sets up the statement with quote links
     * 
     * @param text
     *            - the text
     * @param timeWait
     *            - the time to be waited after the statement, passed down
     * 
     * @return
     * @throws XapiParserException 
     */
    public XrefStatement parseStatement(String originalText, double timeWait) throws XapiParserException {
        String text = originalText;
        // Parse the label
        String label = null;
        if (text.startsWith("(")) {
            int closingPar = text.indexOf(")");
            label = text.substring(1, closingPar);
            text = text.substring(closingPar + 1).trim();
        }
        // reads statements as long as they are indirect statements.
        List<String> inquits = new ArrayList<>();
        XrefStatement retval = null;
        try (Scanner scan = new Scanner(text)) {
            scan.useDelimiter("//");
            String statementText = scan.next();
            retval = parseSingleStatement(statementText, label, timeWait);
            XrefStatement current = retval;
            while (current.getViType() == ViType.QUOTE) {
                inquits.add(statementText);
                statementText = scan.next();
                XrefStatement quoteStatement =
                        parseSingleStatement(statementText, null, 0.0);
                current.setQuote(quoteStatement);
                current = quoteStatement;
            }
            if (!inquits.isEmpty()) {
                lastInquits = inquits;
            }
            scan.close();
        }
        return retval;
    }

    /**
     * 
     * Creates a Xapi reference for a verb phrase
     * @throws XapiParserException - if the words cannot be resolved
     * 
     */
    public XrefVerb parseVerbPhrase(String text) throws XapiParserException {
        try (Scanner scan = new Scanner(text)) {
            String next = scan.next();
            VerbOverlay verbs = parseVo(scan, next).getKey();
            return new XrefVerb(null, verbs, text, XapiPositionInParent.VERB);
        }
    }

    /**
     * Parses a VO from the current location and first next string
     * 
     * @param scan
     * @param originalNext
     *            - the next item in the parse
     * @return
     * @throws XapiParserException - if the words cannot be resolved
     */
    public SimpleEntry<VerbOverlay, String> parseVo(Scanner scan,
            String originalNext) throws XapiParserException {
        String next = originalNext;
        List<String> words = new ArrayList<>();
        String current;
        while (true) {
            current = next;
            words.add(current);
            if (!scan.hasNext()) {
                break;
            }
            next = scan.next();
            if (next.equals("+") || next.equals("--")) {
                break;
            }
        }
        VerbOverlay verbs = WordResolutionHelper.resolveWordsToVo(agent, words);
        return new SimpleEntry<>(verbs, next);
    }

    /**
     * Utility function - parses a string as if it would be a CO
     * 
     * @param text
     *            - not used by the parser, but by text programs
     * @return
     * @throws XapiParserException - if the words cannot be resolved 
     */
    public VerbOverlay parseVo(String text) throws XapiParserException {
        return parseVerbPhrase(text).getVo();
    }

}
