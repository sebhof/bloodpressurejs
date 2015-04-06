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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import static de.shofmann.bloodpressurejs.Tools.*;

/**
 *
 * @author shofmann
 */
@Stateless
public class BloodPressureBean {

    @PersistenceContext(unitName = "bloodpressurejsPU")
    private EntityManager em;

    @Resource
    SessionContext sessionContext;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveBloodPressure(BloodPressure bloodPressure) {
        this.em.persist(bloodPressure);
    }

    public BloodPressureReport createReport(Date from, Date to) {
        
        BloodPressureBean self = this.sessionContext.getBusinessObject(BloodPressureBean.class);
        
        List<BloodPressure> bloodPressures = self.getBloodPressure(from, to);
        BloodPressureReport report = new BloodPressureReport(from, to);
        for (BloodPressure bloodPressure : bloodPressures) {
            report.addItem(new BloodPressureReportItem(bloodPressure));
        }
        report.sortItems();
        
        // calculate averages
        Calendar averageFrom = Calendar.getInstance();
        averageFrom.setTime(to);
        averageFrom.add(Calendar.DAY_OF_MONTH, -7);
        bloodPressures = self.getBloodPressure(getBeginOfDayDate(averageFrom.getTime()), to);
        report.setAverage7Days(self.calculateAverage(bloodPressures));
        
        averageFrom = Calendar.getInstance();
        averageFrom.setTime(to);
        averageFrom.add(Calendar.DAY_OF_MONTH, -30);
        bloodPressures = self.getBloodPressure(getBeginOfDayDate(averageFrom.getTime()), to);
        report.setAverage30Days(self.calculateAverage(bloodPressures));
        
        averageFrom = Calendar.getInstance();
        averageFrom.setTime(to);
        averageFrom.add(Calendar.DAY_OF_MONTH, -90);
        bloodPressures = self.getBloodPressure(getBeginOfDayDate(averageFrom.getTime()), to);
        report.setAverage90Days(self.calculateAverage(bloodPressures));
        
        return report;
    }

    public List<BloodPressure> getBloodPressure(Date from, Date to) {
        return this.em.createQuery("SELECT bp FROM BloodPressure bp WHERE bp.date BETWEEN :from AND :to")
                .setParameter("from", from, TemporalType.TIMESTAMP)
                .setParameter("to", to, TemporalType.TIMESTAMP)
                .getResultList();
    }
    
    public BloodPressureReportItem calculateAverage(List<BloodPressure> bloodPressures) {
        int systoleSum = 0;
        int diastoleSum = 0;
        int rateSum = 0;
        for (BloodPressure bloodPressure : bloodPressures) {
            systoleSum += bloodPressure.getSystole();
            diastoleSum += bloodPressure.getDiastole();
            rateSum += bloodPressure.getRate();
        }
        float systoleAverage = systoleSum / bloodPressures.size();
        float diastoleAverage = diastoleSum / bloodPressures.size();
        float rateAverage = rateSum / bloodPressures.size();
        BloodPressureReportItem item = new BloodPressureReportItem();
        item.setSystole(Math.round(systoleAverage));
        item.setDiastole(Math.round(diastoleAverage));
        item.setRate(Math.round(rateAverage));
        item.calculateWHOState();
        return item;
    }
}
