package com.example.recordskeeper.address;

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
        String miner = Block.blockinfo("100");
        assertEquals(miner, mainaddress);

        //int size = Block.blockinfo("100");
       // assertEquals(size, 300);

        //int nonce = Block.blockinfo("100");
        //assertEquals(nonce, 260863);

        String merkleroot = Block.blockinfo("100");
        assertEquals(merkleroot, "c6d339bf75cb969baa4c65e1ffd7fade562a191fa90aac9dd495b764f2c1b429");
    }

    @Test
    public void retreiveBlocks() throws Exception{
        String miner = Block.retrieveBlocks("10-20");
        assertEquals(miner, mainaddress);

     //   int blocktime = Block.retrieveBlocks("10-20");
     //   assertEquals(blocktime, 1522831624);

     //   String blockhash = Block.retrieveBlocks("10-20");
     //   assertEquals(blockhash, "000002d184165e5c18facde8a5678acd975ba9d315eb440752d83dcd70d4abd5");

      //  int txcount = Block.retrieveBlocks("10-20");
      //  assertEquals(txcount, 1);
    }
}