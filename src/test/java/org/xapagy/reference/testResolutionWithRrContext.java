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
package org.xapagy.reference;

import java.util.AbstractMap.SimpleEntry;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.xapagy.ArtificialDomain;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.reference.rrState.rrPhase;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;

/**
 * A series of tests which test the resolution starting from the rrContext
 * object. This is always using the general purpose
 * ReferenceResolution.resolveReference function.
 * 
 * @author Ladislau Boloni
 * Created on: Apr 15, 2014
 */
public class testResolutionWithRrContext {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Create an rrContext object from a reference string. The assumption is
     * that we are referencing in the current scene and as the reference being
     * put as a subject
     * 
     * @param r
     *            - the runner
     * @param ref
     *            - the reference string, as a text
     * @return
     * @throws XapiParserException
     */
    public static rrContext createRefAsSubject(Runner r, String ref)
            throws XapiParserException {
        XapiParser xp = r.agent.getXapiParser();
        Instance scene = r.agent.getFocus().getCurrentScene();
        ViPart partInVi = ViPart.Subject;
        VerbOverlay verbsInVi = xp.parseVo("wa_v_av10");
        VerbInstance viInquitParent = null;
        XapiReference reference =
                xp.parseNounPhrase(ref, XapiPositionInParent.SUBJECT);
        rrContext rrc = rrContext.createFromXapiReference(r.agent, reference,
                verbsInVi, partInVi, scene, viInquitParent);
        return rrc;
    }

    /**
     * Creates a simple basic story in which references can be checked.
     * 
     * @return
     */
    public Runner createBasicStory() {
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("A w_c_bai20 'Achilles' #Achilles / exists.");
        r.exec("A w_c_bai20 w_c_bai22 'Hector' #Hector / exists.");
        r.exec("'Achilles' / wcr_vr_rel1 / a w_c_bai21 #AchillesSword.");
        r.exec("'Hector' / wcr_vr_rel1 / a w_c_bai21 #HectorSword.");
        return r;
    }

    /**
     * Checks a reference in story, returns the pair of reference and
     * confidence.
     * 
     * @param r
     *            - the runner
     * @param reference
     *            - the reference string
     * @param expectedPhase
     *            - the expected phase - UNDETERMINED, WINNER, etc.
     * @param expectedLabel
     *            - if the reference returned something, the expected label of
     *            the reference
     * 
     * @return
     * @throws XapiParserException
     * @throws rrException
     */
    public SimpleEntry<Instance, rrState> checkReferenceInStory(Runner r,
            String reference, rrPhase expectedPhase, String expectedLabel)
                    throws XapiParserException, rrException {
        String expectedLabelFull = null;
        if (expectedLabel != null) {
            expectedLabelFull = r.agent.getLabelSpaces().fullLabel(expectedLabel);
        }
        rrContext rrc =
                testResolutionWithRrContext.createRefAsSubject(r, reference);
        SimpleEntry<Instance, rrState> result =
                ReferenceResolution.resolveReference(rrc);
        TextUi.println(result.getValue().toString());
        TextUi.println(result.getKey().toString());
        // see if we got the phase we expected
        Assert.assertEquals(result.getValue().getPhase(), expectedPhase);
        // if we expected a winner, see if we got the instance with the expected
        // label
        if (expectedPhase == rrPhase.WINNER) {
            Assert.assertNotNull(result.getKey());
            if (expectedLabelFull != null) {
                r.ah.hasLabel(result.getKey(), expectedLabelFull);
            }
        }
        return result;
    }

    /**
     * Reference with proper name. We are trying an access with Achilles. In
     * this story, this will be successful, and it will return the instance that
     * has Achilles in it.
     * 
     * @throws XapiParserException
     * @throws rrException
     */
    @Test
    public void testProperNameReference()
            throws XapiParserException, rrException {
        Runner r = createBasicStory();
        checkReferenceInStory(r, "\"Achilles\"", rrPhase.WINNER, "#Achilles");
    }

    /**
     * Reference with an unambiguos attribute. Only Hector has the w_c_bai22
     * attribute
     * 
     * @throws XapiParserException
     * @throws rrException
     */
    @Test
    public void testUnambiguousAttributeReference()
            throws XapiParserException, rrException {
        Runner r = createBasicStory();
        checkReferenceInStory(r, "w_c_bai22", rrPhase.WINNER, "#Hector");
    }

    /**
     * Reference with an unambiguos attribute. Only Hector has the w_c_bai22
     * attribute
     * 
     * @throws XapiParserException
     * @throws rrException
     */
    @Test
    public void testGroupReference() throws XapiParserException, rrException {
        Runner r = createBasicStory();
        SimpleEntry<Instance, rrState> retval = checkReferenceInStory(r,
                "w_c_bai22 + \"Achilles\"", rrPhase.WINNER, null);
        r.ah.hasAttribute(retval.getKey(), "c_group");
    }

    /**
     * Relational referencing
     * 
     * @throws XapiParserException
     * @throws rrException
     */
    @Test
    public void testRelationalReference()
            throws XapiParserException, rrException {
        Runner r = createBasicStory();
        checkReferenceInStory(r, "w_c_bai21 -- wv_vr_rel1 -- \"Achilles\"",
                rrPhase.WINNER, "#AchillesSword");
    }

    /**
     * Relational referencing
     * 
     * @throws XapiParserException
     * @throws rrException
     */
    @Test
    public void testNotFoundAttribute()
            throws XapiParserException, rrException {
        thrown.expect(rrException.class);
        Runner r = createBasicStory();
        @SuppressWarnings("unused")
        SimpleEntry<Instance, rrState> retval =
                checkReferenceInStory(r, "w_c_bai29", rrPhase.WINNER, null);
    }

}
