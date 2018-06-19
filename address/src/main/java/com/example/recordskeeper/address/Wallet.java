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

public class Wallet {

    private String public_address;
    private String filename;
    private String password;
    private int unlock_time;
    private String old_password;
    private String new_password;
    private String private_key;
    private String message;
    private String address;
    private String signedMessage;
    private String resp;

    Config cfg = new Config();
    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    public Wallet() throws IOException {}

    public String createWallet() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createkeypairs\",\"params\":[],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        JSONObject object = array.getJSONObject(0);
        String public_address = object.getString("address");
        String private_key = object.getString("privkey");
        String public_key = object.getString("pubkey");

        this.public_address = "\"" + public_address + "\"";

        boolean False = false;

        RequestBody body1 = RequestBody.create(mediaType, "{\"method\":\"importaddress\",\"params\":[" + this.public_address + ",\"\"," + False + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        if (jsonObject1.isNull("result")) {
            System.out.println("Imported Successfully");
        } else {
            System.out.println(jsonObject1.getString("result"));
        }
        return public_address;//public_address,private_key, public_key;
    }

    public String getPrivateKey(String public_address) throws IOException, JSONException {

        this.public_address = "\"" + public_address + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"dumpprivkey\",\"params\":[" + this.public_address + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String private_key = "";
        if (jsonObject.isNull("result")) {
            private_key = jsonObject.getString("error");
        } else {
            private_key = jsonObject.getString("result");
        }
        return private_key;
    }

    public int retrieveWalletinfo() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getwalletinfo\",\"params\":[],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        int balance = object.getInt("balance");
        int tx_count = object.getInt("txcount");
        int unspent_tx = object.getInt("utxocount");

        return balance;
    }

    public String backupWallet(String filename) throws IOException, JSONException {

        this.filename = "\"" + filename + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"backupwallet\",\"params\":[" + this.filename + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String message = "";

        if (jsonObject.isNull("result")) {
            message = "Backup Successful";
        } else {
            JSONArray array = jsonObject.getJSONArray("error");
            JSONObject object = array.getJSONObject(0);
            message = object.getString("message");
        }
        return message;
    }

    public String importWallet(String filename) throws IOException, JSONException {

        this.filename = "\"" + filename + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"importwallet\",\"params\":[" + this.filename + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        String result = "";
        if (jsonObject.isNull("result"))
            result = "Wallet is successfully imported";
        else {
            JSONArray array = jsonObject.getJSONArray("error");
            JSONObject object = array.getJSONObject(0);
            result = object.getString("message");
        }
        return result;
    }

    public String dumpWallet(String filename) throws IOException, JSONException {

        this.filename = "\"" + filename + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"dumpwallet\",\"params\":[" + this.filename + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        resp = response.body().string();
        JSONObject object = new JSONObject(resp);
        String res = "";
        if (object.isNull("result"))
            res = "Wallet is successfully dumped";
        else {
            JSONArray array = object.getJSONArray("error");
            JSONObject jsonObject = array.getJSONObject(0);
            res = jsonObject.getString("message");
        }
        return res;
    }

    public String lockWallet(String password) throws IOException, JSONException {

        this.password = "\"" + password + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"dumpwallet\",\"params\":[" + this.password + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        resp = response.body().string();
        JSONObject object = new JSONObject(resp);
        String res = "";
        if (object.isNull("result")) {
            res = "Wallet is successfully encrypted.";
        } else {
            JSONArray array = object.getJSONArray("error");
            JSONObject jsonObject = array.getJSONObject(0);
            res = jsonObject.getString("message");
        }
        return res;
    }

    public String unlockWallet(String password, int unlock_time) throws IOException, JSONException {

        this.password = "\"" + password + "\"";
        this.unlock_time = unlock_time;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"walletpassphrase\",\"params\":[" + this.password + "," + this.unlock_time + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        resp = response.body().string();
        JSONObject object = new JSONObject(resp);
        String res = "";
        if (object.isNull("result")) {
            res = "Wallet is successfully encrypted.";
        } else {
            JSONArray array = object.getJSONArray("error");
            JSONObject jsonObject = array.getJSONObject(0);
            res = jsonObject.getString("message");
        }
        return res;
    }

    public String changeWalletPassword(String old_password, String new_password) throws IOException, JSONException {

        this.old_password = "\"" + old_password + "\"";
        this.new_password = "\"" + new_password + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"walletpassphrasechange\",\"params\":[" + this.old_password + "," + this.new_password + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        resp = response.body().string();
        JSONObject object = new JSONObject(resp);
        String res = "";

        if (object.isNull("result")) {
            res = "Password successfully changed!";
        } else {
            JSONArray array = object.getJSONArray("error");
            JSONObject jsonObject = array.getJSONObject(0);
            res = jsonObject.getString("message");
        }
        return res;
    }

    public String signMessage(String private_key, String message) throws IOException, JSONException {

        this.private_key = "\"" + private_key + "\"";
        this.message = "\"" + message + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"signmessage\",\"params\":[" + this.private_key + "," + this.message + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        resp = response.body().string();
        JSONObject object = new JSONObject(resp);
        signedMessage = object.getString("result");

        return signedMessage;
    }

    public String verifyMessage(String address, String signedMessage, String message) throws IOException, JSONException {

        this.address = "\"" +address+ "\"";
        this.signedMessage = "\"" +signedMessage+ "\"";
        this.message = "\"" +message+ "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"verifymessage\",\"params\":[" + this.address+ "," + this.signedMessage + ","+this.message+"],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
        boolean verifiedMessage = jsonObject.getBoolean("result");
        String validity = "";
        if(verifiedMessage == true)
            validity = "Yes, message is verified";
        else
            validity = "No, signedMessage is not correct";

        return validity;
    }
}