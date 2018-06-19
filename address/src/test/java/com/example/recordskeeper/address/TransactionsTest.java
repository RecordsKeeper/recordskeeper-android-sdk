package com.example.recordskeeper.address;

import org.json.JSONException;
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
        int tx_size = txhex.length();
        assertEquals(tx_size, 268);

    }

    @Test
    public void signrawtransaction() throws IOException, JSONException {

        String txhex = Transactions.signRawTransaction("0100000001fd52d9622262c0d491a93bd14b0190362b3d1c362a429750411a255bfd83e91a0000000000ffffffff03005a6202000000001976a914b2bc8a974aa0b9c4ad82ac0a7017160df0751f5888ac0000000000000000066a0474657374a0e61239000000001976a9145f2976565b53d4ed013b6131e98201e89787518688ac00000000", privatekey);
        int tx_size = txhex.length();
        assertEquals(tx_size, 268);
    }

    @Test
    public void sendrawtransaction() throws IOException, JSONException {

        String txid = Transactions.sendRawTransaction("0100000001fd52d9622262c0d491a93bd14b0190362b3d1c362a429750411a255bfd83e91a000000006b483045022100d05595c9a60b3ee0d9ae6479a6f7be64a6fccf993bbfba1135fb1d68d873e41a02202fb4754f8447e0da862e0637183853640a4a0758f4dc0b42315d0d6332d2c2f90121038c1d7be91850add595b238c541d17cfa6e6d780a410bb7db4930a982cad933d4ffffffff03005a6202000000001976a914b2bc8a974aa0b9c4ad82ac0a7017160df0751f5888ac0000000000000000066a0474657374a0e61239000000001976a9145f2976565b53d4ed013b6131e98201e89787518688ac00000000");
        int tx_size = txid.length();
        assertEquals(tx_size, 64);
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
        String sentdata = Transactions.retrieveTransaction(dumptxid);
        assertEquals(sentdata, "hellodata");

        //int sentamount = Transactions.retrieveTransaction(dumptxid);
        //assertNotEquals(sentamount, 0);
    }

    @Test
    public void getfee() throws IOException, JSONException {
        double fees = Transactions.getFee(miningaddress, "4b1fbf9fb1e5c93cfee2d37ddc5fef444da0a05cc9354a834dc7155ff861a5e0");
        assertEquals(fees, 0.0269, 0.001);
    }

}