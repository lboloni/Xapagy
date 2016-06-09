/*
   This file is part of the Xapagy project
   Created on: May 17, 2012
 
   org.xapagy.recall.SvirType
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import org.xapagy.concepts.Hardwired;

public enum FslType {
    ANSWER, COINCIDENCE, CONTEXT, CONTEXT_IMPLICATION, IN_SHADOW,
    PREDECESSOR, QUESTION, SUCCESSOR;

    /**
     * As most FSL-s are built upon a link, this function maps back the FslType
     * to the link which forms it. The exception is the IN_SHADOW, which is not
     * built on a link
     * 
     * @param fslType
     * @return
     */
    public static String getLinksForFslType(FslType fslType) {
        switch (fslType) {
        case CONTEXT:
            return Hardwired.LINK_IR_CONTEXT;
        case CONTEXT_IMPLICATION:
            return Hardwired.LINK_IR_CONTEXT_IMPLICATION;
        case IN_SHADOW:
            return null;
        case PREDECESSOR:
            return Hardwired.LINK_PREDECESSOR;
        case SUCCESSOR:
            return Hardwired.LINK_SUCCESSOR;
        case QUESTION:
            return Hardwired.LINK_QUESTION;
        case ANSWER:
            return Hardwired.LINK_ANSWER;
        case COINCIDENCE:
            return Hardwired.LINK_COINCIDENCE;
        }
        throw new Error("It should never get here");
    }
}