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

public class Permissions {

    private String address;
    private String permissions;
    private String resp;

    Config cfg = new Config();

    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    public Permissions() throws IOException {}

    public String grantPermission(String address, String permissions) throws IOException, JSONException {

        this.address = "\"" + address + "\"";
        this.permissions = "\"" + permissions + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"grant\",\"params\":[" + this.address + "," + this.permissions + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String result;
        if (jsonObject.isNull("result")) {
            JSONObject object = jsonObject.getJSONObject("error");
            result = object.getString("message");
        } else
            result = jsonObject.getString("result");

        return result;
    }

    public String revokePermission(String address, String permissions) throws IOException, JSONException {

        this.address = "\"" +address+ "\"";
        this.permissions = "\"" +permissions+ "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"grant\",\"params\":[" + this.address + "," + this.permissions + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String result;
        if (jsonObject.isNull("result")) {
            JSONObject object = jsonObject.getJSONObject("error");
            result = object.getString("message");
        } else
            result = jsonObject.getString("result");

        return result;
    }
}