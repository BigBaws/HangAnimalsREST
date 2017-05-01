package com.example;

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
@Path("/MultiPlayerLogic")
public class MultiplayerLogicResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MultiPlayerLogicResource
     */
    public MultiplayerLogicResource() {}

    /**
     * Retrieves representation of an instance of com.example.MultiPlayerLogicResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of MultiPlayerLogicResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}