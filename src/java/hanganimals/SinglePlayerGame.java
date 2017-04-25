package hanganimals;

import java.util.ArrayList;

public class SinglePlayerGame {

    private String gameid, userid, word, time;
    private ArrayList<String> usedletters = new ArrayList<>();
    private int combo, gamescore;
    
    public SinglePlayerGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SinglePlayerGame(String gameid, String userid, String word, ArrayList<String> usedletters, int combo, int gamescore) {
        this.gameid = gameid;
        this.userid = userid;
        this.word = word;
        this.usedletters = usedletters;
        this.combo = combo;
        this.gamescore = gamescore;
    }
    

}
