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
package de.shofmann.health.weight;

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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author shofmann
 */
@Entity
@Table(name = "weight", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"creation_date, weight, body_fat, water, muscle, bone, bmi"})
})
@XmlRootElement
public class Weight implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date date;

    @Column(name = "muscle", nullable = false)
    @NotNull
    private float muscle;

    @Column(name = "weight", nullable = false)
    @NotNull
    private float weight;

    @Column(name = "body_fat", nullable = false)
    @NotNull
    private float bodyFat;

    @Column(name = "water", nullable = false)
    @NotNull
    private float water;

    @Column(name = "bone", nullable = false)
    @NotNull
    private float bone;
    
    @Column(name = "bmi", nullable = false)
    @NotNull
    private float bmi;

    /**
     * Get the value of bmi
     *
     * @return the value of bmi
     */
    public float getBmi() {
        return bmi;
    }

    /**
     * Set the value of bmi
     *
     * @param bmi new value of bmi
     */
    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    /**
     * Get the value of bone
     *
     * @return the value of bone
     */
    public float getBone() {
        return bone;
    }

    /**
     * Set the value of bone
     *
     * @param bone new value of bone
     */
    public void setBone(float bone) {
        this.bone = bone;
    }

    /**
     * Get the value of water
     *
     * @return the value of water
     */
    public float getWater() {
        return water;
    }

    /**
     * Set the value of water
     *
     * @param water new value of water
     */
    public void setWater(float water) {
        this.water = water;
    }

    /**
     * Get the value of bodyFat
     *
     * @return the value of bodyFat
     */
    public float getBodyFat() {
        return bodyFat;
    }

    /**
     * Set the value of bodyFat
     *
     * @param bodyFat new value of bodyFat
     */
    public void setBodyFat(float bodyFat) {
        this.bodyFat = bodyFat;
    }

    /**
     * Get the value of weight
     *
     * @return the value of weight
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Set the value of weight
     *
     * @param weight new value of weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Get the value of muscle
     *
     * @return the value of muscle
     */
    public float getMuscle() {
        return muscle;
    }

    /**
     * Set the value of muscle
     *
     * @param muscle new value of muscle
     */
    public void setMuscle(float muscle) {
        this.muscle = muscle;
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
        return "BloodPressure{" + "id=" + id + ", date=" + date + ", weight=" + weight + ", bodyFat=" + bodyFat + ", muscle=" + muscle + ", water=" + water + ", bone=" + bone + "}";
    }
}
