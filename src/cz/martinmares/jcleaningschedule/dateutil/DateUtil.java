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
 * This class does the most of dates operation in application.
 * <code>DateUtil</code> is used in many situations, because default java calendar
 * class <code>GregorianCalendar</code> doesn't provide everything needed.
 * @author Martin Mareš
 * @see java.util.GregorianCalendar
 */
 
public class DateUtil {
    
    final static int MILLIS_IN_WEEK = 1000*60*60*24*7;
    
     /**
     * It returns true if the year has 53 weeks or it returns false. 
     * @return true - 53 weeks; false - 52 weeks
     */
    public static boolean isLongWeekYear(int year) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.YEAR, year);
        if (gc.isLeapYear(year) == true) {
            gc.set(GregorianCalendar.DAY_OF_YEAR, 366);
        } else {
            gc.set(GregorianCalendar.DAY_OF_YEAR, 365);
        }
        if (gc.get(GregorianCalendar.WEEK_OF_YEAR) == 53) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Retruns number of weeks in year.
     * @param year year 
     * @return number of weeks
     */
    public static int getWeeksCount(int year) {
        if(isLongWeekYear(year)) {
            return 53;
        }
        else {
            return 52;
        }
    }
    
    /**
     * It counts number of weeks between years (without last year)
     * @param begin_year Starting year
     * @param end_year Ending year (without)
     * @return Count of weeks between years 
     */
    public static int getWeeksCount(int begin_year, int end_year) {
        return getWeeksCount(new YearWeekDate(begin_year,1),
                new YearWeekDate(end_year,1));
    }
    
    /**
     * It counts number of weeks between years (without last year)
     * @param begin begining date
     * @param end ending data (without)
     * @return Count of weeks between years
     */
    public static int getWeeksCount(YearWeekDate begin, YearWeekDate end) {        
        return (int)((end.getGregorianCalendar().getTimeInMillis()
                -begin.getGregorianCalendar().getTimeInMillis())/MILLIS_IN_WEEK);
        //TODO: make it more safety
    }
    
}
