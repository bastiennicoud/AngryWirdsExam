package ch.cpnv.angrywirds.Models.Stage;

import com.badlogic.gdx.math.Vector2;

import ch.cpnv.angrywirds.Activities.Play;

/**
 * Created by Xavier on 06.05.18.
 */

public abstract class MovingObject extends PhysicalObject {

    public final static float G = 250f; // Gravity, for objects that fall

    protected Vector2 speed;
    protected boolean frozen; // Allows to temporarily freeze the movement

    public MovingObject(Vector2 position, float width, float height, String picname, Vector2 speed) {
        super(position, width, height, picname);
        this.speed = speed;
    }

    // the accelerate method implements the speed change, which depends on the physics of the derived object, reason why it is abstract here
    public abstract void accelerate(float dt);

    public void freeze() {
        this.frozen = true;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void unFreeze() {
        this.frozen = false;
    }

    // Make the object move according to its own speed
    public final void move(float dt)
    {
        if (!frozen) {
            this.sprite.translate(speed.x * dt, speed.y * dt);
            if (getPosition().x < 0 || getPosition().x > Play.WORLD_WIDTH || getPosition().y < Play.FLOOR_HEIGHT || getPosition().y > Play.WORLD_HEIGHT) // Hit screen boundary
                stop(); // Calm down !!!
        }
    }

    public final void stop()
    {
        speed.x = 0; // Calm down
        speed.y = 0;
    }

    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    @Override
    public final String toString()
    {
        return getClass().getSimpleName()+" at ("+this.sprite.getX()+","+this.sprite.getY()+"), moving at ("+speed.x+","+speed.y+")";
    }

}
