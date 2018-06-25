package ch.cpnv.angrywirds.Models.Stage;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

import ch.cpnv.angrywirds.AngryWirds;
import ch.cpnv.angrywirds.Models.Data.Word;

/**
 * Contains all the static items to display in our world
 * Created by Xavier on 12.05.18.
 */

public final class Scenery {

    private ArrayList<PhysicalObject> scene;
    private ArrayList<Pig> pigs; // keep a filtered list to speed un word selection

    public Scenery() {
        scene = new ArrayList<PhysicalObject>();
        pigs = new ArrayList<Pig>();
    }

    public void addElement (PhysicalObject el) throws Exception
    {
        for (PhysicalObject o : scene)
            if (el.collidesWith(o))
                throw new Exception("No can do !!!!");
        scene.add(el);
        if (el.getClass().equals(Pig.class))
            pigs.add((Pig)el);
    }

    public void draw(Batch batch)
    {
        for (PhysicalObject p : scene) p.draw(batch);
    }

    /**
     * Returns the object of the scenary that object o has hit (null if none)
     * @param o
     * @return
     */
    public PhysicalObject collidesWith(PhysicalObject o)
    {
        for (PhysicalObject el : scene)
            if (el.collidesWith(o))
                return el;
        return null;
    }

    /**
     * Returns the pig at the given location - if any (null if none)
     *
     * @param x
     * @param y
     * @return
     */
    public Pig pigTouched(float x, float y)
    {
        for (PhysicalObject el : scene)
            if (el.getClass().equals(Pig.class)) // we only care about Pigs
                if (el.getRectangle().contains(x,y))
                    return (Pig)el; // must cast because el is declared as PhysicalObject
        return null;
    }

    /**
     *  A smart scenery is capable of returning one of the words that is hidden in one of its pigs
     * @return
     */
    public Word pickAWord()
    {
        return pigs.get(AngryWirds.alea.nextInt(pigs.size())).getWord();
    }
}
