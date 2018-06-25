package ch.cpnv.angrywirds.Models.Stage;

import com.badlogic.gdx.math.Vector2;

import ch.cpnv.angrywirds.Models.Data.Word;

/**
 * Created by Xavier on 06.05.18.
 */

public final class Pig extends PhysicalObject {

    private static final String PICNAME = "pig.png";
    private static final int WIDTH = 60;
    public static final int HEIGHT = 60; // made public for the bubble placement

    private Word word; // The word of the vocabulary that this pig carries

    public Pig(Vector2 position, Word word) {
        super(position, WIDTH, HEIGHT, PICNAME);
        this.word = word;
    }

    public String getWordValue() {
        return word.getValue2();
    }

    public Word getWord() { return word; }

    public void setWord(Word word) {
        this.word = word;
    }
}
