package ch.cpnv.angrywirds.Providers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;

import javax.naming.Context;

import ch.cpnv.angrywirds.AngryWirds;
import ch.cpnv.angrywirds.Models.Data.Assignment;
import ch.cpnv.angrywirds.Models.Data.Language;
import ch.cpnv.angrywirds.Models.Data.MessageBag;
import ch.cpnv.angrywirds.Models.Data.PostAssignmentsDatas;
import ch.cpnv.angrywirds.Models.Data.Vocabulary;
import ch.cpnv.angrywirds.Models.Data.Word;

/**
 * Created by Xavier on 07.06.18.
 */

public abstract class VocProvider {
    private static final String API = "http://voxerver.mycpnv.ch/api/v1/";

    public enum Status { unknown, ready, cancelled, nocnx }
    public static Status status = Status.unknown;

    public static ArrayList<Language> languages;
    public static ArrayList<Vocabulary> vocabularies;
    public static ArrayList<Assignment> assignments = new ArrayList<Assignment>();

    /**
     * Allows you to get a vocabulary by his databse id
     * @param vocabId
     * @return
     */
    static public Vocabulary getVocabulary(int vocabId) {
        // Check the id of each vocabularies
        for(Vocabulary vocab : vocabularies){
            if (vocab.getId() == vocabId) {
                // If the id match, return the vocabulary
                return vocab;
            }
        }
        // For now, return the base vocabulay
        return vocabularies.get(0);
    }

