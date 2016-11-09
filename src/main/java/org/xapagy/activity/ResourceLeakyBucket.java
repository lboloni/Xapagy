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
package org.xapagy.activity;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Nov 17, 2011
 */
public abstract class ResourceLeakyBucket implements Serializable {

    private static final long serialVersionUID = -816304480663917246L;

    private Agent agent;

    private double lastUsedQuantity = -1;

    /**
     * The name of the resource
     */
    private String name;

    /**
     * The maximum available resource
     */
    private double paramMaxAvailable;

    /**
     * The pipe width parameter
     */
    private double paramPipeWidth;

    /**
     * The total quantity of the resource
     */
    private double paramTotalQuantity;

    /**
     * The quantity of immediately available resource
     */
    private double quantityAvailable;

    /**
     * The quantity of the resource in the backup storage
     */
    private double quantityBackup;

    /**
     * Creates a leaky bucket resource, and distributes the values filling up
     * the available completely
     * 
     * @param agent
     * @param name
     * @param paramTotalQuantity
     * @param paramMaxAvailable
     * @param paramPipeWidth
     */
    public ResourceLeakyBucket(Agent agent, String name,
            double paramTotalQuantity, double paramMaxAvailable,
            double paramPipeWidth) {
        super();
        this.agent = agent;
        this.name = name;
        this.paramTotalQuantity = paramTotalQuantity;
        this.paramMaxAvailable = paramMaxAvailable;
        this.paramPipeWidth = paramPipeWidth;
        // init the quantities
        quantityAvailable = 0;
        quantityBackup = 0;
        // partition the amount of resources
        double remaining = paramTotalQuantity;
        // I am going with the assumption that initial quantity used is zero
        // chicken egg problem
        // remaining -= getQuantityUsed();
        if (remaining > 0.0) {
            quantityAvailable = Math.min(remaining, paramMaxAvailable);
            remaining -= quantityAvailable;
        }
        if (remaining > 0.0) {
            quantityBackup = remaining;
        }
    }

    /**
     * Verifies if there was a resource leak. Current behavior is that
     * 
     */
    public void checkLeak() {
        // now verify if there was any leak...
        double quantityRealTotal =
                quantityBackup + quantityAvailable + getQuantityUsed();
        double leak = paramTotalQuantity - quantityRealTotal;
        if (Math.abs(leak) > 0.001) {
            TextUi.errorPrint("There was a leak on resource " + name);
            TextUi.errorPrint("Real quantity used:" + quantityRealTotal);
            TextUi.errorPrint("Total quantity " + paramTotalQuantity);
            TextUi.errorPrint("Refilling the bucket... not a final fix!!!");
            quantityAvailable += leak;
        }

    }

    /**
     * @return the agent
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the paramMaxAvailable
     */
    public double getParamMaxAvailable() {
        return paramMaxAvailable;
    }

    /**
     * @return the paramPipeWidth
     */
    public double getParamPipeWidth() {
        return paramPipeWidth;
    }

    /**
     * @return the paramTotalQuantity
     */
    public double getParamTotalQuantity() {
        return paramTotalQuantity;
    }

    /**
     * @return the quantityAvailable
     */
    public double getQuantityAvailable() {
        return quantityAvailable;
    }

    /**
     * @return the quantityBackup
     */
    public double getQuantityBackup() {
        return quantityBackup;
    }

    /**
     * 
     * This function returns the amount of this resource - must be implemented
     * at creation time
     * 
     * @return
     */
    public abstract double getQuantityUsed();

    /**
     * Implements the passing of time, the resources are moved from backup to
     * immediate until they fill it up
     * 
     * @param time
     */
    public void timePassing(double time) {
        double quantityTransfer = paramPipeWidth * time;
        quantityTransfer = Math.min(quantityTransfer, quantityBackup);
        quantityTransfer =
                Math.min(quantityTransfer, paramMaxAvailable
                        - quantityAvailable);
        // perform the transfer
        quantityAvailable += quantityTransfer;
        quantityBackup -= quantityTransfer;
        // do a leak checking
        checkLeak();
    }

    /**
     * Sets a resource management checkpoint
     */
    public void usageLock() {
        if (lastUsedQuantity != -1) {
            throw new Error("Resource already locked!!!");
            // usageUnlock();
        }
        lastUsedQuantity = getQuantityUsed();
    }

    /**
     * Release the resource management lock, consume resource from available
     */
    public void usageUnlock() {
        double usage = getQuantityUsed() - lastUsedQuantity;
        // if resources have been used, remove them from the quantityAvailable
        if (usage > 0.0) {
            if (usage > quantityAvailable) {
                throw new Error(
                        "More resources have been used than they were available");
            }
            quantityAvailable -= usage;
        }
        // if resources have been released, add them to the backup
        if (usage < 0.0) {
            quantityBackup = quantityBackup - usage;
        }
        lastUsedQuantity = -1;
    }

}
