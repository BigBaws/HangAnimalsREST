package hanganimals.rest;

import hanganimals.gamelogic.SingleplayerLogic;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;

@Path("/SinglePlayerLogic")
public class SingleplayerLogicResource {

    ArrayList<SinglePlayerGameObjects> singleplayerGames = new ArrayList<>();
    
    @Context
    private UriInfo context;

    public SingleplayerLogicResource() {}

    private String InitializeGameWithUserName(String token, String username) {
        for (SinglePlayerGameObjects g : singleplayerGames) {
            if (g.username.equals(username)) {
                return g.token;
            }
        }
        SingleplayerLogic logic = new SingleplayerLogic();
        //singleplayerGames.add(new SinglePlayerGameObjects(logic, username, token));
        singleplayerGames.add(new SinglePlayerGameObjects(logic, username, token));
        return token;
        
    }
    
    private class SinglePlayerGameObjects {
        //Galgelogik galgelogik;
        SingleplayerLogic logic;
        String username;
        String token;
        
        //public SinglePlayerGameObjects(Galgelogik g, String username, String token) {
        public SinglePlayerGameObjects(SingleplayerLogic logic, String username, String token) {
            //this.galgelogik = g;
            this.logic = logic;
            this.username = username;
            this.token = token;
        }
    }
}