/*
 * jCleaningSchedule - program for printing house cleaning schedules
 * Copyright (C) 2012  Martin Mareš <mmrmartin[at]gmail[dot]com>
 *
 * This file is part of jCleaningSchedule.
 *
 * jCleaningSchedule is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jCleaningSchedule is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jCleaningSchedule.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.martinmares.jcleaningschedule.dateutil;

import java.util.GregorianCalendar;

/**
 * This class helps with transfering dates. 
 * Class saves only year and week of year.
 * @author Martin Mareš
 */
public class YearWeekDate {
    
    int year;
    int week;
    
    /**
     * Create a new YearWeekDate class with specified date.
     * @param year year
     * @param week week of year
     */
    public YearWeekDate(int year, int week) {
        this.year = year;
        this.week = week;
    }
   
    /**
     * Sets specified date.
     * @param year year
     * @param week week of year
     */
    public void setDate(int year, int week) {
        this.year = year;
        this.week = week;
    }
    
    /**
     * Returns year.
     * @return year
     */
    public int getYear() {
        return year;
    }
    
    /**
     * Return week of year.
     * @return week of year.
     */
    public int getWeek() {
        return week;
    }
    
    /**
     * Returns date represented with GregorianCalendar.
     * @return date
     * @see java.util.GregorianCalendar
     */
    public GregorianCalendar getGregorianCalendar() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.YEAR, year);
        gc.set(GregorianCalendar.WEEK_OF_YEAR, week);
        return gc;
    }
}
