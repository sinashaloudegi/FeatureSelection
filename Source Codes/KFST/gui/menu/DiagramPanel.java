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

import KFST.util.MathFunc;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
//import javax.swing.UIManager;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

/**
 * This java class is used to create and show the 2-D diagrams of the input
 * values
 *
 * @author Sina Tabakhi
 */
public class DiagramPanel extends JPanel implements ActionListener, MouseMotionListener {

    JFrame f;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem mi_save;
    JMenuItem mi_saveAs;
    JMenuItem mi_close;
    JTextField txt_info;
    //----------------------------------------------
    private String pathProject;
    private double[][] listIteration;
    private int[] listCases;
    private ArrayList<TupleValues> axisValues;
    private String nameMainPanel;
    private String nameDiagram;
    private String nameYAxis;
    private String nameLgendLabel;
    private int intervalYDimension = 7;
    private int maxValue;
    private int minValue;
    private double tempMaxValue;
    private double tempMinValue;
    private int xDimensionStart = 80;
    private int xDimensionEnd = 380;
    private int yDimensionStart = 60;
    private int yDimensionEnd = 280;
    private int widthLegend = 100;
    private int xDStartLegend = xDimensionEnd + 20;
    private int yDStartLegeng = yDimensionStart + 10;
    private Color[] colorSet = {new Color(192, 0, 0), //red color
        new Color(68, 114, 196), //blue color
        new Color(112, 173, 71), //green color
        new Color(255, 192, 0), //yellow color
        new Color(128, 128, 192), //purple color
        new Color(0, 0, 0), //black color
        new Color(0, 128, 128), //light green color
        new Color(128, 255, 255), //light blue color
        new Color(255, 128, 64), //orange color
        new Color(250, 50, 50)}; //light red color

    /**
     * Creates new form DiagramPanel. This method is called from within the
     * constructor to initialize the form.
     *
     * @param arrayIteration the results of all iteration
     * @param arrayCases the all different cases
     * @param namePanel the name of main panel
     * @param name the name of diagram
     * @param nameY the name of Y-axis
     * @param nameLegend the name of legend
     * @param path the path of the workspace
     */
    public DiagramPanel(double[][] arrayIteration, int[] arrayCases, String namePanel, String name, String nameY, String nameLegend, String path) {
        //sets the initial values
        listIteration = arrayIteration;
        listCases = arrayCases;
        nameMainPanel = namePanel;
        nameDiagram = name;
        nameYAxis = nameY;
        nameLgendLabel = nameLegend;
        pathProject = path;
        axisValues = new ArrayList<TupleValues>();
        findMaxValue();
        findMinValue();
        maxValue = (int) (tempMaxValue);
        if (tempMaxValue - maxValue > 0) {
            maxValue++;
        }
        minValue = (int) tempMinValue;
        if (maxValue - minValue == 0) {
            if (maxValue < 99) {
                maxValue++;
            } else {
                minValue--;
            }
        }
        if ((maxValue - minValue) <= intervalYDimension) {
            intervalYDimension = maxValue - minValue;
        } else {
            int res = maxValue - minValue;
            double res1 = res / 6.0;
            double res2 = res / 5.0;
            if (res1 == (int) res1) {
                intervalYDimension = 6;
            } else if (res2 == (int) res2) {
                intervalYDimension = 5;
            } else {
                if (((res2 - (int) res2) >= 0.5) && ((res2 - (int) res2) > (res1 - (int) res1))) {
                    intervalYDimension = 5;
                } else {
                    intervalYDimension = 6;
                }
            }
        }

        //create the menu bar in the main panel
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        mi_save = new JMenuItem("Save");
        mi_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        mi_save.addActionListener(this);
        mi_saveAs = new JMenuItem("Save as");
        mi_saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        mi_saveAs.addActionListener(this);
        mi_close = new JMenuItem("Close");
        mi_close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        mi_close.addActionListener(this);
        txt_info = new JTextField("");
        txt_info.setBounds(60, 67, 80, 22);
        txt_info.setBackground(new Color(242, 242, 242));
        txt_info.setVisible(false);
        txt_info.setEditable(false);

        fileMenu.add(mi_save);
        fileMenu.add(mi_saveAs);
        fileMenu.add(mi_close);
        menuBar.add(fileMenu);
        menuBar.setBounds(0, 0, 530, 22);
        setBackground(Color.white);

        f = new JFrame(nameMainPanel);
        f.add(menuBar);
        f.add(txt_info);
        f.add(this);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setIconImage(new ImageIcon(getClass().getResource("/KFST/gui/icons/small_logo.png")).getImage());
        f.setSize(530, 370);
        f.addMouseMotionListener(this);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setVisible(true);
    }

