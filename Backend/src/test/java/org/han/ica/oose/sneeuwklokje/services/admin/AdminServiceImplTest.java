package org.han.ica.oose.sneeuwklokje.services.admin;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.contracts.Election;
import org.han.ica.oose.sneeuwklokje.database.admin.AdminDao;
import org.han.ica.oose.sneeuwklokje.database.admin.AdminDaoImplTest;
import org.han.ica.oose.sneeuwklokje.database.util.SQLConnection;
import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.admin.newelection.PartyListResponse;
import org.han.ica.oose.sneeuwklokje.exceptions.NoPlannedElectionException;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.han.ica.oose.sneeuwklokje.services.admin.AdminServiceImpl;
import org.han.ica.oose.sneeuwklokje.services.election.ElectionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.web3j.abi.datatypes.Array;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceImplTest {

    @Mock
    private AdminDao adminDao;

    @Mock
    ElectionService mockElectionService;

    @Mock
    ContractConnector mockContractConnector;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    public void testIfGetRunningElectionsReturnsListOfElections(){
        ElectionListResponse electionListResponse = new ElectionListResponse();
        when(adminDao.getElections('>')).thenReturn(electionListResponse);
        ElectionListResponse resp = adminService.getRunningElections();
        Assert.assertEquals(electionListResponse, resp);
    }

    @Test
    public void testIfGetRunningElectionsReturnsNull(){
        when(adminDao.getElections('>')).thenReturn(null);
        ElectionListResponse resp = adminService.getRunningElections();
        Assert.assertEquals(null, resp);
    }

    @Test
    public void testGetPartiesForMakingNewElection() {
        PartyListResponse partyListResponse = new PartyListResponse();
        when(adminDao.getPartiesFromDatabase()).thenReturn(partyListResponse);
        PartyListResponse resp = adminService.getPartiesForMakingNewElection();
        Assert.assertEquals(partyListResponse, resp);
    }

    @Test
    public void testGetPartiesForMakingNewElectionToCatch() {
        when(adminDao.getPartiesFromDatabase()).thenReturn(null);
        PartyListResponse resp = adminService.getPartiesForMakingNewElection();
        Assert.assertEquals(null, resp);
    }

    @Test
    public void testCloseRunningElection() throws SmartContractInteractionException {
        Election election = null;
        when(mockContractConnector.getElection("Address")).thenReturn(election);
        when(mockElectionService.getSmartContractAddress(1)).thenReturn("address");
        adminService.closeRunningElection(1);
        verify(adminDao, times(1)).closeRunningElectionInDatabase(1);
    }

    @Test
    public void testCreateNewElection() throws ParseException, SQLException {
        int[] intArray = new int[3];
        adminService.createNewElection("name", "date", "date", intArray);
        verify(adminDao, times(1)).insertNewElectionInDatabase("name", "date", "date");
    }

}
