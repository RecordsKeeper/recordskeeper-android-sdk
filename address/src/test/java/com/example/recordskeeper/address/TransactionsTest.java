package com.example.recordskeeper.address;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class TransactionsTest {

    Transactions Transactions = new Transactions();
    Config cfg = new Config();
    String miningaddress = cfg.getProperty("miningaddress");
    String validaddress = cfg.getProperty("validaddress");
    double amount = Double.parseDouble(cfg.getProperty("amount"));
    String testdata = cfg.getProperty("testdata");
    String dumptxhex = cfg.getProperty("dumptxhex");
    String privatekey = cfg.getProperty("privatekey");
    String dumpsignedtxhex = cfg.getProperty("dumpsignedtxhex");
    String dumptxid = cfg.getProperty("dumptxid");

    public TransactionsTest() throws IOException {
    }


    @Test
    public void sendTransaction() throws Exception {

        byte[] bytes = "Hello".getBytes("UTF-8");
        BigInteger bigInt = new BigInteger(bytes);
        String hexString = bigInt.toString(16);

        String txid = Transactions.sendTransaction(miningaddress, validaddress, hexString, 0.2);
        int tx_size = txid.length();
        assertEquals(tx_size, 64);
    }

    @Test
    public void createRawTransaction() throws IOException, JSONException {

        byte[] bytes = testdata.getBytes("UTF-8");
        BigInteger bigInt = new BigInteger(bytes);
        String hexString = bigInt.toString(16);

        String txhex = Transactions.createRawTransaction(miningaddress, validaddress, amount, hexString);
        System.out.println(txhex);
        int tx_size = txhex.length();
        assertEquals(tx_size, 268);

    }

    @Test
    public void signrawtransaction() throws IOException, JSONException {

        String txhex = Transactions.signRawTransaction(dumptxhex, privatekey);
        int tx_size = txhex.length();
        assertEquals(tx_size, 268);
    }

    @Test
    public void sendrawtransaction() throws IOException, JSONException {

        String txid = Transactions.sendRawTransaction(dumpsignedtxhex);
        assertEquals(txid, "transaction already in block chain");
    }

    @Test
    public void sendsignedtransaction() throws IOException, JSONException {

        byte[] bytes = testdata.getBytes("UTF-8");
        BigInteger bigInt = new BigInteger(bytes);
        String hexString = bigInt.toString(16);

        String txid = Transactions.sendSignedTransaction(miningaddress, validaddress, amount, privatekey, hexString);
        int tx_size = txid.length();
        assertEquals(tx_size, 64);
    }

    @Test
    public void retrievetransaction() throws IOException, JSONException {

        JSONObject item = Transactions.retrieveTransaction(dumptxid);

        String sentdata = item.getString("sent_data");
        assertEquals(sentdata, "hellodata");

        double sentamount = item.getDouble("sent_amount");
        assertEquals(sentamount, 0.01, 0.8);
    }

    @Test
    public void getfee() throws IOException, JSONException {
        double fees = Transactions.getFee(miningaddress, "4b1fbf9fb1e5c93cfee2d37ddc5fef444da0a05cc9354a834dc7155ff861a5e0");
        assertEquals(fees, 0.0269, 0.001);
    }

}