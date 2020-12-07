package org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ElectionListResponseTest {

    @Mock
    Election election;

    @InjectMocks
    ElectionListResponse electionListResponse;

    @Before
    public void init() {
        election.setElectionID(1);
        election.setElectionName("naam");
        election.setEndDate("date");
        election.setStartDate("date");
    }

    @Test
    public void testSetElection() {
        List<Election> electionList = new ArrayList<>();
        electionListResponse.setElections(electionList);
    }

    @Test
    public void testGetElection() {
        int size = electionListResponse.getElections().size();
        Assert.assertEquals(size, electionListResponse.getElections().size());
    }

    @Test
    public void testAddElection() {
        electionListResponse.addElection(election);
        Assert.assertEquals(1, electionListResponse.getElections().size());
    }
}
