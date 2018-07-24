package com.example.recordskeeper.address;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class AddressTest {

    Address Address = new Address();
    String address;
    public Properties prop;
    public String multisigaddress;
    public String validaddress;
    public String invalidaddress;
    public String miningaddress;
    public String nonminingaddress;
    public String wrongimportaddress;

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

    public AddressTest() throws IOException {
        if (getPropert() == true) {
            multisigaddress = prop.getProperty("multisigaddress");
            validaddress = prop.getProperty("validaddress");
            miningaddress = prop.getProperty("miningaddress");
            nonminingaddress = prop.getProperty("nonminingaddress");
            invalidaddress = prop.getProperty("invalidaddress");
            wrongimportaddress = prop.getProperty("wrongimportaddress");
        } else {
            multisigaddress = System.getenv("multisigaddress");
            validaddress = System.getenv("validaddress");
            miningaddress = System.getenv("miningaddress");
            nonminingaddress = System.getenv("nonminingaddress");
            invalidaddress = System.getenv("invalidaddress");
            wrongimportaddress = System.getenv("wrongimportaddress");
        }
    }

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
        address = Address.getMultisigWalletAddress(2, "03b04307378cfb589394ea6538046b9a9c817d096215ab9185e7b497ae98ef7009,0376b4138b1a55e29d0b4957c3325bed914e0b288e70c81479e862dc523d0b52b4");
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
        double balance = Address.checkBalance(nonminingaddress);
        assertEquals(balance, 5.0, 8.0);
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