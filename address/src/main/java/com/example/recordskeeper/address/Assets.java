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

public class Assets {

    private String address;
    private String asset_name;
    private int asset_qty;
    private String issue_id;
    private int issue_qty;
    private String resp;

    Config cfg = new Config();

    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    public Assets() throws IOException {}

    public Object createAsset(String address, String asset_name, int asset_qty) throws IOException, JSONException {

        this.address = "\"" + address + "\"";
        this.asset_name = "\"" + asset_name + "\"";
        this.asset_qty = asset_qty;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"issue\",\"params\":[" + this.address + "," + this.asset_name + "," + this.asset_qty + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        if(jsonObject.isNull("result"))
            return jsonObject.get("result");

        return jsonObject.get("result");
    }

    public String retrieveAssets() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"listassets\",\"params\":[],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        int asset_count = array.length();
        for (int i = 0; i<asset_count; i++) {
            JSONObject object = array.getJSONObject(i);
            asset_name = object.getString("name");
            issue_id = object.getString("issuetxid");
            issue_qty = object.getInt("issueraw");
        }
        return asset_name;//asset_name,issue_id, issue_qty, asset_count;
    }

    public String sendasset(String address, String asset_name, int asset_qty) throws IOException, JSONException {

        this.address = "\"" + address + "\"";
        this.asset_name = "\"" + asset_name + "\"";
        this.asset_qty = asset_qty;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"sendasset\",\"params\":[" + this.address + "," + this.asset_name + "," + this.asset_qty +"],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String txid = jsonObject.getString("result");
        return txid;
    }
}
