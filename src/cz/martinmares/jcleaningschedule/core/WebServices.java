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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WebServices handle some bacic web actions.
 * This class handle basic web activities such as updates, usage statistic etc...
 * @author Martin Mareš
 */
public class WebServices {
    
   /**
    * Returns <code>InputStream</code> from specific url.
    * Opens specific web page with specialy setted HTTP headers.
    * @param url url to be opened
    * @return <code>InputStream</code> of specific web page
    * @throws IOException if an error occurred through connecting to the server.
    */
    public static InputStream openUrl(String url) throws IOException {
        HttpURLConnection con;
        con = (HttpURLConnection) (new URL(url)).openConnection();
        con.setInstanceFollowRedirects(true);
        con.setRequestProperty("User-Agent", "Mozilla/5.0 ("+System.getProperty("os.name")+"; U; JVM; en-US; rv:1.9.2.2) Gecko/20100316 Firefox");
        con.setRequestProperty("Referer", "http://v"+Main.APP_VERSION);
        if(con.getHeaderField("Location")!=null) {
            return openUrl(con.getHeaderField("Location"));
        }
        else {
            return con.getInputStream();
        }
    }
    
   /**
    * Tries to tell authors about activation of application.
    * This method tries to open url to increase activations counter.
    */    
    public static void tellAutohorsAboutActivation() {
        try {
            openUrl(Main.FIRST_RUN_URL);
        } catch (IOException ex) {
            System.err.printf("I can't tell autors about your activation of app!");
            Logger.getLogger(WebServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   /**
    * Returns lasted application version on the web.
    * @return Lasted version, or "ERROR"
    */ 
    public static String getLastVersion() {
        Scanner s;
        try {
            s = new Scanner(openUrl(Main.UPDATE_VERSION_URL));
            return s.nextLine();
        } catch (IOException ex) {
            System.err.printf("I can't check for updates!");
            Logger.getLogger(WebServices.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR";
        }
    }
   
   /**
    * Returns changelog from web.
    * @return The change log or <code>null</code> if any error has occurred
    */     
    public static Scanner getChangelog() {
        try {
            return new Scanner(openUrl(Main.UPDATE_CHANGELOG_URL));
        } catch (IOException ex) {
            System.err.printf("I can't download changelog!");
            Logger.getLogger(WebServices.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
