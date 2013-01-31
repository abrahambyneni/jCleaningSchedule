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

import cz.martinmares.jcleaningschedule.core.Core;
import cz.martinmares.jcleaningschedule.core.JCleaningScheduleData;
import cz.martinmares.jcleaningschedule.dateutil.DateUtil;
import cz.martinmares.jcleaningschedule.print.GuiPrintCore;
import cz.martinmares.jcleaningschedule.print.PrintData;
import java.text.MessageFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * The main window of jCleaningSchedule.
 * @author Martin Mareš
 */
public class MainFrame extends javax.swing.JFrame {
    
    JCleaningScheduleData data;
    SettingsFrame settingsFrame;
    AboutFrame aboutFrame;
    PrintPreviewFrame printPreviewFrame;
    GuiPrintCore printCore;
    UpdateFrame updateFrame;
    
    private boolean onePage = true;
    private static final ResourceBundle str = ResourceBundle.getBundle("cz/martinmares/jcleaningschedule/resources/lang");
    
    static final DefaultTableModel defaultTableModel = new DefaultTableModel(
            new Object [][] {}, new String [] {str.getString("MAIN_WEEK"),str.getString("MAIN_BEGIN"),str.getString("MAIN_END")}) {
        @Override
        public Class getColumnClass(int columnIndex) {
            return java.lang.String.class;
        }
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };
            

    /**
     * Creates new form MainFrame
     */
    public MainFrame(JCleaningScheduleData indata) {
        data = indata;       
        
        initComponents(); 
        printCore = new GuiPrintCore(getPrintData(), data, this);

        //Set first 3 column a little closer
        jTableYear.getColumnModel().getColumn(0)
                .setPreferredWidth(
                jTableYear.getFontMetrics(jTableYear.getFont()).stringWidth(str.getString("MAIN_WEEK"))
                );
        jTableYear.getColumnModel().getColumn(1)
                .setPreferredWidth(
                jTableYear.getFontMetrics(jTableYear.getFont()).stringWidth("88.88.")
                );
        jTableYear.getColumnModel().getColumn(2)
                .setPreferredWidth(
                jTableYear.getFontMetrics(jTableYear.getFont()).stringWidth("88.88.")
                );
        //Alligmnet columns to center
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment( JLabel.CENTER);
        jTableYear.setDefaultRenderer(String.class, rightRenderer);

        //Set componets value on current date
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        setBeginCompomnents(gc);

        //Table
        jSpinnerYear.setValue(gc.get(GregorianCalendar.YEAR));
        jLabelToday.setText(MessageFormat.format(str.getString("MAIN_TODAY"), 
                new Object[] {gc.get(GregorianCalendar.WEEK_OF_YEAR), gc.get(GregorianCalendar.YEAR)}));
        //Set default componets data (update table rows)
        settingsUpdated();
        //Table - Select current week
        jTableYear.getSelectionModel()
                .setSelectionInterval(gc.get(GregorianCalendar.WEEK_OF_YEAR) - 1,
                gc.get(GregorianCalendar.WEEK_OF_YEAR) - 1);
    }
    
   private void settingsUpdated() {
       reloadTable();
       printCore.updateFontData();
       optimalSettingsUpdate();
   }
    
   private synchronized void reloadTable() {
        //Basic inicialization
        final int year = (int)jSpinnerYear.getValue();
        final int weeks = DateUtil.getWeeksCount(year);
        Core core = new Core(year, 1, data);
        int column_num = data.getNumOfFloors()+3;
        //Clear the table
        ((DefaultTableModel)jTableYear.getModel()).setColumnCount(3);
        ((DefaultTableModel)jTableYear.getModel()).setRowCount(0);
        
        //Find column with any people
        int column_id[] = core.getColumnWithPeople();
        
        //Add column title
        int i=1;
        if(column_id[0]==-1) {
            ((DefaultTableModel)jTableYear.getModel())
                    .addColumn(str.getString("MAIN_MAIN_SCHEDULE"), new Object[]{});
            column_num++;
        }
        else {
            i=0;
        }
        
        for(;i<column_id.length;i++) {
            ((DefaultTableModel)jTableYear.getModel())
                        .addColumn(column_id[i]+". "+str.getString("MAIN_FLOOR"), new Object[]{});
        }
        
        //Fill rows with data        
        for(i=0;i<weeks;i++) {
            Object val[] = new Object[column_num];
            //Week
            val[0]=i+1+".";
            int date[] = data.getDateOfWeek(core, true);
            //Begin
            val[1]="";
            if(date[0]<10) {
                val[1]+="0";
            }
            val[1]+=date[0]+". ";
            if(date[1]<10) {
                val[1]+="0";
            }
            val[1]+=date[1]+".";
            //End
            date = data.getDateOfWeek(core, false);
            val[2]="";
            if(date[0]<10) {
                val[2]+="0";
            }
            val[2]+=date[0]+". ";
            if(date[1]<10) {
                val[2]+="0";
            }
            val[2]+=date[1]+".";
            //People in floors
            for(int c=0;c<column_id.length;c++) {
                val[c+3] = core.getPeople(column_id[c]);
            }
            ((DefaultTableModel)jTableYear.getModel())
                    .addRow(val);
            core.nextWeek();
        }
    }
    
