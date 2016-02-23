package gamecraft_2016;

import static org.lwjgl.opengl.GL11.glClearColor;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import silvertiger.tutorial.lwjgl.graphic.Color;
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;

public class VictoryState implements State {

    private final Renderer renderer;
    private Texture bg_texture;
    private Background background;
    private boolean active = false;

	public VictoryState(Renderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void input() {

	}

	@Override
	public void update(float delta) {
       // music.playAsMusic(1.0f, 1.0f, false);
        //SoundStore.get().poll(0);

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
        bg_texture = Texture.loadTexture("res/YEAHH.png");
        /* Initialize game objects */
        background = new Background(Color.WHITE, bg_texture, 0f, 0f, width, height );

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);

	}

	@Override
	public void exit() {
        bg_texture.delete();
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
