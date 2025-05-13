package com.redhat.k8sthoughts;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Random;

@Path("/api/thoughts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ThoughtOfTheDayResource {

    @GET
    public List<ThoughtOfTheDay> list() {
        return ThoughtOfTheDay.listAll();
    }

    @GET
    @Path("/{id}")
    public ThoughtOfTheDay get(@PathParam("id") Long id) {
        ThoughtOfTheDay thought = ThoughtOfTheDay.findById(id);
        if (thought == null) {
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
        return thought;
    }

    @GET
    @Path("/random")
    public ThoughtOfTheDay getRandom() {
        List<ThoughtOfTheDay> thoughts = ThoughtOfTheDay.listAll();
        if (thoughts.isEmpty()) {
            throw new WebApplicationException("No thoughts available.", 404);
        }
        Random random = new Random();
        return thoughts.get(random.nextInt(thoughts.size()));
    }

    @POST
    @Transactional
    public Response create(ThoughtOfTheDay thought) {
        thought.persist();
        return Response.status(201).entity(thought).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public ThoughtOfTheDay update(@PathParam("id") Long id, ThoughtOfTheDay thought) {
        ThoughtOfTheDay existingThought = ThoughtOfTheDay.findById(id);
        if (existingThought == null) {
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
        
        existingThought.thought = thought.thought;
        existingThought.author = thought.author;
        existingThought.day = thought.day;
        
        return existingThought;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        ThoughtOfTheDay thought = ThoughtOfTheDay.findById(id);
        if (thought == null) {
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
        thought.delete();
        return Response.status(204).build();
    }
} 