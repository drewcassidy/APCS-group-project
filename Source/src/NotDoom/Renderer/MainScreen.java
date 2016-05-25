
package NotDoom.Renderer;

import NotDoom.IntVector;
import NotDoom.Map.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

// Our ImageComponent will inherit all the properties of a JComponent,
// allowing it to be added to a JPanel or JFrame.
public class MainScreen extends JComponent
{
    //We will use a BufferedImage object to get the image from a file.
    private BufferedImage image;
    private boolean tab; 
    private ArrayList<Wall> walls;
    // Constructor
    // You may choose to have a parameter, such as a String filename,
    // so this class can handle different files without changing the code.
    
    
    public MainScreen(BufferedImage im, Region[] r )
    {
        
            // ImageIO.read() returns a BufferedImage object, decoding
            // the supplied file with an ImageReader, chosen
            // automatically from registered files. The File is wrapped
            // in an ImageInputStream object, so we don't need one.
            // Null is returned, If no registered ImageReader claims to
            // be able to read the resulting stream.
        image = im;
        System.out.println(r.length+" Region");
        walls = new ArrayList<>();
        
        for (int i = 0; i < r.length; i++){
                System.out.println("IM COCO FOR COCOPUFFS");
                
                for (int j = 0; j < r[i].getWalls().length;j++){
                    System.out.println(r[i].getWalls().length+" Walls");
                    walls.add(r[i].getWalls()[j]);                
                }
        }
        
    }
    
    public void tabed(boolean t){
        tab = t;      
    }
    
    public void giveRegions(Region[] r){
        
        System.out.println("I GAT IT");
    }
    
    
    // This next method will set the preferred size of the image.
    // It overrides the method in the Container class.
    // This method sets the preferred size to the actual image size.
    // You may choose to set the preferred size differently, it does not
    // have to be exactly as written here
    public Dimension getPreferredSize()
    {
        if (image == null)
        {
            return new Dimension(100,100);
        }
        else
        {
            return new Dimension(image.getWidth(null), image.getHeight(null));
        }
    }
    // We will need to override the paint method in order to Ã¢â‚¬Å“drawÃ¢â‚¬Â the image.
    public void paint( Graphics g )
    {
        //System.out.println("I HAVE STUFF");
        
        if(true == true){
            //System.out.println("SUPER STUFFY STUFF");
            
            Graphics2D g2 = (Graphics2D) g;
            
            g2.setColor(Color.RED);
            System.out.println(walls.size());
            for (int j = 0; j < walls.size(); j++){
                //System.out.println("HEllo");
                IntVector v1, v2;
                v1 = walls.get(j).v1();
                v2 = walls.get(j).v2();
                
                Line2D.Double slash = new Line2D.Double(v1.x(),v1.y(),v2.x(),v2.y());
                g2.draw(slash);
            }
        }
        else{
            //System.out.println("WE GATS THE STUFF");
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int x = (this.getWidth() - image.getWidth(null)) / 2;
        int y = (this.getHeight() - image.getHeight(null)) / 2;
        g2d.drawImage(image, x, y, null);
        }
    }
}
