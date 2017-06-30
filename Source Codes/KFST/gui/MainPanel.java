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

import KFST.classifier.WekaClassifier;
import KFST.dataset.DatasetInfo;
import KFST.dataset.WekaFileFormat;
import KFST.featureSelection.filter.supervised.*;
import KFST.featureSelection.filter.supervised.LaplacianScore;
import KFST.featureSelection.filter.supervised.RRFS;
import KFST.featureSelection.filter.unsupervised.*;
import KFST.featureSelection.wrapper.GeneticAlgorithm.GeneticAlgorithmMain;
import KFST.gui.classifier.DTClassifierPanel;
import KFST.gui.classifier.SVMClassifierPanel;
import KFST.gui.featureSelection.*;
import KFST.gui.menu.*;
import KFST.util.MathFunc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show the main panel of the project.
 * The management of all panels and forms have been done in this class.
 *
 * @author Sina Tabakhi
 * @author Shahin Salavati
 */
public class MainPanel extends JPanel {

    JPanel panel_filePath, panel_randomSet, panel_ttSet,
            panel_featureMethod, panel_classifier, panel_config;

    ButtonGroup bg_dataset;
    JRadioButton rd_randSet, rd_ttset;
    //---------------- Random Panel -------------------------------------------
    JButton btn_inputdst, btn_classlbl;
    JLabel lbl_dataset, lbl_inputdst, lbl_classlbl;
    JTextField txt_inputdst, txt_classLbl;
    //--------------- TrainingTest Panel -----------------------------------------
    JButton btn_trainSet, btn_testSet, btn_classLabel;
    JLabel lbl_trainSet, lbl_testSet, lbl_classlabel;
    JTextField txt_trainSet, txt_testSet, txt_classLabel;
    //--------------- Feature Selection Panel ---------------------------------
    JTabbedPane tab_pane;
    JPanel panel_filter, panel_wrapper, panel_embedded, panel_hybrid, panel_numFeat;
    JComboBox cb_supervised, cb_unsupervised, cb_wrapper, cb_embedded, cb_hybrid;
    JLabel lbl_sup, lbl_unsup, lbl_wrapper, lbl_embedded, lbl_hybrid;
    JButton btn_moreOpFilter, btn_moreOpWrapper, btn_moreOpEmbedded, btn_moreOpHybrid;
    JTextArea txtArea_feature;
    JScrollPane sc_feature;
    //--------------- Classifier Panel ----------------------------------------
    JComboBox cb_classifier;
    JLabel lbl_classifier;
    JButton btn_moreOpClassifier;
    //--------------- Configuration Panel--------------------------------------
    JComboBox cb_start;
    JLabel lbl_start;
    JButton btn_start, btn_exit;
    //--------------- Menu Panel ----------------------------------------------
    EventHandler eh;
    JMenuBar menuBar;
    JMenu file, diagram, analyse, help;
    JMenuItem mi_preprocess, mi_exit, mi_exeTime, mi_accur, mi_error, mi_friedman, mi_help, mi_about;
    JSeparator sep_file, sep_help;
    //--------------- Progress Bar --------------------------------------------
    JProgressBar progressBar;
    int progressValue, runCode = 0;
    //-------------- Other variables ------------------------------------------
    private String pathProject;
    private String pathDataCSV;
    private String pathDataARFF;
    private String typeKernel; //SVM classifier panel
    private double confidence; //DT classifier panel
    private int minNum; //DT classifier panel
    private double simValue; //RRFS method
    private double constParam; //Laplacian score method
    private int KNearest; //Laplacian score method
    private int numSelection, sizeSubspace, elimination; //RSM method
    private String multMethodName; //RSM method
    private int numIteration, numAnts, numFeatOfAnt; //ACO-based methods
    private double initPheromone, evRate, alpha, beta, q0; //ACO_based methods

    private double pCrossover, pMutation; //GA_based methods
    private int numPopulation, numGeneration; //GA_based methods

    DatasetInfo data;
    WekaFileFormat arff;
    //-------------- Result variables -----------------------------------------
    private double[][] accuracies;
    private double[][] errorRates;
    private double[][] times;
    private double[][] averageAccuracies;
    private double[][] averageErrorRates;
    private double[][] averageTimes;
    private int[] numSelectedSubsets;

