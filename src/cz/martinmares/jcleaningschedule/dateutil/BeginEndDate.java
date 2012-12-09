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

/**
 * This class handle transfering begin and end dates.
 * @author Martin Mareš
 * @see YearWeekDate;
 */
public class BeginEndDate {
    
    YearWeekDate begin;
    YearWeekDate end;
    
    /**
     * Create a new BeginEndDate with specified dates.
     * @param begin_year begining year
     * @param begin_week begining week
     * @param end_year ending year
     * @param end_week ending week
     */
    public BeginEndDate(int begin_year, int begin_week,
            int end_year, int end_week) {
        this(new YearWeekDate(begin_year, begin_week),
                new YearWeekDate(end_year, end_week));
    }
    
    /**
     * Create a new BeginEndDate with specified dates.
     * @param begin begin of period
     * @param end end of period
     */
    public BeginEndDate(YearWeekDate begin, YearWeekDate end) {
        setBegin(begin);
        setEnd(end);
    }
    
    /**
     * Sets the begin of period.
     * @param begin begin of period
     */
    public void setBegin(YearWeekDate begin) {
        this.begin = begin;
    }
    
    /**
     * Sets the end of period.
     * @param end end of period
     */
    public void setEnd(YearWeekDate end) {
        this.end = end;
    }
    
    /**
     * Returns the begin of period.
     * @return begin of period
     */
    public YearWeekDate getBegin() {
        return begin;
    }
    
    /**
     * Returns the end of period.
     * @return end of period.
     */
    public YearWeekDate getEnd() {
        return end;
    }
    
}
