package gamecraft_2016;

import silvertiger.tutorial.lwjgl.game.Entity;
import silvertiger.tutorial.lwjgl.graphic.Color;
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;

public class Image extends Entity {

	public Image(Color color, Texture texture, float x, float y, float speed, int width, int height, int tx, int ty) {
		super(color, texture, x, y, speed, width, height, tx, ty);
	}
	
	public Image(Texture texture, float x, float y) {
		super(Color.WHITE, texture, x, y, 0, 20, 100, 0, 0);
	}

	@Override
	public int input(Entity entity) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    public void render(Renderer renderer, float x, float y) {
        renderer.begin();
        texture.bind();
        renderer.drawTexture(texture, x, y, Color.WHITE);
        renderer.end();
    }

}
