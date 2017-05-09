package hanganimals.models;

import hanganimals.database.Connector;
import hanganimals.gamelogic.MultiplayerLogic;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ChatMessages {
    
    Connector conn = Connector.getInstance();
        
    public String userid, name, message;
    public Date time;

    public ChatMessages(String userid, String name, String message) throws SQLException {
        this.userid = userid;
        this.name = name;
        this.message = message;
        this.time = new Date();
        
        //conn.query("SELECT * FROM hang_users WHERE userid='"+userid+"';");
    }
    
}
