/*
   This file is part of the Xapagy project
   Created on: Jan 22, 2014
 
   org.xapagy.reference.rrContext
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.reference;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwRrContext;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiReferenceType;
import org.xapagy.xapi.reference.XrefDirect;
import org.xapagy.xapi.reference.XrefGroup;
import org.xapagy.xapi.reference.XrefRelational;
import org.xapagy.xapi.reference.XrefToInstance;
import org.xapagy.xapi.reference.XrefToVo;
import org.xapagy.xapi.reference.XrefVerb;

/**
 * This class encapsulates a context in which a given reference is evaluated.
 * The point here is that it is not only the reference CO matters but the
 * specific situation in which the reference must be done
 * 
 * @author Ladislau Boloni
 * 
 */
public class rrContext {

    /**
     * Creates a direct reference
     * 
     * @return
     */
    public static rrContext createDirectReference(Agent agent,
            ConceptOverlay coDirect, VerbOverlay verbsInVi, ViPart partInVi,
            Instance scene, VerbInstance viInquitParent) {
        rrContext rrc = new rrContext();
        rrc.agent = agent;
        rrc.referenceType = XapiReferenceType.REF_DIRECT;
        rrc.coDirect = coDirect;
        rrc.verbsInVi = verbsInVi;
        rrc.partInVi = partInVi;
        rrc.scene = scene;
        rrc.viInquitParent = viInquitParent;
        return rrc;
    }

    /**
     * Create an rrContext from a Xapi reference
     * 
     * @param agent
     * @param reference
     * @param viEmcompassing
     * @param partInVi
     * @param scene
     * @param viInquitParent
     */
    public static rrContext createFromXapiReference(Agent agent,
            XapiReference reference, VerbOverlay verbsInVi, ViPart partInVi,
            Instance scene, VerbInstance viInquitParent) {
        rrContext rrc = new rrContext();
        rrc.agent = agent;
        rrc.reference = reference;
        rrc.referenceType = reference.getType();
        switch (rrc.referenceType) {
        case REF_DIRECT: {
            XrefDirect xrefdirect = (XrefDirect) rrc.reference;
            rrc.coDirect = xrefdirect.getCo();
            break;
        }
        case REF_GROUP: {
            XrefGroup xrefgroup = (XrefGroup) rrc.reference;
            rrc.groupMembers = new ArrayList<>();
            for (XrefToInstance xri : xrefgroup.getMembers()) {
                rrContext member =
                        rrContext.createFromXapiReference(agent, xri,
                                verbsInVi, partInVi, scene, viInquitParent);
                rrc.groupMembers.add(member);
            }
            break;
        }
        case REF_RELATIONAL: {
            // extract the specification of the relation addressing from the
            // Xapi
            // parse: this is essentially a list of COs and VOs interleaved
            XrefRelational xrefrelational = (XrefRelational) rrc.getReference();
            rrc.relationChain = new ArrayList<>();
            for (XrefToVo xrv : xrefrelational.getRelations()) {
                VerbOverlay vo = ((XrefVerb) xrv).getVo();
                rrc.relationChain.add(vo);
            }
            rrc.relationCOs = new ArrayList<>();
            for (XrefToInstance xri : xrefrelational.getInstances()) {
                ConceptOverlay co = ((XrefDirect) xri).getCo();
                rrc.relationCOs.add(co);
            }
            break;
        }
        default:
            break;
        }
        rrc.verbsInVi = verbsInVi;
        rrc.partInVi = partInVi;
        rrc.scene = scene;
        rrc.viInquitParent = viInquitParent;
        return rrc;
    }

    /**
     * Creates a direct reference which is part of a relational reference.
     * 
     * FIXME: in the current implementation the only change is the reference CO
     * and the scene, this might not be always true
     * 
     * @return
     */
    public static rrContext createRelationalReferenceStep(rrContext rrcOld,
            ConceptOverlay coRef, Instance scene) {
        rrContext rrc = new rrContext();
        rrc.agent = rrcOld.agent;
        rrc.referenceType = XapiReferenceType.REF_DIRECT;
        rrc.coDirect = coRef;
        rrc.verbsInVi = rrcOld.verbsInVi;
        rrc.partInVi = rrcOld.partInVi;
        rrc.scene = scene;
        rrc.viInquitParent = rrcOld.viInquitParent;
        return rrc;
    }

    /**
     * The agent which evaluates the reference
     */
    private Agent agent;
    /**
     * The direct overlay in the case of direct addressing
     */
    private ConceptOverlay coDirect;
    /**
     * For a group reference, we have a list of contexts
     */
    private List<rrContext> groupMembers;
    /**
     * The part which the resolved instance will play in the encompassing VI.
     */
    private ViPart partInVi;
    /**
     * The given reference as evaluated by Xapi. Due to the way the parsing is
     * done, this class actually replicates some of the context stuff here, as
     * it has links to parents etc
     */
    private XapiReference reference = null;
    /**
     * The reference type. It can come from the XapiReference or direction
     */
    private XapiReferenceType referenceType;
    /**
     * The list of verb overlays which specify the relational addressing
     * 
     */
    private List<VerbOverlay> relationChain;
    /**
     * The list of concept overlays which specify the relational addressing
     * 
     */
    private List<ConceptOverlay> relationCOs;
    /**
     * The scene in which the lookup should be done
     */
    private Instance scene;
    /**
     * The verb overlay which is the predicate of this
     */
    private VerbOverlay verbsInVi;

    /**
     * The inquit parent of the encompassible vi (for resolving Quote type VIs)
     */
    private VerbInstance viInquitParent;

    /**
     * Empty constructor, for factory functions
     */
    private rrContext() {
    }

    public Agent getAgent() {
        return agent;
    }

    public ConceptOverlay getCoDirect() {
        return coDirect;
    }

    public List<rrContext> getGroupMembers() {
        return groupMembers;
    }

    public ViPart getPartInVi() {
        return partInVi;
    }

    public XapiReference getReference() {
        return reference;
    }

    public XapiReferenceType getReferenceType() {
        return referenceType;
    }

    public List<VerbOverlay> getRelationChain() {
        return relationChain;
    }

    public List<ConceptOverlay> getRelationCOs() {
        return relationCOs;
    }

    public Instance getScene() {
        return scene;
    }

    /**
     * @return the verbs
     */
    public VerbOverlay getVerbsInVi() {
        return verbsInVi;
    }

    public VerbInstance getViInquitParent() {
        return viInquitParent;
    }

    /**
     * Identifies whether this is a pronoun direct reference
     * 
     * For the time being it only works for I
     * 
     */
    public boolean isPronoun() {
        if (Hardwired.contains(agent, coDirect, Hardwired.C_I)) {
            return true;
        }
        return false;
    }

    /**
     * Printing out an rrContext object - falling back on pwRrContext
     */
    @Override
    public String toString() {
        TwFormatter fmt = new TwFormatter();
        return xwRrContext.xwDetailed(fmt, this, agent);
    }

}
