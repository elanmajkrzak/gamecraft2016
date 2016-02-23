package gamecraft_2016;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
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
    private Background background;
    private boolean active = false;
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
			this.setActive(false);
		}

	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(float alpha) {
        /* Clear drawing area */
        renderer.clear();

        /* Draw game objects */
        renderer.begin();
        background.render(renderer, bg_texture, Color.WHITE);
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
        /* Initialize game objects */
        background = new Background(Color.WHITE, bg_texture, 0f, 0f, width, height );
        
        music.loadFile("res/Sound/GameMusic/menu.wav", 0);
        music.play();

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);

	}
	
	

	@Override
	public void exit() {
        bg_texture.delete();
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

}
