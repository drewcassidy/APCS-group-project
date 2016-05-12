
package NotDoom;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

/**
 *
 * @author 16cassidyandrew
 */
public class Source {

    /**
     * @param args the command line arguments
     */
    
    private boolean running;
    private int key;
    private static int tick, tickTimer;
    
    public static void main(String[] args) {
        // TODO code application logic here
        tickTimer = 0;
        Frame f = new Frame();
        
        player p = new player();
        
        
        run();
    }
    
    public void tick(){
        
        tick++;
        if (tick>tickTimer){
            
            tick = 0;
            
            
            
        }
        
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
        
        while (running == true){
            
            now=System.nanoTime();
            delta += (now-lastTime) / timePerTick;
            //display fps (1 line)
            timer += now - lastTime;
            lastTime = now;
            if (delta >= 1){
                tick();
                //repaint();
            //display fps(1 line)
                ticks++;
                delta = 0;
            
            }
            
            //display fps
            if (timer >= 1000000000){
                System.out.println("Ticks and Frames "+ ticks);
                ticks = 0;
                timer = 0;
            }
            
            
        }
        //create YOU LOST GUI
        JOptionPane.showMessageDialog(null, "YOU LOST");
        int again = JOptionPane.showConfirmDialog(null, "Do you want to play again?");
        if (again == JOptionPane.YES_OPTION){
            running = true;
            
            //reset varriables
            
            tickTimer = 60;
            run();
        }
        
            
    }
    
    private class Key implements KeyListener{

        //wasd, left right turn, space shoot
        
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            key = e.getKeyCode();
        
        }

        @Override
        public void keyReleased(KeyEvent e) {
            key = -1;
        }
        
    }
    
    
    
    
    
}
