package org.han.ica.oose.sneeuwklokje.services.result;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.contracts.Election;
import org.han.ica.oose.sneeuwklokje.database.admin.AdminDao;
import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.result.ElectionResultResponse;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.han.ica.oose.sneeuwklokje.services.election.ElectionService;
import org.han.ica.oose.sneeuwklokje.services.result.ResultServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResultServiceImplTest {

    @Mock
    private AdminDao adminDao;

    @Mock
    private ContractConnector contractConnector;

    @Mock
    private ElectionService electionService;

    @InjectMocks
    private ResultServiceImpl resultService;

    @Test
    public void TestIfClosedElectionResponseReturnsElection(){
        ElectionListResponse electionListResponse = new ElectionListResponse();
        when(adminDao.getElections('<')).thenReturn(electionListResponse);
        ElectionListResponse resp = resultService.closedElectionResponse();
        Assert.assertEquals(electionListResponse, resp);
    }

    @Test
    public void TestIfClosedElectionResponseReturnsNull(){
        when(adminDao.getElections('<')).thenReturn(null);
        ElectionListResponse resp = resultService.closedElectionResponse();
        Assert.assertEquals(null, resp);
    }

    @Test
    public void TestIfIsElectionClosedReturnsTrue(){
        int id = 1;
        when(resultService.isElectionClosed(id)).thenReturn(true);
        boolean resp = resultService.isElectionClosed(id);
        Assert.assertEquals(true, resp);
    }

    @Test
    public void TestIfIsElectionClosedReturnsFalse(){
        int id = 1;
        when(resultService.isElectionClosed(id)).thenReturn(false);
        boolean resp = resultService.isElectionClosed(id);
        Assert.assertEquals(false, resp);
    }

    @Test
    public void TestIfResultElectionResponseReturnsElectionResultResponse() throws SmartContractInteractionException {
//        int id = 1;
//        when(contractConnector.getElection(electionService.getSmartContractAddress(id))).thenReturn(null);
//        //when(adminDao.getElections('<')).thenReturn(electionListResponse);
//        ElectionResultResponse resp = resultService.resultElectionResponse(id);
//        when(resultService.resultElectionResponse(id)).thenReturn(resp);
//       // verify(resultService, Mockito.times(1)).resultElectionResponse(id);
//        Assert.assertEquals(resultService.resultElectionResponse(id), resp);
    }
}

