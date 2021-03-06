
package NotDoom;


import NotDoom.Map.*;
import NotDoom.Renderer.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

import javax.swing.*;

public class GUI{
    private final int FPS = 60;
    private JFrame frame;
    
    private Player p;
    
    private ArrayList<Enemy> e;
    
    private JPanel mainPanel, screen, hotBar, ammoPanel, healthPanel;
    
    private Map m;
    
    private JLabel ammo, health, armor;

    private InfoScreen image, image2;
    private MainScreen mainScreen;
    
    private int WIDTH = 800, HEIGHT = 600;

    private boolean running, tab;
    private int tick, tickTimer;
    
    private int key;
    
    //private boolean 
    
    private KeyListener keys;
    
    private GridBagConstraints c;

    private DoomRenderer renderer;
    private BufferedImage buffer;
    
    
    public GUI(Map m){
        this.m = m;
        c = new GridBagConstraints();
        
        frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.setTitle("DOOM");
        
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        mainScreen = new MainScreen(buffer, m.getRegions());
       
        renderer = new DoomRenderer(buffer);

        image = new InfoScreen("nb.png");
        image2 = new InfoScreen("nb.png");
        
        ammo = new JLabel("0");
        health = new JLabel("100%");
        armor = new JLabel("0%");
        
        
        keys = new Key();
        
        healthPanel = new JPanel();
        //healthPanel.add(health);
        healthPanel.add(image);
        
        ammoPanel = new JPanel();
        //ammoPanel.add(ammo);
        ammoPanel.add(image2);
                
        //hotBar = new JPanel();
        //hotBar.setLayout(new GridLayout(1,2));
        //hotBar.add(healthPanel);
        //hotBar.add(ammoPanel);
        
        screen = new JPanel();
        screen.add(mainScreen);
        
        mainPanel = new JPanel();
        //mainPanel.setLayout(new GridBagLayout());
        //c.gridx = 0;
        //c.gridy = 0;
        mainPanel.add(screen, c);
        
        
        //panel.add(image);
        mainPanel.addKeyListener(keys);
        mainPanel.setFocusable(true);
        frame.add(mainPanel);
        
        
        tickTimer = 60;
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        
        //create player
        p = m.getPlayer();
        
        //create enemys
        e = new ArrayList<>();
        e.add(new Enemy (0, 0));
        e.add(new Enemy (10, 10));
        e.add(new Enemy (20, 20));
        e.add(new Enemy (30, 30));

        
        run();
        
    }
    
    

        
    private class Key implements KeyListener{
        //wasd left right space shoot 
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            key = e.getKeyCode();
            if (key == KeyEvent.VK_U){
                tab = true;
                mainScreen.tabed(true);
            }
            
            p.giveKey(key, true);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            
            key = e.getKeyCode();
            if (key == KeyEvent.VK_U){

                tab = false;
                mainScreen.tabed(false);
            }
            
            p.giveKey(key, false);

        }
            
    }
    
    public ArrayList<Enemy> getEnemys(){
        return e;
    }
  

    public void tick(){
        
        tick++;
        p.update();
        if (tick > tickTimer){
            
            //loop through sprite update
            
            tick = 0;
        }
        
    }
    
    /*
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        Rectangle box = new Rectangle ( 50, 20, 100, 75);
        g2.draw(box);
    }
    */
    
    public void render(){
        
        //mainPanel.removeAll();
        if(tab == true){
            renderer.DrawMap(m);
        }
        else {
            renderer.DrawRegionRecursive(m.currentRegion(), p, 0, WIDTH, 0);
        }
        renderer.DrawFrame();
        //healthPanel.add(health);
        //healthPanel.add(image);
        
        //ammoPanel.add(ammo);
        //ammoPanel.add(image2);

        //hotBar.setLayout(new GridLayout(1,2));
        //hotBar.add(healthPanel);
        //hotBar.add(ammoPanel);
        
            
        //screen.add(mainScreen);
        
/*
        //mainPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();        
        c.fill = GridBagConstraints.NONE;
        
        c.gridx = 0;
        c.gridy = 0;
        
        c.gridwidth = 2;
        //c.gridheight = 2;
        c.ipady = HEIGHT-100;
        c.ipadx = WIDTH;
        
        mainPanel.add(screen, c);
        
        //c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 300;
        c.ipadx = WIDTH/2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        mainPanel.add(healthPanel,c);
        
        //c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        mainPanel.add(ammoPanel,c);
    */ 
        mainPanel.invalidate();
        mainPanel.validate();
        mainPanel.repaint();
        renderer.clearFrame();
    }
    
    
     public void run(){
        double timePerTick = 1000000000 / FPS;
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
            /*
            //display fps
            if (timer >= 1000000000){
                System.out.println("Ticks and Frames " + ticks);
                ticks = 0;
                timer = 0;
            }
                    */
            
            
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

