package org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ElectionTest {

    @InjectMocks
    Election election;

    @Before
    public void init() {
        election.setElectionID(1);
        election.setElectionName("naam");
        election.setEndDate("date");
        election.setStartDate("date");
    }

    @Test
    public void testElectionSetter() {
        Assert.assertEquals(1, election.getElectionID());
        Assert.assertEquals("naam", election.getElectionName());
        Assert.assertEquals("date", election.getEndDate());
        Assert.assertEquals("date", election.getStartDate());
    }

    @Test
    public void testElectionGetter() {
        int electionID = election.getElectionID();
        String electionName = election.getElectionName();
        String electionEndDate = election.getEndDate();
        String electionStartDate = election.getStartDate();
        Assert.assertEquals(election.getElectionID(), electionID);
        Assert.assertEquals(election.getElectionName(), electionName);
        Assert.assertEquals(election.getEndDate(), electionEndDate);
        Assert.assertEquals(election.getStartDate(), electionStartDate);
    }

}
