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

package cz.martinmares.jcleaningschedule.core;

import cz.martinmares.jcleaningschedule.dateutil.DateUtil;
import cz.martinmares.jcleaningschedule.dateutil.YearWeekDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The core class of this application.
 * This class handle all basic operation in application - like finding who does
 * what etc...
 * @author Martin Mareš
 */
public class Core extends YearWeekDate {
    JCleaningScheduleData data;
    //Index of people who do:
    int MN; //main cleaning
    int F[]; //Floors

    /**
     * Create a new Core class with specified date. 
     * @param year begining year
     * @param week begining week
     * @param indata application setting
     */
    public Core(int year, int week, JCleaningScheduleData indata) {
            super(year, week);
            data = indata;
            F = new int[data.getNumOfFloors()];
            dateUpdated();
    }
    
    /**
     * Create a new Core class with specified date. 
     * @param date date of begin
     * @param indata 
     */    
    public Core(YearWeekDate date, JCleaningScheduleData indata) {
            this(date.getYear(), date.getWeek(), indata);
    }
    
    /**
     * Change actual date to specific one.
     */ 
    @Override
    public void setDate(int year, int week) {
        super.setDate(year, week);
        dateUpdated();
    }
    
    /**
     * Protected method whitch is called after change of date.
     */    
    final protected void dateUpdated() {
        int numWeek;
        if (getYear() >= data.getDecisiveYear()) {
            numWeek = DateUtil.getWeeksCount(data.getDecisiveYear(), getYear()) + getWeek();
        } else {
            numWeek = (DateUtil.getWeeksCount(getYear(), data.getDecisiveYear())) + getWeek();
        }
        numWeek--;
        if(data.getMain().length!=0) {
            MN = numWeek % data.getMain().length;
        }
        for(int i=0;i<F.length;i++) {
            if(data.getFloor(i).length != 0) {
                F[i]=numWeek % data.getFloor(i).length;
            }
        }       
    }
    
    /**
     * Change actual date to next week.
     */    
    public synchronized void nextWeek() {
        if(data.getMain().length != 0) {
            MN++;
            MN = MN % data.getMain().length;
        }
        for(int i=0;i<F.length;i++) {
            if(data.getFloor(i).length != 0) {
                F[i]++;
                F[i] = F[i] % data.getFloor(i).length;
            }
        }
        if(getWeek()+1<=DateUtil.getWeeksCount(getYear())) {
            super.setDate(getYear(), getWeek()+1);
        }
        else {
            super.setDate(getYear()+1, 1);
        }
    }
    
    /**
     * Gives a name of person who does cleaning.
     * @param column id of place (main = -1; floor id);
     * @return Name of person who does cleaning.
     * @see getMain()
     * @see getF(int)
     */  
    public String getPeople(int column) {
        if(column==-1) {
            return getMain();
        }
        else {
            return getF(column);
        }
    }
    
    /**
     * Gives an array of indexes of floors where is at least one person.
     * @return int array with indexes (Main = -1)
     */    
    public int[] getColumnWithPeople() {
        List<Integer> intList = new ArrayList<>();
        if(data.getMain().length!=0) {
            intList.add(-1);
        }
        for(int c=0;c<data.getNumOfFloors();c++) {
            if(data.getFloor(c).length!=0) {
                intList.add(c);
            }
        }
        int ret[] = new int[intList.size()];   
        for (int i = 0; i < ret.length; i++) {
            ret[i] = intList.get(i);
        }
        return ret;
    }

    /**
     * Gives a name of person who does main cleaning.
     * @return Name of person who does main cleaning.
     */
    public String getMain() {
        return data.getMain()[MN];
    }
    
    /**
     * Gives a name of person who does cleaning in floor.
     * @param floor id of floor
     * @return Name of person who does cleaning in floor.
     */
    public String getF(int floor) {
        return data.getFloor(floor)[F[floor]];
    }    
}
