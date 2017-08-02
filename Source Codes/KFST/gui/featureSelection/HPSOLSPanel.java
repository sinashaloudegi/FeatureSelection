package KFST.gui.featureSelection;

import KFST.gui.MoreOpPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by sina on 7/13/2017.
 */
public class HPSOLSPanel extends JDialog
        implements ActionListener, KeyListener {
        JLabel lbl_title, lbl_about,
                lbl_numIterates, lbl_numSwarmPopulation, lbl_numSwarmPopulationError, getLbl_numIteratesError;
        JTextField txt_numIterates,
                txt_numSwarmPopulation;
        JButton btn_ok, btn_more;
        JPanel panel_about;

        private int numIterates = 20, numSwarmPopulation = 20;
        private int defnumIterates = 20, defnumSwarmPopulation = 20;

        /**
         * Creates new form {@link HPSOLSPanel}. This method is called from within the
         * constructor to initialize the form.
         */
        public HPSOLSPanel() {
            super();
            lbl_title = new JLabel("HPSOLS settings:");
            lbl_title.setBounds(10, 10, 540, 20);

            panel_about = new JPanel();
            panel_about.setBounds(10, 40, 545, 80);
            panel_about.setLayout(null);
            panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
            lbl_about = new JLabel("<html> HPSOLS </html>");
            lbl_about.setBounds(15, 10, 530, 76);

            panel_about.add(lbl_about);


            lbl_numSwarmPopulation = new JLabel("PSO4_2Swarm Size:");
            lbl_numSwarmPopulation.setBounds(50, 135, 170, 22);
            txt_numSwarmPopulation = new JTextField(String.valueOf(defnumSwarmPopulation));
            txt_numSwarmPopulation.setBounds(170, 135, 120, 21);
            txt_numSwarmPopulation.addKeyListener(this);
            lbl_numSwarmPopulationError = new JLabel("");
            lbl_numSwarmPopulationError.setBounds(300, 135, 50, 22);
            lbl_numSwarmPopulationError.setForeground(Color.red);

            lbl_numIterates= new JLabel("Number of Iteration:");
            lbl_numIterates.setBounds(50, 170, 170, 22);
            txt_numIterates= new JTextField(String.valueOf(defnumIterates));
            txt_numIterates.setBounds(170, 170, 120, 21);
            txt_numIterates.addKeyListener(this);
            getLbl_numIteratesError = new JLabel("");
            getLbl_numIteratesError.setBounds(300, 170, 50, 22);
            getLbl_numIteratesError.setForeground(Color.red);


            btn_ok = new JButton("Ok");
            btn_ok.setBounds(190, 360, 75, 23);
            btn_ok.addActionListener(this);

            btn_more = new JButton("More");
            btn_more.setBounds(310, 360, 75, 23);
            btn_more.addActionListener(this);

            setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            setSize(570, 450);
            setLayout(null);
            add(lbl_title);
            add(panel_about);

            add(lbl_numSwarmPopulation);
            add(txt_numSwarmPopulation);
            add(lbl_numSwarmPopulationError);

            add(lbl_numIterates);
            add(txt_numIterates);
            add(getLbl_numIteratesError);


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
            if (e.getSource().equals(txt_numSwarmPopulation)) {
                txt_numSwarmPopulationKeyReleased(e);
            } else if (e.getSource().equals(txt_numIterates)) {
                txt_numGenerationKeyReleased(e);
            }
        }

        /**
         * This method sets an action for the txt_numSwarmPopulation text field.
         *
         * @param e an action event
         */
        private void txt_numSwarmPopulationKeyReleased(KeyEvent e) {
            String s = txt_numSwarmPopulation.getText();
            if ((s.equals("")) || !isCorrect(s)) {
                lbl_numSwarmPopulationError.setText("*");
                btn_ok.setEnabled(false);
            } else if (checkVisibleButton()) {
                lbl_numSwarmPopulationError.setText("");
                btn_ok.setEnabled(true);
            } else {
                lbl_numSwarmPopulationError.setText("");
            }
        }

        /**
         * This method sets an action for the txt_numIterates text field.
         *
         * @param e an action event
         */
        private void txt_numGenerationKeyReleased(KeyEvent e) {
            String s = txt_numIterates.getText();
            if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
                getLbl_numIteratesError.setText("*");
                btn_ok.setEnabled(false);
            } else if (checkVisibleButton()) {
                btn_ok.setEnabled(true);
                getLbl_numIteratesError.setText("");
            } else {
                getLbl_numIteratesError.setText("");
            }
        }


        public int getNumIterates() {
            return numIterates;
        }

        public void setNumIterates(int numIterates) {
            this.numIterates = numIterates;
        }

        public int getNumSwarmPopulation() {
            return numSwarmPopulation;
        }

        public void setNumSwarmPopulation(int numSwarmPopulation) {
            this.numSwarmPopulation = numSwarmPopulation;
        }

        /**
         * This method sets an action for the btn_ok button.
         *
         * @param e an action event
         */
        private void btn_okActionPerformed(ActionEvent e) {
            setNumSwarmPopulation(Integer.parseInt(txt_numSwarmPopulation.getText()));
            setNumIterates(Integer.parseInt(txt_numIterates.getText()));

            dispose();
        }

        /**
         * This method sets an action for the btn_more button.
         *
         * @param e an action event
         */
        private void btn_moreActionPerformed(ActionEvent e) {
            String str = "Option\n\n";
            str += "Population Size-> the initial swarm size.\n\n";
            str += "Number of Iteration -> Number of Generations.\n\n";


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
            String s = txt_numSwarmPopulation.getText();
            if ((s.equals("")) || (!isCorrect(s))) {
                return false;
            }
            s = txt_numIterates.getText();
            if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
                return false;
            }


            return true;
        }

        /**
         * sets the default values of the  parameters
         */
        public void setDefaultValue() {
            txt_numSwarmPopulation.setText(String.valueOf(defnumSwarmPopulation));
            txt_numIterates.setText(String.valueOf(defnumIterates));


            numSwarmPopulation = defnumSwarmPopulation;
            numIterates = defnumIterates;


        }

        /**
         * sets the last values of the HPSOLS parameters entered by user
         *
         * @param numSwarmPopulation the initial PSO4_2Swarm size
         * @param numIterates  the  number of iterations
         */
        public void setUserValue(int numSwarmPopulation, int numIterates) {
            numSwarmPopulation = numSwarmPopulation;
            numIterates = numIterates;


            txt_numSwarmPopulation.setText(String.valueOf(numSwarmPopulation));
            txt_numIterates.setText(String.valueOf(numIterates));

        }
}
