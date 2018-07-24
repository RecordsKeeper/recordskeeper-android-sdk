package com.example.recordskeeper.address;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

import static org.junit.Assert.*;

public class TransactionsTest{

    Transactions Transactions = new Transactions();
    public Properties prop;
    public String validaddress;
    public String dumptxhex;
    public String miningaddress;
    public String testdata;
    public String privatekey;
    public double amount;
    public String dumpsignedtxhex;
    public String dumptxid;

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

    public TransactionsTest() throws IOException {
        if (getPropert() == true) {
            validaddress = prop.getProperty("validaddress");
            miningaddress = prop.getProperty("miningaddress");
            amount = Double.parseDouble(prop.getProperty(String.valueOf("amount")));
            testdata = prop.getProperty("testdata");
            dumptxhex = prop.getProperty("dumptxhex");
            privatekey = prop.getProperty("privatekey");
            dumpsignedtxhex = prop.getProperty("dumpsignedtxhex");
            dumptxid = prop.getProperty("dumptxid");
        } else {
            validaddress = System.getenv("validaddress");
            miningaddress = System.getenv("miningaddress");
            amount = Double.parseDouble(System.getenv(String.valueOf("amount")));
            testdata = System.getenv("testdata");
            dumptxhex = System.getenv("dumptxhex");
            privatekey = System.getenv("privatekey");
            dumpsignedtxhex = System.getenv("dumpsignedtxhex");
            dumptxid = System.getenv("dumptxid");
        }
    }


    @Test
    public void sendTransaction() throws Exception {

        String txid = Transactions.sendTransaction(miningaddress, validaddress, "hello", 0.2);
        int tx_size = txid.length();
        assertEquals(tx_size, 64);
    }

    @Test
    public void createRawTransaction() throws IOException, JSONException {

        String txhex = Transactions.createRawTransaction(miningaddress, validaddress, amount, testdata);
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

        String txid = Transactions.sendSignedTransaction(miningaddress, validaddress, amount, privatekey, testdata);
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