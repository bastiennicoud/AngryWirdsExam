package ch.cpnv.angrywirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.Random;

import ch.cpnv.angrywirds.Activities.GameActivityManager;
import ch.cpnv.angrywirds.Activities.Welcome;
import ch.cpnv.angrywirds.Providers.FontProvider;

/**
 * Created by Xavier on 10.06.18.
 */

public class AngryWirds extends Game {

    static public GameActivityManager gameActivityManager = new GameActivityManager();
    public static Random alea; // random generator object. Static for app-wide use

    public static String API_AUTHENTICATION_TOKEN = "*D4FB237BB132D581940F9DE816618355B0769A7F";

    @Override
    public void create () {
        alea = new Random();
        FontProvider.load();
        gameActivityManager.push(new Welcome());
    }

    @Override
    public void render () {
        gameActivityManager.handleInput();
        gameActivityManager.update(Gdx.graphics.getDeltaTime());
        gameActivityManager.render();
    }

    @Override
    public void dispose () {
    }
}
