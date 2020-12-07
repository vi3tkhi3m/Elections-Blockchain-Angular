package org.han.ica.oose.sneeuwklokje.database.election;

public interface ElectionDao {

    int getIdOfElectionBasedOnToken(String token);

    String getSmartContractAddressOfElectionId(int electionId);

    boolean isElectionBeforeEndDate(int electionId);
}
