package org.han.ica.oose.sneeuwklokje.controllers;

import org.han.ica.oose.sneeuwklokje.services.result.ResultService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("elections/result")
public class ResultRestController {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    private ResultService resultService;

    /**
     * Returns a list of closed elections
     *
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfClosedElections() {
        try {
            return Response.ok().entity(resultService.closedElectionResponse()).build();
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "Could not get closed election list from ResultRestController");
        }
        return Response.status(400).build();
    }

    /**
     * Returns the results of the election with the given electionId
     *
     * @param electionId
     * @return
     */
    @Path("{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response individualElectionResult(@PathParam("id") int electionId) {
        try {
            if (resultService.isElectionClosed(electionId)) {
                return Response.ok().entity(resultService.resultElectionResponse(electionId)).build();
            } else {
                LOGGER.log(Level.SEVERE, "Given electionID is not closed yet in ResultRestController");
                return Response.status(400).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not post id of election to retrieve the results in ResultRestController");

        }
        return Response.status(400).build();
    }
}