    /**
     * Creates new form MainPanel. This method is called from within the
     * constructor to initialize the form.
     *
     * @param path the path of the project
     */
    public MainPanel(String path) {
        eh = new EventHandler();
        setLayout(null);

        /////////////////// set the path files ///////////////////////////////
        pathProject = path + "\\";
        pathDataCSV = pathProject + "CSV\\";
        pathDataARFF = pathProject + "ARFF\\";

        /////////////////////// "File Paths" panel /////////////////////////////
        panel_filePath = new JPanel();
        panel_filePath.setBounds(10, 15, 825, 210);
        panel_filePath.setLayout(null);
        panel_filePath.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "File paths"));
        lbl_dataset = new JLabel("Dataset option:");
        lbl_dataset.setBounds(15, 25, 85, 20);
        bg_dataset = new ButtonGroup();
        rd_randSet = new JRadioButton("Random training/test sets");
        rd_ttset = new JRadioButton("Training/Test sets");
        rd_randSet.setBounds(105, 25, 170, 20);
        rd_ttset.setBounds(275, 25, 195, 20);

        bg_dataset.add(rd_ttset);
        bg_dataset.add(rd_randSet);

        rd_randSet.addItemListener(eh);
        rd_ttset.addItemListener(eh);

        panel_filePath.add(rd_randSet);
        panel_filePath.add(rd_ttset);
        panel_filePath.add(lbl_dataset);

        ///////////////// "Random Training/Test sets" panel //////////////////////
        panel_randomSet = new JPanel();
        panel_randomSet.setBounds(10, 60, 400, 140);
        panel_randomSet.setLayout(null);
        panel_randomSet.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Random training/test sets"));
        lbl_inputdst = new JLabel("Input dataset:");
        lbl_inputdst.setBounds(15, 46, 80, 15);
        lbl_classlbl = new JLabel("Input class label:");
        lbl_classlbl.setBounds(15, 82, 90, 15);
        txt_inputdst = new JTextField();
        txt_inputdst.setBounds(110, 45, 170, 21);
        txt_inputdst.setEditable(false);
        txt_classLbl = new JTextField();
        txt_classLbl.setBounds(110, 80, 170, 21);
        txt_classLbl.setEditable(false);
        btn_inputdst = new JButton("Open file...");
        btn_inputdst.setBounds(295, 44, 87, 23);
        btn_classlbl = new JButton("Open file...");
        btn_classlbl.setBounds(295, 79, 87, 23);

        btn_inputdst.addActionListener(eh);
        btn_classlbl.addActionListener(eh);


        panel_randomSet.add(lbl_inputdst);
        panel_randomSet.add(lbl_classlbl);
        panel_randomSet.add(txt_inputdst);
        panel_randomSet.add(txt_classLbl);
        panel_randomSet.add(btn_classlbl);
        panel_randomSet.add(btn_inputdst);

        ////////////////// "Training/Test sets" panel ////////////////////////////
        panel_ttSet = new JPanel();
        panel_ttSet.setBounds(415, 60, 400, 140);
        panel_ttSet.setLayout(null);
        panel_ttSet.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Training/Test sets"));
        lbl_trainSet = new JLabel("Input training set:");
        lbl_trainSet.setBounds(15, 37, 90, 15);
        lbl_testSet = new JLabel("Input test set:");
        lbl_testSet.setBounds(15, 72, 80, 15);
        lbl_classlabel = new JLabel("Input class label:");
        lbl_classlabel.setBounds(15, 107, 90, 15);
        txt_trainSet = new JTextField();
        txt_trainSet.setBounds(110, 35, 170, 21);
        txt_trainSet.setEditable(false);
        txt_testSet = new JTextField();
        txt_testSet.setBounds(110, 70, 170, 21);
        txt_testSet.setEditable(false);
        txt_classLabel = new JTextField();
        txt_classLabel.setBounds(110, 105, 170, 21);
        txt_classLabel.setEditable(false);
        btn_trainSet = new JButton("Open file...");
        btn_trainSet.setBounds(295, 34, 87, 23);
        btn_testSet = new JButton("Open file...");
        btn_testSet.setBounds(295, 69, 87, 23);
        btn_classLabel = new JButton("Open file...");
        btn_classLabel.setBounds(295, 104, 87, 23);

        btn_trainSet.addActionListener(eh);
        btn_testSet.addActionListener(eh);
        btn_classLabel.addActionListener(eh);

        panel_ttSet.add(lbl_classlabel);
        panel_ttSet.add(lbl_trainSet);
        panel_ttSet.add(lbl_testSet);
        panel_ttSet.add(txt_trainSet);
        panel_ttSet.add(txt_testSet);
        panel_ttSet.add(txt_classLabel);
        panel_ttSet.add(btn_trainSet);
        panel_ttSet.add(btn_testSet);
        panel_ttSet.add(btn_classLabel);
        panel_filePath.add(panel_randomSet);
        panel_filePath.add(panel_ttSet);

        //////////////// "Feature selection methods" panel ////////////////////
        panel_featureMethod = new JPanel();
        panel_featureMethod.setBounds(10, 235, 555, 160);
        panel_featureMethod.setLayout(null);
        panel_featureMethod.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Feature selection approaches"));
        tab_pane = new JTabbedPane();
        tab_pane.setBounds(20, 25, 520, 120);

        //--------- Filter Panel-----------------------
        panel_filter = new JPanel();
        panel_filter.setLayout(null);

        lbl_sup = new JLabel("Supervised:");
        lbl_sup.setBounds(10, 20, 120, 22);
        cb_supervised = new JComboBox();
        cb_supervised.setModel(new DefaultComboBoxModel(new String[]{"none",
                "Information gain",
                "Gain ratio",
                "Symmetrical uncertainty",
                "Fisher score",
                "Gini index",
                "Laplacian score",
                "Minimal redundancy maximal relevance (MRMR)",
                "Relevance-redundancy feature selection (RRFS)"}));
        cb_supervised.setBounds(90, 20, 280, 22);


        lbl_unsup = new JLabel("Unsupervised:");
        lbl_unsup.setBounds(10, 55, 120, 22);
        cb_unsupervised = new JComboBox();
        cb_unsupervised.setModel(new DefaultComboBoxModel(new String[]{"none",
                "Term variance",
                "Laplacian score",
                "Mutual correlation",
                "Random subspace method (RSM)",
                "Relevance-redundancy feature selection (RRFS)",
                "UFSACO",
                "RRFSACO_1",
                "RRFSACO_2",
                "IRRFSACO_1",
                "IRRFSACO_2",
                "MGSACO"}));
        cb_unsupervised.setBounds(90, 55, 280, 22);


        cb_supervised.addItemListener(eh);
        cb_unsupervised.addItemListener(eh);

        btn_moreOpFilter = new JButton("More option...");
        btn_moreOpFilter.setBounds(400, 35, 105, 23);
        btn_moreOpFilter.setEnabled(false);
        btn_moreOpFilter.addActionListener(eh);


        panel_filter.setLayout(null);
        panel_filter.add(lbl_sup);
        panel_filter.add(lbl_unsup);
        panel_filter.add(cb_supervised);
        panel_filter.add(cb_unsupervised);
        panel_filter.add(btn_moreOpFilter);


        //--------- Wrapper Panel-----------------------
        panel_wrapper = new JPanel();
        lbl_wrapper = new JLabel("Select method:");
        lbl_wrapper.setBounds(10, 35, 120, 22);

        cb_wrapper = new JComboBox();
        cb_wrapper.setModel(new DefaultComboBoxModel(new String[]{"none",
                "GeneticAlgorithm"}));
        cb_wrapper.setBounds(90, 35, 280, 22);
        cb_wrapper.addItemListener(eh);

        btn_moreOpWrapper = new JButton("More option...");
        btn_moreOpWrapper.setBounds(400, 35, 105, 23);
        btn_moreOpWrapper.setEnabled(false);
        btn_moreOpWrapper.addActionListener(eh);


        panel_wrapper.setLayout(null);
        panel_wrapper.add(cb_wrapper);
        panel_wrapper.add(lbl_wrapper);
        panel_wrapper.add(btn_moreOpWrapper);


        //--------- Embedded Panel---------------------
        panel_embedded = new JPanel();
        lbl_embedded = new JLabel("Select method:");
        lbl_embedded.setBounds(10, 35, 120, 22);

        cb_embedded = new JComboBox();
        cb_embedded.setModel(new DefaultComboBoxModel(new String[]{"none"}));
        cb_embedded.setBounds(90, 35, 280, 22);
        cb_embedded.addItemListener(eh);

        btn_moreOpEmbedded = new JButton("More option...");
        btn_moreOpEmbedded.setBounds(400, 35, 105, 23);
        btn_moreOpEmbedded.setEnabled(false);
        btn_moreOpEmbedded.addActionListener(eh);


        panel_embedded.setLayout(null);
        panel_embedded.add(cb_embedded);
        panel_embedded.add(lbl_embedded);
        panel_embedded.add(btn_moreOpEmbedded);


        //--------- Hybrid Panel-----------------------
        panel_hybrid = new JPanel();
        lbl_hybrid = new JLabel("Select method:");
        lbl_hybrid.setBounds(10, 35, 120, 22);

        cb_hybrid = new JComboBox();
        cb_hybrid.setModel(new DefaultComboBoxModel(new String[]{"none"}));
        cb_hybrid.setBounds(90, 35, 280, 22);
        cb_hybrid.addItemListener(eh);

        btn_moreOpHybrid = new JButton("More option...");
        btn_moreOpHybrid.setBounds(400, 35, 105, 23);
        btn_moreOpHybrid.setEnabled(false);
        btn_moreOpHybrid.addActionListener(eh);


        panel_hybrid.setLayout(null);
        panel_hybrid.add(cb_hybrid);
        panel_hybrid.add(lbl_hybrid);
        panel_hybrid.add(btn_moreOpHybrid);

        //--------- Num Feature Selected Panel------------
        txtArea_feature = new JTextArea("5,10,15,20");
        txtArea_feature.setLineWrap(true);

        sc_feature = new JScrollPane();
        sc_feature.setBounds(15, 25, 225, 120);
        sc_feature.setViewportView(txtArea_feature);


        panel_numFeat = new JPanel();
        panel_numFeat.setBounds(580, 235, 255, 160);
        panel_numFeat.setLayout(null);
        panel_numFeat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Numbers of selected features"));
        panel_numFeat.add(sc_feature);

        //--------- sets all panel in the Feature Panel----------
        tab_pane.add("Filter", panel_filter);
        tab_pane.add("Wrapper", panel_wrapper);
        tab_pane.add("Embedded", panel_embedded);
        tab_pane.add("Hybrid", panel_hybrid);

        panel_featureMethod.add(tab_pane);

        ////////////////////// "Classifier" panel /////////////////////////////
        panel_classifier = new JPanel();
        panel_classifier.setBounds(10, 405, 408, 140);
        panel_classifier.setLayout(null);
        panel_classifier.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Classifier"));
        cb_classifier = new JComboBox();
        cb_classifier.setModel(new DefaultComboBoxModel(new String[]{"none",
                "Support Vector Machine (SVM)",
                "Naive Bayes (NB)",
                "Decision Tree (C4.5)"}));
        cb_classifier.setBounds(110, 40, 250, 22);
        lbl_classifier = new JLabel("Select classifier:");
        lbl_classifier.setBounds(15, 40, 140, 22);

        btn_moreOpClassifier = new JButton("More option...");
        btn_moreOpClassifier.setBounds(140, 90, 110, 23);
        btn_moreOpClassifier.setEnabled(false);

        btn_moreOpClassifier.addActionListener(eh);
        cb_classifier.addItemListener(eh);

        panel_classifier.add(lbl_classifier);
        panel_classifier.add(cb_classifier);
        panel_classifier.add(btn_moreOpClassifier);

        ///////////////////// "Run configuration" panel ///////////////////////
        panel_config = new JPanel();
        panel_config.setBounds(428, 405, 408, 140);
        panel_config.setLayout(null);
        panel_config.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Run configuration"));
        cb_start = new JComboBox();
        cb_start.setModel(new DefaultComboBoxModel(new String[]{"none", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
        cb_start.setBounds(130, 40, 200, 22);
        cb_start.addItemListener(eh);
        lbl_start = new JLabel("Number of runs:");
        lbl_start.setBounds(15, 40, 140, 22);
        btn_start = new JButton("start");
        btn_start.addActionListener(eh);
        btn_exit = new JButton("Exit");
        btn_start.setBounds(110, 90, 80, 23);
        btn_exit.setBounds(210, 90, 80, 23);
        btn_exit.addActionListener(eh);

        panel_config.add(btn_start);
        panel_config.add(btn_exit);
        panel_config.add(cb_start);
        panel_config.add(lbl_start);


        //---------------------------------------------------------------
        add(panel_filePath);
        add(panel_featureMethod);
        add(panel_numFeat);
        add(panel_classifier);
        add(panel_config);
        this.setBackground(new Color(240, 240, 240));

        ////////////////////////////// menuBar  ///////////////////////////////
        menuBar = new JMenuBar();

        ///// file menu /////
        mi_preprocess = new JMenuItem("Preprocess");
        mi_preprocess.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        mi_preprocess.addActionListener(eh);
        mi_exit = new JMenuItem("Exit");
        mi_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        mi_exit.addActionListener(eh);
        file = new JMenu("File");
        file.setMnemonic('F');
        file.add(mi_preprocess);
        file.addSeparator();
        file.add(mi_exit);

        ///// diagram menu /////
        mi_exeTime = new JMenuItem("Execution time");
        mi_exeTime.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        mi_exeTime.setEnabled(false);
        mi_exeTime.addActionListener(eh);
        mi_accur = new JMenuItem("Accuracy");
        mi_accur.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        mi_accur.setEnabled(false);
        mi_accur.addActionListener(eh);
        mi_error = new JMenuItem("Error rate");
        mi_error.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        mi_error.setEnabled(false);
        mi_error.addActionListener(eh);

        diagram = new JMenu("Diagram");
        diagram.setMnemonic('D');
        diagram.add(mi_exeTime);
        diagram.add(mi_accur);
        diagram.add(mi_error);

        ///// analyse menu /////
        mi_friedman = new JMenuItem("Friedman test");
        mi_friedman.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        mi_friedman.addActionListener(eh);
        analyse = new JMenu("Analyze");
        analyse.setMnemonic('A');
        analyse.add(mi_friedman);

        ///// help menu /////
        mi_help = new JMenuItem("Help contents");
        mi_help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.SHIFT_MASK));
        mi_help.addActionListener(eh);
        mi_about = new JMenuItem("About");
        mi_about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        mi_about.addActionListener(eh);
        help = new JMenu("Help");
        help.setMnemonic('H');
        help.add(mi_help);
        help.addSeparator();
        help.add(mi_about);


        menuBar.add(file);
        menuBar.add(diagram);
        menuBar.add(analyse);
        menuBar.add(help);

        ////////////////////////// progressBar  ///////////////////////////////
        progressBar = new JProgressBar();
        progressBar.setBounds(10, 560, 825, 20);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(51, 153, 255));
        progressBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));

        add(progressBar);
    }

    /**
     * Recreates MainPanel based on the updated progress bar value.
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        progressBar.setValue(progressValue);
    }

    /**
     * The event handler class for handling action events.
     */
    class EventHandler implements ActionListener, ItemListener {

        /**
         * The listener method for receiving action events.
         * Invoked when an action occurs.
         *
         * @param e an action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(mi_preprocess)) {
                mi_preprocessActionPerformed(e);
            } else if (e.getSource().equals(mi_exit)) {
                mi_exitActionPerformed(e);
            } else if (e.getSource().equals(mi_exeTime)) {
                mi_exeTimeActionPerformed(e);
            } else if (e.getSource().equals(mi_accur)) {
                mi_accurActionPerformed(e);
            } else if (e.getSource().equals(mi_error)) {
                mi_errorActionPerformed(e);
            } else if (e.getSource().equals(mi_friedman)) {
                mi_friedmanActionPerformed(e);
            } else if (e.getSource().equals(mi_help)) {
                mi_helpActionPerformed(e);
            } else if (e.getSource().equals(mi_about)) {
                mi_aboutActionPerformed(e);
            } else if (e.getSource().equals(btn_inputdst)) {
                btn_inputdstActionPerformed(e);
            } else if (e.getSource().equals(btn_classlbl)) {
                btn_classlblActionPerformed(e);
            } else if (e.getSource().equals(btn_trainSet)) {
                btn_trainSetActionPerformed(e);
            } else if (e.getSource().equals(btn_testSet)) {
                btn_testSetActionPerformed(e);
            } else if (e.getSource().equals(btn_classLabel)) {
                btn_classLabelActionPerformed(e);
            } else if (e.getSource().equals(btn_moreOpFilter)) {
                btn_moreOpFilterActionPerformed(e);
            } else if (e.getSource().equals(btn_moreOpWrapper)) {
                btn_moreOpWrapperActionPerformed(e);
            } else if (e.getSource().equals(btn_moreOpEmbedded)) {
                btn_moreOpEmbeddedActionPerformed(e);
            } else if (e.getSource().equals(btn_moreOpHybrid)) {
                btn_moreOpHybridActionPerformed(e);
            } else if (e.getSource().equals(btn_moreOpClassifier)) {
                btn_moreOpClassifierActionPerformed(e);
            } else if (e.getSource().equals(btn_start)) {
                btn_startActionPerformed(e);
            } else if (e.getSource().equals(btn_exit)) {
                btn_exitActionPerformed(e);
            }
        }

        /**
         * The listener method for receiving item events.
         * Invoked when an item has been selected or deselected by the user.
         *
         * @param e an action event
         */
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource().equals(rd_randSet)) {
                rd_randSetItemStateChanged(e);
            } else if (e.getSource().equals(rd_ttset)) {
                rd_ttsetSetItemStateChanged(e);
            } else if (e.getSource().equals(cb_supervised)) {
                cb_supervisedItemStateChanged(e);
            } else if (e.getSource().equals(cb_unsupervised)) {
                cb_unsupervisedItemStateChanged(e);
            } else if (e.getSource().equals(cb_wrapper)) {
                cb_wrapperItemStateChanged(e);
            } else if (e.getSource().equals(cb_embedded)) {
                cb_embeddedItemStateChanged(e);
            } else if (e.getSource().equals(cb_hybrid)) {
                cb_hybridItemStateChanged(e);
            } else if (e.getSource().equals(cb_classifier)) {
                cb_classifierItemStateChanged(e);
            } else if (e.getSource().equals(cb_start)) {
                cb_startItemStateChanged(e);
            }
        }
    }

    /**
     * This method sets an action for the mi_preprocess menu item.
     *
     * @param e an action event
     */
    private void mi_preprocessActionPerformed(ActionEvent e) {
        PreprocessPanel processPanel = new PreprocessPanel();
    }

    /**
     * This method sets an action for the mi_exit menu item.
     *
     * @param e an action event
     */
    private void mi_exitActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    /**
     * This method sets an action for the mi_exeTime menu item.
     *
     * @param e an action event
     */
    private void mi_exeTimeActionPerformed(ActionEvent e) {
        SelectModePanel selectPanel = new SelectModePanel();
        JDialog selecdlg = new JDialog(selectPanel);
        selectPanel.setVisible(true);
        if (selectPanel.getNameMode().equals("Average")) {
            DiagramPanel digPanel = new DiagramPanel(averageTimes, numSelectedSubsets, "Average execution time diagram", cb_classifier.getSelectedItem().toString(), "          Execution Time (s)", "average", pathProject);
        } else if (selectPanel.getNameMode().equals("Total")) {
            DiagramPanel digPanel = new DiagramPanel(times, numSelectedSubsets, "Execution time diagram", cb_classifier.getSelectedItem().toString(), "          Execution Time (s)", "iteration", pathProject);
        }
    }

    /**
     * This method sets an action for the mi_accur menu item.
     *
     * @param e an action event
     */
    private void mi_accurActionPerformed(ActionEvent e) {
        SelectModePanel selectPanel = new SelectModePanel();
        JDialog selecdlg = new JDialog(selectPanel);
        selectPanel.setVisible(true);
        if (selectPanel.getNameMode().equals("Average")) {
            DiagramPanel digPanel = new DiagramPanel(averageAccuracies, numSelectedSubsets, "Average classification accuracy diagram", cb_classifier.getSelectedItem().toString(), "Classification Accuracy (%)", "average", pathProject);
        } else if (selectPanel.getNameMode().equals("Total")) {
            DiagramPanel digPanel = new DiagramPanel(accuracies, numSelectedSubsets, "Classification accuracy diagram", cb_classifier.getSelectedItem().toString(), "Classification Accuracy (%)", "iteration", pathProject);
        }
    }

    /**
     * This method sets an action for the mi_error menu item.
     *
     * @param e an action event
     */
    private void mi_errorActionPerformed(ActionEvent e) {
        SelectModePanel selectPanel = new SelectModePanel();
        JDialog selecdlg = new JDialog(selectPanel);
        selectPanel.setVisible(true);
        if (selectPanel.getNameMode().equals("Average")) {
            DiagramPanel digPanel = new DiagramPanel(averageErrorRates, numSelectedSubsets, "Average classification error rate diagram", cb_classifier.getSelectedItem().toString(), "Classification Error Rate (%)", "average", pathProject);
        } else if (selectPanel.getNameMode().equals("Total")) {
            DiagramPanel digPanel = new DiagramPanel(errorRates, numSelectedSubsets, "Classification error rate diagram", cb_classifier.getSelectedItem().toString(), "Classification Error Rate (%)", "iteration", pathProject);
        }
    }

    /**
     * This method sets an action for the mi_friedman menu item.
     *
     * @param e an action event
     */
    private void mi_friedmanActionPerformed(ActionEvent e) {
        FriedmanPanel friedtest_Panel = new FriedmanPanel();
    }

    /**
     * This method sets an action for the mi_help menu item.
     *
     * @param e an action event
     */
    private void mi_helpActionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("http://kfst.uok.ac.ir/document.html"));
        } catch (Exception ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method sets an action for the mi_about menu item.
     *
     * @param e an action event
     */
    private void mi_aboutActionPerformed(ActionEvent e) {
        AboutPanel aboutPan = new AboutPanel();
        Dialog aboutdlg = new Dialog(aboutPan);
        aboutPan.setVisible(true);
    }

    /**
     * This method sets an action for the btn_inputdst button.
     *
     * @param e an action event
     */
    private void btn_inputdstActionPerformed(ActionEvent e) {
        JFileChooser jfch = new JFileChooser();
        jfch.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (jfch.showOpenDialog(btn_inputdst) == JFileChooser.APPROVE_OPTION) {
            txt_inputdst.setText(jfch.getSelectedFile().getPath());
        }
    }

    /**
     * This method sets an action for the btn_classlbl button.
     *
     * @param e an action event
     */
    private void btn_classlblActionPerformed(ActionEvent e) {
        JFileChooser jfch = new JFileChooser();
        jfch.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (jfch.showOpenDialog(btn_classlbl) == JFileChooser.APPROVE_OPTION) {
            txt_classLbl.setText(jfch.getSelectedFile().getPath());
        }
    }

    /**
     * This method sets an action for the btn_trainSet button.
     *
     * @param e an action event
     */
    private void btn_trainSetActionPerformed(ActionEvent e) {
        JFileChooser jfch = new JFileChooser();
        jfch.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (jfch.showOpenDialog(btn_trainSet) == JFileChooser.APPROVE_OPTION) {
            txt_trainSet.setText(jfch.getSelectedFile().getPath());
        }
    }

    /**
     * This method sets an action for the btn_testSet button.
     *
     * @param e an action event
     */
    private void btn_testSetActionPerformed(ActionEvent e) {
        JFileChooser jfch = new JFileChooser();
        jfch.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (jfch.showOpenDialog(btn_testSet) == JFileChooser.APPROVE_OPTION) {
            txt_testSet.setText(jfch.getSelectedFile().getPath());
        }
    }

    /**
     * This method sets an action for the btn_classLabel button.
     *
     * @param e an action event
     */
    private void btn_classLabelActionPerformed(ActionEvent e) {
        JFileChooser jfch = new JFileChooser();
        jfch.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (jfch.showOpenDialog(btn_classLabel) == JFileChooser.APPROVE_OPTION) {
            txt_classLabel.setText(jfch.getSelectedFile().getPath());
        }
    }

    /**
     * This method sets an action for the btn_moreOpFilter button.
     *
     * @param e an action event
     */
    private void btn_moreOpFilterActionPerformed(ActionEvent e) {
        if (cb_supervised.getSelectedItem().equals("Laplacian score")) {
            LaplacianScorePanel lapScorePanel = new LaplacianScorePanel();
            Dialog lapScoreDlg = new Dialog(lapScorePanel);
            lapScorePanel.setUserValue(KNearest, constParam);
            lapScorePanel.setVisible(true);
            KNearest = lapScorePanel.getKNearest();
            constParam = lapScorePanel.getConstParam();
//            System.out.println("user value...   k-NN = " + KNearest
//                    + "   constParam = " + constParam);
        } else if (cb_supervised.getSelectedItem().equals("Relevance-redundancy feature selection (RRFS)")) {
            RRFSPanel rrfsPanel = new RRFSPanel();
            Dialog rrfsDlg = new Dialog(rrfsPanel);
            rrfsPanel.setUserValue(simValue);
            rrfsPanel.setVisible(true);
            simValue = rrfsPanel.getSimilarity();
//            System.out.println("user value:   simValue = " + simValue);
        } else if (cb_unsupervised.getSelectedItem().equals("Laplacian score")) {
            LaplacianScorePanel lapScorePanel = new LaplacianScorePanel();
            Dialog lapScoreDlg = new Dialog(lapScorePanel);
            lapScorePanel.setUserValue(KNearest, constParam);
            lapScorePanel.setVisible(true);
            KNearest = lapScorePanel.getKNearest();
            constParam = lapScorePanel.getConstParam();
//            System.out.println("user value...   k-NN = " + KNearest
//                    + "   constParam = " + constParam);
        } else if (cb_unsupervised.getSelectedItem().equals("Random subspace method (RSM)")) {
            RSMPanel rsmPanel = new RSMPanel();
            Dialog rsmDlg = new Dialog(rsmPanel);
            rsmPanel.setUserValue(numSelection, sizeSubspace, elimination, multMethodName);
            rsmPanel.setVisible(true);
            numSelection = rsmPanel.getNumSelection();
            sizeSubspace = rsmPanel.getSizeSubspace();
            elimination = rsmPanel.getElimination();
            multMethodName = rsmPanel.getMultMethodName();
//            System.out.println("user values:   numSelection = " + numSelection
//                    + "   sizeSubspace = " + sizeSubspace
//                    + "   elimination = " + elimination
//                    + "    multMethodName = " + multMethodName);
        } else if (cb_unsupervised.getSelectedItem().equals("Relevance-redundancy feature selection (RRFS)")) {
            RRFSPanel rrfsPanel = new RRFSPanel();
            Dialog rrfsDlg = new Dialog(rrfsPanel);
            rrfsPanel.setUserValue(simValue);
            rrfsPanel.setVisible(true);
            simValue = rrfsPanel.getSimilarity();
//            System.out.println("user value:   simValue = " + simValue);
        } else if (cb_unsupervised.getSelectedItem().equals("UFSACO")) {
            UFSACOPanel ufsacoPanel = new UFSACOPanel();
            Dialog ufsacoDlg = new Dialog(ufsacoPanel);
            ufsacoPanel.setUserValue(initPheromone, numIteration, numAnts, numFeatOfAnt, evRate, beta, q0);
            ufsacoPanel.setVisible(true);
            initPheromone = ufsacoPanel.getInitPheromone();
            numIteration = ufsacoPanel.getNumIteration();
            numAnts = ufsacoPanel.getNumAnts();
            numFeatOfAnt = ufsacoPanel.getNumFeatOfAnt();
            evRate = ufsacoPanel.getEvRate();
            beta = ufsacoPanel.getBeta();
            q0 = ufsacoPanel.getQ0();
//            System.out.println("user value:   initPheromone = " + initPheromone
//                    + "   numIteration = " + numIteration
//                    + "   numAnts = " + numAnts
//                    + "   numFeatOfAnt = " + numFeatOfAnt
//                    + "   evRate = " + evRate
//                    + "   beta = " + beta
//                    + "   q0 = " + q0);
        } else if (cb_unsupervised.getSelectedItem().equals("RRFSACO_1")) {
            RRFSACO_1Panel rrfsacoPanel = new RRFSACO_1Panel();
            Dialog rrfsacoDlg = new Dialog(rrfsacoPanel);
            rrfsacoPanel.setUserValue(numIteration, numAnts, numFeatOfAnt, evRate, beta, q0);
            rrfsacoPanel.setVisible(true);
            numIteration = rrfsacoPanel.getNumIteration();
            numAnts = rrfsacoPanel.getNumAnts();
            numFeatOfAnt = rrfsacoPanel.getNumFeatOfAnt();
            evRate = rrfsacoPanel.getEvRate();
            beta = rrfsacoPanel.getBeta();
            q0 = rrfsacoPanel.getQ0();
//            System.out.println("user value:   numIteration = " + numIteration
//                    + "   numAnts = " + numAnts
//                    + "   numFeature = " + numFeatOfAnt
//                    + "   evRate = " + evRate
//                    + "   beta = " + beta
//                    + "   q0 = " + q0);
        } else if (cb_unsupervised.getSelectedItem().equals("RRFSACO_2")) {
            RRFSACO_2Panel rrfsacoPanel = new RRFSACO_2Panel();
            Dialog rrfsacoDlg = new Dialog(rrfsacoPanel);
            rrfsacoPanel.setUserValue(initPheromone, numIteration, numAnts, numFeatOfAnt, evRate, alpha, beta, q0);
            rrfsacoPanel.setVisible(true);
            initPheromone = rrfsacoPanel.getInitPheromone();
            numIteration = rrfsacoPanel.getNumIteration();
            numAnts = rrfsacoPanel.getNumAnts();
            numFeatOfAnt = rrfsacoPanel.getNumFeatOfAnt();
            evRate = rrfsacoPanel.getEvRate();
            alpha = rrfsacoPanel.getAlpha();
            beta = rrfsacoPanel.getBeta();
            q0 = rrfsacoPanel.getQ0();
//            System.out.println("user value:   initPheromone = " + initPheromone
//                    + "   numIteration = " + numIteration
//                    + "   numAnts = " + numAnts
//                    + "   numFeature = " + numFeatOfAnt
//                    + "   evRate = " + evRate
//                    + "   alpha = " + alpha
//                    + "   beta = " + beta
//                    + "   q0 = " + q0);
        } else if (cb_unsupervised.getSelectedItem().equals("IRRFSACO_1")) {
            IRRFSACO_1Panel irrfsacoPanel = new IRRFSACO_1Panel();
            Dialog irrfsacoDlg = new Dialog(irrfsacoPanel);
            irrfsacoPanel.setUserValue(numIteration, numAnts, numFeatOfAnt, evRate, beta, q0);
            irrfsacoPanel.setVisible(true);
            numIteration = irrfsacoPanel.getNumIteration();
            numAnts = irrfsacoPanel.getNumAnts();
            numFeatOfAnt = irrfsacoPanel.getNumFeatOfAnt();
            evRate = irrfsacoPanel.getEvRate();
            beta = irrfsacoPanel.getBeta();
            q0 = irrfsacoPanel.getQ0();
//            System.out.println("user value:   numIteration = " + numIteration
//                    + "   numAnts = " + numAnts
//                    + "   numFeature = " + numFeatOfAnt
//                    + "   evRate = " + evRate
//                    + "   beta = " + beta
//                    + "   q0 = " + q0);
        } else if (cb_unsupervised.getSelectedItem().equals("IRRFSACO_2")) {
            IRRFSACO_2Panel irrfsacoPanel = new IRRFSACO_2Panel();
            Dialog irrfsacoDlg = new Dialog(irrfsacoPanel);
            irrfsacoPanel.setUserValue(initPheromone, numIteration, numAnts, numFeatOfAnt, evRate, alpha, beta, q0);
            irrfsacoPanel.setVisible(true);
            initPheromone = irrfsacoPanel.getInitPheromone();
            numIteration = irrfsacoPanel.getNumIteration();
            numAnts = irrfsacoPanel.getNumAnts();
            numFeatOfAnt = irrfsacoPanel.getNumFeatOfAnt();
            evRate = irrfsacoPanel.getEvRate();
            alpha = irrfsacoPanel.getAlpha();
            beta = irrfsacoPanel.getBeta();
            q0 = irrfsacoPanel.getQ0();
//            System.out.println("user value:   initPheromone = " + initPheromone
//                    + "   numIteration = " + numIteration
//                    + "   numAnts = " + numAnts
//                    + "   numFeature = " + numFeatOfAnt
//                    + "   evRate = " + evRate
//                    + "   alpha = " + alpha
//                    + "   beta = " + beta
//                    + "   q0 = " + q0);
        } else if (cb_unsupervised.getSelectedItem().equals("MGSACO")) {
            MGSACOPanel mgsacoPanel = new MGSACOPanel();
            Dialog mgsacoDlg = new Dialog(mgsacoPanel);
            mgsacoPanel.setUserValue(initPheromone, numIteration, numAnts, evRate, beta, q0);
            mgsacoPanel.setVisible(true);
            initPheromone = mgsacoPanel.getInitPheromone();
            numIteration = mgsacoPanel.getNumIteration();
            numAnts = mgsacoPanel.getNumAnts();
            evRate = mgsacoPanel.getEvRate();
            beta = mgsacoPanel.getBeta();
            q0 = mgsacoPanel.getQ0();
//            System.out.println("user value:   initPheromone = " + initPheromone
//                    + "   numIteration = " + numIteration
//                    + "   numAnts = " + numAnts
//                    + "   evRate = " + evRate
//                    + "   beta = " + beta
//                    + "   q0 = " + q0);
        }
    }

    /**
     * This method sets an action for the btn_moreOpWrapper button.
     *
     * @param e an action event
     */
    private void btn_moreOpWrapperActionPerformed(ActionEvent e) {

        if (cb_wrapper.getSelectedItem().equals("GeneticAlgorithm")) {
            GeneticPanel geneticPanel = new GeneticPanel();
            Dialog geneticDialog = new Dialog(geneticPanel);
            geneticPanel.setUserValue(numPopulation, numGeneration, pCrossover, pMutation);
            geneticPanel.setVisible(true);
            numPopulation = geneticPanel.getNumPopulation();
            numGeneration = geneticPanel.getNumGeneration();
            pCrossover = geneticPanel.getpCrossover();
            pMutation = geneticPanel.getpMutation();

        }
        System.out.println("More option Wrapper");
    }

    /**
     * This method sets an action for the btn_moreOpEmbedded button.
     *
     * @param e an action event
     */
    private void btn_moreOpEmbeddedActionPerformed(ActionEvent e) {
        System.out.println("More option Embedded");
    }

    /**
     * This method sets an action for the btn_moreOpHybrid button.
     *
     * @param e an action event
     */
    private void btn_moreOpHybridActionPerformed(ActionEvent e) {
        System.out.println("More option Hybrid");
    }

    /**
     * This method sets an action for the btn_moreOpClassifier button.
     *
     * @param e an action event
     */
    private void btn_moreOpClassifierActionPerformed(ActionEvent e) {
        if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
            SVMClassifierPanel svmPanel = new SVMClassifierPanel();
            Dialog svmDlg = new Dialog(svmPanel);
            svmPanel.setUserValue(typeKernel);
            svmPanel.setVisible(true);
            typeKernel = svmPanel.getKernel();
//            System.out.println("kernel = " + typeKernel);
        } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
            DTClassifierPanel dtPanel = new DTClassifierPanel();
            Dialog dtDlg = new Dialog(dtPanel);
            dtPanel.setUserValue(confidence, minNum);
            dtPanel.setVisible(true);
            confidence = dtPanel.getConfidence();
            minNum = dtPanel.getMinNum();
//            System.out.println("min num = " + minNum + "  confidence = " + confidence);
        }
    }

    /**
     * This method sets an action for the btn_start button.
     *
     * @param e an action event
     */
    private void btn_startActionPerformed(ActionEvent e) {
        //gets the number of selected features
        if (!txtArea_feature.getText().equals("")) {
            String[] numCases = txtArea_feature.getText().split(",");
            numSelectedSubsets = new int[numCases.length];
            for (int i = 0; i < numCases.length; i++) {
                numSelectedSubsets[i] = Integer.parseInt(numCases[i]);
            }
        }

        //reads the data information
        data = new DatasetInfo();
        if (isCorrectDataset()) {
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            } else {
                data.preProcessing(txt_trainSet.getText(), txt_testSet.getText(), txt_classLabel.getText());
            }
        }
        arff = new WekaFileFormat();

        if (printErrorMessages()) {
            //delete old CSV and ARFF files
            File dir1 = new File(pathDataCSV);
            File dir2 = new File(pathDataARFF);
            if (dir1.isDirectory()) {
                File[] directory = dir1.listFiles();
                for (int i = 0; i < directory.length; i++) {
                    directory[i].delete();
                }
            }
            if (dir2.isDirectory()) {
                File[] directory = dir2.listFiles();
                for (int i = 0; i < directory.length; i++) {
                    directory[i].delete();
                }
            }

            //runs the progress bar
            Counter c = new Counter();
            Thread t = new Thread(c);
            t.start();
            repaint();
            runCode = 0;
        }
    }

    /**
     * This method sets an action for the btn_exit button.
     *
     * @param e an action event
     */
    private void btn_exitActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    /**
     * This method sets an action for the rd_randSet radio button.
     *
     * @param e an action event
     */
    private void rd_randSetItemStateChanged(ItemEvent e) {
        txt_trainSet.setEnabled(false);
        txt_trainSet.setText("");
        txt_trainSet.setBackground(new Color(240, 240, 240));
        txt_testSet.setEnabled(false);
        txt_testSet.setText("");
        txt_testSet.setBackground(new Color(240, 240, 240));
        txt_classLabel.setEnabled(false);
        txt_classLabel.setText("");
        txt_classLabel.setBackground(new Color(240, 240, 240));
        btn_trainSet.setEnabled(false);
        btn_testSet.setEnabled(false);
        btn_classLabel.setEnabled(false);


        txt_inputdst.setEnabled(true);
        txt_inputdst.setBackground(Color.WHITE);
        txt_classLbl.setEnabled(true);
        txt_classLbl.setBackground(Color.WHITE);
        btn_classlbl.setEnabled(true);
        btn_inputdst.setEnabled(true);
    }

    /**
     * This method sets an action for the rd_ttsetSet radio button.
     *
     * @param e an action event
     */
    private void rd_ttsetSetItemStateChanged(ItemEvent e) {
        txt_inputdst.setEnabled(false);
        txt_inputdst.setText("");
        txt_inputdst.setBackground(new Color(240, 240, 240));
        txt_classLbl.setEnabled(false);
        txt_classLbl.setText("");
        txt_classLbl.setBackground(new Color(240, 240, 240));
        btn_inputdst.setEnabled(false);
        btn_classlbl.setEnabled(false);

        txt_classLabel.setEnabled(true);
        txt_classLabel.setBackground(Color.WHITE);
        txt_testSet.setEnabled(true);
        txt_testSet.setBackground(Color.WHITE);
        txt_trainSet.setEnabled(true);
        txt_trainSet.setBackground(Color.WHITE);
        btn_classLabel.setEnabled(true);
        btn_testSet.setEnabled(true);
        btn_trainSet.setEnabled(true);
    }

    /**
     * This method sets an action for the cb_supervised combo box.
     *
     * @param e an action event
     */
    private void cb_supervisedItemStateChanged(ItemEvent e) {
        if (!cb_supervised.getSelectedItem().equals("none")) {
            cb_unsupervised.setSelectedItem("none");
            cb_wrapper.setSelectedItem("none");
            cb_embedded.setSelectedItem("none");
            cb_hybrid.setSelectedItem("none");
            if (cb_supervised.getSelectedItem().equals("Laplacian score")) {
                LaplacianScorePanel lapScorePanel = new LaplacianScorePanel();
                lapScorePanel.setDefaultValue();
                KNearest = lapScorePanel.getKNearest();
                constParam = lapScorePanel.getConstParam();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   k-NN = " + KNearest
//                        + "   constParam = " + constParam);
            } else if (cb_supervised.getSelectedItem().equals("Relevance-redundancy feature selection (RRFS)")) {
                RRFSPanel rrfsPanel = new RRFSPanel();
                rrfsPanel.setDefaultValue();
                simValue = rrfsPanel.getSimilarity();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   simValue = " + simValue);
            } else {
                btn_moreOpFilter.setEnabled(false);
            }
        } else {
            btn_moreOpFilter.setEnabled(false);
        }
    }

    /**
     * This method sets an action for the cb_unsupervised combo box.
     *
     * @param e an action event
     */
    private void cb_unsupervisedItemStateChanged(ItemEvent e) {
        if (!cb_unsupervised.getSelectedItem().equals("none")) {
            cb_supervised.setSelectedItem("none");
            cb_wrapper.setSelectedItem("none");
            cb_embedded.setSelectedItem("none");
            cb_hybrid.setSelectedItem("none");
            if (cb_unsupervised.getSelectedItem().equals("Laplacian score")) {
                LaplacianScorePanel lapScorePanel = new LaplacianScorePanel();
                lapScorePanel.setDefaultValue();
                KNearest = lapScorePanel.getKNearest();
                constParam = lapScorePanel.getConstParam();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   k-NN = " + KNearest
//                        + "   constParam = " + constParam);
            } else if (cb_unsupervised.getSelectedItem().equals("Random subspace method (RSM)")) {
                RSMPanel rsmPanel = new RSMPanel();
                rsmPanel.setDefaultValue();
                numSelection = rsmPanel.getNumSelection();
                sizeSubspace = rsmPanel.getSizeSubspace();
                elimination = rsmPanel.getElimination();
                multMethodName = rsmPanel.getMultMethodName();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   numSelection = " + numSelection
//                        + "   sizeSubspace = " + sizeSubspace
//                        + "   elimination = " + elimination
//                        + "    multMethodName = " + multMethodName);
            } else if (cb_unsupervised.getSelectedItem().equals("Relevance-redundancy feature selection (RRFS)")) {
                RRFSPanel rrfsPanel = new RRFSPanel();
                rrfsPanel.setDefaultValue();
                simValue = rrfsPanel.getSimilarity();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   simValue = " + simValue);
            } else if (cb_unsupervised.getSelectedItem().equals("UFSACO")) {
                UFSACOPanel ufsacoPanel = new UFSACOPanel();
                ufsacoPanel.setDefaultValue();
                initPheromone = ufsacoPanel.getInitPheromone();
                numIteration = ufsacoPanel.getNumIteration();
                numAnts = ufsacoPanel.getNumAnts();
                numFeatOfAnt = ufsacoPanel.getNumFeatOfAnt();
                evRate = ufsacoPanel.getEvRate();
                beta = ufsacoPanel.getBeta();
                q0 = ufsacoPanel.getQ0();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   initPheromone = " + initPheromone
//                        + "   numIteration = " + numIteration
//                        + "   numAnts = " + numAnts
//                        + "   numFeature = " + numFeatOfAnt
//                        + "   evRate = " + evRate
//                        + "   beta = " + beta
//                        + "   q0 = " + q0);
            } else if (cb_unsupervised.getSelectedItem().equals("RRFSACO_1")) {
                RRFSACO_1Panel rrfsacoPanel = new RRFSACO_1Panel();
                rrfsacoPanel.setDefaultValue();
                numIteration = rrfsacoPanel.getNumIteration();
                numAnts = rrfsacoPanel.getNumAnts();
                numFeatOfAnt = rrfsacoPanel.getNumFeatOfAnt();
                evRate = rrfsacoPanel.getEvRate();
                beta = rrfsacoPanel.getBeta();
                q0 = rrfsacoPanel.getQ0();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   numIteration = " + numIteration
//                        + "   numAnts = " + numAnts
//                        + "   numFeature = " + numFeatOfAnt
//                        + "   evRate = " + evRate
//                        + "   beta = " + beta
//                        + "   q0 = " + q0);
            } else if (cb_unsupervised.getSelectedItem().equals("RRFSACO_2")) {
                RRFSACO_2Panel rrfsacoPanel = new RRFSACO_2Panel();
                rrfsacoPanel.setDefaultValue();
                initPheromone = rrfsacoPanel.getInitPheromone();
                numIteration = rrfsacoPanel.getNumIteration();
                numAnts = rrfsacoPanel.getNumAnts();
                numFeatOfAnt = rrfsacoPanel.getNumFeatOfAnt();
                evRate = rrfsacoPanel.getEvRate();
                alpha = rrfsacoPanel.getAlpha();
                beta = rrfsacoPanel.getBeta();
                q0 = rrfsacoPanel.getQ0();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   initPheromone = " + initPheromone
//                        + "   numIteration = " + numIteration
//                        + "   numAnts = " + numAnts
//                        + "   numFeature = " + numFeatOfAnt
//                        + "   evRate = " + evRate
//                        + "   alpha = " + alpha
//                        + "   beta = " + beta
//                        + "   q0 = " + q0);
            } else if (cb_unsupervised.getSelectedItem().equals("IRRFSACO_1")) {
                IRRFSACO_1Panel irrfsacoPanel = new IRRFSACO_1Panel();
                irrfsacoPanel.setDefaultValue();
                numIteration = irrfsacoPanel.getNumIteration();
                numAnts = irrfsacoPanel.getNumAnts();
                numFeatOfAnt = irrfsacoPanel.getNumFeatOfAnt();
                evRate = irrfsacoPanel.getEvRate();
                beta = irrfsacoPanel.getBeta();
                q0 = irrfsacoPanel.getQ0();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   numIteration = " + numIteration
//                        + "   numAnts = " + numAnts
//                        + "   numFeature = " + numFeatOfAnt
//                        + "   evRate = " + evRate
//                        + "   beta = " + beta
//                        + "   q0 = " + q0);
            } else if (cb_unsupervised.getSelectedItem().equals("IRRFSACO_2")) {
                IRRFSACO_2Panel irrfsacoPanel = new IRRFSACO_2Panel();
                irrfsacoPanel.setDefaultValue();
                initPheromone = irrfsacoPanel.getInitPheromone();
                numIteration = irrfsacoPanel.getNumIteration();
                numAnts = irrfsacoPanel.getNumAnts();
                numFeatOfAnt = irrfsacoPanel.getNumFeatOfAnt();
                evRate = irrfsacoPanel.getEvRate();
                alpha = irrfsacoPanel.getAlpha();
                beta = irrfsacoPanel.getBeta();
                q0 = irrfsacoPanel.getQ0();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   initPheromone = " + initPheromone
//                        + "   numIteration = " + numIteration
//                        + "   numAnts = " + numAnts
//                        + "   numFeature = " + numFeatOfAnt
//                        + "   evRate = " + evRate
//                        + "   alpha = " + alpha
//                        + "   beta = " + beta
//                        + "   q0 = " + q0);
            } else if (cb_unsupervised.getSelectedItem().equals("MGSACO")) {
                MGSACOPanel mgsacoPanel = new MGSACOPanel();
                mgsacoPanel.setDefaultValue();
                initPheromone = mgsacoPanel.getInitPheromone();
                numIteration = mgsacoPanel.getNumIteration();
                numAnts = mgsacoPanel.getNumAnts();
                evRate = mgsacoPanel.getEvRate();
                beta = mgsacoPanel.getBeta();
                q0 = mgsacoPanel.getQ0();
                btn_moreOpFilter.setEnabled(true);
//                System.out.println("default:   initPheromone = " + initPheromone
//                        + "   numIteration = " + numIteration
//                        + "   numAnts = " + numAnts
//                        + "   evRate = " + evRate
//                        + "   beta = " + beta
//                        + "   q0 = " + q0);
            } else {
                btn_moreOpFilter.setEnabled(false);
            }
        } else {
            btn_moreOpFilter.setEnabled(false);
        }
    }

    /**
     * This method sets an action for the cb_wrapper combo box.
     *
     * @param e an action event
     */
    private void cb_wrapperItemStateChanged(ItemEvent e) {
        if (!cb_wrapper.getSelectedItem().equals("none")) {
            cb_supervised.setSelectedItem("none");
            cb_unsupervised.setSelectedItem("none");
            cb_embedded.setSelectedItem("none");
            cb_hybrid.setSelectedItem("none");
            if (cb_wrapper.getSelectedItem().equals("GeneticAlgorithm")) {

                GeneticPanel mgsacoPanel = new GeneticPanel();
                mgsacoPanel.setDefaultValue();
                numPopulation = mgsacoPanel.getNumPopulation();
                numGeneration = mgsacoPanel.getNumGeneration();
                pCrossover = mgsacoPanel.getpCrossover();
                pMutation = mgsacoPanel.getpMutation();
                btn_moreOpWrapper.setEnabled(true);
            } else {
                btn_moreOpWrapper.setEnabled(false);
            }
        } else {
            btn_moreOpWrapper.setEnabled(false);
        }
    }

    /**
     * This method sets an action for the cb_embedded combo box.
     *
     * @param e an action event
     */
    private void cb_embeddedItemStateChanged(ItemEvent e) {
        if (!cb_embedded.getSelectedItem().equals("none")) {
            cb_supervised.setSelectedItem("none");
            cb_unsupervised.setSelectedItem("none");
            cb_wrapper.setSelectedItem("none");
            cb_hybrid.setSelectedItem("none");
//            if (cb_embedded.getSelectedItem().equals("name methods")) {
//                btn_moreOpEmbedded.setEnabled(true);
//            } else {
//                btn_moreOpEmbedded.setEnabled(false);
//            }
        } else {
            btn_moreOpEmbedded.setEnabled(false);
        }
    }

    /**
     * This method sets an action for the cb_hybrid combo box.
     *
     * @param e an action event
     */
    private void cb_hybridItemStateChanged(ItemEvent e) {
        if (!cb_hybrid.getSelectedItem().equals("none")) {
            cb_supervised.setSelectedItem("none");
            cb_unsupervised.setSelectedItem("none");
            cb_wrapper.setSelectedItem("none");
            cb_embedded.setSelectedItem("none");
//            if (cb_hybrid.getSelectedItem().equals("name methods")) {
//                btn_moreOpHybrid.setEnabled(true);
//            } else {
//                btn_moreOpHybrid.setEnabled(false);
//            }
        } else {
            btn_moreOpHybrid.setEnabled(false);
        }
    }

    /**
     * This method sets an action for the cb_classifier combo box.
     *
     * @param e an action event
     */
    private void cb_classifierItemStateChanged(ItemEvent e) {
        if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
            SVMClassifierPanel svmPanel = new SVMClassifierPanel();
            svmPanel.setDefaultValue();
            typeKernel = svmPanel.getKernel();
            btn_moreOpClassifier.setEnabled(true);
//            System.out.println("default:   kernel = " + typeKernel);
        } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
            DTClassifierPanel dtPanel = new DTClassifierPanel();
            dtPanel.setDefaultValue();
            confidence = dtPanel.getConfidence();
            minNum = dtPanel.getMinNum();
            btn_moreOpClassifier.setEnabled(true);
