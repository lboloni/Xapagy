/*
   This file is part of the Xapagy project
   Created on: Mar 12, 2014
 
   org.xapagy.agents.ChoiceSelector
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.parameters.Parameters;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * This class packages everything there is to know about choice selection -
 * automatic, text based, web-based
 * 
 * @author Ladislau Boloni
 * 
 */
public class ChoiceSelector implements Serializable {
    private static final long serialVersionUID = -514104262384762327L;
    private Agent agent;
    private Choice automaticChoice = null;
    private Choice interactiveChoice = null;
    /**
     * This choice allows us to pre-select a choice to be instantiated through
     * external means (such as the web interface). The null value means that
     * there is no pre-selected choice
     */
    private Choice preselectedChoice = null;

    public ChoiceSelector(Agent agent) {
        this.agent = agent;
    }

    /**
     * Interactively select a choice
     */
    public Choice interactiveChoiceSelector() {
        TextUi.printHeader("Entering interactive choice selector");
        if (preselectedChoice != null) {
            TextUi.println("There is a preselected choice:");
            TextUi.println(PrettyPrint.ppConcise(preselectedChoice, agent));
            boolean b = TextUi.confirm("Do you want to accept it?", true);
            if (b) {
                return preselectedChoice;
            }
            preselectedChoice = null;
        }
        if (automaticChoice != null) {
            TextUi.println("There is an automatic choice:");
            TextUi.println(PrettyPrint.ppConcise(automaticChoice, agent));
            boolean b = TextUi.confirm("Do you want to accept it?", true);
            if (b) {
                return automaticChoice;
            }
            automaticChoice = null;
        }
        // let us select:
        HeadlessComponents hlss = agent.getHeadlessComponents();
        List<Choice> choicesAll =
                hlss.getChoices(HeadlessComponents.comparatorDependentScore);
        // filter out the choices - for the time being, keep only continuations
        List<Choice> choices = new ArrayList<>();
        for (Choice choice : choicesAll) {
            if (choice.getChoiceType() == ChoiceType.CONTINUATION) {
                choices.add(choice);
            }
        }
        // print them in reverse order, for convenience
        for (int i = choices.size() - 1; i >= 0; i--) {
            Choice choice = choices.get(i);
            TextUi.println("[" + i + "]" + PrettyPrint.ppConcise(choice, agent));
        }
        TextUi.println("Choose the choice you want to instantiate, or <ENTER> for none.");
        int v = TextUi.inputInteger("Value", -1);
        if (v >= 0 && v < choices.size()) {
            interactiveChoice = choices.get(v);
            TextUi.println("Choice to be instantiated:"
                    + PrettyPrint.ppConcise(interactiveChoice, agent));
        } else {
            TextUi.println("You selected none!");
        }
        TextUi.printHeader("Leaving interactive choice selector");
        return interactiveChoice;
    }

    /**
     * Selects the next choice, or returns zero
     * 
     * @return
     */
    public Choice selectChoice() {
        //
        // Calculate the automatic choice
        //
        HeadlessComponents hlss = agent.getHeadlessComponents();
        Parameters p = agent.getParameters();
        double minimumChoiceStrengthToInstantiate =
                p.get("A_HLSM", "G_GENERAL",
                        "N_INTERNAL_CHOICE_THRESHOLD");
        boolean debugInteractiveChoiceSelector =
                p.getBoolean("A_DEBUG", "G_GENERAL",
                        "N_INTERACTIVE_CHOICE_SELECTOR");
        try {
            automaticChoice =
                    Collections.max(hlss.getChoices().values(),
                            HeadlessComponents.comparatorMoodScore);
        } catch (NoSuchElementException nsee) {
            automaticChoice = null;
        }
        if (automaticChoice != null
                && automaticChoice.getChoiceScore().getScoreMood() < minimumChoiceStrengthToInstantiate) {
            automaticChoice = null;
        }
        if (debugInteractiveChoiceSelector) {
            interactiveChoiceSelector();
        }
        Choice retval;
        if (interactiveChoice != null) {
            retval = interactiveChoice;
        } else if (preselectedChoice != null) {
            retval = preselectedChoice;
        } else {
            retval = automaticChoice;
        }
        interactiveChoice = null;
        preselectedChoice = null;
        automaticChoice = null;
        return retval;
    }
}
