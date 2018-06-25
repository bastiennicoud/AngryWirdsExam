package ch.cpnv.angrywirds.Activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ch.cpnv.angrywirds.AngryWirds;
import ch.cpnv.angrywirds.Models.Data.MessageBag;
import ch.cpnv.angrywirds.Models.Stage.ScoreBoard;
import ch.cpnv.angrywirds.Models.Stage.Title;
import ch.cpnv.angrywirds.Providers.VocProvider;

/**
 * Created by Xavier on 10.06.18.
 */

public class GameOver extends GameActivity {

    private Texture background;
    private Title title;
    private MessageBag messageBag;

    public GameOver(int vocabularyId)
    {
        super();
        background = new Texture(Gdx.files.internal("background.png"));
        title = new Title("Game Over\n score: "+ ScoreBoard.score);
        messageBag = new MessageBag();

        // Sends the results of the session to the server
        VocProvider.submitResults(vocabularyId, ScoreBoard.score);
    }


    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched())
        {
            AngryWirds.gameActivityManager.pop(); // game over
            AngryWirds.gameActivityManager.pop(); // play
            AngryWirds.gameActivityManager.pop();
            AngryWirds.gameActivityManager.push(new SelectVocabulary());
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        title.draw(spriteBatch);
        messageBag.draw(spriteBatch);
        spriteBatch.end();
    }
}
