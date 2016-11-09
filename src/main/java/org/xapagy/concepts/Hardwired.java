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
package org.xapagy.concepts;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Oct 20, 2011
 */
public class Hardwired {

    /**
     * The possible relations between scenes. There is a limited set of these,
     * with fixed semantics, thus we will hardwire them here.
     * 
     * @author Ladislau Boloni
     * 
     */
    public enum SceneRelation {
        FICTIONAL_FUTURE, NONE, SUCCESSOR, VIEW
    }

    // the verbs which require an S-V-Adj structure
    public static List<String> adjectiveVerbs = new ArrayList<>();
    //
    // concepts
    //
    public static final String C_EXPLICIT = "c_explicit";
    public static final String C_GROUP = "c_group";
    public static final String C_I = "c_i";
    public static final String C_INEXISTENT = "c_inexistent";
    public static final String C_MANY = "c_many";
    public static final String C_PAIR = "c_pair";
    public static final String C_SCENE = "c_scene";
    public static final String C_THING = "c_thing";
    public static final String C_THOU = "c_thou";
    public static final String C_WE = "c_we";
    public static final String C_WH = "c_wh";
    public static final String C_WHAT = "c_what";
    public static final String C_YOU = "c_you";
    public static final String CC_PROPERNAME = "cc_proper_name";
    public static final String CONCEPT_PREFIX_NEGATION = "not-";
    // these are labels used to denote scene relations in internal code (eg in
    // macros, in RecordedStories etc.
    public static final String LABEL_SCENEREL_FICTIONAL_FUTURE =
            "fictional-future";
    public static final String LABEL_SCENEREL_NONE = "none";
    public static final String LABEL_SCENEREL_SUCCESSOR = "successor";
    public static final String LABEL_SCENEREL_VIEW = "view";
    //
    // Verbs
    //
    public static final String V_COMMUNICATION = "v_communication";
    public static final String V_DOES_NOTHING = "v_does_nothing";
    public static final String VM_ACHIEVE = "vm_achieve";
    public static final String VM_ACTION_MARKER = "vm_action_marker";
    public static final String VM_ACTS_LIKE = "vm_acts_like";
    public static final String VM_CHANGES = "vm_changes";
    public static final String VM_CLONE_SCENE = "vm_clone_scene";
    public static final String VM_CREATE_CHAIN_IDENTITY =
            "vm_create_chain_identity";
    public static final String VM_CREATE_INSTANCE = "vm_create_instance";
    public static final String VM_CREATE_ONE_SOURCE_RELATION =
            "vm_create_one_source_relation";
    public static final String VM_CREATE_RELATION = "vm_create_relation";
    public static final String VM_CREATE_SUCCESSION_IDENTITY =
            "vm_create_succession_identity";
    public static final String VM_FORGET = "vm_forget";
    public static final String VM_IS_A = "vm_is_a";
    public static final String VM_NARRATE = "vm_narrate";
    public static final String VM_RECALL = "vm_recall";

    public static final String VM_RELATION_MARKER = "vm_relation_marker";
    public static final String VM_REMOVE_RELATION = "vm_remove_relation";
    public static final String VM_SCENE_IS_ONLY = "vm_scene_is_only";
    public static final String VM_SUCCESSOR = "vm_successor";
    public static final String VM_THUS = "vm_thus";
    public static final String VM_TRANSFORMS = "vm_transforms";
    public static final String VM_USES = "vm_uses";
    public static final String VM_WHETHER = "vm_whether";
    public static final String VM_WHY = "vm_why";
    public static final String VMC_RELATION = "vmc_relation";

    public static final String VR_HOLDS = "vr_holds";
    public static final String VR_IDENTITY = "vr_identity";
    public static final String VR_IN_SCENE = "vr_in_scene";
    public static final String VR_IS_PART_OF = "vr_is_part_of";
    public static final String VR_LEFT = "vr_left";
    public static final String VR_LEGALLY_OWNS = "vr_legally_owns";
    public static final String VR_MEMBER_OF_GROUP = "vr_member_of_group";
    public static final String VR_OBJECT_IS_SCENE = "vr_object_is_scene";
    public static final String VR_PART_OF_SCENE = "vr_part_of_scene";
    public static final String VR_RIGHT = "vr_Right";
    public static final String VR_SCENE_FICTIONAL_FUTURE =
            "vr_scene_fictional_future";
    public static final String VR_SCENE_SUCCESSION = "vr_scene_succession";
    public static final String VR_SCENE_VIEW = "vr_scene_view";
    public static final String VR_SUBJECT_IS_SCENE = "vr_subject_is_scene";

    //
    // The words which are used to create scene relations.
    //
    public static final String W_SCENEREL_FICTIONAL_FUTURE =
            "has-fictional-future";
    public static final String W_SCENEREL_SUCCESSOR = "has-successor";
    public static final String W_SCENEREL_VIEW = "has-view";

    //
    // VerbOverlay words with specific purpose
    //
    public static final String VO_ACTION_VERB = "ActionVerb";
    public static final String VO_CREATE_ONE_SOURCE_RELATION =
            "CreateOneSourceRelation";
    public static final String VO_CREATE_RELATION = "CreateRelation";
    public static final String VO_REMOVE_RELATION = "RemoveRelation";
    public static final String VO_THUS = "thus";
    
    //
    // Special purpose labels: start with ## (instead of just one #)
    //
    public static final String LABEL_SUMMARIZATION = "##Summarization";
    
