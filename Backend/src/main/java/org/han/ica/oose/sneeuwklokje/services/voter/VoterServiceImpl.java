package org.han.ica.oose.sneeuwklokje.services.voter;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.contracts.Election;
import org.han.ica.oose.sneeuwklokje.database.voter.VoterDao;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.han.ica.oose.sneeuwklokje.services.election.ElectionService;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VoterServiceImpl implements VoterService {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    private VoterDao voterDao;

    @Inject
    private ElectionService electionService;

    @Inject
    private ContractConnector contractConnector;

    /**
     * Pushes a vote to the blockchain.
     * @param token The token of the election to push to the blockchain
     * @param memberId The id of a member
     * @param electionId The id of an election
     * @throws SmartContractInteractionException
     */
    @SuppressWarnings("Duplicates")
    @Override
    public boolean pushVoteToBlockchain(String token, int memberId, int electionId) throws SmartContractInteractionException {
        Election electionContract = contractConnector.getElection(getSmartContractAddressForElection(electionId));
        // Save the vote to the blockchain by incrementing the voteCount of the chosen party member
        TransactionReceipt receipt = new TransactionReceipt();

        do {
            try{
                receipt = electionContract.vote(BigInteger.valueOf(memberId), token).send();
            } catch (RuntimeException e) {
                LOGGER.log(Level.INFO, e.getMessage() + "Attempting to vote again");
                receipt.setStatus("0x0");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Send Error. ", e);
            }
            if("0x0".equals(receipt.getStatus())){
                LOGGER.log(Level.INFO, "tried but failed to vote for unknown reasons");
            }
        } while("0x0".equals(receipt.getStatus()));
            LOGGER.log(Level.INFO, receipt.getStatus());
        if("0x1".equals(receipt.getStatus())){
            deleteTokenFromDatabaseAfterVote(token);
            return true;
        } else  {
            return false;
        }
    }

    /**
     * Deletes a token from the database so the user can't use it again.
     * @param token the token to delete.
     */
    @Override
    // Remove the now used token from the database to prevent people from voting twice with the same token.
    public void deleteTokenFromDatabaseAfterVote(String token) {
        voterDao.deleteTokenAfterVote(token);
    }

    /**
     * Checks if the users is authorized to complete an action based on a token.
     * @param token the token to response.
     * @return boolean
     */
    @Override
    public boolean doAuthenticationToken(String token) {
        return voterDao.checkAuthenticationToken(token);
    }

    /**
     * Returns the smartContractAddress of election with electionId
     * @param electionID The token of an election
     * @return smartContractAddress
     */
    @Override
    public String getSmartContractAddressForElection(int electionID) {
        return electionService.getSmartContractAddress(electionID);
    }

    /**
     * Returns the electionId of election with token
     * @param token The token used in the database to get the electionId of the election linked to it
     * @return electionId
     */
    @Override
    public int getElectionIdBasedOnToken(String token) {
        return electionService.getElectionIdBasedOnToken(token);
    }
}
