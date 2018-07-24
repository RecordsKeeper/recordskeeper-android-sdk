package com.example.recordskeeper.address;

import org.json.JSONException;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class PermissionsTest {

    Permissions permissions = new Permissions();
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


    public PermissionsTest() throws IOException {
        if (getPropert() == true) {
            validaddress = prop.getProperty("validaddress");
        } else {
            validaddress = System.getenv("validaddress");
        }
    }

    @Test
    public void grantPermission() throws Exception {
        String result = permissions.grantPermission(validaddress, "create,connect");
        int len = result.length();
        assertEquals(len, 64);
    }

    @Test
    public void failgrantpermissions() throws IOException, JSONException {
        String result = permissions.grantPermission(validaddress, "create,connect");
        assertEquals(result, "e3bba87d1f0a980b65f12388d31c734ea38b08d11d00aaab1004e470ca419556");
    }

    @Test
    public void revokepermissions() throws IOException, JSONException {

        String result = permissions.revokePermission(validaddress, "send");
        int len = result.length();
        assertEquals(len, 64);
    }

    @Test
    public void failrevokepermissions() throws IOException, JSONException {

        String result = permissions.revokePermission(validaddress, "create,connect");
        assertEquals(result, "e3bba87d1f0a980b65f12388d31c734ea38b08d11d00aaab1004e470ca419556");
    }
}