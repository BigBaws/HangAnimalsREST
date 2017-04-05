package com.example;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import hanganimals.gamelogic.SinglePlayerLogic;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import javax.servlet.ServletException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/Login")
public class LoginResource {
    

    //ArrayList<MultiPlayerGameObjects> multiplayerGames = new ArrayList<>();
    
    @Context
    private UriInfo context;
    
    public LoginResource() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String rmiLogin(@QueryParam("Username") String username, @QueryParam("Password") String password) throws Exception {
        try {
            Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
            Bruger b = ba.hentBruger(username, password);
            if (b != null) {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://185.121.172.101:3306/zhgmzrgi_hanganimals", "zhgmzrgi_REST", "xcv123REST");
                PreparedStatement statement = con.prepareStatement("SELECT * FROM hang_users WHERE userid = '"+b.brugernavn+"';");
                ResultSet res = statement.executeQuery();
                if (res.first()) {
                    String token = generateToken();
                    return "{"
                        + "\"name\":\""+b.fornavn+" "+b.efternavn+"\", "
                        + "\"id\":\""+b.brugernavn+"\", "
                        + "\"image\":\"https://www.dtubasen.dtu.dk/showimage.aspx?id="+b.campusnetId+"\", "
                        + "\"study\":\""+b.studeretning+"\", "
                        + "\"currency\":\""+res.getInt("currency")+"\", "
                        + "\"gameid\":\""+res.getInt("gameid")+"\", "
                        + "\"animal\":\""+res.getString("animal")+"\", "
                        + "\"animalcolor\":\""+res.getString("animalcolor")+"\" "
                        + "\"token\":\""+token+"\" "
                        + "}";
                } else {
                    statement = con.prepareStatement("INSERT INTO hang_users (userid, currency, gameid, animal, animalcolor) VALUES ('"+b.brugernavn+"', '0', '0', 'sheep', 'white');");
                    statement.execute();
                    String token = generateToken();
                    return "{"
                        + "\"name\":\""+b.fornavn+" "+b.efternavn+"\", "
                        + "\"id\":\""+b.brugernavn+"\", "
                        + "\"image\":\"https://www.dtubasen.dtu.dk/showimage.aspx?id="+b.campusnetId+"\", "
                        + "\"study\":\""+b.studeretning+"\", "
                        + "\"currency\":\"0\", "
                        + "\"gameid\":\"0\", "
                        + "\"animal\":\"sheep\", "
                        + "\"animalcolor\":\"white\", "
                        + "\"token\":\""+token+"\" "
                        + "}";
                }
            } else {
                return null;
            }
        } catch (NotBoundException e) {
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
