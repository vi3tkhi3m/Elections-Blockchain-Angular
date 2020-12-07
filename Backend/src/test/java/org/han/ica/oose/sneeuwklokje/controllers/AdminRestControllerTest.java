package org.han.ica.oose.sneeuwklokje.controllers;

import org.han.ica.oose.sneeuwklokje.dtos.admin.newelection.NewElectionRequest;
import org.han.ica.oose.sneeuwklokje.services.admin.AdminService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminRestControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminRestController adminRestController;

    @Test
    public void TestIfOpenAdminPageAndShowRunningElectionsReturnsStatus200(){
        Response resp = adminRestController.openAdminPageAndShowRunningElections();
        verify(adminService, Mockito.times(1)).getRunningElections();
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void TestIfCloseRunningElectionOnAdminPageReturnsStatus200() {
        Response resp = adminRestController.closeRunningElectionOnAdminPage(1);
        verify(adminService, Mockito.times(1)).closeRunningElection(1);
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void TestIfgetPartiesForCreatingNewElectionsReturnsStatus200() {
        Response resp = adminRestController.getPartiesForCreatingNewElections();
        verify(adminService, Mockito.times(1)).getPartiesForMakingNewElection();
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void TestIfpostDataForCreatingNewElectionReturnsStatus200() {
        NewElectionRequest mockedNewElectionRequest = Mockito.mock(NewElectionRequest.class);
        Response resp = adminRestController.postDataForCreatingNewElection(mockedNewElectionRequest);
        verify(adminService, Mockito.times(1)).createNewElection(mockedNewElectionRequest.getName(),mockedNewElectionRequest.getStartDate(),mockedNewElectionRequest.getEndDate(),mockedNewElectionRequest.getPartyIds());
        Assert.assertEquals(200, resp.getStatus());
    }


    @Test
    public void TestCatchInOpenAdminPageAndShowRunningElections() {
        when(adminService.getRunningElections()).thenThrow(new NullPointerException());
        Response resp = adminRestController.openAdminPageAndShowRunningElections();
        verify(adminService, Mockito.times(1)).getRunningElections();
        Assert.assertEquals(400, resp.getStatus());
    }

    @Test
    public void TestCatchInGetPartiesForCreatingNewElections() {
        when(adminService.getPartiesForMakingNewElection()).thenThrow(new NullPointerException());
        Response resp = adminRestController.getPartiesForCreatingNewElections();
        verify(adminService, Mockito.times(1)).getPartiesForMakingNewElection();
        Assert.assertEquals(400, resp.getStatus());
    }

    @Test
    public void TestCatchCloseRunningElectionOnAdminPage() {
        doThrow(Exception.class).when(adminService).closeRunningElection(0);
        Response resp = adminRestController.closeRunningElectionOnAdminPage(0);
        verify(adminService, Mockito.times(1)).closeRunningElection(0);
        Assert.assertEquals(400, resp.getStatus());
    }

    @Test
    public void TestCatchPostDataForCreatingNewElection() {
        NewElectionRequest request = new NewElectionRequest();
        doThrow(Exception.class).when(adminService).createNewElection(request.getName(),request.getStartDate(),request.getEndDate(), request.getPartyIds());
        Response resp = adminRestController.postDataForCreatingNewElection(request);
        verify(adminService, Mockito.times(1)).createNewElection(request.getName(),request.getStartDate(),request.getEndDate(), request.getPartyIds());
        Assert.assertEquals(400, resp.getStatus());
    }
}

