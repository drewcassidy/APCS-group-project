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
    //private final int moveSpeed=30;

    
    
    public Player(Vector pos){//, Map m){
        this.m = m;
        health = 100;
        ammo = 50;
        this.pos = pos;
        rot = ( float ) (Math.PI / 4);
        ammoTimer = 30;
        height = 15;
        characterMap = new HashMap();
        characterMap.put(KeyEvent.VK_A, false);
        characterMap.put(KeyEvent.VK_W, false);
        characterMap.put(KeyEvent.VK_S, false);
        characterMap.put(KeyEvent.VK_D, false);
        characterMap.put(KeyEvent.VK_LEFT, false);
        characterMap.put(KeyEvent.VK_RIGHT, false);
        characterMap.put(KeyEvent.VK_SPACE, false);
    }
    
    public void update(){
        decAmmoTime();
        System.out.println("updating p");
        System.out.println(characterMap.get(KeyEvent.VK_A) + " THIS IS THE AAAAAAAAAAAAA");
        if (characterMap.get(KeyEvent.VK_A)){
            moveForward((float)(Math.PI*1.5));
            System.out.println("a");
        }
        if (characterMap.get(KeyEvent.VK_W))
            moveForward(0);
        if (characterMap.get(KeyEvent.VK_S))
            moveForward((float)Math.PI);
        if (characterMap.get(KeyEvent.VK_D))
            moveForward((float)(Math.PI*.5));
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
        System.out.println(k + " " + b);
        
    }
    
    public boolean inSights(Enemy e){      
        
        Vector v = worldToLocal(new Vector(e.getX(),e.getY()));
        if(v.x() < e.width() / 2){
            return true;
        }
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
    
    public void moveForward(float dir){
        pos.moveX((float)(-1 * Math.cos(rot + dir - Math.PI / 2) / moveSpeed));
        pos.moveY((float)(Math.sin(rot + dir - Math.PI / 2) / moveSpeed));
        System.out.println("i moved");
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
    
    public void lookLeft(float drot){
        rot += drot;
        rot %= (float) (Math.PI * 2);
    }
    
    public void lookLeft(){
        lookLeft(0.1f);
    }
    
    public void lookRight(float drot){
        rot -= drot;
        rot = (float) (rot + Math.PI * 2) % (float) (Math.PI * 2);
    }
    
    public void lookRight(){
        lookRight(0.1f);
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }
    
    public float getRot() {
        return rot;
    }

    public Vector worldToLocal(IntVector v) {
        Vector v1 = new Vector(pos.x() - v.x(), pos.y() - v.y());
        double angle = Math.atan2(v1.y(), v1.x());
        return new Vector(v1.magnitude() * (float) Math.cos(angle + rot), v1.magnitude() * (float) Math.sin(angle + rot));
    }
    public Vector worldToLocal(Vector v) {
        Vector v1 = new Vector(pos.x() - v.x(), pos.y() - v.y());
        double angle = Math.atan2(v1.y(), v1.x());
        return new Vector(v1.magnitude() * (float) Math.cos(angle + rot), v1.magnitude() * (float) Math.sin(angle + rot));
    }
}