//            System.out.println("default:    min num = " + minNum + "  confidence = " + confidence);
        } else {
            btn_moreOpClassifier.setEnabled(false);
        }
    }

    /**
     * This method sets an action for the cb_start combo box.
     *
     * @param e an action event
     */
    private void cb_startItemStateChanged(ItemEvent e) {
    }

    /**
     * This method performs the feature selection based on information gain
     * method.
     *
     * @see KurdFeast.featureSelection.filter.supervised.InformationGain
     */
    private void infoGainPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                InformationGain method = new InformationGain(numSelectedSubsets[j]);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                double[] computeValues = method.getValues();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(computeValues, "information gain"));
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on gain ratio method.
     *
     * @see KurdFeast.featureSelection.filter.supervised.GainRatio
     */
    private void gainRatioPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                GainRatio method = new GainRatio(numSelectedSubsets[j]);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                double[] computeValues = method.getValues();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(computeValues, "gain ratio"));
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on symmetrical
     * uncertainty method.
     *
     * @see KurdFeast.featureSelection.filter.supervised.SymmetricalUncertainty
     */
    private void symmetricalUncertaintyPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                SymmetricalUncertainty method = new SymmetricalUncertainty(numSelectedSubsets[j]);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                double[] computeValues = method.getValues();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(computeValues, "symmetrical uncertainty"));
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on Fisher score method.
     *
     * @see KurdFeast.featureSelection.filter.supervised.FisherScore
     */
    private void fisherScorePerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                FisherScore method = new FisherScore(numSelectedSubsets[j]);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                double[] computeValues = method.getValues();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(computeValues, "Fisher score"));
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on gini index method.
     *
     * @see KurdFeast.featureSelection.filter.supervised.GiniIndex
     */
    private void giniIndexPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                GiniIndex method = new GiniIndex(numSelectedSubsets[j]);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                double[] computeValues = method.getValues();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(computeValues, "gini index"));
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on supervised Laplacian
     * score method.
     *
     * @see KurdFeast.featureSelection.filter.supervised.LaplacianScore
     */
    private void laplacianScoreSupPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                LaplacianScore method = new LaplacianScore(numSelectedSubsets[j], constParam);
