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
package KFST.gui;

//import java.awt.Dialog;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show a panel for the "more option"
 * button of the feature selection methods.
 *
 * @author Shahin Salavati
 */
public class MoreOpPanel extends JDialog {

    JTextArea txt_main;
    JScrollPane scroll;

    /**
     * Creates new form MoreOpPanel. This method is called from within the
     * constructor to initialize the form.
     *
     * @param text the text that is shown in the panel
     */
    public MoreOpPanel(String text) {
        super();
        txt_main = new JTextArea(text);
        txt_main.setEditable(false);
        txt_main.setLineWrap(true);
        scroll = new JScrollPane(txt_main);
        scroll.setBounds(0, 0, 400, 400);
        scroll.setViewportView(txt_main);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        setSize(400, 400);
        setLayout(new GridLayout(1, 1));
        add(scroll);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Information");
    }

//    public static void main(Strings[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        MoreOpPanel mop = new MoreOpPanel("Showing some information...");
//        Dialog dlg = new Dialog(mop);
//        mop.setVisible(true);
//    }
}
