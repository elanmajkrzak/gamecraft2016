package gamecraft_2016;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwWaitEvents;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import silvertiger.tutorial.lwjgl.graphic.Color;
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;

public class StartState implements State {

    private final Renderer renderer;
    private Texture bg_texture;
    private Texture story_texture;
    private Background background;
    private boolean active = false;
    private boolean storyShown = false;
	private Audio music;

	public StartState(Renderer renderer, Audio music) {
		this.renderer = renderer;
		this.music = music;
	}

	@Override
	public void input() {
        long window = GLFW.glfwGetCurrentContext();
		if (glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS) {
			//do a thing
			if (storyShown) {
				this.setActive(false);
			} else {
				background.update(story_texture);
				storyShown = true;
			}
		}

	}

	@Override
	public void render(float alpha) {
        /* Clear drawing area */
        renderer.clear();

        /* Draw game objects */
        renderer.begin();
        if (storyShown) {
        	background.render(renderer, story_texture, Color.WHITE);
        } else {
        	background.render(renderer, bg_texture, Color.WHITE);
        }
        glfwWaitEvents();
        renderer.end();

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
        bg_texture = Texture.loadTexture("res/startscreen.png");
        story_texture = Texture.loadTexture("res/Storyscreen.png");
        /* Initialize game objects */
        background = new Background(Color.WHITE, bg_texture, 0f, 0f, width, height );
        
        music.loadFile("res/Sound/GameMusic/menu.wav", 0);
        music.play();

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);

	}
	
	

	@Override
	public void exit() {
		System.out.println("exit StartState");
        bg_texture.delete();
        story_texture.delete();
        music.stop();
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

}
