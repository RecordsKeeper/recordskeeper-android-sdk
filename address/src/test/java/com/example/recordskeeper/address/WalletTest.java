package com.example.recordskeeper.address;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class WalletTest {

    Wallet wallet = new Wallet();
    public Properties prop;
    public String miningaddress;
    public String testdata;
    public String privatekey;
    public String signedtestdata;

    public boolean getPropert() throws IOException {

        prop = new Properties();

        String path = "config.properties";
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fs = new FileInputStream(path);
            prop.load(fs);
            fs.close();
            return true;
        } else {
            return false;
        }
    }

    public WalletTest() throws IOException {
        if (getPropert() == true) {
            miningaddress = prop.getProperty("miningaddress");
            testdata = prop.getProperty("testdata");
            privatekey = prop.getProperty("privatekey");
            signedtestdata = prop.getProperty("signedtestdata");
        } else {
            miningaddress = System.getenv("miningaddress");
            testdata = System.getenv("testdata");
            privatekey = System.getenv("privatekey");
            signedtestdata = System.getenv("signedtestdata");
        }
    }

    @Test
    public void createWallet() throws Exception {

        JSONObject item = wallet.createWallet();
        String address = item.getString("public_address");
        int address_size = address.length();
        assertEquals(address_size, 34);

    }

    @Test
    public void getPrivatekey() throws Exception{
        String checkprivkey = wallet.getPrivateKey(miningaddress);
        assertEquals(checkprivkey, privatekey);
    }

    @Test
    public void retreieveWalletinfo() throws IOException, JSONException {
        JSONObject item = wallet.retrieveWalletinfo();
        int tx_count = item.getInt("tx_count");
        assertNotEquals(tx_count, 463757);
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
        String validity = wallet.verifyMessage(miningaddress, signedtestdata, testdata);
        assertEquals(validity, "Yes, message is verified");
    }
}