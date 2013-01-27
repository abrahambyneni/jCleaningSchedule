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

import cz.martinmares.jcleaningschedule.core.Main;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * The about frame of app.
 * @author Martin Mareš
 */
public class AboutFrame extends javax.swing.JDialog {
    
    LicenseAgreementFrame licenseFrame;

    /**
     * Creates new form AboutFrame
     */
    public AboutFrame(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitle = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButtonLicense = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabelDes = new javax.swing.JLabel();
        jLabelTranslate = new javax.swing.JLabel();
        jLabelAutor = new javax.swing.JLabel();
        jLabelVersion = new javax.swing.JLabel();
        jLabelLicense = new javax.swing.JLabel();
        jLabelHomepage = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("cz/martinmares/jcleaningschedule/resources/lang"); // NOI18N
        setTitle(bundle.getString("ABOUT")); // NOI18N
        setResizable(false);

        jLabelTitle.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        jLabelTitle.setText("jCleaningSchedule");

        jLabel2.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel2.setText(bundle.getString("ABOUT_AUTOR")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel3.setText(bundle.getString("ABOUT_VERSION")); // NOI18N

        jLabel4.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel4.setText(bundle.getString("ABOUT_TRANSLATE")); // NOI18N

        jLabel5.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel5.setText(bundle.getString("ABOUT_LICENSE")); // NOI18N

        jButtonLicense.setText(bundle.getString("ABOUT_SHOW_LICENSE")); // NOI18N
        jButtonLicense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLicenseActionPerformed(evt);
            }
        });

        jButtonClose.setText(bundle.getString("DIALOG_CLOSE")); // NOI18N
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/martinmares/jcleaningschedule/gui/logo.jpg"))); // NOI18N

        jLabelDes.setText(bundle.getString("ABOUT_DESCRIPTION")); // NOI18N

        jLabelTranslate.setText(bundle.getString("TRANSLATION_AUTOR")); // NOI18N

        jLabelAutor.setText("<html>Martin Mareš<br>mmrmartin[at]gmail[dot]com</html>");

        jLabelVersion.setText(Main.APP_VERSION);

        jLabelLicense.setText("GPLv3");

        jLabelHomepage.setText("<html><a href="+Main.APP_HOMEPAGE+">"+Main.APP_HOMEPAGE+"</a></html>");
        jLabelHomepage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabelHomepageMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonLicense)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonClose))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelTitle)
                            .addComponent(jLabelDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelLicense)
                                    .addComponent(jLabelVersion)
                                    .addComponent(jLabelAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelTranslate)))
                            .addComponent(jLabelHomepage))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelHomepage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabelVersion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabelAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabelTranslate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabelLicense))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonLicense)
                            .addComponent(jButtonClose)))
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonLicenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLicenseActionPerformed
        if(licenseFrame==null) {
            licenseFrame = new LicenseAgreementFrame(null);
            licenseFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            licenseFrame.setFirstRunVersion(false);
            licenseFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        }
        licenseFrame.setVisible(true);
    }//GEN-LAST:event_jButtonLicenseActionPerformed

    private void jLabelHomepageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelHomepageMousePressed
            try {
                Desktop.getDesktop().browse(new URI(Main.APP_HOMEPAGE));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(AboutFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_jLabelHomepageMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonLicense;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelAutor;
    private javax.swing.JLabel jLabelDes;
    private javax.swing.JLabel jLabelHomepage;
    private javax.swing.JLabel jLabelLicense;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelTranslate;
    private javax.swing.JLabel jLabelVersion;
    // End of variables declaration//GEN-END:variables
}