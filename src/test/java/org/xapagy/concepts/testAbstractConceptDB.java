package org.xapagy.concepts;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.TestHelper;

public class testAbstractConceptDB {

    /**
     * Tests the storage of the impacts, and the fact that these are not
     * symmetric.
     */
    @Test
    public void testImpactStorage() {
        String description = "Test the storage of impacts";
        TestHelper.testStart(description);
        AbstractConceptDB<Concept> cb = new AbstractConceptDB<>();
        Concept animal = new Concept("animal", "x1");
        cb.addConcept(animal);
        Concept plant = new Concept("plant", "x2");
        cb.addConcept(plant);
        Concept human = new Concept("human", "x3");
        cb.addConcept(human);
        cb.setArea(animal, 0.30);
        cb.setArea(human, 0.10);
        cb.setImpact(animal, human, 0.5);
        Assert.assertTrue(cb.getImpact(animal, human) == 0.5);
        Assert.assertTrue(cb.getImpact(human, animal) == 0.0);
        TestHelper.testDone();
    }

    /**
     * Creates some concepts manually, sets the overlaps and then reads them
     * again
     * 
     * Tests the fact that the overlaps are symmetric!
     * 
     */
    @Test
    public void testOverlapSet() {
        String description =
                "Test the storage of overlaps in the AbstractConceptDB";
        TestHelper.testStart(description);
        AbstractConceptDB<Concept> cb = new AbstractConceptDB<>();
        Concept animal = new Concept("animal", "x1");
        cb.addConcept(animal);
        Concept plant = new Concept("plant", "x2");
        cb.addConcept(plant);
        Concept human = new Concept("human", "x3");
        cb.addConcept(human);
        cb.setArea(animal, 0.30);
        cb.setArea(plant, 0.30);
        cb.setArea(human, 0.10);
        cb.setOverlap(animal, human, 0.1);
        double h_a_ovr = cb.getOverlap(human, animal);
        // TextUi.println("Human-animal overlap:" + h_a_ovr);
        Assert.assertTrue(h_a_ovr == 0.1);
        double a_h_ovr = cb.getOverlap(human, animal);
        Assert.assertTrue(a_h_ovr == 0.1);
        // TextUi.println("Animal-human overlap:" + a_h_ovr);
        double p_h_ovr = cb.getOverlap(plant, human);
        Assert.assertTrue(p_h_ovr == 0.0);
        // TextUi.println("plant-human overlap:" + p_h_ovr);
        double p_p_ovr = cb.getOverlap(plant, plant);
        Assert.assertTrue(p_p_ovr == 0.3);
        // TextUi.println("plant-plant overlap:" + p_p_ovr);
        TestHelper.testDone();
    }

}
