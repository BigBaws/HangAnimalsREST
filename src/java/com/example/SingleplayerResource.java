package com.example;

import static com.example.MultiplayerResource.multiplayerGames;
import hanganimals.SingleplayerGame;
import hanganimals.database.Connector;
import hanganimals.gamelogic.SingleplayerLogic;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

@Path("/singleplayer")
public class SingleplayerResource {
    
    public static HashMap<String, SingleplayerGame> games = new HashMap<>();
    Connector conn = Connector.getInstance();
    
    @Context
    private UriInfo context;
    
    public SingleplayerResource() {
        /* Get singleplayer games from database */
        
    }
    
    @PUT
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public String createGame(@QueryParam("token") String token, @QueryParam("userid") String userid, @QueryParam("gameid") String gameid) throws Exception {
        /* Check if the user have a game that is already in the hashmap */
        if (games.containsKey(gameid)) {
            SingleplayerGame entry = games.get(gameid);
            JSONObject object = new JSONObject();
            object.put("gameid", entry.gameid);
            object.put("userid", entry.userid);
            object.put("word", entry.userword);
            object.put("usedletters", entry.usedletters);
            object.put("combo", entry.combo);
            object.put("combo_active", entry.lastLetterCorrect);
            object.put("gamescore", entry.gamescore);
            object.put("start", entry.start);
            return object.toString();
        }
        
        /* Check if the user have a game in the database */
        try {
            ResultSet res = conn.query("SELECT * FROM hang_single WHERE gameid = '"+gameid+"';");
            if (res.first()) {
                JSONObject object = new JSONObject();
                object.put("gameid", res.getString("gameid"));
                object.put("userid", res.getString("userid"));
                object.put("word", res.getString("userword"));
                object.put("usedletters", res.getString("usedletters"));
                object.put("combo", res.getInt("combo"));
                object.put("combo_active", res.getBoolean("combo_active"));
                object.put("gamescore", res.getInt("gamescore"));
                object.put("start", res.getDate("start"));
                SingleplayerGame game = new SingleplayerGame(res.getString("gameid"), res.getString("userid"), res.getString("word"), res.getString("userword"), res.getString("usedletters"), res.getInt("combo"), res.getBoolean("combo_active"), res.getInt("gamescore"), res.getDate("start"));
                games.put(gameid, game);
                return object.toString();
            }
        } catch (Exception e) {
            throw e;
        }
        
        /* Create game */
        SingleplayerGame game = new SingleplayerGame(userid);
        games.put(game.gameid, game);
        /* Update MySQL */
        conn.update("INSERT INTO hang_single (gameid, userid, word, userword, combo, combo_active, gamescore) VALUES ('"+game.gameid+"', '"+game.userid+"', '"+game.word+"', '"+game.userword+"', '0', '0', '0');");
        /* Return JSON */
        JSONObject object = new JSONObject();
        object.put("gameid", game.gameid);
        object.put("userid", game.userid);
        object.put("word", game.userword);
        object.put("usedletters", game.usedletters);
        object.put("combo", game.combo);
        object.put("combo_active", game.lastLetterCorrect);
        object.put("gamescore", game.gamescore);
        object.put("start", game.start);
        return object.toString();
    }
    
    @POST
    @Path("/guess")
    @Produces(MediaType.APPLICATION_JSON)
    public String guess(
            @QueryParam("token") String token,
            @QueryParam("userid") String userid,
            @QueryParam("gameid") String gameid,
            @QueryParam("letter") String letter) throws Exception {
        SingleplayerGame game = games.get(gameid);
        
        SingleplayerLogic.guessLetter(game, userid, letter.toLowerCase());
        
        JSONObject object = new JSONObject();
        object.put("word", game.userword);
        object.put("used", game.usedletters);
        object.put("wrongs", game.wrongs);
        object.put("score", game.gamescore);
        return object.toString();
    }
    
    @GET
    @Path("{gameid}/listen")
    @Produces(MediaType.APPLICATION_JSON)
    public String listen(
            @Suspended final AsyncResponse asyncResponse,
            @PathParam("gameid") String gameid,
            @QueryParam("token") String token,
            @QueryParam("userid") String userid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = veryExpensiveOperation();
                asyncResponse.resume(result);
            }
            
            public String veryExpensiveOperation() {
                while (!(games.get(gameid).gameIsWon || games.get(gameid).gameIsLost)) {
                    /* Do Nothing */
                }
                try {
                    JSONObject object = new JSONObject();
                    if (games.get(gameid).gameIsWon) {
                        object.put("message", "You have won");
                    } else if (games.get(gameid).gameIsLost) {
                        object.put("message", "You have lost");
                    }
                    object.put("gamescore", games.get(gameid).gamescore);
                    object.put("highscore", "notyetimpl");
                    object.put("start", games.get(gameid).start);
                    object.put("combo", games.get(gameid).combo);
                    return object.toString();
                } catch (Exception e) {
                    return e.getMessage();
                }
                
            }
        }).start();
        return "WTF";
    }
    
    @DELETE
    @Path("/leave")
    @Produces(MediaType.APPLICATION_JSON)
    public String leave(
            @QueryParam("token") String token,
            @QueryParam("userid") String userid,
            @QueryParam("gameid") String gameid) throws Exception {
        games.remove(gameid);
        return "success";
    }
    
}