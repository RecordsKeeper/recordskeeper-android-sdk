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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.lang.Math.abs;
import static okio.ByteString.decodeHex;

/**
 * <h1>Transaction Class Usage</h1>
 * Transaction class is used to call transaction related functions like create raw transaction, sign transaction, send transaction , retrieve transaction and verify transaction functions which are used to create raw transactions, send transactions, sign transactions, retrieve transactions and verify transactions on the RecordsKeeeper Blockchain.
 * <p>Library to work with RecordsKeeper transactions.</p>
 * You can send transaction, create raw transaction, sign raw transaction, send raw transaction, send signed transaction,
 retrieve transaction information and calculate transaction's fees by using transaction class. You just have to pass
 parameters to invoke the pre-defined functions.
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
 * import java.io.IOException;</code></p>
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

public class Transactions {

    private String sender_address;
    private String receiver_address;
    private String data;
    private double amount;
    private String datahex;
    private String txHex;
    private String private_key;
    private String signed_txHex;
    private String txid;
    private String resp;
    private String res;
    public JSONObject item;
    public Double fees;
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

    public Transactions() throws IOException {
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
     * Send Transaction without signing with private key.<br>
     * sendTransaction() function is used to send transaction by passing reciever's address, sender's address and amount.
     * <p><code>sendTransaction(sender_address, receiver_address, amount); <br>
     * txid = sendTransaction(sender_address, receiver_address, amount); <br>
     * System.out.println(txid);                  //prints transaction id of the sent transaction</code></p>
     * You have to pass these three arguments to the sendTransaction function call:
     * @param sender_address Transaction's sender address
     * @param receiver_address Transaction's receiver address
     * @param data hex value of the data
     * @param amount Amount to be sent in transaction
     * @return It will return the transaction id of the raw transaction.
     */

    public String sendTransaction(String sender_address, String receiver_address, String data, double amount) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        byte[] bytes = data.getBytes("UTF-8");
        BigInteger bigInt = new BigInteger(bytes);
        String hexString = bigInt.toString(16);

        this.datahex = "\"" + hexString + "\"";
        this.sender_address = "\"" + sender_address + "\"";
        this.receiver_address = "\"" + receiver_address + "\"";
        this.amount = amount;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createrawsendfrom\",\"params\":[" + this.sender_address + ", {" + this.receiver_address + " :" + this.amount + "}, [" + this.datahex + "],\"send\"],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    /**
     * Create raw transaction. <br>
     * createRawTransaction() function is used to create raw transaction by passing reciever's address, sender's address and amount.
     * <p><code> createRawTransaction(sender_address, reciever_address, amount); <br>
     * tx_hex = createRawTransaction(sender_address, reciever_address, amount);<br>
     * System.out.println(tx_hex);      //prints transaction hex of the raw transaction</code></p>
     * You have to pass these three arguments to the createRawTransaction function call:
     * @param sender_address Transaction's sender address
     * @param receiver_address Transaction's receiver address
     * @param amount Amount to be sent in transaction
     * @param data hex value of data
     * @return It will return transaction hex of the raw transaction.
     */

    public String createRawTransaction(String sender_address, String receiver_address, double amount, String data) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        byte[] bytes = data.getBytes("UTF-8");
        BigInteger bigInt = new BigInteger(bytes);
        String hexString = bigInt.toString(16);

        this.sender_address = "\"" + sender_address + "\"";
        this.receiver_address = "\"" + receiver_address + "\"";
        this.amount = amount;
        this.datahex = "\"" + hexString + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createrawsendfrom\",\"params\":[" + this.sender_address + ", {" + this.receiver_address + " :" + this.amount + "}, [" + this.datahex + "],\"\"],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    /**
     * Sign raw transaction. <br>
     * signRawTransaction() function is used to sign raw transaction by passing transaction hex of the raw transaction and the private key to sign the raw transaction.
     * <p><code>  signRawTransaction(tx_hex, private_key); <br>
     * signed_hex = signRawTransaction(txHex, private_key); <br>
     * System.out.println(signed_hex);      //prints signed transaction hex of the raw transaction</code></p>
     * You have to pass these three arguments to the signRawTransaction function call:
     * @param txHex Transaction hex of the raw transaction
     * @param private_key Private key to sign raw transaction
     * @return It will return signed transaction hex of the raw transaction.
     */

    public String signRawTransaction(String txHex, String private_key) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        this.txHex = "\"" + txHex + "\"";
        this.private_key = "\"" + private_key + "\"";
        List<String> output = Arrays.asList(this.private_key);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"signrawtransaction\",\"params\":[" + this.txHex + ",[]," + output + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    /**
     * Send raw transaction. <br>
     * sendRawTransaction() function is used to send raw transaction by passing signed transaction hex of the raw transaction.
     * <p><code> sendRawTransaction(signed_txHex); <br>
     * tx_id = sendRawTransaction(signed_txHex); <br>
     * System.out.println(tx_id);     //prints transaction id of the raw transaction</code></p>
     * You have to pass these three arguments to the sendRawTransaction function call:
     * @param signed_txHex Signed transaction hex of the raw transaction
     * @return It will return transaction id of the raw transaction sent on to the Blockchain.
     */

    public String sendRawTransaction(String signed_txHex) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        this.signed_txHex = "\"" + signed_txHex + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"sendrawtransaction\",\"params\":[" + this.signed_txHex + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        } else {
            txid = jsonObject.getString("result");
        }
        return txid;
    }

    /**
     * Send Transaction by signing with private key.<br>
     * sendSignedTransaction() function is used to send transaction by passing reciever's address, sender's address, private key of sender and amount. In this function private key is required to sign transaction.
     * <p><code>sendSignedTransaction(); <br>
     * transaction_id = sendSignedTransaction(); <br>
     * System.out.println(transaction_id);        //prints transaction id of the signed transaction</code></p>
     * You have to pass these four arguments to the sendSignedTransaction function call:
     * @param sender_address Transaction's sender address
     * @param receiver_address Transaction's receiver address
     * @param amount Amount to be sent in transaction
     * @param private_key Private key of the sender's address
     * @param data hex value of the data
     * @return It will return transaction id of the signed transaction.
     */

    public String sendSignedTransaction(String sender_address, String receiver_address, double amount, String private_key, String data) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        byte[] bytes = data.getBytes("UTF-8");
        BigInteger bigInt = new BigInteger(bytes);
        String hexString = bigInt.toString(16);

        this.sender_address = "\"" + sender_address + "\"";
        this.receiver_address = "\"" + receiver_address + "\"";
        this.amount = amount;
        this.private_key = "\"" + private_key + "\"";
        this.datahex = "\"" + hexString + "\"";
        List<String> output = Arrays.asList(this.private_key);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createrawsendfrom\",\"params\":[" + this.sender_address + ", {" + this.receiver_address + " :" + this.amount + "}, [" + this.datahex + "],\"\"],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

        RequestBody body1 = RequestBody.create(mediaType, "{\"method\":\"signrawtransaction\",\"params\":[\"" + txhexi + "\",[]," + output + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

        RequestBody body2 = RequestBody.create(mediaType, "{\"method\":\"sendrawtransaction\",\"params\":[\"" + signedHexi + "\"],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        } else {
            txid = jsonObject2.getString("result");
        }
        return txid;
    }

    /**
     * Retrieve a transaction from the Blockchain. <br>
     * retrieveTransaction() function is used to retrieve transaction's information by passing transaction id to the function.
     * <p><code> retrieveTransaction(tx_id); <br>
     * sent_data, sent_amount, reciever_address = retrieveTransaction(tx_id); <br>
     * System.out.println(sent_data);              //prints sent data <br>
     * System.out.println(sent_amount);                //prints sent amount <br>
     * System.out.println(receiver_address);            //prints receiver's address  </code></p>
     * You have to pass given argument to the retrieveTransaction function call:
     * @param txid Transaction id of the transaction you want to retrieve
     * @return It will return the sent data, sent amount and reciever's address of the retrieved transaction.
     */

    public JSONObject retrieveTransaction(String txid) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        this.txid = "\"" + txid + "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getrawtransaction\",\"params\":[" + this.txid + ", 1],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        double sent_amount = value.getDouble("value");

        JSONObject item = new JSONObject();
        item.put("sent_data", sent_data);
        item.put("sent_amount", sent_amount);

        return item;
    }

    /**
     * Calculate a particular transaction's fee on RecordsKeeper Blockchain. <br>
     * getFee() function is used to calculate transaction's fee by passing transaction id and sender's address to the function.
     * <p><code> getFee(address, tx_id);
     * Fees = getFee(address, tx_id);
     * System.out.println(Fees);                   //prints fees consumed in the verified transaction</code></p>
     * You have to pass these two arguments to the getFee function call:
     * @param addr Sender's address
     * @param txid Transaction id of the transaction you want to calculate fee for
     * @return It will return the fees consumed in the transaction.
     */

    public Double getFee(String addr, String txid) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        boolean True = true;

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getaddresstransaction\",\"params\":[\"" + addr + "\",\"" + txid + "\","+True+"],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        double balance_amount = balance.getDouble("amount");
        fees = (abs(balance_amount) - sent_amount);

        return fees;
    }

}
