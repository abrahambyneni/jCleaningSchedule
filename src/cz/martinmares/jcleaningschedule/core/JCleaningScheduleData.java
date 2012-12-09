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

import cz.martinmares.jcleaningschedule.dateutil.YearWeekDate;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * This class handle saving of application data.
 * Class saves data to <code>xml</code> file through <code>Properties</code> class. 
 * @author Martin Mareš
 * @see java.util.Properties
 */

public class JCleaningScheduleData {
    
    static final String DATA_FILE="data.xml";
    static final String FILE_COMMENT="jCleaningSchedule data file";
    static final String ENCODING="UTF-8";
    static final String FILE_VERSION="3.0";
    
    private String file;
    private int decisiveYear;
    private String main[];
    private String floors[][];
    
    private Color ScBgColor = Main.DEFAULT_SC_BG_COLOR;
    private Color ScTxtColor = Main.DEFAULT_SC_TXT_COLOR;
    private String defaultFontName = Main.DEFAULT_FONT_NAME;
    private int weekStarts = GregorianCalendar.getInstance().
            getFirstDayOfWeek() - GregorianCalendar.SUNDAY;
    
    private boolean autoUpdatesEnabled = Main.DEFAULT_AUTO_UPDATES_ENABLED;

    /**
     * Create a new JCleaningScheduleData class with default file location.
     * @throws DataNotFoundException if file wasn't found
     * @throws IOException if an error occurred through reading the file. 
     */
    public JCleaningScheduleData() throws DataNotFoundException, IOException {
       this(DATA_FILE);
    }
    
    /**
     * Create a new JCleaningScheduleData class with default settings.
     * This constructor doesn't load anything from file. Some data saves into RAM.
     * @param decisiveYear The year in which had started scheduling plans.
     * @param main array of people who do main cleaning
     * @param floors array of array (floor) of people who do floor cleaning
     */
    public JCleaningScheduleData(int decisiveYear, String[] main, String floors[][]) {
        setData(decisiveYear, main, floors);
        file = DATA_FILE;
    }
    
    /**
     * Create a new JCleaningScheduleData class with specified file location.
     * @param file - url of file
     * @throws DataNotFoundException if file wasn't found
     * @throws IOException if an error occurred through reading the file. 
     */
    public JCleaningScheduleData(String file) throws DataNotFoundException, IOException {
        reload(file);
    }

    /**
     * Returns an array of people who do main cleaning.
     * @return array of people
     */
    public String[] getMain() {
        return main;
    }
    
    /**
     * Returns an array of people who do cleaning in specified floor.
     * @param num floor id
     * @return array of people
     */
    public String[] getFloor(int num) {
        return floors[num];
    }
    
    /**
     * Returns array of array (floor) of people who do floor cleaning.
     * @return array of array of people
     */        
    public String[][] getFloors() {
        return floors;
    }

    /**
     * Returns decisive year.
     * The year in which had started scheduling plans.
     * @return decisive year
     */
    public int getDecisiveYear() {
        return decisiveYear;
    }
    
    /**
     * Returns number of floors.
     * @return number of floors
     */    
    public int getNumOfFloors() {
        return floors.length;
    }
    /**
     * Returns array of people who do cleaning.
     * @param column id of place (main = -1; floor id);
     * @return array of names of people
     */
    public String[] getNames(int column) {
        if(column==-1) {
            return getMain();
        } else {
            return getFloor(column);
        }
    }
    
    /**
     * Returns date based on current settings.
     * @param date date of week
     * @param first true - first day of week; false - last day of week
     * @return date in array of <code>int</code> in format {DAY_OF_MONTH, MONTH, YEAR}
     */
    public int[] getDateOfWeek(YearWeekDate date, boolean first) {
        GregorianCalendar gc = date.getGregorianCalendar();
        gc.setFirstDayOfWeek(GregorianCalendar.SUNDAY + getWeekStarts());
        int day = getWeekStarts();
        if(!first) {
            day = (day+6)%7;
        }
        day+=GregorianCalendar.SUNDAY;
        gc.set(GregorianCalendar.DAY_OF_WEEK, day);
        return new int[] {gc.get(GregorianCalendar.DAY_OF_MONTH), 
            gc.get(GregorianCalendar.MONTH)+1, gc.get(GregorianCalendar.YEAR)};
    }
    
    /**
     * Tells if auto updates is enabled.
     * @return true - enabled; false; disabled
     */
    public boolean isAutoUpdatesEnabled() {
        return autoUpdatesEnabled;
    }
    
    /**
     * Enables auto updates.
     * @param autoUpdates true - enabled; false - diabled
     */
    public void setAutoUpdatesEnabled(boolean autoUpdates) {
        this.autoUpdatesEnabled = autoUpdates;
    }
    
    /**
     * Retruns color of background on each second row in table for print.
     * @retun color of background
     */    
    public Color getScBgColor() {
        return ScBgColor;
    }
    
    /**
     * Sets color of background on each second row in table for print.
     * @param c color of background
     */
    public void setScBgColor(Color c) {
        ScBgColor = c;
    }

    /**
     * Returns color of text on each second row in table for print.
     * @return color of text
     */
    public Color getScTxtColor() {
        return ScTxtColor;
    }
    
