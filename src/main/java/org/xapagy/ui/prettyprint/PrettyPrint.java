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
        return PrettyPrint.ppGuessClass(object, agent, "ppConcise");
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
        return PrettyPrint.ppGuessClass(object, agent, "ppDetailed");
    }

    /**
     * Guess the name
     * 
     * @param o
     * @return
     */
    public static String ppGuessClass(Object o, Agent agentPassed, String methodName) {
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
        if (o == null) {
            return "<< ppConcise: null object>>";
        }
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
                        ppClass.getMethod(methodName, currentClass, Agent.class);
                String result = (String) method.invoke(null, o, agent);
                return result;
            } catch (NoSuchMethodException e) {
                TextUi.errorPrint("No method " + methodName + " on "
                        + ppClassName);
                System.exit(1);
            } catch (ClassNotFoundException e) {
                TextUi.println("No class " + ppClassName + " trying ancestor");
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
