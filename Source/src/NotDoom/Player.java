package NotDoom;
public class Player {
    
    private int ammo, health,ammoTimer;  
    private Vector pos;
    private float rot;
    private float height;
    private final int moveSpeed=30;
    
    
    public Player(Vector pos){
        health = 100;
        ammo = 50;
        this.pos = pos;
        rot = ( float ) (Math.PI / 4);
        ammoTimer = 30;
        height = 15;
    }
    
    public void update(){
        decAmmoTime();
    }
    
    public boolean inSights(Enemy e){
        float ex = e.getX();
        float ey = e.getY();
        float dx = ex - pos.x();
        float dy = ey - pos.y();
        
        //int theta = (int)()
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
}
