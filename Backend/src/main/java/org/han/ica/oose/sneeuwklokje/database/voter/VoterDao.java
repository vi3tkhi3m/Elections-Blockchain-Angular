package org.han.ica.oose.sneeuwklokje.database.voter;


public interface VoterDao {
    boolean checkAuthenticationToken(String token);

    void deleteTokenAfterVote(String token);
}
