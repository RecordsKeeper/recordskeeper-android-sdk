package com.example.recordskeeper.address;

import org.json.JSONException;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class BlockchainTest {

    Blockchain Blockchain = new Blockchain();
    Config cfg = new Config();
    String chain = cfg.getProperty("chain");
    String stream = cfg.getProperty("stream");
    int port = Integer.parseInt(cfg.getProperty("port"));

    public BlockchainTest() throws IOException {}

    @Test
    public void getChainInfo() throws Exception {
        String chainname = Blockchain.getChainInfo();
        assertEquals(chainname, chain);

   //     String rootstream = Blockchain.getChainInfo();
   //     assertEquals(rootstream, stream);

       // int rpcport = Blockchain.getChainInfo();
       // assertEquals(rpcport, port);

       // int networkport = Blockchain.getChainInfo();
       // assertEquals(networkport, 8379);
    }

    @Test
    public void getNodeInfo() throws IOException, JSONException {
        int info = Blockchain.getNodeInfo();
        assertNotEquals(info, 60);

     // int balance = Blockchain.getNodeInfo();
     // assertNotNull(balance);

     //   int difficulty = Blockchain.getNodeInfo();
     //   assertNotSame(difficulty, 1);
    }

    @Test
    public void permissions() throws IOException, JSONException {
        String output = Blockchain.permissions();
        assertEquals(output, "mine,admin,activate,connect,send,receive,issue,create");
    }

    @Test
    public void getpendingTransactions() throws IOException, JSONException {
        String pendingtx = Blockchain.getpendingTransactions();
        assertEquals(pendingtx, "[]");

        //int pendingtxcount = Blockchain.getpendingTransactions();
        //assertEquals(pendingtxcount, 0);
    }

    @Test
    public void checkNodeBalance() throws IOException, JSONException {
        int balance = Blockchain.checkNodeBalance();
        assertNotEquals(balance, 0);
    }
}