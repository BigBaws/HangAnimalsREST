package hanganimals.models;

import hanganimals.database.Connector;
import java.sql.SQLException;
import java.util.ArrayList;

public class SingleplayerUser {
    
    Connector conn = Connector.getInstance();
        
    public ArrayList<String> usedletters = new ArrayList<>();
    public int wrongs, combo, gamescore;
    public String userid, name, userword;
    public boolean lastLetterCorrect;
    
    public boolean Won;
    public boolean Lost;

    public SingleplayerUser(String userid, SingleplayerGame game) throws SQLException {
        this.userid = userid;
        
        //conn.query("SELECT * FROM hang_users WHERE userid='"+userid+"';");
        
    }
    
}
