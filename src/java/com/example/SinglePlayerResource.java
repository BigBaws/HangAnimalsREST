package com.example;

import hanganimals.gamelogic.SinglePlayerLogic;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/singleplayer")
public class SinglePlayerResource {

    public static ArrayList<SinglePlayerGameObjects> singleplayerGames = new ArrayList<>();
    
    @Context
    private UriInfo context;

    public SinglePlayerResource() {}

    @GET
    @Path("/createNewGame")
    @Produces(MediaType.APPLICATION_JSON)
    public String createNewGame(@QueryParam("Token") String token, @QueryParam("Userid") String userid) throws Exception {
        SinglePlayerLogic logic = new SinglePlayerLogic();
        SinglePlayerGameObjects game = new SinglePlayerGameObjects(logic, token, userid);
        
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://185.121.172.101:3306/zhgmzrgi_hanganimals", "zhgmzrgi_REST", "xcv123REST");
        PreparedStatement statement = con.prepareStatement("INSERT INTO hang_single (gameid, userid, word) VALUES ('"+game.gameid+"', '"+userid+"', '"+logic.getOrdet()+"');");
        statement.execute();
        
        singleplayerGames.add(game);
        return "single: "+singleplayerGames.get(0).gameid +" word: "+singleplayerGames.get(0).logic.getOrdet();
    }
    
    @GET
    @Path("/loadGame")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadGame(@QueryParam("Userid") String userid) throws Exception {
        if (!userid.isEmpty()) {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://185.121.172.101:3306/zhgmzrgi_hanganimals", "zhgmzrgi_REST", "xcv123REST");
            PreparedStatement statement = con.prepareStatement("SELECT * FROM hang_single WHERE userid = '"+userid+"';");
            ResultSet res = statement.executeQuery();

            if (res.first()) {
                for (SinglePlayerGameObjects spgo : singleplayerGames) {
                    if (spgo.userid.equals(res.getString("userid"))) {
                        return spgo.userid+" - "+spgo.logic.getOrdet();
                    }
                }
            }
            return "There's no available saved game.";
        }
        return "Wrong userid";
    }
    
    @PUT
    @Path("/saveGame")
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveGame() {
        
        return "";
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getGameID() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of SinglePlayerLogicResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    private String InitializeGameWithUserName(String token, String userid) {
        for (SinglePlayerGameObjects g : singleplayerGames) {
            if (g.userid.equals(userid)) {
                return g.token;
            }
        }
        SinglePlayerLogic logic = new SinglePlayerLogic();
        //singleplayerGames.add(new SinglePlayerGameObjects(logic, username, token));
        singleplayerGames.add(new SinglePlayerGameObjects(logic, userid, token));
        return token;
        
    }
    
    private class SinglePlayerGameObjects {
        //Galgelogik galgelogik;
        SinglePlayerLogic logic;
        String userid;
        String token;
        String gameid;
        
        //public SinglePlayerGameObjects(Galgelogik g, String username, String token) {
        public SinglePlayerGameObjects(SinglePlayerLogic logic, String token, String userid) {
            //this.galgelogik = g;
            this.logic = logic;
            this.token = token;
            this.userid = userid;
            this.gameid = generateGameID();
        }
        
        private String generateGameID() {
            Random random = new SecureRandom();
            String gameid = new BigInteger(130, random).toString(32);
        return gameid;
    }
    }
}