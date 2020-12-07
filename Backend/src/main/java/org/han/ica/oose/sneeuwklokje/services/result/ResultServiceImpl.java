package org.han.ica.oose.sneeuwklokje.services.result;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.contracts.Election;
import org.han.ica.oose.sneeuwklokje.database.admin.AdminDao;
import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.result.Member;
import org.han.ica.oose.sneeuwklokje.dtos.result.ElectionResultResponse;
import org.han.ica.oose.sneeuwklokje.dtos.result.Party;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.han.ica.oose.sneeuwklokje.services.election.ElectionService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple5;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultServiceImpl implements ResultService {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    private AdminDao adminDao;

    @Inject
    private ContractConnector contractConnector;

    @Inject
    private ElectionService electionService;

    /**
     * Returns list of closed elections
     * @return ElectionListResponse
     */
    @Override
    public ElectionListResponse closedElectionResponse() {
        return adminDao.getElections('<');
    }

    /**
     * Returns result of election with electionId
     * @param electionId The id of an election
     * @return ElectionResultResponse
     */
    @Override
    public ElectionResultResponse resultElectionResponse(int electionId) {
        ElectionResultResponse electionResultResponse = new ElectionResultResponse();

        try {
            Election electionContract = contractConnector.getElection(electionService.getSmartContractAddress(electionId));
            BigInteger numberOfParties = electionContract.partiesCount().send();
            BigInteger numberOfMembers = electionContract.membersCount().send();
            String name = electionContract.name().send();
            electionResultResponse.setName(name);

            addPartiesToElectionResultResponse(electionResultResponse, electionContract, numberOfParties);
            addMembersToElectionResultResponse(electionResultResponse, electionContract, numberOfMembers);

        } catch (SmartContractInteractionException e) {
            LOGGER.log(Level.SEVERE, "Can't interact with the smart contract. ", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Something wrong happened. ", e);
        }
        return electionResultResponse;
    }

    private void addPartiesToElectionResultResponse(ElectionResultResponse electionResultResponse, Election electionContract, BigInteger numberOfParties) throws SmartContractInteractionException {
        for(int i = 0; i < numberOfParties.intValue(); i++){
            Tuple2<String, BigInteger> party = null;
            try {
                party = electionContract.getPartyResults(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                throw new SmartContractInteractionException("Error while requesting the result.");
            }
            Party partyDto = new Party();
            partyDto.setId(i);
            partyDto.setName(party.getValue1());
            partyDto.setVoteCount(party.getValue2().intValue());
            electionResultResponse.addParty(partyDto);
        }
    }

    private void addMembersToElectionResultResponse(ElectionResultResponse electionResultResponse, Election electionContract, BigInteger numberOfMembers) throws SmartContractInteractionException {
        for(int i = 0; i <numberOfMembers.intValue(); i++){
            Tuple5<String, String, String, BigInteger, BigInteger> member = null;
            try {
                member = electionContract.getMemberResults(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                throw new SmartContractInteractionException("Error while requesting the result.");
            }
            Member memberDto = new Member();
            memberDto.setId(i);
            memberDto.setFirstname(member.getValue1());
            memberDto.setInitials(member.getValue2());
            memberDto.setLastname(member.getValue3());
            memberDto.setPartyId(member.getValue4().intValue());
            memberDto.setVoteCount(member.getValue5().intValue());
            electionResultResponse.addMember(memberDto);
        }
    }

    /**
     * Returns if election with electionId is closed
     * @param electionId The id of an election
     * @return true || false
     */
    @Override
    public boolean isElectionClosed(int electionId) {
        return adminDao.isElectionClosed(electionId);
    }


}
