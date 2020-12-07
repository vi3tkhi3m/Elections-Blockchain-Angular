package org.han.ica.oose.sneeuwklokje.database.admin;

import org.han.ica.oose.sneeuwklokje.database.DaoImpl;
import org.han.ica.oose.sneeuwklokje.database.util.NamedQueries;
import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.Election;
import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.admin.newelection.Party;
import org.han.ica.oose.sneeuwklokje.dtos.admin.newelection.PartyListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.election.ElectionInfo;
import org.han.ica.oose.sneeuwklokje.dtos.election.MemberInfo;
import org.han.ica.oose.sneeuwklokje.dtos.election.PartyInfo;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDaoImpl extends DaoImpl implements AdminDao {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    /**
     * Returns a list of elections. The selector ('>' || '<') determines if the election is running or closed.
     *
     * @param selector Option > || <
     * @return electionListResponse || null when no elections are found
     */
    @Override
    public ElectionListResponse getElections(char selector) {
        ElectionListResponse electionListResponse = new ElectionListResponse();

        String query = NamedQueries.GET_ELECTIONS_BEGIN + selector + NamedQueries.GET_ELECTIONS_END;

        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            while (rs.next()) {
                Election election = new Election();
                election.setElectionID(rs.getInt("id"));
                election.setElectionName(rs.getString("name"));
                election.setStartDate(df.format(rs.getDate("startDate")));
                election.setEndDate(df.format(rs.getDate("endDate")));
                electionListResponse.addElection(election);
            }
            return electionListResponse;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cant get elections. ", e);
        } finally {
            closeConnection();
        }
        return null;
    }

    /**
     * Closes a running election in the database based on the electionId
     *
     * @param electionId The id of an election
     */
    @Override
    public void closeRunningElectionInDatabase(int electionId) {
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.CLOSE_RUNNING_ELECTION);
            stmt.setInt(1, electionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cant close running elections in database. ", e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Checks if an election with electionId is closed
     *
     * @param electionId The id of an election
     * @return true || || false when no elections are found
     */
    @Override
    public boolean isElectionClosed(int electionId) {
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.IS_ELECTION_CLOSED);
            stmt.setInt(1, electionId);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Date endDate = rs.getDate("endDate");
                Date today = new Date(System.currentTimeMillis());
                return today.after(endDate);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't check if election is closed. ", e);
        } finally {
            closeConnection();
        }
        return false;
    }

    /**
     * Returns a list of Parties
     *
     * @return partyListResponse || null when no parties are found
     */
    @Override
    public PartyListResponse getPartiesFromDatabase() {
        PartyListResponse partyListResponse = new PartyListResponse();

        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.GET_ALL_PARTIES);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Party party = new Party();
                party.setId(rs.getInt("id"));
                party.setName(rs.getString("name"));
                partyListResponse.addParty(party);
            }
            return partyListResponse;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't get parties from the database. ", e);
        } finally {
            closeConnection();
        }
        return null;
    }

    /**
     * Inserts a new election in the database
     *
     * @param name      The name of an election
     * @param startDate The startDate of an election
     * @param endDate   The endDate of an election
     * @throws ParseException
     */
    @Override
    public void insertNewElectionInDatabase(String name, String startDate, String endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedStartDate = format.parse(startDate);
        java.sql.Date sqlStartDate = new java.sql.Date(parsedStartDate.getTime());

        java.util.Date parsedEndDate = format.parse(endDate);
        java.sql.Date sqlEndDate = new java.sql.Date(parsedEndDate.getTime());

        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.INSERT_NEW_ELECTION);
            stmt.setString(1, name);
            stmt.setDate(2, sqlStartDate);
            stmt.setDate(3, sqlEndDate);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't insert new election into the database. ", e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Inserts parties for the election in the database
     *
     * @param name    The name of a party
     * @param partyId The id of a party
     */
    @Override
    public void insertPartiesForNewElection(String name, int partyId) {
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.INSERT_PARTY_INTO_ELECTION);
            stmt.setString(1, name);
            stmt.setInt(2, partyId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't insert parties into election. ", e);
        }
    }

    /**
     * Returns a list of planned elections
     *
     * @return ElectionListResponse || null when no planned elections are found
     */
    @Override
    public ElectionListResponse getPlannedElections() {
        ElectionListResponse electionListResponse = new ElectionListResponse();
        try {
            con = sqlConnection.getConnection();

            stmt = con.prepareStatement(NamedQueries.GET_PLANNED_ELECTION_LIST);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Election election = new Election();
                election.setElectionID(rs.getInt("id"));
                election.setElectionName(rs.getString("name"));
                electionListResponse.addElection(election);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while getting planned elections", e);

        } finally {
            closeConnection();
        }
        return electionListResponse;
    }

    /**
     * Checks if the election with electionId is not published to the blockchain
     *
     * @param electionId The id of an election
     * @return true || false when no planned elections are found
     */
    @Override
    public boolean isPlannedElection(int electionId) {
        try {
            con = sqlConnection.getConnection();

            stmt = con.prepareStatement(NamedQueries.IS_ELECTION_PLANNED);
            stmt.setInt(1, electionId);
            rs = stmt.executeQuery();
            if(rs.next()) {
                return (electionId == rs.getInt("id"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while checking if election is a planned election.", e);
        } finally{
            closeConnection();
        }
        return false;
    }

    /**
     * Returns a list of parties for the planned election with electionId
     *
     * @param electionId The id of an election
     * @return partyInfos
     */
    @Override
    public List<PartyInfo> getPlannedPartiesInElection(int electionId) {
        List<PartyInfo> partyInfos = new ArrayList<>();
        try {
            con = sqlConnection.getConnection();

            stmt = con.prepareStatement(NamedQueries.GET_PARTIES_FROM_PLANNED_ELECTION);
            stmt.setInt(1, electionId);
            rs = stmt.executeQuery();

            int partyId = 0;
            while (rs.next()) {
                String name = rs.getString("name");
                int databaseId = rs.getInt("id");
                partyInfos.add(new PartyInfo(name, databaseId, partyId));
                partyId++;
            }
            return partyInfos;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't get the parties of the planned elections.", e);
        } finally{
            closeConnection();
        }
        return partyInfos;
    }

    /**
     * Returns a list of members for parties of the planned election with electionId
     * @param electionId The id of an election
     * @param partyInfos The information about a party
     * @return memberInfos
     */
    @Override
    public List<MemberInfo> getPlannedMembersInElection(int electionId, List<PartyInfo> partyInfos) {
        List<MemberInfo> memberInfos = new ArrayList<>();
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.GET_MEMBERS_FROM_PLANNED_ELECTION);
            stmt.setInt(1, electionId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setPosition(rs.getInt("position"));
                memberInfo.setLastName(rs.getString("lastname"));
                memberInfo.setInitials(rs.getString("initials"));
                memberInfo.setFirstName(rs.getString("firstname"));
                memberInfo.setGender(rs.getString("gender"));
                memberInfo.setLocation(rs.getString("location"));
                memberInfo.setParty(getSmartContractIndex(rs.getInt("Party_ID"), partyInfos));
                memberInfos.add(memberInfo);
            }
            return memberInfos;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't get the members of the planned elections.", e);
            return memberInfos;
        } finally {
            closeConnection();
        }
    }

    /**
     * Returns the smartcontractId of the party
     *
     * @param partyIdDatabase The id of a party
     * @param partyInfos      The information about a party
     * @return smartContractId || 1000 when no party is found
     */
    private int getSmartContractIndex(int partyIdDatabase, List<PartyInfo> partyInfos){
        for (PartyInfo partyInfo:
             partyInfos) {
            if(partyIdDatabase == partyInfo.getDatabaseId()){
                return partyInfo.getSmartContractId();
            }
        }
        return 1000;
    }

    /**
     * Returns the data of an election with electionId
     * @param electionId The id of an election
     * @return electionInfo
     */
    @Override
    public ElectionInfo getElectionInfo(int electionId) {
        ElectionInfo electionInfo = new ElectionInfo();
        try {
            con = sqlConnection.getConnection();

            stmt = con.prepareStatement(NamedQueries.GET_ELECTION_INFO);
            stmt.setInt(1, electionId);
            rs = stmt.executeQuery();

            if(rs.next()) {
                String name = rs.getString("name");
                Timestamp startDate = rs.getTimestamp("startDate");
                Timestamp endDate = rs.getTimestamp("endDate");

                Calendar startCalender = Calendar.getInstance();
                startCalender.setTime(startDate);

                Calendar endCalender = Calendar.getInstance();
                endCalender.setTime(endDate);

                electionInfo.setName(name);
                electionInfo.setStartDate(startCalender);
                electionInfo.setEndDate(endCalender);
            }

            return electionInfo;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't get the election info of the planned elections.", e);
        } finally{
            closeConnection();
        }
        return electionInfo;
    }

    /**
     * Inserts the contractAddress in the database
     *
     * @param electionId      The id of an election
     * @param contractAddress The smartContractAddress of an election
     */
    @Override
    public void putAddressInDatabase(int electionId, String contractAddress) {
        try {
            con = sqlConnection.getConnection();

            stmt = con.prepareStatement(NamedQueries.POST_ELECTION_ADRES);
            stmt.setInt(1, electionId);
            stmt.setString(2, contractAddress);
            stmt.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't put the address in the database.", e);
        } finally {
            closeConnection();
        }
    }
}

