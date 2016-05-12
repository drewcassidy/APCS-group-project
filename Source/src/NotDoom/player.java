
package NotDoom;

import NotDoom.Map.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;




public class player {
    
    private int ammo, health;    
    private float X,Y,rot;
    
    
    public player(){
        health = 100;
        ammo=50;
        X=0;
        Y=0;
        rot=0;
        
        
        
        
        
        
    }
    
    
    
    public void removeAmmo(int amount){
        ammo-=amount;
    }
    
    public void removeAmmo(){
        removeAmmo(1);
    }
    
    public void addAmmo(int amount){
        ammo+=amount;
    }
    
    public void takeDamage(int amount){
        health-=amount;
    }
    
    public void takeDamage(){
        takeDamage(1);
    }
    
    public void heal(int amount){
        health+=amount;
    }
    
    public int getHealth(){
        return health;
    }
    
    public int getAmmo(){
        return ammo;
    }
    
    public float getX(){
        return X;
    }
    
    public float getY(){
        return Y;
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
