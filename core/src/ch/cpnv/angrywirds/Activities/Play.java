package ch.cpnv.angrywirds.Activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import ch.cpnv.angrywirds.AngryWirds;
import ch.cpnv.angrywirds.Models.Data.Vocabulary;
import ch.cpnv.angrywirds.Models.Data.Word;
import ch.cpnv.angrywirds.Models.Stage.Bird;
import ch.cpnv.angrywirds.Models.Stage.Board;
import ch.cpnv.angrywirds.Models.Stage.Bubble;
import ch.cpnv.angrywirds.Models.Stage.PhysicalObject;
import ch.cpnv.angrywirds.Models.Stage.Pig;
import ch.cpnv.angrywirds.Models.Stage.RubberBand;
import ch.cpnv.angrywirds.Models.Stage.Scenery;
import ch.cpnv.angrywirds.Models.Stage.ScoreBoard;
import ch.cpnv.angrywirds.Models.Stage.TNT;
import ch.cpnv.angrywirds.Models.Stage.Wasp;
import ch.cpnv.angrywirds.Providers.VocProvider;

public class Play extends GameActivity implements InputProcessor {

    public static final int FLOOR_HEIGHT = 120;
    private static final int SLINGSHOT_WIDTH = 75;
    private static final int SLINGSHOT_HEIGHT = 225;
    private static final int SLINGSHOT_OFFSET = 100; // from left edge
    public static final int TWEETY_START_X = SLINGSHOT_OFFSET + (SLINGSHOT_WIDTH - Bird.WIDTH) / 2;
    public static final int TWEETY_START_Y = FLOOR_HEIGHT + SLINGSHOT_HEIGHT - Bird.HEIGHT;
    private static final float ELASTICITY = 6f;
    private final int SCORE_BUMP_SUCCESS = 7;
    private final int SCORE_BUMP_FAIL = 1;
    private final int TNT_PENALTY = 5;

    private Scenery scenery;
    private Bird tweety;
    private Wasp waspy;
    private ArrayList<Bubble> babble;

    private Texture background;
    private Texture slingshot1;
    private Texture slingshot2;
    private Board board;
    private ScoreBoard scoreBoard;
    private RubberBand rubberBand1;
    private RubberBand rubberBand2;

    private Queue<Touch> actions;
    private Vocabulary vocabulary; // The vocabulary we train

    public Play(int vocabularyId) {
        super();

        babble = new ArrayList<Bubble>();
        // Get the right vocabulary from the voc provider
        vocabulary = VocProvider.getVocabulary(vocabularyId);

        background = new Texture(Gdx.files.internal("background.png"));
        slingshot1 = new Texture(Gdx.files.internal("slingshot1.png"));
        slingshot2 = new Texture(Gdx.files.internal("slingshot2.png"));

        tweety = new Bird();
        tweety.freeze(); // it won't fly until we launch
        rubberBand1 = new RubberBand();
        rubberBand2 = new RubberBand();

        waspy = new Wasp(new Vector2(WORLD_WIDTH / 2, WORLD_HEIGHT / 2), new Vector2(20, 20));
        scenery = new Scenery();
        for (int i = 5; i < WORLD_WIDTH / 50; i++) {
            try {
                scenery.addElement(new PhysicalObject(new Vector2(i * 50, FLOOR_HEIGHT), 50, 50, "block.png"));
            } catch (Exception e) {
                Gdx.app.log("ANGRY", "Could not add block to scenery");
            }
        }
        for (int i = 0; i < 2; i++) {
            try {
                scenery.addElement(new TNT(new Vector2(AngryWirds.alea.nextInt(WORLD_WIDTH * 2 / 3) + WORLD_WIDTH / 3, FLOOR_HEIGHT + 50), TNT_PENALTY));
            } catch (Exception e) {
                Gdx.app.log("ANGRY", "Could not add TNT to scenery");
            }
        }
        for (int i = 0; i < 8; i++) {
            try {
                scenery.addElement(new Pig(new Vector2(AngryWirds.alea.nextInt(WORLD_WIDTH * 2 / 3) + WORLD_WIDTH / 3, FLOOR_HEIGHT + 50), vocabulary.pickAWord()));
            } catch (Exception e) {
                Gdx.app.log("ANGRY", "Could not add Pig to scenery");
            }
        }

        board = new Board(scenery.pickAWord()); // Put one word from a pig on the board
        scoreBoard = new ScoreBoard(70, 240);

        Gdx.input.setInputProcessor(this);
        actions = new LinkedList<Touch>(); // User inputs are queued in here when events fire, handleInput processes them

    }

