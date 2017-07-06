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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show a panel for the parameter settings
 * of the random subspace method (RSM) method.
 *
 * @see KFST.featureSelection.filter.unsupervised.RSM
 *
 * @author Shahin Salavati
 */
public class RSMPanel extends JDialog
        implements ActionListener, KeyListener {

    JLabel lbl_title, lbl_about,
            lbl_numSelection, lbl_numSelectionError,
            lbl_sizeSubspace, lbl_sizeSubspaceError,
            lbl_elimination, lbl_eliminationError,
            lbl_multivalMethod;
    JTextField txt_numSelection,
            txt_sizeSubspace,
            txt_elimination;
    JComboBox cb_multivalMethod;
    JButton btn_ok, btn_more;
    JPanel panel_about;
    private int numSelection = 50, sizeSubspace = 0, elimination = 0;
    private int defNumSelection = 50, defSizeSubspace = 0, defElimination = 0;
    private String multMethodName = "Mutual correlation";
    private String defMultMethodName = "Mutual correlation";

    /**
     * Creates new form RSMPanel. This method is called from within the
     * constructor to initialize the form.
     */
    public RSMPanel() {
        super();
        lbl_title = new JLabel("Random subspace method (RSM) settings:");
        lbl_title.setBounds(10, 10, 450, 20);

        panel_about = new JPanel();
        panel_about.setBounds(10, 40, 490, 80);
        panel_about.setLayout(null);
        panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
        lbl_about = new JLabel("<html> Random subspace method (RSM) has been proposed to reduce the computational complexity in determining the relevance features for multivariate methods. This method applies mutual correlation as a multivariate methods in the search strategy. </html>");
        lbl_about.setBounds(15, 10, 480, 76);

        panel_about.add(lbl_about);


        lbl_numSelection = new JLabel("Number of selections:");
        lbl_numSelection.setBounds(50, 135, 170, 22);
        txt_numSelection = new JTextField(String.valueOf(defNumSelection));
        txt_numSelection.setBounds(170, 135, 120, 21);
        txt_numSelection.addKeyListener(this);
        lbl_numSelectionError = new JLabel("");
        lbl_numSelectionError.setBounds(300, 135, 50, 22);
        lbl_numSelectionError.setForeground(Color.red);

        lbl_sizeSubspace = new JLabel("Size of subspace:");
        lbl_sizeSubspace.setBounds(50, 170, 170, 22);
        txt_sizeSubspace = new JTextField(Integer.toString(defSizeSubspace));
        txt_sizeSubspace.setBounds(170, 170, 120, 21);
        txt_sizeSubspace.addKeyListener(this);
        lbl_sizeSubspaceError = new JLabel("");
        lbl_sizeSubspaceError.setBounds(300, 170, 50, 22);
        lbl_sizeSubspaceError.setForeground(Color.red);

        lbl_elimination = new JLabel("Elimination threshold:");
        lbl_elimination.setBounds(50, 205, 170, 22);
        txt_elimination = new JTextField(Integer.toString(defElimination));
        txt_elimination.setBounds(170, 205, 120, 21);
        txt_elimination.addKeyListener(this);
        lbl_eliminationError = new JLabel("");
        lbl_eliminationError.setBounds(300, 205, 50, 22);
        lbl_eliminationError.setForeground(Color.red);

        lbl_multivalMethod = new JLabel("Multivariate method:");
        lbl_multivalMethod.setBounds(50, 240, 170, 22);
        cb_multivalMethod = new JComboBox(new String[]{"Mutual correlation"});
        cb_multivalMethod.setBounds(170, 240, 120, 22);


        btn_ok = new JButton("Ok");
        btn_ok.setBounds(160, 290, 75, 23);
        btn_ok.addActionListener(this);

        btn_more = new JButton("More");
        btn_more.setBounds(280, 290, 75, 23);
        btn_more.addActionListener(this);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(520, 380);
        setLayout(null);
        add(lbl_title);
        add(panel_about);

        add(lbl_numSelection);
        add(txt_numSelection);
        add(lbl_numSelectionError);

        add(lbl_sizeSubspace);
        add(txt_sizeSubspace);
        add(lbl_sizeSubspaceError);

        add(lbl_elimination);
        add(txt_elimination);
        add(lbl_eliminationError);

        add(lbl_multivalMethod);
        add(cb_multivalMethod);

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
        if (e.getSource().equals(txt_numSelection)) {
            txt_numSelectionKeyReleased(e);
        } else if (e.getSource().equals(txt_sizeSubspace)) {
            txt_sizeSubspaceKeyReleased(e);
        } else if (e.getSource().equals(txt_elimination)) {
            txt_eliminationKeyReleased(e);
        }
    }

    /**
     * This method sets an action for the txt_numSelection text field.
     *
     * @param e an action event
     */
    private void txt_numSelectionKeyReleased(KeyEvent e) {
        String s = txt_numSelection.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            lbl_numSelectionError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            btn_ok.setEnabled(true);
            lbl_numSelectionError.setText("");
        } else {
            lbl_numSelectionError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_sizeSubspace text field.
     *
     * @param e an action event
     */
    private void txt_sizeSubspaceKeyReleased(KeyEvent e) {
        String s = txt_sizeSubspace.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || (Integer.parseInt(s) < 1 && Integer.parseInt(s) != 0)) {
            lbl_sizeSubspaceError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_sizeSubspaceError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_sizeSubspaceError.setText("");
        }
    }

    /**
     * This method sets an action for the txt_elimination text field.
     *
     * @param e an action event
     */
    private void txt_eliminationKeyReleased(KeyEvent e) {
        String s = txt_elimination.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || (Integer.parseInt(s) < 1 && Integer.parseInt(s) != 0)) {
            lbl_eliminationError.setText("*");
            btn_ok.setEnabled(false);
        } else if (checkVisibleButton()) {
            lbl_eliminationError.setText("");
            btn_ok.setEnabled(true);
        } else {
            lbl_eliminationError.setText("");
        }
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        setNumSelection(Integer.parseInt(txt_numSelection.getText()));
        setSizeSubspace(Integer.parseInt(txt_sizeSubspace.getText()));
        setElimination(Integer.parseInt(txt_elimination.getText()));
        setMultMethodName(String.valueOf(cb_multivalMethod.getSelectedItem()));
        dispose();
    }

    /**
     * This method sets an action for the btn_more button.
     *
     * @param e an action event
     */
    private void btn_moreActionPerformed(ActionEvent e) {
        String str = "Option\n\n";
        str += "Number of selections -> determines the number of selections in the algorithm.\n\n";
        str += "Size of subspace -> the size of subspace which is selected from the original features (0 means the size of subspace is set to half of the number of original features).\n\n";
        str += "Elimination threshold -> determines the number of relevant features in the selected subspace (0 means the elimination threshold is set to half of the size of subspace).\n\n";
        str += "Multivariate method -> the name of multivariate feature selection method applied in the algorithm.\n\n";
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
        String s = txt_numSelection.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || Integer.parseInt(s) < 1) {
            return false;
        }
        s = txt_sizeSubspace.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || (Integer.parseInt(s) < 1 && Integer.parseInt(s) != 0)) {
            return false;
        }
        s = txt_elimination.getText();
        if (s.equals("") || !isCorrect(s) || !isInteger(s) || (Integer.parseInt(s) < 1 && Integer.parseInt(s) != 0)) {
            return false;
        }
        return true;
    }

    /**
     * This method returns the number of selections value.
     *
     * @return the <code>numSelection</code> parameter
     */
    public int getNumSelection() {
        return numSelection;
    }

    /**
     * This method sets the number of selections value.
     *
     * @param numSelection the number of selections value
     */
    public void setNumSelection(int numSelection) {
        this.numSelection = numSelection;
    }

    /**
     * This method returns the size of subspace value.
     *
     * @return the <code>sizeSubspace</code> parameter
     */
    public int getSizeSubspace() {
        return sizeSubspace;
    }

    /**
     * This method sets the size of subspace value.
     *
     * @param sizeSubspace the size of subspace value
     */
    public void setSizeSubspace(int sizeSubspace) {
        this.sizeSubspace = sizeSubspace;
    }

    /**
     * This method returns the elimination threshold value.
     *
     * @return the <code>elimination</code> parameter
     */
    public int getElimination() {
        return elimination;
    }

    /**
     * This method sets the elimination threshold value.
     *
     * @param elimination the elimination threshold value
     */
    public void setElimination(int elimination) {
        this.elimination = elimination;
    }

    /**
     * This method returns the name of multivariate method. The current values
     * are:<br>
     * - "Mutual correlation": the mutual correlation method.
     *
     * @return the <code>multMethodName</code>
     *
     * @see KFST.featureSelection.filter.unsupervised.MutualCorrelation
     */
    public String getMultMethodName() {
        return multMethodName;
    }

    /**
     * This method sets the name of multivariate method. The current values
     * are:<br>
     * - "Mutual correlation": the mutual correlation method.
     *
     * @param multMethodName the name of multivariate method
     *
     * @see KFST.featureSelection.filter.unsupervised.MutualCorrelation
     */
    public void setMultMethodName(String multMethodName) {
        this.multMethodName = multMethodName;
    }

    /**
     * sets the default values of the RSM parameters
     */
    public void setDefaultValue() {
        txt_numSelection.setText(String.valueOf(defNumSelection));
        txt_sizeSubspace.setText(String.valueOf(defSizeSubspace));
        txt_elimination.setText(String.valueOf(defElimination));
        cb_multivalMethod.setSelectedItem(defMultMethodName);

        numSelection = defNumSelection;
        sizeSubspace = defSizeSubspace;
        elimination = defElimination;
        multMethodName = defMultMethodName;
    }

    /**
     * sets the last values of the RSM parameters entered by user
     *
     * @param numSelect the number of iteration in the RSM method
     * @param sizeSpace the size of the subspace
     * @param eliminValue the number of selected features in each subspace
     * @param nameMultMethod the name of the multivariate approach used in the RSM
     *
     * @see KFST.featureSelection.filter.unsupervised.MutualCorrelation
     */
    public void setUserValue(int numSelect, int sizeSpace, int eliminValue, String nameMultMethod) {
        numSelection = numSelect;
        sizeSubspace = sizeSpace;
        elimination = eliminValue;
        multMethodName = nameMultMethod;

        txt_numSelection.setText(String.valueOf(numSelection));
        txt_sizeSubspace.setText(String.valueOf(sizeSubspace));
        txt_elimination.setText(String.valueOf(elimination));
        cb_multivalMethod.setSelectedItem(multMethodName);
    }

//    public static void main(Strings[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        RSMPanel dtpanel = new RSMPanel();
//        Dialog dlg = new Dialog(dtpanel);
//        dtpanel.setVisible(true);
//        System.out.println("num selection = " + dtpanel.getNumSelection());
//        System.out.println("size subspace = " + dtpanel.getSizeSubspace());
//        System.out.println("elimination = " + dtpanel.getElimination());
//        System.out.println("multivariate method = " + dtpanel.getMultMethodName());
//    }
}
