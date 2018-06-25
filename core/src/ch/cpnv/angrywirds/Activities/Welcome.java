package ch.cpnv.angrywirds.Activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ch.cpnv.angrywirds.AngryWirds;
import ch.cpnv.angrywirds.Models.Data.MessageBag;
import ch.cpnv.angrywirds.Models.Stage.Title;
import ch.cpnv.angrywirds.Providers.VocProvider;

/**
 * Created by Xavier on 10.06.18.
 */

public class Welcome extends GameActivity{
    private Texture background;
    private Title title;
    private float splashTime = 2;
    private Sprite wheel1;
    private Sprite wheel2;
    private MessageBag messageBag;

    public Welcome()
    {
        super();
        background = new Texture(Gdx.files.internal("background.png"));
        title = new Title("Angry Wirds");
        wheel1 = new Sprite(new Texture("cog.png"));
        wheel2 = new Sprite(new Texture("cog.png"));
        wheel1.setBounds(camera.viewportWidth/2 - 45, camera.viewportHeight/4,100,100);
        wheel2.setBounds(camera.viewportWidth/2 + 45, camera.viewportHeight/4,100,100);
        wheel1.setOrigin(50,50);
        wheel2.setOrigin(50,50);
        wheel2.setRotation(15);
        messageBag = new MessageBag();
        VocProvider.load();
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        wheel1.rotate(1);
        wheel2.rotate(-1);
        if (splashTime > 0)
            splashTime -= dt;
        else
            if (VocProvider.status == VocProvider.Status.ready)
                AngryWirds.gameActivityManager.push(new SelectAssignment());
    }

    @Override
    public void render() {
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        title.draw(spriteBatch);
        wheel1.draw(spriteBatch);
        wheel2.draw(spriteBatch);
        messageBag.draw(spriteBatch);
        spriteBatch.end();
    }
}
