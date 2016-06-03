/*
 * Copyright 2016 16cassidyandrew.
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
package NotDoom.Renderer;
import NotDoom.IntVector;
import NotDoom.Map.*;
import NotDoom.Player;
import NotDoom.Vector;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author 16cassidyandrew
 */
public class DoomRenderer {

    private BufferedImage buffer;
    private int[] backBuffer;
    private final int WIDTH;
    private final int HEIGHT;
    private ArrayList<Wall> walls;
    private final int MAPSCALE = 10;
    private final float FOV = 0.8f;
    private final float CLIP = 0.1f;
    private final int STRIPCLIP = 50;
    private final int ZSCALE = 10;
    private final int TEXSCALE = 8;
    private final int PLANEDIST = 50;

    public DoomRenderer(BufferedImage buffer) {
        this.buffer = buffer;
        WIDTH = buffer.getWidth();
        HEIGHT = buffer.getHeight();
        backBuffer = new int[WIDTH * HEIGHT];
        walls = new ArrayList<>();
        
        
    }



    public void DrawRegionRecursive(Region r, Player p, int minx, int maxx, int depth) { 
        if (r == null) return;
        if (depth == 0) {
            p.setHeight(r.getFloor() + 5);
        }
        if (depth > 3) {
            return;
        }
        Wall[] walls = r.getWalls();
        for (int i = 0; i < walls.length; i++) {
            Region neighbor = r.getNeighbor(i);
            DrawWallRecursive(walls[i], p, minx, maxx, r.getFloor(), r.getCeiling(), neighbor, r.getFloorTex(), r.getCeilingTex(), depth);
            if (neighbor != null) {
                if (neighbor.getFloor() > r.getFloor()) {
                    DrawWallRecursive(walls[i], p, minx, maxx, r.getFloor(), neighbor.getFloor(), null, r.getFloorTex(), null, depth);
                } else {
                    DrawWallRecursive(walls[i], p, minx, maxx, r.getFloor(), r.getFloor(), null, r.getFloorTex(), null, depth);
                }
                if (neighbor.getCeiling() < r.getCeiling()) {
                    DrawWallRecursive(walls[i], p, minx, maxx, neighbor.getCeiling(), r.getCeiling(), null, null, r.getCeilingTex(), depth);
                } else { 
                    DrawWallRecursive(walls[i], p, minx, maxx, r.getCeiling(), r.getCeiling(), null, null, r.getCeilingTex(), depth);
                }
            }
        }
    }

    public void DrawPixel(int x, int y, int c) {
        if (x >= 0 && x <= WIDTH && y >= 0 && y <= HEIGHT) {
            backBuffer[x + y * WIDTH] = c;
        }
    }

    public void DrawColumn(int x, int y1, int y2, int height, int offset, BufferedImage texture) {
        int screenHeight = (y2 - y1);
        float texelsPerPixel = (float) height / (float) screenHeight;
        int dy = (y1 < 0) ? y1 * -1 : 0;
        int texY = (int) (dy * texelsPerPixel);
        float tError = dy * texelsPerPixel % 1;
        int texX = (offset + texture.getWidth()) % texture.getWidth();
        int texHeight = texture.getHeight();

        while(dy <= screenHeight && y1 + dy < HEIGHT) {
            DrawPixel(x, y1 + dy, texture.getRGB(texX, texY % texHeight));
            tError += texelsPerPixel;
            texY += (int) tError;
            tError %= 1;
            dy++;
        }
    }

