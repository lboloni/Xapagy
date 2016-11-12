/*
   This file is part of the Xapagy project
   Created on: Jan 29, 2013
 
   org.xapagy.concepts.TestDomain
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.DomainGenerator;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsGenerator;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.util.DirUtil;

/**
 * Creates an artificial domain to be used in unit tests
 * 
 * The domain has the following kind of concepts:
 * 
 * <ul>
 * <li>c_bai0 ... c_bai29
 * <li>c_bao0 ... c_bao10
 * <li>cc_cat0 ... cc_cat10
 * <li>c_catx_cmi0 ... c_catx_cmi9
 * <li>cc_cot0 ... cc_cot10
 * <li>c_cotx_cmo0 ... c_cotx_cmo4
 * <li>cc_catv0 ... cc_catv20 - variable size
 * <li>c_catv0_cmiv0 ... c_catv10_cmiv9
 * <li>'PN0' ... 'PN9'
 * </ul>
 * 
 * Verbs:
 * 
 * <ul>
 * <li>v_av0 ... v_av49
 * <li>vr_rel0 ... vr_rel49
 * <li>vm_rcat0 ... vm_rcat10
 * <li>vr_rcat0_rmi1 ... vr_rcat10_rmi12 (+2 from the cat number)
 * <li>vm_rcot0 ... vm_rcot10
 * <li>vm_rcot0_rmo1 .... vm_rcot10_rmi4 (overlap increases with cot number)
 * </ul>
 * 
 * 
 * @author Ladislau Boloni
 * 
 */
public class ArtificialDomain {

	// file root for the saved agent for the artificial domain with warmup
	public static final String FILE_ARTIFICIAL_DOMAIN = "artificial_domain";
	// file root for the saved agent for the artificial domain only
	public static final String FILE_ARTIFICIAL_AUTOBIOGRAPHY = "artificial_autobiography";
	// counter for the automatically generated instance labels
	public static int instanceLabel = 0;
	public static final String outputDirName = "output/artificialdomain/";

	/**
	 * 
	 * Creates an agent from a specific domain and a series of stories passed as
	 * parameters.
	 * 
	 * @param prefix
	 *            the prefix which is used for storing the intermediary files
	 * @param stories
	 *            the stories which the agent will have in its autobiography
	 * @param parameterPrepack
	 *            the parametrization used while stories are executed
	 * @return
	 * @throws IOException
	 */
	private static Agent createLoadedAgent(String prefix, ABStory story, String parameterPrepack)
			throws IOException {
		// int count = 0;
		// out of sheer luck: do we have the final?
		String outputFileName = outputDirName + prefix + "_agent" + ".xa";
		File outputFile = new File(outputFileName);
		if (outputFile.exists()) {
			Runner r = new Runner(outputFile);
			return r.agent;
		} 
		// we don't have it, we need to run it
		String bigStoryName = outputDirName + prefix + ".xapi";
		story.saveTo(bigStoryName);
		// force the focus-only parameters
		Xapagy.main(parameterPrepack, bigStoryName, "--output-agent", outputFileName);
		Runner r = new Runner(outputFile);
		return r.agent;
	}


	/**
	 * Generates a reciprocal story using two instances and the specified list
	 * of strings. Add labels to the instances for unique access.
	 * 
	 * @param instance1
	 * @param instance2
	 * @param verbs
	 * @return
	 */
	private static ABStory storyReciprocal(String instance1, String instance2, List<String> verbs) {
		String instOne = instance1 + " #instance" + ArtificialDomain.instanceLabel++;
		String instTwo = instance2 + " " + " #instance" + ArtificialDomain.instanceLabel++;
		RecordedStory rs = RsGenerator.generateReciprocal(instOne, instTwo, verbs);
		ABStory theStory = rs.getFullStory();
		return theStory;
	}

