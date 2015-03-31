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
package de.shofmann.bloodpressurejs.service;

import de.shofmann.bloodpressurejs.BloodPressure;
import de.shofmann.bloodpressurejs.DateParam;
import de.shofmann.bloodpressurejs.ImportResult;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author shofmann
 */
@Stateless
@Path("bloodpressure")
@Consumes({"application/json"})
@Produces({"application/json"})
public class BloodPressureService {

    private static final Logger LOG = Logger.getLogger(BloodPressureService.class.getName());
    
    @PersistenceContext(unitName = "bloodpressurejsPU")
    private EntityManager em;
    
    @Resource
    SessionContext sessionContext;
    
    @GET
    @Path("{from}/{to}")
    public List<BloodPressure> get(@PathParam("from") DateParam from, @PathParam("to") DateParam to) {
        return this.em.createQuery("SELECT bp FROM BloodPressure bp WHERE bp.date BETWEEN :from AND :to ORDER BY bp.date DESC")
                .setParameter("from", from.getDate(), TemporalType.TIMESTAMP)
                .setParameter("to", to.getDate(), TemporalType.TIMESTAMP)
                .getResultList();
    }
    
    @POST
    public Response create(List<BloodPressure> bloodPressures) {
        BloodPressureService self = this.sessionContext.getBusinessObject(BloodPressureService.class);
        int successCount = 0;
        int errorCount = 0;
        for (BloodPressure bloodPressure : bloodPressures) {
            try {
                self.saveBloodPressure(bloodPressure);
                successCount++;
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error storing bloodpressure ["+bloodPressure.toString()+"]", e);
                errorCount++;
            }
        }
        ImportResult result = new ImportResult();
        result.setErrorCount(errorCount);
        result.setSuccessCount(successCount);
        Response response = null;
        if(errorCount != 0) {
            response = Response.status(Response.Status.NOT_ACCEPTABLE).entity(result).build();
        } else {
            response = Response.ok(result).build();
        }
        return response;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveBloodPressure(BloodPressure bloodPressure) {
        this.em.persist(bloodPressure);
    }
    
}
