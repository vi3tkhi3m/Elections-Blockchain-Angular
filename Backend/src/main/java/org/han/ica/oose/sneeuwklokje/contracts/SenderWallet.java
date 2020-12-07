package org.han.ica.oose.sneeuwklokje.contracts;

import org.han.ica.oose.sneeuwklokje.exceptions.InvalidCredentialsException;
import org.web3j.crypto.Credentials;

public interface SenderWallet {
    Credentials getWalletCredentials() throws InvalidCredentialsException;
}
