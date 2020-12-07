package org.han.ica.oose.sneeuwklokje.database.voter;

import org.han.ica.oose.sneeuwklokje.database.election.ElectionDao;
import org.han.ica.oose.sneeuwklokje.database.election.ElectionDaoImpl;
import org.han.ica.oose.sneeuwklokje.database.util.SQLConnection;
import org.han.ica.oose.sneeuwklokje.services.election.ElectionService;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoterDaoImplTest {
    @Mock
    Connection mockConn;
    @Mock
    PreparedStatement mockPreparedStmnt;
    @Mock
    ResultSet mockResultSet;

    @InjectMocks
    private VoterDao voterDao = new VoterDaoImpl();

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
    public void testCheckAuthenticationToken() throws SQLException {
        voterDao.checkAuthenticationToken("1");

        //verify and assert
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
    }

    @Test
    public void testCheckAuthenticationTokenToReturnFalse() throws SQLException {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(false, voterDao.checkAuthenticationToken("1"));
    }

    @Test
    public void testDeleteTokenAfterVote() throws SQLException {
        voterDao.deleteTokenAfterVote("1");

        //verify and assert
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteTokenAfterVoteToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            voterDao.deleteTokenAfterVote("1");
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setString(anyInt(), anyString());
            verify(mockPreparedStmnt, times(0)).executeUpdate();
            throw se;
        }
    }
}