    /**
     * Sets color of text on each second row in table for print.
     * @param c color of text
     */
    public void setScTxtColor(Color c) {
        this.ScTxtColor = c;
    }
    
    /**
     * Retuns the default font name.
     * @return font name.
     */
    public String getDefaultFontName() {
        return defaultFontName;
    }
    
    /**
     * Sets the default font name.
     * @param dafaultFontName font name
     */
    public void setDefaultFontName(String dafaultFontName) {
        this.defaultFontName = dafaultFontName;
    }
    
    /**
     * Gets first day of week.
     * @return First day from week based on GregorianCalendar constants.
     * @see java.util.GregorianCalendar;
     */
    public int getWeekStarts() {
        return weekStarts;
    }
    
    /**
     * Sets first day of week.
     * @param weekStarts  first day from week based on GregorianCalendar constants.
     * @see java.util.GregorianCalendar;
     */
    public void setWeekStarts(int weekStarts) {
        this.weekStarts = weekStarts;
    }
    
    /**
     * Sets some basic data to RAM.
     * @param decisiveYear The year in which had started scheduling plans.
     * @param main array of people who do main cleaning
     * @param floors array of array (floor) of people who do floor cleaning
     */
    public void setData(int decisiveYear, String[] main, String floors[][]) {
        this.decisiveYear = decisiveYear;
        this.main = main;
        this.floors = floors;
    }
    
    /**
     * Saves data from RAM to file.
     * @throws IOException if an error occurred through saving to file.
     */
    public void saveData() throws IOException {
        Properties prop = new  Properties();
        prop.setProperty("version", FILE_VERSION);
        //General
        prop.setProperty("year", ""+decisiveYear);
        prop.setProperty("weekStarts", ""+weekStarts);
        //Main
        prop.setProperty("main.count", ""+main.length);
        for(int i=0;i<main.length;i++) {
            prop.setProperty("main."+i, main[i]);
        }
        //Floors
        prop.setProperty("floor.count", ""+floors.length);
        for(int f = 0;f<floors.length;f++) {
            prop.setProperty("floor."+f+".count", ""+floors[f].length);
            for(int i = 0;i<floors[f].length;i++) {
                prop.setProperty("floor."+f+"."+i, floors[f][i]);
            }
        }
        //Optimal
        prop.setProperty("optimal.scBgColor", ""+ScBgColor.getRGB());
        prop.setProperty("optimal.scTxtColor", ""+ScTxtColor.getRGB());
        prop.setProperty("optimal.defaultFontName", defaultFontName);
        String bol;
        if(autoUpdatesEnabled) {
            bol="true";
        }
        else {
            bol="false";
        }
        prop.setProperty("optimal.autoUpdatesEnabled", bol);
        
        try  (FileOutputStream out = new  FileOutputStream(file, false)) {
            prop.storeToXML(out, FILE_COMMENT, ENCODING);
        } catch  (IOException ioe) {
            System.err.println("Problem processing file " + file);
            throw ioe;
        }
    }
    
    /**
     * Reloads data to RAM from specified file.
     * @param file url of file
     * @throws DataNotFoundException if file wasn't found
     * @throws IOException if an error occurred through reading the file. 
     */
    private void reload(String file) throws DataNotFoundException, IOException {
        try  (FileInputStream fis = new  FileInputStream(file)) {
            Properties prop = new  Properties();
            prop.loadFromXML(fis);
            String ver=prop.getProperty("version");
            if (!ver.equals(FILE_VERSION)) {
                System.err.println("The file \""+file+"\" is in version "+ver+
                        " but this app is designed for version "+ FILE_VERSION);
                System.err.println("Settings file could be damaged.");
            }
            //General
            decisiveYear=Integer.valueOf(prop.getProperty("year"));
            weekStarts = Integer.valueOf(prop.getProperty("weekStarts"));
            //Main
            main=new String[Integer.valueOf(prop.getProperty("main.count"))];
            for(int i=0;i<main.length;i++) {
                main[i]=prop.getProperty("main."+i);
            }
            //Floors
            floors=new String[Integer.valueOf(prop.getProperty("floor.count"))][];
            for(int f=0;f<floors.length;f++) {
                floors[f]=new String[Integer.valueOf(prop.getProperty("floor."+f+".count"))];
                for(int i = 0;i<floors[f].length;i++) {
                    floors[f][i]=prop.getProperty("floor."+f+"."+i);
                }
            }
            //Optimal
            ScBgColor = new Color(Integer.valueOf(prop.getProperty("optimal.scBgColor")));
            ScTxtColor = new Color(Integer.valueOf(prop.getProperty("optimal.scTxtColor")));
            defaultFontName = prop.getProperty("optimal.defaultFontName");
            if(prop.getProperty("optimal.autoUpdatesEnabled").equals("false")) {
                autoUpdatesEnabled=false;
            } else {
                autoUpdatesEnabled=true;
            }
            this.file=file;
        } catch  (FileNotFoundException fnfe) {
            System.err.println("The file " + file + " was not found!");
            throw new DataNotFoundException("The file " + file + " was not found!");
        } catch  (IOException ioe) {
            System.err.println("Problem loading file " + file);
            throw ioe;
        }
    
    }

}
