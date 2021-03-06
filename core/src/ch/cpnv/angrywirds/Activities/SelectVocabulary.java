package ch.cpnv.angrywirds.Activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import ch.cpnv.angrywirds.AngryWirds;
import ch.cpnv.angrywirds.Models.Data.MessageBag;
import ch.cpnv.angrywirds.Models.Data.Vocabulary;
import ch.cpnv.angrywirds.Models.Stage.Button;
import ch.cpnv.angrywirds.Providers.VocProvider;


/**
 * This view allows you to select a vocabulary
 */
public class SelectVocabulary extends GameActivity implements InputProcessor {

    private Texture background;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private MessageBag messageBag;

    public SelectVocabulary ()
    {
        super();
        background = new Texture(Gdx.files.internal("background.png"));
        int basePos = 300;

        for (Vocabulary vocabulary: VocProvider.vocabularies){
            buttons.add(
                    new Button(vocabulary.getVocName(), vocabulary.getId(), WORLD_WIDTH/2-150, basePos, 300, 80, "button.png")
            );
            basePos += 100;
        }
        messageBag = new MessageBag();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        for (Button button: buttons){
            button.draw(spriteBatch);
        }
        messageBag.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 positionTouched = camera.unproject(new Vector3(screenX, screenY, 0));
        // Check if a button is touched
        for (Button buttonn: buttons) {
            if (buttonn.getSprite().getBoundingRectangle().contains(new Vector2(positionTouched.x, positionTouched.y))) {
                AngryWirds.gameActivityManager.push(new Play(buttonn.getId()));
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
