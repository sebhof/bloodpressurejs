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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author shofmann
 */
@Entity
@Table(name = "blood_pressure", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"creation_date", "systole", "diastole", "rate"})
})
@XmlRootElement
public class BloodPressure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date date;

    @Column(name = "systole", nullable = false)
    @NotNull
    @Min(1)
    private int systole;

    @Column(name="diastole", nullable = false)
    @NotNull
    @Min(1)
    private int diastole;

    @Column(name="rate", nullable = false)
    @NotNull
    @Min(1)
    private int rate;

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

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BloodPressure{" + "id=" + id + ", date=" + date + ", systole=" + systole + ", diastole=" + diastole + ", rate=" + rate + '}';
    }
    
}