    /**
     * finds the maximum value of the input array
     */
    private void findMaxValue() {
        tempMaxValue = listIteration[0][0];
        for (int i = 0; i < listIteration.length; i++) {
            for (int j = 0; j < listIteration[0].length; j++) {
                if (listIteration[i][j] > tempMaxValue) {
                    tempMaxValue = listIteration[i][j];
                }
            }
        }
    }

    /**
     * finds the minimum value of the input array
     */
    private void findMinValue() {
        tempMinValue = listIteration[0][0];
        for (int i = 0; i < listIteration.length; i++) {
            for (int j = 0; j < listIteration[0].length; j++) {
                if (listIteration[i][j] < tempMinValue) {
                    tempMinValue = listIteration[i][j];
                }
            }
        }
    }

    /**
     * This method is used to show the diagram.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        axisValues = new ArrayList<TupleValues>();

        /**
         * draw framework of the diagram
         */
        g.drawLine(xDimensionStart, yDimensionStart - 1, xDimensionEnd, yDimensionStart - 1); //draw up horizontal line(-)
        g.drawLine(xDimensionEnd, yDimensionStart, xDimensionEnd, yDimensionEnd); //draw right vertical line(|)
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(xDimensionStart, yDimensionEnd, xDimensionEnd, yDimensionEnd); //draw down horizontal line(-)
        g2d.drawLine(xDimensionStart, yDimensionStart, xDimensionStart, yDimensionEnd); //draw left vertical line(|)
        g2d.setStroke(new BasicStroke(1));

