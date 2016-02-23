/*
 * The MIT License (MIT)
 *
 * Copyright © 2015, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package gamecraft_2016;

import org.lwjgl.glfw.GLFW;

import silvertiger.tutorial.lwjgl.game.Entity;
import silvertiger.tutorial.lwjgl.graphic.Color;
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

import java.util.Random;


public class Cat extends Entity {

    private final boolean player;
    //this should really be an associative array but whatever
    private Texture[] textures;
    private Texture cur_texture;

    public Cat(Color color, Texture texture, String[] textures, float x, float y, boolean player) {
    	super(color, texture, x, y, 0, 20, 100, 0, 0);
    	this.textures = new Texture[textures.length];
    	for (int i = 0; i < textures.length; i++) {
    		this.textures[i] = Texture.loadTexture(textures[i]);
    	}

    	this.cur_texture = texture;
        this.player = player;
    }

    @Override
    public int input(Entity entity) {
    	int out = -1;
        if (player) {
            long window = GLFW.glfwGetCurrentContext();
            if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
                cur_texture = textures[2];
                out = 2;
            }
            if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
                
            }
            if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
                cur_texture = textures[0];
                out = 0;
            }
            if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
                cur_texture = textures[1];
                out = 1;
            }
        } else {
        	Random randomGenerator = new Random();
        	int randomInt = randomGenerator.nextInt(3);
        	out = randomInt;
        }
        
        return out;
    }
    
    public int input() {
		return input(null);
    	
    }
    
    
    public void render(Renderer renderer, float x, float y) {
        renderer.begin();
        cur_texture.bind();
        renderer.drawTexture(cur_texture, x, y, Color.WHITE);
        renderer.end();
    }
    
    public void delete() {
    	for (int i = 0; i < textures.length; i++) {
    		textures[i].delete();
    	}
    }
    
    public void setTexture(int direction) {
    	cur_texture = textures[direction];
    	if (cur_texture == null) {
    		cur_texture = textures[0];
    	}
    }
}
