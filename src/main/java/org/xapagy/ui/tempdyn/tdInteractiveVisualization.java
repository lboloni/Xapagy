/*
   This file is part of the Xapagy project
   Created on: Sep 7, 2013
 
   org.xapagy.ui.tempdyn.tdInteractiveVisualization
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PpChoice;

/**
 * A TextUi based, interactive tool to choose visualization models. The
 * objective of this tool is to generate plots for the papers and technical
 * reports.
 * 
 * @author Ladislau Boloni
 * 
 */
public class tdInteractiveVisualization {

    private static final String MENU_CHOICES = "Choices";
    private static final String MENU_EXIT = "Exit";
    private static final String MENU_INSTANCE_FOCUS_MEMORY_SHADOWS =
            "Instance focus / memory / shadows";
    private static final String MENU_LINKS_BETWEEN_VIS =
            "Links between two VIs";
    private static final String MENU_LINKS_FROM_A_VI = "Links from a VI";
    private static final String MENU_VI_FOCUS_MEMORY_SHADOWS =
            "VI focus / memory / shadows";

    /**
     * Interactive choosing of the energy color for shadows
     * 
     * @return
     */
    private static String chooseEnergyColor(Agent agent) {
        List<String> menuItems = new ArrayList<>();
        for (String ec : agent.getEnergyColors().getAllEnergies()) {
            menuItems.add(ec);
        }
        String choice =
                TextUi.menu(menuItems, EnergyColors.SHV_GENERIC.toString(),
                        "Choose the energy color");
        return choice;
    }

    private Agent agent;

    private tdDataBase tdb;

    public tdInteractiveVisualization(tdDataBase tdb, Agent agent) {
        this.tdb = tdb;
        this.agent = agent;
    }

    /**
     * Utility function for choosing a tdComponent of a VI
     * 
     * @return
     */
    private tdComponent chooseVi(String message, Agent agent) {
        String ec = tdInteractiveVisualization.chooseEnergyColor(agent);
        List<String> menuItems = new ArrayList<>();
        for (tdComponent tdc : tdb.getFocusVis()) {
            int count =
                    tdb.getShadowComponents(tdc.getIdentifier(), ec, -1).size();
            menuItems
                    .add(tdc.getLastPrettyPrint() + " - " + count + " shadows");
        }
        menuItems.add("all");
        menuItems.add("none");
        int choice = TextUi.menu(menuItems, 0, message);
        if (choice == menuItems.size() - 1) {
            TextUi.println("You chose none, going back.");
            return null;
        }
        if (choice == menuItems.size() - 2) {
            TextUi.println("You chose all, can't do it, going back.");
            return null;
        }
        tdComponent selected = tdb.getFocusVis().get(choice);
        String identifier = selected.getIdentifier();
        VerbInstance vi =
                agent.getAutobiographicalMemory().getVerbInstance(identifier);
        TextUi.println(vi);
        return selected;
    }

    /**
     * The choice submenu
     */
    private void menuChoices() {
        List<String> menuItems = new ArrayList<>();
        for (tdComponent tdc : tdb.getChoices()) {
            Choice choice = tdc.getChoice();
            String menuitem = PpChoice.ppConcise(choice, agent);
            menuItems.add(menuitem);
        }
        int result =
                TextUi.menu(menuItems, 0,
                        "Choose the choice you want to visualize: ");
        tdComponent tdcSel = tdb.getChoices().get(result);
        //
        // Visualizes the metrics of the selected choice on the terminal for the
        // time range
        //
        Formatter fmt = new Formatter();
        for (double time = tdb.getTimeStart() + 0.001; time <= tdb.getTimeEnd() + 0.01; time =
                time + 1.0) {
            fmt.is(Formatter.fmt(time),
                    tdb.getChoiceScoreDependent(tdcSel.getIdentifier(), time)
                            + " - "
                            + tdb.getChoiceScoreIndependent(
                                    tdcSel.getIdentifier(), time)
                            + " "
                            + tdb.getChoiceScoreMood(tdcSel.getIdentifier(),
                                    time));
        }
        TextUi.println(fmt.toString());
        List<Double> index = GraphEvolution.getTimelineDaSteps(tdb, agent, -1);
        double scale =
                TextUi.inputDouble("What scale should we plot the choices? ",
                        0.003);
        GraphEvolution.graphChoiceEvolution(tdcSel, tdb, agent, index, scale);
        //
        // Often, the choice started later, so the graph has a big empty space
        // at
        // the beginning. So ask if we want to replot from a different
        // timepoints
        //
        if (!TextUi.confirm("Replot with different start?", true)) {
            return;
        }
        double startFrom =
                TextUi.inputDouble("Start from: ", tdb.getTimeStart());
        index = GraphEvolution.getTimelineDaSteps(tdb, agent, startFrom);
        GraphEvolution.graphChoiceEvolution(tdcSel, tdb, agent, index, scale);
    }

