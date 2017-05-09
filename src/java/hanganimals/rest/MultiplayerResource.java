package hanganimals.rest;

import hanganimals.exceptions.TokenException;
import hanganimals.models.MultiplayerGame;
import hanganimals.database.Connector;
import hanganimals.gamelogic.MultiplayerLogic;
import hanganimals.models.MultiplayerUser;
import hanganimals.validators.ValidateUser;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/multiplayer/room")
public class MultiplayerResource {
    
    public static HashMap<String, MultiplayerGame> multiplayerGames = new HashMap<>();
    Connector conn = Connector.getInstance();
    
    @Context
    private UriInfo context;
    
    public MultiplayerResource() {}
    
    @PUT
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public String createRoom(@QueryParam("token") String token) throws Exception {
        /* Token Validation */
        if (ValidateUser.validateToken(token)) {
            /* Create new game */
            MultiplayerGame game = new MultiplayerGame(generateGameID());
            conn.update("INSERT INTO hang_multi_rooms (roomid, round, word) VALUES ('"+game.roomid+"', '1', '"+game.word+"');");

            /* Add game to Multiplayer Rooms */
            multiplayerGames.put(game.roomid, game);
            JSONObject object = new JSONObject();
            object.put("roomid", game.roomid);
            return object.toString();
        } else {
            throw new TokenException("Token or Userid was not accepted.");
        }
    }
    
    @GET
    @Path("listRooms")
    @Produces(MediaType.APPLICATION_JSON)
    public String listGames(@QueryParam("token") String token, @QueryParam("username") String username) throws Exception {
        JSONArray jsonMap = new JSONArray();
        for(String key : multiplayerGames.keySet()) {
            MultiplayerGame entry = multiplayerGames.get(key);
            JSONObject object = new JSONObject();
            object.put("roomid", entry.roomid);
            object.put("round", entry.round);
            jsonMap.put(object);
        }
        JSONObject returnObject = new JSONObject();
        returnObject.put("games", jsonMap);
        return returnObject.toString();
    }
    
    @POST
    @Path("{roomid}/join")
    @Produces(MediaType.APPLICATION_JSON)
    public String joinRoom (
            @PathParam("roomid") String roomid,
            @QueryParam("token") String token,
            @QueryParam("userid") String userid) throws Exception {
        if (!userid.isEmpty() || !roomid.isEmpty()) {
            
            /* Check if room is in the object list */
            if(multiplayerGames.containsKey(roomid)) {
                MultiplayerGame game = multiplayerGames.get(roomid);
                conn.update("INSERT INTO hang_multi_users (roomid, userid) VALUES ('"+roomid+"', '"+userid+"');");
                MultiplayerUser user = new MultiplayerUser(userid, game);
                game.addUser(user);
                JSONObject object = new JSONObject();
                object.put("roomid", game.roomid);
                object.put("round", game.round);
                return object.toString();
            } else {
                /* Not found in object list, check database */
                ResultSet res = conn.query("SELECT * FROM hang_multi_rooms WHERE roomid='"+roomid+"';");
                if (res.first()) {
                    conn.update("INSERT INTO hang_multi_users (roomid, userid) VALUES ('"+roomid+"', '"+userid+"');");
                    MultiplayerGame game = multiplayerGames.get(roomid);
                    MultiplayerUser user = new MultiplayerUser(userid, game);
                    game.addUser(user);
                    JSONObject object = new JSONObject();
                    object.put("roomid", res.getString("roomid"));
                    object.put("round", res.getInt("round"));
                    return object.toString();
                }
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
                try {
                    JSONObject object = new JSONObject();
                    object.put("winner", multiplayerGames.get(roomid).winner);
                    return object.toString();
                } catch (Exception e) {
                    return e.getMessage();
                }
                
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
    
    @POST
    @Path("{roomid}/guess")
    @Produces(MediaType.APPLICATION_JSON)
    public String guess(
            @PathParam("roomid") String roomid,
            @QueryParam("token") String token,
            @QueryParam("userid") String userid,
            @QueryParam("letter") String letter) throws Exception {
        MultiplayerGame game = multiplayerGames.get(roomid);
        MultiplayerUser user = game.getUser(userid);
        
        String lower = letter.toLowerCase();
        MultiplayerLogic.guessLetter(game, userid, lower);
        
        JSONObject object = new JSONObject();
        object.put("word", user.userword);
        object.put("used", user.usedletters);
        object.put("wrongs", user.wrongs);
        object.put("score", user.gamescore);
        
        return object.toString();
    }
    
    
    @GET
    @Path("{roomid}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers(
            @PathParam("roomid") String roomid,
            @QueryParam("token") String token) throws Exception {
        
        Collection<MultiplayerUser> usersCollection = multiplayerGames.get(roomid).users.values();
        MultiplayerUser[] users = usersCollection.toArray(new MultiplayerUser[usersCollection.size()]);
        
        JSONArray jsonMap = new JSONArray();
        for(MultiplayerUser u : users) {
            JSONObject object = new JSONObject();
            object.put("name", u.userid);
            object.put("gamescore", u.gamescore);
            jsonMap.put(object);
        }
        
        JSONObject returnObject = new JSONObject();
        returnObject.put("users", jsonMap);
        return returnObject.toString();
    }
    
    @POST
    @Path("{roomid}/leave")
    @Produces(MediaType.APPLICATION_JSON)
    public void leave(
            @PathParam("roomid") String roomid,
            @QueryParam("token") String token,
            @QueryParam("userid") String userid) throws Exception {
        if (!userid.isEmpty() || !roomid.isEmpty()) {
            
            /* Check if room is in the object list */
            if(multiplayerGames.containsKey(roomid)) {
                MultiplayerGame game = multiplayerGames.get(roomid);
                MultiplayerUser user = new MultiplayerUser(userid, game);
                conn.query("DELETE FROM hang_multi_users WHERE userid='"+userid+"'");
                game.removeUser(user);
            }
        }
    }
    
    
    @GET
    @Path("{roomid}/nextRound")
    @Produces(MediaType.APPLICATION_JSON)
    public String nextRound(@Suspended final AsyncResponse asyncResponse, @PathParam("roomid") String roomid) {
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
                try {
                    Thread.sleep(10000);
                    JSONObject object = new JSONObject();
                    object.put("word", multiplayerGames.get(roomid).users.get(0).userword);
                    object.put("round", multiplayerGames.get(roomid).round);
                    return object.toString();
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
        }).start();
        return null;
    }
    
    private String generateGameID() {
        Random random = new SecureRandom();
        String gameid = new BigInteger(130, random).toString(32);
        return gameid;
    }
}