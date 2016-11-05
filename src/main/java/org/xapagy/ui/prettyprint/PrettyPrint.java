/*
   This file is part of the Xapagy project
   Created on: Jun 6, 2011
 
   org.xapagy.ui.prettyprint.PrettyPrint
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.TwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class PrettyPrint {

    public static Agent lastAgent = null;

    /**
     * Dispatcher function pretty printing at various detail levels
     * 
     * @param object
     * @param agent
     * @return
     */
    public static String pp(Object object, Agent agentPassed,
            PrintDetail detailLevel) {
        Agent agent;
        if (agentPassed == null) {
            agent = PrettyPrint.lastAgent;
        } else {
            PrettyPrint.lastAgent = agentPassed;
            agent = agentPassed;
        }
        if (object instanceof List<?>) {
            StringBuffer retval = new StringBuffer();
            for (Object ob2 : (List<?>) object) {
                retval.append(PrettyPrint.pp(ob2, agent, detailLevel));
            }
            return retval.toString();
        }
        switch (detailLevel) {
        case DTL_CONCISE:
            return PrettyPrint.ppConcise(object, agent);
        case DTL_DETAIL:
            return PrettyPrint.ppDetailed(object, agent);
        default:
            throw new Error("pp cannot handle detail level " + detailLevel);
        }
    }

    /**
     * Concise printing, direct to output
     * 
     * @param object
     * @param agent
     */
    public static void ppc(Object object, Agent agent) {
        TextUi.println(PrettyPrint.pp(object, agent, PrintDetail.DTL_CONCISE));
    }

    /**
     * Dispatcher function for concise pretty printing
     * 
     * @param object
     * @param agent
     * @return
     */
    public static String ppConcise(Object object, Agent agent) {
        return PrettyPrint.printThroughIntrospection(object, agent, "Concise");
    }

    /**
     * Concise printing, direct to output
     * 
     * @param object
     * @param agent
     */
    public static void ppd(Object object, Agent agent) {
        TextUi.println(PrettyPrint.pp(object, agent, PrintDetail.DTL_DETAIL));
    }

    /**
     * Dispatcher function for concise detailed printing
     * 
     * @param object
     * @param agent
     * @return
     */
    public static String ppDetailed(Object object, Agent agent) {
        return PrettyPrint.printThroughIntrospection(object, agent, "Detailed");
    }

    
    /**
     * Prints a Xapagy object through introspection, by trying to find an appropriate formatter.
     * First it is looking for an Xw type formatter, which is the recommended approach.
     * 
     * @param o - the Xapagy object to be printed
     * @param agentPassed - the agent 
     * @param methodName - the printing method "Detailed", "Concise", "SuperConcise" or something like that
     * @return
     */
    private static String printThroughIntrospection(Object o, Agent agentPassed, String methodName) {
        if (o == null) {
            return "<< pp: null object>>";
        }
        Agent agent;
        if (agentPassed == null) {
            agent = PrettyPrint.lastAgent;
        } else {
            agent = agentPassed;
            PrettyPrint.lastAgent = agent;
        }
        if (agent == null) {
            TextUi.abort("ppGuessClass:Agent must be set!!!");
        }
        // first check whether there is an xw type formatter
        String retval = printThroughIntrospectionXw(o, agent, methodName);
        if (retval != null) {
        	return retval;
        }
        // fall back on Pp
        return printThroughIntrospectionPp(o, agent, methodName);
    }
    
    /**
     * Goes through the call hierarchy looking for XwFormatters
     * @param o
     * @param agent
     * @param methodName
     * @return
     */
    private static String printThroughIntrospectionXw(Object o, Agent agent, String methodName) {
    	Class<?> objectClass = o.getClass();
        Class<?> currentClass = objectClass;
        // TextUi.println(className);

        while (currentClass != null) {
        	String className = currentClass.getSimpleName();
        	String classFormatter = "org.xapagy.ui.prettygeneral.xw" + className.substring(0, 1).toUpperCase() + className.substring(1);
            try {
                Class<?> formatterClass = Class.forName(classFormatter);
                Method method =
                        formatterClass.getMethod("xw" + methodName, IXwFormatter.class, currentClass, Agent.class);
                String result = (String) method.invoke(null, new TwFormatter(), o, agent);
                return result;
            } catch (NoSuchMethodException e) {
                TextUi.errorPrint("No method " + methodName + " on "
                        + classFormatter);
                System.exit(1);
            } catch (ClassNotFoundException e) {
                // TextUi.println("No class " + classFormatter + " trying ancestor");
                currentClass = currentClass.getSuperclass();
                continue;
            } catch (IllegalArgumentException | IllegalAccessException e) {
                TextUi.println("Exception when trying to invoke " + methodName
                        + " on " + o.getClass().getCanonicalName());
                // e.printStackTrace();
                // System.exit(1);
                return "<<Exception IAE>>";
            } catch (InvocationTargetException e) {
                TextUi.println("InvocationTargetException when trying to invoke "
                        + methodName
                        + " on "
                        + o.getClass().getCanonicalName()
                        + " cause is " + e.getCause());
                // e.printStackTrace();
                // System.exit(1);
                return "<<Exception ITE>>";
            }
        }
        return null;
    }
    
    
    /**
     * Prints through introspection by looking for a Pp type class.
     * Invokes the passed method (ppDetailed, ppConcise or something like that) on 
     * a formatter that has a format of org.xapagy.ui.prettyprint.PpClassName
     * 
     * @param o
     * @param agent
     * @param methodName
     * @return
     */
    private static String printThroughIntrospectionPp(Object o, Agent agent, String methodName) {
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof List<?>) {
            StringBuffer retval = new StringBuffer();
            for (Object ob2 : (List<?>) o) {
                retval.append(PrettyPrint.ppConcise(ob2, agent) + "\n");
            }
            return retval.toString();
        }
        Class<?> objectClass = o.getClass();
        Class<?> currentClass = objectClass;
        // TextUi.println(className);

        while (currentClass != null) {
            String ppClassName =
                    "org.xapagy.ui.prettyprint.Pp"
                            + currentClass.getSimpleName();
            try {
                Class<?> ppClass = Class.forName(ppClassName);
                Method method =
                        ppClass.getMethod("pp" + methodName, currentClass, Agent.class);
                String result = (String) method.invoke(null, o, agent);
                return result;
            } catch (NoSuchMethodException e) {
                TextUi.errorPrint("No method " + methodName + " on "
                        + ppClassName);
                System.exit(1);
            } catch (ClassNotFoundException e) {
                //TextUi.println("No class " + ppClassName + " trying ancestor");
                currentClass = currentClass.getSuperclass();
                continue;
            } catch (IllegalArgumentException | IllegalAccessException e) {
                TextUi.println("Exception when trying to invoke " + methodName
                        + " on " + o.getClass().getCanonicalName());
                // e.printStackTrace();
                // System.exit(1);
                return "<<Exception IAE>>";
            } catch (InvocationTargetException e) {
                TextUi.println("InvocationTargetException when trying to invoke "
                        + methodName
                        + " on "
                        + o.getClass().getCanonicalName()
                        + " cause is " + e.getCause());
                // e.printStackTrace();
                // System.exit(1);
                return "<<Exception ITE>>";
            }
        }
        return "<< can not prettyprint class " + objectClass.getCanonicalName()
                + ">>";
    }

    /**
     * This can be used as a fallback of the toString for various components,
     * needed for the Eclipse debugger to show proper values
     * 
     * @return
     */
    public static String ppString(Object object) {
        if (PrettyPrint.lastAgent == null) {
            return "ppString: lastAgent is null, set it somehow";
        }
        return PrettyPrint.pp(object, PrettyPrint.lastAgent,
                PrintDetail.DTL_CONCISE);
    }

}
