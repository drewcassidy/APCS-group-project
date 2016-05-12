/*
 * Copyright 2016 16kohnegrant.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package NotDoom;

public class Enemy extends Sprite{
    
    
    private int health;    
    private Vector pos;
    
    
    public Enemy(){
        health = 100;
        
        pos= new Vector(0,0);
    }
    
    
    public void takeDamage(int amount){
        health-=amount;
    }
    
    public void takeDamage(){
        takeDamage(1);
    }
    
    public int getHealth(){
        return health;
    }
    
    public float getX(){
        return pos.x();
    }
    
    public float getY(){
        return pos.y();
    }
}
