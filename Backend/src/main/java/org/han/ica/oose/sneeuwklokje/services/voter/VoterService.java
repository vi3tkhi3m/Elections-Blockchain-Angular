package org.han.ica.oose.sneeuwklokje.services.voter;

import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;

public interface VoterService {
    boolean doAuthenticationToken(String token);

    boolean pushVoteToBlockchain(String token, int memberId, int electionId) throws SmartContractInteractionException;

    void deleteTokenFromDatabaseAfterVote(String token);

    String getSmartContractAddressForElection(int electionID);

    int getElectionIdBasedOnToken(String token);
}
