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
import java.util.Arrays;
import java.util.List;
import static java.lang.Math.abs;
import static okio.ByteString.decodeHex;

public class Transactions {

    private String sender_address;
    private String reciever_address;
    private String data;
    private double amount;
    private String datahex;
    private String txHex;
    private String private_key;
    private String signed_txHex;
    private String txid;
    private String address;
    private String resp;

    Config cfg = new Config();

    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    public Transactions() throws IOException {}

    public String sendTransaction(String sender_address, String reciever_address, String data, double amount) throws IOException, JSONException {

        this.datahex = "\"" + data + "\"";
        this.sender_address = "\"" + sender_address + "\"";
        this.reciever_address = "\"" + reciever_address + "\"";
        this.amount = amount;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createrawsendfrom\",\"params\":[" + this.sender_address + ", {" + this.reciever_address + " :" + this.amount + "}, [" + this.datahex + "],\"send\"],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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

    public String createRawTransaction(String sender_address, String reciever_address, double amount, String data) throws IOException, JSONException {

        this.sender_address = "\"" + sender_address + "\"";
        this.reciever_address = "\"" + reciever_address + "\"";
        this.amount = amount;
        this.datahex = "\"" + data + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createrawsendfrom\",\"params\":[" + this.sender_address + ", {" + this.reciever_address + " :" + this.amount + "}, [" + this.datahex + "],\"\"],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String txhex = jsonObject.getString("result");

        return txhex;
    }

    public String signRawTransaction(String txHex, String private_key) throws IOException, JSONException {

        this.txHex = "\"" + txHex + "\"";
        this.private_key = "\"" + private_key + "\"";
        List<String> output = Arrays.asList(this.private_key);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"signrawtransaction\",\"params\":[" + this.txHex + ",[]," + output + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String signedHex = object.getString("hex");

        return signedHex;
    }

    public String sendRawTransaction(String signed_txHex) throws IOException, JSONException {

        this.signed_txHex = "\"" + signed_txHex + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"sendrawtransaction\",\"params\":[" + this.signed_txHex + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        if (jsonObject.isNull("result")) {
            JSONObject object = jsonObject.getJSONObject("error");
            txid = object.getString("message");
            System.out.println(txid);
        } else {
            txid = jsonObject.getString("result");
            System.out.println(txid);
        }
        return txid;
    }

    public String sendSignedTransaction(String sender_address, String reciever_address, double amount, String private_key, String data) throws IOException, JSONException {


        this.sender_address = "\"" + sender_address + "\"";
        this.reciever_address = "\"" + reciever_address + "\"";
        this.amount = amount;
        this.private_key = "\"" + private_key + "\"";
        this.datahex = "\"" + data + "\"";
        List<String> output = Arrays.asList(this.private_key);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createrawsendfrom\",\"params\":[" + this.sender_address + ", {" + this.reciever_address + " :" + this.amount + "}, [" + this.datahex + "],\"\"],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String txhexi;
        txhexi = jsonObject.getString("result");

        RequestBody body1 = RequestBody.create(mediaType, "{\"method\":\"signrawtransaction\",\"params\":[\"" + txhexi + "\",[]," + output + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        JSONObject object1 = jsonObject1.getJSONObject("result");
        String signedHexi;
              signedHexi  = object1.getString("hex");

        RequestBody body2 = RequestBody.create(mediaType, "{\"method\":\"sendrawtransaction\",\"params\":[\"" + signedHexi + "\"],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
        Request request2 = new Request.Builder()
                .url(url)
                .method("POST", body2)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response2 = client.newCall(request2).execute();
        String resp2 = response2.body().string();
        JSONObject jsonObject2 = new JSONObject(resp2);
        if (jsonObject.isNull("result")) {
            JSONObject object = jsonObject2.getJSONObject("error");
            txid = object.getString("message");
            System.out.println(txid);
        } else {
            txid = jsonObject2.getString("result");
        }
        return txid;
    }

    public String retrieveTransaction(String txid) throws IOException, JSONException {

        this.txid = "\"" + txid + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getrawtransaction\",\"params\":[" + this.txid + ", 1],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        JSONArray data = object.getJSONArray("data");
        byte[] bytes = decodeHex(data.getString(0)).toByteArray();
        String sent_data = new String(bytes);
        JSONArray amount = object.getJSONArray("vout");
        JSONObject value = amount.getJSONObject(0);
        System.out.println(value);
        double sent_amount = (double) value.get("value");
        System.out.println(sent_amount);

        return sent_data; //sent_data, sent_amount;
    }

    public double getFee(String address, String txid) throws IOException, JSONException {

        this.address = "\"" +address+ "\"";
        this.txid = "\"" +txid+ "\"";
        boolean True = true;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getaddresstransaction\",\"params\":[" + this.address + ","+this.txid+","+True+"],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        JSONArray value = object.getJSONArray("vout");
        JSONObject amount = value.getJSONObject(0);
        double sent_amount = amount.getInt("amount");
        JSONObject balance = object.getJSONObject("balance");
        double balance_amount = (double) balance.get("amount");
        double fees = (abs(balance_amount) - sent_amount);

        return fees;
    }
}
