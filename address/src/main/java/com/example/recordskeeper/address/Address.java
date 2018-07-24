package com.example.recordskeeper.address;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * <h1>Address Class Usage</h1>
 * Address class is used to call address related functions like generate new address, list all addresses and no of
 addresses on the node's wallet, check if given address is valid or not, check if given address has mining permission
 or not and check a particular address balance on the node functions which are used on the RecordsKeeeper Blockchain.
 * <p>Library to work with RecordsKeeper addresses.</p>
 * You can generate new address, check all addresses, check address validity, check address permissions, check address balance
 by using Address class. You just have to pass parameters to invoke the pre-defined functions.
 * <h2>Libraries</h2>
 * Import these java libraries first to get started with the functionality.<br>
 * <p><code>import com.squareup.okhttp.Credentials;<br>
 * import com.squareup.okhttp.MediaType;<br>
 * import com.squareup.okhttp.OkHttpClient;<br>
 * import com.squareup.okhttp.Request;<br>
 * import com.squareup.okhttp.RequestBody;<br>
 * import com.squareup.okhttp.Response;<br>
 * import org.json.JSONArray;<br>
 * import org.json.JSONException;<br>
 * import org.json.JSONObject;<br>
 * import java.io.IOException;</code> </p>
 * <h2>Create Connection</h2>
 * Entry point for accessing any class resources. Import values from config file.
 * <p><code>Config cfg = new Config();<br>
 * String url = cfg.getProperty("url");<br>
 * String chain = cfg.getProperty("chain");</code></p>
 * URL: Url to connect to the chain ([RPC Host]:[RPC Port]) <br>
 * Chain-name: chain name <br>
 * <p>Set the url and chain name value in the config file to change the network-type.</p>
 * <h2>Node Authentication</h2>
 * Import values from config file.
 * <p><code>String rkuser = cfg.getProperty("rkuser");<br>
 * String passwd = cfg.getProperty("passwd");</code></p>
 * User name: The rpc user is used to call the APIs.<br>
 * Password: The rpc password is used to authenticate the APIs.<br>
 * Now we have node authentication credentials.
 */

public class Address{

    public String address;
    public String res;
    public String key;
    public int nrequired;
    public String public_address;
    public String resp;
    public double balance;
    public Properties prop;
    public String url;
    public String rkuser;
    public String passwd;
    public String chain;

