package org.han.ica.oose.sneeuwklokje.controllers;

import org.han.ica.oose.sneeuwklokje.contracts.ContractConnector;
import org.han.ica.oose.sneeuwklokje.contracts.Election;
import org.han.ica.oose.sneeuwklokje.exceptions.InvalidCredentialsException;
import org.han.ica.oose.sneeuwklokje.exceptions.NoServiceNodeException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/election")
public class ElectionRestController {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    private ContractConnector contractConnector;

    @Path("/deploy")
    @POST
    public Response deployContract() {
        //fixme make a service layer for the election.

        //Deploy
        Election electionContract;
        try {
            Credentials credentials = contractConnector.getWalletCredentials();
            Web3j web3j = contractConnector.getWeb3jservice();
            LOGGER.log(Level.SEVERE, "Credentials loaded");
            electionContract = Election.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();
/*            electionContract.setElectionName("testelection").send();
            electionContract.addParty("Marijnscoolepartij").send();*/
            LoggerContractDeployment(electionContract);
            return Response.ok().build();
            //fixme er moet nog een naam en attributen worden meegegeven aan het smart contract. De hardcoded waarden moeten nog worden vervangen.
        } catch (IOException | NoServiceNodeException | InvalidCredentialsException e) {
            LOGGER.log(Level.SEVERE, "Can't deploy contract. ", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Can't deploy contract. ", e);
        }
        return Response.status(500).build();
    }

    private void LoggerContractDeployment(Election electionContract) {
        LOGGER.log(Level.SEVERE, "Deploying contract");
        String contractAddress = electionContract.getContractAddress();
        LOGGER.log(Level.SEVERE, "Smart contract deployed to address " + contractAddress);
        LOGGER.log(Level.SEVERE, "View contract at https://rinkeby.etherscan.io/address/" + contractAddress);
    }

}
