package com.example;

import hanganimals.MultiPlayerGame;
import hanganimals.database.Connector;
import hanganimals.gamelogic.MultiPlayerLogic;
import hanganimals.models.MultiplayerUser;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Random;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/multiplayer/room")
public class MultiPlayerResource {
    
    public static HashMap<String, MultiPlayerGame> multiplayerGames = new HashMap<>();
    Connector conn = Connector.getInstance();
    
    @Context
    private UriInfo context;
    
    public MultiPlayerResource() {}
    
    @GET
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public String createRoom(@QueryParam("token") String token) throws Exception {
        MultiPlayerGame game = new MultiPlayerGame(generateGameID());
        
        conn.update("INSERT INTO hang_multi_rooms (roomid, round, word) VALUES ('"+game.roomid+"', '1', '"+game.word+"');");
        
        multiplayerGames.put(game.roomid, game);
        return "Roomid: "+game.roomid +" word: "+game.word;
    }
    
    @GET
    @Path("listGames")
    @Produces(MediaType.APPLICATION_JSON)
    public String listGames(@QueryParam("token") String token, @QueryParam("username") String username) throws Exception {
        
        JSONArray jsonMap = new JSONArray();
        int i = 0;
        for(String key : multiplayerGames.keySet()) {
            MultiPlayerGame entry = multiplayerGames.get(key);
            JSONObject object = new JSONObject();
            object.put("roomid", entry.roomid);
            object.put("round", entry.round);
            jsonMap.put(object);
        }
        
        JSONObject returnObject = new JSONObject();
        returnObject.put("games", jsonMap);
    
        return returnObject.toString();
    }
    
    @GET
    @Path("{roomid}/join")
    @Produces(MediaType.APPLICATION_JSON)
    public String joinRoom (
            @PathParam("roomid") String roomid,
            @QueryParam("token") String token,
            @QueryParam("userid") String userid) throws Exception {
        if (!userid.isEmpty() || !roomid.isEmpty()) {
            
            /* Check if room is in the object list */
            if(multiplayerGames.containsKey(roomid)) {
                MultiPlayerGame game = multiplayerGames.get(roomid);
                conn.update("INSERT INTO hang_multi_users (roomid, userid) VALUES ('"+roomid+"', '"+userid+"');");
                MultiplayerUser user = new MultiplayerUser(userid, game);
                game.addUser(user);
                return "{"
                        + "\"roomid\": [\""+roomid+"\"] , "
                        + "\"round\": [\""+1+"\"] , "
                        + "\"word\": [\""+game.word+"\"] "
                        + "}";
            }
            
            /* Not found in object list, check database */
            ResultSet res = conn.query("SELECT * FROM hang_multi_rooms WHERE roomid='"+roomid+"';");
            if (res.first()) {
                conn.update("INSERT INTO hang_multi_users (roomid, userid) VALUES ('"+roomid+"', '"+userid+"');");
                return "{"
                        + "\"roomid\": [\""+res.getString("roomid")+"\"] , "
                        + "\"round\": [\""+res.getDouble("round")+"\"] , "
                        + "\"word\": [\""+res.getString("word")+"\"] "
                        + "}";
            }
            return "There's no available game with that roomid.";
        } else {
            if (roomid.isEmpty()) {
                return "Empty roomid";
            }
            if (userid.isEmpty()) {
                return "Empty userid";
            }
        }
        return "Something went really wrong";
    }
    
    @GET
    @Path("{roomid}/listen")
    @Produces(MediaType.APPLICATION_JSON)
    public String listen(@Suspended final AsyncResponse asyncResponse, @PathParam("roomid") String roomid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = veryExpensiveOperation();
                asyncResponse.resume(result);
            }
            
            public String veryExpensiveOperation() {
                while (multiplayerGames.get(roomid).gameIsActive()) {
                    /* Do Nothing */
                }
                return multiplayerGames.get(roomid).winner + "WINS! ("+multiplayerGames.get(roomid).gameIsActive()+")";
            }
        }).start();
        return "WTF";
    }
    
    @GET
    @Path("{roomid}/userword")
    @Produces(MediaType.APPLICATION_JSON)
    public String userword(
            @PathParam("roomid") String roomid,
            @QueryParam("token") String token,
            @QueryParam("userid") String userid) throws Exception {
            
        JSONObject object = new JSONObject();
        object.put("userword", multiplayerGames.get(roomid).getUser(userid).userword);
        
        return object.toString();
    }
    
    @GET
    @Path("{roomid}/guess")
    @Produces(MediaType.APPLICATION_JSON)
    public String guess(
            @PathParam("roomid") String roomid,
            @QueryParam("token") String token,
            @QueryParam("userid") String userid,
            @QueryParam("letter") String letter) throws Exception {
        MultiPlayerGame game = multiplayerGames.get(roomid);
        MultiplayerUser user = game.getUser(userid);
        
        String lower = letter.toLowerCase();
        
        MultiPlayerLogic.guessLetter(game, userid, lower);
        
        JSONObject object = new JSONObject();
        object.put("word", user.userword);
        object.put("used", user.usedletters);
        object.put("wrongs", user.wrongs);
        object.put("score", user.gamescore);
        
        return object.toString();
    }
    
    
    private String generateGameID() {
        Random random = new SecureRandom();
        String gameid = new BigInteger(130, random).toString(32);
        return gameid;
    }
}