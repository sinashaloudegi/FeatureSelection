package KFST.gui.featureSelection;

import KFST.gui.MoreOpPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by sina on 8/7/2017.
 */
public class ABCPanel extends JDialog
        implements ActionListener, KeyListener {

    JLabel lbl_title, lbl_about,
            lbl_MR,
            lbl_numIteration, lbl_maxLimit, lbl_maxLimitError, lbl_numIterationError, lbl_MRError;
    JTextField txt_MR,

    txt_numIteration, txt_maxLimit;
    JButton btn_ok, btn_more;
    JPanel panel_about;
    private double MR = 0.1;
    private double defMR = 0.1;
    private int numIteration = 100, maxLimit = 3;
    private int defIteration = 100, defMaxLimit = 3;

    /**
     * Creates new form {@link ABCPanel}. This method is called from within the
     * constructor to initialize the form.
     */
    public ABCPanel() {
        super();
        lbl_title = new JLabel("ABC settings:");
        lbl_title.setBounds(10, 10, 540, 20);

        panel_about = new JPanel();
        panel_about.setBounds(10, 40, 545, 80);
        panel_about.setLayout(null);
        panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
        lbl_about = new JLabel("<html>Artificial Bee Colony Optimization</html>");
        lbl_about.setBounds(15, 10, 530, 76);

        panel_about.add(lbl_about);


        lbl_maxLimit = new JLabel("MAX_LIMIT:");
        lbl_maxLimit.setBounds(50, 135, 170, 22);
        txt_maxLimit = new JTextField(String.valueOf(defMaxLimit));
        txt_maxLimit.setBounds(170, 135, 120, 21);
        txt_maxLimit.addKeyListener(this);
        lbl_maxLimitError = new JLabel("");
        lbl_maxLimitError.setBounds(300, 135, 50, 22);
        lbl_maxLimitError.setForeground(Color.red);

        lbl_numIteration = new JLabel("Number of Iterations:");
        lbl_numIteration.setBounds(50, 170, 170, 22);
        txt_numIteration = new JTextField(String.valueOf(defIteration));
        txt_numIteration.setBounds(170, 170, 120, 21);
        txt_numIteration.addKeyListener(this);
        lbl_numIterationError = new JLabel("");
        lbl_numIterationError.setBounds(300, 170, 50, 22);
        lbl_numIterationError.setForeground(Color.red);

        lbl_MR = new JLabel("MR:");
        lbl_MR.setBounds(50, 205, 170, 22);
        txt_MR = new JTextField(String.valueOf(defMR));
        txt_MR.setBounds(170, 205, 120, 21);
        txt_MR.addKeyListener(this);
        lbl_MRError = new JLabel("");
        lbl_MRError.setBounds(300, 205, 50, 22);
        lbl_MRError.setForeground(Color.red);


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

        add(lbl_maxLimit);
        add(txt_maxLimit);
        add(lbl_maxLimitError);

        add(lbl_numIteration);
        add(txt_numIteration);
        add(lbl_numIterationError);

        add(lbl_MR);
        add(txt_MR);
        add(lbl_MRError);


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
        if (e.getSource().equals(txt_maxLimit)) {
            txt_maxLimitKeyReleased(e);
        } else if (e.getSource().equals(txt_numIteration)) {
            txt_numIterationKeyReleased(e);
        } else if (e.getSource().equals(txt_MR)) {
            txt_MRKeyReleased(e);
        }
    }

    /**
     * This method sets an action for the txt_initPheromone text field.
     *
     * @param e an action event
     */
    private void txt_maxLimitKeyReleased(KeyEvent e) {
        String s = txt_maxLimit.getText();
        if ((s.equals("")) || !isCorrect(s)) {
            lbl_maxLimitError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_maxLimitError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_maxLimitError.setText("");
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
    private void txt_MRKeyReleased(KeyEvent e) {
        String s = txt_MR.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            lbl_MRError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_MRError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_MRError.setText("");
        }
    }


    public double getMR() {
        return MR;
    }

    public void setMR(double MR) {
        this.MR = MR;
    }


    public int getNumIteration() {
        return numIteration;
    }

    public void setNumIteration(int numIteration) {
        this.numIteration = numIteration;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        setMaxLimit(Integer.parseInt(txt_maxLimit.getText()));
        setNumIteration(Integer.parseInt(txt_numIteration.getText()));
        setMR(Double.parseDouble(txt_MR.getText()));

        dispose();
    }

    /**
     * This method sets an action for the btn_more button.
     *
     * @param e an action event
     */
    private void btn_moreActionPerformed(ActionEvent e) {
        String str = "Option\n\n";
        str += "MAX_LIMIT -> Maximum Limit.\n\n";
        str += "Number of Iterations -> Number of Iterations.\n\n";
        str += "MR ->Modification Rate.\n\n";

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
        String s = txt_maxLimit.getText();
        if ((s.equals("")) || (!isCorrect(s))) {
            return false;
        }
        s = txt_numIteration.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            return false;
        }
        s = txt_MR.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            return false;
        }


        return true;
    }

    /**
     * sets the default values of the ABC parameters
     */
    public void setDefaultValue() {
        txt_maxLimit.setText(String.valueOf(defMaxLimit));
        txt_numIteration.setText(String.valueOf(defIteration));
        txt_MR.setText(String.valueOf(defMR));


        maxLimit = defMaxLimit;
        numIteration = defIteration;
        MR = defMR;

    }

    /**
     * sets the last values of the ABC Algorithm parameters entered by user
     *
     * @param maxLimit     MAXIMUM_LIMIT
     * @param numIteration the  number of Iterations
     * @param MR           Modification Rate
     */
    public void setUserValue(int maxLimit, int numIteration, double MR) {
        maxLimit = maxLimit;
        numIteration = numIteration;
        MR = MR;


        txt_maxLimit.setText(String.valueOf(maxLimit));
        txt_numIteration.setText(String.valueOf(numIteration));
        txt_MR.setText(String.valueOf(MR));

    }

}
