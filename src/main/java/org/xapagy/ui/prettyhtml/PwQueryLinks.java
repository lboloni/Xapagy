package org.xapagy.ui.prettyhtml;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.LoopItem;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.Verb;
import org.xapagy.debug.storyline.StoryLine;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.headless_shadows.HlsNewInstance;
import org.xapagy.headless_shadows.SOSP;
import org.xapagy.headless_shadows.StaticFSLI;
import org.xapagy.headless_shadows.StaticHls;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.set.InstanceSet;
import org.xapagy.set.ViSet;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyprint.PpConcept;
import org.xapagy.ui.prettyprint.PpVerb;
import org.xapagy.ui.queryhandlers.qh_CHOICE;
import org.xapagy.ui.queryhandlers.qh_FOCUS_SHADOW_LINKED;
import org.xapagy.ui.queryhandlers.qh_FSL_INTERPRETATION;
import org.xapagy.ui.queryhandlers.qh_HLS_SUPPORTED;
import org.xapagy.ui.queryhandlers.qh_HLS_NEW_INSTANCE;
import org.xapagy.ui.queryhandlers.qh_LOOP_ITEM;
import org.xapagy.ui.queryhandlers.qh_SOSP;
import org.xapagy.ui.queryhandlers.qh_STATIC_FSLI;
import org.xapagy.ui.queryhandlers.qh_STATIC_HLS;
import org.xapagy.ui.smartprint.SpFocus;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.xapi.ConceptWord;
import org.xapagy.xapi.VerbWord;

public class PwQueryLinks implements IQueryAttributes {

    /**
     * Adds a web link to a specific query. Should be widely used
     * 
     * @param fmt
     * @param gq
     * @param label
     */
    public static String addLinkToQuery(IXwFormatter fmt, RESTQuery gq,
            String label, String theClass, String... params) {
        String p[] = new String[2 + params.length];
        p[0] = "href=" + gq.toQuery();
        p[1] = "class=" + theClass;
        for (int i = 0; i != params.length; i++) {
            p[2 + i] = params[i];
        }
        // fmt.openA("href=" + gq.toQuery(), "class=" + theClass, params);
        fmt.openA(p);
        fmt.add(label);
        fmt.closeA();
        return fmt.toString();
    }

    /**
     * Navigational line for moving the cursor
     * 
     * @return
     */
    public static String cursorLinks(IXwFormatter fmt, RESTQuery gq) {
        SimpleEntry<RESTQuery, RESTQuery> entry =
                QueryHelper.createCursorNeighbors(gq);
        RESTQuery gqPrevious = entry.getKey();
        RESTQuery gqNext = entry.getValue();
        if (gqPrevious != null) {
            PwQueryLinks.addLinkToQuery(fmt, gqPrevious, " << ",
                    PwFormatter.CLASS_BODYLINK);
        }
        int cursorFrom = Integer.parseInt(gq.getAttribute(Q_CURSOR_FROM));
        int cursorTo = Integer.parseInt(gq.getAttribute(Q_CURSOR_TO));
        int cursorTotal = Integer.parseInt(gq.getAttribute(Q_CURSOR_TOTAL));
        fmt.add("[" + (cursorFrom + 1) + " - " + cursorTo + " ] of "
                + cursorTotal);
        if (gqNext != null) {
            PwQueryLinks.addLinkToQuery(fmt, gqNext, " >> ",
                    PwFormatter.CLASS_BODYLINK);
        }
        return fmt.toString();
    }

