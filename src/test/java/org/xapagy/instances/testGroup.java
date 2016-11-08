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
