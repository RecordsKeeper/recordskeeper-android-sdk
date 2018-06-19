package com.example.recordskeeper.address;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Block {

    private String block_height;
    private String block_range;
    private String blockhash;
    private String miner;
    private int blocktime;
    private int tx_count;
    private String resp;

    Config cfg = new Config();

    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    public Block() throws IOException {}

    public String blockinfo(String block_height) throws IOException, JSONException {

        this.block_height = "\"" +block_height+ "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getblock\",\"params\":["+this.block_height+"],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        resp = response.body().string();
        JSONObject jsonObject = new JSONObject(resp);
        JSONObject object = jsonObject.getJSONObject("result");
        JSONArray array = object.getJSONArray("tx");
        String tx = array.getString(0);
        int tx_count = array.length();
        int size = object.getInt("size");
        int nonce = object.getInt("nonce");
        int blocktime = object.getInt("time");
        int difficulty = object.getInt("difficulty");
        String miner = object.getString("miner");
        String blockHash = object.getString("hash");
        //String prevblock = object.getString("previousblockhash");
        String nextblock = object.getString("nextblockhash");
        String merkleroot = object.getString("merkleroot");
        for (int i = 0; i<tx_count; i++){
            String add = array.getString(i);
        }
        return miner;  //tx_count, tx, miner, size, nonce, blockHash, prevblock, nextblock, merkleroot, blocktime, difficulty;
    }

    public String retrieveBlocks(String block_range) throws IOException, JSONException {

        this.block_range = "\"" + block_range + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"listblocks\",\"params\":[" + this.block_range + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        resp = response.body().string();
        JSONObject jsonObject = new JSONObject(resp);
        JSONArray array = jsonObject.getJSONArray("result");
        int block_count = array.length();
        for (int i = 0; i < block_count; i++) {

            JSONObject object = array.getJSONObject(i);
            blockhash = object.getString("hash");
            miner = object.getString("miner");
            blocktime = object.getInt("time");
            tx_count = object.getInt("txcount");
        }
            return miner; //blockhash, miner, blocktime, tx_count;
    }
}