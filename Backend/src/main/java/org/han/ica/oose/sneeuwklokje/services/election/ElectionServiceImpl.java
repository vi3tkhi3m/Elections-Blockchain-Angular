package org.han.ica.oose.sneeuwklokje.services.election;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.contracts.Election;
import org.han.ica.oose.sneeuwklokje.database.election.ElectionDao;
import org.han.ica.oose.sneeuwklokje.dtos.authentication.electionform.BallotResponse;
import org.han.ica.oose.sneeuwklokje.dtos.authentication.electionform.Member;
import org.han.ica.oose.sneeuwklokje.dtos.authentication.electionform.Party;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple7;

import javax.inject.Inject;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElectionServiceImpl implements ElectionService {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    private ElectionDao electionDao;

    @Inject
    private ContractConnector contractConnector;

    /**
     * Returns an election based id based on a token the user provides.
     *
     * @param token The token of a voter used to get electionId from the database
     * @return int electionid.
     */
    @Override
    public int getElectionIdBasedOnToken(String token) {
        return electionDao.getIdOfElectionBasedOnToken(token);
    }

    /**
     * This method returns a smart contract address based on the election id
     *
     * @param electionId The id of an election
     * @return String smart contract address
     */
    @Override
    public String getSmartContractAddress(int electionId) {
        return electionDao.getSmartContractAddressOfElectionId(electionId);
    }

    /**
     * returns if election with electionId is running
     *
     * @param electionId The id of an election
     * @return true || false
     */
    @Override
    public boolean isElectionBeforeEndDate(int electionId) {
        return electionDao.isElectionBeforeEndDate(electionId);
    }

    /**
     * Creates the vote form for the user.
     *
     * @param electionId The id of an election
     * @return BallotResponse
     * @throws SmartContractInteractionException
     */
    @Override
    public BallotResponse createBallotResponse(int electionId) throws SmartContractInteractionException {
        Election electionContract = contractConnector.getElection(getSmartContractAddress(electionId));

        BigInteger numberOfParties;
        BigInteger numberOfMembers;

        String electionName = null;

        String beginDate;
        String endDate;

        List<Party> electionParties = new ArrayList<>();

        try {
            electionName = electionContract.name().send();

            BigInteger electionStartDate = electionContract.startDate().send();
            BigInteger electionEndDate = electionContract.endDate().send();
            beginDate = formatSmartContractTime(electionStartDate);
            endDate = formatSmartContractTime(electionEndDate);

            numberOfParties = electionContract.getNumberOfParties().send();
            numberOfMembers = electionContract.membersCount().send();

            addPartiesToList(electionContract, numberOfParties, electionParties);
            addMembersToParty(electionContract, numberOfMembers, electionParties);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Something wrong happened. ", e);
            throw new SmartContractInteractionException(e.getMessage());
        }
        return new BallotResponse(electionId, electionName, electionParties, beginDate.toString(), endDate.toString());
    }

    private void addPartiesToList(Election electionContract, BigInteger numberOfParties, List<Party> electionParties) throws SmartContractInteractionException {
        //Get all parties
        for (int i = 0; i < numberOfParties.intValue(); i++) {
            Tuple2<BigInteger, String> party = null;
            try {
                party = electionContract.getPartyInfo(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                throw new SmartContractInteractionException("Error while requesting the result.");
            }
            Party partyDto = new Party();
            partyDto.setId(i);
            partyDto.setName(party.getValue2());
            electionParties.add(i, partyDto);
        }
    }

    private void addMembersToParty(Election electionContract, BigInteger numberOfMembers, List<Party> electionParties) throws SmartContractInteractionException {
        //Get all members and add them to parties.
        for (int i = 0; i < numberOfMembers.intValue(); i++) {
            Tuple7<BigInteger, String, String, String, String, String, BigInteger> member = null;
            try {
                member = electionContract.getMemberInfo(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                throw new SmartContractInteractionException("Error while requesting the result.");
            }
            int id = i;
            int position = member.getValue1().intValue();
            String lastName = member.getValue2();
            String initials = member.getValue3();
            String firstName = member.getValue4();
            String gender = member.getValue5();
            String location = member.getValue6();
            int partyId = member.getValue7().intValue();
            electionParties.get(partyId).addMember(new Member(id, position, lastName, initials, firstName, gender, location));
        }
    }

    /**
     * Returns the DateTime
     *
     * @param smTime Bigint with the time.
     * @return DateTime
     */
    private String formatSmartContractTime(BigInteger smTime) {
        Timestamp timestamp = new Timestamp(smTime.longValue() * 1000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY) - 1);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        return (year + "-" + month + "-" + day + "-" + hour);
    }
}



