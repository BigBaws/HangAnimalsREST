package hanganimals.models;

import hanganimals.MultiPlayerGame;
import hanganimals.gamelogic.MultiPlayerLogic;
import java.util.ArrayList;

public class MultiplayerUser {
    
    public ArrayList<String> usedletters = new ArrayList<>();
    public int wrongs, combo, gamescore;
    public String userid, name, userword;
    public boolean lastLetterCorrect;
    
    public boolean Won;
    public boolean Lost;

    public MultiplayerUser(String userid, MultiPlayerGame game ) {
        this.userid = userid;
    }
    
}