	/**
	 * Returns the filename of a starter agent that has the artificial domain already loaded
	 * @return
	 */
	public static String agentfileArtificialDomain() {
		String file = ArtificialDomain.outputDirName + FILE_ARTIFICIAL_DOMAIN + "_agent.xa";
		// ensure it is there
		runnerArtificialDomain();
		return file;
	}

	/**
	 * Creates an artificial autobiography where the distribution of the
	 * instances with specific concepts models certain settings for the testing
	 * of the algorithms.
	 * 
	 * The stories (and their verbs) are not relevant for this autobiography, so
	 * all the stories are simple reciprocal stories.
	 */
	private static ABStory storyArtificialAutobiography() {
		ABStory retval = new ABStory();
		retval.add( storyArtificialDomain());
		// the verbs used in the generation
		List<String> verbs = Arrays.asList("wa_v_av1", "wa_v_av2", "wa_v_av3");
		// most of the frequencies are exponentially distributed
		// the problem here, is that it would create a too large story, so
		// we are keeping this tempering factor
		// 0.5: the largest frequency is 148
		double tempering = 0.5;
		//
		// A series of instances with a single BAI attribute
		//
		// Uses: c_bai0 ... c_bai9
		// c_bai0 --- used \sum(i*i) times
		// c_baix --- used i * i times (x 1..9)
		//
		int base = 0;
		String instOne = "w_c_bai" + 0;
		for (int i = 1; i != 10; i++) {
			// exponential distribution of frequences
			int usesOfBaiI = (int) Math.exp(tempering * i);
			String instTwo = "w_c_bai" + (base + i);
			for (int j = 0; j != usesOfBaiI; j++) {
				retval.add(storyReciprocal(instOne, instTwo, verbs));
			}
		}
		//
		// A series of instances with more than 1 BAI attributes
		// Using c_bai11..c_bai20
		base = 10;
		String anchor = "w_c_bai" + base;
		// c_bai10 appears 10 times alone
		for (int i = 1; i != 10; i++) {
			String instTwo = anchor;
			retval.add(storyReciprocal(instOne, instTwo, verbs));
		}
		// 10 times each with c_bai2...c_bai5 only
		// 10 times each with c_bai2... c_bai5 + c_bai6
		// 10 times each with c_bai2... c_bai5 + c_bai6 + c_bai7
		for (int iter = 0; iter != 3; iter++) {
			String addOn = "";
			switch (iter) {
			case 0:
				addOn = "";
				break;
			case 1:
				addOn = " w_c_bai" + (base + 6);
				break;
			case 2:
				addOn = " w_c_bai" + (base + 6) + " w_c_bai" + (base + 7);
			}
			for (int i = 1; i != 10; i++) {
				for (int j = 1; j != 5; j++) {
					String instTwo = anchor + " " + "w_c_bai" + (base + j) + addOn;
					retval.add(storyReciprocal(instOne, instTwo, verbs));
				}
			}
		}
		// this tries to assure that what comes after will be created with a
		// clean focus
		ABStory absClear = new ABStory();
		// absClear.add("$NewSceneOnly #Clear,none");
		absClear.add("$CreateScene #Clear CloseOthers");
		absClear.add("----");
		retval.add(absClear);
		return retval;
	}	
	
