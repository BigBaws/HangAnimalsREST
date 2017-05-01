package hanganimals;

import hanganimals.gamelogic.SingleplayerLogic;
import hanganimals.gamelogic.WordEngine;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class SingleplayerGame {
    
    public String gameid, userid, word, userword;
    public ArrayList<String> usedletters = new ArrayList<>();
    public int combo, gamescore, wrongs;
    public Date start;
    
    public boolean lastLetterCorrect = false;
    public boolean gameIsWon = false;
    public boolean gameIsLost = false;
    
    public SingleplayerGame(String userid) {
        this.gameid = generateGameID();
        this.userid = userid;
        this.word = WordEngine.getWord();
        SingleplayerLogic.updateVisibleWord(this, userid);
    }
    
    public SingleplayerGame() {
        
    }

    public SingleplayerGame(String gameid, String userid, String word, String userword, String usedletters, int combo, boolean combo_active, int gamescore, Date start) {
        this.gameid = gameid;
        this.userid = userid;
        this.word = word;
        this.userword = userword;
        this.usedletters.add(usedletters);
        this.combo = combo;
        this.lastLetterCorrect = combo_active;
        this.gamescore = gamescore;
        this.start = start;
    }
    
    private String generateGameID() {
        Random random = new SecureRandom();
        String gameid = new BigInteger(130, random).toString(32);
        return gameid;
    }
}
