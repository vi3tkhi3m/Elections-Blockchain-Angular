package org.han.ica.oose.sneeuwklokje.database.voter;

import org.han.ica.oose.sneeuwklokje.database.DaoImpl;
import org.han.ica.oose.sneeuwklokje.database.util.NamedQueries;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Duplicates")
public class VoterDaoImpl extends DaoImpl implements VoterDao {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    /**
     * Verifies the token of a user. Returns weather the users has a correct token.
     * @param token The token of a voter used for authentication
     * @return true || false when no token is found
     */
    @Override
    public boolean checkAuthenticationToken(String token) {
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.CHECK_AUTHENTICATION_TOKEN);
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't check Authentication token. ", e);
        } finally {
            closeConnection();
        }
        return false;
    }

    /**
     * Deletes the token of a user, this method is called when the voter voted, so his vote stays anonymous.
     * @param token The token used to delete a linked election
     */
    @Override
    public void deleteTokenAfterVote(String token) {
        try {
            con = sqlConnection.getConnection();
            stmt = con.prepareStatement(NamedQueries.DELETE_TOKEN_AFTER_VOTE);
            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't delete Token after Voting. ", e);
        } finally {
            closeConnection();
        }
    }
}
