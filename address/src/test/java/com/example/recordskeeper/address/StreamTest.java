package com.example.recordskeeper.address;

import org.json.JSONException;
import org.junit.Test;
import java.io.IOException;
import java.math.BigInteger;
import static org.junit.Assert.*;

/**
 * Created by Dell on 11-06-2018.
 */
public class StreamTest {

    Stream Stream = new Stream();
    Config cfg = new Config();
    String miningaddress = cfg.getProperty("miningaddress");
    String stream = cfg.getProperty("stream");
    String testdata = cfg.getProperty("testdata");

    public StreamTest() throws IOException {}

    @Test
    public void publish() throws Exception {
        byte[] bytes = "This is test data".getBytes("UTF-8");
        BigInteger bigInt = new BigInteger(bytes);
        String hexString = bigInt.toString(16);

        String txid = Stream.publish(miningaddress, stream, testdata, hexString);
        int tx_size = txid.length();
        assertEquals(tx_size, 64);
    }

    @Test
    public void retrieve() throws IOException, JSONException {
        String raw_data = Stream.retrieve(stream, "eef0c0c191e663409169db0972cc75ff91e577a072289ee02511b410bc304d90");
        assertEquals(raw_data,"testdata");
    }

    @Test
    public void retrieveWithAddress() throws IOException, JSONException {
        String data = Stream.retrieveWithAddress(stream, miningaddress);
        assertEquals(data, "5468697320697320746573742064617461");
    }

    @Test
    public void retrieveWithKey() throws IOException, JSONException {
        String data = Stream.retrieveWithKey(stream, testdata);
        assertEquals(data, "5468697320697320746573742064617461");
    }

    @Test
    public void verifydata() throws IOException, JSONException {
        String result = Stream.verifyData(stream, testdata, 5);
        assertEquals(result, "Data is successfully verified.");
    }

    @Test
    public void retrieveItems() throws IOException, JSONException {
        String result = Stream.retrieveItems(stream, 5);
        assertEquals(result, "This is test data");
    }
}