/*
   This file is part of the Xapagy project
   Created on: Jul 13, 2011
 
   org.xapagy.recall.testCharacterizationScore
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import org.junit.Assert;

import org.xapagy.TestHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.learning.LearnProperNames;
import org.xapagy.ui.TextUi;

/**
 * 
 * A variety of tests for the characterization score
 * 
 * FIXME: these are not turned on, because: 1 the characterization is not really
 * cleaned up yet and 2. they are too dependent on some peculiarities of the old
 * debug Iliad domain
 * 
 * @author Ladislau Boloni
 * 
 */
public class testCharacterizationScoreHelper {

    // @Test
    public void testAlreadyPresentScore() {
        String description = "CharacterizationScore.getAlreadyPresentScore";
        TestHelper.testStart(description);
        Runner r = new Runner("Iliad");
        r.printOn = true;
        r.exec("A scene #Scene1 / exists.");
        r.exec("$ChangeScene #Scene1");
        VerbInstance vi = r.exac("A woman / exists.");
        Instance instWoman = vi.getSubject();
        ConceptOverlay coWoman = instWoman.getConcepts();
        ConceptOverlay coMan = r.agent.getXapiDictionary().getCoForWord("man");
        double val =
                CharacterizationScoreHelper.getAlreadyPresentScore(r.agent,
                        coWoman, coMan);
        TextUi.println("val=" + val);
        Assert.assertTrue(val == 0);
        ConceptOverlay coFearless =
                r.agent.getXapiDictionary().getCoForWord("fearless");
        // TextUi.println(PrettyPrint.ppDetailed(coFearless, r.agent));
        val =
                CharacterizationScoreHelper.getAlreadyPresentScore(r.agent,
                        coWoman, coFearless);
        // TextUi.println("val=" + val);
        Assert.assertTrue(val == 0);
        TestHelper.testDone();
    }

    // @Test
    public void testEssenceScore() {
        String description = "CharacterizationScore.getUniquenessScore";
        TestHelper.testStart(description);
        Runner r = new Runner("Iliad");
        r.printOn = true;
        r.exec("A scene #Scene1 / exists.");
        r.exec("$ChangeScene #Scene1");
        VerbInstance vi = r.exac("A 'Hector' / exists.");
        Instance instHector = vi.getSubject();
        // trying out
        // ConceptOverlay coTrojan =
        // r.agent.getXapiDictionary().getCoForWord("\"Mu\"");
        ConceptOverlay coTrojan =
                LearnProperNames.learnTheWord(r.agent, "\"Mu\"");
        double val =
                CharacterizationScoreHelper.getEssenceScore(r.agent,
                        instHector.getConcepts(), coTrojan);
        // TextUi.println("essence adding 'Mu' = " + val);
        Assert.assertTrue(val == 0.0);
        ConceptOverlay coMan = r.agent.getXapiDictionary().getCoForWord("man");
        // TextUi.println(PrettyPrint.ppDetailed(coMan, r.agent));
        val =
                CharacterizationScoreHelper.getEssenceScore(r.agent,
                        instHector.getConcepts(), coMan);
        // TextUi.println("essence adding man = " + val);
        Assert.assertTrue(val == 2.0);
        TestHelper.testDone();
    }

    /**
     * What this tests is how much closer to the target we are getting if we are
     * adding some kind of instance - one of the problem we are having here is
     * that it is not testing whether we are introducing something conflicting:
     * 
     * FIXME: reimplement this using the MatchTest
     */
    // @Test
    public void testTargetDifference() {
        String description = "CharacterizationScore.getTargetDifference";
        TestHelper.testStart(description);
        Runner r = new Runner("Iliad");
        r.printOn = true;
        r.exec("A scene #Scene1 / exists.");
        r.exec("$ChangeScene #Scene1");
        VerbInstance vi = r.exac("A 'Hector' / exists.");
        Instance instHector = vi.getSubject();
        // trying out
        ConceptOverlay coTarget = new ConceptOverlay(r.agent);
        coTarget.addOverlay(r.agent.getXapiDictionary().getCoForWord(
                "\"Hector\""));
        coTarget.addOverlay(r.agent.getXapiDictionary().getCoForWord("man"));
        coTarget.addOverlay(r.agent.getXapiDictionary().getCoForWord("warrior"));
        coTarget.addOverlay(r.agent.getXapiDictionary().getCoForWord("trojan"));

        ConceptOverlay coTrojan =
                r.agent.getXapiDictionary().getCoForWord("trojan");
        ConceptOverlay coCourageous =
                r.agent.getXapiDictionary().getCoForWord("courageous");
        double val =
                CharacterizationScoreHelper.getTargetDifference(r.agent,
                        instHector.getConcepts(), coTrojan, coTarget);
        // TextUi.println("target difference trojan = " + val);
        Assert.assertTrue(val == 1.0);
        val =
                CharacterizationScoreHelper.getTargetDifference(r.agent,
                        instHector.getConcepts(), coCourageous, coTarget);
        // TextUi.println("target difference female = " + val);
        Assert.assertTrue(val == 0.0);
        TestHelper.testDone();
    }

    // @Test
    public void testUniquenessScore() {
        String description = "CharacterizationScore.getUniquenessScore";
        TestHelper.testStart(description);
        Runner r = new Runner("Iliad");
        r.printOn = true;
        r.exec("A scene #Scene1 / exists.");
        r.exec("$ChangeScene #Scene1");
        VerbInstance vi = r.exac("A woman / exists.");
        Instance instWoman = vi.getSubject();
        r.exec("A fearless man / exists.");
        // trying out
        ConceptOverlay coTrojan =
                r.agent.getXapiDictionary().getCoForWord("trojan");
        double val =
                CharacterizationScoreHelper.getUniquenessScore(r.agent,
                        instWoman, coTrojan);
        // TextUi.println("val=" + val);
        Assert.assertTrue(val == 1.0);
        ConceptOverlay coFearless =
                r.agent.getXapiDictionary().getCoForWord("fearless");
        // TextUi.println(PrettyPrint.ppDetailed(coFearless, r.agent));
        val =
                CharacterizationScoreHelper.getUniquenessScore(r.agent,
                        instWoman, coFearless);
        // TextUi.println("val=" + val);
        Assert.assertTrue(val == 0);
        TestHelper.testDone();
    }

}
