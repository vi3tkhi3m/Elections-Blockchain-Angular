package org.han.ica.oose.sneeuwklokje.services.admin;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.contracts.Election;
import org.han.ica.oose.sneeuwklokje.database.admin.AdminDao;
import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.admin.newelection.PartyListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.election.ElectionInfo;
import org.han.ica.oose.sneeuwklokje.dtos.election.MemberInfo;
import org.han.ica.oose.sneeuwklokje.dtos.election.PartyInfo;
import org.han.ica.oose.sneeuwklokje.exceptions.InvalidCredentialsException;
import org.han.ica.oose.sneeuwklokje.exceptions.NoPlannedElectionException;
import org.han.ica.oose.sneeuwklokje.exceptions.NoServiceNodeException;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.han.ica.oose.sneeuwklokje.services.election.ElectionService;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import javax.inject.Inject;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminServiceImpl implements AdminService {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    private AdminDao adminDao;

    @Inject
    private ContractConnector contractConnector;

    @Inject
    private ElectionService electionService;

    /**
     * Returns a list of running elections
     * @return ElectionListResponse
     */
    @Override
    public ElectionListResponse getRunningElections() {
        return adminDao.getElections('>');
    }

    /**
     * Returns the parties for needed making a new election
     * @return PartyListResponse
     */
    @Override
    public PartyListResponse getPartiesForMakingNewElection() {
        return adminDao.getPartiesFromDatabase();
    }

    /**
     * Closes running election with electioId
     * @param electionId The id of an election
     */
    @Override
    public void closeRunningElection(int electionId) {
        adminDao.closeRunningElectionInDatabase(electionId);
        Election election = null;
        try {
            election = contractConnector.getElection(electionService.getSmartContractAddress(electionId));

            OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
            BigInteger year = BigInteger.valueOf(utc.getYear());
            BigInteger month = BigInteger.valueOf(utc.getMonthValue());
            BigInteger day = BigInteger.valueOf(utc.getDayOfMonth());
            BigInteger hour = BigInteger.valueOf(utc.getHour());

            election.setEndDate(year, month, day, hour).send();
        } catch (SmartContractInteractionException e) {
            LOGGER.log(Level.SEVERE, "Can't interact with the smart contract. ", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Something wrong happened. ", e);
        }
    }

    /**
     * Creates new election with given parameters
     * @param name The name of an election
     * @param startDate The startDate of an election
     * @param endDate The endDate of an election
     * @param partyIds The partyIds of an election
     */
    @Override
    public void createNewElection(String name, String startDate, String endDate, int[] partyIds) {
        try {
            adminDao.insertNewElectionInDatabase(name, startDate, endDate);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Failed to parse. ", e);
        }
        for (int partyId : partyIds) {
            adminDao.insertPartiesForNewElection(name, partyId);
        }
    }

    /**
     * Returns a list of planned elections
     * @return ElectionListResponse
     */
    @Override
    public ElectionListResponse getPlannedElections() {
        return adminDao.getPlannedElections();
    }

    /**
     * Publishes an election with electionId to the blockchain
     * @param electionId The id of an election
     * @throws SmartContractInteractionException
     * @throws NoPlannedElectionException
     */
    @Override
    public void publishElection(int electionId) throws SmartContractInteractionException, NoPlannedElectionException {
        if(!adminDao.isPlannedElection(electionId)){
            throw new NoPlannedElectionException();
        }
        Election election = getElection();

        ElectionInfo electionInfo = adminDao.getElectionInfo(electionId);
        List<PartyInfo> parties = adminDao.getPlannedPartiesInElection(electionId);
        List<MemberInfo> memberInfos = adminDao.getPlannedMembersInElection(electionId, parties);

        Calendar startCalender = electionInfo.getStartDate();
        Calendar endCalender   = electionInfo.getEndDate();

        LOGGER.log(Level.INFO, "Fetched all data from the database. The insertion of data in the smart contract will begin now.");

        injectDataIntoSmartContract(election, electionInfo, startCalender, endCalender);

        TransactionReceipt reciept = null;
        addPartiesToReceipt(election, parties, reciept);
        addMembersToReceipt(election, memberInfos, reciept);

        final String CONTRACTADDRESS = election.getContractAddress();
        adminDao.putAddressInDatabase(electionId, CONTRACTADDRESS);
        LOGGER.log(Level.INFO, "Election published succesfully.");
    }

    private void addMembersToReceipt(Election election, List<MemberInfo> memberInfos, TransactionReceipt reciept) {
        for (MemberInfo memberInfo : memberInfos) {
            do {
                try {
                    reciept = election.addMember(BigInteger.valueOf(memberInfo.getPosition()),
                            memberInfo.getLastName(),
                            memberInfo.getInitials(),
                            memberInfo.getFirstName(),
                            memberInfo.getGender(),
                            memberInfo.getLocation(),
                            BigInteger.valueOf(memberInfo.getParty())).send();
                    LOGGER.log(Level.INFO, "Added member: " + memberInfo.getLastName() + " on position: " + memberInfo.getPosition());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
            }while (reciept.getStatus() == "0x0");
        }
    }

    private TransactionReceipt addPartiesToReceipt(Election election, List<PartyInfo> parties, TransactionReceipt reciept) {
        for (PartyInfo party: parties) {
            do{
                try {
                    reciept = election.addParty(party.getName()).send();
                    LOGGER.log(Level.INFO, "Added party: " + party.getName() + " with the id: " + party.getSmartContractId());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
            }while (reciept.getStatus() == "0x0");
        }
        return reciept;
    }

    private void injectDataIntoSmartContract(Election election, ElectionInfo electionInfo, Calendar startCalender, Calendar endCalender) throws SmartContractInteractionException {
        try {
            election.setElectionName(electionInfo.getName()).send();
            election.setStartDate(getYear(startCalender), getMonth(startCalender), getDay(startCalender), getHour(startCalender)).send();
            election.setEndDate(getYear(endCalender), getMonth(endCalender), getDay(endCalender), getHour(endCalender)).send();
            LOGGER.log(Level.INFO, "Injected the name, start and end-date in the smart contract");
        } catch (Exception e) {
            String error = "Coud not inject primary data in the smartcontract.";
            LOGGER.log(Level.SEVERE, error);
            throw new SmartContractInteractionException(error);
        }
    }

    private Election getElection() throws SmartContractInteractionException {
        Election election;
        try {
            Credentials credentials = contractConnector.getWalletCredentials();
            Web3j web3j = contractConnector.getWeb3jservice();
            election = Election.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();
        } catch (InvalidCredentialsException e) {
            String error = "Credentials couldn't be loaded";
            LOGGER.log(Level.SEVERE, error, e);
            throw new SmartContractInteractionException(error);
        } catch (NoServiceNodeException e) {
            String error = "Couldn't connect to a node";
            LOGGER.log(Level.SEVERE, error, e);
            throw new SmartContractInteractionException(error);
        } catch (Exception e) {
            String error = "Couldn't send the deploy request to the specified node";
            LOGGER.log(Level.SEVERE, error, e);
            throw new SmartContractInteractionException(error);
        }
        return election;
    }

    /**
     * Returns current hour of the day
     * @param calendar instance of abstract class that provides methods
     * for converting between a specific instant in time
     * @return HOUR_OF_DAY
     */
    private BigInteger getHour(Calendar calendar){
       return BigInteger.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    }

    /**
     * Returns current day of the month
     * @param calendar instance of abstract class that provides methods
     * for converting between a specific instant in time
     * @return DAY_OF_MONTH
     */
    private BigInteger getDay(Calendar calendar){
        return BigInteger.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Returns current month of the year
     * @param calendar instance of abstract class that provides methods
     * for converting between a specific instant in time
     * @return MONTH
     */
    private BigInteger getMonth(Calendar calendar){
        return BigInteger.valueOf(calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * Returns current year
     * @param calendar instance of abstract class that provides methods
     * for converting between a specific instant in time
     * @return YEAR
     */
    private BigInteger getYear(Calendar calendar){
        return BigInteger.valueOf(calendar.get(Calendar.YEAR));
    }
}
