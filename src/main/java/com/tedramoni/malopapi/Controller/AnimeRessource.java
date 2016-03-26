package com.tedramoni.malopapi.Controller;

import com.tedramoni.malopapi.Model.Anime;
import com.tedramoni.malopapi.Parser.AnimeParser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by Ted on 22/03/2016.
 */
@Path("anime")
public class AnimeRessource {

    AnimeParser animeParser = new AnimeParser();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response getAnimeOpening(@PathParam("id") Integer id ){
        Anime anime = null;
        try {
            anime = animeParser.crawl(id);
        } catch (IOException e) {
            return Response.status(404).build();
        }
        return Response.status(200).entity(anime).build();
    }
}
