package org.han.ica.oose.sneeuwklokje.controllers;

import org.han.ica.oose.sneeuwklokje.dtos.voter.VoterRequest;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.han.ica.oose.sneeuwklokje.services.voter.VoterService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoterRestControllerTest {
    @Mock
    private VoterService voterService;

    @InjectMocks
    private VoterRestController voterRestController;

    @Test
    public void testEmptyVoterRequest() throws SmartContractInteractionException {
        VoterRequest voterRequest = new VoterRequest();
        voterRequest.setId(-1);
        voterRequest.setToken("");
        when(voterService.doAuthenticationToken("")).thenReturn(false);
        Response response = voterRestController.vote(voterRequest);
        verify(voterService, Mockito.times(1)).doAuthenticationToken("");
        verify(voterService, Mockito.times(0)).pushVoteToBlockchain(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
        Assert.assertEquals(400, response.getStatus());
    }


    @Test
    public void testIfpushVoteToBlockchainExtractionReturnsStatus503() throws SmartContractInteractionException {
        VoterRequest request = Mockito.mock(VoterRequest.class);
        when(voterService.doAuthenticationToken(request.getToken())).thenReturn(true);
        when(voterService.pushVoteToBlockchain(request.getToken(), request.getId(), -1)).thenReturn(false);
        Response response = voterRestController.vote(request);
        Assert.assertEquals(503, response.getStatus());
    }

    @Test
    public void testIfpushVoteToBlockchainExtractionReturnsStatus500() throws SmartContractInteractionException {
        VoterRequest request = new VoterRequest();
        when(voterService.doAuthenticationToken(request.getToken())).thenReturn(true);
        when(voterService.pushVoteToBlockchain(request.getToken(),request.getId(),1)).thenThrow(new SmartContractInteractionException(("error")));
        when(voterService.getElectionIdBasedOnToken(request.getToken())).thenReturn(1);
        Response response = voterRestController.vote(request);
        Assert.assertEquals(500, response.getStatus());
    }

    @Test
    public void testValidVoterRequest() throws SmartContractInteractionException {
        String token = "xxxx-yyyy-zzzz";
        int memberId = 10;
        int electionId = 1;

        VoterRequest voterRequest = new VoterRequest();
        voterRequest.setId(memberId);
        voterRequest.setToken(token);

        when(voterService.doAuthenticationToken(token)).thenReturn(true);
        when(voterService.getElectionIdBasedOnToken(token)).thenReturn(electionId);
        when(voterService.pushVoteToBlockchain(token, memberId, electionId)).thenReturn(true);

        Response vote = voterRestController.vote(voterRequest);

        verify(voterService, Mockito.times(1)).doAuthenticationToken(token);
        verify(voterService, Mockito.times(1)).getElectionIdBasedOnToken(token);
        verify(voterService, Mockito.times(1)).pushVoteToBlockchain(token, memberId, electionId);

        Assert.assertEquals(200, vote.getStatus());
    }
}
