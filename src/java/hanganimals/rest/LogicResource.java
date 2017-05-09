package hanganimals.rest;

import java.math.BigInteger;
import java.security.SecureRandom;
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

@Path("/logic")
public class LogicResource {

    @Context
    private UriInfo context;

    public LogicResource() {
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getWord(@QueryParam("token") String token) {
        return "";
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
}
