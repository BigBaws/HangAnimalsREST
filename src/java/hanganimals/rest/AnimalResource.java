/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hanganimals.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author BigBaws
 */
@Path("/animal")
public class AnimalResource {

    @Context
    private UriInfo context;
    
    public AnimalResource() {
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAnimal(@QueryParam("username") String username) throws Exception{
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://185.121.172.101:3306/zhgmzrgi_hanganimals", "zhgmzrgi_REST", "xcv123REST");
            PreparedStatement statement = con.prepareStatement("SELECT * FROM hang_users");
            ResultSet res = statement.executeQuery();
            res.first();
            return res.getString("userid");
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * PUT method for updating or creating an instance of AnimalResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
