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
package org.xapagy.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.agents.liHlsChoiceBased;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwInstance;
import org.xapagy.xapi.XapiParserException;

/**
 * This class provides a series of functions which either directly enable
 * unit-test assertions, or provide tools for finding components in the focus,
 * shadows, choices etc. on which later assertions can be provided
 * 
 * @author Ladislau Boloni
 * Created on: Aug 17, 2011
 */
public class AssertionHelper implements Serializable {

    public enum AssertionType {
        EXACTLY_ONE, EXISTS, NONE, NOT_EXIST
    }

    public enum ViMatcher {
        VERB, VERBALIZED
    }

    private static final long serialVersionUID = -6906475268268617982L;
    private Agent agent;
    private ViMatch vimatch;

    @Deprecated
    private AssertionHelper ahNotAsserting;
    /**
     * When set to true, all the returns values are verified with a JUnit
     * assertion
     */
    private boolean junitAssert;

    /**
     * @param agent
     */
    public AssertionHelper(Agent agent, boolean junitAssert) {
        super();
        this.agent = agent;
        this.junitAssert = junitAssert;
        this.vimatch = new ViMatch(agent);
        if (junitAssert) {
            ahNotAsserting = new AssertionHelper(agent, false);
        } else {
            ahNotAsserting = this;
        }
    }

    /**
     * Checks whether a choice is of a given type and its hls template is of a
     * given style
     * 
     * @param choice
     * @param type
     * @param verbName
     * @param params
     * @throws XapiParserException
     */
    public boolean choiceIs(Choice choice, ChoiceType type, String verbName,
            String... params) throws XapiParserException {
        if (choice.getChoiceType() != type) {
            return false;
        }
        // fall back on viIs()
        return viIs(choice.getHls().getViTemplate(), null, params);
    }

