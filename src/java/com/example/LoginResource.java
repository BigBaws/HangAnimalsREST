package com.example;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import hanganimals.User;
import hanganimals.database.Connector;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Random;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

@Path("/login")
public class LoginResource {
    
    Connector conn = Connector.getInstance();
    
    /* User */
    String token, animal, animalcolor;
    int currency, singleplayer, multiplayer;
    
    @Context
    private UriInfo context;
    
    public LoginResource() {
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String rmiLogin(@QueryParam("username") String username, @QueryParam("password") String password) throws Exception {
        try {
            Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
            Bruger b = ba.hentBruger(username, password);
            if (b != null) {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://185.121.172.101:3306/zhgmzrgi_hanganimals", "zhgmzrgi_REST", "xcv123REST");
                PreparedStatement statement = con.prepareStatement("SELECT * FROM hang_users WHERE userid = '"+b.brugernavn+"';");
                ResultSet res = statement.executeQuery();
                if (res.first()) {
                    token = generateToken();
                    currency = res.getInt("currency");
                    singleplayer = res.getInt("singleplayer");
                    multiplayer = res.getInt("multiplayer");
                    animal = res.getString("animal");
                    animalcolor = res.getString("animalcolor");
                } else {
                    statement = con.prepareStatement("INSERT INTO hang_users (userid, currency, singleplayer, multiplayer, animal, animalcolor) VALUES ('"+b.brugernavn+"', '0', '0', '0', 'sheep', 'white');");
                    statement.execute();
                    token = generateToken();
                    currency = 0;
                    singleplayer = 0;
                    multiplayer = 0;
                    animal = "sheep";
                    animalcolor = "white";
                }
                User user = new User(
                        b.fornavn+" "+b.efternavn,
                        b.brugernavn,
                        "https://www.dtubasen.dtu.dk/showimage.aspx?id="+b.campusnetId,
                        b.studeretning,
                        currency,
                        singleplayer,
                        multiplayer,
                        animal,
                        animalcolor,
                        token
                );
                UserResource.onlineUsers.add(user);
                JSONObject object = new JSONObject();
                object.put("name", b.fornavn+" "+b.efternavn);
                object.put("userid", b.brugernavn);
                object.put("image", "https://www.dtubasen.dtu.dk/showimage.aspx?id="+b.campusnetId);
                object.put("study", b.studeretning);
                object.put("currency", currency);
                object.put("singleplayer", singleplayer);
                object.put("multiplayer", multiplayer);
                object.put("animal", animal);
                object.put("animalcolor", animalcolor);
                object.put("token", token);
                return object.toString();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw e;
        } catch (MalformedURLException e) {
            throw e;
        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
    
    private String generateToken() {
        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);
        return token;
    }
    
    
}
