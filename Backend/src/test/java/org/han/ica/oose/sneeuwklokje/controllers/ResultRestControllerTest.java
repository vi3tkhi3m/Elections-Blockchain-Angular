package org.han.ica.oose.sneeuwklokje.controllers;

import org.han.ica.oose.sneeuwklokje.services.result.ResultService;
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
public class ResultRestControllerTest {

    @Mock
    private ResultService resultService;

    @InjectMocks
    private ResultRestController resultRestController;

    @Test
    public void testIfGetListOfClosedElectionsReturnsStatus200() {
        Response resp = resultRestController.getListOfClosedElections();
        verify(resultService, Mockito.times(1)).closedElectionResponse();
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testIfGetListOfClosedElectionsReturnsStatus400() {
        when(resultService.closedElectionResponse()).thenThrow(NullPointerException.class);
        Response resp = resultRestController.getListOfClosedElections();
        verify(resultService, Mockito.times(1)).closedElectionResponse();
        Assert.assertEquals(400, resp.getStatus());
    }

    @Test
    public void testIfGetElectionResultReturnsStatus200() {
        when(resultService.isElectionClosed(4)).thenReturn(true);
        Response resp = resultRestController.individualElectionResult(4);
        verify(resultService, Mockito.times(1)).resultElectionResponse(4);
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testIfGetElectionResultReturnsStatus400() {
        when(resultService.isElectionClosed(4)).thenReturn(false);
        Response resp = resultRestController.individualElectionResult(4);
        Assert.assertEquals(400, resp.getStatus());
    }

    @Test
    public void testCatchInPostElectionResult() {
        when(resultService.isElectionClosed(4)).thenThrow(Exception.class);
        Response resp = resultRestController.individualElectionResult(4);
        Assert.assertEquals(400, resp.getStatus());
    }
}
