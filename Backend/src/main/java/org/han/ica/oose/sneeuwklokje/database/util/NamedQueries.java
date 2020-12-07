package org.han.ica.oose.sneeuwklokje.database.util;

public class NamedQueries {

    private NamedQueries() {
        throw new IllegalStateException("NamedQueries class");
    }

    // AdminDao
    public static final String CLOSE_RUNNING_ELECTION = "UPDATE Election SET endDate = CURRENT_TIMESTAMP() WHERE id = ?;";
    public static final String IS_ELECTION_CLOSED = "SELECT endDate FROM Election E WHERE id = ?";
    public static final String GET_ALL_PARTIES = "SELECT name, id FROM party";
    public static final String INSERT_NEW_ELECTION = "INSERT INTO election (name, startDate, endDate) VALUES (?, ?, ?)";
    public static final String INSERT_PARTY_INTO_ELECTION = "INSERT INTO electionparties (ElectionID, PartyID) VALUES ((SELECT id FROM election WHERE name = ?), ?)";
    public static final String IS_ELECTION_PLANNED = "SELECT id FROM election E WHERE E.id NOT IN (SELECT EA.ElectionID FROM electionaddress EA) AND E.id = ?";
    public static final String GET_PARTIES_FROM_PLANNED_ELECTION = "SELECT party.id, party.name FROM electionparties INNER JOIN party ON electionparties.PartyID = party.id WHERE electionparties.ElectionID = ?";
    public static final String GET_MEMBERS_FROM_PLANNED_ELECTION = "SELECT member.* FROM member WHERE member.Party_ID IN (SELECT electionparties.PartyID FROM electionparties WHERE electionparties.ElectionID = ?)";
    public static final String GET_ELECTION_INFO = "SELECT election.name, election.startDate, election.endDate FROM election WHERE election.id = ?";
    public static final String POST_ELECTION_ADRES = "INSERT INTO `electionaddress` (`ElectionID`, `Address`) VALUES (?, ?);";


    public static final String GET_ELECTIONS_BEGIN = "SELECT id, name, startDate, endDate FROM Election E WHERE endDate ";
    public static final String GET_ELECTIONS_END = " CURRENT_TIMESTAMP();";

    public static final String GET_PLANNED_ELECTION_LIST= "SELECT election.id, election.name FROM election WHERE id NOT IN (SELECT ElectionID FROM electionaddress)";


    // ElectionDao
    public static final String GET_ELECTIONID_WITH_TOKEN = "SELECT E.id FROM Election E INNER JOIN ElectionToken ET ON E.id = ET.electionID WHERE ET.token = ?;";
    public static final String GET_SMARTCONTRACT_ADDRESS_WITH_ELECTIONID = "SELECT Address FROM ElectionAddress WHERE ElectionID = ?";
    public static final String IS_ELECTION_BEFORE_ENDDATE = "SELECT id FROM election WHERE election.endDate > CURRENT_TIMESTAMP AND election.id = ?";

    // VoterDao
    public static final String CHECK_AUTHENTICATION_TOKEN = "SELECT token FROM TokenVoter WHERE token = ?";
    public static final String DELETE_TOKEN_AFTER_VOTE = "DELETE FROM TokenVoter WHERE token = ?";



}