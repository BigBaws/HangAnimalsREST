package com.example;

import static com.example.MultiPlayerResource.multiplayerGames;
import hanganimals.MultiplayerGame;
import hanganimals.models.ChatMessages;
import hanganimals.models.ChatUser;
import hanganimals.models.MultiplayerUser;
import java.util.HashMap;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/chat")
public class ChatResource {

    public static HashMap<String, ChatMessages> chat = new HashMap<>();
    public static HashMap<String, ChatUser> users = new HashMap<>();
    
    @Context
    private UriInfo context;

    public ChatResource() {
    }

    @GET
    @Path("getChat")
    @Produces(MediaType.APPLICATION_JSON)
    public String getChat(
            @QueryParam("token") String token,
            @QueryParam("userid") String userid) throws Exception {
        
        JSONArray jsonMap = new JSONArray();
        for(String key : chat.keySet()) {
            ChatMessages entry = chat.get(key);
            JSONObject object = new JSONObject();
            object.put("userid", entry.userid);
            object.put("name", entry.name);
            object.put("time", entry.time);
            object.put("message", entry.message);
            jsonMap.put(object);
        }
        JSONObject returnObject = new JSONObject();
        returnObject.put("chat", jsonMap);
        return returnObject.toString();
    }
    
    @POST
    @Path("join")
    @Produces(MediaType.APPLICATION_JSON)
    public void join(
            @QueryParam("token") String token,
            @QueryParam("userid") String userid) throws Exception {
        if (!userid.isEmpty()) {
            ChatUser user = new ChatUser(userid);
            users.put("user", user);
            // TODO: Add to the database
        }
    }
    
    @POST
    @Path("send")
    @Produces(MediaType.APPLICATION_JSON)
    public void send(
            @QueryParam("token") String token,
            @QueryParam("userid") String userid,
            @QueryParam("message") String message) throws Exception {
        if (!userid.isEmpty() ||!message.isEmpty()) {
            ChatMessages msg = new ChatMessages(userid, token, message);
            chat.put("message", msg);
        }
    }
    
//    @GET
//    @Path("listen")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String listen(@Suspended final AsyncResponse asyncResponse) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String result = veryExpensiveOperation();
//                asyncResponse.resume(result);
//            }
//            
//            public String veryExpensiveOperation() {
//                try {
//                    JSONObject object = new JSONObject();
//                    object.put("winner", multiplayerGames.get(roomid).winner);
//                    return object.toString();
//                } catch (Exception e) {
//                    return e.getMessage();
//                }
//                
//            }
//        }).start();
//        return "WTF";
//    }

}
