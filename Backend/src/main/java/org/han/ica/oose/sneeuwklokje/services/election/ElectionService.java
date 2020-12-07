package org.han.ica.oose.sneeuwklokje.services.election;

import org.han.ica.oose.sneeuwklokje.dtos.authentication.electionform.BallotResponse;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;

public interface ElectionService {
    int getElectionIdBasedOnToken(String token);

    BallotResponse createBallotResponse(int electionId) throws SmartContractInteractionException;

    String getSmartContractAddress(int electionId);

    boolean isElectionBeforeEndDate(int electionId);
}
