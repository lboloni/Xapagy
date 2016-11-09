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
package org.xapagy.activity.hlsmaintenance;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import org.xapagy.activity.DiffusionActivity;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.headless_shadows.HlsCharacterization;
import org.xapagy.headless_shadows.StaticHls;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * A DA which maintains the collection of choices.
 * 
 * As of May 2014, there are basically three kinds of algorithms:
 * 
 * <ul>
 * <li>the characterization one (not link based)
 * <li>the continuation one which uses link predictors, followed by link based
 * and already instantiated based inhibitors - basically, the dependent score
 * <li>everything else - link based and uses the independent score
 * </ul>
 * 
 * @author Ladislau Boloni
 * Created on: Sep 7, 2011
 */
public class DaHlsmChoices extends DiffusionActivity {
    private static final long serialVersionUID = -3248033751779526335L;
    private double threshold = 0;
    private boolean enableContinuations;
    private boolean enableCharacterization;
    private boolean enableMissingAction;
    private boolean enableMissingRelation;
    private boolean enableStatic;
    

    public DaHlsmChoices(Agent agent, String name) {
        super(agent, name);
    }

    
    /* (non-Javadoc)
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        enableContinuations = getParameterString("enableContinuation").equals("true");
        enableCharacterization = getParameterString("enableCharacterization").equals("true");
        enableMissingAction = getParameterString("enableMissingAction").equals("true");
        enableMissingRelation = getParameterString("enableMissingRelation").equals("true");
        enableStatic = getParameterString("enableStatic").equals("true");
    }
    
    /**
     * Creates all the choices (provided that the preferences are not null), and
     * replaces the ones in the HeadlessComponents collection. In this respect,
     * this is an Sa, not a Da.
     * 
     * The continuations and characterizations have a specific function.
     * 
     */
    @Override
    protected void applyInner(double time) {
        HeadlessComponents hlss = agent.getHeadlessComponents();
        List<Choice> allChoices = new ArrayList<>();
        if (enableContinuations) {
            List<Choice> continuations = getScoredContinuations();
            allChoices.addAll(continuations);
        }
        // characterizations
        if (enableCharacterization) {
            List<Choice> characterizations = getScoredCharacterizations();
            allChoices.addAll(characterizations);
        }
        // missing actions
        if (enableMissingAction) {
            List<Choice> missingActions =
                    getScoredChoices(ChoiceType.MISSING_ACTION, threshold);
            allChoices.addAll(missingActions);
        }
        // missing relations
        if (enableMissingRelation) {
            List<Choice> missingRelations =
                    getScoredChoices(ChoiceType.MISSING_RELATION, threshold);
            allChoices.addAll(missingRelations);
        }
        if (enableStatic) {
            List<Choice> staticChoices = createStaticChoices();
            allChoices.addAll(staticChoices);
        }
        // for each choice, calculate the mood score
        for (Choice choice : allChoices) {
            ChoiceScore choiceScore = choice.getChoiceScore();
            if (choiceScore != null) {
                choiceScore.getScoreMood();
            }
        }
        hlss.replaceChoices(allChoices);
    }

    /**
     * Create the static type choices
     * 
     * @return
     */
    public List<Choice> createStaticChoices() {
        List<Choice> retval = new ArrayList<>();
        // first, simplest model, create a new choice for each
        HeadlessComponents hc = agent.getHeadlessComponents();
        for (StaticHls shls : hc.getStaticHlss()) {
            Choice choice = new Choice(agent, shls);
            retval.add(choice);
        }
        return retval;
    }

    /**
     * Returns the list of possible characterizations, sorted by strength
     * 
     * The characterizations are different from the other, link based choices.
     * This function simply calls getScoredCharacterizations for every instance,
     * and puts them together in a list
     * 
     * @return
     */
    public List<Choice> getScoredCharacterizations() {
        List<Choice> retval = new ArrayList<>();
        Focus fc = agent.getFocus();
        for (Instance instance : fc.getInstanceList(EnergyColors.FOCUS_INSTANCE)) {
            List<Choice> list = getScoredCharacterizationsForInstance(instance);
            retval.addAll(list);
        }
        return retval;
    }