   private void optimalSettingsUpdate() {
       jCheckBoxPrintMainSchedule.setEnabled(data.getMain().length!=0);
       if(data.getMain().length==0) {
           jCheckBoxPrintMainSchedule.setSelected(false);
       }
       jSpinnerFirstColumn.setEnabled(data.getNumOfFloors()!=0);
       if(data.getNumOfFloors()!=0) {
           int min;
           if(data.getFloor(0).length!=0) {
               min=0;
           }
           else {
               min=1;
           }
           jSpinnerFirstColumn.setModel(new SpinnerNumberModel(min,min,
                data.getNumOfFloors()-1,1));               
       }
    }
    
   private void updatePrintData() throws IllegalArgumentException {
      printCore.setPrintData(getPrintData());
      optimalPrintSettingsUpdated();
   }
   
   private PrintData getPrintData() throws IllegalArgumentException {
       PrintData pdata = new PrintData((int)jSpinnerBeginYear.getValue(),
               (int)jSpinnerBeginWeek.getValue(),
               (int)jSpinnerEndYear.getValue(),
               (int)jSpinnerEndWeek.getValue(),
               data);
      if(pdata.getWeeksCount()<=0) {
           throw new IllegalArgumentException("\"End\" date is before \"begin\" date");
      }
      return pdata;
   }
    
   private void setBeginCompomnents(GregorianCalendar gc) {
        jSpinnerBeginYear.setValue(gc.get(GregorianCalendar.YEAR));
        jSpinnerBeginWeek.setValue(gc.get(GregorianCalendar.WEEK_OF_YEAR));       
   }
   
   private void setTos(GregorianCalendar gc) {
        jSpinnerEndYear.setValue(gc.get(GregorianCalendar.YEAR));
        jSpinnerEndWeek.setValue(gc.get(GregorianCalendar.WEEK_OF_YEAR));      
   }
   
   protected void showDateErrDialog() {
      JOptionPane.showMessageDialog(this,  
              str.getString("MAIN_ERROR_DATES"), str.getString("DIALOG_ERROR"),
              JOptionPane.ERROR_MESSAGE);
   }
   
   private void optimalPrintSettingsUpdated() {
      printCore.getPrintData().setOptimalData(jCheckBoxPeriod.isSelected(),
              jCheckBoxSameWideColumns.isSelected(),
              jCheckBoxPrintMainSchedule.isSelected(),
              jCheckBoxOnePage.isSelected(),
              (int)jSpinnerFirstColumn.getValue());
   }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelPrint = new javax.swing.JPanel();
        jPanelBegin = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSpinnerBeginWeek = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerBeginYear = new javax.swing.JSpinner();
        jPanelEnd = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerEndWeek = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinnerEndYear = new javax.swing.JSpinner();
        jCheckBoxOnePage = new javax.swing.JCheckBox();
        jButtonPrintPreview = new javax.swing.JButton();
        jButtonPrint = new javax.swing.JButton();
        jPanelOptimal = new javax.swing.JPanel();
        jCheckBoxPeriod = new javax.swing.JCheckBox();
        jCheckBoxSameWideColumns = new javax.swing.JCheckBox();
        jCheckBoxPrintMainSchedule = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jSpinnerFirstColumn = new javax.swing.JSpinner();
        jPanelTable = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSpinnerYear = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableYear = new javax.swing.JTable();
        jLabelToday = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemPrintPreview = new javax.swing.JMenuItem();
        jMenuItemPrint = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSettings = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemQuit = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemUpdate = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("jCleaningSchedule");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("cz/martinmares/jcleaningschedule/resources/lang"); // NOI18N
        jPanelPrint.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MAIN_PRINT_PANEL"))); // NOI18N

        jPanelBegin.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MAIN_BEGIN"))); // NOI18N

