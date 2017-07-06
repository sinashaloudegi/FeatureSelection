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
 * of the relevance-redundancy feature selection(RRFS) method.
 *
 * @see KFST.featureSelection.filter.unsupervised.RRFS
 * @see KFST.featureSelection.filter.supervised.RRFS
 *
 * @author Shahin Salavati
 */
public class RRFSPanel extends JDialog
        implements ActionListener, KeyListener {

    JLabel lbl_title, lbl_about, lbl_similarity, lbl_similarityError;
    JTextField txt_similarity;
    JButton btn_ok, btn_more;
    JPanel panel_about;
    private double similarity = 0.4;
    private double defSimilarity = 0.4;

    /**
     * Creates new form RRFSPanel. This method is called from within the
     * constructor to initialize the form.
     */
    public RRFSPanel() {
        super();
        lbl_title = new JLabel("Relevance-redundancy feature selection (RRFS) settings:");
        lbl_title.setBounds(10, 10, 440, 20);

        panel_about = new JPanel();
        panel_about.setBounds(10, 40, 450, 80);
        panel_about.setLayout(null);
        panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
        lbl_about = new JLabel("<html>Relevance-redundancy feature selection (RRFS) is an efficient feature selection method based on relevance and redundancy analyses, which can work in both supervised and unsupervised modes.</html>");
        lbl_about.setBounds(15, 10, 430, 76);

        panel_about.add(lbl_about);


        lbl_similarity = new JLabel("Similarity threshold:");
        lbl_similarity.setBounds(50, 135, 170, 22);
        txt_similarity = new JTextField(String.valueOf(defSimilarity));
        txt_similarity.setBounds(155, 135, 120, 21);
        txt_similarity.addKeyListener(this);
        lbl_similarityError = new JLabel("");
        lbl_similarityError.setBounds(285, 135, 50, 22);
        lbl_similarityError.setForeground(Color.red);

        btn_ok = new JButton("Ok");
        btn_ok.setBounds(140, 185, 75, 23);
        btn_ok.addActionListener(this);

        btn_more = new JButton("More");
        btn_more.setBounds(260, 185, 75, 23);
        btn_more.addActionListener(this);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(480, 280);
        setLayout(null);
        add(lbl_title);
        add(panel_about);

        add(lbl_similarity);
        add(txt_similarity);
        add(lbl_similarityError);

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
        if (e.getSource().equals(txt_similarity)) {
            txt_similarityKeyReleased(e);
        }
    }

    /**
     * This method sets an action for the txt_similarity text field.
     *
     * @param e an action event
     */
    private void txt_similarityKeyReleased(KeyEvent e) {
        String s = txt_similarity.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            lbl_similarityError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_similarityError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_similarityError.setText("");
        }
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        setSimilarity(Double.parseDouble(txt_similarity.getText()));
        dispose();
    }

    /**
     * This method sets an action for the btn_more button.
     *
     * @param e an action event
     */
    private void btn_moreActionPerformed(ActionEvent e) {
        String str = "Option\n\n";
        str += "Similarity threshold -> maximum allowed similarity between two features (a real number in the range of [0, 1]).\n\n";
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
     * This method checks the text field values due to correct format
     *
     * @return true if all text field values are in the correct format
     */
    private boolean checkVisibleButton() {
        String s = txt_similarity.getText();
        if (s.equals("") || !isCorrect(s) || Double.parseDouble(s) < 0 || Double.parseDouble(s) > 1) {
            return false;
        }
        return true;
    }

    /**
     * This method returns the similarity threshold value.
     *
     * @return the <code>similarity threshold</code> parameter
     */
    public double getSimilarity() {
        return similarity;
    }

    /**
     * This method sets the similarity threshold value.
     *
     * @param similarity the similarity threshold value
     */
    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    /**
     * sets the default values of the RRFS parameters
     */
    public void setDefaultValue() {
        txt_similarity.setText(String.valueOf(defSimilarity));
        similarity = defSimilarity;
    }

    /**
     * sets the last values of the RRFS parameters entered by user
     *
     * @param simValue the value of the similarity
     */
    public void setUserValue(double simValue) {
        similarity = simValue;
        txt_similarity.setText(String.valueOf(similarity));
    }

//    public static void main(Strings[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        RRFSPanel dtpanel = new RRFSPanel();
//        Dialog dlg = new Dialog(dtpanel);
//        dtpanel.setVisible(true);
//        System.out.println("similarity value = " + dtpanel.getSimilarity());
//    }
}
