
package NotDoom;

import NotDoom.Map.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;




public class Player {
    
    private int ammo, health, key;    
    private Vector pos;
    private float rot;
    private Key keys;
    
    
    
    public Player () {
        health = 100;
        ammo = 50;
        rot=0;
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
    
    public void removeAmmo(int amount) {
        ammo-=amount;
    }
    
    public void removeAmmo() {
        removeAmmo(1);
    }
    
    public void addAmmo(int amount) {
        ammo += amount;
    }
    
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }
    
    public void takeDamage() {
        takeDamage(1);
    }
    
    public void heal(int amount) {
        health += amount;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getAmmo() {
        return ammo;
    }

    public Vector getPos() {
        return pos;
    }
    
    public void look(float deg){
        rot=deg;
    }
    
    public void lookLeft(float deg){
        rot+=deg;
        rot = (rot+360) % 360;
    }
    
    public void lookLeft(){
        lookLeft(1);
    }
    
    public void lookRight(float deg){
        rot-=deg;
        rot = (rot+360) % 360;
    }
    
    public void lookRight(){
        lookRight(1);
    }
}
