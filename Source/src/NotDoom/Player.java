package NotDoom;

import NotDoom.Map.Map;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Player {
    
    private int ammo, health,ammoTimer;  
    private Vector pos;
    private float rot;
    private float height;
    private final int moveSpeed=60;
    private HashMap <Integer, Boolean> characterMap;
    private Map m;
    
    
    public Player(Vector pos){//, Map m){
        this.m = m;
        health = 100;
        ammo = 50;
        this.pos = pos;
        rot = 0;
        ammoTimer = 30;
        
        characterMap = new HashMap<Integer, Boolean>();
        characterMap.put(KeyEvent.VK_A, false);
        characterMap.put(KeyEvent.VK_W, false);
        characterMap.put(KeyEvent.VK_S, false);
        characterMap.put(KeyEvent.VK_D, false);
        characterMap.put(KeyEvent.VK_SPACE, false);
        characterMap.put(KeyEvent.VK_LEFT, false);
        characterMap.put(KeyEvent.VK_RIGHT, false);
        
        
    }
    
    public void update(){
        decAmmoTime();
        
        if (characterMap.get(KeyEvent.VK_A))
            moveForward(270);
        if (characterMap.get(KeyEvent.VK_W))
            moveForward(0);
        if (characterMap.get(KeyEvent.VK_S))
            moveForward(180);
        if (characterMap.get(KeyEvent.VK_D))
            moveForward(90);
        if (characterMap.get(KeyEvent.VK_LEFT))
            lookLeft();
        if (characterMap.get(KeyEvent.VK_RIGHT))
            lookRight();
        if (characterMap.get(KeyEvent.VK_SPACE)){
        /*    
            if ( canShoot() == true ){
                
                for ( int i  = 0; i < m.getSprites().length; i++){
                    
                    if( inSights (m.getSprites()[i])){
                        
                    }
                    
                    
                }
                
                
            }
            
           */  
        }
           
        
        
        
    }
    
    public void giveKey(int k, boolean b){
        
        characterMap.replace(k, b);
        
    }
    
    public boolean inSights(Enemy e){
        float ex = e.getX();
        float ey = e.getY();
        float dx = ex - pos.x();
        float dy = ey - pos.y();
        
        float hyp = (float) Math.sqrt(dx * dx + dy * dy);
        float theta1 = (float) Math.asin(dx/hyp);
                
        float theta = (float)(Math.atan(e.width()/hyp));
        
        if( rot >= theta1 - theta && rot <= theta1 + theta)
            return true;
        return false;
        
    }
    
    
    public void decAmmoTime(){
        if (ammoTimer>0)
            ammoTimer--;
    }
    
    public boolean canShoot(){
        if (ammoTimer == 0 && ammo > 0){
            return true;
        }
        
        return false;
    }
    
    public void moveForward(int deg){
        pos.moveX((float)(Math.sin(rot+deg)/moveSpeed));
        pos.moveY((float)(Math.cos(rot+deg)/moveSpeed));
    }
    
    public void removeAmmo(int amount){
        ammo -= amount;
    }
    
    public void removeAmmo(){
        removeAmmo(1);
    }
    
    public void addAmmo(int amount){
        ammo += amount;
    }
    
    public void takeDamage(int amount){
        health -= amount;
    }
    
    public void takeDamage(){
        takeDamage(1);
    }
    
    public void heal(int amount){
        health += amount;
    }
    
    public int getHealth(){
        return health;
    }
    
    public int getAmmo(){
        return ammo;
    }
    
    public Vector getPos(){
        return pos;
    }
    
    public float getX(){
        return pos.x();
    }
    
    public float getY(){
        return pos.y();
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

    public float getHeight() {
        return height;
    }

    public Vector worldToLocal(IntVector v) {
        Vector v1 = new Vector(pos.x() - v.x(), pos.y() - v.y());
        double angle = Math.atan2(v1.y(), v1.x());
        return new Vector(v1.magnitude() * (float) Math.cos(angle), v1.magnitude() * (float) Math.sin(angle));
    }
}
