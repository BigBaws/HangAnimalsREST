package hanganimals.rest;

import hanganimals.models.User;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;

@Path("/users")
public class UserResource {
    
    @Context
    private UriInfo context;
    
    public UserResource() { }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getOnlineUsers() {
        /* Need method to remove users (update a timer when REST is called) */
        JSONObject online = new JSONObject();
        JSONObject json = new JSONObject();
        for(String key : LoginResource.users.keySet()) {
            User user = LoginResource.users.get(key);
            json.put("userid", user.userid);
            json.put("name", user.name);
            json.put("image", user.image);
            json.put("study", user.study);
            json.put("currency", user.currency);
            json.put("multiplayer", user.multiplayer);
            json.put("animal", user.animal);
            json.put("animalcolor", user.animalcolor);
        }
        online.put("users", json);
        return online.toString();
    }
    
}