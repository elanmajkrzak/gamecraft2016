package gamecraft_2016;

import silvertiger.tutorial.lwjgl.game.Entity;
import silvertiger.tutorial.lwjgl.graphic.Color;
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;

public class Background extends Entity {

    public Background(Color color, Texture texture, float x, float y, int width, int height) {
        super(color, texture, x, y, 0, width, height, 0, 0);
    }
    
    public void render(Renderer renderer, Texture texture, Color c) {
        texture.bind();
        renderer.drawTexture(texture, 0f, 0f, c);
    }

	@Override
	public int input(Entity entity) {
		return 0;
		// TODO Auto-generated method stub
		
	}
}
