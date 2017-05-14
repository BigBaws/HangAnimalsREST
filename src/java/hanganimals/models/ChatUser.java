package hanganimals.models;

import hanganimals.database.Connector;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatUser {
    
    Connector conn = Connector.getInstance();
        
    public String userid, name, userword;
    public boolean lastLetterCorrect;
    
    public boolean Won;
    public boolean Lost;

    public ChatUser(String userid) throws SQLException {
        this.userid = userid;
        
        //conn.query("SELECT * FROM hang_users WHERE userid='"+userid+"';");
        
    }
    
}