    public void handleInput() {
        Touch action;
        while ((action = actions.poll()) != null) {
            switch (action.type) {
                case down:
                    if (tweety.isFrozen() && action.point.x < TWEETY_START_X && action.point.y >= FLOOR_HEIGHT && action.point.y < TWEETY_START_Y) {
                        tweety.getSprite().setX(action.point.x);
                        tweety.getSprite().setY(action.point.y);
                    }

                    Pig piggy = scenery.pigTouched(action.point.x, action.point.y);
                    if (piggy != null)
                        babble.add(new Bubble(piggy.getPosition().x, piggy.getPosition().y, piggy.getWordValue(), 2));
                    break;
                case up:
                    if (tweety.isFrozen() && action.point.x < TWEETY_START_X && action.point.y >= FLOOR_HEIGHT && action.point.y < TWEETY_START_Y) {
                        tweety.setSpeed(new Vector2(100 + (TWEETY_START_X - action.point.x) * ELASTICITY, 100 + (TWEETY_START_Y - action.point.y) * ELASTICITY));
                        tweety.unFreeze();
                    }
                    break;
                case drag:
                    if (tweety.isFrozen() && action.point.x < TWEETY_START_X && action.point.y >= FLOOR_HEIGHT && action.point.y < TWEETY_START_Y) {
                        tweety.getSprite().setX(action.point.x);
                        tweety.getSprite().setY(action.point.y);
                    }
                    break;
            }
        }
    }

    public void update(float dt) {
        // --------- Bird
        tweety.accelerate(dt);
        tweety.move(dt);
        PhysicalObject hit = scenery.collidesWith(tweety);
        if (hit != null) {
            String c = hit.getClass().getSimpleName();
            // elsifs instead of switch, just to keep out of JDK version trouble
            if (c.equals("TNT")) {
                scoreBoard.scoreChange(-((TNT) hit).getNegativePoints());
            } else if (c.equals("Pig")) {
                Pig p = (Pig)hit;
                if (p.getWord().getId() == board.getWordId()) { // Correct answer
                    scoreBoard.scoreChange(SCORE_BUMP_SUCCESS);
                    p.setWord(vocabulary.pickAWord());
                    board.setWord(scenery.pickAWord());
                } else {
                    scoreBoard.scoreChange(-SCORE_BUMP_FAIL);
                }
            }
            tweety.reset();
        }

        // --------- Wasp
        waspy.accelerate(dt);
        waspy.move(dt);
        if (tweety.collidesWith(waspy)) {
            scoreBoard.scoreChange(-100);
            AngryWirds.gameActivityManager.push(new GameOver(vocabulary.getId()));
        }
        if (tweety.getSprite().getX() > WORLD_WIDTH - Bird.WIDTH) tweety.reset();

        // --------- Bubbles
        for (int i = babble.size() - 1; i >= 0; i--) { // we go reverse, so that removing items does not affect the rest of the loop
            babble.get(i).ageAway(dt);
            if (babble.get(i).isDead()) babble.remove(i);
        }

        // --------- Rubberbands
        rubberBand1.between(new Vector2(tweety.getSprite().getX() + 20, tweety.getSprite().getY() + 10), new Vector2(SLINGSHOT_OFFSET + SLINGSHOT_WIDTH, SLINGSHOT_HEIGHT + FLOOR_HEIGHT - 40));
        rubberBand2.between(new Vector2(tweety.getSprite().getX() + 20, tweety.getSprite().getY() + 10), new Vector2(SLINGSHOT_OFFSET + 15, SLINGSHOT_HEIGHT + FLOOR_HEIGHT - 40));

        // --------- Scoreboard
        scoreBoard.update(dt);
        if (scoreBoard.gameOver())
            AngryWirds.gameActivityManager.push(new GameOver(vocabulary.getId()));
    }

    @Override
    public void render() {
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        board.draw(spriteBatch);
        scoreBoard.draw(spriteBatch);
        spriteBatch.draw(slingshot1, SLINGSHOT_OFFSET, FLOOR_HEIGHT, SLINGSHOT_WIDTH, SLINGSHOT_HEIGHT);
        if (tweety.isFrozen()) // Some things are only displayed while aiming
        {
            for (Bubble b : babble) b.draw(spriteBatch);
            rubberBand1.draw(spriteBatch);
        }
        tweety.draw(spriteBatch);
        if (tweety.isFrozen())
            rubberBand2.draw(spriteBatch);
        waspy.draw(spriteBatch);
        scenery.draw(spriteBatch);
        spriteBatch.draw(slingshot2, SLINGSHOT_OFFSET, FLOOR_HEIGHT, SLINGSHOT_WIDTH, SLINGSHOT_HEIGHT);
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
        Vector3 pointTouched = camera.unproject(new Vector3(screenX, screenY, 0)); // Convert from screen coordinates to camera coordinates
        actions.add(new Touch(pointTouched, Touch.Type.down));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 pointTouched = camera.unproject(new Vector3(screenX, screenY, 0)); // Convert from screen coordinates to camera coordinates
        actions.add(new Touch(pointTouched, Touch.Type.up));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 pointTouched = camera.unproject(new Vector3(screenX, screenY, 0)); // Convert from screen coordinates to camera coordinates
        actions.add(new Touch(pointTouched, Touch.Type.drag
        ));
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
