package com.example.recordskeeper.address;

import org.json.JSONException;
import org.json.JSONObject;
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

        JSONObject item = Blockchain.getChainInfo();

        String chain_name = item.getString("chain_name");
        assertEquals(chain_name, chain);

        String rootstream = item.getString("root_stream_name");
        assertEquals(rootstream, stream);

        int rpcport = item.getInt("default_rpcport");
        assertEquals(rpcport, port);

        int networkport = item.getInt("default_networkport");
        assertEquals(networkport, 8379);
    }

    @Test
    public void getnodeInfo() throws IOException, JSONException {

        JSONObject item = Blockchain.getNodeInfo();

        int blocks = item.getInt("blocks");
        assertNotNull(blocks);

        int balance = item.getInt("balance");
        assertNotNull(balance);

        double difficulty = item.getDouble("difficulty");
        assertNotNull(difficulty);
    }

    @Test
    public void permissions() throws IOException, JSONException {
        String output = Blockchain.permissions();
        assertEquals(output, "mine,admin,activate,connect,send,receive,issue,create");
    }

    @Test
    public void getpendingTransactions() throws IOException, JSONException {

        JSONObject item = Blockchain.getpendingTransactions();

        String pendingtx = item.getString("tx");
        assertEquals(pendingtx, "");

        double pendingtxcount = item.getDouble("tx_count");
        assertEquals(pendingtxcount, 0.0, 0.1);
    }

    @Test
    public void checkNodeBalance() throws IOException, JSONException {
        double balance = Blockchain.checkNodeBalance();
        assertNotNull(balance);
    }
}