    /**
     * Returns the list of scored characterizations for a particular instance
     * 
     * FIXME: document how this is actually done
     * 
     * @param instance
     * @return
     */
    public List<Choice>
            getScoredCharacterizationsForInstance(Instance instance) {
        Shadows sf = agent.getShadows();
        double shadowSum = 0;
        List<Instance> members =
                sf.getMembers(instance, EnergyColors.SHI_GENERIC);
        for (Instance s : members) {
            shadowSum += sf.getSalience(instance, s, EnergyColors.SHI_GENERIC);
        }
        double sum =
                agent.getFocus().getSalience(instance, EnergyColors.FOCUS_INSTANCE)
                        * shadowSum;
        // create a concept overlay which integrates all the concepts in the
        // shadow
        ConceptOverlay coShadow = new ConceptOverlay(agent);
        for (Instance si : sf.getMembers(instance, EnergyColors.SHI_GENERIC)) {
            double valAbsSi =
                    sf.getSalience(instance, si, EnergyColors.SHI_GENERIC);
            coShadow.addOverlay(si.getConcepts(), valAbsSi / sum);
        }
        // generate the concept overlays
        List<ConceptOverlay> candidates = new ArrayList<>();
        for (SimpleEntry<Concept, Double> entry : coShadow.getList()) {
            ConceptOverlay coAdded = new ConceptOverlay(agent);
            coAdded.addFullEnergy(entry.getKey());
            candidates.add(coAdded);
        }
        // FIXME: we can add some other candidates, for instance, based on words
        // ok, we have all the candidates, now score them
        List<Choice> retval = new ArrayList<>();
        for (ConceptOverlay coAdded : candidates) {
            HlsCharacterization hlsc =
                    new HlsCharacterization(instance, coAdded);
            CharacterizationScore cs =
                    new CharacterizationScore(agent, instance, coAdded,
                            coShadow);
            Choice choice = new Choice(agent, hlsc, cs);
            retval.add(choice);
        }
        return retval;
    }

    /**
     * Returns the scored choices where the independent score is larger than the
     * threshold (this function can be called externally for debugging)
     * 
     * Creates the possible choices for all the existing HLSs scored with the
     * ChoiceType. The calculation of the scoring is done by the ChoiceScore
     * object in the Choice. Ignores those choices where the score is smaller
     * than the threshold.
     * 
     * @param choiceType
     *            -the
     * @return
     */
    public List<Choice> getScoredChoices(ChoiceType choiceType, double thr) {
        HeadlessComponents hlc = agent.getHeadlessComponents();
        List<Choice> retval = new ArrayList<>();
        for (Hls hls : hlc.getHlss()) {
            Choice choice = new Choice(agent, hls, choiceType);
            // Debug: turn threashold
            // if (choice.getChoiceScore().getScoreIndependent() > thr) {
            retval.add(choice);
            // }
        }
        return retval;
    }

    /**
     * Returns the list of continuation choices. It differs from the other
     * choice calculations in that it calculates the cross inhibitions and the
     * continuation fired shadow inhibitions calculates the cross choice.
     * 
     * @return
     */
    private List<Choice> getScoredContinuations() {
        // create a list of choices
        List<Choice> listContVal =
                getScoredChoices(ChoiceType.CONTINUATION, 0.0);
        // apply the inhibiting effect of the back links
        // this can be only calculating after the other ones are done
        List<Choice> retval = new ArrayList<>();
        for (Choice choice : listContVal) {
            ChoiceScore choiceScore = choice.getChoiceScore();
            choiceScore.calculateContinuationCrossInhibition(listContVal);
            choiceScore.calculateContinuationFiredShadowInhibition();
            // if (choiceScore.getScoreDependent() > 0) {
            retval.add(choice);
            // }
        }
        return retval;
    }


    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaHlsmChoices");
        fmt.indent();
        fmt.is("threshold", threshold);
        fmt.is("enableContinuations", enableContinuations);
        fmt.is("enableCharacterization", enableCharacterization);
        fmt.is("enableMissingAction", enableMissingAction);
        fmt.is("enableMissingRelation", enableMissingRelation);
        fmt.is("enableStatic", enableStatic);
        fmt.deindent();
    }



}
