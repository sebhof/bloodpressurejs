/*
 * Copyright (C) 2015 shofmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.shofmann.bloodpressurejs;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author shofmann
 */
@XmlRootElement
public class BloodPressureReportItem implements Serializable {

    private Date date;

    private int systole;

    private int diastole;

    private int rate;

    private WHOState whoState;

    public BloodPressureReportItem() {
    }
    
    public BloodPressureReportItem(BloodPressure bloodPressure) {
        this.date = bloodPressure.getDate();
        this.systole = bloodPressure.getSystole();
        this.diastole = bloodPressure.getDiastole();
        this.rate = bloodPressure.getRate();
        this.calculateWHOState();
    }

    /**
     * Calculates the WHO state
     */
    public void calculateWHOState() {
        /**
         * We have to calcalate from "worst" to "best" because one codition is
         * enough to get a worser rate...
         */
        if (this.systole > 180 || this.diastole > 110) {
            this.whoState = WHOState.STAGE_3;
        } else if (between(this.systole, 160, 179) || between(this.diastole, 100, 109)) {
            this.whoState = WHOState.STAGE_2;
        } else if (between(this.systole, 140, 159) || between(this.diastole, 90, 99)) {
            this.whoState = WHOState.STAGE_1;
        } else if (between(this.systole, 130, 139) || between(this.diastole, 85, 89)) {
            this.whoState = WHOState.HIGH_NORMAL;
        } else if (between(this.systole, 120, 129) || between(this.diastole, 80, 84)) {
            this.whoState = WHOState.NORMAL;
        } else if (this.systole < 120 && this.diastole < 80) {
            this.whoState = WHOState.OPTIMAL;
        } else {
            this.whoState = WHOState.UNDEFINED;
        }
    }

    /**
     * Indicates whether the value is between lowest and highest (including)
     *
     * @param value Value to check
     * @param lowest lowest value
     * @param highest highest value
     * @return true, if value is between highest and lowest, false otherwise
     */
    private boolean between(int value, int lowest, int highest) {
        return value >= lowest && value <= highest;
    }

    /**
     * Get the value of whoState
     *
     * @return the value of whoState
     */
    public WHOState getWhoState() {
        return whoState;
    }

    /**
     * Set the value of whoState
     *
     * @param whoState new value of whoState
     */
    public void setWhoState(WHOState whoState) {
        this.whoState = whoState;
    }

    /**
     * Get the value of rate
     *
     * @return the value of rate
     */
    public int getRate() {
        return rate;
    }

    /**
     * Set the value of rate
     *
     * @param rate new value of rate
     */
    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * Get the value of diastole
     *
     * @return the value of diastole
     */
    public int getDiastole() {
        return diastole;
    }

    /**
     * Set the value of diastole
     *
     * @param diastole new value of diastole
     */
    public void setDiastole(int diastole) {
        this.diastole = diastole;
    }

    /**
     * Get the value of systole
     *
     * @return the value of systole
     */
    public int getSystole() {
        return systole;
    }

    /**
     * Set the value of systole
     *
     * @param systole new value of systole
     */
    public void setSystole(int systole) {
        this.systole = systole;
    }

    /**
     * Get the value of date
     *
     * @return the value of date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the value of date
     *
     * @param date new value of date
     */
    public void setDate(Date date) {
        this.date = date;
    }

}
