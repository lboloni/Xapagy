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
package org.xapagy.agents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.xapagy.activity.DaComposite;
import org.xapagy.activity.SaComposite;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.DebugEvent;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.instances.IdentifierGenerator;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.introspect.Introspect;
import org.xapagy.links.Links;
import org.xapagy.parameters.Parameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;
import org.xapagy.storyline.StoryLineRepository;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.observers.IAgentObserver;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.verbalize.VerbalMemory;
import org.xapagy.verbalize.Verbalize;
import org.xapagy.xapi.XapiDictionary;
import org.xapagy.xapi.XapiParser;

/**
 * Representation of Xapagy agent
 *
 * @author Lotzi Boloni
 *
 */
public class Agent implements Serializable {

	private static final long serialVersionUID = 9139251314949324440L;
	/**
	 * The autobiographical memory of the agent
	 */
	private AutobiographicalMemory autobiographicalMemory;
	/**
	 * The database of all the concepts defined in the agent, as well as their
	 * properties (size, overlap, impact etc)
	 */
	private AbstractConceptDB<Concept> conceptDB = new AbstractConceptDB<>();
	/**
	 * The top level repository of DaComposite objects that are used in the
	 * agent
	 */
	private Map<String, DaComposite> daComposites = new HashMap<>();
	/**
	 * An auxiliary class containing debugging information - should not affect
	 * the behavior of a functioning Xapagy agent
	 */
	private AgentDebugInfo debugInfo = null;
	/**
	 * An auxiliary class containing labeling information ($Define's and
	 * namespaces)
	 */
	private AgentLabelSpaces labelSpaces = null;

	private List<VerbInstance> debugList = new ArrayList<>();

	/**
	 * A string to accummulate documentation from the Xapi command line.
	 */
	private String documentation = "";
	private EnergyColors energyColors;
	private Focus focus;
	private HeadlessComponents headlessComponents;
	private IdentifierGenerator identifierGenerator;
	private Links links;
	private Loop loop;
	private String name;
	private transient Map<String, IAgentObserver> observers;
	private Parameters parameters = new Parameters();
	/**
	 * The random generator of the Xapagy agent - to be used in stochastic DAs
	 */
	private Random random = new Random(0);
	private ReferenceAPI referenceAPI;
	/**
	 * The top level repository of SaComposite objects that are used in the
	 * agent
	 */
	private Map<String, SaComposite> saComposites = new HashMap<>();
	private transient ScriptEngine scriptEngine;
	private Shadows shadows;
	private StoryLineRepository storyLineRepository;
	private double time = 0.0;
	private Verbalize verbalize;
	private VerbalMemory verbalMemory;
	private AbstractConceptDB<Verb> verbDB = new AbstractConceptDB<>();
	private XapiDictionary xapiDictionary;
	private XapiParser xapiParser;
	/**
	 * The current set of drives of the agent
	 */
	private Drives drives = new Drives(this);

	/**
	 * @return the drives
	 */
	public Drives getDrives() {
		return drives;
	}

	/**
	 * This is the constructor called after unserialization
	 */
	public Agent() {
		PrettyPrint.lastAgent = this;
		observers = new HashMap<>();
	}