    /**
     * Checks whether an instance has an attribute (either directly or through
     * overlap)
     * 
     * @param instPedro
     * @param string
     */
    public boolean coContains(ConceptOverlay co, String conceptName) {
        boolean retval = Hardwired.contains(agent, co, conceptName);
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Checks whether an instance has an attribute (either directly or through
     * overlap)
     * 
     * @param instPedro
     * @param string
     */
    public boolean coDoesntContain(ConceptOverlay co, String conceptName) {
        boolean retval = !Hardwired.contains(agent, co, conceptName);
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Checks whether an instance has an attribute (either directly or through
     * overlap)
     * 
     * @param instPedro
     * @param string
     */
    public boolean hasAttribute(Instance instance, String conceptName) {
        return coContains(instance.getConcepts(), conceptName);
    }

    /**
     * Find all the choices which match the specified requirements. Verify a
     * certain assertion type. Return the matching ones if the assertion had
     * been passed.
     * 
     * @param assertionType
     * @param type
     * @param verbName
     * @param params
     * @return
     * @throws XapiParserException
     */
    public List<Choice> hasChoiceWhich(AssertionType assertionType,
            ChoiceType type, String verbName, String... params)
                    throws XapiParserException {
        List<Choice> retval = new ArrayList<>();
        for (Choice choice : agent.getHeadlessComponents().getChoices()
                .values()) {
            if (choiceIs(choice, type, verbName, params)) {
                retval.add(choice);
                break;
            }
        }
        //
        // if we need to enforce assertions, let us enforce them here
        //
        if (junitAssert) {
            switch (assertionType) {
            case EXACTLY_ONE:
                Assert.assertTrue(retval.size() == 1);
                break;
            case EXISTS:
                Assert.assertTrue(retval.size() > 0);
                break;
            case NOT_EXIST:
                Assert.assertTrue(retval.size() == 0);
                break;
            case NONE:
                break;
            }
        }
        return retval;
    }

    /**
     * Checks whether there is a VI of a given type
     * 
     * @param string
     * @param string2
     * @param string3
     * @throws XapiParserException
     */
    public boolean hasFocusViWhich(ViType viType, String verbName,
            String... params) throws XapiParserException {
        boolean found = false;
        Focus fc = agent.getFocus();
        for (VerbInstance vi : fc.getViList(EnergyColors.FOCUS_VI)) {
            if (ahNotAsserting.viIs(vi, viType, params)) {
                found = true;
                break;
            }
        }
        if (junitAssert) {
            Assert.assertTrue(found);
        }
        return found;
    }

    /**
     * Checks whether there is a HLS whose template matches the verbname and
     * parameters passed
     * 
     * @param string
     * @param string2
     * @param string3
     */
    public boolean hasHlsWhich(FslType algorithm, ViType viType,
            String verbName, String... params) {
        boolean found = false;
        HeadlessComponents hlc = agent.getHeadlessComponents();
        for (Hls hls : hlc.getHlss()) {
            double value = hls.summativeSupport(algorithm, agent).getValue();
            if (value <= 0.0) {
                continue;
            }
            VerbInstance vit = hls.getViTemplate();
            if (ahNotAsserting.verbInstanceTemplateIs(vit, viType, verbName,
                    params)) {
                found = true;
                break;
            }
        }
        if (junitAssert) {
            Assert.assertTrue(found);
        }
        return found;
    }

    /**
     * Verifies is a given instance has a specific label - easy way to test for
     * identity without the need to capture it first
     * 
     * @param key
     * @param string
     */
    public boolean hasLabel(Instance instance, String label) {
        boolean retval = instance.getConcepts().getLabels().contains(label);
        if (junitAssert) {
            Assert.assertTrue(
                    "Instance " + xwInstance.xwConcise(new TwFormatter(), instance, agent)
                            + " doesn't have the required label " + label,
                    retval);
        }
        return retval;
    }

    /**
     * Verifies that in the history of the agent, back steps back, we have a
     * loop item which had been internally generated and it is a verb of the
     * given type and with a given verb
     * 
     * @param back
     * @param verbName
     * @throws XapiParserException
     */
    public boolean historyIsInternal(int back, ViType viType, String verbName)
            throws XapiParserException {
        List<AbstractLoopItem> history = agent.getLoop().getHistory();
        AbstractLoopItem li = history.get(history.size() - back);
        boolean retval;
        if (!(li instanceof liHlsChoiceBased)) {
            retval = false;
        } else {
            retval = ahNotAsserting.viIs(li.getExecutionResult().get(0), viType,
                    verbName);
        }
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Verifies if a double value is in the interval
     * 
     * @param value
     * @param low
     * @param high
     * @return
     */
    @Deprecated
    public boolean inInterval(double value, double low, double high) {
        boolean retval = value >= low && value <= high;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Verifies if an integer value is in the interval
     * 
     * @param value
     * @param low
     * @param high
     * @return
     */
    public boolean inInterval(int value, int low, int high) {
        boolean retval = value >= low && value <= high;
        if (junitAssert) {
            Assert.assertTrue("value " + value + " is not in the range " + low
                    + " to " + high, retval);
        }
        return retval;
    }

    /**
     * Verifies the inst1 is in the specified relation with inst2
     * 
     * @param relationName
     * @param instHector
     * @param instSword
     */
    public boolean inRelation(String relationName, Instance inst1,
            Instance inst2) {
        double val = RelationHelper.getRelation(true, agent, relationName,
                inst1, inst2);
        boolean retval = val > 0.2;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * @param string
     * @param instAchilles
     */
    public boolean inUnaryRelation(String relationName, Instance inst1) {
        double val =
                RelationHelper.getRelation(true, agent, relationName, inst1);
        boolean retval = val > 0.2;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Checks whether a specific instance is in a specific scene
     * 
     * @param scene
     * @param instance
     * @return
     */
    public boolean isInScene(Instance scene, Instance instance) {
        boolean retval = scene.getSceneMembers().contains(instance);
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * An instance is not in the focus
     * 
     * @param scene
     */
    public boolean isNotInFocus(Instance instance) {
        double strength =
                agent.getFocus().getSalience(instance, EnergyColors.FOCUS_INSTANCE);
        boolean retval = strength == 0.0;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * A verb instance is not in the focus
     * 
     * @param scene
     */
    public boolean isNotInFocus(VerbInstance vi) {
        double strength = agent.getFocus().getSalience(vi, EnergyColors.FOCUS_VI);
        boolean retval = strength == 0.0;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * @param initialScene
     * @param instWoman
     */
    public boolean isNotInScene(Instance scene, Instance instance) {
        boolean retval = !scene.getSceneMembers().contains(instance);
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * An instance is in the focus and it is strong
     * 
     * @param scene
     */
    public boolean isStrongInFocus(Instance instance) {
        double strength =
                agent.getFocus().getSalience(instance, EnergyColors.FOCUS_INSTANCE);
        boolean retval = strength > 0.5;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * A verb instance is in the focus and it is strong
     * 
     * @param scene
     */
    public boolean isStrongInFocus(VerbInstance vi) {
        double strength = agent.getFocus().getSalience(vi, EnergyColors.FOCUS_VI);
        boolean retval = strength > 0.5;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Returns true if the second VI is the successor of the first
     * 
     * @param first
     * @param second
     * @return
     */
    public boolean isSuccessorOf(VerbInstance first, VerbInstance second) {
        double value = agent.getLinks().getLinkValueByLinkName(first, second,
                Hardwired.LINK_SUCCESSOR);
        boolean retval = value > 0.0;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Checks if fromVi is linked with the specified link to toVi (any value
     * larger than 0)
     * 
     * @param linkName
     * @param fromVi
     * @param toVi
     * @return
     */
    public boolean linkedBy(String linkName, VerbInstance fromVi,
            VerbInstance toVi) {
        return linkedBy(linkName, fromVi, toVi, 0.0);
    }

    /**
     * Checks if fromVi is linked with the specified link to toVi, and the
     * strength is larger than the specified treshold
     * 
     * @param linkName
     * @param fromVi
     * @param toVi
     * @param threshold
     * @return
     */
    public boolean linkedBy(String linkName, VerbInstance fromVi,
            VerbInstance toVi, double threshold) {
        double value = agent.getLinks().getLinkValueByLinkName(fromVi, toVi,
                linkName);
        boolean retval = value > threshold;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * @param relationName
     * @param instHector
     * @param instSword
     */
    public boolean notInRelation(String relationName, Instance inst1,
            Instance inst2) {
        double val = RelationHelper.getRelation(true, agent, relationName,
                inst1, inst2);
        boolean retval = val == 0.0;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * @param string
     * @param instAchilles
     */
    public boolean notInUnaryRelation(String relationName, Instance inst1) {
        double val =
                RelationHelper.getRelation(true, agent, relationName, inst1);
        boolean retval = val == 0.0;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Checks if fromVi is NOT linked with the specified link to toVi
     * 
     * @param linkName
     * @param fromVi
     * @param toVi
     * @return
     */
    public boolean notLinkedBy(String linkName, VerbInstance fromVi,
            VerbInstance toVi) {
        return notLinkedBy(linkName, fromVi, toVi, 0.0);
    }

    /**
     * Checks if fromVi is NOT linked with the specified link to toVi with
     * strength is larger than the specified treshold
     * 
     * @param linkName
     * @param fromVi
     * @param toVi
     * @param threshold
     * @return
     */
    public boolean notLinkedBy(String linkName, VerbInstance fromVi,
            VerbInstance toVi, double threshold) {
        double value = agent.getLinks().getLinkValueByLinkName(fromVi, toVi,
                linkName);
        boolean retval = (value <= threshold);
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * @param inst1
     * @param inst2
     */
    public boolean notTheSameInstance(Instance inst1, Instance inst2) {
        boolean retval = inst1 != inst2;
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * 
     * Verifies that the recent history of the agents contains a sequence of
     * verb instances which have the specified choice type and the specified
     * verb
     * 
     * @param choiceType
     *            CONTINUATION for recall, MISSING_ACTION, MISSING_RELATION etc
     * @param matchStrings
     * @throws XapiParserException
     */
    public boolean sequenceInRecentHistory(ChoiceType choiceType,
            ViMatcher matcher, String... matchStrings) {
        Formatter fmt = new Formatter();
        List<AbstractLoopItem> history = agent.getLoop().getHistory();
        int back = 1;
        int count = matchStrings.length - 1;
        boolean retval = true;
        while (count != -1) {
            if (back > history.size() - 1) {
                retval = false;
                break;
            }
            AbstractLoopItem li = history.get(history.size() - back);
            back++;
            if (!(li instanceof liHlsChoiceBased)) {
                continue;
            }
            // so now we know it is a 
            liHlsChoiceBased li2 = (liHlsChoiceBased) li;
            if (!li2.getChoice().getChoiceType().equals(choiceType)) {
                continue;
            }
            // the matching algorithm
            boolean matches = false;
            switch (matcher) {
            case VERB: {
                String verbName = matchStrings[count];
                matches = ahNotAsserting.viIs(li.getExecutionResult().get(0),
                        null, verbName);
                break;
            }
            case VERBALIZED: {
                String verbalize = matchStrings[count];
                VerbInstance vi = li.getExecutionResult().get(0);
                String vrb = agent.getVerbalize().verbalize(vi);
                matches = verbalize.equals(vrb);
            }
            }
            // the matching algorithm
            if (!matches) {
                fmt.add("NOT Matched: " + matchStrings[count]);
                retval = false;
                break;
            }
            // if it matches, go lower in the count
            fmt.add("Matched: " + matchStrings[count] + " moving on");
            count = count - 1;
        }
        if (!retval) {
            TextUi.println(fmt);
        }
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * 
     * Verifies that the recent history of the agents contains a sequence of
     * verb instances which have the specified choice type and the specified
     * verb
     * 
     * @param choiceType
     * @param verbNames
     * @return
     * @throws XapiParserException
     */
    public boolean sequenceOfVerbsInRecentHistory(ChoiceType choiceType,
            String... verbNames) {
        return sequenceInRecentHistory(choiceType, ViMatcher.VERB, verbNames);
    }

    /**
     * Tests whether a certain verb instance template is of a certain type
     * 
     * @param vt
     * @param type
     * @param verbName
     * @return
     */
    public boolean verbInstanceTemplateIs(VerbInstance vt, ViType type,
            String verbName, String... params) {
        boolean retval = vt.getViType() == type;
        retval &= vt.getVerbs()
                .getEnergy(agent.getVerbDB().getConcept(verbName)) > 0.5;
        if (retval) {
            if (params.length > 0) {
                Instance instSubject =
                        (Instance) vt.getResolvedParts().get(ViPart.Subject);
                if (instSubject == null) {
                    retval = false;
                } else {
                    retval &=
                            ahNotAsserting.hasAttribute(instSubject, params[0]);
                }
            }
            if (params.length > 1) {
                Instance instObject =
                        (Instance) vt.getResolvedParts().get(ViPart.Object);
                if (instObject == null) {
                    retval = false;
                } else {
                    retval &=
                            ahNotAsserting.hasAttribute(instObject, params[1]);
                }
            }
        }
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Tests whether a certain vi is of a certain type and kind
     * 
     * FIXME: use ViMatch directly
     * 
     * @param vi
     * @param type
     *            - the type we expect. If we pass null, don't check
     * @param verbName
     * @return
     * @throws XapiParserException
     */
    public boolean viIs(VerbInstance vi, ViType type, String... params) {
        boolean retval = false;
        retval = vimatch.match(vi, type, params);
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Checks whether a verb overlay contains a certain verb
     * 
     * @param vo
     * @param verbName
     * @return
     */
    public boolean voContains(VerbOverlay vo, String verbName) {
        boolean retval = Hardwired.contains(agent, vo, verbName);
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }

    /**
     * Checks whether a verb overlay contains a certain verb
     * 
     * @param vo
     * @param verbName
     * @return
     */
    public boolean voDoesntContain(VerbOverlay vo, String verbName) {
        boolean retval = !Hardwired.contains(agent, vo, verbName);
        if (junitAssert) {
            Assert.assertTrue(retval);
        }
        return retval;
    }
}