        jLabel2.setLabelFor(jSpinnerBeginWeek);
        jLabel2.setText(bundle.getString("WEEK")); // NOI18N

        jSpinnerBeginWeek.setModel(new javax.swing.SpinnerNumberModel(1, 1, 52, 1));
        jSpinnerBeginWeek.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onSpinnerBeginChange(evt);
            }
        });

        jLabel3.setLabelFor(jSpinnerBeginYear);
        jLabel3.setText(bundle.getString("YEAR")); // NOI18N

        jSpinnerBeginYear.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2012), null, null, Integer.valueOf(1)));
        jSpinnerBeginYear.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onSpinnerBeginChange(evt);
                onSpinnerFromYearChange(evt);
            }
        });

        javax.swing.GroupLayout jPanelBeginLayout = new javax.swing.GroupLayout(jPanelBegin);
        jPanelBegin.setLayout(jPanelBeginLayout);
        jPanelBeginLayout.setHorizontalGroup(
            jPanelBeginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBeginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBeginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jSpinnerBeginWeek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelBeginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSpinnerBeginYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBeginLayout.setVerticalGroup(
            jPanelBeginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBeginLayout.createSequentialGroup()
                .addGroup(jPanelBeginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBeginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerBeginWeek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerBeginYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelEnd.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MAIN_END"))); // NOI18N

        jLabel4.setLabelFor(jSpinnerEndWeek);
        jLabel4.setText(bundle.getString("WEEK")); // NOI18N

        jSpinnerEndWeek.setModel(new javax.swing.SpinnerNumberModel(2, 1, 52, 1));
        jSpinnerEndWeek.setEnabled(false);

        jLabel5.setLabelFor(jSpinnerEndYear);
        jLabel5.setText(bundle.getString("YEAR")); // NOI18N

        jSpinnerEndYear.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2012), null, null, Integer.valueOf(1)));
        jSpinnerEndYear.setEnabled(false);
        jSpinnerEndYear.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerEndYearStateChanged(evt);
            }
        });

        jCheckBoxOnePage.setSelected(true);
        jCheckBoxOnePage.setText(bundle.getString("MAIN_FILL_ONE_PAGE")); // NOI18N
        jCheckBoxOnePage.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onFillOnePageStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelEndLayout = new javax.swing.GroupLayout(jPanelEnd);
        jPanelEnd.setLayout(jPanelEndLayout);
        jPanelEndLayout.setHorizontalGroup(
            jPanelEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEndLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEndLayout.createSequentialGroup()
                        .addGroup(jPanelEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinnerEndWeek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jSpinnerEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jCheckBoxOnePage))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelEndLayout.setVerticalGroup(
            jPanelEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEndLayout.createSequentialGroup()
                .addGroup(jPanelEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerEndWeek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxOnePage)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonPrintPreview.setText(bundle.getString("PRINT_PREVIEW")); // NOI18N
        jButtonPrintPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintPreviewActionPerformed(evt);
            }
        });

        jButtonPrint.setText(bundle.getString("PRINT")); // NOI18N
        jButtonPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintActionPerformed(evt);
            }
        });

        jPanelOptimal.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MAIN_OPTIMAL_SETTINGS"))); // NOI18N

        jCheckBoxPeriod.setSelected(true);
        jCheckBoxPeriod.setText(bundle.getString("PRINT_PERIOD")); // NOI18N
        jCheckBoxPeriod.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxPeriodStateChanged(evt);
            }
        });

        jCheckBoxSameWideColumns.setSelected(true);
        jCheckBoxSameWideColumns.setText(bundle.getString("MAIN_PRINT_SAME_WIDE_COLUMNS")); // NOI18N

        jCheckBoxPrintMainSchedule.setSelected(true);
        jCheckBoxPrintMainSchedule.setText(bundle.getString("MAIN_PRINT_MAIN_SCHEDULE")); // NOI18N

        jLabel6.setLabelFor(jSpinnerFirstColumn);
        jLabel6.setText(bundle.getString("MAIN_FIRST_PRINTED_FLOOR")); // NOI18N

        jSpinnerFirstColumn.setModel(new javax.swing.SpinnerNumberModel(0, 0, 4, 1));

        javax.swing.GroupLayout jPanelOptimalLayout = new javax.swing.GroupLayout(jPanelOptimal);
        jPanelOptimal.setLayout(jPanelOptimalLayout);
        jPanelOptimalLayout.setHorizontalGroup(
            jPanelOptimalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptimalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOptimalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelOptimalLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerFirstColumn))
                    .addGroup(jPanelOptimalLayout.createSequentialGroup()
                        .addGroup(jPanelOptimalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxPeriod)
                            .addComponent(jCheckBoxSameWideColumns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBoxPrintMainSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelOptimalLayout.setVerticalGroup(
            jPanelOptimalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptimalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxPeriod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxSameWideColumns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOptimalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelOptimalLayout.createSequentialGroup()
                        .addComponent(jCheckBoxPrintMainSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelOptimalLayout.createSequentialGroup()
                        .addComponent(jSpinnerFirstColumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelPrintLayout = new javax.swing.GroupLayout(jPanelPrint);
        jPanelPrint.setLayout(jPanelPrintLayout);
        jPanelPrintLayout.setHorizontalGroup(
            jPanelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrintLayout.createSequentialGroup()
                        .addComponent(jButtonPrintPreview)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonPrint))
                    .addGroup(jPanelPrintLayout.createSequentialGroup()
                        .addGroup(jPanelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelBegin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelOptimal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanelPrintLayout.setVerticalGroup(
            jPanelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrintLayout.createSequentialGroup()
                .addComponent(jPanelBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelOptimal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrint)
                    .addComponent(jButtonPrintPreview))
                .addContainerGap())
        );

        jPanelTable.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MAIN_TABLE"))); // NOI18N

        jLabel1.setLabelFor(jSpinnerYear);
        jLabel1.setText(bundle.getString("MAIN_YEAR")); // NOI18N

        jSpinnerYear.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2012), null, null, Integer.valueOf(1)));
        jSpinnerYear.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerYearStateChanged(evt);
            }
        });

        jTableYear.setModel(defaultTableModel);
        jScrollPane1.setViewportView(jTableYear);

        jLabelToday.setText("Today is ?. week of 2012");

        javax.swing.GroupLayout jPanelTableLayout = new javax.swing.GroupLayout(jPanelTable);
        jPanelTable.setLayout(jPanelTableLayout);
        jPanelTableLayout.setHorizontalGroup(
            jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTableLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelToday)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanelTableLayout.setVerticalGroup(
            jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTableLayout.createSequentialGroup()
                .addGroup(jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelToday))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenuFile.setText(bundle.getString("FILE")); // NOI18N

        jMenuItemPrintPreview.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemPrintPreview.setText(bundle.getString("PRINT_PREVIEW")); // NOI18N
        jMenuItemPrintPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPrintPreviewActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemPrintPreview);

        jMenuItemPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemPrint.setText(bundle.getString("PRINT")); // NOI18N
        jMenuItemPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPrintActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemPrint);

        jSeparator2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jMenuFile.add(jSeparator2);

        jMenuItemSettings.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSettings.setText(bundle.getString("SETTINGS")); // NOI18N
        jMenuItemSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSettingsActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSettings);

        jSeparator1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jMenuFile.add(jSeparator1);

        jMenuItemQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemQuit.setText(bundle.getString("QUIT")); // NOI18N
        jMenuItemQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemQuitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemQuit);

        jMenuBar.add(jMenuFile);

        jMenuHelp.setText(bundle.getString("HELP")); // NOI18N

        jMenuItemUpdate.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemUpdate.setText(bundle.getString("MAIN_CHECK_FOR_UPDATES")); // NOI18N
        jMenuItemUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUpdateActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemUpdate);

        jSeparator3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jMenuHelp.add(jSeparator3);

        jMenuItemAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAbout.setText(bundle.getString("ABOUT")); // NOI18N
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);

        jMenuBar.add(jMenuHelp);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemQuitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemQuitActionPerformed

    private void jMenuItemSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSettingsActionPerformed
        if(settingsFrame==null) {
            settingsFrame = new SettingsFrame(this, true, data);
            settingsFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    settingsFrame.setVisible(false);
                }
            });
            settingsFrame.setSettingsChangeListener(new SettingsChangeListener() {
                @Override
                public void onSettingsChange() {
                    settingsUpdated();
                }
            });
        }
        settingsFrame.reloadSettings();
        settingsFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItemSettingsActionPerformed

    private void jSpinnerYearStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerYearStateChanged
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                reloadTable();
            }
        });
    }//GEN-LAST:event_jSpinnerYearStateChanged

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        if(aboutFrame==null) {
            aboutFrame = new AboutFrame(this, true);
        }
        aboutFrame.setVisible(true); 
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void onSpinnerBeginChange(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_onSpinnerBeginChange
        if(onePage) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.set(GregorianCalendar.YEAR,((int)jSpinnerBeginYear.getValue()));
            gc.set(GregorianCalendar.WEEK_OF_YEAR, ((int) jSpinnerBeginWeek.getValue()) 
                    + printCore.getNumberOfRowsOnPage() - 1);
            setTos(gc);
        }
    }//GEN-LAST:event_onSpinnerBeginChange

    private void onFillOnePageStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_onFillOnePageStateChanged
        onePage = jCheckBoxOnePage.isSelected();
        jSpinnerEndWeek.setEnabled(!onePage);
        jSpinnerEndYear.setEnabled(!onePage);
        if(onePage) {
            onSpinnerBeginChange(null);
        }
    }//GEN-LAST:event_onFillOnePageStateChanged

    private void jButtonPrintPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintPreviewActionPerformed
        try {
            updatePrintData();
        } catch(IllegalArgumentException e) {
            showDateErrDialog();
            return;
        }
        if(printCore.pageDialog()) {
            printPreviewFrame = new PrintPreviewFrame(this, true, 
                    printCore);
            printPreviewFrame.setVisible(true);
        }
    }//GEN-LAST:event_jButtonPrintPreviewActionPerformed

    private void jSpinnerEndYearStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerEndYearStateChanged
        if(DateUtil.isLongWeekYear((int)jSpinnerEndYear.getValue())) { 
            ((SpinnerNumberModel)jSpinnerEndWeek.getModel()).setMaximum(53);
        }
        else {
            if((int)jSpinnerEndWeek.getValue()==53) {
                jSpinnerEndWeek.setValue(52);
            }
            ((SpinnerNumberModel)jSpinnerEndWeek.getModel()).setMaximum(52);
        }
    }//GEN-LAST:event_jSpinnerEndYearStateChanged

    private void onSpinnerFromYearChange(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_onSpinnerFromYearChange
        if(DateUtil.isLongWeekYear((int)jSpinnerBeginYear.getValue())) { 
            ((SpinnerNumberModel)jSpinnerBeginWeek.getModel()).setMaximum(53);
        }
        else {
            if((int)jSpinnerBeginWeek.getValue()==53) {
                jSpinnerBeginWeek.setValue(52);
            }
            ((SpinnerNumberModel)jSpinnerBeginWeek.getModel()).setMaximum(52);
        }
        onSpinnerBeginChange(null);
    }//GEN-LAST:event_onSpinnerFromYearChange

    private void jMenuItemPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPrintActionPerformed
        jButtonPrintActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemPrintActionPerformed

    private void jButtonPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintActionPerformed
        try {
            updatePrintData();
        } catch(IllegalArgumentException e) {
            showDateErrDialog();
            return;  
        }
        printCore.printAll();
    }//GEN-LAST:event_jButtonPrintActionPerformed

    private void jMenuItemPrintPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPrintPreviewActionPerformed
        jButtonPrintPreviewActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemPrintPreviewActionPerformed

    private void jCheckBoxPeriodStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxPeriodStateChanged
        optimalPrintSettingsUpdated();
        onSpinnerBeginChange(evt);
    }//GEN-LAST:event_jCheckBoxPeriodStateChanged

    private void jMenuItemUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUpdateActionPerformed
       updateFrame = new UpdateFrame(this, true);
       updateFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItemUpdateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPrint;
    private javax.swing.JButton jButtonPrintPreview;
    private javax.swing.JCheckBox jCheckBoxOnePage;
    private javax.swing.JCheckBox jCheckBoxPeriod;
    private javax.swing.JCheckBox jCheckBoxPrintMainSchedule;
    private javax.swing.JCheckBox jCheckBoxSameWideColumns;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelToday;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemPrint;
    private javax.swing.JMenuItem jMenuItemPrintPreview;
    private javax.swing.JMenuItem jMenuItemQuit;
    private javax.swing.JMenuItem jMenuItemSettings;
    private javax.swing.JMenuItem jMenuItemUpdate;
    private javax.swing.JPanel jPanelBegin;
    private javax.swing.JPanel jPanelEnd;
    private javax.swing.JPanel jPanelOptimal;
    private javax.swing.JPanel jPanelPrint;
    private javax.swing.JPanel jPanelTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSpinner jSpinnerBeginWeek;
    private javax.swing.JSpinner jSpinnerBeginYear;
    private javax.swing.JSpinner jSpinnerEndWeek;
    private javax.swing.JSpinner jSpinnerEndYear;
    private javax.swing.JSpinner jSpinnerFirstColumn;
    private javax.swing.JSpinner jSpinnerYear;
    private javax.swing.JTable jTableYear;
    // End of variables declaration//GEN-END:variables
}
