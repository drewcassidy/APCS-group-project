
package NotDoom;


import NotDoom.Map.Map;
import NotDoom.Renderer.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class GUI{
    
    private JFrame frame;
    
    private JPanel mainPanel, screen, hotBar, ammoPanel, healthPanel;
    
    private Map m;
    
    private JLabel ammo, health, armor;

    private MainScreen image, image2, mainScreen, mapScreen;
    
    private int WIDTH = 800, HEIGHT = 600;

    private boolean running, tab;
    private int tick, tickTimer;
    
    private int key;
    
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
        mainScreen = new MainScreen(buffer);
        renderer = new DoomRenderer(buffer);

        image = new MainScreen("nb.png");
        image2 = new MainScreen("nb.png");
        mapScreen = new MainScreen("blank.png");
        
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
        mainPanel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(screen, c);
        
        //mainPanel.add(hotBar, c);
        
        
        //panel.add(image);
        mainPanel.addKeyListener(keys);
        mainPanel.setFocusable(true);
        frame.add(mainPanel);
        
        
        tickTimer = 60;
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        
        
        run();
        
    }
    

        
    private class Key implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            key = e.getKeyCode();
            if (key == KeyEvent.VK_U){
                mapScreen.tabed(true);
            }
            System.out.println("typed");
        }

        @Override
        public void keyReleased(KeyEvent e) {
            key = -1;
            tab = false;
            mapScreen.tabed(false);
        
        }
            
    }
    
    public void tick(){
        
        tick++;
        if (tick > tickTimer){
            
            //ammo.setLocation(20, 500);
            
            tick = 0;
            System.out.println("Word");
            //label.setText(key+"");
        }
        
    }
    
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        Rectangle box = new Rectangle ( 50, 20, 100, 75);
        g2.draw(box);
    }
    
    
    public void render(){
        
        
        mainPanel.removeAll();
        
        if (tab == true){
            mainPanel.add(mapScreen);
        }
        
        else {
            renderer.DrawRegion(m.currentRegion());
            renderer.DrawPixel(15, tick, 0xFF00FF);
            renderer.DrawFrame();

            //healthPanel.add(health);
            healthPanel.add(image);
            
            //ammoPanel.add(ammo);
            ammoPanel.add(image2);

            //hotBar.setLayout(new GridLayout(1,2));
            //hotBar.add(healthPanel);
            //hotBar.add(ammoPanel);
            
            screen.add(mainScreen);

            mainPanel.setLayout(new GridBagLayout());
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
        }
         
        mainPanel.invalidate();
        mainPanel.validate();
        mainPanel.repaint();
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