    /**
     * Adds a link to a choice
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param choice
     */
    public static String linkToChoice(IXwFormatter fmt, Agent agent,
            RESTQuery gq, Choice choice) {
        RESTQuery gqChoice = QueryHelper.copyWithEmptyCommand(gq);
        gqChoice.setAttribute(Q_QUERY_TYPE, "CHOICE");
        gqChoice.setAttribute(Q_ID, choice.getIdentifier());
        String label = qh_CHOICE.pwConcise(choice, agent);
        return PwQueryLinks.addLinkToQuery(fmt, gqChoice, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an <a href> link to an instance, currently the class is
     * CLASS_BODYLINK
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param member
     */
    public static String linkToConcept(IXwFormatter fmt, Agent agent,
            RESTQuery gq, Concept concept) {
        String label = PpConcept.ppConcise(concept, agent);
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(gq);
        gqNew.setAttribute(Q_QUERY_TYPE, "CONCEPT");
        gqNew.setAttribute(Q_ID, concept.getIdentifier());
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates a link to a concept word
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param conceptWord
     */
    public static String linkToConceptWord(IXwFormatter fmt, Agent agent,
            RESTQuery gq, ConceptWord conceptWord) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(gq);
        gqNew.setAttribute(Q_QUERY_TYPE, "CONCEPT_WORD");
        gqNew.setAttribute(Q_ID, conceptWord.getIdentifier());
        String label = conceptWord.getTextFormat();
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an <a href> link to a FSL. The class of the html element is
     * CLASS_BODYLINK
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param fsl
     */
    public static String linkToFsl(PwFormatter fmt, Agent agent, RESTQuery gq,
            FocusShadowLinked fsl) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(gq);
        gqNew.setAttribute(Q_QUERY_TYPE, "FOCUS_SHADOW_LINKED");
        gqNew.setAttribute(Q_ID, fsl.getIdentifier());
        String label =
                qh_FOCUS_SHADOW_LINKED.pwConcise(fmt.getEmpty(), fsl, agent);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an <a href> link to a Svir. The class of the html element is
     * CLASS_BODYLINK
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param fsli
     */
    public static String linkToFsli(String howtolabel, IXwFormatter fmt,
            Agent agent, RESTQuery gq, FslInterpretation fsli) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(gq);
        gqNew.setAttribute(Q_QUERY_TYPE, "FSL_INTERPRETATION");
        gqNew.setAttribute(Q_ID, fsli.getIdentifier());
        String label =
                qh_FSL_INTERPRETATION.pwConcise(howtolabel, fmt.getEmpty(),
                        fsli, agent);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an <a href> link to a HlsSupported. The class of the html element
     * is CLASS_BODYLINK
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param sb
     */
    public static String linkToHls(IXwFormatter fmt, Agent agent, RESTQuery gq,
            Hls hls) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(gq);
        gqNew.setAttribute(Q_QUERY_TYPE, "HLS_SUPPORTED");
        gqNew.setAttribute(Q_ID, hls.getIdentifier());
        String label = qh_HLS_SUPPORTED.pwConcise(hls, agent);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an <a href> link to a HlsNewInstance. The class of the html
     * element is CLASS_BODYLINK
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param hlsni
     */
    public static String linkToHlsNewInstance(IXwFormatter fmt, Agent agent,
            RESTQuery gq, HlsNewInstance hlsni) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(gq);
        gqNew.setAttribute(Q_QUERY_TYPE, "HLS_NEW_INSTANCE");
        gqNew.setAttribute(Q_ID, hlsni.getIdentifier());
        String label = qh_HLS_NEW_INSTANCE.pwConcise(hlsni, agent);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an <a href> link to an instance, currently the class is
     * CLASS_BODYLINK
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param instance
     */
    public static String linkToInstance(IXwFormatter fmt, Agent agent,
            RESTQuery query, Instance instance) {
        String label = "";
        label += new PwFormatter().addIdentifier(instance);
        if (!instance.isScene()) {
            label +=
                    SpInstance.spInstance(instance, instance.getScene(), agent,
                            false);
            label +=
                    " of scene "
                            + new PwFormatter().addIdentifier(instance
                                    .getScene())
                            + SpFocus.ppsScene(instance.getScene(), agent,
                                    false);
        } else {
            label += new PwFormatter().addBold("Scene:");
            label += SpFocus.ppsScene(instance, agent, false);
        }
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(query);
        gqNew.setAttribute(Q_QUERY_TYPE, "INSTANCE");
        gqNew.setAttribute(Q_ID, instance.getIdentifier());
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an UL with links to the instances
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param instancelist
     */
    public static String linkToInstanceList(PwFormatter fmt, Agent agent,
            RESTQuery gq, List<Instance> instancelist) {
        fmt.openUL();
        for (Instance member : instancelist) {
            fmt.openLI();
            PwQueryLinks.linkToInstance(fmt, agent, gq, member);
            fmt.closeLI();
        }
        fmt.closeUL();
        return fmt.toString();
    }

    /**
     * Creates a series of P components with links to instances
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param instancelist
     */
    public static String linkToInstanceSet(PwFormatter fmt, Agent agent,
            RESTQuery gq, InstanceSet is) {
        for (SimpleEntry<Instance, Double> entry : is
                .getDecreasingStrengthList()) {
            fmt.openP();
            fmt.progressBar(entry.getValue(), 1.0);
            PwQueryLinks.linkToInstance(fmt, agent, gq, entry.getKey());
            fmt.closeP();
        }
        return fmt.toString();
    }

    /**
     * Creates a link to the explanation of a particular shadow for instances
     * 
     * -it is somewhat unusual because it is creating the identifier from two
     * other identifiers
     * 
     * @return
     */
    public static String linkToInstanceShadowExplanation(IXwFormatter fmt,
            Agent agent, RESTQuery query, Instance fi, Instance si) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(query);
        gqNew.setAttribute(Q_QUERY_TYPE, "INSTANCE_SHADOW_EXPLANATION");
        // gqNew.setAttribute(Q_ID, qh_INSTANCE_SHADOW_EXPLANATION.mergeIds(fi,
        // si));
        gqNew.setAttribute(Q_ID, fi.getIdentifier());
        gqNew.setAttribute(Q_SECOND_ID, si.getIdentifier());
        String label = "[explain]";
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an <a href> link to a VI. The class of the html element is
     * CLASS_BODYLINK
     * 
     * Due to the way the text is serialized, currently it might not work for
     * anything than action VI.
     * 
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param member
     */
    public static String linkToLoopItem(IXwFormatter fmt, Agent agent,
            RESTQuery gq, LoopItem li, String... params) {
        // RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(gq);
        // gqNew.setAttribute(Q_QUERY_TYPE, "LOOP_ITEM");
        // gqNew.setAttribute(Q_ID, li.getIdentifier());
        RESTQuery gqNew =
                QueryHelper.newQuery(agent, "LOOP_ITEM", li.getIdentifier());
        String label = qh_LOOP_ITEM.pwConcise(new PwFormatter(), li, agent);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK, params);
    }

    /**
     * Creates a link to Link object.
     * 
     * @param fmt
     * @param label
     *            - the label to be passed on. Many times, this will not list
     *            both items, so we cannot generate a default one here
     * @param agent
     * @param from
     * @param to
     * @param linkName
     * @param params
     * @return
     */
    public static String linkToLink(IXwFormatter fmt, String label,
            Agent agent, VerbInstance from, VerbInstance to, String linkName,
            String... params) {
        RESTQuery gqNew =
                QueryHelper.newQuery(agent, "LINK", from.getIdentifier());
        gqNew.setAttribute(Q_SECOND_ID, to.getIdentifier());
        gqNew.setAttribute(Q_LINK_TYPE, linkName);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK, params);
    }

    /**
     * Creates a colored link to a story line
     * 
     * FIXME: there are no pages for story line right now, so for the time being
     * this will be just the color display
     * 
     * @param fmt
     * @param agent
     */
    public static void linkToStoryLineColor(IXwFormatter fmt, Agent agent,
            XapagyComponent xc, ColorCodeRepository ccr) {
        StoryLine st = null;
        if (xc instanceof VerbInstance) {
            st =
                    agent.getStoryLineRepository().getStoryLineOfVi(
                            (VerbInstance) xc);
        } else if (xc instanceof Instance) {
            st =
                    agent.getStoryLineRepository().getStoryLineOfInstance(
                            (Instance) xc);
        } else {
            TextUi.abort("weird. ");
        }
        ColorCode cc = ccr.getColorCode(st);
        fmt.addColorCode(cc);
    }

    /**
     * Creates an <a href> link to an instance, currently the class is
     * CLASS_BODYLINK
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param member
     */
    public static String linkToVerb(IXwFormatter fmt, Agent agent,
            RESTQuery gq, Verb verb) {
        String label = PpVerb.ppConcise(verb, agent);
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(gq);
        gqNew.setAttribute(Q_QUERY_TYPE, "VERB");
        gqNew.setAttribute(Q_ID, verb.getIdentifier());
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates a link to a verb word
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param conceptWord
     */
    public static String linkToVerbWord(IXwFormatter fmt, Agent agent,
            RESTQuery query, VerbWord verbWord) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(query);
        gqNew.setAttribute(Q_QUERY_TYPE, "VERB_WORD");
        gqNew.setAttribute(Q_ID, verbWord.getIdentifier());
        String label = verbWord.getTextFormat();
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates an <a href> link to a VI. The class of the html element is
     * CLASS_BODYLINK
     * 
     * Due to the way the text is serialized, currently it might not work for
     * anything than action VI.
     * 
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param member
     */
    public static String linkToVi(IXwFormatter fmt, Agent agent,
            RESTQuery query, VerbInstance vi) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(query);
        gqNew.setAttribute(Q_QUERY_TYPE, "VERB_INSTANCE");
        gqNew.setAttribute(Q_ID, vi.getIdentifier());
        String label = SpFocus.ppsViXapiForm(vi, agent);
        label += fmt.getEmpty().addIdentifier(vi);
        // adds the scene of the subject!
        Instance instance = vi.getSubject();
        label +=
                " of scene "
                        + new PwFormatter().addIdentifier(instance.getScene())
                        + SpFocus.ppsScene(instance.getScene(), agent, false);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates a series of links to a ViSet
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param vis
     * @return
     */
    public static String linkToViSet(IXwFormatter fmt, Agent agent,
            RESTQuery query, ViSet vis) {
        for (SimpleEntry<VerbInstance, Double> entry : vis
                .getDecreasingStrengthList()) {
            fmt.openP();
            fmt.progressBar(entry.getValue(), 1.0);
            PwQueryLinks.linkToVi(fmt, agent, query, entry.getKey());
            fmt.closeP();
        }
        return fmt.toString();
    }

    /**
     * Creates a link to the explanation of a particular shadow for instances
     * 
     * -it is somewhat unusual because it is creating the identifier from two
     * other identifiers
     * 
     * @return
     */
    public static String linkToViShadowExplanation(IXwFormatter fmt,
            Agent agent, RESTQuery query, VerbInstance fvi, VerbInstance svi) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(query);
        gqNew.setAttribute(Q_QUERY_TYPE, "VI_SHADOW_EXPLANATION");
        gqNew.setAttribute(Q_ID, fvi.getIdentifier());
        gqNew.setAttribute(Q_SECOND_ID, svi.getIdentifier());
        String label = "[explain]";
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates a link to an SOSP
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param sosp
     */
    public static String linkToSOSP(IXwFormatter fmt, Agent agent,
            RESTQuery query, SOSP sosp) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(query);
        gqNew.setAttribute(Q_QUERY_TYPE, "SOSP");
        gqNew.setAttribute(Q_ID, sosp.getIdentifier());
        String label = qh_SOSP.concise(agent, sosp);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates a link to static FSLI
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param sfsli
     */
    public static String linkToStaticFSLI(IXwFormatter fmt, Agent agent,
            RESTQuery query, StaticFSLI sfsli) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(query);
        gqNew.setAttribute(Q_QUERY_TYPE, "STATIC_FSLI");
        gqNew.setAttribute(Q_ID, sfsli.getIdentifier());
        String label = qh_STATIC_FSLI.pwConcise(agent, sfsli);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

    /**
     * Creates a link to static FSLI
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param sfsli
     */
    public static String linkToStaticHls(IXwFormatter fmt, Agent agent,
            RESTQuery query, StaticHls shls) {
        RESTQuery gqNew = QueryHelper.copyWithEmptyCommand(query);
        gqNew.setAttribute(Q_QUERY_TYPE, "STATIC_HLS");
        gqNew.setAttribute(Q_ID, shls.getIdentifier());
        String label = qh_STATIC_HLS.pwConcise(agent, shls);
        return PwQueryLinks.addLinkToQuery(fmt, gqNew, label,
                PwFormatter.CLASS_BODYLINK);
    }

}
