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
import static okio.ByteString.decodeHex;

public class Stream {

    private String address;
    private String stream;
    private String txid;
    private String key;
    private String data;
    private String datahex;
    private String publishers;
    private String raw_data = "";
    private String resp;
    private int count;

    Config cfg = new Config();

    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    public Stream() throws IOException {}

    public String publish(String address, String stream, String key, String data) throws IOException, JSONException {

        this.datahex = "\"" + data + "\"";
        this.address = "\"" + address + "\"";
        this.stream = "\"" + stream + "\"";
        this.key = "\"" + key + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"publishfrom\",\"params\":[" + this.address + "," + this.stream + "," + this.key + "," + this.datahex + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        txid = jsonObject.getString("result");

        return txid;
    }

    public String retrieve(String stream, String txid) throws IOException, JSONException {

        this.stream = "\"" + stream + "\"";
        this.txid = "\"" + txid + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getstreamitem\",\"params\":[" + this.stream + "," + this.txid + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        data = object.getString("data");
        byte[] bytes = decodeHex(data).toByteArray();
        raw_data = new String(bytes);

        return raw_data;
    }

    public String retrieveWithAddress(String stream, String address) throws IOException, JSONException {

        this.stream = "\"" + stream + "\"";
        this.address = "\"" + address + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"liststreampublisheritems\",\"params\":[" + this.stream + "," + this.address + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            key = object.getString("key");
            data = object.getString("data");
            byte[] bytes = decodeHex(data).toByteArray();
            raw_data = new String(bytes);
            txid = object.getString("txid");
        }
        return data; //key, raw_data, txid;
    }

    public String retrieveWithKey(String stream, String key) throws IOException, JSONException {

        this.stream = "\"" + stream + "\"";
        this.key = "\"" + key + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"liststreamkeyitems\",\"params\":[" + this.stream + "," + this.key + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        JSONObject object1 = array.getJSONObject(0);
        JSONArray publishers = object1.getJSONArray("publishers");
        String pubs = publishers.getString(0);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            data = object.getString("data");
            byte[] bytes = decodeHex(data).toByteArray();
            raw_data = new String(bytes);
            txid = object.getString("txid");
        }

        return data; // publisher, raw_data, txid;
    }

    public String verifyData(String stream, String data, int count) throws IOException, JSONException {

        this.stream = "\"" + stream + "\"";
        this.data = "\"" + data + "\"";
        this.count = count;
        boolean False = false;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"liststreamitems\",\"params\":[" + this.stream + "," + False + "," + this.count + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        for (int i = 0; i < count; i++) {
            JSONObject object = array.getJSONObject(i);
            data = object.getString("data");
            byte[] bytes = decodeHex(data).toByteArray();
            raw_data = new String(bytes);
        }
        String result;
        if (data == null) {
            result = "Data not found";
        } else {
            result = "Data is successfully verified.";
        }

        return result;
    }

    public String retrieveItems(String stream, int count) throws IOException, JSONException {

        this.stream = "\"" +stream+ "\"";
        this.count = count;

        boolean False = false;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"liststreamitems\",\"params\":[" + this.stream + "," + False + "," + this.count + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        for(int i = 0; i<count; i++){
            JSONObject object = array.getJSONObject(i);
            JSONArray array1 = object.getJSONArray("publishers");
            address = array1.getString(0);
            key = object.getString("key");
            data = object.getString("data");
            byte[] bytes = decodeHex(data).toByteArray();
            raw_data = new String(bytes);
            txid = object.getString("txid");
        }

        JSONObject jsonObject1 = array.getJSONObject(0);
        data = jsonObject1.getString("data");
        byte[] bytes = decodeHex(data).toByteArray();
        raw_data = new String(bytes);

        return raw_data; //address,key_value,raw_data,txid;
    }
}