    public void DrawWallRecursive(Wall w, Player p, int minx, int maxx, int floor, int ceiling, Region neighbor, BufferedImage floorTex, BufferedImage ceilingTex, int depth) {
        Vector v1 = p.worldToLocal(w.v1());
        Vector v2 = p.worldToLocal(w.v2());
        float trim1 = 1;
        float trim2 = 1;

        if (v1.y() < CLIP) {
            trim1 = (CLIP - v2.y()) / (v2.y() - v1.y());
            float x = v2.x() + (v2.x() - v1.x()) * trim1; 
            v1 = new Vector(x, CLIP);
        }

        if (v2.y() < CLIP) {
            trim2 = (CLIP - v1.y()) / (v1.y() - v2.y());
            float x = v1.x() + (v1.x() - v2.x()) * trim2;
            v2 = new Vector(x, CLIP);
        }

        int x1 = (int) ((WIDTH / 2) + (float) (WIDTH / 2) * v1.x() / (v1.y() * FOV));
        int x2 = (int) ((WIDTH / 2) + (float) (WIDTH / 2) * v2.x() / (v2.y() * FOV));

        if (neighbor == null) {
            int bot1 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * (p.getHeight() - floor) / (v1.y() * FOV * ZSCALE));
            int bot2 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * (p.getHeight() - floor) / (v2.y() * FOV * ZSCALE));
            int top1 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * (p.getHeight() - ceiling) / (v1.y() * FOV * ZSCALE));
            int top2 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * (p.getHeight() - ceiling) / (v2.y() * FOV * ZSCALE));
            float d1 = v1.y();
            float d2 = v2.y();
            float length = (TEXSCALE * ZSCALE * w.length());

            for (int dx = (x1 < minx) ? (minx - x1) : 0; dx <= x2 - x1 && dx + x1 < WIDTH && dx + x1 < maxx; dx++) {
                float step = ((float) dx / (float) (x2 - x1));
                float tStep = step * Math.abs(trim2) * Math.abs(trim1) + (1 - Math.abs(trim1));
                int bot = (int) (bot1 + step * (bot2 - bot1));
                int top = (int) (top1 + step * (top2 - top1));
                int u = (int) (((1 - tStep) * (TEXSCALE * (1 - Math.abs(trim1)) / d1) + (tStep * (length / d2))) / ((1 - tStep) * (1 / d1) + tStep * (1 / d2)));
                if (dx + x1 >= minx && dx + x1 < maxx) {
                    DrawColumn(dx + x1, top, bot, TEXSCALE * (ceiling - floor), u, w.getTexture());
                    if (ceilingTex != null) {
                        drawStrip(dx + x1, top, (int) p.getHeight() - ceiling, p.getX() * TEXSCALE * ZSCALE  / FOV * -1, p.getY() * TEXSCALE * ZSCALE * FOV, ceilingTex, p.getRot());
                    }
                    if (floorTex != null) {
                        drawStrip(dx + x1, bot, (int) p.getHeight() - floor, p.getX() * TEXSCALE * ZSCALE  / FOV * -1, p.getY() * TEXSCALE * ZSCALE * FOV , floorTex, p.getRot());
                    }
                }
                if (step > 0.9) {
                    int n = (int) Math.sqrt(15);
                }
            }
        } else {
            if (x1 > maxx || x2 < minx) return;
            x1 = Math.max(x1, minx);
            x2 = Math.min(x2, maxx);
            DrawRegionRecursive(neighbor, p, x1, x2, depth + 1);
        }
    }
    
    public void drawStrip(int x, int maxy, int h, float u1, float vOffset, BufferedImage texture, float rot) {
        //float d2 = Math.abs((FOV * ZSCALE * 2 * Math.abs(y - (HEIGHT / 2))) / (WIDTH * h));
        maxy = Math.min(maxy, HEIGHT - 1 - maxy);
        float d1 = (float) Math.abs((float) h / (float) ZSCALE / FOV / (float) TEXSCALE);
        float d2 = PLANEDIST;
        float u2 = ((d2 - d1) * TEXSCALE * ZSCALE * 10);
        float v1 = (x - (WIDTH / 2)) / 4;
        int y1 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * -1 * Math.abs(h) / (d1 * FOV * ZSCALE * TEXSCALE));
        int y2 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * -1 * Math.abs(h) / (d2 * FOV * ZSCALE * TEXSCALE));
        int length = Math.abs(y1 - y2);
        float sin = (float) Math.sin(rot);
        float cos = (float) Math.cos(rot); //store trig values for more fastness

        for (int dy = (y1 < 0) ? (y1 * -1) : 0; dy + y1 < y2 && y1 + dy < HEIGHT && dy + y1 < maxy; dy++) {
            float step = (float) (dy) / (float) length;
            float u = correctUV(step, 0, u2, d1, d2);
            float v = v1 / (1.0f - step);
            float vRot = (u * cos - v * sin) - vOffset;
            float uRot = (u * sin + v * cos) + u1;
            int color = texture.getRGB((int) Math.floorMod((int) vRot, texture.getWidth()), (int) Math.floorMod((int) uRot, texture.getHeight()));
            DrawPixel(x, (h <= 0) ? y1 + dy : HEIGHT - 1 - (y1 + dy), color);
        }
    }
    
    public void DrawMap(Map m){
        Region [] r = m.getRegions();
              
        for (int i = 0; i < r.length; i++){
            walls.addAll(r[i].getSolidWalls());
        }
        
        for (int j = 0; j < walls.size(); j++){
            IntVector v1, v2;
            v1 = walls.get(j).v1();
            v2 = walls.get(j).v2();
            
            DrawLine(10 + v1.x() * MAPSCALE, 10 + v2.x() * MAPSCALE, 10 + v1.y() * MAPSCALE, 10 + v2.y() * MAPSCALE, 0xFFFFFF);
        }

        Player p = m.getPlayer();
        IntVector v = new IntVector((int) (MAPSCALE * p.getPos().x()), (int) (MAPSCALE * p.getPos().y()));
        drawBox(v.x() + 7, v.y() + 7, 6, 6, 0xFF0000);
        DrawLine(10 + v.x(), (int) (10 + Math.sin(p.getRot()) * 10) + v.x(), 10 + v.y(), (int) (10 + Math.cos(p.getRot()) * -10) + v.y(), 0xFF0000);
    }

    public void DrawLine(int x1, int x2, int y1, int y2, int color){
        int dx = x2 - x1;
        int dy = y2 - y1;
        
        if (dx != 0) {
            float error = -1.0f;
            float dError = Math.abs((float) dy / (float) dx);
            if (dError < 1) {
                if (x1 > x2) {
                    int temp = x1;
                    x1 = x2;
                    x2 = temp;
                    temp = y1;
                    y1 = y2;
                    y2 = temp;
                }
                int y = y1;
                for (int x = x1; x < x2; x++) {
                    DrawPixel(x, y, color);
                    error += dError;
                    while (error >=  0 ) {
                        y += Math.signum(y2 - y1);
                        error -= 1;
                    }
                }
            } else {
                if (y1 > y2) {
                    int temp = x1;
                    x1 = x2;
                    x2 = temp;
                    temp = y1;
                    y1 = y2;
                    y2 = temp; 
                }
                int x = x1;
                for (int y = y1; y < y2; y++) {
                    DrawPixel(x, y, color);
                    error += (1 / dError);
                    while (error >= 0) {
                        x += Math.signum(x2 - x1);
                        error -= 1;
                    }
                }
            }
        } else {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp; 
            }
            for (int y = y1; y <= y2; y++) {
                DrawPixel(x1, y, color);
            }
        }
    }

    public void drawBox(int x, int y, int l, int h, int color) {
        for (int dx = 0; dx <= l; dx++) {
            for (int dy = 0; dy <= h; dy++) {
                DrawPixel(x + dx, y + dy, color);
            }
        }
    }

    public void DrawFrame() {
        buffer.setRGB(0, 0, WIDTH, HEIGHT, backBuffer, 0, WIDTH);
    }
    public void clearFrame() {
        for (int i = 0; i < backBuffer.length; i++) {
            backBuffer[i] = 0;
        }
    }

    private float correctUV(float step, float u1, float u2, float d1, float d2) {
        return (((((1 - step) * (u1 / d1))) + (step * (u2 / d2))) / (((1 - step) / d1) + (step / d2)));
    }
}
