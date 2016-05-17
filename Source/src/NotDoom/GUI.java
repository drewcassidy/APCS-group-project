
package NotDoom;


import NotDoom.Renderer.ImageComponent;
import java.awt.event.*;

import javax.swing.*;

public class GUI{
    
    private JFrame frame;
    
    private JPanel panel;
    // |
    // | 
    // |       |
    // |-------|
    // |  |  |
    // |  |  |
    //
    private JLabel ammo, health, armor;

    private ImageComponent image;

    private boolean running;
    private int tick, tickTimer;
    
    private int key;
    
    private KeyListener keys;
    
    
    public GUI(){
        frame = new JFrame();
        frame.setSize(800,600);
        frame.setTitle("DOOM");
        
        //image = new ImageComponent();
        
        ammo = new JLabel("0");
        health = new JLabel("100%");
        armor = new JLabel("0%");
        
        
        keys = new Key();
        
        panel = new JPanel();
        panel.add(ammo);
        panel.add(health);
        panel.add(armor);
       
        //panel.add(image);
        panel.addKeyListener(keys);
        panel.setFocusable(true);
        frame.add(panel);
        
        
        tickTimer = 60;
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        
        run();
        
    }
    

        
    private class Key implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            key = e.getKeyCode();
            System.out.println("typed");
        }

        @Override
        public void keyReleased(KeyEvent e) {
            key = -1;
        
        }
            
    }
    
    public void tick(){
        
        tick++;
        if (tick > tickTimer){
            
            tick = 0;
            System.out.println("Word");
            //label.setText(key+"");
            
            
            
            
            
        }
        
    }
    
    public void render(){
        
        panel.removeAll();
        
        panel.add(ammo);
        
        ammo.setLocation(20, 500);
        //panel.add(image);
        panel.invalidate();
        panel.validate();
        panel.repaint();
        
    }
    
    
     public void run(){
        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        //display fps (2 lines)
        long timer = 0;
        int ticks = 0;
        
        running = true;
        
        //main game loop
        while (running == true){
            
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            //display fps (1 line)
            timer += now - lastTime;
            lastTime = now;
            if (delta >= 1){
                
                tick();
                render();
                
            //display fps(1 line)
                ticks++;
                delta = 0;
            
            }
            //display fps
            if (timer >= 1000000000){
                System.out.println("Ticks and Frames " + ticks);
                ticks = 0;
                timer = 0;
            }
            
            
        }
        //create YOU LOST GUI
        JOptionPane.showMessageDialog(null, "YOU LOST");
        int again = JOptionPane.showConfirmDialog(null, "Do you want to play again?");
        if (again==JOptionPane.YES_OPTION){
            running =true;
            
            //reset variables
            
            tickTimer=60;
            run();
        }
        
            
    }
     
     
    
    
    
    
}