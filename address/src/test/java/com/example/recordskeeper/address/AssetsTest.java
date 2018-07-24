package com.example.recordskeeper.address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class AssetsTest {

    Assets Assets = new Assets();
    public Properties prop;
    public String validaddress;

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

    public AssetsTest() throws IOException {
        if (getPropert() == true) {
            validaddress = prop.getProperty("validaddress");
        } else {
            validaddress = System.getenv("validaddress");
        }
    }

    @Test
    public void createAsset() throws Exception {

        String txid = Assets.createAsset(validaddress, "qwerty", 100);
        assertEquals(txid, "Asset or stream with this name already exists.");

    }

    @Test
    public void retrieveasset() throws IOException, JSONException {

        JSONObject item = Assets.retrieveAssets();
        JSONArray array = item.getJSONArray("Output");
        for(int i = 0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String asset_name = object.getString("asset_name");
            String issue_id = object.getString("issue_id");
            int issue_qty = object.getInt("issue_qty");
            if(asset_name == "xyzd")
                assertEquals(asset_name, "xyzd");
            if(issue_id == "xyzd")
                assertEquals(issue_id, "xyzd");
            if(issue_qty == 100)
                assertEquals(issue_qty, 100);
        }
    }

    @Test
    public void sendAsset() throws Exception {

        String txid = Assets.sendAssets(validaddress, "xyz", 10);
        int txid_len = txid.length();
        assertEquals(txid_len, 64);
    }
}