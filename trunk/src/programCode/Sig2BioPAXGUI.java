//    Sig2BioPAXv4, converts SIG flat files to BioPAX level 3
//    Copyright (C) 2010 Ryan Logan Webb
//    This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//    You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
package programCode;
import java.awt.Toolkit;
import java.io.File;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class Sig2BioPAXGUI extends javax.swing.JFrame {

    /** Creates new form Sig2BioPAXGUI */
//    ImageIcon icon = createImageIcon("..\\img\\SBCNY Logo.gif", "SBCNY");
//    ImageIcon mssm = createImageIcon("..\\img\\mssm.gif", "MSSM");
    public Sig2BioPAXGUI() {
        
        initComponents();
        inputFileTextField.setText((System.getProperty("user.dir"))+ "\\input.txt" );
        outputFileTextField.setText((System.getProperty("user.dir"))+ "\\output.owl" );
        templateTypeComboBox.addItem("sig (default)");
        templateTypeComboBox.addItem("source_target");
        templateTypeComboBox.addItem("tf_target");
        
    }
    File inFile = null;
    File outFile = null;
    Boolean overwrite = false;
    String templateType = "sig";
    private Task task;

    class Task extends SwingWorker<Void, Void> {
    /*
     * Main task. Executed in background thread.
     */
 //   @Override
    String[] p_args;
    public Task(String[] args)
    {
        p_args = new String[args.length];
        p_args = args.clone();
    }
    public Void doInBackground()
    {
      jProgressBar1.setIndeterminate(true);
      jProgressBar1.setStringPainted(false);
      Sig2BioPaxv4.main(p_args);
      return null;
    }

    /*
     * Executed in event dispatch thread
     */
    public void done() {
      Toolkit.getDefaultToolkit().beep();
      jProgressBar1.setIndeterminate(false);
      jProgressBar1.setStringPainted(true);
      runProgramButton.setEnabled(true);
    }
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        templateHelpDialog = new javax.swing.JDialog();
        templateHelpScrollPane = new javax.swing.JScrollPane();
        templateHelpTextPanel = new javax.swing.JTextPane();
        inputFileTextField = new javax.swing.JTextField();
        openFileButton = new javax.swing.JButton();
        runProgramButton = new javax.swing.JButton();
        saveFileButton = new javax.swing.JButton();
        outputFileTextField = new javax.swing.JTextField();
        overwriteCheckBox = new javax.swing.JCheckBox();
        templateTypeComboBox = new javax.swing.JComboBox();
        templateLabel = new javax.swing.JLabel();
        templateHelpButton = new javax.swing.JButton();
        logoSBCNY = new javax.swing.JLabel();
        logoMSSM = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();

        templateHelpDialog.setTitle("Input Template Help");
        templateHelpDialog.setResizable(false);

        templateHelpTextPanel.setContentType("text/html");
        templateHelpTextPanel.setText("<html>Input Template Type -  Defines the format of the flat input file. Select from list or specify your own.<BR>\n<b>sig</b> (default) format syntax: <code>SN SHA SMA ST SL TN THA TMA TT TL E TOI PID</code><br>\n<b>source_target</b> syntax:  <code>SN SL TN TL E TOI PID</code><br>\n<b>tf_target</b> syntax:  <code>SN TN PID</code><br>\nIn <b>tf_target</b> format, transcription is the presupposed TypeOfInteraction<br>\n<BR>\n<b>KEY</b><hr> \t\t\nSN = SourceName: Name of source molecule<br>\nSHA = SourceHumanAccession: Source Swiss-Prot human accession number<br>\nSMA = SourceMouseAccession: Source Swiss-Prot mouse accession number<br>\nST = SourceType: Type of source molecule<br>\nSL = SourceLocation: Location of source molecule in the cell<br>\n TN = TargetName: Name of target molecule <br>\nTHA = TargetHumanAccession: target Swiss-Prot human accession number<br>\nTMA = TargetMouseAccession: target Swiss-Prot mouse accession number<br>\nTT = TargetType: Type of target molecule<br>\nTL =  TargetLocation: Location of target molecule in the cell<br>\nE = Effect: Effect of source on target. + (activating), _ (deactivating), or 0 (neutral)<br>\nTOI = TypeOfInteraction: Reaction type definition<br>\nPID = PubMedID: ID of article that identified this reaction");
        templateHelpScrollPane.setViewportView(templateHelpTextPanel);

        javax.swing.GroupLayout templateHelpDialogLayout = new javax.swing.GroupLayout(templateHelpDialog.getContentPane());
        templateHelpDialog.getContentPane().setLayout(templateHelpDialogLayout);
        templateHelpDialogLayout.setHorizontalGroup(
            templateHelpDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(templateHelpScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
        );
        templateHelpDialogLayout.setVerticalGroup(
            templateHelpDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(templateHelpScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sig2BioPAX");
        setName("Sig2BioPAX"); // NOI18N
        setResizable(false);

        openFileButton.setText("Open Input File...");
        openFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileButtonActionPerformed(evt);
            }
        });

        runProgramButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        runProgramButton.setText("Run Sig2BioPAX");
        runProgramButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runProgramButtonActionPerformed(evt);
            }
        });

        saveFileButton.setText("Select Output File...");
        saveFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileButtonActionPerformed(evt);
            }
        });

        overwriteCheckBox.setText("Overwrite");
        overwriteCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overwriteCheckBoxActionPerformed(evt);
            }
        });

        templateTypeComboBox.setEditable(true);
        templateTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateTypeComboBoxActionPerformed(evt);
            }
        });

        templateLabel.setText("Input Template Type...");

        templateHelpButton.setText("?");
        templateHelpButton.setMinimumSize(null);
        templateHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateHelpButtonActionPerformed(evt);
            }
        });

        logoSBCNY.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SBCNYLogo.gif"))); // NOI18N

        logoMSSM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mssm.gif"))); // NOI18N

        jProgressBar1.setBorderPainted(false);
        jProgressBar1.setString("Done!                                          ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveFileButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(overwriteCheckBox)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                                .addComponent(logoSBCNY, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(logoMSSM, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(outputFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(openFileButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(templateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(templateTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(templateHelpButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(inputFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)))
                    .addComponent(runProgramButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {openFileButton, saveFileButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openFileButton)
                    .addComponent(inputFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(templateLabel)
                        .addComponent(templateTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(templateHelpButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveFileButton)
                            .addComponent(outputFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(logoSBCNY, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(logoMSSM, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(overwriteCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(runProgramButton)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileButtonActionPerformed
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inFile = fc.getSelectedFile();
        }
        inputFileTextField.setText(inFile.toString());
    }//GEN-LAST:event_openFileButtonActionPerformed

    private void runProgramButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runProgramButtonActionPerformed
        int argNum = 0;
        if (inputFileTextField.getText().isEmpty()==false) argNum++;
        if (outputFileTextField.getText().isEmpty()==false) argNum++;
        if (overwrite) argNum++;
        if (templateTypeComboBox.getSelectedItem().toString().isEmpty()==false)
            argNum++;

        // Check if the infile exists (if the form field is not blank)
        inFile = new File(inputFileTextField.getText());
        if (inFile.exists()==false)
        {
             String message = "Input file not found.";
              JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
              JOptionPane.ERROR_MESSAGE);
              return;  // should fix this, should not have multiple function exits
        }
        if (inFile.exists()==true && inputFileTextField.getText()=="")
        {
             String message = "Default file input.txt found, will use that.";
              JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
              JOptionPane.ERROR_MESSAGE);
              // should fix this, should not have multiple function exits
        }
        outFile = new File(outputFileTextField.getText());
        if (outFile.exists() && overwrite==false)
        {
             String message = "Output file already exists. Will append filename.";
              JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
              JOptionPane.ERROR_MESSAGE);
              // should fix this, should not have multiple function exits
        }

        String args[] = new String [argNum];
        int argAccess = 0;
        if (inputFileTextField.getText().isEmpty()==false)
        {
            args[argAccess]= "-in:" + inputFileTextField.getText();
            argAccess++;
        }
        if (outputFileTextField.getText().isEmpty()==false)
        {
            args[argAccess]= "-out:" +outputFileTextField.getText();
            argAccess++;
        }
        if (overwrite)
        {
             args[argAccess] = "-o";
             argAccess++;
        }
        if (templateType.isEmpty()==false)
        {
             args[argAccess] = "-t:" + templateType;
             argAccess++;
        }
        runProgramButton.setEnabled(false);
        task = new Task(args);
        task.execute();
    }//GEN-LAST:event_runProgramButtonActionPerformed

    private void saveFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileButtonActionPerformed
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            outFile = fc.getSelectedFile();
        }
        outputFileTextField.setText(outFile.toString());
    }//GEN-LAST:event_saveFileButtonActionPerformed

    private void overwriteCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overwriteCheckBoxActionPerformed
        if (overwrite==false) {overwrite = true;}
        else {overwrite = false;}
    }//GEN-LAST:event_overwriteCheckBoxActionPerformed

    private void templateTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateTypeComboBoxActionPerformed
        if (templateTypeComboBox.getSelectedItem()=="sig (default)") {templateType="sig";}
        else
        {
            templateType = templateTypeComboBox.getSelectedItem().toString();
        }
    }//GEN-LAST:event_templateTypeComboBoxActionPerformed

    private void templateHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateHelpButtonActionPerformed

    templateHelpDialog.setSize(600, 430);
    templateHelpDialog.setVisible(true);

    }//GEN-LAST:event_templateHelpButtonActionPerformed


    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        boolean isCmdMode = false;
        for (int i = 0; i < args.length; i++)
        {
           if (args[i].equalsIgnoreCase("-cmd"))
           {
               Sig2BioPaxv4.main(args);
               isCmdMode = true;
            }
        }
        if (isCmdMode == false)
        {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Sig2BioPAXGUI().setVisible(true);
                }
            });
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField inputFileTextField;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel logoMSSM;
    private javax.swing.JLabel logoSBCNY;
    private javax.swing.JButton openFileButton;
    private javax.swing.JTextField outputFileTextField;
    private javax.swing.JCheckBox overwriteCheckBox;
    private javax.swing.JButton runProgramButton;
    private javax.swing.JButton saveFileButton;
    private javax.swing.JButton templateHelpButton;
    private javax.swing.JDialog templateHelpDialog;
    private javax.swing.JScrollPane templateHelpScrollPane;
    private javax.swing.JTextPane templateHelpTextPanel;
    private javax.swing.JLabel templateLabel;
    private javax.swing.JComboBox templateTypeComboBox;
    // End of variables declaration//GEN-END:variables

}
