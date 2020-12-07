package org.han.ica.oose.sneeuwklokje.services.voter;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.database.voter.VoterDao;
import org.han.ica.oose.sneeuwklokje.services.election.ElectionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.stubVoid;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoterServiceImplTest {

    @Mock
    private VoterDao voterDao;

    @Mock
    ContractConnector mockContractConnector;

    @Mock
    private ElectionService electionService;

    @InjectMocks
    private VoterServiceImpl voterService;

    @Test
    public void testIfDoAuthenticationTokenReturnsTrue() {
        String token = "1111-1111-1111";
        when(voterDao.checkAuthenticationToken(token)).thenReturn(true);
        boolean authentication = voterService.doAuthenticationToken(token);
        Assert.assertEquals(true, authentication);
    }

    @Test
    public void testIfDoAuthenticationTokenReturnsFalse() {
        String token = "1111-1111-1111";
        when(voterDao.checkAuthenticationToken(token)).thenReturn(false);
        boolean authentication = voterService.doAuthenticationToken(token);
        Assert.assertEquals(false, authentication);
    }

    @Test
    public void testIfDeleteTokenFromDatabaseAfterVoteRuns(){
        String token = "1111-1111-1111";
        stubVoid(voterDao).toReturn().on().deleteTokenAfterVote(token);
    }

    @Test
    public void testIfGetSmartContractAddressForElectionReturnsString() {
        String testaddress = "1234";
        when(electionService.getSmartContractAddress(1)).thenReturn(testaddress);
        String address = voterService.getSmartContractAddressForElection(1);
        Assert.assertEquals(testaddress, address);
    }

    @Test
    public void testIfgetElectionIdBasedOnTokenReturnsCorrectInt() {
        String token = "1111-1111-1111";
        when(electionService.getElectionIdBasedOnToken(token)).thenReturn(1);
        int id = voterService.getElectionIdBasedOnToken(token);
        Assert.assertEquals(1, id);
    }

//    @Test
//    public void testPushVoteToBlockchain() throws Exception {
//        Election electionContract = mock(Election.class);
//        TransactionReceipt receipt = mock(TransactionReceipt.class);
//        String status = "0x1";
//        receipt.setStatus(status);
//        when(mockContractConnector.getElection("Address")).thenReturn(electionContract);
//        when(electionContract.vote(BigInteger.valueOf(anyInt()), eq("token")).send()).thenReturn(receipt);
//
//        Assert.assertEquals(true,voterService.pushVoteToBlockchain("1",1,1));
//    }
}
