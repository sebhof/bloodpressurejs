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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author shofmann
 */
@XmlRootElement
public class BloodPressureReport implements Serializable {

    private List<BloodPressureReportItem> items = new ArrayList<>();

    private Date from;

    private Date to;

    public BloodPressureReport() {
    }

    public BloodPressureReport(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Get the value of to
     *
     * @return the value of to
     */
    public Date getTo() {
        return to;
    }

    /**
     * Set the value of to
     *
     * @param to new value of to
     */
    public void setTo(Date to) {
        this.to = to;
    }

    /**
     * Get the value of from
     *
     * @return the value of from
     */
    public Date getFrom() {
        return from;
    }

    /**
     * Set the value of from
     *
     * @param from new value of from
     */
    public void setFrom(Date from) {
        this.from = from;
    }

    /**
     * Get the value of items
     *
     * @return the value of items
     */
    public List<BloodPressureReportItem> getItems() {
        return items;
    }

    /**
     * Set the value of items
     *
     * @param items new value of items
     */
    public void setItems(List<BloodPressureReportItem> items) {
        this.items = items;
    }

    /**
     * Adds a new Item to the report
     *
     * @param item Item to add
     */
    public void addItem(BloodPressureReportItem item) {
        this.items.add(item);
    }

    /**
     * Sorts all items by date in ascending order
     */
    public void sortItems() {
        this.items.sort(new Comparator<BloodPressureReportItem>() {
            @Override
            public int compare(BloodPressureReportItem o1, BloodPressureReportItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

}
