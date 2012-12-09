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

package cz.martinmares.jcleaningschedule.print;

import cz.martinmares.jcleaningschedule.core.JCleaningScheduleData;
import cz.martinmares.jcleaningschedule.dateutil.BeginEndDate;
import cz.martinmares.jcleaningschedule.dateutil.DateUtil;
import java.awt.Color;

/**
 * This class handle transferring of printing data.
 * This class helps trasfering "from" and "to" dates into another classes in
 * application.
 * @author Martin Mareš
 */

public class PrintData extends BeginEndDate {
    
    JCleaningScheduleData data;
    boolean printPeriod = true;
    boolean sameWideColumns = true;
    boolean printMainSchedule = true;
    boolean fillOnePage = false;
    int firstColumn = 0;
            
    public PrintData(int begin_year, int begin_week, int end_year, int end_week, 
            JCleaningScheduleData indata) {
        super(begin_year, begin_week, end_year, end_week);
        
        data = indata;
    }
    
    public void setOptimalData(boolean printPeriod, boolean sameWideColumns,
            boolean printMainSchedule, boolean fillOnePage, int firstColumn) {
        this.printPeriod = printPeriod;
        this.sameWideColumns = sameWideColumns;
        this.printMainSchedule = printMainSchedule;
        this.fillOnePage = fillOnePage;
        this.firstColumn = firstColumn;
    }
    
    public boolean printPeriod() {
        return printPeriod;
    }
    
    public boolean printSameWideColumns() {
        return sameWideColumns;
    }
    
    public boolean printMainSchedule() {
        return printMainSchedule;
    }
    
    public boolean fillOnePage() {
        return fillOnePage;
    }
    
    public int getFirstPritableFloor() {
        return firstColumn;
    }
    
    public Color getSecondBgColor() {
        //TODO: let user to change it between printing
        return data.getScBgColor();
    }
    
    public int getWeeksCount() {
        return DateUtil.getWeeksCount(getBegin(), getEnd())+1;            
    }    
}
