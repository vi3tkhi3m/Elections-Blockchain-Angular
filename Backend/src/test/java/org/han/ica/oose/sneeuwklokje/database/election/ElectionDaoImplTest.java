package org.han.ica.oose.sneeuwklokje.database.election;

import org.han.ica.oose.sneeuwklokje.database.util.SQLConnection;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ElectionDaoImplTest {

    @Mock
    Connection mockConn;
    @Mock
    PreparedStatement mockPreparedStmnt;
    @Mock
    ResultSet mockResultSet;

    @InjectMocks
    private ElectionDao electionDao = new ElectionDaoImpl();

    @Mock
    private SQLConnection mockSqlConnection;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void init() throws SQLException {
        when(mockSqlConnection.getConnection()).thenReturn(mockConn);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetIdOfElectionBasedOnToken() throws SQLException {
        electionDao.getIdOfElectionBasedOnToken("test");

        //verify and assert
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getInt("id");
    }

    @Test
    public void testGetIdOfElectionBasedOnTokenReturnZero() throws SQLException {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(0, electionDao.getIdOfElectionBasedOnToken("test"));
    }

    @Test
    public void testGetSmartContractAddressOfElectionId() throws SQLException {
        electionDao.getSmartContractAddressOfElectionId(1);

        //verify and assert
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getString("Address");
    }

    @Test
    public void testGetSmartContractAddressOfElectionIdToReturnNull() throws SQLException {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(null, electionDao.getSmartContractAddressOfElectionId(1));
    }

    @Test
    public void testIsElectionBeforeEndDate() throws SQLException {
        electionDao.isElectionBeforeEndDate(1);

        //verify and assert
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
    }

    @Test
    public void testIsElectionBeforeEndDateToReturnFalse() throws SQLException {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(false, electionDao.isElectionBeforeEndDate(1));
    }

}
