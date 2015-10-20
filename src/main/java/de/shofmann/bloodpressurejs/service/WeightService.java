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

import de.shofmann.health.weight.Weight;
import de.shofmann.health.weight.WeightBean;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author shofmann
 */
@Stateless
@Path("weight")
@Consumes({"application/json"})
@Produces({"application/json"})
public class WeightService {

    private static final Logger LOG = Logger.getLogger(WeightService.class.getName());

    @EJB
    private WeightBean weightBean;

    @POST
    public Response create(List<Weight> weights) {
        int successCount = 0;
        int errorCount = 0;
        for (Weight weight : weights) {
            try {
                this.weightBean.saveWeight(weight);
                successCount++;
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error storing weight [" + weight.toString() + "]", e);
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
