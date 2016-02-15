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
 * of the Laplacian score method.
 *
 * @see KFST.featureSelection.filter.unsupervised.LaplacianScore
 * @see KFST.featureSelection.filter.supervised.LaplacianScore
 *
 * @author Shahin Salavati
 */
public class LaplacianScorePanel extends JDialog
        implements ActionListener, KeyListener {

    JLabel lbl_title, lbl_about,
            lbl_KNearest, lbl_KNearestError,
            lbl_constParam, lbl_constParamError;
    JTextField txt_KNearest,
            txt_constParam;
    JButton btn_ok, btn_more;
    JPanel panel_about;
    private double constParam = 100, defConstParam = 100;
    private int KNearest = 5, defKNearest = 5;

    /**
     * Creates new form LaplacianScorePanel. This method is called from within
     * the constructor to initialize the form.
     */
    public LaplacianScorePanel() {
        super();
        lbl_title = new JLabel("Laplacian score settings:");
        lbl_title.setBounds(10, 10, 540, 20);

        panel_about = new JPanel();
        panel_about.setBounds(10, 40, 545, 80);
        panel_about.setLayout(null);
        panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
        lbl_about = new JLabel("<html>Laplacian score of a feature evaluates locality preserving power of the feature. It is assumed that if the distances between two samples are as small as possible, they are related to the same subject. Laplacian score can be applied in the two supervised and unsupervised modes. </html>");
        lbl_about.setBounds(15, 10, 530, 76);

        panel_about.add(lbl_about);


        lbl_KNearest = new JLabel("k-nearest neighbor:");
        lbl_KNearest.setBounds(50, 135, 170, 22);
        txt_KNearest = new JTextField(String.valueOf(defKNearest));
        txt_KNearest.setBounds(170, 135, 120, 21);
        txt_KNearest.addKeyListener(this);
        lbl_KNearestError = new JLabel("");
        lbl_KNearestError.setBounds(300, 135, 50, 22);
        lbl_KNearestError.setForeground(Color.red);

        lbl_constParam = new JLabel("Constant parameter:");
        lbl_constParam.setBounds(50, 170, 170, 22);
        txt_constParam = new JTextField(String.valueOf(defConstParam));
        txt_constParam.setBounds(170, 170, 120, 21);
        txt_constParam.addKeyListener(this);
        lbl_constParamError = new JLabel("");
        lbl_constParamError.setBounds(300, 170, 50, 22);
        lbl_constParamError.setForeground(Color.red);

        btn_ok = new JButton("Ok");
        btn_ok.setBounds(190, 220, 75, 23);
        btn_ok.addActionListener(this);

        btn_more = new JButton("More");
        btn_more.setBounds(310, 220, 75, 23);
        btn_more.addActionListener(this);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(570, 300);
        setLayout(null);
        add(lbl_title);
        add(panel_about);

        add(lbl_KNearest);
        add(txt_KNearest);
        add(lbl_KNearestError);

        add(lbl_constParam);
        add(txt_constParam);
        add(lbl_constParamError);

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
        if (e.getSource().equals(txt_KNearest)) {
            txt_KNearestKeyReleased(e);
        } else if (e.getSource().equals(txt_constParam)) {
            txt_constParamKeyReleased(e);
        }
    }

    /**
     * This method sets an action for the txt_KNearest text field.
     *
     * @param e an action event
     */
    private void txt_KNearestKeyReleased(KeyEvent e) {
        String s = txt_KNearest.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            lbl_KNearestError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_KNearestError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_KNearestError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_constParam text field.
     *
     * @param e an action event
     */
    private void txt_constParamKeyReleased(KeyEvent e) {
        String s = txt_constParam.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) <= 0) {
            lbl_constParamError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            btn_ok.setEnabled(true);
            lbl_constParamError.setText("");
        } else {
            lbl_constParamError.setText("");
        }
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        setKNearest(Integer.parseInt(txt_KNearest.getText()));
        setConstParam(Double.parseDouble(txt_constParam.getText()));
        dispose();
    }

    /**
     * This method sets an action for the btn_more button.
     *
     * @param e an action event
     */
    private void btn_moreActionPerformed(ActionEvent e) {
        String str = "Option\n\n";
        str += "k-nearest neighbor -> determines the number of data used as nearest neighbor (this parameter is used in the unsupervised version of Laplacian score).\n\n";
        str += "Constant parameter -> the normalize parameter.\n\n";
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
        String s = txt_KNearest.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            return false;
        }
        s = txt_constParam.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) <= 0) {
            return false;
        }
        return true;
    }

    /**
     * This method returns the k-nearest neighbor value.
     *
     * @return the <code>k-nearest neighbor</code> parameter
     */
    public int getKNearest() {
        return KNearest;
    }

    /**
     * This method sets the k-nearest neighbor value.
     *
     * @param KNearest the k-nearest neighbor value.
     */
    public void setKNearest(int KNearest) {
        this.KNearest = KNearest;
    }

    /**
     * This method returns the const parameter value.
     *
     * @return the <code>const parameter</code>
     */
    public double getConstParam() {
        return constParam;
    }

    /**
     * This method sets the const parameter value.
     *
     * @param constParam the const parameter value
     */
    public void setConstParam(double constParam) {
        this.constParam = constParam;
    }

    /**
     * sets the default values of the Laplacian score parameters
     */
    public void setDefaultValue() {
        txt_KNearest.setText(String.valueOf(defKNearest));
        txt_constParam.setText(String.valueOf(defConstParam));

        KNearest = defKNearest;
        constParam = defConstParam;
    }

    /**
     * sets the last values of the Laplacian score parameters entered by user
     *
     * @param kNNValue the k-nearest neighbor value
     * @param constant the constant value used in the similarity measure
     */
    public void setUserValue(int kNNValue, double constant) {
        KNearest = kNNValue;
        constParam = constant;

        txt_KNearest.setText(String.valueOf(KNearest));
        txt_constParam.setText(String.valueOf(constParam));
    }

//    public static void main(String[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        LaplacianScorePanel dtpanel = new LaplacianScorePanel();
//        Dialog dlg = new Dialog(dtpanel);
//        dtpanel.setVisible(true);
//        System.out.println("K-Nearest neighbor = " + dtpanel.getKNearest());
//        System.out.println("const parameter = " + dtpanel.getConstParam());
//    }
}
