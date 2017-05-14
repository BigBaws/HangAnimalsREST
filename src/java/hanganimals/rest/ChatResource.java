package hanganimals.rest;

import hanganimals.models.ChatMessages;
import hanganimals.models.ChatUser;
import hanganimals.validators.ValidateUser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/chat")
public class ChatResource
{

    //public static List<ChatMessages> chat = new ArrayList<>();
    //public static HashMap<String, ChatUser> users = new HashMap<>();

    @Context
    private UriInfo context;

    public ChatResource()
    {
    }

    /*
    @GET
    //@Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getChat(
            @QueryParam("token") String token,
            @QueryParam("userid") String userid) throws Exception
    {

        JSONArray jsonMap = new JSONArray();
        for (ChatMessages entry : chat)
        {
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
    }*/

    /*
    @POST
    @Path("join")
    @Produces(MediaType.APPLICATION_JSON)
    public void join(
            @FormParam("token") String token,
            @QueryParam("userid") String userid) throws Exception
    {
        
        if (!userid.isEmpty())
        {
            ChatUser user = new ChatUser(userid);
            users.put("user", user);
            // TODO: Add to the database
        }
    }*/

//    @GET
//    @Path("messages/latest")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getLatestMessage(
//            @QueryParam("token") String token,
//            @QueryParam("userid") String userid
//    ) throws Exception {
//        Date latest = chat.get(chat.size() - 1).time;
//        if (!userid.isEmpty()) {
//            while(true) {
//                ChatMessages entry = chat.get(chat.size() - 1);
//                if(latest != entry.time) {
//                    JSONObject object = new JSONObject();
//                    object.put("userid", entry.userid);
//                    object.put("name", entry.name);
//                    object.put("time", entry.time);
//                    object.put("message", entry.message);
//                    
//                    return object.toString();
//                }
//            }
//        }
//        
//        return null;
//    }
//    @POST
//    @Path("send")
//    @Produces(MediaType.APPLICATION_JSON)
//    public void send(
//            @FormParam("token") String token,
//            @FormParam("userid") String userid,
//            @FormParam("message") String message) throws Exception
//    {
//        if (!userid.isEmpty() || !message.isEmpty())
//        {
//            ChatMessages msg = new ChatMessages(userid, token, message);
//            chat.add(msg);
//        }
//    }

    final static Map<String, AsyncResponse> waiters = new ConcurrentHashMap<>();
    final static ExecutorService ex = Executors.newSingleThreadExecutor();
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Path("listen/{userid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void listen(@Suspended AsyncResponse asyncResp, @PathParam("userid") String userid)
    {
        waiters.put(userid, asyncResp);
    }

    @POST
    @Path("send")
    @Produces(MediaType.APPLICATION_JSON)
    public void send(
            @FormParam("token") String token,
            @FormParam("userid") String userid,
            @FormParam("message") String message) throws Exception
    {
        ValidateUser.checkToken(token, userid);
        ex.submit(new Runnable()
        {
            @Override
            public void run()
            {
                Set<String> users = waiters.keySet();
                //chat.add(msg);
                String now = sdf.format(new Date());
                String name = LoginResource.users.get(userid).name;
                for (String n : users)
                {
                    // [28-03-2017 | 18:01:08] Thomas Anthony: Wuhu.
                    String newmessage = "[" + now + "]" + " " + name + ": " + message;
                    // Sends message to all, except sender  
                    waiters.get(n).resume(newmessage);
                }
            }
        });
        //return "Message is sent..";
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
