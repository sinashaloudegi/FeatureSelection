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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
//import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.UIManager;

/**
 * This java class is used to create and show a panel for description of the
 * tool.
 *
 * @author Sina Tabakhi
 */
public class AboutPanel extends JDialog implements MouseListener {

    JLabel lbl_logo, lbl_name, lbl_descrip, lbl_ver, lbl_verVal,
            lbl_author, lbl_authorVal, lbl_home, lbl_homVal, lbl_footer_1,
            lbl_footer_2, lbl_footer_3, lbl_nameUni, lbl_License;
    JPanel panel_footer;

    /**
     * Creates new form AboutPanel. This method is called from within the
     * constructor to initialize the form.
     */
    public AboutPanel() {
        super();
        lbl_logo = new JLabel(new ImageIcon(getClass().getResource("/KFST/gui/icons/logo.png")));
        lbl_logo.setBounds(1, 1, 240, 240);

        lbl_name = new JLabel(new ImageIcon(getClass().getResource("/KFST/gui/icons/logo_name.png")));
        lbl_name.setBounds(240, 10, 270, 90);

        lbl_descrip = new JLabel("<html>Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed completely in Java, for performing feature selection process in different areas of research.<html>");
        lbl_descrip.setBounds(260, 100, 375, 50);

        lbl_ver = new JLabel("Version:");
        lbl_ver.setFont(new Font("Tahoma", Font.BOLD, 11));
        lbl_ver.setBounds(260, 160, 70, 15);

        lbl_verVal = new JLabel("0.1");
        lbl_verVal.setBounds(340, 160, 100, 15);

        lbl_author = new JLabel("Author:");
        lbl_author.setFont(new Font("Tahoma", Font.BOLD, 11));
        lbl_author.setBounds(260, 185, 70, 15);

        lbl_authorVal = new JLabel("<html><a href=\"\">KFST team</a></html>");
        lbl_authorVal.setBounds(340, 185, 100, 15);
        lbl_authorVal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl_authorVal.addMouseListener(this);

        lbl_home = new JLabel("Home Page:");
        lbl_home.setFont(new Font("Tahoma", Font.BOLD, 11));
        lbl_home.setBounds(260, 210, 70, 15);

        lbl_homVal = new JLabel("<html><a href=\"\">http://kfst.uok.ac.ir</a></html>");
        lbl_homVal.setBounds(340, 210, 100, 15);
        lbl_homVal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl_homVal.addMouseListener(this);


        panel_footer = new JPanel();
        panel_footer.setBounds(0, 250, 670, 45);
        panel_footer.setLayout(null);
        panel_footer.setBackground(new Color(225, 225, 225));

        lbl_footer_1 = new JLabel("KFST is developed at ");
        lbl_footer_1.setBounds(50, 20, 104, 15);

        lbl_nameUni = new JLabel("<html><a href=\"\">University of Kurdistan</a></html>");
        lbl_nameUni.setBounds(154, 20, 109, 15);
        lbl_nameUni.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl_nameUni.addMouseListener(this);

        lbl_footer_2 = new JLabel(", Iran, and distributed under the terms of the ");
        lbl_footer_2.setBounds(262, 20, 223, 15);

        lbl_License = new JLabel("<html><a href=\"\">GNU General Public License</a></html>");
        lbl_License.setBounds(483, 20, 129, 15);
        lbl_License.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl_License.addMouseListener(this);

        lbl_footer_3 = new JLabel(".");
        lbl_footer_3.setBounds(612, 20, 5, 15);

        
        panel_footer.add(lbl_footer_1);
        panel_footer.add(lbl_nameUni);
        panel_footer.add(lbl_footer_2);
        panel_footer.add(lbl_License);
        panel_footer.add(lbl_footer_3);


        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(670, 323);
        setLayout(null);

        add(lbl_logo);
        add(lbl_name);
        add(lbl_descrip);
        add(lbl_ver);
        add(lbl_verVal);
        add(lbl_author);
        add(lbl_authorVal);
        add(lbl_home);
        add(lbl_homVal);
        add(panel_footer);

        setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("About KFST");
    }

    /**
     * opens default browser using the given URL
     *
     * @param url the url of a webpage
     */
    private void openURL(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            Logger.getLogger(AboutPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The listener method for receiving interesting mouse events on a 
     * component. Invoked when the mouse button has been clicked on a component.
     *
     * @param e an mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(lbl_authorVal)) {
            openURL("http://kfst.uok.ac.ir/people.html");
        } else if (e.getSource().equals(lbl_homVal)) {
            openURL("http://kfst.uok.ac.ir");
        } else if (e.getSource().equals(lbl_nameUni)) {
            openURL("http://en.uok.ac.ir/UOK.aspx");
        } else if (e.getSource().equals(lbl_License)) {
            openURL("http://www.gnu.org/licenses/gpl.html");
        }
    }

    /**
     * The listener method for receiving interesting mouse events on a
     * component. Invoked when the mouse button has been pressed on a component.
     *
     * @param e an mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * The listener method for receiving interesting mouse events on a
     * component. Invoked when the mouse button has been released on a component.
     *
     * @param e an mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * The listener method for receiving interesting mouse events on a
     * component. Invoked when the mouse enters a component.
     *
     * @param e an mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * The listener method for receiving interesting mouse events on a
     * component. Invoked when the mouse exits a component.
     *
     * @param e an mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

//    public static void main(Strings[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        AboutPanel mop = new AboutPanel();
//        Dialog dlg = new Dialog(mop);
//        mop.setVisible(true);
//    }
}
