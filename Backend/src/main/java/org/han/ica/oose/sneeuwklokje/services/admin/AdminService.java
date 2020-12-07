package org.han.ica.oose.sneeuwklokje.services.admin;

import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.admin.newelection.PartyListResponse;
import org.han.ica.oose.sneeuwklokje.exceptions.NoPlannedElectionException;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;

public interface AdminService {
    ElectionListResponse getRunningElections();

    PartyListResponse getPartiesForMakingNewElection();

    void closeRunningElection(int electionId);

    void createNewElection(String name, String startDate, String endDate, int[] partyIds);

    ElectionListResponse getPlannedElections();

    void publishElection(int electionId) throws SmartContractInteractionException, NoPlannedElectionException;
}
