package ch.cpnv.angrywirds.Models.Data;

import com.badlogic.gdx.Gdx;

/**
 * Assignments
 */
public class Assignment {

    private int assignment_id;
    private int vocabulary_id;
    private String title;
    private String result;

    public Assignment (int assignment_id, int vocabulary_id, String title, String result) {
        this.assignment_id = assignment_id;
        this.vocabulary_id = vocabulary_id;
        this.title = title;
        this.result = result;
    }

    /**
     * Generates a pretty message depending the state of this assignment
     * @return String
     */
    public String getMessage () {
        if (this.result == null) {
            return "Jamais joué";
        } else {
            return "Score : " + this.result;
        }
    }

    public int getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
    }

    public int getVocabulary_id() {
        return vocabulary_id;
    }

    public void setVocabulary_id(int vocabulary_id) {
        this.vocabulary_id = vocabulary_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
