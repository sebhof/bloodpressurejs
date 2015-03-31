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

/**
 *
 * @author shofmann
 */
public class ImportResult {

    private int successCount;

    private int errorCount;

    /**
     * Get the value of errorCount
     *
     * @return the value of errorCount
     */
    public int getErrorCount() {
        return errorCount;
    }

    /**
     * Set the value of errorCount
     *
     * @param errorCount new value of errorCount
     */
    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    /**
     * Get the value of successCount
     *
     * @return the value of successCount
     */
    public int getSuccessCount() {
        return successCount;
    }

    /**
     * Set the value of successCount
     *
     * @param successCount new value of successCount
     */
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

}
