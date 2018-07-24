package com.example.recordskeeper.address;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by Dell on 11-06-2018.
 */
public class StreamTest {

    Stream Stream = new Stream();
    public Properties prop;
    public String miningaddress;
    public String stream;
    public String testdata;

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

    public StreamTest() throws IOException {
        if (getPropert() == true) {
            miningaddress = prop.getProperty("miningaddress");
            stream = prop.getProperty("stream");
            testdata = prop.getProperty("testdata");
        } else {
            miningaddress = System.getenv("miningaddress");
            stream = System.getenv("stream");
            testdata = System.getenv("testdata");
        }
    }

    @Test
    public void publish() throws Exception {

        String txid = Stream.publish(miningaddress, stream, testdata, "This is test data");
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
        JSONObject item = Stream.retrieveWithAddress(stream, miningaddress);
        String data = item.getString("data");
        assertEquals(data, "5468697320697320746573742064617461");
    }

    @Test
    public void retrieveWithKey() throws IOException, JSONException {
        JSONObject item = Stream.retrieveWithKey(stream, testdata);
        String data = item.getString("data");
        assertEquals(data, "5468697320697320746573742064617461");
    }

    @Test
    public void verifydata() throws IOException, JSONException {
        String result = Stream.verifyData(stream, testdata, 5);
        assertEquals(result, "Data is successfully verified.");
    }

    @Test
    public void retrieveItems() throws IOException, JSONException {

        JSONObject item = Stream.retrieveItems(stream, 5);
        String raw_data = item.getString("raw_data");
        assertEquals(raw_data, "LANDOWNERSHIP");
    }
}