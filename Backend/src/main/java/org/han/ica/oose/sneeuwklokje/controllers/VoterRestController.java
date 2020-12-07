package org.han.ica.oose.sneeuwklokje.controllers;

import org.han.ica.oose.sneeuwklokje.dtos.voter.VoterRequest;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.han.ica.oose.sneeuwklokje.services.voter.VoterService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("vote")
public class VoterRestController {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    private VoterService voterService;

    /**
     * Pushes a vote to the blockchain
     * @param request
     * @return Response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response vote(VoterRequest request) {
        String token = request.getToken();
        if (checkAuthenticationToken(token)) return Response.status(400).build();
        return pushVoteToBlockchainExtraction(request, token);
    }

    private Response pushVoteToBlockchainExtraction(VoterRequest request, String token) {
        int electionId = voterService.getElectionIdBasedOnToken(token);

        try {
            if (voterService.pushVoteToBlockchain(token, request.getId(), electionId)) {
                return Response.ok().build();
            } else {
                LOGGER.log(Level.SEVERE, "Could not push vote to blockchain in VoterRestController");
                return Response.status(503).build();
            }
        } catch (SmartContractInteractionException e) {
            LOGGER.log(Level.SEVERE, "SmartContractInteractionException in VoterRestController");
            return Response.status(500).build();
        }
    }

    private boolean checkAuthenticationToken(String token) {
        return (!voterService.doAuthenticationToken(token));
    }
}

