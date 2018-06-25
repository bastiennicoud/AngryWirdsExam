package ch.cpnv.angrywirds.Models.Stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Xavier on 12.05.18.
 */

public final class Bubble {

    private static final String PICNAME = "bubble.png";
    private static final int WIDTH = 260;
    private static final int HEIGHT = 160;
    private static final int BUBBLE_OFFSET = 20; // to center the spike on the head
    private static final int TEXT_OFFSET_X = 40; // to place the text inside the bubble
    private static final int TEXT_OFFSET_Y = 100;

    private String message;
    private float timeLeft; // life time in seconds
    private Sprite sprite;
    private BitmapFont font;

    /**
     * Bubble will appear for the pig located at x,y
     *
     * @param x
     * @param y
     * @param message
     * @param duration
     */
    public Bubble(float x, float y, String message, int duration) {
        this.message = message;
        this.timeLeft = duration;
        sprite = new Sprite(new Texture(PICNAME));
        sprite.setBounds(x-WIDTH/2-BUBBLE_OFFSET, y+Pig.HEIGHT, WIDTH, HEIGHT);
        font= new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
    }

    public void ageAway(float dt)
    {
        timeLeft -= dt;
    }

    public boolean isDead() {
        return timeLeft <= 0;
    }

    public void draw(Batch batch)
    {
        sprite.draw(batch);
        font.draw(batch, message, sprite.getX()+TEXT_OFFSET_X, sprite.getY()+TEXT_OFFSET_Y);
    }
}
