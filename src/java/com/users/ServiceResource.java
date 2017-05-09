package com.users;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author BigBaws
 */
@Path("service")
public class ServiceResource {
 
    @Context
    private UriInfo context;
 
    public ServiceResource() {
    }
 
    @GET
    @Produces("text/html")
    public String getHtml() {
        return "<b>Get</b>some REST!";
    }
 
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) {
    }
}