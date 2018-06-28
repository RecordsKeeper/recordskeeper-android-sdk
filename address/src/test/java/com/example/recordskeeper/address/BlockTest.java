package com.example.recordskeeper.address;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 * Created by Dell on 08-06-2018.
 */
public class BlockTest {

    Block Block = new Block();
    Config cfg = new Config();
    String mainaddress = cfg.getProperty("mainaddress");

    public BlockTest() throws IOException {}

    @Test
    public void blockinfo() throws Exception {

        JSONObject item = Block.blockinfo(100);

        String miner = item.getString("miner");
        assertEquals(miner, mainaddress);

        int size = item.getInt("size");
        assertEquals(size, 300);

        int nonce = item.getInt("nonce");
        assertEquals(nonce, 260863);

        String merkleroot = item.getString("merkleroot");
        assertEquals(merkleroot, "c6d339bf75cb969baa4c65e1ffd7fade562a191fa90aac9dd495b764f2c1b429");
    }

    @Test
    public void retreiveBlocks() throws Exception {

        JSONObject object = Block.retrieveBlocks("10-20");
        JSONArray array = object.getJSONArray("Output");

        JSONObject item = array.getJSONObject(0);
        String miner = item.getString("miner");
        assertEquals(miner, mainaddress);

        JSONObject item1 = array.getJSONObject(2);
        int blocktime = item1.getInt("blocktime");
        assertEquals(blocktime, 1522831624);

        JSONObject item2 = array.getJSONObject(4);
        String blockhash = item2.getString("hash");
        assertEquals(blockhash, "000002d184165e5c18facde8a5678acd975ba9d315eb440752d83dcd70d4abd5");

        JSONObject item3 = array.getJSONObject(3);
        int tx_count = item3.getInt("tx_count");
        assertEquals(tx_count, 1);

    }
}