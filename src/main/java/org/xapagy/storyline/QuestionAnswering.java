package org.xapagy.storyline;

import java.util.AbstractMap.SimpleEntry;
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

	/**
	 * The dispatcher for the question answering. Returns a combination of
	 * formatted text, suitable for presentation to a human and a list of VIs
	 * that are the answer which might be actually created
	 * 
	 * @param agent
	 * @param question
	 * @return
	 */
	public static SimpleEntry<String, List<VerbInstance>> answerQuestion(Agent agent, VerbInstance question) {
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
			return answerWhetherFuture(agent, introspect, question);
		}
		// whether question about the past
		if (QuestionHelper.isWhetherQuestion(question, agent) && QuestionHelper.isPastQuestion(agent, question)) {
			return answerWhetherPast(agent, introspect, question);
		}
		// what question about the future
		if (QuestionHelper.isWhatQuestion(question, agent) && QuestionHelper.isFutureQuestion(agent, question)) {
			return answerWhatFuture(agent, introspect, question);
		}
		// what question about the future
		if (QuestionHelper.isWhatQuestion(question, agent) && QuestionHelper.isPastQuestion(agent, question)) {
			return answerWhatPast(agent, introspect, question);
		}
		//
		// FIXME: there are many other type of questions, for instance x / is /
		// what??? Descriptive questions
		//
		// TextUi.println("storyLine.QuestionAnswering.answerQuestion: only
		// trivial question");
		return answerWhatFocus(agent, introspect, question);
	}

	/**
	 * Finds the answer to the question in a list of VIs.
	 * 
	 * Returns the answer. For the time being, the answer is just one VI, but
	 * FIXME this might be extended to represent the more interesting
	 * circumstances.
	 * 
	 * The implementation, for the time being is just searching for matching in
	 * the list.
	 * 
	 * @param agent
	 * @param question
	 *            - the question VI
	 * @param story
	 *            - the real, fictional or partly fictional story in which we
	 *            are looking for answers
	 * @return - the list of VIs that constitute the answer. If it is ok.
	 */
	public static List<VerbInstance> findAnswerInStory(Agent agent, VerbInstance question, List<VerbInstance> story) {
		List<VerbInstance> answer = new ArrayList<>();
		List<VerbInstance> vis = new ArrayList<>(story);
		Collections.reverse(vis);
		for (VerbInstance cand : vis) {
			if (QuestionAnswerPairing.isPair(question, cand, agent, true)) {
				answer.add(cand);
				break;
			}
		}
		return answer;
	}

	/**
	 * Answering a "whether question about the future". Eg. Did Hector hit
	 * Achilles?
	 * 
	 * @param agent
	 * @param question
	 * @return
	 */
	private static SimpleEntry<String, List<VerbInstance>> answerWhetherFuture(Agent agent, Introspect introspect,
			VerbInstance question) {
		// List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering whether question about the future is not implemented yet");
		return null;
	}

	/**
	 * Answering a "whether question about the future". Eg. whether Hector will
	 * die???
	 * 
	 * @param agent
	 * @param introspect
	 * @param question
	 * @return
	 */
	private static SimpleEntry<String, List<VerbInstance>> answerWhetherPast(Agent agent, Introspect introspect,
			VerbInstance question) {
		// List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering whether question about the past is not implemented yet");
		return null;
	}

	/**
	 * Answering a "what/who-type question about the future". Eg. Who will hit
	 * Hector?
	 * 
	 * @param agent
	 * @param introspect
	 * @param question
	 * @return
	 */
	private static SimpleEntry<String, List<VerbInstance>> answerWhatFuture(Agent agent, Introspect introspect,
			VerbInstance question) {
		// List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering what/who question about the future is not implemented yet");
		return null;
	}

	/**
	 * Answering a "what/who-type question about the future". Eg. Who will hit
	 * Hector?
	 * 
	 * @param agent
	 * @param introspect
	 * @param question
	 * @return
	 */
	private static SimpleEntry<String, List<VerbInstance>> answerWhatPast(Agent agent, Introspect introspect,
			VerbInstance question) {
		StoryLine line = introspect.currentStoryLine();
		List<VerbInstance> answer = findAnswerInStory(agent, question, introspect.currentStoryLine().getVis());
		String text = formatWhatAnswer(agent, question, answer);
		return new SimpleEntry<String, List<VerbInstance>>(text, answer);
	}

	
	/**
	 * Formats the answer for a what question
	 * @param answer
	 * @return
	 */
	private static String formatWhatAnswer(Agent agent, VerbInstance question, List<VerbInstance> answer) {
		StringBuffer buf = new StringBuffer("Answer: ");
		if (answer.isEmpty()) {
			buf.append("I don't know " + XapiPrint.ppsViXapiForm(question, agent));
		} else {
			for (VerbInstance vi : answer) {
				buf.append(XapiPrint.ppsViXapiForm(vi, agent));
			}
		}
		return buf.toString();
	}
	
	
	/**
	 * Answering a "why" type question. Eg. why is Hector and Achilles fighting?
	 * 
	 * @param agent
	 * @param question
	 * @return
	 */
	private static List<VerbInstance> answerWhy(Agent agent, VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		TextUi.println("Answering a why question is not implemented yet");
		return retval;
	}

	/**
	 * Returns a trivial answer by checking the question against what is
	 * currently in focus
	 * 
	 * @return
	 */
	public static SimpleEntry<String, List<VerbInstance>> answerWhatFocus(Agent agent, Introspect introspect, VerbInstance question) {
		StoryLine line = introspect.currentStoryLine();
		List<VerbInstance> vis = introspect.filterInFocus(line);
		List<VerbInstance> answer = findAnswerInStory(agent, question, vis);
		String text = formatWhatAnswer(agent, question, answer);
		return new SimpleEntry<String, List<VerbInstance>>(text, answer);

	}

}
