/*
   This file is part of the Xapagy project
   Created on: Jul 21, 2011
 
   org.xapagy.links.testGroup
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.instances;

import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * 
 */
public class testGroup {

    /**
     * Tests the joining of the group and group membership
     */
    @Test
    public void testGroupMembership() {
        String description =
                "Creation and the removal of the ownership relation.";
        TestHelper.testStart(description);
        Runner r = new Runner("Core");
        r.exec("$CreateScene #Troy CloseOthers With Instances 'Achilles', 'Hector', group 'AH'");
        VerbInstance vi = r.exac("'Achilles' / joins-group / 'AH'.");
        Instance instAchilles = vi.getSubject();
        Instance instGroup = vi.getObject();
        Instance instHector =
                r.exac("'Hector' / joins-group / 'AH'.").getSubject();
        String relationName = Hardwired.VR_MEMBER_OF_GROUP;
        r.ah.inRelation(relationName, instAchilles, instGroup);
        r.ah.inRelation(relationName, instHector, instGroup);
        r.exec("'Hector' / leaves-group / 'AH'.");
        r.ah.notInRelation(relationName, instHector, instGroup);
        TestHelper.testDone();
    }

}
