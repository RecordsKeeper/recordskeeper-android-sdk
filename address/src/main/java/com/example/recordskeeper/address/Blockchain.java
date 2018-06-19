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

public class Blockchain {

    private String permission;
    private String resp;

    Config cfg = new Config();

    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    public Blockchain() throws IOException {
    }

    public String getChainInfo() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getblockchainparams\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        String chain_protocol = object.getString("chain-protocol");
        String chain_name = object.getString("chain-name");
        String chain_description = object.getString("chain-description");
        String root_stream = object.getString("root-stream-name");
        int max_blocksize = object.getInt("maximum-block-size");
        int default_networkport = object.getInt("default-network-port");
        int default_rpcport = object.getInt("default-rpc-port");
        int mining_diversity =  object.getInt("mining-diversity");
        return chain_name;//chain_protoco,chain_description, root_stream, max_blocksize, default_networkport, default_rpcport, mining_diversity, chain_name;
    }

    public int getNodeInfo() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getinfo\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        int node_balance = object.getInt("balance");
        int synced_blocks = object.getInt("blocks");
        String node_address = object.getString("nodeaddress");
        int difficulty = object.getInt("difficulty");

        return synced_blocks; //node_balance,synced_blocks, node_address, difficulty;
    }

    public String permissions() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"listpermissions\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        int pms_count = array.length();
        String output = "";
        for (int i = 0; i<pms_count; i++) {
            permission = array.getJSONObject(i).getString("type");
            if(i != pms_count - 1)
                output += permission +",";
            else output += permission;
        }
        return output;
    }

    public String getpendingTransactions() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getmempoolinfo\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        int tx_count = object.getInt("size");

        RequestBody body1 = RequestBody.create(mediaType, "{\"method\":\"getrawmempool\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
        Request request1 = new Request.Builder()
                .url(url)
                .method("POST", body1)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response1 = client.newCall(request1).execute();
        String resp1 = response1.body().string();
        JSONObject jsonObject1 = new JSONObject(resp1);
        JSONArray array = jsonObject1.getJSONArray("result");
        String tx = "";
        for (int i = 0; i<jsonObject1.length(); i++) {
            tx = String.valueOf(jsonObject1.get("result"));
        }
        return tx; //tx_count, tx;
    }

    public int checkNodeBalance() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getmultibalances\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        JSONArray object1 = object.getJSONArray("total");
        JSONObject object2 = object1.getJSONObject(0);
        int qty = object2.getInt("qty");

        return qty;
    }
}
