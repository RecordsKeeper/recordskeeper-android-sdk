package com.example.recordskeeper.address;

import org.json.JSONException;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class WalletTest {

    Wallet wallet = new Wallet();

    Config cfg = new Config();
    String validaddress = cfg.getProperty("validaddress");
    String privatekey = cfg.getProperty("privatekey");
    String testdata = cfg.getProperty("testdata");
    String signedtestdata = cfg.getProperty("signedtestdata");

    public WalletTest() throws IOException {}

    @Test
    public void createWallet() throws Exception {
        String address = wallet.createWallet();
        int address_size = address.length();
        assertEquals(address_size, 38);
    }

    @Test
    public void getPrivatekey() throws Exception{
        String checkprivkey = wallet.getPrivateKey(validaddress);
        assertEquals(checkprivkey, privatekey);
    }

    @Test
    public void retreieveWalletinfo() throws IOException, JSONException {
        int wallet_balance = wallet.retrieveWalletinfo();
        assertNotEquals(wallet_balance, 0);
    }
/*
    @Test
    public void importwallet(){
        String a = wallet.importWallet();
    }

    @Test
    public void dumpwallet(){
        String a = wallet.dumpWallet();
    }

    @Test
    public void lockwallet(){
        String a = wallet.lockWallet();
    }

    @Test
    public void unlockwallet(){
        String a = wallet.unlockWallet();
    }

    @Test
    public void changeWalletPassword(){
        String a = wallet.changeWalletPassword();
    }
*/
    @Test
    public void signmessage() throws IOException, JSONException {
        String signedMessage = wallet.signMessage(privatekey, testdata);
        assertEquals(signedMessage, signedtestdata);
    }

    @Test
    public void verifymessage() throws IOException, JSONException {
        String validity = wallet.verifyMessage(validaddress, signedtestdata, testdata);
        assertEquals(validity, "Yes, message is verified");
    }
}