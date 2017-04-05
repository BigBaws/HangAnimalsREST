package hanganimals.gamelogic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SinglePlayerLogic {
    
    private ArrayList<String> muligeOrd = new ArrayList<String>();
    private String ordet;
    private ArrayList<String> brugteBogstaver = new ArrayList<>();
    private String synligtOrd;
    private int antalForkerteBogstaver;
    private boolean sidsteBogstavVarKorrekt;
    private boolean spilletErVundet;
    private boolean spilletErTabt;
    
    public SinglePlayerLogic() {
        setWordFromDR();
        opdaterSynligtOrd();
    }
    
    public void setWordFromDR() {
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
                    muligeOrd.add(word);
                }
            }
            System.out.println("mulige ord: " + muligeOrd);
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        nulstil();
    }
    
    public ArrayList<String> getBrugteBogstaver() {
        return brugteBogstaver;
    }
    
    public String getSynligtOrd() {
        return synligtOrd;
    }
    
    public String getOrdet() {
        return ordet;
    }
    
    public int getAntalForkerteBogstaver() {
        return antalForkerteBogstaver;
    }
    
    public boolean erSidsteBogstavKorrekt() {
        return sidsteBogstavVarKorrekt;
    }
    
    public boolean erSpilletVundet() {
        return spilletErVundet;
    }
    
    public boolean erSpilletTabt() {
        return spilletErTabt;
    }
    
    public boolean erSpilletSlut() {
        return spilletErTabt || spilletErVundet;
    }
    
    public ArrayList<String> getMuligeOrd() {
        return muligeOrd;
    }
    
    public void setMuligeOrd(ArrayList<String> muligeOrd) {
        this.muligeOrd = muligeOrd;
    }
    
    public void nulstil() {
        brugteBogstaver.clear();
        antalForkerteBogstaver = 0;
        spilletErVundet = false;
        spilletErTabt = false;
        ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
        opdaterSynligtOrd();
    }
    
    public void opdaterSynligtOrd() {
        synligtOrd = "";
        spilletErVundet = true;
        for (int n = 0; n < ordet.length(); n++) {
            String bogstav = ordet.substring(n, n + 1);
            if (brugteBogstaver.contains(bogstav)) {
                synligtOrd = synligtOrd + bogstav;
            } else {
                synligtOrd = synligtOrd + "*";
                spilletErVundet = false;
            }
        }
    }
    
    public void gætBogstav(String bogstav) {
        if (bogstav.length() != 1) return;
        System.out.println("Der gættes på bogstavet: " + bogstav);
        if (brugteBogstaver.contains(bogstav)) return;
        if (spilletErVundet || spilletErTabt) return;
        
        brugteBogstaver.add(bogstav);
        
        if (ordet.contains(bogstav)) {
            sidsteBogstavVarKorrekt = true;
            System.out.println("Bogstavet var korrekt: " + bogstav);
        } else {
            // Vi gættede på et bogstav der ikke var i ordet.
            sidsteBogstavVarKorrekt = false;
            System.out.println("Bogstavet var IKKE korrekt: " + bogstav);
            antalForkerteBogstaver = antalForkerteBogstaver + 1;
            if (antalForkerteBogstaver >= 6) {
                spilletErTabt = true;
            }
        }
        opdaterSynligtOrd();
    }
    
    public void logStatus() {
        System.out.println("---------- ");
        System.out.println("- ordet (skult) = " + ordet);
        System.out.println("- synligtOrd = " + synligtOrd);
        System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver);
        System.out.println("- brugeBogstaver = " + brugteBogstaver);
        if (spilletErTabt) System.out.println("- SPILLET ER TABT");
        if (spilletErVundet) System.out.println("- SPILLET ER VUNDET");
        System.out.println("---------- ");
    }
    
    
    public String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }
    
    public void hentOrdFraDr() throws Exception {
        String data = hentUrl("http://dr.dk");
        System.out.println("data = " + data);
        
        data = data.substring(data.indexOf("<body")).
                replaceAll("<.+?>", " ").toLowerCase().replaceAll("[^a-zæøå]", " ").
                replaceAll(" [a-zæøå] ", " "). // fjern 1-bogstavsord
                replaceAll(" [a-zæøå][a-zæøå] ", " "); // fjern 2-bogstavsord
        System.out.println("data = " + data);
        muligeOrd.clear();
        muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));
        
        System.out.println("muligeOrd = " + muligeOrd);
        nulstil();
    }
    
    public void setOrdet(String ordet) {
        this.ordet = ordet;
    }
    
    public void saveData(String name, int gamescore, boolean win){
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(new FileReader("json/" + name + ".json"));
            if(win){
                int wins = Integer.parseInt(json.get("vundet").toString());
                json.put("vundet", wins + 1);
                int highscore = Integer.parseInt(json.get("highscore").toString());
                if (gamescore > highscore) {
                    json.put("highscore", gamescore);
                }
            } else {
                int losses = Integer.parseInt(json.get("tabt").toString());
                json.put("tabt", losses + 1);
            }
            try (FileWriter file = new FileWriter("json/" + name + ".json")) {
			file.write(json.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + json);
		}
        } catch (FileNotFoundException ex) {
                Logger.getLogger(SinglePlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SinglePlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SinglePlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String[] getData(String name){
        String[] data = new String[6];
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(new FileReader("json/" + name + ".json"));
            String studname = json.get("name").toString();
            String studnr = json.get("studienummer").toString();
            String studret = json.get("studieretning").toString();
            String wins = json.get("vundet").toString();
            String losses = json.get("tabt").toString();
            String highscore = json.get("highscore").toString();
            data[0] = studname;
            data[1] = studnr;
            data[2] = studret;
            data[3] = wins;
            data[4] = losses;
            data[5] = highscore;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SinglePlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(SinglePlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SinglePlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
