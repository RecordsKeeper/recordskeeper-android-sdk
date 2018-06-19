package com.example.recordskeeper.address;

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
        address = Address.getMultisigAddress(2,  "miygjUPKZNV94t9f8FqNvNG9YjCkp5qqBZ, mwDbTVQcATL263JwpoE8AHCMGM5hE1kd7m, mpC8A8Fob9ADZQA7iLrctKtwzyWTx118Q9" );
        assertEquals(address, multisigaddress);
    }

    @Test
    public void getMultisigWalletAddress() throws Exception {
        address = Address.getMultisigWalletAddress(2, "miygjUPKZNV94t9f8FqNvNG9YjCkp5qqBZ, mwDbTVQcATL263JwpoE8AHCMGM5hE1kd7m, mpC8A8Fob9ADZQA7iLrctKtwzyWTx118Q9");
        assertEquals(address, multisigaddress);
    }

    @Test
    public void retrieveAddresses() throws Exception{
        address = Address.retrieveAddress();
    }

    @Test
    public void checkifvalid() throws Exception{
        String add = Address.checkifValid(validaddress);
        assertEquals(add, validaddress);
    }

    @Test
    public void failcheckifvalid() throws Exception{
        String add = Address.checkifValid(invalidaddress);
        assertEquals(add, invalidaddress);
    }

    @Test
    public void checkifMineAllowed() throws Exception{
        String add = Address.checkifMineAllowed(miningaddress);
        assertEquals(add, miningaddress);
    }

    @Test
    public void failcheckifMineAllowed() throws Exception{
        String add = Address.checkifMineAllowed(nonminingaddress);
        assertEquals(add, nonminingaddress);
    }

    @Test
    public void checkBalance() throws Exception{
        int balance = Address.checkBalance(nonminingaddress);
        assertEquals(balance, 5);
    }

    @Test
    public void importAddress() throws Exception{
        String public_address = Address.importAddress(miningaddress);
        assertEquals(public_address, miningaddress);
    }

    @Test
    public void wrongimportAddress() throws Exception{
        String public_address = Address.importAddress(wrongimportaddress);
        assertEquals(public_address, wrongimportaddress);
    }
}