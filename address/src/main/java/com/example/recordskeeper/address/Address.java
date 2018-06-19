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

public class Address{

    private String address;
    private String res;
    private String key;
    private int nrequired;
    private String public_address;
    private String resp;

    Config cfg = new Config();

    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    public Address() throws IOException {}

    public String getAddress() throws IOException, JSONException{

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getnewaddress\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        address = jsonObject.getString("result");
        return address;
    }

    public String getMultisigAddress(int nrequired, String key) throws IOException, JSONException{

        this.nrequired = nrequired;
        this.key = key;

        String keys = "";
        String output = "";
        String[] key_list = key.split(",");
        for (int i = 0; i < key_list.length; i++){
            keys = "\"" + key_list[i] + "\"";
            if(i!=key_list.length-1)
                output += keys +",";
            else output += keys;
        }

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createmultisig\",\"params\":["+this.nrequired+","+Arrays.asList(output)+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\""+chain+"\"}\n");
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
        JSONObject add = jsonObject.getJSONObject("result");
        if(add.isNull("address")){
            JSONObject error = jsonObject.getJSONObject("error");
            res = error.getString("message");
        }else{
            res = add.getString("address");
        }
        return res;
    }

    public String getMultisigWalletAddress(int nrequired, String key) throws IOException, JSONException{

        this.nrequired = nrequired;
        this.key = key;

        String keys = "";
        String output = "";
        String[] key_list = key.split(",");
        for (int i = 0; i < key_list.length; i++){
            keys = "\"" + key_list[i] + "\"";
            if(i!=key_list.length-1)
                output += keys +",";
            else output += keys;
        }

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"addmultisigaddress\",\"params\":["+this.nrequired+","+Arrays.asList(output)+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\""+chain+"\"}\n");
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
        if(jsonObject.isNull("result")){
            JSONObject error = jsonObject.getJSONObject("error");
            res = error.getString("message");
            System.out.println(res);
        }else {
            res = jsonObject.getString("result");
        }

        return res;
    }

    public String retrieveAddress() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getaddresses\",\"params\":[],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\"" + chain + "\"}\n");
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
        System.out.println(array.length());
        for(int i=0; i<array.length();i++) {
            String add = array.getString(i);
            System.out.println(add);
        }
        address = String.valueOf(array);

        return address;
    }

    public String checkifValid(String address) throws IOException, JSONException{

        this.address = "\""+address+"\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"validateaddress\",\"params\":["+this.address+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\"" + chain + "\"}\n");
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
        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
        String add = jsonObject1.getString("address");
        Boolean valid = jsonObject1.getBoolean("isvalid");
        if (valid) {
            System.out.println(valid + ": The Address is Valid");
        }
        else
            System.out.println("The Address is not Valid");

        return add;
    }

    public String checkifMineAllowed(String address) throws IOException, JSONException{

        this.address = "\""+address+"\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"validateaddress\",\"params\":["+this.address+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\"" + chain + "\"}\n");
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
        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
        String add = jsonObject1.getString("address");
        Boolean permission = jsonObject1.getBoolean("ismine");

        if (permission)
            System.out.println("The Address has mining permission");
        else
            System.out.println("The Address does not have mining permission");

        return add;
    }

    public int checkBalance(String address) throws IOException, JSONException{

        this.address = "\""+address+"\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getaddressbalances\",\"params\":["+this.address+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\"" + chain + "\"}\n");
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
        int balance = object.getInt("qty");

        return balance;
    }

    public String importAddress(String public_address) throws IOException, JSONException{
        this.public_address = "\""+public_address+"\"";
        boolean False = false;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"importaddress\",\"params\":["+this.public_address+", \""+null+"\" , "+False+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\"" + chain + "\"}\n");
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
        if (jsonObject.isNull("result")){
            if(jsonObject.isNull("error"))
                System.out.println("Address successfully imported");
            else {
                JSONObject object= jsonObject.getJSONObject("error");
                String objectString = object.getString("message");
                System.out.println(objectString);
            }
        }
        return public_address;
    }
}
