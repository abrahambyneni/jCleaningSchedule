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

package cz.martinmares.jcleaningschedule.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * Class for painting on frame.
 * @author Martin Mareš
 */
public class PrintPreviewCanvas extends JPanel {
    
    Image img;
    Dimension minDim = new Dimension(595,841);
    
    static final int TOP_MARGIN = 20;
    
    public void setImage(Image img) {
        minDim = new Dimension(img.getWidth(null), img.getHeight(null)+(TOP_MARGIN*2));
        this.setPreferredSize(minDim);
        this.img = img;
        this.repaint();
    }
    
    public Image getImage() {
        return img;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        //Centers the img
        int x =(int) (this.getWidth()-minDim.getWidth())/2;
        //Clears
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        //Background
        g.setColor(Color.WHITE);
        g.fillRect(x, TOP_MARGIN, (int)minDim.getWidth(), (int)minDim.getHeight()-(TOP_MARGIN*2));
        //Corners
        g.setColor(Color.BLACK);
        g.drawRect(x-1, TOP_MARGIN-1, (int)minDim.getWidth()+2, 
                (int)minDim.getHeight()+2-(TOP_MARGIN*2));
        // Draws the image to the canvas
        g.drawImage(img, x, TOP_MARGIN , null);
    }
}
