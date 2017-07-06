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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show a panel for the parameter settings
 * of the support vector machine classifier.
 *
 * @author Shahin Salavati
 */
public class SVMClassifierPanel extends JDialog implements ActionListener {

    JLabel lbl_title, lbl_kernel, lbl_about;
    JComboBox cb_kernel;
    JButton btn_ok, btn_more;
    JPanel panel_about;
    private String typeKernel = "Polynomial kernel";
    private String defTypeKernel = "Polynomial kernel";

    /**
     * Creates new form SVMClassifierPanel. This method is called from within
     * the constructor to initialize the form.
     */
    public SVMClassifierPanel() {
        super();
        lbl_title = new JLabel("Support vector machine settings:");
        lbl_title.setBounds(10, 10, 160, 20);

        panel_about = new JPanel();
        panel_about.setBounds(10, 50, 400, 60);
        panel_about.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(171, 170, 170)), "About"));
        lbl_about = new JLabel("The SVM classifier with one-against-rest strategy for the multiclass problems.");

        panel_about.add(lbl_about);

        lbl_kernel = new JLabel("Kernel:");
        lbl_kernel.setBounds(50, 135, 120, 22);

        cb_kernel = new JComboBox(new String[]{"Polynomial kernel",
                    "RBF kernel",
                    "Pearson VII function-based universal kernel"});
        cb_kernel.setBounds(100, 135, 240, 22);

        btn_ok = new JButton("Ok");
        btn_ok.setBounds(120, 190, 75, 23);
        btn_ok.addActionListener(this);

        btn_more = new JButton("More");
        btn_more.setBounds(240, 190, 75, 23);
        btn_more.addActionListener(this);


        setModalityType(ModalityType.APPLICATION_MODAL);
        setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        setSize(440, 280);
        setLayout(null);
        add(lbl_title);
        add(panel_about);
        add(lbl_kernel);
        add(cb_kernel);
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
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    private void btn_okActionPerformed(ActionEvent e) {
        setKernel(cb_kernel.getSelectedItem().toString());
        dispose();
    }

    /**
     * This method sets an action for the btn_more button.
     *
     * @param e an action event
     */
    private void btn_moreActionPerformed(ActionEvent e) {
        String str = "Option\n\n";
        str += "Kernel -> the kernel to use.\n\n";
        MoreOpPanel morePanel = new MoreOpPanel(str);
        morePanel.setVisible(true);
    }

    /**
     * This method returns the name of kernel.
     *
     * @return the <code>Kernel</code> parameter
     */
    public String getKernel() {
        return typeKernel;
    }

    /**
     * This method sets the the name of kernel.
     *
     * @param kernelName the name of kernel
     */
    public void setKernel(String kernelName) {
        this.typeKernel = kernelName;
    }

    /**
     * sets the default values of the support vector machine parameters
     */
    public void setDefaultValue() {
        cb_kernel.setSelectedItem(defTypeKernel);
        typeKernel = defTypeKernel;
    }

    /**
     * sets the last values of the support vector machine parameters entered by
     * user
     * 
     * @param type the name of the kernel
     */
    public void setUserValue(String type) {
        typeKernel = type;
        cb_kernel.setSelectedItem(typeKernel);
    }

//    public static void main(Strings[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        SVMClassifierPanel dtpanel = new SVMClassifierPanel();
//        Dialog dlg = new Dialog(dtpanel);
//        dtpanel.setVisible(true);
//        System.out.println("kernel = " + dtpanel.getKernel());
//    }
}
