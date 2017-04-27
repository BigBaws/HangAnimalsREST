package com.example;

import hanganimals.gamelogic.SinglePlayerLogic;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author BigBaws
 */
@Path("/SinglePlayerLogic")
public class SinglePlayerLogicResource {

    ArrayList<SinglePlayerGameObjects> singleplayerGames = new ArrayList<>();
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SinglePlayerLogicResource
     */
    public SinglePlayerLogicResource() {
    }

    /**
     * Retrieves representation of an instance of com.example.SinglePlayerLogicResource
     * @return an instance of java.lang.String
     */
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
    
    private String InitializeGameWithUserName(String token, String username) {
        for (SinglePlayerGameObjects g : singleplayerGames) {
            if (g.username.equals(username)) {
                return g.token;
            }
        }
        SinglePlayerLogic logic = new SinglePlayerLogic();
        //singleplayerGames.add(new SinglePlayerGameObjects(logic, username, token));
        singleplayerGames.add(new SinglePlayerGameObjects(logic, username, token));
        return token;
        
    }
    
    private class SinglePlayerGameObjects {
        //Galgelogik galgelogik;
        SinglePlayerLogic logic;
        String username;
        String token;
        
        //public SinglePlayerGameObjects(Galgelogik g, String username, String token) {
        public SinglePlayerGameObjects(SinglePlayerLogic logic, String username, String token) {
            //this.galgelogik = g;
            this.logic = logic;
            this.username = username;
            this.token = token;
        }
    }
}