    /**
     * In this submenu we can first choose whether which instance are we
     * visualizing, then we plot it
     */
    private void menuInstanceFocusMemoryShadows() {
        List<String> menuItems = new ArrayList<>();
        // FIXME: we are choosing based on SHADOW_GENERIC
        for (tdComponent tdc : tdb.getFocusInstances()) {
            int count =
                    tdb.getShadowComponents(tdc.getIdentifier(),
                            EnergyColors.SHI_GENERIC, -1).size();
            menuItems
                    .add(tdc.getLastPrettyPrint() + " - " + count + " shadows");
        }
        menuItems.add("all");
        menuItems.add("none");
        int choice =
                TextUi.menu(menuItems, 0,
                        "Choose the instance whose shadows you want to visualize: ");
        if (choice == menuItems.size() - 1) {
            TextUi.println("You chose none, going back.");
            return;
        }
        if (choice == menuItems.size() - 2) {
            TextUi.println("You chose all, can't do it, going back.");
            return;
        }
        tdComponent selected = tdb.getFocusInstances().get(choice);
        String identifier = selected.getIdentifier();
        Instance instance =
                agent.getAutobiographicalMemory().getInstance(identifier);
        TextUi.println(instance);
        List<Double> index = GraphEvolution.getTimelineDaSteps(tdb, agent, -1);
        int shadows = TextUi.inputInteger("How many shadows to plot? ", 0);
        double scale = 1.0;
        if (shadows != 0) {
            scale =
                    TextUi.inputDouble(
                            "What scale should we plot the shadows? ", 1.0);
        }
        GraphEvolutionDescriptor ged =
                GraphEvolutionDescriptor.createKitchenSink(agent);
        GraphEvolution.graphFMSComposite(selected, tdb,
                agent, index, shadows, scale, ged);
        //
        // Now ask again for the index
        //
        if (!TextUi.confirm("Replot with different start?", true)) {
            return;
        }
        double startFrom =
                TextUi.inputDouble("Start from: ", tdb.getTimeStart());
        index = GraphEvolution.getTimelineDaSteps(tdb, agent, startFrom);
        GraphEvolution.graphFMSComposite(selected, tdb,
                agent, index, shadows, scale, ged);
    }

    /**
     * In this submenu we are generating plots for the evolution and creation of
     * the links
     */
    private void menuLinksBetweenVis() {
        tdComponent fromVi =
                chooseVi("Choose the VI from which the link will start: ", agent);
        tdComponent toVi =
                chooseVi("Choose the VI to which the link will go: ", agent);
        List<Double> index = GraphEvolution.getTimelineDaSteps(tdb, agent, -1);
        GraphEvolution.graphLinksBetweenVis(fromVi, toVi, tdb, agent, index);
        //
        // Now ask again for the index
        //
        if (!TextUi.confirm("Replot with different start?", true)) {
            return;
        }
        double startFrom =
                TextUi.inputDouble("Start from: ", tdb.getTimeStart());
        index = GraphEvolution.getTimelineDaSteps(tdb, agent, startFrom);
        GraphEvolution.graphLinksBetweenVis(fromVi, toVi, tdb, agent, index);

    }