    public boolean getPropert() throws IOException {

        prop = new Properties();

        String path = "config.properties";
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fs = new FileInputStream(path);
            prop.load(fs);
            fs.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Default Constructor Class
     */

    public Address() throws IOException{
        if (getPropert() == true) {
            url = prop.getProperty("url");
            rkuser = prop.getProperty("rkuser");
            passwd = prop.getProperty("passwd");
            chain = prop.getProperty("chain");
        } else {
            url = System.getenv("url");
            rkuser = System.getenv("rkuser");
            passwd = System.getenv("passwd");
            chain = System.getenv("chain");
        }
    }

    /**
     * Generate new address on the node's wallet.<br>
     * getAddress() function is used to generate a new wallet address.
     * <p><code>getAddress(); <br>
     * newAddress = getAddress();                 //getAddress() function call <br>
     * System.out.println(newAddress);                  //prints a new address </code></p>
     * @return It will return a new address of the wallet
     */

    public String getAddress() throws IOException, JSONException{

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

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

    /**
     * Generate a new multisignature address.
     * <p><code>getMultisigAddress(nrequired, key); <br>
     * newAddress = getMultisigAddress(nrequired, key);          //getMultisigAddress() function call<br>
     * System.out.println(newAddress);                           //prints a new address </code> </p>
     * You have to pass these two arguments to the getMultisigAddress function call:<br>
     * @param nrequired to pass the no of signatures that are must to sign a transaction
     * @param key  pass any no of comma-seperated public addresses that are to be used with this multisig address as a single variable
     * @return It will return a new multisignature address on RecordsKeeper Blockchain.
     */

    public String getMultisigAddress(int nrequired, String key) throws IOException, JSONException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

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
        if(jsonObject.isNull("result")){
            JSONObject error = jsonObject.getJSONObject("error");
            res = error.getString("message");
        }else{
            JSONObject add = jsonObject.getJSONObject("result");
            res = add.getString("address");
        }
        return res;
    }

    /**
     * Generate a new multisignature address on the node's wallet.
     * <p><code>getMultisigWalletAddress(nrequired, key); <br>
     * newAddress = getMultisigWalletAddress(nrequired, key);          //getMultisigAddress() function call<br>
     * System.out.println(newAddress);                           //prints a new address </code> </p>
     * You have to pass these two arguments to the getMultisigWalletAddress function call:<br>
     * @param nrequired to pass the no of signatures that are must to sign a transaction
     * @param key  pass any no of comma-seperated public addresses that are to be used with this multisig address as a single variable
     * @return It will return a new multisignature address on the wallet.
     */

    public String getMultisigWalletAddress(int nrequired, String key) throws IOException, JSONException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        this.nrequired = nrequired;
        this.key = key;

        String keys;
        String output = "";
        String[] key_list = key.split(",");
        for (int i = 0; i < key_list.length; i++){
            keys = "\"" + key_list[i] + "\"";
            if(i!=key_list.length-1)
                output += keys +",";
            else output += keys;
        }

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"addmultisigaddress\",\"params\":["+this.nrequired+","+Arrays.asList(output)+"], \"id\": 1, \"chain_name\":\""+chain+"\"}\n");
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
        }else {
            res = jsonObject.getString("result");
        }

        return res;
    }

    /**
     * List all addresses and no of addresses on the node's wallet. <br>
     * retrieveAddresses() function is used to list all addresses and no of addresses on the node's wallet.
     * <p><code>retrieveAddresses();<br>
     * allAddresses, address_count = retrieveAddresses();       //retrieveAddresses() function call<br>
     * System.out.println(allAddresses);       //prints all the addresses of the wallet<br>
     * System.out.println(address_count);     //prints the address count</code></p>
     * @return It will return all the addresses and the count of the addresses on the wallet.
     */

    public JSONObject retrieveAddresses() throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getaddresses\",\"params\":[],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\""+chain+"\"}\n");
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
        int address_count = array.length();
        address = String.valueOf(array);

        JSONObject item = new JSONObject();
        item.put("address_count", address_count);
        item.put("addresses", address);

        return item;
    }

    /**
     * Check validity of the address.<br>
     * checkifValid() function is used to check validity of a particular address.
     * <p><code>checkifValid();<br>
     * addressC = checkifValid(address);                //checkifValid() function call<br>
     * System.out.println(addressC);      //prints validity</code></p>
     * You have to pass address as argument to the checkifValid function call:
     * @param address to check the validity
     * @return It will return if an address is valid or not.
     */

    public String checkifValid(String address) throws IOException, JSONException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        this.address = "\""+address+"\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"validateaddress\",\"params\":["+this.address+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\""+chain+"\"}\n");
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
        Boolean valid = jsonObject1.getBoolean("isvalid");
        if (valid) {
            res = "The Address is Valid";
        }
        else
            res = "The Address is not Valid";

        return res;
    }

    /**
     * Check if given address has mining permission or not. <br>
     * checkifMineAllowed() function is used to sign raw transaction by passing transaction hex of the raw transaction and the private key to sign the raw transaction.
     * <p><code>checkifMineAllowed(address);<br>
     * permissionCheck = checkifMineAllowed(address);   //checkifMineAllowed() function call <br>
     * System.out.println(permissionCheck);      //prints permission status of the given address</code></p>
     * You have to pass address as argument to the checkifMineAllowed function call:
     * @param address to check the permission status
     * @return It will return if mining permission is allowed or not.
     */

    public String checkifMineAllowed(String address) throws IOException, JSONException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        this.address = "\""+address+"\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"validateaddress\",\"params\":["+this.address+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\""+chain+"\"}\n");
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
        Boolean permission = jsonObject1.getBoolean("ismine");

        if (permission)
            res = "The Address has mining permission";
        else
            res = "The Address does not have mining permission";

        return res;
    }

    /**
     * Check address balance on a particular node. <br>
     * checkBalance() function is used to check the balance of the address.
     * <p><code>checkBalance(address);<br>
     * address_balance = checkBalance(address);     //checkBalance() function call<br>
     * System.out.println(address_balance);    //prints balance of the address </code></p>
     * You have to pass address as argument to the checkifMineAllowed function call:
     * @param address to check the balance
     * @return It will return the balance of the address on RecordsKeeper Blockchain.
     */

    public double checkBalance(String address) throws IOException, JSONException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getaddressbalances\",\"params\":[\""+address+"\"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\""+chain+"\"}\n");
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
        balance = object.getDouble("qty");

        return balance;
    }

    /**
     * Import a non-wallet address on RecordsKeeeper Blockchain. <br>
     * importAddress() function is used to check the balance of the address.
     * <p><code>importAddress(public_address); <br>
     * response = importAddress(public_address);     //importAddress() function call <br>
     * System.out.println(response);   //prints response</code></p>
     * You have to pass address as argument to the importAddress function call:
     * @param public_address non-wallet address to import on a particular node
     * @return It will return the response of the importAddress() function call.
     */

    public String importAddress(String public_address) throws IOException, JSONException{

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        this.public_address = "\""+public_address+"\"";
        boolean False = false;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"importaddress\",\"params\":["+this.public_address+", \""+null+"\" , "+False+"],\"jsonrpc\": \"2.0\", \"id\": \"curltext\", \"chain_name\":\""+chain+"\"}\n");
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
                res = "Address successfully imported";
            else {
                JSONObject object= jsonObject.getJSONObject("error");
                res = object.getString("message");
            }
        }
        return res;
    }
}
