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
package org.xapagy.debug.storygenerator;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.autobiography.ABStory;

/**
 * A series of classes which generate RecordedStory objects. They are used to
 * package new tests.
 * 
 * @author Ladislau Boloni
 * Created on: Feb 14, 2013
 */
public class RsGenerator {

    /**
     * Creates a recorded story and fills in the events.
     * 
     * In the scene "direct" there is only one instance, the narrator. In the
     * scene "quoted", there are two instances
     * 
     * The narrator describes a series of series of reciprocal actions which
     * happen between the instances in the quoted scene
     * 
     * @return
     */
    public static RecordedStory generateNarratedReciprocal(String narrator,
            String instance1, String instance2, List<String> actionVerbs) {
        RecordedStory retval =
                new RecordedStory(RsTemplates.DIRECT, RsTemplates.QUOTED);
        // Generate the embedded story
        retval.getRsScene(RsTemplates.QUOTED).setInstanceLabels(instance1,
                instance2);
        ABStory storyEmbedded =
                StoryTemplates.templateReciprocalAction(actionVerbs.size(),
                        "I", "V", 0);
        storyEmbedded.subs("I", retval.getRsScene(RsTemplates.QUOTED)
                .getInstanceLabels());
        storyEmbedded.subs("V", actionVerbs);

        // Generate the narrated story
        retval.getRsScene(RsTemplates.DIRECT).setInstanceLabels(narrator);
        retval.addStory(RsTemplates.generateNarratedStory(retval, narrator,
                RsTemplates.QUOTED, storyEmbedded));
        return retval;
    }

    /**
     * Creates a recorded story and fills in the events.
     * 
     * There is a single scene "direct" with two instances. The two instances
     * perform a series of reciprocal actions.
     * 
     * @param sg
     * @return
     */
    public static RecordedStory generateReciprocal(String instance1,
            String instance2, List<String> verbs) {
        RecordedStory retval = new RecordedStory(RsTemplates.DIRECT);
        retval.getRsScene(RsTemplates.DIRECT).setInstanceLabels(instance1,
                instance2);
        retval.addStory(RsTemplates.generateReciprocalAction(instance1,
                instance2, verbs));
        return retval;
    }

    /**
     * Generates a recorded story with a single scene which has two instances,
     * connected with relations...
     * 
     * @param instance1
     * @param instance2
     * @param verbs
     * @param rels12
     * @param rels21
     * @return
     */
    public static RecordedStory generateReciprocalWithRelations(
            String instance1, String instance2, List<String> verbs,
            List<String> rels12, List<String> rels21) {
        RecordedStory rs = new RecordedStory(RsTemplates.DIRECT);
        rs.getRsScene(RsTemplates.DIRECT).setInstanceLabels(instance1,
                instance2);
        if (rels12 != null) {
            List<String> relCreations = new ArrayList<>();
            for (String rel : rels12) {
                relCreations.add("CreateRelation " + rel);
            }
            ABStory absRelationsFrom =
                    RsTemplates.generateOneWayAction(instance1, instance2,
                            relCreations);
            rs.addStory(absRelationsFrom);
        }
        // the relations from 2 to 1
        if (rels21 != null) {
            List<String> relCreations = new ArrayList<>();
            for (String rel : rels21) {
                relCreations.add("CreateRelation " + rel);
            }
            ABStory absRelationsFrom =
                    RsTemplates.generateOneWayAction(instance2, instance1,
                            relCreations);
            rs.addStory(absRelationsFrom);
        }
        // the actions
        ABStory absActions =
                RsTemplates.generateReciprocalAction(instance1, instance2,
                        verbs);
        rs.addStory(absActions);
        return rs;
    }

}
