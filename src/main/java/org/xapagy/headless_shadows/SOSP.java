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
package org.xapagy.headless_shadows;

import java.io.Serializable;

import org.xapagy.instances.Instance;
import org.xapagy.instances.XapagyComponent;

/**
 * A Subject-Object shadow pair (SOSP) object is used in calculating the StaticFSLI objects, represent a match
 * between two focus instances and two shadow instances sharing a scene (for an S-V-O).
 *
 * The subject and object instances can be identical (in which case we have an
 * S-V).
 *
 * @author Ladislau Boloni
 * Created on: Aug 31, 2014
 */
public class SOSP implements XapagyComponent, Serializable, Comparable<SOSP> {

    private static final long serialVersionUID = -2392761374499091664L;
    private Instance fiObject;
    private Instance fiSubject;
    private double score;
    private Instance shadowScene;
    private Instance siObject;
    private Instance siSubject;
    private boolean sv;
    private String identifier;

    /**
     * @param identifier
     * @param fiSubject
     * @param siSubject
     * @param fiObject
     * @param siObject
     * @param shadowScene
     * @param score
     */
    public SOSP(String identifier, Instance fiSubject, Instance siSubject,
            Instance fiObject, Instance siObject, Instance shadowScene,
            double score) {
        super();
        this.identifier = identifier;
        this.fiSubject = fiSubject;
        this.siSubject = siSubject;
        this.fiObject = fiObject;
        this.siObject = siObject;
        this.shadowScene = shadowScene;
        this.score = score;
        sv = (fiSubject.equals(fiObject));
    }

    public Instance getFiObject() {
        return fiObject;
    }

    public Instance getFiSubject() {
        return fiSubject;
    }

    public double getScore() {
        return score;
    }

    public Instance getShadowScene() {
        return shadowScene;
    }

    public Instance getSiObject() {
        return siObject;
    }

    public Instance getSiSubject() {
        return siSubject;
    }

    /**
     * An SOSP is of a subject verb type if the focus subject and object is the same 
     * @return
     */
    public boolean isSV() {
        return sv;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SOSP other) {
        return Double.compare(score, other.score);
    }

    /* (non-Javadoc)
     * @see org.xapagy.instances.XapagyComponent#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

}
