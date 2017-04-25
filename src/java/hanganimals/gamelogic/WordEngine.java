package hanganimals.gamelogic;

import hanganimals.models.MultiplayerUser;
import java.util.ArrayList;
import java.util.Random;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WordEngine {
    
    public static ArrayList<String> words = getWordsFromDR();
    
    private static ArrayList<String> getWordsFromDR() {
        ArrayList<String> temp = new ArrayList<>();
        Client client = ClientBuilder.newClient();
        Response res = client.target("http://www.dr.dk/mu-online/api/1.3/list/view/mostviewed?channeltype=TV&limit=3&offset=0")
                .request(MediaType.APPLICATION_JSON).get();
        String svar = res.readEntity(String.class);
        
        try {
            //Parse svar som et JSON-objekt
            JSONParser parser = new JSONParser();

            JSONObject json1 = (JSONObject) parser.parse(svar);
            JSONArray json2 = (JSONArray) json1.get("Items");
            ArrayList<String> slug = new ArrayList<>();
            String allWords = "";
            
            for (int i = 0; i < json2.size(); i++) {
                JSONObject innerJson = (JSONObject) json2.get(i);
                slug.add(innerJson.get("Slug").toString());
                res = client.target("http://www.dr.dk/mu-online/api/1.3/programcard/" + slug.get(i)).request(MediaType.APPLICATION_JSON).get();
                svar = res.readEntity(String.class);
                JSONObject json3 = (JSONObject) parser.parse(svar);
                allWords += json3.get("Description").toString();
            }
            
           
            String[] words = allWords.split(" ");
            System.out.println("Antallet af ord er: " + words.length);
            for (String word : words) {
                word = word.toLowerCase()
                        .replace(",", "")
                        .replace(".", "").replace("-", "")
                        .replace("?", "").replace("%", "")
                        .replaceAll("[0-9]", "").replace("!", "")
                        .replace("/", "")
                        .replaceAll(" [a-zæøå] ", " ") // fjern 1-bogstavsord
                        .replaceAll(" [a-zæøå][a-zæøå] ", " "); // fjern 2-bogstavsord;
                if (!word.startsWith(" ")) {
                    temp.add(word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    public static String getWord() {
        return words.get(new Random().nextInt(words.size()));
    }
}
