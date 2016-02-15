/*
 * Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed
 * completely in Java, for performing feature selection process in different
 * areas of research.
 * For more information about KFST, please visit:
 *     http://kfst.uok.ac.ir/index.html
 *
 * Copyright (C) 2016 KFST development team at University of Kurdistan,
 * Sanandaj, Iran.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package KFST.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show a panel for showing the results.
 *
 * @author Shahin Salavati
 * @author Sina Tabakhi
 */
public class ResultPanel extends JPanel implements ActionListener {

    JButton btn_showFeat, btn_showDir, btn_saveRes, btn_exit;
    JTextArea txtArea_main;
    JScrollPane scroll;
    String pathProject;
    JFrame f;

    /**
     * Creates new form ResultPanel. This method is called from within the
     * constructor to initialize the form.
     *
     * @param path the path of the project
     */
    public ResultPanel(String path) {
        super();
        pathProject = path;

        txtArea_main = new JTextArea();
        txtArea_main.setLineWrap(true);
        txtArea_main.setEditable(false);

        scroll = new JScrollPane();
        scroll.setBounds(0, 0, 695, 400);
        scroll.setViewportView(txtArea_main);


        btn_showFeat = new JButton("View subsets");
        btn_showFeat.setBounds(70, 420, 130, 23);
        btn_showFeat.setEnabled(false);
        btn_showFeat.addActionListener(this);

        btn_showDir = new JButton("View train/test sets");
        btn_showDir.setBounds(210, 420, 130, 23);
        btn_showDir.setEnabled(false);
        btn_showDir.addActionListener(this);

        btn_saveRes = new JButton("Save results");
        btn_saveRes.setBounds(350, 420, 130, 23);
        btn_saveRes.setEnabled(false);
        btn_saveRes.addActionListener(this);

        btn_exit = new JButton("Close");
        btn_exit.setBounds(490, 420, 130, 23);
        btn_exit.setEnabled(false);
        btn_exit.addActionListener(this);


        setSize(700, 400);
        setLayout(null);
        add(scroll);
        add(btn_showFeat);
        add(btn_showDir);
        add(btn_saveRes);
        add(btn_exit);

        f = new JFrame("Results");
        f.add(this);
        f.setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(700, 500);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setVisible(true);
    }

    /**
     * The listener method for receiving action events.
     * Invoked when an action occurs.
     *
     * @param e an action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btn_showFeat)) {
            btn_showFeatActionPerformed(e);
        } else if (e.getSource().equals(btn_showDir)) {
            btn_showDirActionPerformed(e);
        } else if (e.getSource().equals(btn_saveRes)) {
            btn_saveResActionPerformed(e);
        } else if (e.getSource().equals(btn_exit)) {
            btn_exitActionPerformed(e);
        }
    }

    /**
     * This method sets an action for the btn_showFeat button.
     *
     * @param e an action event
     *
     * @see KurdFeast.gui.MainPanel
     */
    private void btn_showFeatActionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().open(new File(pathProject + "FeatureSubsetsFile.txt"));
        } catch (IOException ex) {
            Logger.getLogger(ResultPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method sets an action for the btn_showDir button.
     *
     * @param e an action event
     */
    private void btn_showDirActionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().open(new File(pathProject));
        } catch (IOException ex) {
            Logger.getLogger(ResultPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method sets an action for the btn_saveRes button.
     *
     * @param e an action event
     */
    private void btn_saveResActionPerformed(ActionEvent e) {
        JFileChooser jfch = new JFileChooser();
        jfch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfch.showSaveDialog(btn_saveRes) == JFileChooser.APPROVE_OPTION) {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileWriter(jfch.getSelectedFile().getPath() + "\\ResultFile.txt"));
                pw.println(txtArea_main.getText());
                pw.close();
            } catch (IOException ex) {
                Logger.getLogger(ResultPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method sets an action for the btn_exit button.
     *
     * @param e an action event
     */
    private void btn_exitActionPerformed(ActionEvent e) {
        f.dispose();
    }

    /**
     * appends the given text to the end of the documents
     * 
     * @param text the text to insert
     */
    public void setMessage(String text) {
        txtArea_main.append(text);
        txtArea_main.setCaretPosition(txtArea_main.getDocument().getLength());
    }

    /**
     * enables the status of all buttons
     */
    public void setEnabledButton() {
        btn_showFeat.setEnabled(true);
        btn_showDir.setEnabled(true);
        btn_saveRes.setEnabled(true);
        btn_exit.setEnabled(true);
    }

//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        ResultPanel mop = new ResultPanel("C:\\Users\\ST\\Desktop\\");
//
//        for (int i = 0; i < 30; i++) {
//            mop.setMessage("Showing results ....................(" + i + ")\n");
//        }
//        mop.setEnabledButton();
//    }
}
