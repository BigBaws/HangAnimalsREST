package hanganimals.gamelogic;

import hanganimals.MultiPlayerGame;
import hanganimals.models.MultiplayerUser;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MultiPlayerLogic {
    
    public static String guessLetter(MultiPlayerGame game, String userid, String letter) {
        MultiplayerUser user = game.getUser(userid);
        if (user.usedletters.contains(letter)) {
            return "You have already guessed on "+letter;
        }
        user.usedletters.add(letter);
        if (game.word.contains(letter)) {
            user.lastLetterCorrect = true;
            user.gamescore++;
        } else {
            user.lastLetterCorrect = false;
            user.wrongs = user.wrongs + 1;
            if (user.wrongs >= 6) {
                user.Lost = true;
            }
        }
        return updateVisibleWord(game, userid);
    }
    
    public static String updateVisibleWord(MultiPlayerGame game, String userid) {
        MultiplayerUser user = game.getUser(userid);
        user.userword = "";
        for (int n = 0; n < game.word.length(); n++) {
            String letter = game.word.substring(n, n + 1);
            if (user.usedletters.contains(letter)) {
                user.userword = user.userword + letter;
            } else {
                user.userword = user.userword + "*";
            }
        }
        if (!user.userword.contains("*")) {
            int users = game.getNumberOfFinishedUsers();
            
            if(users == 0)
                user.gamescore += 10;
            else if(users == 1)
                user.gamescore += 5;
            else if(users == 2)
                user.gamescore += 2;
            
            user.Won = true;
            game.gameIsWon = true;
            //game.winner = user.name;
            game.winner = user.userid; 
        }
        return user.userword;
    }
//    
//    public ArrayList<String> getBrugteBogstaver() {
//        return brugteBogstaver;
//    }
//    
//    public String getSynligtOrd() {
//        return synligtOrd;
//    }
//    
//    public String getOrdet() {
//        return word;
//    }
//    
//    public int getAntalForkerteBogstaver() {
//        return antalForkerteBogstaver;
//    }
//    
//    public boolean erSidsteBogstavKorrekt() {
//        return sidsteBogstavVarKorrekt;
//    }
//    
//    public boolean erSpilletVundet() {
//        return spilletErVundet;
//    }
//    
//    public boolean erSpilletTabt() {
//        return spilletErTabt;
//    }
//    
//    public boolean erSpilletSlut() {
//        return spilletErTabt || spilletErVundet;
//    }
//    
//    public ArrayList<String> getMuligeOrd() {
//        return muligeOrd;
//    }
//    
//    public void setMuligeOrd(ArrayList<String> muligeOrd) {
//        this.muligeOrd = muligeOrd;
//    }
//    
//    public void nulstil() {
//        brugteBogstaver.clear();
//        antalForkerteBogstaver = 0;
//        spilletErVundet = false;
//        spilletErTabt = false;
//        
//        opdaterSynligtOrd();
//    }
//    
//
//    
//    public void guessLetter(MultiPlayerGame game, String userid, String letter) {
//        MultiplayerUser user = game.getUser(userid);
//        if (user.usedletters.contains(letter)) {
//            return;
//        }
//        user.usedletters.add(letter);
//        if (game.word.contains(letter)) {
//            user.lastLetterCorrect = true;
//        } else {
//            user.lastLetterCorrect = false;
//            user.wrongs = user.wrongs + 1;
//            if (user.wrongs >= 6) {
//                user.Lost = true;
//            }
//        }
//        opdaterSynligtOrd();
//    }
//    
//    public void logStatus() {
//        System.out.println("---------- ");
//        System.out.println("- ordet (skult) = " + word);
//        System.out.println("- synligtOrd = " + synligtOrd);
//        System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver);
//        System.out.println("- brugeBogstaver = " + brugteBogstaver);
//        if (spilletErTabt) System.out.println("- SPILLET ER TABT");
//        if (spilletErVundet) System.out.println("- SPILLET ER VUNDET");
//        System.out.println("---------- ");
//    }
//    
//    
//    public String hentUrl(String url) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
//        StringBuilder sb = new StringBuilder();
//        String linje = br.readLine();
//        while (linje != null) {
//            sb.append(linje + "\n");
//            linje = br.readLine();
//        }
//        return sb.toString();
//    }
//    
//    public void hentOrdFraDr() throws Exception {
//        String data = hentUrl("http://dr.dk");
//        System.out.println("data = " + data);
//        
//        data = data.substring(data.indexOf("<body")).
//                replaceAll("<.+?>", " ").toLowerCase().replaceAll("[^a-zæøå]", " ").
//                replaceAll(" [a-zæøå] ", " "). // fjern 1-bogstavsord
//                replaceAll(" [a-zæøå][a-zæøå] ", " "); // fjern 2-bogstavsord
//        System.out.println("data = " + data);
//        muligeOrd.clear();
//        muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));
//        
//        System.out.println("muligeOrd = " + muligeOrd);
//        nulstil();
//    }
//    
//    public void setOrdet(String ordet) {
//        this.word = ordet;
//    }
//    
//    public void saveData(String name, int gamescore, boolean win){
//        try {
//            JSONParser parser = new JSONParser();
//            JSONObject json = (JSONObject) parser.parse(new FileReader("json/" + name + ".json"));
//            if(win){
//                int wins = Integer.parseInt(json.get("vundet").toString());
//                json.put("vundet", wins + 1);
//                int highscore = Integer.parseInt(json.get("highscore").toString());
//                if (gamescore > highscore) {
//                    json.put("highscore", gamescore);
//                }
//            } else {
//                int losses = Integer.parseInt(json.get("tabt").toString());
//                json.put("tabt", losses + 1);
//            }
//            try (FileWriter file = new FileWriter("json/" + name + ".json")) {
//			file.write(json.toString());
//			System.out.println("Successfully Copied JSON Object to File...");
//			System.out.println("\nJSON Object: " + json);
//		}
//        } catch (FileNotFoundException ex) {
//                Logger.getLogger(MultiPlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(MultiPlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(MultiPlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    public String[] getData(String name){
//        String[] data = new String[6];
//        try {
//            JSONParser parser = new JSONParser();
//            JSONObject json = (JSONObject) parser.parse(new FileReader("json/" + name + ".json"));
//            String studname = json.get("name").toString();
//            String studnr = json.get("studienummer").toString();
//            String studret = json.get("studieretning").toString();
//            String wins = json.get("vundet").toString();
//            String losses = json.get("tabt").toString();
//            String highscore = json.get("highscore").toString();
//            data[0] = studname;
//            data[1] = studnr;
//            data[2] = studret;
//            data[3] = wins;
//            data[4] = losses;
//            data[5] = highscore;
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MultiPlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
//        }catch (IOException ex) {
//            Logger.getLogger(MultiPlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(MultiPlayerLogic.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return data;
//    }

}
