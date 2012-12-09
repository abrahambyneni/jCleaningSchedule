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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * The GUI extension of <code>PrintCore</code>.
 * @author Martin Mareš
 * @see PrintCore
 */
public class GuiPrintCore extends PrintCore {
    
    Component parent;
    private static final ResourceBundle str = ResourceBundle.getBundle("cz/martinmares/jcleaningschedule/resources/lang");
    
    public GuiPrintCore(PrintData inpd, JCleaningScheduleData indata, 
            Component parent) {
        super(inpd, indata);
        this.parent = parent;
    }
    
    public void setParentComponent(Component c) {
        parent = c;
    }
    
    public Component getParentComponent() {
        return parent;
    }
    
    @Override
    public boolean printAll() {
        if(printerJob.printDialog(printAtt)) {
            try {
                super.printAll();
                JOptionPane.showMessageDialog(parent, 
                    str.getString("PRINT_SENT"),
                    str.getString("PRINT_SUCCESSFUL"),
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(parent, 
                    str.getString("PRINT_ERROR")+ex.toString(),
                    str.getString("DIALOG_ERROR"),
                    JOptionPane.INFORMATION_MESSAGE);
                Logger.getLogger(GuiPrintCore.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean pageDialog() {
        PageFormat pf = printerJob.pageDialog(printAtt);
        if(pf!=null) {
            setPageFormat(pf);
            return true;
        }
        return false;
    }
    
    public Image getImg(int page) {
        try {
            Image img = new BufferedImage((int)pageFormat.getWidth()
                    ,(int)pageFormat.getHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            print(g, pageFormat, page);
            return img;
        } catch (PrinterException ex) {
            throw new IllegalArgumentException("I can't draw page "+page);                       
        }
    }
}
