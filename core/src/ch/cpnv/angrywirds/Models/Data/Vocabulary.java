package ch.cpnv.angrywirds.Models.Data;

import java.util.ArrayList;

import ch.cpnv.angrywirds.AngryWirds;
import ch.cpnv.angrywirds.Providers.VocProvider;

/**
 * Created by Xavier on 07.06.18.
 */

public class Vocabulary {
    int id;
    String vocName;
    int langprof;
    int langeleve;
    ArrayList<Word> words;

    public Vocabulary(int id, String vocName, int langprof, int langeleve){
        this.id = id;
        this.vocName = vocName;
        this.langprof = langprof;
        this.langeleve = langeleve;
        this.words = new ArrayList<Word>();
    }

    public void addWord(Word w) {
        words.add(w);
    }

    public Word pickAWord() {
        return words.get(AngryWirds.alea.nextInt(words.size()));
    }

    public String getVocName() {
        return vocName;
    }

    public int getId() {
        return id;
    }
}