//                System.out.println("Laplacian score...   constant param: " + constParam);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                double[] computeValues = method.getValues();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(computeValues, "Laplacian score"));
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on minimal redundancy
     * maximal relevance (mRMR) method.
     *
     * @see KurdFeast.featureSelection.filter.supervised.MRMR
     */
    private void MRMRPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                MRMR method = new MRMR(numSelectedSubsets[j]);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on supervised
     * relevance-redundancy feature selection(RRFS) method.
     *
     * @see KurdFeast.featureSelection.filter.supervised.RRFS
     */
    private void RRFSSupPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                long startTime = System.currentTimeMillis();

                RRFS method = new RRFS(numSelectedSubsets[j], simValue);
//                System.out.println("RRFS.....    similarity = " + simValue);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                //shows new results in the panel of results
                resPanel.setMessage("    " + subset.length + " feature selected:\n");
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on term variance method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.TermVariance
     */
    private void termVariancePerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                TermVariance method = new TermVariance(numSelectedSubsets[j]);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                double[] computeValues = method.getValues();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(computeValues, "term variance"));
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on unsupervised
     * Laplacian score method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.LaplacianScore
     */
    private void laplacianScoreUnsupPerform() {
        if (KNearest >= data.getNumTrainSet()) {
            JOptionPane.showMessageDialog(null, "The parameter value of Laplacian score (k-nearest neighbor) is incorred.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());

            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;

            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    long startTime = System.currentTimeMillis();

                    KFST.featureSelection.filter.unsupervised.LaplacianScore method = new KFST.featureSelection.filter.unsupervised.LaplacianScore(numSelectedSubsets[j], constParam, KNearest);
//                    System.out.println("Laplacian score...   constant param: " + constParam
//                            + "   KNN = " + KNearest);
                    method.loadDataSet(data);
                    method.evaluateFeatures();

                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();
                    double[] computeValues = method.getValues();
                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(computeValues, "Laplacian score"));
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                //randomly splits the datasets
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();
        }
    }

    /**
     * This method performs the feature selection based on mutual correlation
     * method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.MutualCorrelation
     */
    private void mutualCorrelationPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                long startTime = System.currentTimeMillis();

                MutualCorrelation method = new MutualCorrelation(numSelectedSubsets[j]);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                //shows new results in the panel of results
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on random subspace
     * method (RSM) method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.RSM
     */
    private void RSMPerform() {
        if (sizeSubspace > data.getNumFeature() || elimination > sizeSubspace) {
            JOptionPane.showMessageDialog(null, "The parameter values of RSM (size of subspace or elimination threshold) are incorred.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());

            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;

            //sets the correct values of the RSM parameter
            int newSizeSubspace = sizeSubspace;
            int newElimination = elimination;
            if (sizeSubspace == 0 && elimination == 0) {
                newSizeSubspace = data.getNumFeature() / 2 + 1;
                newElimination = newSizeSubspace / 2 + 1;
            } else if (elimination == 0) {
                newElimination = newSizeSubspace / 2 + 1;
            }

            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    long startTime = System.currentTimeMillis();

                    RSM method = new RSM(numSelectedSubsets[j], numSelection, newSizeSubspace, newElimination, multMethodName);
//                    System.out.println("RSM...   num Selection = " + numSelection
//                            + "   newSizeSubspace = " + newSizeSubspace
//                            + "   newElimination = " + newElimination
//                            + "   multMethodName = " + multMethodName);
                    method.loadDataSet(data);
                    method.evaluateFeatures();

                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();
                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                //randomly splits the datasets
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();
        }
    }

    /**
     * This method performs the feature selection based on unsupervised
     * relevance-redundancy feature selection(RRFS) method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.RRFS
     */
    private void RRFSUnsupPerform() {
        progressValue = 1;
        repaint();
        ResultPanel resPanel = new ResultPanel(pathProject);
        //save initial information of the dataset
        resPanel.setMessage(addTextToPanel());

        int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
        accuracies = new double[numRuns][numSelectedSubsets.length];
        times = new double[numRuns][numSelectedSubsets.length];
        String[][] Results = new String[numRuns][numSelectedSubsets.length];
        double totalValuesProgress = numRuns * numSelectedSubsets.length;

        for (int i = 0; i < numRuns; i++) {
            resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
            for (int j = 0; j < numSelectedSubsets.length; j++) {
                long startTime = System.currentTimeMillis();

                KFST.featureSelection.filter.unsupervised.RRFS method = new KFST.featureSelection.filter.unsupervised.RRFS(numSelectedSubsets[j], simValue);
//                System.out.println("RRFS.....    similarity = " + simValue);
                method.loadDataSet(data);
                method.evaluateFeatures();

                long endTime = System.currentTimeMillis();
                times[i][j] = (endTime - startTime) / 1000.0;

                int[] subset = method.getSelectedFeatureSubset();
                //shows new results in the panel of results
                resPanel.setMessage("    " + subset.length + " feature selected:\n");
                resPanel.setMessage(addTextToPanel(subset));

                Results[i][j] = data.createFeatNames(subset);

                String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                    accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                    accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                    accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                }

                //updates the value of progress bar
                progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                repaint();
            }
            //randomly splits the datasets
            if (rd_randSet.isSelected()) {
                data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
            }
        }
        createFeatureFiles(Results, pathProject);
        errorRates = MathFunc.computeErrorRate(accuracies);
        averageAccuracies = MathFunc.computeAverageArray(accuracies);
        averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
        averageTimes = MathFunc.computeAverageArray(times);

        //show the result values in the panel of result
        resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
        resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
        resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
        resPanel.setMessage(addTextToPanel(times, "Execution times"));
        resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
        resPanel.setEnabledButton();
        setEnabledItem();
    }

    /**
     * This method performs the feature selection based on unsupervised feature
     * selection based on ant colony optimization (UFSACO) method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.UFSACO
     */
    private void UFSACOPerform() {
        if (numAnts > data.getNumFeature() || numFeatOfAnt > data.getNumFeature()) {
            JOptionPane.showMessageDialog(null, "The parameter values of UFSACO (number of ants or number of features for ant) are incorred.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());

            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;

            //sets the correct values of the UFSACO parameters
            int newNumFeatOfAnt = numFeatOfAnt;
            int newNumAnts = numAnts;
            if (numAnts == 0) {
                if (data.getNumFeature() < 100) {
                    newNumAnts = data.getNumFeature();
                } else {
                    newNumAnts = 100;
                }
            }

            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    if (numFeatOfAnt == 0) {
                        newNumFeatOfAnt = numSelectedSubsets[j];
                    }

                    long startTime = System.currentTimeMillis();

                    UFSACO method = new UFSACO(numSelectedSubsets[j], initPheromone, numIteration, newNumAnts, newNumFeatOfAnt, evRate, beta, q0);
//                    System.out.println("UFSACO...   initPheromone = " + initPheromone
//                            + "   numIteration = " + numIteration
//                            + "   newNumAnts = " + newNumAnts
//                            + "   newNumFeatOfAnt = " + newNumFeatOfAnt
//                            + "   evRate = " + evRate
//                            + "   beta = " + beta
//                            + "   q0 = " + q0);
                    method.loadDataSet(data);
                    method.evaluateFeatures();

                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();
                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                //randomly splits the datasets
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();
        }
    }

    /**
     * This method performs the feature selection based on relevance–redundancy
     * feature selection based on ant colony optimization, version1 (RRFSACO_1)
     * method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.RRFSACO_1
     */
    private void RRFSACO_1Perform() {
        if (numAnts > data.getNumFeature() || numFeatOfAnt > data.getNumFeature()) {
            JOptionPane.showMessageDialog(null, "The parameter values of RRFSACO_1 (number of ants or number of features for ant) are incorred.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());

            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;

            //sets the correct values of the RRFSACO_1 parameters
            int newNumFeatOfAnt = numFeatOfAnt;
            int newNumAnts = numAnts;
            if (numAnts == 0) {
                if (data.getNumFeature() < 100) {
                    newNumAnts = data.getNumFeature();
                } else {
                    newNumAnts = 100;
                }
            }

            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    if (numFeatOfAnt == 0) {
                        newNumFeatOfAnt = numSelectedSubsets[j];
                    }

                    long startTime = System.currentTimeMillis();

                    RRFSACO_1 method = new RRFSACO_1(numSelectedSubsets[j], numIteration, newNumAnts, newNumFeatOfAnt, evRate, beta, q0);
//                    System.out.println("RRFSACO_1...   numIteration = " + numIteration
//                            + "   newNumAnts = " + newNumAnts
//                            + "   newNumFeatOfAnt = " + newNumFeatOfAnt
//                            + "   evRate = " + evRate
//                            + "   beta = " + beta
//                            + "   q0 = " + q0);
                    method.loadDataSet(data);
                    method.evaluateFeatures();

                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();
                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                //randomly splits the datasets
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();
        }
    }

    /**
     * This method performs the feature selection based on relevance–redundancy
     * feature selection based on ant colony optimization, version2 (RRFSACO_2)
     * method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.RRFSACO_2
     */
    private void RRFSACO_2Perform() {
        if (numAnts > data.getNumFeature() || numFeatOfAnt > data.getNumFeature()) {
            JOptionPane.showMessageDialog(null, "The parameter values of RRFSACO_2 (number of ants or number of features for ant) are incorred.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());

            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;

            //sets the correct values of the RRFSACO_2 parameters
            int newNumFeatOfAnt = numFeatOfAnt;
            int newNumAnts = numAnts;
            if (numAnts == 0) {
                if (data.getNumFeature() < 100) {
                    newNumAnts = data.getNumFeature();
                } else {
                    newNumAnts = 100;
                }
            }

            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    if (numFeatOfAnt == 0) {
                        newNumFeatOfAnt = numSelectedSubsets[j];
                    }

                    long startTime = System.currentTimeMillis();

                    RRFSACO_2 method = new RRFSACO_2(numSelectedSubsets[j], initPheromone, numIteration, newNumAnts, newNumFeatOfAnt, evRate, alpha, beta, q0);
//                    System.out.println("RRFSACO_2...   initPheromone = " + initPheromone
//                            + "   numIteration = " + numIteration
//                            + "   newNumAnts = " + newNumAnts
//                            + "   newNumFeatOfAnt = " + newNumFeatOfAnt
//                            + "   evRate = " + evRate
//                            + "   alpha = " + alpha
//                            + "   beta = " + beta
//                            + "   q0 = " + q0);
                    method.loadDataSet(data);
                    method.evaluateFeatures();

                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();
                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                //randomly splits the datasets
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();
        }
    }

    /**
     * This method performs the feature selection based on incremental
     * relevance–redundancy feature selection based on ant colony optimization,
     * version1 (IRRFSACO_1) method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.IRRFSACO_1
     */
    private void IRRFSACO_1Perform() {
        if (numAnts > data.getNumFeature() || numFeatOfAnt > data.getNumFeature()) {
            JOptionPane.showMessageDialog(null, "The parameter values of IRRFSACO_1 (number of ants or number of features for ant) are incorred.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());

            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;

            //sets the correct values of the IRRFSACO_1 parameters
            int newNumFeatOfAnt = numFeatOfAnt;
            int newNumAnts = numAnts;
            if (numAnts == 0) {
                if (data.getNumFeature() < 100) {
                    newNumAnts = data.getNumFeature();
                } else {
                    newNumAnts = 100;
                }
            }

            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    if (numFeatOfAnt == 0) {
                        newNumFeatOfAnt = numSelectedSubsets[j];
                    }

                    long startTime = System.currentTimeMillis();

                    IRRFSACO_1 method = new IRRFSACO_1(numSelectedSubsets[j], numIteration, newNumAnts, newNumFeatOfAnt, evRate, beta, q0);
//                    System.out.println("IRRFSACO_1...   numIteration = " + numIteration
//                            + "   newNumAnts = " + newNumAnts
//                            + "   newNumFeatOfAnt = " + newNumFeatOfAnt
//                            + "   evRate = " + evRate
//                            + "   beta = " + beta
//                            + "   q0 = " + q0);
                    method.loadDataSet(data);
                    method.evaluateFeatures();

                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();
                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                //randomly splits the datasets
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();
        }
    }

    /**
     * This method performs the feature selection based on incremental
     * relevance–redundancy feature selection based on ant colony optimization,
     * version2 (IRRFSACO_2) method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.IRRFSACO_2
     */
    private void IRRFSACO_2Perform() {
        if (numAnts > data.getNumFeature() || numFeatOfAnt > data.getNumFeature()) {
            JOptionPane.showMessageDialog(null, "The parameter values of IRRFSACO_2 (number of ants or number of features for ant) are incorred.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());

            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;

            //sets the correct values of the IRRFSACO_2 parameters
            int newNumFeatOfAnt = numFeatOfAnt;
            int newNumAnts = numAnts;
            if (numAnts == 0) {
                if (data.getNumFeature() < 100) {
                    newNumAnts = data.getNumFeature();
                } else {
                    newNumAnts = 100;
                }
            }

            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    if (numFeatOfAnt == 0) {
                        newNumFeatOfAnt = numSelectedSubsets[j];
                    }

                    long startTime = System.currentTimeMillis();

                    IRRFSACO_2 method = new IRRFSACO_2(numSelectedSubsets[j], initPheromone, numIteration, newNumAnts, newNumFeatOfAnt, evRate, alpha, beta, q0);
//                    System.out.println("IRRFSACO_2...   initPheromone = " + initPheromone
//                            + "   numIteration = " + numIteration
//                            + "   newNumAnts = " + newNumAnts
//                            + "   newNumFeatOfAnt = " + newNumFeatOfAnt
//                            + "   evRate = " + evRate
//                            + "   alpha = " + alpha
//                            + "   beta = " + beta
//                            + "   q0 = " + q0);
                    method.loadDataSet(data);
                    method.evaluateFeatures();

                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();
                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                //randomly splits the datasets
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();
        }
    }

    /**
     * This method performs the feature selection based on microarray gene
     * selection based on ant colony optimization (MGSACO) method.
     *
     * @see KurdFeast.featureSelection.filter.unsupervised.MGSACO
     */
    private void MGSACOPerform() {
        if (numAnts > data.getNumFeature()) {
            JOptionPane.showMessageDialog(null, "The parameter value of MGSACO (number of ants) is incorred.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());

            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;

            //sets the correct values of the MGSACO parameter
            int newNumAnts = numAnts;
            if (numAnts == 0) {
                if (data.getNumFeature() < 100) {
                    newNumAnts = data.getNumFeature();
                } else {
                    newNumAnts = 100;
                }
            }

            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    long startTime = System.currentTimeMillis();

                    MGSACO method = new MGSACO(numSelectedSubsets[j], initPheromone, numIteration, newNumAnts, evRate, beta, q0);
//                    System.out.println("MGSACO...   initPheromone = " + initPheromone
//                            + "   numIteration = " + numIteration
//                            + "   newNumAnts = " + newNumAnts
//                            + "   evRate = " + evRate
//                            + "   beta = " + beta
//                            + "   q0 = " + q0);
                    method.loadDataSet(data);
                    method.evaluateFeatures();

                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();
                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);
                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);
                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);

                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                //randomly splits the datasets
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();
        }
    }


    /**
     * This method performs the feature selection based on Genetic Algorithm
     *
     * @see KFST.featureSelection.wrapper.GeneticAlgorithm
     */
    private void geneticAlgorithmPerform() throws Exception {
        if (false) {
            JOptionPane.showMessageDialog(null, "genetic algorithm parameter error", "Error", JOptionPane.ERROR_MESSAGE);

        } else {

            progressValue = 1;
            repaint();
            ResultPanel resPanel = new ResultPanel(pathProject);
            //save initial information of the dataset
            resPanel.setMessage(addTextToPanel());
            int numRuns = Integer.parseInt(cb_start.getSelectedItem().toString());
            accuracies = new double[numRuns][numSelectedSubsets.length];
            times = new double[numRuns][numSelectedSubsets.length];
            String[][] Results = new String[numRuns][numSelectedSubsets.length];
            double totalValuesProgress = numRuns * numSelectedSubsets.length;
            for (int i = 0; i < numRuns; i++) {
                resPanel.setMessage("  Iteration (" + (i + 1) + "):\n");
                for (int j = 0; j < numSelectedSubsets.length; j++) {
                    resPanel.setMessage("    " + numSelectedSubsets[j] + " feature selected:\n");
                    long startTime = System.currentTimeMillis();


                    GeneticAlgorithmMain method = new GeneticAlgorithmMain(numSelectedSubsets[j], numPopulation, numGeneration, pCrossover, pMutation, cb_classifier.getSelectedItem().toString());
                  /*  System.out.println("GeneticAlgorithm...   numPopulation = " + numPopulation
                            + "   numGeneration = " + numGeneration
                            + "   pCrossover = " + pCrossover
                            + "   pMutation = " + pMutation);*/
                    method.loadDataSet(data);
                    method.initialize();
                    method.start();
                    method.evaluateFeatures();


                    long endTime = System.currentTimeMillis();
                    times[i][j] = (endTime - startTime) / 1000.0;

                    int[] subset = method.getSelectedFeatureSubset();


                    //shows new results in the panel of results
                    resPanel.setMessage(addTextToPanel(subset));

                    Results[i][j] = data.createFeatNames(subset);

                    String nameTrainDataCSV = pathDataCSV + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTrainDataARFF = pathDataARFF + "trainSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";
                    String nameTestDataCSV = pathDataCSV + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].csv";
                    String nameTestDataARFF = pathDataARFF + "testSet[" + (i + 1) + "-" + numSelectedSubsets[j] + "].arff";

                    data.createCSVFile(data.getTrainSet(), subset, nameTrainDataCSV);

                    data.createCSVFile(data.getTestSet(), subset, nameTestDataCSV);

                    arff.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, pathProject, subset.length, data);
                    arff.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, pathProject, subset.length, data);
                    if (cb_classifier.getSelectedItem().equals("Support Vector Machine (SVM)")) {
                        accuracies[i][j] = WekaClassifier.SVM(nameTrainDataARFF, nameTestDataARFF, typeKernel);
                    } else if (cb_classifier.getSelectedItem().equals("Naive Bayes (NB)")) {
                        accuracies[i][j] = WekaClassifier.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
                    } else if (cb_classifier.getSelectedItem().equals("Decision Tree (C4.5)")) {
                        accuracies[i][j] = WekaClassifier.dTree(nameTrainDataARFF, nameTestDataARFF, confidence, minNum);
                    }

                    //updates the value of progress bar
                    progressValue = (int) ((upProgValue(numSelectedSubsets.length, i, j) / totalValuesProgress) * 100);
                    repaint();
                }
                if (rd_randSet.isSelected()) {
                    data.preProcessing(txt_inputdst.getText(), txt_classLbl.getText());
                }
            }
            createFeatureFiles(Results, pathProject);
            errorRates = MathFunc.computeErrorRate(accuracies);
            averageAccuracies = MathFunc.computeAverageArray(accuracies);
            averageErrorRates = MathFunc.computeErrorRate(averageAccuracies);
            averageTimes = MathFunc.computeAverageArray(times);

            //show the result values in the panel of result
            resPanel.setMessage(addTextToPanel(Results, "Subsets of selected features in each iteration"));
            resPanel.setMessage(addTextToPanel(accuracies, "Classification accuracies"));
            resPanel.setMessage(addTextToPanel(averageAccuracies, "Average classification accuracies", true));
            resPanel.setMessage(addTextToPanel(times, "Execution times"));
            resPanel.setMessage(addTextToPanel(averageTimes, "Average execution times", true));
            resPanel.setEnabledButton();
            setEnabledItem();

        }
    }


    /**
     * enables the status of diagrams menu item
     */
    private void setEnabledItem() {
        mi_exeTime.setEnabled(true);
        mi_accur.setEnabled(true);
        mi_error.setEnabled(true);
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about some information of the dataset.
     *
     * @return the created message as a string
     * @see KurdFeast.gui.ResultPanel
     * @see KurdFeast.dataset.DatasetInfo
     */
    private String addTextToPanel() {
        String messages = "General infomation:\n";
        messages += "   Path of the workspace: " + pathProject + "\n";
        messages += "   Number of samples in the training set: " + data.getNumTrainSet() + "\n";
        messages += "   Number of samples in the test set: " + data.getNumTestSet() + "\n";
        messages += "   Number of features: " + data.getNumFeature() + "\n";
        messages += "   Number of classes: " + data.getNumClass() + "\n";
        messages += "   Name of classes {";
        for (int i = 0; i < data.getNumClass() - 1; i++) {
            messages += data.getClassLabel()[i] + ", ";
        }
        messages += data.getClassLabel()[data.getNumClass() - 1] + "}\n\n";
        messages += "Start feature selection process:\n";
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the subset of selected features.
     *
     * @param array an array of text to insert
     * @return the created message as a string
     * @see KurdFeast.gui.ResultPanel
     */
    private String addTextToPanel(int[] array) {
        String messages = "\n        subset selected {";
        for (int k = 0; k < array.length - 1; k++) {
            messages += String.valueOf(array[k] + 1) + ", ";
        }
        messages += String.valueOf(array[array.length - 1] + 1) + "}\n\n";
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the relevance values of features computed by given
     * feature selection method.
     *
     * @param array      an array of text to insert
     * @param nameMethod the name of feature selection method
     * @return the created message as a string
     * @see KurdFeast.gui.ResultPanel
     */
    private String addTextToPanel(double[] array, String nameMethod) {
        String messages = "";
        for (int k = 0; k < array.length; k++) {
            messages += "        " + nameMethod + "(" + data.getNameFeatures()[k] + ") = " + MathFunc.roundDouble(array[k], 4) + "\n";
        }
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the final subsets of selected features.
     *
     * @param array an array of text to insert
     * @param title the name of results
     * @return the created message as a string
     * @see KurdFeast.gui.ResultPanel
     */
    private String addTextToPanel(String[][] array, String title) {
        String messages = "\n\n" + title + ":\n";
        for (int i = 0; i < array.length; i++) {
            messages += "     Iteration (" + String.valueOf(i + 1) + "):\n";
            for (int j = 0; j < array[0].length; j++) {
                messages += "          " + String.valueOf(array[i][j]) + "\n";
            }
            messages += "\n";
        }
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the classification accuracy and execution time in each
     * iteration.
     *
     * @param array an array of text to insert
     * @param title the name of results
     * @return the created message as a string
     * @see KurdFeast.gui.ResultPanel
     */
    private String addTextToPanel(double[][] array, String title) {
        String messages = "\n\n" + title + ":\n";
        for (int i = 0; i < array.length; i++) {
            messages += "     Iteration (" + String.valueOf(i + 1) + "):";
            for (int j = 0; j < array[0].length; j++) {
                messages += "  " + MathFunc.roundDouble(array[i][j], 3);
            }
            messages += "\n";
        }
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the average classification accuracies and average
     * execution times in all iteration.
     *
     * @param array     an array of text to insert
     * @param title     the name of results
     * @param isAverage shows that the average values must be displayed
     * @return the created message as a string
     * @see KurdFeast.gui.ResultPanel
     */
    private String addTextToPanel(double[][] array, String title, boolean isAverage) {
        String messages = "\n\n" + title + ":\n";
        messages += "     All Iterations:  ";
        for (int j = 0; j < array[0].length; j++) {
            messages += MathFunc.roundDouble(array[0][j], 3) + "  ";
        }
        messages += "\n";
        return messages;
    }

    /**
     * This method create a text file of the subsets of selected features
     *
     * @param result      the subsets of selected features in each iterations
     * @param pathProject the path of the project
     */
    private void createFeatureFiles(String[][] result, String pathProject) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(pathProject + "FeatureSubsetsFile.txt"));
            pw.println("Subsets of selected features in each iteration:\n");
            for (int i = 0; i < result.length; i++) {
                pw.println("     Iteration (" + (i + 1) + "):");
                for (int j = 0; j < result[0].length; j++) {
                    pw.println("          " + result[i][j]);
                }
                pw.println();
            }
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method checks the status of the paths of the data files
     *
     * @return true if the paths are valid
     */
    private boolean isCorrectDataset() {
        if ((rd_randSet.isSelected()
                && (txt_inputdst.getText().equals("")
                || txt_classLbl.getText().equals("")))
                || (rd_ttset.isSelected()
                && (txt_trainSet.getText().equals("")
                || txt_testSet.getText().equals("")
                || txt_classLabel.getText().equals("")))) {
            return false;
        }
        return true;
    }

    /**
     * This method prints the error messages due to unselected or incorrect
     * input values in the dataset, parameter settings, classifier,
     * and run configuration panels
     *
     * @return true if any error have been occurred
     */
    private boolean printErrorMessages() {
        String errorMessages[] = {"  - Path of input dataset or class label isn't valid.",
                "  - Dataset file is incorrect.",
                "  - Class label file is incorrect.",
                "  - Train and test sets aren't compatible.",
                "  - Labels of the samples aren't compatible to the class label file.",
                "  - Feature selction method hasn't been selected.",
                "  - Number of selected features is greater than the original features.",
                "  - Numbers of selected features are empty.",
                "  - Classifier hasn't been selected.",
                "  - Number of runs haven't been selected."};
        String selectedMessages = "Following errors were occured before the starting feature selection process:\n";

        boolean checkError = false;

        //checks the status of the dataset panel
        if (isCorrectDataset()) {
            if (!data.isCorrectDataset()) {
                selectedMessages += errorMessages[1] + "\n";
                checkError = true;
            }
            if (!data.isCompatibleTrainTestSet()) {
                selectedMessages += errorMessages[3] + "\n";
                checkError = true;
            }
            if (!data.isCorrectClassLabel()) {
                selectedMessages += errorMessages[2] + "\n";
                checkError = true;
            }
            if (!data.isCorrectSamplesClass()) {
                selectedMessages += errorMessages[4] + "\n";
                checkError = true;
            }
            //checks the status of the num selected features panel
            if (txtArea_feature.getText().equals("")) {
                selectedMessages += errorMessages[7] + "\n";
                checkError = true;
            } else {
                for (int i = 0; i < numSelectedSubsets.length; i++) {
                    if (numSelectedSubsets[i] > data.getNumFeature()) {
                        selectedMessages += errorMessages[6] + "\n";
                        checkError = true;
                        break;
                    }
                }
            }
        } else {
            selectedMessages += errorMessages[0] + "\n";
            checkError = true;
        }
        //checks the status of the feature selection panel
        if (cb_supervised.getSelectedItem().equals("none")
                && cb_unsupervised.getSelectedItem().equals("none")
                && cb_wrapper.getSelectedItem().equals("none")
                && cb_embedded.getSelectedItem().equals("none")
                && cb_hybrid.getSelectedItem().equals("none")) {
            selectedMessages += errorMessages[5] + "\n";
            checkError = true;
        }
        //checks the status of the classifier panel
        if (cb_classifier.getSelectedItem().equals("none")) {
            selectedMessages += errorMessages[8] + "\n";
            checkError = true;
        }
        //checks the status of the run configuration panel
        if (cb_start.getSelectedItem().equals("none")) {
            selectedMessages += errorMessages[9] + "\n";
            checkError = true;
        }
        if (checkError) {
            JOptionPane.showMessageDialog(null, selectedMessages, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * updates the value of progress bar
     *
     * @param totalSize   the size of different values of feature subsets
     * @param currentRun  the current runs of the algorithm
     * @param currentSize the index of the current subset
     * @return the new value of progress bar
     */
    private int upProgValue(int totalSize, int currentRun, int currentSize) {
        return (currentRun * totalSize) + currentSize + 1;
    }

    /**
     * create and show the main panel of the project
     */
    public void createAndShow() {
        JFrame f = new JFrame();
        f.setTitle("KFST Main Panel");
        f.setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        f.setLayout(new BorderLayout());
        f.add(this, BorderLayout.CENTER);
        f.add(menuBar, BorderLayout.NORTH);
        f.setSize(865, 660);
        rd_randSet.setSelected(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    /**
     * This class is used to create a thread
     */
    class Counter implements Runnable {

        /**
         * This method takes any action whatsoever.
         */
        @Override
        public void run() {

            if (runCode == 0) {
                if (cb_supervised.getSelectedItem().equals("Information gain")) {
                    infoGainPerform();
                } else if (cb_supervised.getSelectedItem().equals("Gain ratio")) {
                    gainRatioPerform();
                } else if (cb_supervised.getSelectedItem().equals("Symmetrical uncertainty")) {
                    symmetricalUncertaintyPerform();
                } else if (cb_supervised.getSelectedItem().equals("Fisher score")) {
                    fisherScorePerform();
                } else if (cb_supervised.getSelectedItem().equals("Gini index")) {
                    giniIndexPerform();
                } else if (cb_supervised.getSelectedItem().equals("Laplacian score")) {
                    laplacianScoreSupPerform();
                } else if (cb_supervised.getSelectedItem().equals("Minimal redundancy maximal relevance (MRMR)")) {
                    MRMRPerform();
                } else if (cb_supervised.getSelectedItem().equals("Relevance-redundancy feature selection (RRFS)")) {
                    RRFSSupPerform();
                } else if (cb_unsupervised.getSelectedItem().equals("Term variance")) {
                    termVariancePerform();
                } else if (cb_unsupervised.getSelectedItem().equals("Laplacian score")) {
                    laplacianScoreUnsupPerform();
                } else if (cb_unsupervised.getSelectedItem().equals("Mutual correlation")) {
                    mutualCorrelationPerform();
                } else if (cb_unsupervised.getSelectedItem().equals("Random subspace method (RSM)")) {
                    RSMPerform();
                } else if (cb_unsupervised.getSelectedItem().equals("Relevance-redundancy feature selection (RRFS)")) {
                    RRFSUnsupPerform();
                } else if (cb_unsupervised.getSelectedItem().equals("UFSACO")) {
                    UFSACOPerform();
                } else if (cb_unsupervised.getSelectedItem().equals("RRFSACO_1")) {
                    RRFSACO_1Perform();
                } else if (cb_unsupervised.getSelectedItem().equals("RRFSACO_2")) {
                    RRFSACO_2Perform();
                } else if (cb_unsupervised.getSelectedItem().equals("IRRFSACO_1")) {
                    IRRFSACO_1Perform();
                } else if (cb_unsupervised.getSelectedItem().equals("IRRFSACO_2")) {
                    IRRFSACO_2Perform();
                } else if (cb_unsupervised.getSelectedItem().equals("MGSACO")) {
                    MGSACOPerform();
                } else if (cb_wrapper.getSelectedItem().equals("GeneticAlgorithm")) {
                    try {
                        geneticAlgorithmPerform();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }

    }

/*
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainPanel ui = new MainPanel("C:\\Users\\ST\\Desktop");
                ui.createAndShow();
            }
        });
    }
*/
}