	/**
	 * Returns a runner for an agent which has the artificial domain, but no warmup autobiography
	 *  
	 * @return
	 */
	public static Runner runnerArtificialAutobiography() {
		ABStory story = storyArtificialAutobiography();
		Agent loadedAgent = null;
		try {
			loadedAgent = createLoadedAgent(ArtificialDomain.FILE_ARTIFICIAL_AUTOBIOGRAPHY, story, "P-FocusOnly");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Runner(loadedAgent);
	}

	/**
	 * Returns a story that creates the artificial domain
	 * 
	 * @return
	 */
	private static ABStory storyArtificialDomain() {
		DirUtil.guaranteeDirectory(ArtificialDomain.outputDirName);
		DomainGenerator dg = new DomainGenerator();
		ABStory story = new ABStory();
		//
		// Create the concepts
		//
		// 30 independent base attributes
		dg.createBAIs(30, "bai", story);
		// 10 pairs of overlapping base attributes, with overlap ranging from
		// 0.0 to 1.0
		// they will be called bao3_1 etc.
		for (int i = 0; i <= 10; i++) {
			String name = "bao" + i + "_";
			double overlap = 0.1 * i;
			dg.createBAOPair(name, overlap, story);
		}
		// hard exclusion domains
		// 10 categories with an exclusion domain called cat3 etc.
		// each of them with 10 independent category members called cat3_cmi4
		// etc
		for (int i = 0; i <= 10; i++) {
			String catname = "cat" + i;
			String cminame = catname + "_cmi";
			dg.createCAT(catname, 1.0, story);
			dg.createCMO(catname, 1.0, cminame, 10, 0.0, story);
		}
		// soft exclusion domains
		// 10 categories with an exclusion domain called cot3 etc
		// each of them with 10 overlapping category members called
		for (int i = 0; i <= 10; i++) {
			String cotname = "cot" + i;
			String cmoname = cotname + "_cmo";
			double overlap = 0.1 * i;
			dg.createCAT(cotname, 1.0, story);
			dg.createCMO(cotname, 1.0, cmoname, 4, overlap, story);
		}
		// EXPERIMENTAL: categories with variable sizes (ranging from 0.1 to
		// 2.0)
		for (int i = 0; i <= 20; i++) {
			String catname = "catv" + i;
			String cminame = catname + "_cmiv";
			double size = 0.1 * i;
			dg.createCAT(catname, size, story);
			dg.createCMO(catname, size, cminame, 10, 0.0, story);
		}
		// 10 proper names, called propername1,...
		dg.createPropernames("PN", 10, story);
		//
		// Creates the verbs
		//
		// 50 independent action verbs
		dg.createIndependentActionVerbs(50, "av", story);
		// 50 independent relation verbs
		dg.createIndependentRelationVerbs(50, "rel", story);
		// 10 different categories of independent category relation verbs with
		// different numbers for each category
		for (int i = 0; i != 10; i++) {
			String rcatname = "rcat" + i;
			String rminame = rcatname + "_rmi";
			dg.createCategoryVerb(rcatname, 1.0, story);
			dg.createCategoryRelationVerbs(rcatname, 1.0, i + 2, rminame, 0.0, story);
		}
		// 10 different categories of overlapping category relation verbs with 5
		// verbs each and different overlaps
		for (int i = 0; i != 10; i++) {
			String rcatname = "rcot" + i;
			String rminame = rcatname + "_rmo";
			double overlap = 0.1 * i;
			dg.createCategoryVerb(rcatname, 1.0, story);
			dg.createCategoryRelationVerbs(rcatname, 1.0, 5, rminame, overlap, story);
		}
		return story;
	}

	public static Runner runnerArtificialDomain() {
		ABStory story = storyArtificialDomain();
		Agent loadedAgent = null;
		try {
			loadedAgent = createLoadedAgent(ArtificialDomain.FILE_ARTIFICIAL_DOMAIN, story, "P-FocusOnly");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Runner(loadedAgent);
	}

	/**
	 * Prints and possibly executes a recorded story in an unloaded runner
	 * 
	 * @param label
	 * @param rs
	 */
	public static Runner doIt(String label, RecordedStory rs, boolean printIt, boolean runIt) {
		Formatter fmt = new Formatter();
		fmt.add(label);
		fmt.indent();
		fmt.add(rs.getFullStory());
		if (printIt) {
			TextUi.println(fmt.toString());
		}
		if (runIt) {
			Runner r = ArtificialDomain.runnerArtificialDomain();
			rs.runAll(r);
			return r;
		}
		return null;
	}

}
