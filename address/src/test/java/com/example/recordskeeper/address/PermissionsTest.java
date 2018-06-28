package com.example.recordskeeper.address;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PermissionsTest {

    Permissions permissions = new Permissions();
    Config cfg = new Config();

    String validaddress = cfg.getProperty("validaddress");

    public PermissionsTest() throws IOException {
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