    /**
     * In this submenu we are generating a plot for all the links of a certain
     * type which start from a given VI
     */
    private void menuLinksFromAVi() {
        tdComponent fromVi =
                chooseVi("Choose the VI from which the link will start: ", agent);
        List<String> menuItems = new ArrayList<>();
        for (String linkName : agent.getLinks().getLinkTypeNames()) {
            menuItems.add(linkName);
        }
        menuItems.add("all");
        String linkType =
                TextUi.menu(menuItems, menuItems.get(0),
                        "Choose the link type:");
        if (linkType == "all") {
            linkType = null;
        }
        List<Double> index = GraphEvolution.getTimelineDaSteps(tdb, agent, -1);
        GraphEvolution
                .graphLinksFromAVi(fromVi, linkType, tdb, agent, index);
        //
        // Now ask again for the index
        //
        if (!TextUi.confirm("Replot with different start?", true)) {
            return;
        }
        double startFrom =
                TextUi.inputDouble("Start from: ", tdb.getTimeStart());
        index = GraphEvolution.getTimelineDaSteps(tdb, agent, startFrom);
        GraphEvolution
                .graphLinksFromAVi(fromVi, linkType, tdb, agent, index);

    }

    /**
     * In this submenu we are interactively choosing what VIs shadows we
     * generate
     */
    private void menuViFocusMemoryShadows() {
        // EnergyColor ec = tdInteractiveVisualization.chooseEnergyColor();
        tdComponent selected = chooseVi("Choose the focus VI to visualize: ", agent);
        List<Double> index = GraphEvolution.getTimelineDaSteps(tdb, agent, -1);
        int shadows = TextUi.inputInteger("How many shadows to plot? ", 0);

        double scale = 1.0;
        if (shadows != 0) {
            TextUi.inputDouble("What scale should we plot the shadows? ", 1.0);
        }
        GraphEvolutionDescriptor ged =
                GraphEvolutionDescriptor.createKitchenSink(agent);
        GraphEvolution.graphFMSComposite(selected, tdb,
                agent, index, shadows, scale, ged);
        //
        // Now ask again for the index
        //
        if (!TextUi.confirm("Replot with different start?", true)) {
            return;
        }
        double startFrom =
                TextUi.inputDouble("Start from: ", tdb.getTimeStart());
        index = GraphEvolution.getTimelineDaSteps(tdb, agent, startFrom);
        GraphEvolution.graphFMSComposite(selected, tdb,
                agent, index, shadows, scale, ged);
    }

    public void visualize() {
        while (true) {
            List<String> menu = new ArrayList<>();
            menu.add(tdInteractiveVisualization.MENU_INSTANCE_FOCUS_MEMORY_SHADOWS);
            menu.add(tdInteractiveVisualization.MENU_VI_FOCUS_MEMORY_SHADOWS);
            menu.add(tdInteractiveVisualization.MENU_CHOICES);
            menu.add(tdInteractiveVisualization.MENU_LINKS_BETWEEN_VIS);
            menu.add(tdInteractiveVisualization.MENU_LINKS_FROM_A_VI);
            menu.add(tdInteractiveVisualization.MENU_EXIT);
            String value =
                    TextUi.menu(menu, tdInteractiveVisualization.MENU_EXIT,
                            "Choose: ");
            switch (value) {
            case MENU_INSTANCE_FOCUS_MEMORY_SHADOWS:
                menuInstanceFocusMemoryShadows();
                break;
            case MENU_VI_FOCUS_MEMORY_SHADOWS:
                menuViFocusMemoryShadows();
                break;
            case MENU_CHOICES:
                menuChoices();
                break;
            case MENU_LINKS_BETWEEN_VIS:
                menuLinksBetweenVis();
                break;
            case MENU_LINKS_FROM_A_VI:
                menuLinksFromAVi();
                break;
            case MENU_EXIT:
                TextUi.println("Ok, exiting.");
                System.exit(0);
            }
        }
    }
}