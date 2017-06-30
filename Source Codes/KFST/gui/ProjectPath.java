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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This java class is used to create and show a panel for the selecting path for
 * the project.
 *
 * @author Shahin Salavati
 */
public class ProjectPath extends JFrame implements ActionListener {

    JButton btn_browse, btn_select;
    JLabel lbl_select, lbl_path;
    JTextField txt_path;
    String path = "";
    JPanel mainPanel;

    /**
     * Creates new form ProjectPath. This method is called from within the
     * constructor to initialize the form.
     */
    public ProjectPath() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        lbl_select = new JLabel("Please select a folder for the tool:");
        lbl_select.setBounds(20, 30, 204, 14);

        lbl_path = new JLabel("Folder:");
        lbl_path.setBounds(20, 70, 34, 14);

        txt_path = new JTextField();
        txt_path.setBounds(60, 67, 180, 21);
        txt_path.setBackground(Color.WHITE);
        txt_path.setEditable(false);

        btn_browse = new JButton("Browse...");
        btn_browse.setBounds(250, 66, 80, 23);
        btn_browse.addActionListener(this);

        btn_select = new JButton("Ok");
        btn_select.setBounds(140, 125, 69, 23);
        btn_select.addActionListener(this);
        btn_select.setEnabled(false);

        mainPanel.add(lbl_select);
        mainPanel.add(btn_browse);
        mainPanel.add(btn_select);
        mainPanel.add(lbl_path);
        mainPanel.add(txt_path);

        this.setTitle("Workspase Selection");
        this.setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        this.setSize(350, 190);
        this.setLocationRelativeTo(null);
        this.add(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * The listener method for receiving action events.
     * Invoked when an action occurs.
     *
     * @param e an action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btn_browse)) {
            btn_browseActionPerformed(e);
        } else if (e.getSource().equals(btn_select)) {
            btn_selectActionPerformed(e);
        }
    }

    /**
     * This method sets an action for the btn_browse button.
     *
     * @param e an action event
     */
    private void btn_browseActionPerformed(ActionEvent e) {
        JFileChooser jfch = new JFileChooser();
        jfch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txt_path.setText(jfch.getSelectedFile().getPath());
            path = txt_path.getText();
            btn_select.setEnabled(true);
        }
    }

    /**
     * This method sets an action for the btn_select button.
     *
     * @param e an action event
     */
    private void btn_selectActionPerformed(ActionEvent e) {
        //creates the two folder (CSV - ARFF)in the selected path
        String pathDataCSV = path + "\\CSV\\";
        String pathDataARFF = path + "\\ARFF\\";
        File dir1 = new File(pathDataCSV);
        File dir2 = new File(pathDataARFF);
        dir1.mkdir();
        dir2.mkdir();

        MainPanel ui = new MainPanel(path);
        ui.createAndShow();
        this.setVisible(false);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ProjectPath sp = new ProjectPath();
            }
        });
    }
}
