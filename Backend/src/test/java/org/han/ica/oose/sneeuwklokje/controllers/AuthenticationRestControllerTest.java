package org.han.ica.oose.sneeuwklokje.controllers;

import org.han.ica.oose.sneeuwklokje.dtos.authentication.AuthenticationRequest;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.han.ica.oose.sneeuwklokje.services.election.ElectionService;
import org.han.ica.oose.sneeuwklokje.services.voter.VoterService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationRestControllerTest {

    @Mock
    private VoterService voterService;

    @Mock
    private ElectionService electionService;

    @Mock
    private AuthenticationRequest rq;

    @InjectMocks
    private AuthenticationRestController authenticationRestController;

    @Before
    public void setup() {
        rq = new AuthenticationRequest();
    }

    @Test
    public void testIfEmptyRequestReturnsStatus403() {
        when(voterService.doAuthenticationToken("")).thenReturn(false);
        when(electionService.getElectionIdBasedOnToken("")).thenReturn(1);
        Response resp = authenticationRestController.authenticate(rq);
        Assert.assertEquals(403, resp.getStatus());
    }

     @Test(expected = MockitoException.class)
     public void testIfEmptyRequestReturnsStatus500() {
         when(voterService.doAuthenticationToken("")).thenThrow(new SmartContractInteractionException("e"));
         when(electionService.getElectionIdBasedOnToken("")).thenReturn(1);
         Mockito.doThrow(new SmartContractInteractionException("message")).when(voterService.doAuthenticationToken(""));
         Response resp = authenticationRestController.authenticate(rq);
         Assert.assertEquals(500, resp.getStatus());
     }

    @Test
    public void testIfAuthenticateRequestReturnsStatus200() {
        rq.setToken("1111-1111-1111");
        when(voterService.doAuthenticationToken(rq.getToken())).thenReturn(true);
        when(electionService.getElectionIdBasedOnToken(rq.getToken())).thenReturn(4);
        when(electionService.isElectionBeforeEndDate(4)).thenReturn(true);
        Response resp = authenticationRestController.authenticate(rq);
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testCatchInAuthenticate() throws SmartContractInteractionException {
        when(electionService.getElectionIdBasedOnToken(rq.getToken())).thenReturn(1);
        when(voterService.doAuthenticationToken(rq.getToken())).thenReturn(true);
        when(electionService.isElectionBeforeEndDate(1)).thenReturn(true);
        when(electionService.createBallotResponse(1)).thenThrow(new SmartContractInteractionException("error"));
        Response resp = authenticationRestController.authenticate(rq);
        Assert.assertEquals(500, resp.getStatus());
    }
}
