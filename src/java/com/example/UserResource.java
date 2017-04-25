package com.example;

import hanganimals.User;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author BigBaws
 */
@Path("/user")
public class UserResource {
    
    public static ArrayList<User> onlineUsers = new ArrayList<>();
    
    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getOnlineUsers() {
        
        JSONObject online = new JSONObject();
        JSONObject json = new JSONObject();
        
        for (User user: onlineUsers) {
            json.put(user.userid, user.name);
//            json.put("name", user.name);
//            json.put("id", user.userid);
//            json.put("image", user.image);
//            json.put("study", user.study);
//            json.put("currency", user.currency);
//            json.put("gameid", user.gameid);
//            json.put("animal", user.animal);
//            json.put("animalcolor", user.animalcolor);
        }
        online.put("users", json);
        return "{\"online\":\""+online+"\"}";
    }
    
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public String postHandler(String content) {
        return content + "MYTEST";
    }
    
}
