package hanganimals.models;

import hanganimals.gamelogic.MultiplayerLogic;
import hanganimals.gamelogic.WordEngine;
import hanganimals.models.MultiplayerUser;
import java.util.HashMap;

public class MultiplayerGame {
    
    public HashMap<String, MultiplayerUser> users = new HashMap<>();
    public String roomid, word, time, winner;
    public int round;
    
    public boolean gameIsWon = false;
    public boolean gameIsLost = false;
    
    public MultiplayerGame(String roomid) {
        this.roomid = roomid;
        this.word = WordEngine.getWord();
        this.round = 1;
    }
    
    public MultiplayerUser getUser(String userid) {
        return users.get(userid);
    }
    
    public void addUser(MultiplayerUser user) throws Exception {
        users.put(user.userid, user);
        MultiplayerLogic.updateVisibleWord(this, user.userid);
    }
    
    public void removeUser(MultiplayerUser user) {
        users.remove(user);
    }
    
    public int getNumberOfFinishedUsers() {
        int i = 0;
        for(String key : users.keySet()) {
            MultiplayerUser user = users.get(key);
            if(user.Won)
                i++;
        }
        
        return i;
    }
    
    public void nextRound() throws Exception {
        for (String key : users.keySet()) {
            MultiplayerUser user = users.get(key);
            if (this.winner == user.userid) {
                this.word = WordEngine.getWord();
            }
            user.usedletters.clear();
            user.wrongs = 0;
            user.lastLetterCorrect = false;
            MultiplayerLogic.updateVisibleWord(this, user.userid);
        }
        this.round++;
        this.gameIsWon = false;
        this.gameIsLost = false;
    }
    
    /*
    * Async call
    */
    public boolean gameIsActive() {
        if (gameIsWon) {
            return false;
        } else if (gameIsLost) {
            return false;
        }
        return true;
    }
    
}