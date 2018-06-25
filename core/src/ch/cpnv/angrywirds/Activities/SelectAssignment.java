package ch.cpnv.angrywirds.Activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import ch.cpnv.angrywirds.AngryWirds;
import ch.cpnv.angrywirds.Models.Data.Assignment;
import ch.cpnv.angrywirds.Models.Data.MessageBag;
import ch.cpnv.angrywirds.Models.Stage.Button;
import ch.cpnv.angrywirds.Providers.VocProvider;

public class SelectAssignment extends GameActivity implements InputProcessor {

    private Texture background;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private MessageBag messageBag;

    public SelectAssignment ()
    {
        super();
        background = new Texture(Gdx.files.internal("background.png"));
        // The base position to start generating buttons (for assignment listing)
        int basePos = 300;
        for (Assignment assignment: VocProvider.assignments){
            // Create a little message for the score or the not traineds vocabularies
            buttons.add(
                    new Button(
                            assignment.getTitle() + " - " + assignment.getMessage(),
                            assignment.getVocabulary_id(),
                            WORLD_WIDTH/2-250,
                            basePos,
                            500,
                            80,
                            "button.png"
                    )
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
        Gdx.app.log("TUTU", "Touched");
        Vector3 positionTouched = camera.unproject(new Vector3(screenX, screenY, 0));
        Gdx.app.log("TUTU", screenX + "   " + screenY);
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
