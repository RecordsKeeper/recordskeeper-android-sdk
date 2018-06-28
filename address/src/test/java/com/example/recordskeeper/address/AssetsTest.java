package com.example.recordskeeper.address;

import org.json.JSONException;
import org.json.JSONObject;
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

        String txid = Assets.createAsset(validaddress, "qwerty", 100);
        assertEquals(txid, "Asset or stream with this name already exists.");

    }

    @Test
    public void retrieveasset() throws IOException, JSONException {

        JSONObject item = Assets.retrieveAssets();

        String asset_name = item.getString("asset_name");
        assertEquals(asset_name, "xyzd");

        String txid = item.getString("issue_id");
        assertEquals(txid, "xyzd");

        int qty = item.getInt("issue_qty");
        assertEquals(qty, 100);

    }

    @Test
    public void sendAsset() throws Exception {

        String txid = Assets.sendAssets(validaddress, "xyz", 10);
        int txid_len = txid.length();
        assertEquals(txid_len, 64);

    }

}