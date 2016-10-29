package org.xapagy.storyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.introspect.Introspect;
import org.xapagy.questions.QuestionAnswerPairing;
import org.xapagy.questions.QuestionHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * This class describes the story line based question answering mechanism
 * 
 * @author lboloni
 *
 */
public class QuestionAnswering {

	public static List<VerbInstance> answerQuestion(Agent agent, VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		Introspect introspect = new Introspect(agent);
		// just make sure that it is a question we are looking at
		if (!QuestionHelper.isAQuestion(question, agent)) {
			throw new Error("Not a question:" + XapiPrint.ppsViXapiForm(question, agent));
		}
		//
		// whether questions about the future
		//
		if (QuestionHelper.isWhetherQuestion(question, agent) && QuestionHelper.isFutureQuestion(agent, question)) {
			answerWhetherFuture(agent, question);
		}		
		// whether question about the past
		if (QuestionHelper.isWhetherQuestion(question, agent) && QuestionHelper.isPastQuestion(agent, question)) {
			answerWhetherPast(agent, question);
		}		
		// what question about the future
		if (QuestionHelper.isWhatQuestion(question, agent) && QuestionHelper.isFutureQuestion(agent, question)) {
			answerWhetherPast(agent, question);
		}		
		// what question about the future
		if (QuestionHelper.isWhatQuestion(question, agent) && QuestionHelper.isPastQuestion(agent, question)) {
			answerWhetherPast(agent, question);
		}		
		//
		//  FIXME: there are many other type of questions, for instance x / is / what??? Descriptive questions
		//
		TextUi.println("storyLine.QuestionAnswering.answerQuestion: only trivial question");
		return trivialAnswer(agent, introspect, question);
	}

	
	/**
	 * Answering a "whether question about the future". Eg. Did Hector hit Achilles?
	 * 
	 * @param agent
	 * @param question
	 * @return
	 */
	private static List<VerbInstance> answerWhetherFuture(Agent agent,
			VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering whether question about the future is not implemented yet");
		return retval;
	}

	
	/**
	 * Answering a "whether question about the future". Eg. whether Hector will die???
	 * 
	 * @param agent
	 * @param question
	 * @return
	 */
	private static List<VerbInstance> answerWhetherPast(Agent agent,
			VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering whether question about the past is not implemented yet");
		return retval;
	}

	
	/**
	 * Answering a "what/who-type question about the future". Eg. Who will hit Hector?
	 * 
	 * @param agent
	 * @param question
	 * @return
	 */
	private static List<VerbInstance> answerWhatFuture(Agent agent,
			VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering what/who question about the future is not implemented yet");
		return retval;
	}
	
	/**
	 * Answering a "what/who-type question about the future". Eg. Who will hit Hector?
	 * 
	 * @param agent
	 * @param question
	 * @return
	 */
	private static List<VerbInstance> answerWhatPast(Agent agent,
			VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering what/who question about the past is not implemented yet");
		return retval;
	}
		

	/**
	 * Answering a "why" type question. Eg. why is Hector and Achilles fighting?
	 * 
	 * @param agent
	 * @param question
	 * @return
	 */
	private static List<VerbInstance> answerWhy(Agent agent,
			VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering a why question is not implemented yet");
		return retval;
	}

	
	
	/**
	 * Returns a trivial answer by checking the question against the current
	 * story line
	 * 
	 * @return
	 */
	public static List<VerbInstance> trivialAnswer(Agent agent, Introspect introspect, VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		StoryLine line = introspect.currentStoryLine();
		List<VerbInstance> vis = new ArrayList<>(line.getVis());
		Collections.reverse(vis);
		for(VerbInstance cand: vis) {
			if (QuestionAnswerPairing.isPair(question, cand, agent, true)) {
				retval.add(cand);
				break;
			}
		}
		return retval;
	}

}