    static public void load() {
        languages = new ArrayList<Language>();
        vocabularies = new ArrayList<Vocabulary>();

        HttpRequestBuilder requestLangues = new HttpRequestBuilder();
        Net.HttpRequest httpRequestLangues = requestLangues.newRequest().method(Net.HttpMethods.GET).url(API+"languages").build();
        Gdx.net.sendHttpRequest(httpRequestLangues, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                JsonReader jsonlangue = new JsonReader();
                JsonValue baselangue = jsonlangue.parse(httpResponse.getResultAsString());
                for (JsonValue langages : baselangue.iterator())
                {
                    languages.add(new Language(langages.getInt("id"),langages.getString("lName")));
                }
            }

            @Override
            public void failed(Throwable t) {
                status = Status.nocnx;
                Gdx.app.log("ANGRY", "No connection", t);
            }

            @Override
            public void cancelled() {
                status = Status.cancelled;
                Gdx.app.log("ANGRY", "cancelled");
            }
        });

        httpRequestLangues = requestLangues.newRequest().method(Net.HttpMethods.GET).url(API+"fullvocs").build();
        Gdx.net.sendHttpRequest(httpRequestLangues, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                JsonReader jreader = new JsonReader();
                JsonValue vocs = jreader.parse(httpResponse.getResultAsString());
                for (JsonValue voc : vocs.iterator())
                {
                    Vocabulary newvoc = new Vocabulary(voc.getInt("mId"),voc.getString("mTitle"),voc.getInt("mLang1"),voc.getInt("mLang2"));
                    for (JsonValue word : voc.get("Words").iterator())
                    {
                        newvoc.addWord(new Word(word.getInt("mId"), word.getString("mValue1"), word.getString("mValue2")));
                    }
                    vocabularies.add(newvoc);
                }
            }

            @Override
            public void failed(Throwable t) {
                status = Status.nocnx;
                Gdx.app.log("ANGRY", "No connection", t);
            }

            @Override
            public void cancelled() {
                status = Status.cancelled;
                Gdx.app.log("ANGRY", "cancelled");
            }
        });

        // loads the assignements of the user
        getMyAssignements();

    }

    /**
     * Gets all the assignments for the current user
     * Authenticated by his token.
     */
    static public void getMyAssignements () {
        // PREPARE THE REQUEST
        HttpRequestBuilder requestAssignments = new HttpRequestBuilder();
        Net.HttpRequest httpRequestAssignments = requestAssignments
                .newRequest()
                .method(Net.HttpMethods.GET)
                .url(API+"assignments/"+ AngryWirds.API_AUTHENTICATION_TOKEN)
                .build();
        // SEND THE REQUEST TO THE SERVER
        Gdx.net.sendHttpRequest(httpRequestAssignments, new Net.HttpResponseListener() {

            // Parse the response
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                // Check errors
                if (httpResponse.getStatus().getStatusCode() == 200) {
                    // The request is success
                    JsonReader jsonReader = new JsonReader();
                    JsonValue assignmentsDatas = jsonReader.parse(httpResponse.getResultAsString());
                    // Iterates over the assignments returned by the server
                    for (JsonValue assignment : assignmentsDatas.iterator())
                    {
                        assignments.add(new Assignment(
                                assignment.getInt("assignment_id"),
                                assignment.getInt("vocabulary_id"),
                                assignment.getString("title"),
                                assignment.getString("result")
                        ));
                    }
                    // Display message to the user
                    MessageBag.setMessage("Devoirs correctement chargé", 5);
                    // Pass the status to ready to open the assignment page
                    status = Status.ready;
                } else {
                    // The server returns an http error
                    status = Status.nocnx;
                    MessageBag.setMessage("Les devoirs ne se sont pas chargés", 5);
                    Gdx.app.log("GETASSIGNEMENTS", "Erreur http :" + httpResponse.getStatus().getStatusCode());
                }
            }

            @Override
            public void failed(Throwable t) {
                status = Status.nocnx;
                MessageBag.setMessage("Les devoirs ne se sont pas chargés", 5);
                Gdx.app.log("ANGRY", "No connection", t);
            }

            @Override
            public void cancelled() {
                status = Status.cancelled;
                MessageBag.setMessage("Les devoirs ne se sont pas chargés", 5);
                Gdx.app.log("ANGRY", "cancelled");
            }
        });
    }

    /**
     * Allows you to submits the results of our session
     * @param vocabularyId
     * @param score
     */
    static public void submitResults (int vocabularyId, int score) {
        Gdx.app.log("AJAXPOST", "Appel ajax demandé");

        // Gets the assignment id
        int assignmentId = 0;
        for(Assignment assign : assignments) {
            // Check if a assignment have the corresponding vocabulary id
            if (assign.getVocabulary_id() == vocabularyId) {
                assignmentId = assign.getAssignment_id();
            } else {
                assignmentId = 0;
                Gdx.app.log("ASSIGNMENT_ID", "No vocabulary for this assignment");
            }
        }

        // Create the http request builder
        HttpRequestBuilder requestSubmitResults = new HttpRequestBuilder();

        // Generate the json with double quotes
        PostAssignmentsDatas datas = new PostAssignmentsDatas(assignmentId, AngryWirds.API_AUTHENTICATION_TOKEN, score);
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonDatas = json.toJson(datas);

        // Create the request with the right datas
        Net.HttpRequest httpRequestSubmitResults = requestSubmitResults.newRequest()
                .method(Net.HttpMethods.POST)
                // Headers for laravel
                .header("Content-Type", "application/json")
                .header("X-Requested-With", "XmlHttpRequest")
                // Json content
                .content(jsonDatas)
                .url(API+"result")
                .build();

        // Send the request
        Gdx.net.sendHttpRequest(httpRequestSubmitResults, new Net.HttpResponseListener() {

            // Ok
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (httpResponse.getStatus().getStatusCode() == 200) {
                    MessageBag.setMessage("Resultats correctements envoyés au serveur", 5);
                } else {
                    MessageBag.setMessage("Une erreur est survenue lors de l'envoi du score.", 5);
                }
            }

            @Override
            public void failed(Throwable t) {
                status = Status.nocnx;
                Gdx.app.log("AJAXPOST", "No connection", t);
            }

            @Override
            public void cancelled() {
                status = Status.cancelled;
                Gdx.app.log("AJAXPOST", "cancelled");
            }
        });
    }

}
