/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 *
 * @author David Xie
 */
public class DrawingApplicationFrame extends JFrame 
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    private final JPanel line1;
    private final JLabel label1;
    private final JComboBox<String> shapesJComboBox;
    private final JButton color1;
    private Color drawColor1 = Color.BLACK;
    private final JButton color2;
    private Color drawColor2 = Color.BLACK;
    private final JButton undo;
    private final JButton clear;
    private static final String[] Shapes = {"Rectangle" ,"Line","Oval"};
    
    private final JPanel line2;
    private final JLabel label2;
    private final JCheckBox filled;
    private final JCheckBox gradient;
    private final JCheckBox dashed;
    private final JLabel width;
    private final JSpinner widthSize;
    private final JLabel length;
    private final JSpinner lengthSize;
    
    private final BorderLayout layout;
    private final JPanel line3;
    
    private final DrawPanel D;
    private final JLabel statusBar;

    
    
    
    
  
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        super("Java 2D Drawings");
        // add widgets to panels
        // create the widgets for the firstLine Panel.
        // firstLine widgets
        line1 = new JPanel();
        label1  = new JLabel("Shape:");
        shapesJComboBox = new JComboBox<>(Shapes);
        shapesJComboBox.setMaximumRowCount(3);
        color1 = new JButton("1st Color");
        color2 = new JButton("2nd Color");         
        undo = new JButton("Undo");        
        clear = new JButton("Clear");           
        line1.add(label1);
        line1.add(shapesJComboBox);
        line1.add(color1);
        line1.add(color2);
        line1.add(undo);
        line1.add(clear);

        
        // secondLine widgets
        //create the widgets for the secondLine Panel.
        line2 = new JPanel();
        label2 = new JLabel("Options:");
        filled = new JCheckBox("Filled");
        gradient = new JCheckBox("Use Gradient");
        dashed = new JCheckBox("Dashed");
        width = new JLabel("Line WIdth:");
        widthSize = new JSpinner(new SpinnerNumberModel(1,1,99,1));
        length = new JLabel("Dashed Length:");
        lengthSize = new JSpinner(new SpinnerNumberModel(1,1,99,1));
        
        line2.add(label2);
        line2.add(filled);
        line2.add(gradient);
        line2.add(dashed);
        line2.add(width);
        line2.add(widthSize);
        line2.add(length);
        line2.add(lengthSize);
        
        
        line1.setBackground(Color.CYAN);
        line2.setBackground(Color.CYAN);
       
        
        //for the first 2 lines use 2 panel and put the panel in another panel and use gridlayout to set it up
        // add top panel of two panels
        line3 = new JPanel();
        line3.setLayout (new GridLayout(2,1));
        line3.add(line1);
        line3.add(line2);
        
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        layout = new BorderLayout();
        setLayout(layout);
        add(line3,BorderLayout.NORTH);
        
        // Variables for drawPanel.
        D = new DrawPanel();
        D.setBackground(Color.WHITE);
        add(D, BorderLayout.CENTER);
        
        // add status label
        statusBar = new JLabel();
        add(statusBar, BorderLayout.SOUTH);


        //add listeners and event handlers
        color1.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent event){
                        drawColor1 = JColorChooser.showDialog(null,"Choose a color", drawColor1);
                    
                    }
                }
        );
        
        color2.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent event){
                        drawColor2 = JColorChooser.showDialog(null,"Choose a color", drawColor2);

                    }
                }
        );
         
        undo.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent event){
                        //arraylist of shapes
                        DrawingApplicationFrame.this.D.allshapesList.remove(DrawingApplicationFrame.this.D.allshapesList.size()-1);
                
                        
                    }
                }
        );
          
        clear.addActionListener(
                    new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent event){
                            //clear the arraylist
                            DrawingApplicationFrame.this.D.allshapesList.clear();
                        }
                    }
            );
    }
    // Create event handlers, if needed
    
            
            
    // Create a private inner class for the DrawPanel.
    
   
    private class DrawPanel extends JPanel
    {
        public final ArrayList<MyShapes> allshapesList;
        
        public DrawPanel()
        {
            allshapesList = new ArrayList();
            MouseHandler handler = new MouseHandler();
            addMouseListener(handler);
            addMouseMotionListener(handler);
            
        }
        @Override
        public void paintComponent(Graphics g) //does the drawing
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            repaint();
            for(MyShapes shapes : allshapesList){
                shapes.draw(g2d);
            }

        }
        
        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            Point start;
            Point end;
            int lineWidth;
            float[] dashLength;
            BasicStroke stroke;
            boolean fill;
            Paint paint;
                
            @Override
            public void mousePressed(MouseEvent event)
            {   
                int x = event.getX();
                int y = event.getY();
                start = new Point(x,y);
                end = new Point(x,y);
                
                if(gradient.isSelected()){
                    this.paint = new GradientPaint(0 , 0 , drawColor1 , 50 , 50 , drawColor2, true);
                            
                }else{
                    this.paint = drawColor1;
                }
                
                this.lineWidth = (int)widthSize.getValue();
                this.dashLength = new float[1];
                dashLength[0] = (int)lengthSize.getValue();
                
                if (dashed.isSelected())
            {
                stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0);
            } else
            {
                stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }
                
                this.fill = filled.isSelected();
                               
                if(shapesJComboBox.getSelectedItem()== "Line"){
                    MyLine line = new MyLine(this.start, this.end , this.paint, this.stroke);
                    allshapesList.add(line);
                }
                
                if(shapesJComboBox.getSelectedItem() == "Rectangle"){
                    MyRectangle rectangle = new MyRectangle(this.start, this.end, this.paint, this.stroke, this.fill);
                    allshapesList.add(rectangle);
                }
                if (shapesJComboBox.getSelectedItem() == "Oval"){
                    MyOval oval = new MyOval(this.start,this.end,this.paint,this.stroke,this.fill);
                    allshapesList.add(oval);
                }
                
                
            }
            @Override
            public void mouseReleased(MouseEvent event)
            {
                int x = event.getX();
                int y = event.getY();
                end = new Point(x,y);
                
                allshapesList.get(allshapesList.size()-1).setEndPoint(end);
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                int x = event.getX();
                int y = event.getY();
                end = new Point(x,y);
                allshapesList.get(allshapesList.size()-1).setEndPoint(end);
                
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusBar.setText(String.format("%d, %d",event.getX(),event.getY()));
            }
        }

    }
}
