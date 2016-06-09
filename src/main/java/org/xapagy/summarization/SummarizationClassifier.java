/*
   This file is part of the Xapagy project
   Created on: Dec 21, 2014
 
   org.xapagy.summarization.SummarizationClassifier
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.summarization;

import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 *
 */
public class SummarizationClassifier {

    /**
     * @param vi
     * @return
     */
    public static boolean isExplicitSummarizationVi(VerbInstance vi) {
        if (vi.getVerbs().getLabels().contains(Hardwired.LABEL_SUMMARIZATION)) {
            return true;
        }
        return false;
    }

}
