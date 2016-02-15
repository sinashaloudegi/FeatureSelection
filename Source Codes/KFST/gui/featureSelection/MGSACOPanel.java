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
package KFST.gui.featureSelection;

import KFST.gui.MoreOpPanel;
import java.awt.Color;
//import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show a panel for the parameter settings
 * of the microarray gene selection based on ant colony optimization (MGSACO)
 * method.
 *
 * @see KFST.featureSelection.filter.unsupervised.MGSACO
 *
 * @author Shahin Salavati
 */
public class MGSACOPanel extends JDialog
        implements ActionListener, KeyListener {

    JLabel lbl_title, lbl_about,
            lbl_initPheromone, lbl_initPheromoneError,
            lbl_numIteration, lbl_numIterationError,
            lbl_numAnts, lbl_numAntsError,
            lbl_evRate, lbl_evRateError,
            lbl_beta, lbl_betaError,
            lbl_q0, lbl_q0Error;
    JTextField txt_initPheromone,
            txt_numIteration,
            txt_numAnts, txt_evRate,
            txt_beta, txt_q0;
    JButton btn_ok, btn_more;
    JPanel panel_about;
    private double initPheromone = 0.2, evRate = 0.2, beta = 1, q0 = 0.7;
    private double defInitPheromone = 0.2, defEvRate = 0.2, defBeta = 1, defQ0 = 0.7;
    private int numIteration = 50, numAnts = 0;
    private int defNumIteration = 50, defNumAnts = 0;

    /**
     * Creates new form MGSACOPanel. This method is called from within the
     * constructor to initialize the form.
     */
    public MGSACOPanel() {
        super();
        lbl_title = new JLabel("MGSACO settings:");
        lbl_title.setBounds(10, 10, 540, 20);

        panel_about = new JPanel();
        panel_about.setBounds(10, 40, 545, 80);
        panel_about.setLayout(null);
        panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
        lbl_about = new JLabel("<html> Microarray gene selection based on ant colony optimization (MGSACO) is a framework to combine the computational efficiency of the filter approach and the good performance of the ACO algorithm. MGSACO is unsupervised filter based gene selection method. </html>");
        lbl_about.setBounds(15, 10, 530, 76);

        panel_about.add(lbl_about);


        lbl_initPheromone = new JLabel("Initial pheromone:");
        lbl_initPheromone.setBounds(50, 135, 170, 22);
        txt_initPheromone = new JTextField(String.valueOf(defInitPheromone));
        txt_initPheromone.setBounds(170, 135, 120, 21);
        txt_initPheromone.addKeyListener(this);
        lbl_initPheromoneError = new JLabel("");
        lbl_initPheromoneError.setBounds(300, 135, 50, 22);
        lbl_initPheromoneError.setForeground(Color.red);

        lbl_numIteration = new JLabel("Number of iterations:");
        lbl_numIteration.setBounds(50, 170, 170, 22);
        txt_numIteration = new JTextField(String.valueOf(defNumIteration));
        txt_numIteration.setBounds(170, 170, 120, 21);
        txt_numIteration.addKeyListener(this);
        lbl_numIterationError = new JLabel("");
        lbl_numIterationError.setBounds(300, 170, 50, 22);
        lbl_numIterationError.setForeground(Color.red);

        lbl_numAnts = new JLabel("Number of ants:");
        lbl_numAnts.setBounds(50, 205, 170, 22);
        txt_numAnts = new JTextField(String.valueOf(defNumAnts));
        txt_numAnts.setBounds(170, 205, 120, 21);
        txt_numAnts.addKeyListener(this);
        lbl_numAntsError = new JLabel("");
        lbl_numAntsError.setBounds(300, 205, 50, 22);
        lbl_numAntsError.setForeground(Color.red);

        lbl_evRate = new JLabel("Evaporation rate:");
        lbl_evRate.setBounds(50, 240, 170, 22);
        txt_evRate = new JTextField(String.valueOf(defEvRate));
        txt_evRate.setBounds(170, 240, 120, 21);
        txt_evRate.addKeyListener(this);
        lbl_evRateError = new JLabel("");
        lbl_evRateError.setBounds(300, 240, 50, 22);
        lbl_evRateError.setForeground(Color.red);

        lbl_beta = new JLabel("Parameter beta:");
        lbl_beta.setBounds(50, 275, 170, 22);
        txt_beta = new JTextField(String.valueOf(defBeta));
        txt_beta.setBounds(170, 275, 120, 21);
        txt_beta.addKeyListener(this);
        lbl_betaError = new JLabel("");
        lbl_betaError.setBounds(300, 275, 50, 22);
        lbl_betaError.setForeground(Color.red);

        lbl_q0 = new JLabel("Parameter q0:");
        lbl_q0.setBounds(50, 310, 170, 22);
        txt_q0 = new JTextField(String.valueOf(defQ0));
        txt_q0.setBounds(170, 310, 120, 21);
        txt_q0.addKeyListener(this);
        lbl_q0Error = new JLabel("");
        lbl_q0Error.setBounds(300, 310, 50, 22);
        lbl_q0Error.setForeground(Color.red);


        btn_ok = new JButton("Ok");
        btn_ok.setBounds(190, 360, 75, 23);
        btn_ok.addActionListener(this);

        btn_more = new JButton("More");
        btn_more.setBounds(310, 360, 75, 23);
        btn_more.addActionListener(this);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(570, 450);
        setLayout(null);
        add(lbl_title);
        add(panel_about);

        add(lbl_initPheromone);
        add(txt_initPheromone);
        add(lbl_initPheromoneError);

        add(lbl_numIteration);
        add(txt_numIteration);
        add(lbl_numIterationError);

        add(lbl_numAnts);
        add(txt_numAnts);
        add(lbl_numAntsError);

        add(lbl_evRate);
        add(txt_evRate);
        add(lbl_evRateError);

        add(lbl_beta);
        add(txt_beta);
        add(lbl_betaError);

        add(lbl_q0);
        add(txt_q0);
        add(lbl_q0Error);

        add(btn_ok);
        add(btn_more);

        setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Parameter Settings Panel");
    }

    /**
     * The listener method for receiving action events.
     * Invoked when an action occurs.
     *
     * @param e an action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btn_ok)) {
            btn_okActionPerformed(e);
        } else if (e.getSource().equals(btn_more)) {
            btn_moreActionPerformed(e);
        }
    }

    /**
     * The listener method for receiving keyboard events (keystrokes).
     * Invoked when a key has been typed.
     *
     * @param e an action event
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * The listener method for receiving keyboard events (keystrokes).
     * Invoked when a key has been pressed.
     *
     * @param e an action event
     */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
     * The listener method for receiving keyboard events (keystrokes).
     * Invoked when a key has been released.
     *
     * @param e an action event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(txt_initPheromone)) {
            txt_initPheromoneKeyReleased(e);
        } else if (e.getSource().equals(txt_numIteration)) {
            txt_numIterationKeyReleased(e);
        } else if (e.getSource().equals(txt_numAnts)) {
            txt_numAntsKeyReleased(e);
        } else if (e.getSource().equals(txt_evRate)) {
            txt_evRateKeyReleased(e);
        } else if (e.getSource().equals(txt_beta)) {
            txt_betaKeyReleased(e);
        } else if (e.getSource().equals(txt_q0)) {
            txt_q0KeyReleased(e);
        }
    }

    /**
     * This method sets an action for the txt_initPheromone text field.
     *
     * @param e an action event
     */
    private void txt_initPheromoneKeyReleased(KeyEvent e) {
        String s = txt_initPheromone.getText();
        if ((s.equals("")) || !isCorrect(s)) {
            lbl_initPheromoneError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_initPheromoneError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_initPheromoneError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_numIteration text field.
     *
     * @param e an action event
     */
    private void txt_numIterationKeyReleased(KeyEvent e) {
        String s = txt_numIteration.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            lbl_numIterationError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            btn_ok.setEnabled(true);
            lbl_numIterationError.setText("");
        } else {
            lbl_numIterationError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_numAnts text field.
     *
     * @param e an action event
     */
    private void txt_numAntsKeyReleased(KeyEvent e) {
        String s = txt_numAnts.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || (Integer.parseInt(s) < 1 && Integer.parseInt(s) != 0)) {
            lbl_numAntsError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_numAntsError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_numAntsError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_evRate text field.
     *
     * @param e an action event
     */
    private void txt_evRateKeyReleased(KeyEvent e) {
        String s = txt_evRate.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) <= 0 || Double.parseDouble(s) >= 1) {
            lbl_evRateError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_evRateError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_evRateError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_beta text field.
     *
     * @param e an action event
     */
    private void txt_betaKeyReleased(KeyEvent e) {
        String s = txt_beta.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) <= 0) {
            lbl_betaError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_betaError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_betaError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_q0 text field.
     *
     * @param e an action event
     */
    private void txt_q0KeyReleased(KeyEvent e) {
        String s = txt_q0.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            lbl_q0Error.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_q0Error.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_q0Error.setText("");
        }
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        setInitPheromone(Double.parseDouble(txt_initPheromone.getText()));
        setNumIteration(Integer.parseInt(txt_numIteration.getText()));
        setNumAnts(Integer.parseInt(txt_numAnts.getText()));
        setEvRate(Double.parseDouble(txt_evRate.getText()));
        setBeta(Double.parseDouble(txt_beta.getText()));
        setQ0(Double.parseDouble(txt_q0.getText()));
        dispose();
    }

    /**
     * This method sets an action for the btn_more button.
     *
     * @param e an action event
     */
    private void btn_moreActionPerformed(ActionEvent e) {
        String str = "Option\n\n";
        str += "Initial pheromone -> the initial value of the pheromone.\n\n";
        str += "Number of iterations -> the maximum number of allowed iterations that algorithm repeated.\n\n";
        str += "Number of ants -> define the number of ants which it's depend to the number of original features (0 means the number of ants for each data set is set to the number of its original features. But,for the data sets with more than 100 features this parameter is set to 100).\n\n";
        str += "Evaporation rate -> the evaporation rate of the pheromone (a real number in the range of (0, 1)).\n\n";
        str += "Parameter beta -> the beta parameter in the state transition rule (a real number greater than zero (i.e., beta>0)).\n\n";
        str += "Parameter q0 -> the q0 parameter in the state transition rule (a real number in the range of [0, 1]).";
        MoreOpPanel morePanel = new MoreOpPanel(str);
        morePanel.setVisible(true);
    }

    /**
     * This method checks the status of the text field due to correct
     * input value
     *
     * @param s the input string
     *
     * @return true if the input string is in the correct format
     */
    private boolean isCorrect(String s) {
        int countDot = 0;
        for (int locate = 0; locate < s.length(); locate++) {
            if (s.charAt(locate) == '.') {
                countDot++;
            } else if (!Character.isDigit(s.charAt(locate))) {
                return false;
            }
        }
        if (countDot > 1) {
            return false;
        }
        return true;
    }

    /**
     * This method checks the input string due to correct integer value
     *
     * @param s the input string
     *
     * @return true if the input string is in the correct format
     */
    private boolean isInteger(String s) {
        for (int locate = 0; locate < s.length(); locate++) {
            if (s.charAt(locate) == '.') {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks the text field values due to correct format
     *
     * @return true if all text field values are in the correct format
     */
    private boolean checkVisibleButton() {
        String s = txt_initPheromone.getText();
        if ((s.equals("")) || (!isCorrect(s))) {
            return false;
        }
        s = txt_numIteration.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            return false;
        }
        s = txt_numAnts.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || (Integer.parseInt(s) < 1 && Integer.parseInt(s) != 0)) {
            return false;
        }
        s = txt_evRate.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) <= 0 || Double.parseDouble(s) >= 1) {
            return false;
        }
        s = txt_beta.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) <= 0) {
            return false;
        }
        s = txt_q0.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            return false;
        }
        return true;
    }

    /**
     * This method returns the initial pheromone value.
     *
     * @return the <code>initPheromone</code> parameter
     */
    public double getInitPheromone() {
        return initPheromone;
    }

    /**
     * This method sets the initial pheromone value.
     *
     * @param initPheromone the initial pheromone value
     */
    public void setInitPheromone(double initPheromone) {
        this.initPheromone = initPheromone;
    }

    /**
     * This method returns the number of iterations value.
     *
     * @return the <code>numIteration</code> parameter
     */
    public int getNumIteration() {
        return numIteration;
    }

    /**
     * This method sets the number of iterations value.
     *
     * @param numIteration the number of iterations value
     */
    public void setNumIteration(int numIteration) {
        this.numIteration = numIteration;
    }

    /**
     * This method returns the number of ants value.
     *
     * @return the <code>numAnts</code> parameter
     */
    public int getNumAnts() {
        return numAnts;
    }

    /**
     * This method sets the number of ants value.
     *
     * @param numAnts the number of ants value
     */
    public void setNumAnts(int numAnts) {
        this.numAnts = numAnts;
    }

    /**
     * This method returns the evaporation rate.
     *
     * @return the <code>evaporation rate</code> parameter
     */
    public double getEvRate() {
        return evRate;
    }

    /**
     * This method sets the evaporation rate value.
     *
     * @param evRate the evaporation rate value
     */
    public void setEvRate(double evRate) {
        this.evRate = evRate;
    }

    /**
     * This method returns the beta.
     *
     * @return the <code>beta</code> parameter
     */
    public double getBeta() {
        return beta;
    }

    /**
     * This method sets the beta value.
     *
     * @param beta the beta value
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }

    /**
     * This method returns the q0.
     *
     * @return the <code>q0</code> parameter
     */
    public double getQ0() {
        return q0;
    }

    /**
     * This method sets the q0 value.
     *
     * @param q0 the q0 value
     */
    public void setQ0(double q0) {
        this.q0 = q0;
    }

    /**
     * sets the default values of the MGSACO parameters
     */
    public void setDefaultValue() {
        txt_initPheromone.setText(String.valueOf(defInitPheromone));
        txt_numIteration.setText(String.valueOf(defNumIteration));
        txt_numAnts.setText(String.valueOf(defNumAnts));
        txt_evRate.setText(String.valueOf(defEvRate));
        txt_beta.setText(String.valueOf(defBeta));
        txt_q0.setText(String.valueOf(defQ0));

        initPheromone = defInitPheromone;
        numIteration = defNumIteration;
        numAnts = defNumAnts;
        evRate = defEvRate;
        beta = defBeta;
        q0 = defQ0;
    }

    /**
     * sets the last values of the MGSACO parameters entered by user
     *
     * @param initPheromoneValue the initial value of the pheromone
     * @param numIterations the maximum number of iteration
     * @param numberAnt the number of ants
     * @param evaporationRate the evaporation rate of the pheromone
     * @param betaParameter the beta parameter in the state transition rule
     * @param q0_Parameter the q0 parameter in the state transition rule
     */
    public void setUserValue(double initPheromoneValue, int numIterations, int numberAnt, double evaporationRate, double betaParameter, double q0_Parameter) {
        initPheromone = initPheromoneValue;
        numIteration = numIterations;
        numAnts = numberAnt;
        evRate = evaporationRate;
        beta = betaParameter;
        q0 = q0_Parameter;

        txt_initPheromone.setText(String.valueOf(initPheromone));
        txt_numIteration.setText(String.valueOf(numIteration));
        txt_numAnts.setText(String.valueOf(numAnts));
        txt_evRate.setText(String.valueOf(evRate));
        txt_beta.setText(String.valueOf(beta));
        txt_q0.setText(String.valueOf(q0));
    }

//    public static void main(String[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        MGSACOPanel dtpanel = new MGSACOPanel();
//        Dialog dlg = new Dialog(dtpanel);
//        dtpanel.setVisible(true);
//        System.out.println("Init pheromone = " + dtpanel.getInitPheromone());
//        System.out.println("num iteration = " + dtpanel.getNumIteration());
//        System.out.println("num ants = " + dtpanel.getNumAnts());
//        System.out.println("evaporation rate = " + dtpanel.getEvRate());
//        System.out.println("beta = " + dtpanel.getBeta());
//        System.out.println("parameter q0 = " + dtpanel.getQ0());
//    }
}
