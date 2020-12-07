package org.han.ica.oose.sneeuwklokje.database.admin;

import java.lang.reflect.Method;
import java.sql.*;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.sql.DataSource;
import javax.validation.constraints.AssertFalse;

import org.han.ica.oose.sneeuwklokje.database.Dao;
import org.han.ica.oose.sneeuwklokje.database.util.NamedQueries;
import org.han.ica.oose.sneeuwklokje.database.util.SQLConnection;
import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.admin.newelection.PartyListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.election.ElectionInfo;
import org.han.ica.oose.sneeuwklokje.dtos.election.PartyInfo;
import org.junit.*;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminDaoImplTest {

    @Mock
    public
    Connection mockConn;
    @Mock
    public
    PreparedStatement mockPreparedStmnt;
    @Mock
    ResultSet mockResultSet;

    @InjectMocks
    private AdminDao adminDao = new AdminDaoImpl();

    @Mock
    private SQLConnection mockSqlConnection;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void init() throws SQLException, ParseException {
        java.sql.Date startDate = new java.sql.Date(2018, 6, 5);
        java.sql.Date endDate = new java.sql.Date(2019, 6, 5);
        when(mockSqlConnection.getConnection()).thenReturn(mockConn);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(mockResultSet.getDate("startDate")).thenReturn(startDate);
        when(mockResultSet.getDate("endDate")).thenReturn(endDate);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetElections() throws SQLException {
        adminDao.getElections('>' );

        //verify and assert
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
        verify(mockResultSet, times(1)).getInt("id");
        verify(mockResultSet, times(1)).getString("name");
        verify(mockResultSet, times(1)).getDate("startDate");
        verify(mockResultSet, times(1)).getDate("endDate");
    }

    @Test
    public void testGetElectionsToCatch() throws SQLException {
        try {
            adminDao.getElections('>' );
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).executeQuery();
            verify(mockResultSet, times(0)).next();
            verify(mockResultSet, times(0)).getInt("id");
            verify(mockResultSet, times(0)).getString("name");
            verify(mockResultSet, times(0)).getDate("startDate");
            verify(mockResultSet, times(0)).getDate("endDate");
            throw se;
        }
    }

    @Test
    public void testGetElectionsToReturnNull() throws SQLException {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(null, adminDao.getElections('>'));
    }

    @Test
    public void testCloseRunningElectionInDatabase() throws SQLException {
        adminDao.closeRunningElectionInDatabase(1);
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
    }

    @Test
    public void testCloseRunningElectionInDatabaseToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            adminDao.closeRunningElectionInDatabase(1);
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setInt(anyInt(), anyInt());
            verify(mockPreparedStmnt, times(0)).executeUpdate();
            throw se;
        }
    }

    @Test
    public void testIsElectionClosed() throws SQLException {
        adminDao.isElectionClosed(1);
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getDate("endDate");
    }

    @Test
    public void testIsElectionClosedToReturnFalse() throws SQLException {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(false, adminDao.isElectionClosed(1));
    }

    @Test
    public void testGetPartiesFromDatabase() throws SQLException {
        adminDao.getPartiesFromDatabase();
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).getInt("id");
        verify(mockResultSet, times(1)).getString("name");
    }

    @Test
    public void testGetPartiesFromDatabaseReturnNull() throws SQLException {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(null, adminDao.getPartiesFromDatabase());
    }

    @Test
    public void testInsertNewElectionInDatabase() throws ParseException, SQLException {
        adminDao.insertNewElectionInDatabase("test", "2018-12-12", "2019-12-12");
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(2)).setDate(anyInt(), any());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
    }

    @Test
    public void testInsertNewElectionInDatabaseToCatch() throws ParseException, SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            adminDao.insertNewElectionInDatabase("test", "2018-12-12", "2019-12-12");
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setString(anyInt(), anyString());
            verify(mockPreparedStmnt, times(0)).setDate(anyInt(), any());
            verify(mockPreparedStmnt, times(0)).executeUpdate();
            throw se;
        }
    }

    @Test
    public void testInsertPartiesForNewElection() throws SQLException {
        adminDao.insertPartiesForNewElection("name", 1);
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
    }

    @Test
    public void testInsertPartiesForNewElectionToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            adminDao.insertPartiesForNewElection("name", 1);
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setString(anyInt(), anyString());
            verify(mockPreparedStmnt, times(0)).setInt(anyInt(), anyInt());
            verify(mockPreparedStmnt, times(0)).executeUpdate();
            throw se;
        }
    }

    @Test
    public void testGetPlannedElections() throws SQLException {
        adminDao.getPlannedElections();
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
    }

    @Test
    public void testGetPlannedElectionsToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            adminDao.getPlannedElections();
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).executeQuery();
            verify(mockResultSet, times(0)).next();
            throw se;
        }
    }

    @Test
    public void testIsPlannedElection() throws SQLException {
        adminDao.isPlannedElection(1);
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getInt("id");
    }

    @Test
    public void testIsPlannedElectionToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            adminDao.isPlannedElection(1);
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setInt(anyInt(), anyInt());
            verify(mockPreparedStmnt, times(0)).executeQuery();
            verify(mockResultSet, times(0)).next();
            verify(mockResultSet, times(0)).getInt("id");
            throw se;
        }
    }

    @Test
    public void testIsPlannedElectionToReturnFalse() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        adminDao.isPlannedElection(1);
        assertFalse(adminDao.isPlannedElection(1));
    }

    @Test
    public void testGetPlannedPartiesInElection() throws SQLException {
        adminDao.getPlannedPartiesInElection(1);
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
        verify(mockResultSet, times(1)).getString("name");
        verify(mockResultSet, times(1)).getInt("id");
    }

    @Test
    public void testGetPlannedPartiesInElectionToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            adminDao.getPlannedPartiesInElection(1);
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setInt(anyInt(), anyInt());
            verify(mockPreparedStmnt, times(0)).executeQuery();
            verify(mockResultSet, times(0)).next();
            verify(mockResultSet, times(0)).getString("name");
            verify(mockResultSet, times(0)).getInt("id");
            throw se;
        }
    }

    @Test
    public void testGetPlannedMembersInElection() throws SQLException {
        List<PartyInfo> partyInfos = new ArrayList<>();
        adminDao.getPlannedMembersInElection(1, partyInfos);
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
        verify(mockResultSet, times(1)).getInt("position");
        verify(mockResultSet, times(1)).getString("lastname");
        verify(mockResultSet, times(1)).getString("initials");
        verify(mockResultSet, times(1)).getString("firstname");
        verify(mockResultSet, times(1)).getString("gender");
        verify(mockResultSet, times(1)).getString("location");
        verify(mockResultSet, times(1)).getInt("Party_ID");
    }

    @Test
    public void testGetPlannedMembersInElectionToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            List<PartyInfo> partyInfos = new ArrayList<>();
            adminDao.getPlannedMembersInElection(1, partyInfos);
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setInt(anyInt(), anyInt());
            verify(mockPreparedStmnt, times(0)).executeQuery();
            verify(mockResultSet, times(0)).next();
            verify(mockResultSet, times(0)).getInt("position");
            verify(mockResultSet, times(0)).getString("lastname");
            verify(mockResultSet, times(0)).getString("initials");
            verify(mockResultSet, times(0)).getString("firstname");
            verify(mockResultSet, times(0)).getString("gender");
            verify(mockResultSet, times(0)).getString("location");
            verify(mockResultSet, times(0)).getInt("Party_ID");
            throw se;
        }
    }

    @Test
    public void testGetElectionInfo() throws SQLException {
        when(mockResultSet.getTimestamp("startDate")).thenReturn(new Timestamp(0));
        when(mockResultSet.getTimestamp("endDate")).thenReturn(new Timestamp(0));
        adminDao.getElectionInfo(1);
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getString("name");
        verify(mockResultSet, times(1)).getTimestamp("startDate");
        verify(mockResultSet, times(1)).getTimestamp("endDate");
    }

    @Test
    public void testGetElectionInfoToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            adminDao.getElectionInfo(1);
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setInt(anyInt(), anyInt());
            verify(mockPreparedStmnt, times(0)).executeQuery();
            verify(mockResultSet, times(0)).next();
            verify(mockResultSet, times(0)).getString("name");
            verify(mockResultSet, times(0)).getTimestamp("startDate");
            verify(mockResultSet, times(0)).getTimestamp("endDate");
            throw se;
        }
    }

    @Test
    public void testputAddressInDatabase() throws SQLException {
        adminDao.putAddressInDatabase(1, "address");
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).execute();
    }

    @Test
    public void testPutAddressInDatabaseToCatch() throws SQLException {
        try {
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException());
            adminDao.putAddressInDatabase(1, "address");
        } catch (SQLException se) {
            verify(mockConn, times(1)).prepareStatement(anyString());
            verify(mockPreparedStmnt, times(0)).setInt(anyInt(), anyInt());
            verify(mockPreparedStmnt, times(0)).setString(anyInt(), anyString());
            verify(mockPreparedStmnt, times(0)).execute();
            throw se;
        }
    }
}
