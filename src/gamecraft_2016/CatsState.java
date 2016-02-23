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

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL10;

import silvertiger.tutorial.lwjgl.graphic.Color;
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.opengl.GL11.glClearColor;
/**
 * This class contains a simple game.
 *
 * @author Heiko Brumme
 */
public class CatsState implements State {

    private Texture bg_texture;
    private Texture girl_texture;
    private Texture boy_texture;
    private final Renderer renderer;
    private static final int total_steps = 6;
    private int girl_steps = 0;
    private int boy_steps = 0;

    private Background background;
    private Cat girl;
    private Cat boy;
    private int[] girl_moves = new int[total_steps];
    private int[] boy_moves = new int[total_steps];
    //initialize to something we won't set it as
    private int last_move = -1;
    
    private boolean moveable = false;
	private int gameWidth;
	private int gameHeight;
	private boolean active = false;
	private Audio music;
    
    private static final String[] girl_textures = {"res/girlleft.png", "res/girlright.png", "res/girlup.png"};
    private static final String[] boy_textures = {"res/boyleft.png", "res/boyright.png", "res/boyup.png"};

    public CatsState(Renderer renderer, Audio music) {
        this.renderer = renderer;
        this.music = music;
    }

    @Override
    public void input() {
    	if (moveable) {
    		int temp = boy.input();
    		if (temp != last_move) {
    			last_move = temp;
    		}
    	}
    	else if (!moveable && girl_steps < total_steps) {
    		int temp = girl.input();
    		if (temp != last_move) {
    			last_move = temp;
    		}
    	}
    	else if (girl_steps == total_steps) {
    		moveable = true;
    	}
    	

    	//CHEATS
        long window = GLFW.glfwGetCurrentContext();
		if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
			//do a thing
			this.setActive(false);
		}
    }

    @Override
    public void render(float alpha) {
        /* Clear drawing area */
        renderer.clear();

        /* Draw game objects */
        renderer.begin();
        background.render(renderer, bg_texture, new Color(0.792f, 0.714f, 0.82f));
        
        renderer.end();

        boy.render(renderer, 760f, -25f);
        girl.render(renderer, 450f, 0f);
        
        if (moveable) {
            String danceText = "DANCE";
            int danceTextWidth = renderer.getTextWidth(danceText);
            int danceTextHeight = renderer.getTextHeight(danceText);
            float danceTextX = (gameWidth - danceTextWidth) / 2f;
            float danceTextY = gameHeight - danceTextHeight - 5;
            renderer.drawText(danceText, danceTextX, danceTextY, Color.BLACK);
        }
    }

    @Override
    public void enter() {
    	
        /* Get width and height of framebuffer */
        long window = GLFW.glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();

        /* Load textures */
        bg_texture = Texture.loadTexture("res/KlubBackground.png");
        boy_texture = Texture.loadTexture("res/boyleft.png");
        girl_texture = Texture.loadTexture("res/girlleft.png");

        /* Initialize game objects */
        background = new Background(new Color(0.792f, 0.714f, 0.82f), bg_texture, 0f, 0f, width, height );
        boy = new Cat(Color.WHITE, boy_texture, boy_textures, width, height, true);
        girl = new Cat(Color.WHITE, girl_texture, girl_textures, width, height, false);
        AL10.alGetError();
        music.loadFile("res/Sound/GameMusic/gameplay.wav", 1);
        music.play();
        
        gameWidth = width;
        gameHeight = height;

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
    }

    @Override
    public void exit() {
    	girl_texture.delete();
        boy_texture.delete();
    	boy.delete();
    	girl.delete();
        bg_texture.delete();
        music.stop();
    }

	@Override
	public void update(float delta) {
    	System.out.println("moveable:" + moveable + " boy_steps:" + boy_steps + " girl_steps:" + girl_steps + " total_steps:" + total_steps + " last_move:" + last_move);
		if (moveable) {
			if (boy_steps < total_steps) {
				if (boy_steps >= 0) {
					boy_moves[boy_steps] = last_move;
				}
				else {
					boy_moves[total_steps] = last_move;
				}
				boy_steps++;
			}
			else {
				moveable = false;
				boy_steps = 0;
				girl_steps = 0;
				last_move = -1;
			}
		}
		else {
			girl.setTexture(last_move);
			if (last_move >= 0 && girl_steps < total_steps) {
				if (girl_steps >= 0) {
					girl_moves[girl_steps] = last_move;
				} else {
					girl_moves[total_steps] = last_move;
				}
				girl_steps++;
			}
			else {
				moveable = true;
				boy_steps = 0;
				girl_steps = 0;
			}
		}
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}
}
