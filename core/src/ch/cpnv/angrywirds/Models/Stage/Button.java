package ch.cpnv.angrywirds.Models.Stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Button {

    private String content;
    // Allows you to store an id for button identification
    private int id;
    private Sprite sprite;
    private BitmapFont font;

    /**
     * Constructor
     * @param content
     * @param id
     * @param x
     * @param y
     * @param width
     * @param height
     * @param path
     */
    public Button (String content, int id, float x, float y, float width, float height, String path) {
        this.content = content;
        this.id = id;
        sprite = new Sprite(new Texture(path));
        sprite.setBounds(x, y, width, height);
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
        font.draw(batch, content, sprite.getX()+50, sprite.getY()+50);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getId() {
        return id;
    }
}
