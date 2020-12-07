package org.han.ica.oose.sneeuwklokje.database.admin;

import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.admin.newelection.PartyListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.election.ElectionInfo;
import org.han.ica.oose.sneeuwklokje.dtos.election.MemberInfo;
import org.han.ica.oose.sneeuwklokje.dtos.election.PartyInfo;

import java.text.ParseException;
import java.util.List;

public interface AdminDao {
    ElectionListResponse getElections(char selector);

    void closeRunningElectionInDatabase(int electionId);

    boolean isElectionClosed(int electionId);

    PartyListResponse getPartiesFromDatabase();

    void insertNewElectionInDatabase(String name, String startDate, String endDate) throws ParseException;

    void insertPartiesForNewElection(String name, int partyIds);

    ElectionListResponse getPlannedElections();

    boolean isPlannedElection(int electionId);

    List<PartyInfo> getPlannedPartiesInElection(int electionId);

    List<MemberInfo> getPlannedMembersInElection(int electionId, List<PartyInfo> partyInfos);

    ElectionInfo getElectionInfo(int electionId);

    void putAddressInDatabase(int electionId, String contractAddress);
}
