package org.han.ica.oose.sneeuwklokje.database.election;

import org.han.ica.oose.sneeuwklokje.database.DaoImpl;
import org.han.ica.oose.sneeuwklokje.database.util.NamedQueries;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Duplicates")
public class ElectionDaoImpl extends DaoImpl implements ElectionDao {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    /**
     * Returns the id of an election based on a user token.
     * @param token The token that is linked to an election
     * @return electionId || 0 when no election is found
     */
    @SuppressWarnings("Duplicates")
    @Override
    public int getIdOfElectionBasedOnToken(String token) {
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.GET_ELECTIONID_WITH_TOKEN);
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't get ID of Election from database. ", e);
        } finally {
            closeConnection();
        }
        return 0;
    }

    @Override
    public String getSmartContractAddressOfElectionId(int electionId) {
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.GET_SMARTCONTRACT_ADDRESS_WITH_ELECTIONID);
            stmt.setInt(1, electionId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Address");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't get SmartContract Address with ElectionID. ", e);
        } finally {
            closeConnection();
        }
        return null;
    }

    /**
     * Returns true if election with electionId is still running
     * @param electionId The id of an election
     * @return true || false when no election is found
     */
    @Override
    public boolean isElectionBeforeEndDate(int electionId) {
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.IS_ELECTION_BEFORE_ENDDATE);
            stmt.setInt(1, electionId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't check if Election is before EndDate. ", e);
        } finally {
            closeConnection();
        }
        return false;
    }
}
