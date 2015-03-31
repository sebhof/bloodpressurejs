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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static javax.faces.component.UIInput.isEmpty;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author shofmann
 */
public class DateParam {

    private final Date date;

    public DateParam(String dateStr) throws WebApplicationException {
        if (isEmpty(dateStr)) {
            this.date = null;
            return;
        }
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
                    .entity("Couldn't parse date string: " + e.getMessage())
                    .build());
        }
    }

    public Date getDate() {
        return date;
    }
}