        /**
         * draw labels of diagram
         */
        //x-axis title
        g.setFont(new Font("Tahoma", Font.BOLD, 13));
        g.drawString("number of features", xDimensionStart + ((xDimensionEnd - xDimensionStart) / 4), yDimensionEnd + 50);
        //title diagram
        g.setFont(new Font("Times New Roman", Font.BOLD, 22));
        g.drawString(nameDiagram, 160, 45);
        //y-axis title
        g2d.rotate(Math.toRadians(270), 10, 10);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 13));
        g2d.drawString(nameYAxis, -(yDimensionEnd - (yDimensionStart / 2)), 30);
        g2d.rotate(Math.toRadians(90), 10, 10);

        /**
         * draw x-axis gridline and value(-)
         */
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 11));
        g2d.setStroke(
                new BasicStroke(1, //width
                BasicStroke.CAP_SQUARE, //end cap
                BasicStroke.JOIN_MITER, //join style
                1, //miter limit
                new float[]{3, 3}, //dash pattern
                0.0f));
        g2d.setColor(new Color(242, 242, 242));
        //draw x-axis value labels
        int cutPoint2 = (xDimensionEnd - xDimensionStart) / (listCases.length + 1);
        int maskCutPoint2 = cutPoint2;
        for (int i = 0; i < listCases.length; i++) {
            int XPosition = xDimensionStart + maskCutPoint2;
            maskCutPoint2 += cutPoint2;
            //draw vertical gridline (|)
            g2d.drawLine(XPosition, yDimensionEnd - 2, XPosition, yDimensionStart + 1);
        }

        g2d.setColor(Color.black);
        maskCutPoint2 = cutPoint2;
        for (int i = 0; i < listCases.length; i++) {
            int XPosition = xDimensionStart + maskCutPoint2;
            maskCutPoint2 += cutPoint2;
            g2d.drawLine(XPosition, yDimensionEnd + 4, XPosition, yDimensionEnd); //draw small vertical line(|)
            g2d.drawString(String.valueOf(listCases[i]), XPosition - 3, yDimensionEnd + 22); //draw label small vertical line(|)
        }

        /**
         * draw y-axis gridline and value(-)
         */
        g2d.setColor(new Color(242, 242, 242));
        g2d.setStroke(
                new BasicStroke(1, //width
                BasicStroke.CAP_SQUARE, //end cap
                BasicStroke.JOIN_MITER, //join style
                1, //miter limit
                new float[]{3, 3}, //dash pattern
                0.0f));                //dash phase
        double cutPoint1 = ((yDimensionEnd - 20) - yDimensionStart) / (intervalYDimension);
        double maskCutPoint1 = cutPoint1;
        for (int i = 0; i < intervalYDimension; i++) {
            int yPosition = (int) (yDimensionEnd - maskCutPoint1);
            maskCutPoint1 += cutPoint1;
            //draw horizontal gridline (-)
            g2d.drawLine(xDimensionStart + 1, yPosition, xDimensionEnd, yPosition);
        }

        g2d.setColor(Color.black);
        maskCutPoint1 = 0;
        int interval = (int) Math.round((maxValue - minValue) / (double) intervalYDimension);
        for (int i = 0; i <= (intervalYDimension); i++) {
            int yPosition = (int) (yDimensionEnd - maskCutPoint1);
            maskCutPoint1 += cutPoint1;
            g2d.drawLine(xDimensionStart, yPosition, xDimensionStart - 5, yPosition); //draw small horizontal line(-)
            g2d.drawString(String.valueOf(minValue + (interval * i)), xDimensionStart - 22, yPosition + 3); //Draw label horizontal line(-)
        }

        /**
         *  sets data point values
         */
        g2d.setStroke(new BasicStroke(2));
        double yDimension = ((yDimensionEnd - 20.0) - yDimensionStart) / intervalYDimension;
        for (int i = 0; i < listIteration.length; i++) {
            maskCutPoint2 = cutPoint2;
            g2d.setColor(colorSet[i]);
            int XPosition = xDimensionStart + maskCutPoint2;
            int YPosition = (int) (((listIteration[i][0] - minValue) / interval) * yDimension);
            Point key = new Point(XPosition - 3, yDimensionEnd - YPosition - 3);
            axisValues.add(new TupleValues(key, new Point(i, 0)));
            g2d.fillRect(key.x, key.y, 8, 8);

            for (int j = 1; j < listCases.length; j++) {
                maskCutPoint2 += cutPoint2;
                int XNewPosition = xDimensionStart + maskCutPoint2;
                int YNewPosition = (int) (((listIteration[i][j] - minValue) / interval) * yDimension);
                key = new Point(XNewPosition - 3, yDimensionEnd - YNewPosition - 3);
                axisValues.add(new TupleValues(key, new Point(i, j)));
                g2d.fillRect(key.x, key.y, 8, 8);
                g2d.drawLine(XPosition, yDimensionEnd - YPosition, XNewPosition, yDimensionEnd - YNewPosition);
                XPosition = XNewPosition;
                YPosition = YNewPosition;
            }
        }

        /**
         * draw legend
         */
        //draw framework of legend
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.black);
        g2d.drawLine(xDStartLegend, yDimensionStart, xDStartLegend + widthLegend + 5, yDimensionStart); //draw up horizontal line(-)

        //draw String title
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 12));
        int height = 20;
        for (int i = 0; i < listIteration.length; i++) {
            g2d.drawString(nameLgendLabel + "(" + (i + 1) + ")", xDStartLegend + 35, yDStartLegeng + (height * i) + 4); //draw String
        }

        //draw sign and square
        g2d.setStroke(new BasicStroke(2));
        int maskHeight = 0;
        for (int i = 0; i < listIteration.length; i++) {
            g2d.setColor(colorSet[i]);
            g2d.drawLine(xDStartLegend + 4, yDStartLegeng + maskHeight, xDStartLegend + 30, yDStartLegeng + maskHeight); //draw sign
            g2d.fillRect(xDStartLegend + 13, yDStartLegeng + maskHeight - 4, 8, 8); //draw square
            maskHeight += height;
        }

        //draw other framework of legend
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.black);
        int EndLegend = (listIteration.length * height) + yDStartLegeng - (height / 2);
        g2d.drawLine(xDStartLegend + widthLegend + 5, yDimensionStart, xDStartLegend + widthLegend + 5, EndLegend); //draw right vertical line(|)
        g2d.drawLine(xDStartLegend, EndLegend, xDStartLegend + widthLegend + 5, EndLegend); //draw down horizontal line(-)
        g2d.drawLine(xDStartLegend, yDimensionStart, xDStartLegend, EndLegend); //draw left vertical line(|)
    }

    /**
     * The listener method for receiving action events.
     * Invoked when an action occurs.
     *
     * @param e an action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(mi_save)) {
            mi_saveActionPerformed(e);
        } else if (e.getSource().equals(mi_saveAs)) {
            mi_saveAsActionPerformed(e);
        } else if (e.getSource().equals(mi_close)) {
            mi_closeActionPerformed(e);
        }
    }

    /**
     * This method sets an action for the mi_save button.
     *
     * @param e an action event
     */
    private void mi_saveActionPerformed(ActionEvent e) {
        Container c = f.getContentPane();
        BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        c.paint(im.getGraphics());
        BufferedImage im2 = im.getSubimage(0, 25, c.getWidth(), c.getHeight() - 25);
        try {
            ImageIO.write(im2, "png", new File(pathProject + "diagram.png"));
        } catch (IOException ex) {
            Logger.getLogger(DiagramPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method sets an action for the mi_saveAs button.
     *
     * @param e an action event
     */
    private void mi_saveAsActionPerformed(ActionEvent e) {
        JFileChooser jfch = new JFileChooser(pathProject);
        jfch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfch.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String newPath = jfch.getSelectedFile().getPath();
            Container c = f.getContentPane();
            BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
            c.paint(im.getGraphics());
            BufferedImage im2 = im.getSubimage(0, 25, c.getWidth(), c.getHeight() - 25);
            try {
                ImageIO.write(im2, "png", new File(newPath + "\\diagram.png"));
            } catch (IOException ex) {
                Logger.getLogger(DiagramPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method sets an action for the mi_close button.
     *
     * @param e an action event
     */
    private void mi_closeActionPerformed(ActionEvent e) {
        f.dispose();
    }

    /**
     * The listener method for receiving mouse motion events on a component.
     * Invoked when a mouse button is pressed on a component and then dragged.
     *
     * @param e an action event
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * The listener method for receiving mouse motion events on a component.
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e an action event
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        TupleValues point = new TupleValues(new Point(e.getX() - 6, e.getY() - 28), null);
        Point res = point.get(axisValues);
        if (res != null) {
            txt_info.setBounds(e.getX() + 6, e.getY() - 30, 80, 22);
            //txt_info.setText("Value: " + String.valueOf(listIteration[res.x][res.y]));
            txt_info.setText("Value: " + MathFunc.roundDouble(listIteration[res.x][res.y], 3));
            txt_info.setVisible(true);
        } else {
            txt_info.setVisible(false);
        }
    }

    /**
     * This java class is used to create a data structure for pair
     * {@code (key, val)} in which <code>key</code> and <code>val</code> are
     * Point data type.
     */
    public class TupleValues {

        Point key;
        Point val;

        /**
         * Constructs and initializes a TupleValues with the specified
         * {@code (key, val)}.
         * 
         * @param key the key of the newly constructed <code>TupleValues</code>
         * @param val the val of the newly constructed <code>TupleValues</code>
         */
        public TupleValues(Point key, Point val) {
            this.key = key;
            this.val = val;
        }

        /**
         * returns the key of this TupleValues in Point data type.
         * 
         * @return the key of this TupleValues.
         */
        public Point getKey() {
            return this.key;
        }

        /**
         * returns the val of this TupleValues in Point data type.
         *
         * @return the val of this TupleValues.
         */
        public Point getVal() {
            return this.val;
        }

        /**
         * determines whether or not two TupleValues are equal in their keys. 
         * Two instances of <code>TupleValues</code> are equal in their keys
         * if the values of their <code>x</code> and <code>y</code> member
         * fields are the same.
         * 
         * @param obj an object to be compared with this <code>TupleValues</code>
         * 
         * @return <code>true</code> if the object to be compared has the same
         *         values; <code>false</code> otherwise.
         */
        public boolean equalsKey(TupleValues obj) {
            return (this.getKey().x == obj.getKey().x)
                    && (this.getKey().y == obj.getKey().y);
        }

        /**
         * returns the value to which the specified key is saved in the list, or
         * null if this list contains no element for the key.
         * <p>
         * More formally, if this list contains a mapping from a key k to a value
         * v, then this method returns v; otherwise it returns null.
         *
         * @param list the array of <code>TupleValues</code>
         *
         * @return the value to which the specified key is saved in the list, or
         *         null if the key is not exist in the list
         */
        public Point get(ArrayList<TupleValues> list) {
            for (Iterator it = list.listIterator(); it.hasNext();) {
                TupleValues tuple = (TupleValues) it.next();
                if (this.equalsKey(tuple)) {
                    return tuple.getVal();
                }
            }
            return null;
        }
    }

//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//        double[][] testValues = {{10, 20, 30, 40, 50},
//            {20, 30, 30, 20, 40},
//            {50, 40, 30, 20, 15},
//            {5, 15, 20, 22, 27},
//            {12, 14, 18, 25, 30},
//            {15, 25, 35, 45, 55},
//            {25, 35, 35, 25, 45},
//            {55, 45, 35, 25, 20},
//            {10, 20, 25, 27, 34},
//            {17, 19, 25, 30, 35}};
//
//        double[][] averageTestValues = {{10, 20, 30, 40, 50}};
//        int[] allCases = {10, 20, 30, 40, 50};
//
//        //DiagramPanel digPanel = new DiagramPanel(averageTestValues, allCases, "Average classification error rate diagram", "naive bayes", "Classification Error Rate (%)", "average", "C:\\Users\\ST\\Desktop\\");
//        DiagramPanel app = new DiagramPanel(testValues, allCases, "Classification accuracy diagram", "Decision Tree (C4.5)", "Classification Accuracy (%)", "iteration", "C:\\Users\\ST\\Desktop\\");
//    }
}
