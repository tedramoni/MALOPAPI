package com.tedramoni.malopapi.Controller;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Ted on 22/03/2016.
 */
@Path("/")
public class ApiRessource {

    static Logger log = Logger.getLogger(ApiRessource.class.getName());

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("ping")
    public Response ping(@Context HttpServletRequest request){
        log.debug("Ping from: "+request.getRemoteAddr());
        return Response.status(200).entity("OK").build();
    }
}
