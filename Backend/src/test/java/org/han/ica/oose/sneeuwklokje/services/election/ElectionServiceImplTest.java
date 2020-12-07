package org.han.ica.oose.sneeuwklokje.services.election;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.contracts.Election;
import org.han.ica.oose.sneeuwklokje.database.election.ElectionDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ElectionServiceImplTest {
    @Mock
    private ElectionDao electionDao;

    @Mock
    Election electionContract;

    @InjectMocks
    private ElectionServiceImpl electionService;

    @Mock
    ContractConnector mockContractConnector;

    @Test
    public void testIfGetElectionIdBasedOnTokenReturnsOneIfTokenIsInDatabase() throws SQLException {
        String token = "1111-1111-1111";
        when(electionDao.getIdOfElectionBasedOnToken(token)).thenReturn(1);
        int election = electionService.getElectionIdBasedOnToken(token);
        Assert.assertEquals(1, election);
    }

    @Test
    public void testIfGetElectionIdBasedOnTokenReturnsZeroIfTokenIsNotInDatabase() throws SQLException {
        String token = "1111-1111-1111";
        when(electionDao.getIdOfElectionBasedOnToken(token)).thenReturn(0);
        int election = electionService.getElectionIdBasedOnToken(token);
        Assert.assertEquals(0, election);
    }

    @Test
    public void testIfGetSmartContractAddressReturnsSmartContractAddress(){
        String address = "1234";
        when(electionDao.getSmartContractAddressOfElectionId(1)).thenReturn(address);
        String resp = electionService.getSmartContractAddress(1);
        Assert.assertEquals(address, resp);
    }

    @Test
    public void testIfGetSmartContractAddressReturnsNull(){
        when(electionDao.getSmartContractAddressOfElectionId(1)).thenReturn(null);
        String resp = electionService.getSmartContractAddress(1);
        Assert.assertEquals( null, resp);
    }

    @Test
    public void testIfIsElectionBeforeEndDateReturnsTrue(){
        when(electionDao.isElectionBeforeEndDate(1)).thenReturn(true);
        boolean resp = electionService.isElectionBeforeEndDate(1);
        Assert.assertEquals( true, resp);
    }

    @Test
    public void testIfIsElectionBeforeEndDateReturnsFalse(){
        when(electionDao.isElectionBeforeEndDate(1)).thenReturn(false);
        boolean resp = electionService.isElectionBeforeEndDate(1);
        Assert.assertEquals( false, resp);
    }

    @Test
    public void testGetElectionIdBasedOnToken() {
        when(electionService.getElectionIdBasedOnToken("1")).thenReturn(1);
        Assert.assertEquals(1, electionService.getElectionIdBasedOnToken("1"));
    }


}