    // Initialize the list of adjective verbs
    static {
        Hardwired.adjectiveVerbs.add(Hardwired.VM_IS_A);
        Hardwired.adjectiveVerbs.add(Hardwired.VM_CHANGES);
        Hardwired.adjectiveVerbs.add(Hardwired.VM_TRANSFORMS);
        Hardwired.adjectiveVerbs.add(Hardwired.VM_ACTS_LIKE);
        Hardwired.adjectiveVerbs.add(Hardwired.VM_CREATE_CHAIN_IDENTITY);
        Hardwired.adjectiveVerbs.add(Hardwired.VM_CREATE_SUCCESSION_IDENTITY);
        Hardwired.adjectiveVerbs.add(Hardwired.VM_CLONE_SCENE);
    }

    /**
     * Function for verifying that a CO explicitly contains a specific concept
     * 
     * @param agent
     * @param co
     * @param conceptName
     * @return
     */
    public static boolean contains(Agent agent, ConceptOverlay co,
            String conceptName) {
        Concept c = agent.getConceptDB().getConcept(conceptName);
        // double energy = co.getExplicitEnergy(c);
        double energy = co.getEnergy(c);
        return energy > 0.0;
    }

    /**
     * Function for verifying that a VO contains a specific verb
     * 
     * @param agent
     * @param vo
     * @param verbName
     * @return
     */
    public static boolean
            contains(Agent agent, VerbOverlay vo, String verbName) {
        Verb v = agent.getVerbDB().getConcept(verbName);
        double energy = vo.getEnergy(v);
        return energy > 0.0;
    }

    /**
     * Returns true if the VO contains any of the specified verbs
     * 
     * @param agent
     * @param vo
     * @param verbs
     * @return
     */
    public static boolean containsAny(Agent agent, VerbOverlay vo,
            List<String> verbNames) {
        for (String verbName : verbNames) {
            if (Hardwired.contains(agent, vo, verbName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates an identity relation
     * 
     * @param agent
     * @return
     */
    public static VerbOverlay getIdentityVO(Agent agent) {
        VerbOverlay vo =
                VerbOverlay.createVO(agent, Hardwired.VR_IDENTITY,
                        Hardwired.VM_RELATION_MARKER);
        return vo;
    }

    /**
     * Returns the word which creates a specific scene relation (or null, if it
     * is none).
     * 
     * @param sr
     * @return
     */
    public static String getSceneRelationWord(SceneRelation sr) {
        switch (sr) {
        case FICTIONAL_FUTURE:
            return Hardwired.W_SCENEREL_FICTIONAL_FUTURE;
        case SUCCESSOR:
            return Hardwired.W_SCENEREL_SUCCESSOR;
        case VIEW:
            return Hardwired.W_SCENEREL_VIEW;
        case NONE:
        default:
            return null;
        }
    }

    /**
     * Parses a scene relation label into a SceneRelation enumeration
     * 
     * @param sceneRelationLabel
     * @return
     */
    public static SceneRelation parseSceneRelationLabel(
            String sceneRelationLabel) {
        switch (sceneRelationLabel) {
        case Hardwired.LABEL_SCENEREL_NONE:
            return SceneRelation.NONE;
        case Hardwired.LABEL_SCENEREL_SUCCESSOR:
            return SceneRelation.SUCCESSOR;
        case Hardwired.LABEL_SCENEREL_VIEW:
            return SceneRelation.VIEW;
        case Hardwired.LABEL_SCENEREL_FICTIONAL_FUTURE:
            return SceneRelation.FICTIONAL_FUTURE;
        default:
            TextUi.abort("parseSceneRelationLabel: unknown scene relation label: "
                    + sceneRelationLabel
                    + "\n Allowed ones: none, successor, view, fictional-future");
            return null;
        }
    }

    /**
     * Returns a new verb overlay from which the listed set of verbs had been
     * removed
     * 
     * @param agent
     * @param vo
     * @param verbNames
     * @return
     */
    public static VerbOverlay scrapeVerbsFromVO(Agent agent, VerbOverlay vo,
            String... verbNames) {
        VerbOverlay retval = new VerbOverlay(agent);
        retval.addOverlay(vo);
        for (String verbName : verbNames) {
            Verb verb = agent.getVerbDB().getConcept(verbName);
            retval.scrapeEnergy(verb);
        }
        return retval;
    }

    public static final String LINK_ANSWER = "Answer";
    public static final String LINK_COINCIDENCE = "Coincidence";
    public static final String LINK_IR_CONTEXT = "IRContext";
    public static final String LINK_IR_CONTEXT_IMPLICATION = "IRContextImplication";
    public static final String LINK_PREDECESSOR = "Predecessor";
    public static final String LINK_QUESTION = "Question";
    public static final String LINK_SUCCESSOR = "Successor";
    public static final String LINK_SUMMARIZATION_BEGIN = "Summarization_Begin";
    public static final String LINK_SUMMARIZATION_BODY = "Summarization_Body";
    public static final String LINK_SUMMARIZATION_CLOSE = "Summarization_Close";
    public static final String LINK_ELABORATION_BEGIN = "Elaboration_Begin";
    public static final String LINK_ELABORATION_BODY = "Elaboration_Body";
    public static final String LINK_ELABORATION_CLOSE = "Elaboration_Close";
    /**
     * The name of the DaComposite triggered for focus maintenance
     */
    public static final String DA_FOCUS_MAINTENANCE = "FocusMaintenance";
    /**
     * The name of the DaComposite triggered for shadow maintenance
     */
    public static final String DA_SHADOW_MAINTENANCE = "ShadowMaintenance";
    /**
     * The name of the DaComposite triggered for HLS maintenance
     */
    public static final String DA_HLS_MAINTENANCE = "HlsMaintenance";
    /**
     * The name of the SaComposite triggered for VIs before the VI verbs
     */
    public static final String SA_BEFORE_VI = "BeforeVi";
    /**
     * The name of the SaComposite triggered for VIs after the VI verbs
     */
    public static final String SA_AFTER_VI = "AfterVi";
}
