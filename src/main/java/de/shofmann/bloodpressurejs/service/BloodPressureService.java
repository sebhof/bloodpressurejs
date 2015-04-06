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
import de.shofmann.bloodpressurejs.BloodPressureBean;
import de.shofmann.bloodpressurejs.BloodPressureReport;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import static de.shofmann.bloodpressurejs.Tools.*;

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

    @EJB
    private BloodPressureBean bloodPressureBean;

    @GET
    @Path("{from}/{to}")
    public BloodPressureReport get(@PathParam("from") DateParam from, @PathParam("to") DateParam to) {
        return this.bloodPressureBean.createReport(getBeginOfDayDate(from.getDate()), getEndOfDayDate(to.getDate()));
    }

    @POST
    public Response create(List<BloodPressure> bloodPressures) {
        int successCount = 0;
        int errorCount = 0;
        for (BloodPressure bloodPressure : bloodPressures) {
            try {
                this.bloodPressureBean.saveBloodPressure(bloodPressure);
                successCount++;
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error storing bloodpressure [" + bloodPressure.toString() + "]", e);
                errorCount++;
            }
        }
        ImportResult result = new ImportResult();
        result.setErrorCount(errorCount);
        result.setSuccessCount(successCount);
        Response response = null;
        if (errorCount != 0) {
            response = Response.status(Response.Status.NOT_ACCEPTABLE).entity(result).build();
        } else {
            response = Response.ok(result).build();
        }
        return response;
    }

}