	/**
	 * This is the constructor we call when we really do stuff.
	 *
	 * @param name
	 *            - the name of the agent (only useful where we have multiple
	 *            ones)
	 * @throws Exception
	 */
	public Agent(String name) {
		this.name = name;
		this.debugInfo = new AgentDebugInfo(this);
		this.labelSpaces = new AgentLabelSpaces(this);
		//
		// the minimum number of parameters that I can do parsing
		//
		parameters.addParam("A_DEBUG", "G_GENERAL", "N_RECORD_CHOICES_INTO_LOOPITEM", false,
				"If true, LoopItem will record the choices which existed when a given LoopItem had been instantiated.");
		parameters.addParam("A_GENERAL", "G_GENERAL", "N_READING_SPEED", 1.0,
				"XapiParser interprets this value as the time to wait after a sentence terminated with a '.'");
		//
		// end of minimum parameters
		//
		energyColors = new EnergyColors();
		identifierGenerator = new IdentifierGenerator(this);
		loop = new Loop(this);
		xapiParser = new XapiParser(this, false);
		links = new Links(this);
		loop.addReading("$Include 'P-CreateParameters'");
		loop.addReading("$Include 'P-CreateEnergies'");
		try {
			loop.proceed(Integer.MAX_VALUE, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// end of experiment
		// InitParameters.init(parameters);
		focus = new Focus(this);
		autobiographicalMemory = new AutobiographicalMemory(this);
		xapiDictionary = new XapiDictionary(this);
		shadows = new Shadows(this);
		headlessComponents = new HeadlessComponents();
		observers = new HashMap<>();
		verbalize = new Verbalize(this);
		//
		// create the DaComposite and SaComposite objects
		//
		DaComposite tmp = new DaComposite(this, Hardwired.DA_FOCUS_MAINTENANCE);
		daComposites.put(tmp.getName(), tmp);
		tmp = new DaComposite(this, Hardwired.DA_SHADOW_MAINTENANCE);
		daComposites.put(tmp.getName(), tmp);
		tmp = new DaComposite(this, Hardwired.DA_HLS_MAINTENANCE);
		daComposites.put(tmp.getName(), tmp);
		SaComposite satmp = new SaComposite(this, Hardwired.SA_BEFORE_VI);
		saComposites.put(satmp.getName(), satmp);
		satmp = new SaComposite(this, Hardwired.SA_AFTER_VI);
		saComposites.put(satmp.getName(), satmp);
		//
		// now we are ready to read in more initialization Xapi code (Da-s, Sa-s
		// etc)
		//
		loop.addReading("$Include 'P-Default'");
		loop.addReading("$Include IfNotDefined Core 'Core'");
		try {
			loop.proceed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		storyLineRepository = new StoryLineRepository(this);
		verbalMemory = new VerbalMemory();
		referenceAPI = new ReferenceAPI(this);
		PrettyPrint.lastAgent = this;

		// create the very first scene
		Instance firstScene = createInstance(null);
		firstScene.getConcepts().addFullEnergy(getConceptDB().getConcept(Hardwired.C_SCENE));
		focus.setCurrentScene(firstScene);
	}

	public void addObserver(String observerName, IAgentObserver observer) {
		if (observers == null) { // eg. after deserialization
			observers = new HashMap<>();
		}
		// FOR DEBUGGING
		observers = new HashMap<>();
		observer.setAgent(this);
		observers.put(observerName, observer);
	}

	/**
	 * Adds to the internal time of the agent
	 *
	 */
	public void addTime(double timeStep) {
		time = time + timeStep;
	}

	/**
	 * Creates a new Instance, unique for this database
	 *
	 * @return
	 */
	public Instance createInstance(Instance scene) {
		String id = identifierGenerator.getInstanceIdentifier();
		Instance instance = new Instance(id, scene, this);
		return instance;
	}

	/**
	 * Creates a verb instance - the reason why this is here, is to handle the
	 * identifier, and add the debug
	 *
	 * @param verbs
	 * @return
	 */
	public VerbInstance createVerbInstance(ViType instanceType, VerbOverlay verbs) {
		String id = identifierGenerator.getVerbInstanceIdentifier();
		VerbInstance verbInstance = new VerbInstance(instanceType, id, verbs, this);
		debugList.add(verbInstance);
		return verbInstance;
	}

	/**
	 * @return the episodicMemory
	 */
	public AutobiographicalMemory getAutobiographicalMemory() {
		return autobiographicalMemory;
	}

	public AbstractConceptDB<Concept> getConceptDB() {
		return conceptDB;
	}

	/**
	 * Gets a specific DaComposite object
	 *
	 * @param name
	 * @return
	 */
	public DaComposite getDaComposite(String name) {
		return daComposites.get(name);
	}

	/**
	 * @return the debugInfo
	 */
	public AgentDebugInfo getDebugInfo() {
		return debugInfo;
	}

	/**
	 * @return the documentation
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * @return the energyColors
	 */
	public EnergyColors getEnergyColors() {
		return energyColors;
	}

	public Focus getFocus() {
		return focus;
	}

	/**
	 * @return the headlessShadows
	 */
	public HeadlessComponents getHeadlessComponents() {
		return headlessComponents;
	}

	/**
	 * @return the identifierGenerator
	 */
	public IdentifierGenerator getIdentifierGenerator() {
		return identifierGenerator;
	}

	/**
	 * @return the labelSpaces
	 */
	public AgentLabelSpaces getLabelSpaces() {
		return labelSpaces;
	}

	/**
	 * Used only for debugging purposes, not actionable
	 *
	 * @return
	 */
	public VerbInstance getLastVerbInstance() {
		return debugList.get(debugList.size() - 1);
	}

	/**
	 * @return the Links object
	 */
	public Links getLinks() {
		return links;
	}

	/**
	 * @return the loop
	 */
	public Loop getLoop() {
		return loop;
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns a list of observers. This is primarily used to progress over the
	 * breakobserver.
	 *
	 * @return the observers
	 */
	public List<IAgentObserver> getObservers() {
		return new ArrayList<>(observers.values());
	}

	public Parameters getParameters() {
		return parameters;
	}

	/**
	 * @return the random
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * @return the referenceAPI
	 */
	public ReferenceAPI getReferenceAPI() {
		return referenceAPI;
	}

	/**
	 * Gets a specific SaComposite object
	 *
	 * @param name
	 * @return
	 */
	public SaComposite getSaComposite(String name) {
		return saComposites.get(name);
	}

	/**
	 * @return the scriptEngine
	 */
	public ScriptEngine getScriptEngine() {
		if (scriptEngine == null) {
			//
			// create a JavaScript engine: FIXME: this will need to be
			// appropriately initialized to access the components
			//
			ScriptEngineManager factory = new ScriptEngineManager();
			scriptEngine = factory.getEngineByName("JavaScript");
			scriptEngine.put("agent", this);
			scriptEngine.put("ref", referenceAPI);
			Introspect introspect = new Introspect(this);
			scriptEngine.put("introspect", introspect);
		}
		return scriptEngine;
	}

	/**
	 * @return
	 */
	public Shadows getShadows() {
		return shadows;
	}

	public StoryLineRepository getStoryLineRepository() {
		return storyLineRepository;
	}

	/**
	 * Returns the internal time of the agent
	 *
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * @return the verbalize
	 */
	public Verbalize getVerbalize() {
		return verbalize;
	}

	/**
	 * @return the verbalMemory
	 */
	public VerbalMemory getVerbalMemory() {
		return verbalMemory;
	}

	public AbstractConceptDB<Verb> getVerbDB() {
		return verbDB;
	}

	public XapiDictionary getXapiDictionary() {
		return xapiDictionary;
	}

	/**
	 * @return the xapiParser
	 */
	public XapiParser getXapiParser() {
		return xapiParser;
	}

	/**
	 * Notifies all the observers
	 *
	 */
	public void notifyObservers(DebugEvent event) {
		getDebugInfo().setCurrentDebugEvent(event);
		if (observers == null) { // eg. after deserialization
			observers = new HashMap<>();
		}
		for (IAgentObserver observer : observers.values()) {
			try {
				observer.observe(event);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				TextUi.println("Exception in observer, logged but proceed:" + e);
				e.printStackTrace();
			}
		}
		getDebugInfo().setCurrentDebugEvent(null);
	}

	/**
	 * Overwritten to allow the setting of PrettyPrint last agent after loading
	 * an agent from a file
	 *
	 * @param ois
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		PrettyPrint.lastAgent = this;
	}

	public void removeAllObservers() {
		observers.clear();
	}

	public void removeObserver(String observerName) {
		observers.remove(observerName);
	}

	/**
	 * Removes all the observers. Necessary to keep things in check when we save
	 * etc.
	 */
	public void removeObservers() {
		observers.clear();
	}

	/**
	 * @param documentation
	 *            the documentation to set
	 */
	public void setDocumentation(String documentation) {
		if (documentation == null) {
			TextUi.println("Documentation is null, wtf?");
		}
		this.documentation = documentation;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * @param random
	 *            the random to set
	 */
	public void setRandom(Random random) {
		this.random = random;
	}
}
