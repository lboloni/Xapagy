package org.xapagy.ui.queryhandlers;

import org.xapagy.activity.hlsmaintenance.CharacterizationScore;
import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.ChoiceTypeHelper;
import org.xapagy.headless_shadows.HlsCharacterization;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwVerbInstance;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyprint.PpHlsCharacterization;

public class qh_CHOICE implements IQueryHandler, IQueryAttributes {

    
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        Choice choice =
                agent.getHeadlessComponents().getChoices().get(identifier);
        if (choice == null) {
            fmt.addPre("Choice with the id = " + identifier + " not found");
            return;
        }
        String redheader = "Choice" + fmt.getEmpty().addIdentifier(choice);
        fmt.addH2(redheader, "class=identifier");
        qh_CHOICE.pwDetailed(fmt, agent, choice, query);
    }

    
    
    /**
     * For those choice types which do rely on a Hls object, adds the Hls in the
     * text. It is extracted here because it might be selective later.
     * 
     * Currently it is a link and a full embedded Hls
     * 
     * @param fmt
     * @param agent
     * @param choice
     * @param query
     */
    private static void addHls(IXwFormatter fmt, Agent agent, Choice choice,
            RESTQuery query) {
        fmt.openP();
        fmt.add("Hls: ");
        PwQueryLinks.linkToHls(fmt, agent, query, choice.getHls());
        fmt.startEmbedX("Hls");
        qh_HLS_SUPPORTED.pwDetailed(fmt, choice.getHls(), agent, query, false);
        fmt.endEmbedX();
        fmt.closeP();
        // PwHls.pwConcise(choice.getHls(), agent);

    }

    
    /**
     * For those choice types which do rely on a StaticHls object, adds the Hls in the
     * text. It is extracted here because it might be selective later.
     * 
     * Currently it is a link and a full embedded Hls
     * 
     * @param fmt
     * @param agent
     * @param choice
     * @param query
     */
    private static void addStaticHls(IXwFormatter fmt, Agent agent, Choice choice,
            RESTQuery query) {
        fmt.openP();
        fmt.add("StaticHls: ");
        PwQueryLinks.linkToStaticHls(fmt, agent, query, choice.getStaticHls());
        fmt.startEmbedX("StaticHls");
        qh_STATIC_HLS.xwStaticHLS(choice.getStaticHls(), agent, query, fmt);
        fmt.endEmbedX();
        fmt.closeP();
        // PwHls.pwConcise(choice.getHls(), agent);

    }
    

    /**
     * This is the printing which happens for the Choice link and used in
     * PwHelper
     * 
     * FIXME: this uses verbalization, make sure the instantiation does not
     * affect things!!!
     * 
     * @param choice
     * @param agent
     * @return
     */
    public static String pwConcise(Choice choice, Agent agent) {
        PwFormatter fmt = new PwFormatter();
        String viText = null;
        String statusText = null;
        switch (choice.getChoiceStatus()) {
        case NOT_CHOSEN: {
            statusText = "NotChosen";
            if (ChoiceTypeHelper.isHlsBased(choice)) {
                viText =
                       xwVerbInstance.xwConcise(new TwFormatter(), choice.getHls()
                                .getViTemplate(), agent);
            }
            if (ChoiceTypeHelper.isStatic(choice)) {
                viText =
                        xwVerbInstance.xwConcise(new TwFormatter(), choice.getStaticHls()
                                .getViTemplate(), agent);
            }
            if (ChoiceTypeHelper.isCharacterization(choice)) {
                PpHlsCharacterization.ppDetailed(
                        choice.getHlsCharacterization(), agent);
            }
            break;
        }
        case PARTIALLY_INSTANTIATED: {
            statusText = "Partial";
            viText = "vi... ongoing instantiation....";
            break;
        }
        case FULLY_INSTANTIATED: {
            statusText = "Instantiated";
            viText = "";
            for (VerbInstance vi : choice.getInstantiatedVis()) {
                viText += agent.getVerbalize().verbalize(vi) + "; ";
            }
            break;
        }
        }
        switch (choice.getChoiceType()) {
        case CONTINUATION: {
            fmt.addEnum("[Contin:" + statusText + "] ");
            fmt.add(viText);
            break;
        }
        case MISSING_ACTION: {
            fmt.addEnum("[Miss-Act:" + statusText + "] ");
            fmt.add(viText);
            break;
        }
        case MISSING_RELATION: {
            fmt.addEnum("[Miss-Rel:" + statusText + "] ");
            fmt.add(viText);
            break;
        }
        case CHARACTERIZATION: {
            fmt.addEnum("[Charact:" + statusText + "] ");
            fmt.add(viText);
            break;
        }
        case STATIC: {
            fmt.addEnum("[Static:" + statusText + "] ");
            fmt.add(viText);
            break;
        }
        default:
            return "cannot print choice of type " + choice.getChoiceType();
        }
        fmt.addIdentifier(choice);
        return fmt.toString();
    }

    /**
     * Detailed printing of a choice in a format which would allow the embedding
     * in other webpages
     * 
     * @param fmt
     * @param agent
     * @param choice
     * @param query
     * @return
     */
    public static String pwDetailed(IXwFormatter fmt, Agent agent,
            Choice choice, RESTQuery query) {
        fmt.is("Choice type:", choice.getChoiceType());
        fmt.is("Status", choice.getChoiceStatus());
        //
        // Print the template from the HLS
        //
        if (ChoiceTypeHelper.isHlsBased(choice)) {
            String explanation =
                    "The VI template of the associated HLS - this is the VI which will be created if this choice is instantiated.";
            qh_HLS_SUPPORTED.pwHlsViTemplate(fmt, choice.getHls(), agent, query,
                    explanation);
        }
        if (ChoiceTypeHelper.isStatic(choice)) {
            String explanation =
                    "The VI template of the associated StaticHLS - this is the VI which will be created if this choice is instantiated.";
            qh_HLS_SUPPORTED.pwHlsViTemplate(fmt, choice.getStaticHls(), agent, query,
                    explanation);
        }
        
        fmt.addH2("ChoiceScore - explaining how the various scores were reached");
        fmt.startEmbedX("ChoiceScore");
        qh_CHOICE_SCORE.pwDetailed(fmt, choice.getChoiceScore(), agent, query);
        fmt.endEmbedX();

        switch (choice.getChoiceStatus()) {
        case NOT_CHOSEN:
            break;
        case FULLY_INSTANTIATED: {
            fmt.explanatoryNote("VIs resulting from the execution");
            for (VerbInstance vi : choice.getInstantiatedVis()) {
                fmt.openP();
                PwQueryLinks.linkToVi(fmt, agent, query, vi);
                fmt.closeP();
            }
            break;
        }
        case PARTIALLY_INSTANTIATED: {
            fmt.explanatoryNote("FIXME: show here where are we in the instantiation of dependencies!!!");
            break;
        }
        } //
          // the structure
          //
        fmt.addH2("Structure for choice type: " + choice.getChoiceType());
        switch (choice.getChoiceType()) {
        case CONTINUATION:
            qh_CHOICE.structureContinuation(fmt, agent, choice, query);
            break;
        case CHARACTERIZATION:
            qh_CHOICE.structureCharacterization(fmt, agent, choice, query);
            break;
        case MISSING_ACTION:
            qh_CHOICE.structureMissingAction(fmt, agent, choice, query);
            break;
        case MISSING_RELATION:
            qh_CHOICE.structureMissingRelation(fmt, agent, choice, query);
            break;
        case STATIC:
            qh_CHOICE.structureStatic(fmt, agent, choice, query);
            break;
        }
        return fmt.toString();
    }

    /**
     * Generates a list of the other HlsSupported's which are side links to this
     * one.
     * 
     * @param fmt
     * @param hlss
     * @param agent
     * @param query
     */
    public static void pwSideLinks(PwFormatter fmt, Choice choice, Agent agent,
            RESTQuery query) {
        // HeadlessComponents hlc = agent.getHeadlessComponents();
        fmt.explanatoryNote("Side links to other HlsSupported's - these are connected to the current "
                + "HlsSupported with succession or precedence relations, and are used in deciding the correct continuation "
                + "by inhibiting the ones which are not immediate successors.");
        fmt.explanatoryNote("predecessor value / successor value + link to HLS (only for the ones where one is non-zero");
        fmt.explanatoryNote("FIXME: not implemented yet!!!");
        /*
         * for (Choice choiceLink : hlc.getChoices().values()) { double
         * strengthLink = hlss.summativeSupport(
         * Hls.SupportAlgorithm.ALGORITHM_CONTINUATION, agent).getValue(); // if
         * the strength is less then zero, ignore it if (strengthLink < 0.0) {
         * continue; } if (hlsLink != hlss) { // only consider if any of them it
         * is larger than zero double succValue = hlss.getLink(agent, hlsLink,
         * ChoiceType.CONTINUATION, ViLinkDB.SUCCESSOR); double predValue =
         * hlsLink.getLink(agent, hlss, ChoiceType.CONTINUATION,
         * ViLinkDB.SUCCESSOR); if (succValue > 0.0 || predValue > 0.0) {
         * fmt.openP(); PwHelper.progressBar(fmt, predValue, 1.0);
         * PwHelper.progressBar(fmt, succValue, 1.0); PwLinks.linkToHls(fmt,
         * agent, query, hlsLink); fmt.closeP(); } } }
         */
    }

    /**
     * Prints a characterization type choice
     * 
     * @param fmt
     * @param agent
     * @param choice
     * @param query
     */
    private static void structureCharacterization(IXwFormatter fmt, Agent agent,
            Choice choice, RESTQuery query) {
        fmt.explanatoryNote("A characterization choice is based on a HlsCharacterization object"
                + " and its score is maintained ");
        //
        // The HlsCharacterization
        //
        fmt.addH2("HlsCharacterization");
        HlsCharacterization hlsc = choice.getHlsCharacterization();
        qh_HLS_CHARACTERIZATION.pwDetailed(fmt, hlsc, agent, query);
        fmt.addH2("CharacterizationScore:");
        CharacterizationScore cs = choice.getCharacterizationScore();
        String tmp = cs.toString();
        fmt.addPre(tmp);

    }

    /**
     * Prints the continuation type choice
     * 
     * @param fmt
     * @param agent
     * @param choice
     * @param query
     */
    private static void structureContinuation(IXwFormatter fmt, Agent agent,
            Choice choice, RESTQuery query) {
        fmt.explanatoryNote("A continuation choice is based on a Hls object");
        qh_CHOICE.addHls(fmt, agent, choice, query);
    }

    /**
     * Prints the continuation type choice
     * 
     * @param fmt
     * @param agent
     * @param choice
     * @param query
     */
    private static void structureStatic(IXwFormatter fmt, Agent agent,
            Choice choice, RESTQuery query) {
        fmt.explanatoryNote("A static choice is based on a StaticHls choice");
        qh_CHOICE.addStaticHls(fmt, agent, choice, query);
    }

    /**
     * The choice is a missing action
     * 
     * @param fmt
     * @param agent
     * @param choice
     * @param query
     */
    private static void structureMissingAction(IXwFormatter fmt, Agent agent,
            Choice choice, RESTQuery query) {
        fmt.explanatoryNote("A missing action choice is based on an Hls object");
        qh_CHOICE.addHls(fmt, agent, choice, query);
    }

    /**
     * Print the structure of a missing relation choice
     * 
     * @param fmt
     * @param agent
     * @param choice
     * @param query
     */
    private static void structureMissingRelation(IXwFormatter fmt, Agent agent,
            Choice choice, RESTQuery query) {
        fmt.explanatoryNote("A missing relation choice is based on an Hls object");
        qh_CHOICE.addHls(fmt, agent, choice, query);
    }

}
