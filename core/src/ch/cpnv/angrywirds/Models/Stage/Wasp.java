package ch.cpnv.angrywirds.Models.Stage;


import com.badlogic.gdx.math.Vector2;

import ch.cpnv.angrywirds.Activities.Play;
import ch.cpnv.angrywirds.AngryWirds;

/**
 * Created by Xavier on 06.05.18.
 */

public final class Wasp extends MovingObject {

    private static final int AGITATION = 15; // How sharply speed changes
    private static final String PICNAME = "wasp.png";
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    public Wasp(Vector2 position, Vector2 speed) {
        super(position, WIDTH, HEIGHT, PICNAME, speed);
    }

    @Override
    public void accelerate(float dt) {
        // The wasp only slightly alters its speed at random. It is subject to gravity, but it counters it with its flight
        speed.x += (AngryWirds.alea.nextFloat()-sprite.getX()/ Play.WORLD_WIDTH)*AGITATION; // the closer it is to a border, the higher the chances that acceleration goes the other way
        speed.y += (AngryWirds.alea.nextFloat()-sprite.getY()/ Play.WORLD_HEIGHT)*AGITATION;
    }

}
