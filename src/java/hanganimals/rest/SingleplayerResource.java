package hanganimals.rest;

import hanganimals.models.SingleplayerGame;
import hanganimals.database.Connector;
import hanganimals.exceptions.DatabaseException;
import hanganimals.exceptions.ThreadException;
import hanganimals.exceptions.TokenException;
import hanganimals.exceptions.ValidationException;
import hanganimals.gamelogic.SingleplayerLogic;
import hanganimals.validators.ValidateSingleplayer;
import hanganimals.validators.ValidateUser;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
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
        // TODO: !!
    }
    
    /**
     *
     * @param token
     * @param userid
     * @param gameid
     * @return Game Object
     * @throws Exception
     */
    @PUT
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@FormParam("token") String token, @FormParam("userid") String userid, @FormParam("gameid") String gameid) throws Exception {
        /* Token & Userid Validation */
        if (ValidateUser.validateTokenUserid(token, userid)) {
            
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
                ResultSet res = conn.query("SELECT * FROM hang_single WHERE gameid = '"+gameid+"' AND userid='"+userid+"';");
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
            } catch (DatabaseException e) {
                throw new DatabaseException("Could not connect to database.");
            }

            /* Create game */
            SingleplayerGame game = new SingleplayerGame(userid);
            games.put(game.gameid, game);
            /* Update MySQL */
            conn.update("INSERT INTO hang_single (gameid, userid, word, userword, combo, combo_active, gamescore) VALUES ('"+game.gameid+"', '"+game.userid+"', '"+game.word+"', '"+game.userword+"', '0', '0', '0');");
            conn.update("UPDATE hang_users SET singleplayer='"+game.gameid+"' WHERE userid='"+userid+"';");
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
        } else {
            throw new TokenException("Token or Userid was not accepted.");
        }
    }
    
    /**
     *
     * @param token
     * @param userid
     * @param gameid
     * @param letter
     * @return Game Object (updated)
     * @throws Exception
     */
    @POST
    @Path("/guess")
    @Produces(MediaType.APPLICATION_JSON)
    public String guess(
            @FormParam("token") String token,
            @FormParam("userid") String userid,
            @FormParam("gameid") String gameid,
            @FormParam("letter") String letter) throws Exception {
        
        /* Token & Userid Validation */
        if (ValidateUser.validateTokenUserid(token, userid)) {
            /* TODO: Gameid exist? */
            if (ValidateSingleplayer.validateGame(userid, gameid)) {
                /* Letter validation */
                if (ValidateSingleplayer.validateGuess(letter)) {
                    SingleplayerGame game = games.get(gameid);
                    SingleplayerLogic.guessLetter(game, userid, letter.toLowerCase());
                    JSONObject object = new JSONObject();
                    object.put("word", game.userword);
                    object.put("used", game.usedletters);
                    object.put("wrongs", game.wrongs);
                    object.put("score", game.gamescore);
                    return object.toString();
                } else {
                    throw new ValidationException("The Letter ("+letter+") was not accepted.");
                }
            } else {
                throw new ValidationException("The GAMEID ("+gameid+") was not accepted.");
            }
        } else {
            throw new TokenException("Token or Userid was not accepted.");
        }
    }
    
    /**
     *
     * @param asyncResponse
     * @param gameid
     * @return This returns when game is done.
     */
    @GET
    @Path("{gameid}/listen")
    @Produces(MediaType.APPLICATION_JSON)
    public String listen(
            @Suspended final AsyncResponse asyncResponse,
            @PathParam("gameid") String gameid) {
        
        /* Start new Thread */
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
        throw new ThreadException("The thread should not be here.");
    }
    
    /**
     *
     * @param token
     * @param userid
     * @param gameid
     * @return This returns "success" or Exception.
     * @throws Exception
     */
    @POST
    @Path("/leave")
    @Produces(MediaType.APPLICATION_JSON)
    public String leave(
            @FormParam("token") String token,
            @FormParam("userid") String userid,
            @FormParam("gameid") String gameid) throws Exception {
        
        /* Token & Userid Validation */
        if (ValidateUser.validateTokenUserid(token, userid)) {
            /* Gameid exist? */
            if (ValidateSingleplayer.validateGame(userid, gameid)) {
                games.remove(gameid);
                conn.update("UPDATE hang_users SET singleplayer='0' WHERE userid='"+userid+"';");
                return "success";
            } else {
                throw new ValidationException("The GAMEID ("+gameid+") was not accepted.");
            }
        } else {
            throw new TokenException("Token or Userid was not accepted.");
        }
        
    }
    
}