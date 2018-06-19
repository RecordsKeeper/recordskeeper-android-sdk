package com.example.recordskeeper.address;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PermissionsTest {

    Permissions permissions = new Permissions();
    Config cfg = new Config();
    String permissionaddress = cfg.getProperty("permissionaddress");

    public PermissionsTest() throws IOException {
    }

    @Test
    public void grantPermission() throws Exception {
        String result = permissions.grantPermission(permissionaddress, "create, connect");
        assertEquals(result, "Invalid permission");
    }

    @Test
    public void failgrantpermissions() throws IOException, JSONException {
        String result = permissions.grantPermission(permissionaddress, "create, connect");
        assertEquals(result, "e3bba87d1f0a980b65f12388d31c734ea38b08d11d00aaab1004e470ca419556");
    }

    @Test
    public void revokepermissions() throws IOException, JSONException {

        String result = permissions.revokePermission(permissionaddress, "send");
        assertEquals(result, "Invalid permission");
    }

    @Test
    public void failrevokepermissions() throws IOException, JSONException {

        String result = permissions.revokePermission(permissionaddress, "create, connect");
        assertEquals(result, "e3bba87d1f0a980b65f12388d31c734ea38b08d11d00aaab1004e470ca419556");
    }
}