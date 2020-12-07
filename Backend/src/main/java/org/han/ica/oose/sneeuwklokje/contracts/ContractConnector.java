package org.han.ica.oose.sneeuwklokje.contracts;

import org.han.ica.oose.sneeuwklokje.exceptions.InvalidCredentialsException;
import org.han.ica.oose.sneeuwklokje.exceptions.NoServiceNodeException;
import org.han.ica.oose.sneeuwklokje.exceptions.SmartContractInteractionException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ManagedTransaction;
import org.web3j.utils.Async;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContractConnector implements SenderWallet{

    private Properties properties;

    private static ContractConnector contractConnector = null;

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(ContractConnector.class));

    /**
     * Returns a new instance of the contractconnector.
     * @throws InvalidCredentialsException
     */
    private ContractConnector() throws InvalidCredentialsException {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("configuration.properties"));
        } catch (IOException e) {
            throw new InvalidCredentialsException("Something went wrong while trying to load wallet file.", e);
        }
    }

    /**
     * Makes sure only one instance of this class exists at any given time.
     * @return
     */
    public static ContractConnector getContractConnectorInstance() {
        if(contractConnector == null){
            try {
                contractConnector = new ContractConnector();
            } catch (InvalidCredentialsException e) {
                LOGGER.log(Level.SEVERE, "Can't get contract connector instance. ", e);
            }
        }
        return contractConnector;
    }

    /**
     * Returns the credentials of the wallet file.
     * @return Credentials
     * @throws InvalidCredentialsException
     */
    public Credentials getWalletCredentials() throws InvalidCredentialsException {
        String absolutepathtowallet = properties.getProperty("absolutepathtowallet");
        String walletpassword = properties.getProperty("walletpassword");
        try {
            Credentials credentials = WalletUtils.loadCredentials(walletpassword, absolutepathtowallet);
            return credentials;
        } catch (CipherException e) {
            throw new InvalidCredentialsException("The wallet file you provided is either not valid, or the password provided is not correct", e);
        } catch (IOException e) {
            throw new InvalidCredentialsException("The wallet file you provided is either not valid, or the password provided is not correct", e);
        }
    }

    /**
     * Returns the credentials of the Web3j service. (a node connected to the network).
     * @return Web3j
     * @throws NoServiceNodeException
     */
    public Web3j getWeb3jservice() throws NoServiceNodeException {
        String primarynode = properties.getProperty("primarynode");
        String secondarynode = properties.getProperty("secondarynode");
        int pollinginterval = Integer.parseInt(properties.getProperty("pollinginterval"));
        Web3j web3j;
        try{
            web3j = Web3j.build(new HttpService(primarynode) ,pollinginterval, Async.defaultExecutorService());
        }catch(Exception p){
            try{
                web3j = Web3j.build(new HttpService(secondarynode),pollinginterval, Async.defaultExecutorService());
            }catch(Exception s){
                throw new NoServiceNodeException("Couldn't connect to the primary and secondary node. Are you sure both nodes are up and running? Are you connected to the internet?", p);
            }
        }
        return web3j;
    }

    /**
     * Gets an election Based on a smart contract address.
     * @param electionAddress the address of the election to return.
     * @param gasPrice the default gas price you are willing to pay to perform default operations.
     * @return
     * @throws SmartContractInteractionException
     */
    public Election getElection(String electionAddress, BigInteger gasPrice) throws SmartContractInteractionException {
        Credentials credentials;
        Web3j web3j;
        try {
            credentials = this.getWalletCredentials();
            web3j = this.getWeb3jservice();
        } catch (InvalidCredentialsException e) {
            throw new SmartContractInteractionException("An error occurred during the voting process. Check the wallet credentials");
        } catch (NoServiceNodeException e) {
            throw new SmartContractInteractionException("An error occurred during the connection process. " + e.getMessage());
        }

        Election election = Election.load(electionAddress, web3j, credentials, BigInteger.valueOf(4300000), BigInteger.valueOf(4300000));

        if(Integer.valueOf(properties.getProperty("setGasPrice")) == 1){
            election.setGasPrice(gasPrice);
        }

        return election;
    }

    /**
     * Get the election with a default amount of gass
     * @param electionAddress the address of the election to return.
     * @return
     * @throws SmartContractInteractionException
     */
    public Election getElection(String electionAddress) throws SmartContractInteractionException{
        return this.getElection(electionAddress, ManagedTransaction.GAS_PRICE);
    }
    
}
