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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * This java class is used to create and show a panel for the parameter settings
 * of the Genetic Algorithm
 * method.
 *
 * @author Sina Shaloudegi
 * @see KFST.featureSelection.wrapper.GeneticAlgorithm.GeneticAlgorithmMain
 */
public class GeneticPanel extends JDialog
        implements ActionListener, KeyListener {

    JLabel lbl_title, lbl_about,
            lbl_pCrossover, lbl_pMutation,
            lbl_numGeneration, lbl_numPopulation, lbl_numPopulationError, lbl_numGenerationError, lbl_pCrossoverError, lbl_pMutationError;
    JTextField txt_pCrossover,
            txt_pMutation,
            txt_numGeneration, txt_numPopulation;
    JButton btn_ok, btn_more;
    JPanel panel_about;
    private double pCrossover = 0.6, pMutation = 0.033;
    private double defPCrossover = 0.6, defPMutation = 0.033;
    private int numGeneration = 20, numPopulation = 20;
    private int defNumGeneration = 20, defNumPopulation = 20;

    /**
     * Creates new form {@link GeneticPanel}. This method is called from within the
     * constructor to initialize the form.
     */
    public GeneticPanel() {
        super();
        lbl_title = new JLabel("Gentic Algorithm settings:");
        lbl_title.setBounds(10, 10, 540, 20);

        panel_about = new JPanel();
        panel_about.setBounds(10, 40, 545, 80);
        panel_about.setLayout(null);
        panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
        lbl_about = new JLabel("<html> Standard Genetic Algorithm Mitchel 1998 </html>");
        lbl_about.setBounds(15, 10, 530, 76);

        panel_about.add(lbl_about);


        lbl_numPopulation = new JLabel("Population Size:");
        lbl_numPopulation.setBounds(50, 135, 170, 22);
        txt_numPopulation = new JTextField(String.valueOf(defNumPopulation));
        txt_numPopulation.setBounds(170, 135, 120, 21);
        txt_numPopulation.addKeyListener(this);
        lbl_numPopulationError = new JLabel("");
        lbl_numPopulationError.setBounds(300, 135, 50, 22);
        lbl_numPopulationError.setForeground(Color.red);

        lbl_numGeneration = new JLabel("Number of Generations:");
        lbl_numGeneration.setBounds(50, 170, 170, 22);
        txt_numGeneration = new JTextField(String.valueOf(defNumGeneration));
        txt_numGeneration.setBounds(170, 170, 120, 21);
        txt_numGeneration.addKeyListener(this);
        lbl_numGenerationError = new JLabel("");
        lbl_numGenerationError.setBounds(300, 170, 50, 22);
        lbl_numGenerationError.setForeground(Color.red);

        lbl_pCrossover = new JLabel("Crossover Probability:");
        lbl_pCrossover.setBounds(50, 205, 170, 22);
        txt_pCrossover = new JTextField(String.valueOf(defPCrossover));
        txt_pCrossover.setBounds(170, 205, 120, 21);
        txt_pCrossover.addKeyListener(this);
        lbl_pCrossoverError = new JLabel("");
        lbl_pCrossoverError.setBounds(300, 205, 50, 22);
        lbl_pCrossoverError.setForeground(Color.red);

        lbl_pMutation = new JLabel("Mutation Probability:");
        lbl_pMutation.setBounds(50, 240, 170, 22);
        txt_pMutation = new JTextField(String.valueOf(defPMutation));
        txt_pMutation.setBounds(170, 240, 120, 21);
        txt_pMutation.addKeyListener(this);
        lbl_pMutationError = new JLabel("");
        lbl_pMutationError.setBounds(300, 240, 50, 22);
        lbl_pMutationError.setForeground(Color.red);

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

        add(lbl_numPopulation);
        add(txt_numPopulation);
        add(lbl_numPopulationError);

        add(lbl_numGeneration);
        add(txt_numGeneration);
        add(lbl_numGenerationError);

        add(lbl_pCrossover);
        add(txt_pCrossover);
        add(lbl_pCrossoverError);

        add(lbl_pMutation);
        add(txt_pMutation);
        add(lbl_pMutationError);


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
        if (e.getSource().equals(txt_numPopulation)) {
            txt_numPopulationKeyReleased(e);
        } else if (e.getSource().equals(txt_numGeneration)) {
            txt_numGenerationKeyReleased(e);
        } else if (e.getSource().equals(txt_pCrossover)) {
            txt_pCrossoverKeyReleased(e);
        } else if (e.getSource().equals(txt_pMutation)) {
            txt_pMutationKeyReleased(e);
        }
    }

    /**
     * This method sets an action for the txt_initPheromone text field.
     *
     * @param e an action event
     */
    private void txt_numPopulationKeyReleased(KeyEvent e) {
        String s = txt_numPopulation.getText();
        if ((s.equals("")) || !isCorrect(s)) {
            lbl_numPopulationError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_numPopulationError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_numPopulationError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_numIteration text field.
     *
     * @param e an action event
     */
    private void txt_numGenerationKeyReleased(KeyEvent e) {
        String s = txt_numGeneration.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            lbl_numGenerationError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            btn_ok.setEnabled(true);
            lbl_numGenerationError.setText("");
        } else {
            lbl_numGenerationError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_numAnts text field.
     *
     * @param e an action event
     */
    private void txt_pCrossoverKeyReleased(KeyEvent e) {
        String s = txt_pCrossover.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            lbl_pCrossoverError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_pCrossoverError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_pCrossoverError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_evRate text field.
     *
     * @param e an action event
     */
    private void txt_pMutationKeyReleased(KeyEvent e) {
        String s = txt_pMutation.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            lbl_pMutationError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_pMutationError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_pMutationError.setText("");
        }
    }

    public double getpCrossover() {
        return pCrossover;
    }

    public void setpCrossover(double pCrossover) {
        this.pCrossover = pCrossover;
    }

    public double getpMutation() {
        return pMutation;
    }

    public void setpMutation(double pMutation) {
        this.pMutation = pMutation;
    }

    public int getNumGeneration() {
        return numGeneration;
    }

    public void setNumGeneration(int numGeneration) {
        this.numGeneration = numGeneration;
    }

    public int getNumPopulation() {
        return numPopulation;
    }

    public void setNumPopulation(int numPopulation) {
        this.numPopulation = numPopulation;
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        setNumPopulation(Integer.parseInt(txt_numPopulation.getText()));
        setNumGeneration(Integer.parseInt(txt_numGeneration.getText()));
        setpCrossover(Double.parseDouble(txt_pCrossover.getText()));
        setpMutation(Double.parseDouble(txt_pMutation.getText()));

        dispose();
    }

    /**
     * This method sets an action for the btn_more button.
     *
     * @param e an action event
     */
    private void btn_moreActionPerformed(ActionEvent e) {
        String str = "Option\n\n";
        str += "Population Size-> the initial population size.\n\n";
        str += "Number of Generations -> Number of Generations.\n\n";
        str += "Crossover probability-> The probability of crossover in the range of( 0 and 1).\n\n";
        str += "Mutation probability ->  The probability of mutation in the range of ( 0 and 1).\n\n";

        MoreOpPanel morePanel = new MoreOpPanel(str);
        morePanel.setVisible(true);
    }

    /**
     * This method checks the status of the text field due to correct
     * input value
     *
     * @param s the input string
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
        String s = txt_numPopulation.getText();
        if ((s.equals("")) || (!isCorrect(s))) {
            return false;
        }
        s = txt_numGeneration.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            return false;
        }
        s = txt_pCrossover.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            return false;
        }
        s = txt_pMutation.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            return false;
        }

        return true;
    }

    /**
     * sets the default values of the Genetic Algorithm parameters
     */
    public void setDefaultValue() {
        txt_numPopulation.setText(String.valueOf(defNumPopulation));
        txt_numGeneration.setText(String.valueOf(defNumGeneration));
        txt_pCrossover.setText(String.valueOf(defPCrossover));
        txt_pMutation.setText(String.valueOf(defPMutation));


        numPopulation = defNumPopulation;
        numGeneration = defNumGeneration;
        pCrossover = defPCrossover;
        pMutation = defPMutation;

    }

    /**
     * sets the last values of the Genetic Algorithm parameters entered by user
     *
     * @param numPopulation the initial Population size
     * @param numGeneration the  number of generations
     * @param pMutation     mutation probability
     * @param pCrossover    crossover probability
     */
    public void setUserValue(int numPopulation, int numGeneration, double pCrossover, double pMutation) {
        numPopulation = numPopulation;
        numGeneration = numGeneration;
        pCrossover = pCrossover;
        pMutation = pMutation;


        txt_numPopulation.setText(String.valueOf(numPopulation));
        txt_numGeneration.setText(String.valueOf(numGeneration));
        txt_pCrossover.setText(String.valueOf(pCrossover));
        txt_pMutation.setText(String.valueOf(pMutation));

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
