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
package KFST.gui.classifier;

import KFST.gui.MoreOpPanel;
import java.awt.Color;
//import java.awt.Dialog;
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
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show a panel for the parameter settings
 * of the decision tree classifier.
 *
 * @author Shahin Salavati
 */
public class DTClassifierPanel extends JDialog
        implements ActionListener, KeyListener {

    JLabel lbl_title, lbl_confidenc, lbl_minNum, lbl_about, lbl_confidencError, lbl_minNumError;
    JTextField txt_confidence, txt_minNum;
    JButton btn_ok, btn_more;
    JPanel panel_about;
    private double confidence = 0.25, defConfidence = 0.25;
    private int minNum = 2, defMinNum = 2;

    /**
     * Creates new form DTClassifierPanel. This method is called from within the
     * constructor to initialize the form.
     */
    public DTClassifierPanel() {
        super();
        lbl_title = new JLabel("Decision tree settings:");
        lbl_title.setBounds(10, 10, 140, 20);

        panel_about = new JPanel();
        panel_about.setBounds(10, 50, 400, 60);
        panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
        lbl_about = new JLabel("The C4.5 decision tree with the post-pruning algorithm in the pruning phase.");

        panel_about.add(lbl_about);

        lbl_confidenc = new JLabel("Confidence factor:");
        lbl_confidenc.setBounds(30, 135, 120, 22);

        txt_confidence = new JTextField(String.valueOf(defConfidence));
        txt_confidence.setBounds(130, 135, 120, 21);
        txt_confidence.addKeyListener(this);
        lbl_confidencError = new JLabel("");
        lbl_confidencError.setBounds(260, 135, 50, 22);
        lbl_confidencError.setForeground(Color.red);

        lbl_minNum = new JLabel("MinNumSample:");
        lbl_minNum.setBounds(30, 170, 120, 22);
        txt_minNum = new JTextField(Integer.toString(defMinNum));
        txt_minNum.setBounds(130, 170, 120, 21);
        txt_minNum.addKeyListener(this);
        lbl_minNumError = new JLabel("");
        lbl_minNumError.setBounds(260, 170, 50, 22);
        lbl_minNumError.setForeground(Color.red);

        btn_ok = new JButton("Ok");
        btn_ok.setBounds(120, 220, 75, 23);
        btn_ok.addActionListener(this);

        btn_more = new JButton("More");
        btn_more.setBounds(240, 220, 75, 23);
        btn_more.addActionListener(this);


        setModalityType(ModalityType.APPLICATION_MODAL);
        setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        setSize(440, 300);
        setLayout(null);
        add(lbl_title);
        add(panel_about);
        add(lbl_confidenc);
        add(txt_confidence);
        add(lbl_minNum);
        add(txt_minNum);
        add(lbl_confidencError);
        add(lbl_minNumError);
        add(btn_ok);
        add(btn_more);

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
        if (e.getSource().equals(txt_confidence)) {
            txt_confidenceKeyReleased(e);
        } else if (e.getSource().equals(txt_minNum)) {
            txt_minNumKeyReleased(e);
        }
    }

    /**
     * This method sets an action for the txt_confidence text field.
     *
     * @param e an action event
     */
    private void txt_confidenceKeyReleased(KeyEvent e) {
        String confidenceLabel = txt_confidence.getText();
        String minSampelLabel = txt_minNum.getText();
        if (confidenceLabel.equals("") || !isCorrect(confidenceLabel)) {
            lbl_confidencError.setText("*");
            btn_ok.setEnabled(false);
        } else if (!minSampelLabel.equals("") && isCorrect(minSampelLabel) && isInteger(minSampelLabel)) {
            lbl_confidencError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_confidencError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_minNum text field.
     *
     * @param e an action event
     */
    private void txt_minNumKeyReleased(KeyEvent e) {
        String minSampelLabel = txt_minNum.getText();
        String confidenceLabel = txt_confidence.getText();
        if (minSampelLabel.equals("") || (!isCorrect(minSampelLabel)) || (!isInteger(minSampelLabel))) {
            lbl_minNumError.setText("*");
            btn_ok.setEnabled(false);
        } else if ((!confidenceLabel.equals("")) && (isCorrect(confidenceLabel))) {
            lbl_minNumError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_minNumError.setText("");
        }
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        setConfidence(Double.parseDouble(txt_confidence.getText()));
        setMinNum(Integer.parseInt(txt_minNum.getText()));
        dispose();
    }

    /**
     * This method sets an action for the btn_more button.
     *
     * @param e an action event
     */
    private void btn_moreActionPerformed(ActionEvent e) {
        String str = "Option\n\n";
        str += "Confidence factor -> the confidence factor used for pruning (smaller values incur more pruning).\n\n";
        str += "MinNumSample -> the minimum number of samples per leaf.\n\n";
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
     * This method returns the confidence factor value.
     *
     * @return the <code>Confidence factor</code> parameter
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * This method sets the confidence factor value.
     *
     * @param confidence the confidence factor value
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    /**
     * This method returns the minimum number of samples per leaf.
     *
     * @return the <code>MinNumSample</code> parameter
     */
    public int getMinNum() {
        return minNum;
    }

    /**
     * This method sets the minimum number of samples per leaf value.
     *
     * @param minNum the minimum number of samples per leaf value.
     */
    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    /**
     * sets the default values of the decision tree parameters
     */
    public void setDefaultValue() {
        txt_confidence.setText(String.valueOf(defConfidence));
        txt_minNum.setText(String.valueOf(defMinNum));
        confidence = defConfidence;
        minNum = defMinNum;
    }

    /**
     * sets the last values of the decision tree parameters entered by user
     *
     * @param conf the confidence factor
     * @param minSample the minimum number of samples per leaf
     */
    public void setUserValue(double conf, int minSample) {
        confidence = conf;
        minNum = minSample;
        txt_confidence.setText(String.valueOf(confidence));
        txt_minNum.setText(String.valueOf(minNum));
    }

//    public static void main(String[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                DTClassifierPanel dtpanel = new DTClassifierPanel();
//                Dialog dlg = new Dialog(dtpanel);
//                dtpanel.setVisible(true);
//                System.out.println("minNum = " + dtpanel.getMinNum());
//                System.out.println("confidence = " + dtpanel.getConfidence());
//            }
//        });
//    }
}
