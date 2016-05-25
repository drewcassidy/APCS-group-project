package NotDoom;
public class Player {
    
    private int ammo, health;  
    private Vector pos;
    private float rot;
    private float height;
    
    
    public Player(Vector pos){
        health = 100;
        ammo = 50;
        this.pos = pos;
        rot = 0;
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
