package com.redhat.k8sthoughts;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.swing.plaf.ColorUIResource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Path("/api/thoughts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ThoughtOfTheDayResource {

    @Inject
    ThoughtOfTheDayRepository thoughtOfTheDayRepository;

    @GET
    public List<ThoughtOfTheDay> list() {
        return thoughtOfTheDayRepository.listAll();
    }

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final String CURRENT_DATE = LocalDate.now().format(DATE_FORMATTER);

    static final List<ThoughtOfTheDay> DEFAULT_THOUGHTS = List.of(
        new ThoughtOfTheDay("Quarkus Loves You", "Quarkus", CURRENT_DATE),
        new ThoughtOfTheDay("OpenShift Loves You", "OpenShift", CURRENT_DATE),
        new ThoughtOfTheDay("Red Hat Loves You", "Red Hat", CURRENT_DATE)
    );

    @GET
    @Path("/{id}")
    public ThoughtOfTheDay get(@PathParam("id") Long id) {
        Log.debugf("Fetching thought with id %d", id);
        Optional<ThoughtOfTheDay> thought = thoughtOfTheDayRepository.findByIdOptional(id);
        if(thought.isPresent()){
            Log.debugf("Thought with id %d found: %s", id, thought.get());
            return  thought.get();
        }else {
            Log.debugf("Thought with id %d does not exist.", id);
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
    }

    @GET
    @Path("/random")
    public ThoughtOfTheDay getRandom() {
        List<ThoughtOfTheDay> thoughts = thoughtOfTheDayRepository.listAll();
        if (thoughts.isEmpty()) {
            Log.debug("No thoughts available, returning default thoughts.");
            Random random = new Random();
            ThoughtOfTheDay thought = DEFAULT_THOUGHTS.get(random.nextInt(DEFAULT_THOUGHTS.size()));
            Log.debugf("Random default thought selected: %s", thought);
            return thought;
        }
        Random random = new Random();
        ThoughtOfTheDay thought = thoughts.get(random.nextInt(thoughts.size()));
        Log.debugf("Random thought selected: %s", thought);
        return thought;
    }

    @POST
    @Transactional
    public Response create(ThoughtOfTheDay thought) {
        thoughtOfTheDayRepository.persist(thought);
        return Response.status(201).entity(thought).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public ThoughtOfTheDay update(@PathParam("id") Long id, ThoughtOfTheDay thought) {
        Optional<ThoughtOfTheDay> existingThought = thoughtOfTheDayRepository.findByIdOptional(id);
        if (existingThought.isPresent()) {
            ThoughtOfTheDay existing = existingThought.get();
            existing.thought = thought.thought;
            existing.author = thought.author;
            existing.day = thought.day;
            thoughtOfTheDayRepository.persist(existing);
            return existing;
        }else{
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if(thoughtOfTheDayRepository.deleteById(id)){
            return Response.status(204).build();
        }else {
            throw new WebApplicationException("Thought with id " + id + " does not exist.", 404);
        }
    }
} 