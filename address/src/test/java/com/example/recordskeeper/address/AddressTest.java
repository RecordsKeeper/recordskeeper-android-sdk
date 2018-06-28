package com.example.recordskeeper.address;

import org.json.JSONObject;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class AddressTest {

    Address Address = new Address();
    String address;
    Config cfg = new Config();
    String multisigaddress = cfg.getProperty("multisigaddress");
    String validaddress = cfg.getProperty("validaddress");
    String miningaddress = cfg.getProperty("miningaddress");
    String nonminingaddress = cfg.getProperty("nonminingaddress");
    String qty = cfg.getProperty("qty");
    String invalidaddress = cfg.getProperty("invalidaddress");
    String wrongimportaddress = cfg.getProperty("wrongimportaddress");

    public AddressTest() throws IOException {}

    @Test
    public void getAddress() throws Exception {
        address = Address.getAddress();
        int address_size = address.length();
        assertEquals(address_size, 34);
    }

    @Test
    public void getMultisigAddress() throws Exception {
        address = Address.getMultisigAddress(2,  "03b04307378cfb589394ea6538046b9a9c817d096215ab9185e7b497ae98ef7009,0376b4138b1a55e29d0b4957c3325bed914e0b288e70c81479e862dc523d0b52b4");
        assertEquals(address, multisigaddress);
    }

    @Test
    public void getMultisigWalletAddress() throws Exception {
        address = Address.getMultisigWalletAddress(2,  "03b04307378cfb589394ea6538046b9a9c817d096215ab9185e7b497ae98ef7009, 0376b4138b1a55e29d0b4957c3325bed914e0b288e70c81479e862dc523d0b52b4");
        assertEquals(address, multisigaddress);
    }

    @Test
    public void checkifvalid() throws Exception{
        String add = Address.checkifValid(validaddress);
        assertEquals(add, "The Address is Valid");
    }

    @Test
    public void failcheckifvalid() throws Exception{
        String res = Address.checkifValid(invalidaddress);
        assertEquals(res, "The Address is Valid");
    }

    @Test
    public void checkifMineAllowed() throws Exception{
        String res = Address.checkifMineAllowed(miningaddress);
        assertEquals(res, "The Address has mining permission");
    }

    @Test
    public void failcheckifMineAllowed() throws Exception{
        String res = Address.checkifMineAllowed(nonminingaddress);
        assertEquals(res, "The Address has mining permission");
    }

    @Test
    public void checkBalance() throws Exception{
        int balance = Address.checkBalance(nonminingaddress);
        assertEquals(balance, 7);
    }

    @Test
    public void importAddress() throws Exception{
        String res = Address.importAddress(miningaddress);
        assertEquals(res, "Address successfully imported");
    }

    @Test
    public void wrongimportAddress() throws Exception{
        String res = Address.importAddress(wrongimportaddress);
        assertEquals(res, "Address successfully imported");
    }
}