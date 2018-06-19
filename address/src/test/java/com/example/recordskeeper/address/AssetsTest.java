package com.example.recordskeeper.address;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AssetsTest {

    Assets Assets = new Assets();
    Config cfg = new Config();
    String validaddress = cfg.getProperty("validaddress");

    public AssetsTest() throws IOException {
    }

    @Test
    public void createAsset() throws Exception {

        Object txid = Assets.createAsset("1TcfvD39TZES3q6khfG8B43eGDNAVqTqMxAHvv", "xyz", 100);
        assertEquals(txid, null);

    }

    @Test
    public void retrieveasset() throws IOException, JSONException {

        String name = Assets.retrieveAssets();
        assertEquals(name, "xyzd");

        //String txid = Assets.retrieveAssets();
        //assertArrayEquals(txid, "");

        //int qty = Assets.retrieveAssets();
        //assertArrayEquals(qty, 100);

    }

    @Test
    public void sendAsset() throws Exception {

        String txid = Assets.sendasset("1TcfvD39TZES3q6khfG8B43eGDNAVqTqMxAHvv", "xyz", 100);
        System.out.println(txid);
        assertEquals(txid, "618e1145febb40ec8dcbf2aceb1ee31c7fda0466655d76b98576425c683ead35");

    }

}