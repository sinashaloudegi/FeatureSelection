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
package KFST.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show a panel for the selecting mode of
 * showing the results.
 * <p>
 * Two modes are available: results of each iteration or average results of all
 * iteration.
 * 
 * @author Sina Tabakhi
 */
public class SelectModePanel extends JDialog implements ActionListener {

    JLabel lbl_selectMode;
    JButton btn_ok;
    ButtonGroup bg_mode;
    JRadioButton rd_average, rd_total;
    String nameMode;

    /**
     * Creates new form SelectModePanel. This method is called from within the
     * constructor to initialize the form.
     */
    public SelectModePanel() {

        setNameMode("none");
        lbl_selectMode = new JLabel("Selects a mode:");
        lbl_selectMode.setBounds(20, 40, 90, 22);

        rd_average = new JRadioButton("Average of all iteration");
        rd_average.setBounds(110, 30, 170, 20);
        rd_average.setSelected(true);
        rd_total = new JRadioButton("Total results of each iteration");
        rd_total.setBounds(110, 60, 195, 20);

        bg_mode = new ButtonGroup();
        bg_mode.add(rd_average);
        bg_mode.add(rd_total);

        btn_ok = new JButton("Ok");
        btn_ok.setBounds(110, 110, 70, 23);
        btn_ok.addActionListener(this);


        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(310, 190);
        setLayout(null);

        add(rd_average);
        add(rd_total);
        add(lbl_selectMode);
        add(btn_ok);

        setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Selection Mode Panel");
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
        }
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        if (rd_average.isSelected()) {
            setNameMode("Average");
        } else if (rd_total.isSelected()) {
            setNameMode("Total");
        }
        dispose();
    }

    /**
     * This method returns the type of mode. Two values can be returned:<br>
     * - "Average": means that the average results of all iteration<br>
     * - "Total": means that the total results of each iteration
     * 
     * @return the <code>nameMode</code> parameter
     */
    public String getNameMode() {
        return nameMode;
    }

    /**
     * This method sets the type of mode. Two types of modes are as follows:<br>
     * - "Average": shows the average results of all iteration<br>
     * - "Total": shows the total results of each iteration
     *
     * @param nameMode the name of mode
     */
    public void setNameMode(String nameMode) {
        this.nameMode = nameMode;
    }

//    public static void main(Strings[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        SelectModePanel app = new SelectModePanel();
//        JDialog dlg = new JDialog(app);
//        app.setVisible(true);
//        System.out.println("Status = " + app.getNameMode());
//    }
}
