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
import java.util.Properties;

/**
 * <h1>Wallet Class Usage</h1>
 * Wallet class is used to call wallet related functions like create wallet, retrieve private key of wallet address, retrieve wallet's information, dump wallet, lock wallet, unlock wallet, change wallet's password, create wallet's backup, import wallet's backup, sign message and verify message functions on RecordsKeeeper Blockchain.
 * <p>Library to work with RecordsKeeper wallet functionalities.</p>
 * You can create wallet, dump wallet into a file, backup wallet into a file, import wallet from a file, lock wallet, unlock wallet, change wallet's password, retrieve private key, retrieve wallet's information, sign and verify message by using wallet class. You just have to pass parameters to invoke the pre-defined functions.
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

public class Wallet {

    public String filename;
    public String public_address;
    public String password;
    public int unlock_time;
    public String old_password;
    public String new_password;
    public String private_key;
    public String public_key;
    public String message;
    public String address;
    public String signedMessage;
    public String resp;
    public JSONObject item;
    public String resp1;
    public String public_addr;
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

    public Wallet() throws IOException {
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
     *  Create wallet on RecordsKeeper blockchain. <br>
     *  createWallet() function is used to create wallet on RecordsKeeper blockchain.
     *  <p><code>createWallet();
     *  publicaddress, privatekey, publickey = createWallet();
     *  System.out.println(publicaddress);  //prints public address of the wallet
     *  System.out.println(privatekey);  //prints private key of the wallet
     *  System.out.println(publickey);  //prints public key of the wallet</code></p>
     * @return It will return the public address, public key and private key.
     */

    public JSONObject createWallet() throws IOException, JSONException {

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"createkeypairs\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        public_address = object.getString("address");
        public_key = object.getString("pubkey");
        private_key = object.getString("privkey");
        item = new JSONObject();
        item.put("public_address", public_address);
        item.put("public_key", public_key);
        item.put("private_key", private_key);


        boolean False = false;

        RequestBody body1 = RequestBody.create(mediaType, "{\"method\":\"importaddress\",\"params\":[\"" + public_address + "\",\"\"," + False + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
        Request request1 = new Request.Builder()
                .url(url)
                .method("POST", body1)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response1 = client.newCall(request1).execute();
        resp1 = response1.body().string();

        return item;
    }

    /**
     * Retrieve private key of an address.
     * <p><code> getPrivateKey(); <br>
     * privkey = getPrivateKey(); <br>
     * System.out.println(privkey);        //prints private key of the given address</code></p>
     * You have to pass address argument to the getPrivateKey function call:
     * @param public_address address whose private key is to be retrieved
     * @return It will return private key of the given address.
     */

    public String getPrivateKey(String public_address) throws IOException, JSONException {

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"dumpprivkey\",\"params\":[\"" + public_address + "\"],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    /**
     * Retrieve node wallet's information.<br>
     * retrieveWalletinfo() function is used to retrieve node wallet's information.
     * <p><code>retrieveWalletinfo(); <br>
     * balance, tx_count, unspent_tx = retrieveWalletinfo(); <br>
     * System.out.println(balance); //prints wallet's balance <br>
     * System.out.println(tx_count); //prints wallet transaction count  <br>
     * System.out.println(unspent_tx); //prints unspent wallet transactions</code></p>
     * @return It will return wallet's balance, transaction count and unspent transactions.
     */

    public JSONObject retrieveWalletinfo() throws IOException, JSONException {

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getwalletinfo\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

        JSONObject item = new JSONObject();
        item.put("balance", balance);
        item.put("tx_count", tx_count);
        item.put("unspent_tx", unspent_tx);

        return item;
    }

  /*  *//**
     * Create wallet's backup.<br>
     * backupWallet() function is used to create backup of the wallet.dat file.
     * <p><code>backupWallet(filename); <br>
     * result = backupWallet(filename); <br>
     * System.out.println(result);      //prints result</code></p>
     * You have to pass these three arguments to the backupWallet function call:
     * @param filename wallet's backup file name
     * @return It will return the response of the backup wallet function. The backup of the wallet is created in your chain's directory and you can simply access your file by using same filename that you have passed with the backupwallet function. Creates a backup of the wallet.dat file in which the node’s private keys and watch-only addresses are stored. The backup is created in file filename. Use with caution – any node with access to this file can perform any action restricted to this node’s addresses.
     *//*

    public String backupWallet(String filename) throws IOException, JSONException {

        this.filename = "\"" + filename + "\"";

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"backupwallet\",\"params\":[" + this.filename + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    *//**
     * Import backup wallet.<br>
     * importWallet() function is used to import wallet's backup file.
     * <p><code>importWallet(filename); <br>
     * result = importWallet(filename); <br>
     * System.out.println(result);    //prints result</code></p>
     * You have to pass these three arguments to the importWallet function call:
     * @param filename wallet's backup file name
     * @return It will return the response of the import wallet function. It will import the entire set of private keys which were dumped (using dumpwallet) into file filename.
     *//*

    public String importWallet(String filename) throws IOException, JSONException {

        this.filename = "\"" + filename + "\"";

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"importwallet\",\"params\":[" + this.filename + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    *//**
     * Dump wallet on RecordsKeeper blockchain.<br>
     * dumpWallet() function is used to retrieve transaction's information by passing transaction id to the function.
     * <p><code>dumpWallet(filename); <br>
     * result = dumpWallet(filename);  <br>
     * System.out.println(result);                   //prints result</code></p>
     * You have to pass these three arguments to the dumpWallet function call:
     * @param filename file name to dump wallet in
     * @return It will return the response of the dump wallet function. Dumps the entire set of private keys in the wallet into a human-readable text format in file filename. Use with caution – any node with access to this file can perform any action restricted to this node’s addresses.
     *//*

    public String dumpWallet(String filename) throws IOException, JSONException {

        this.filename = "\"" + filename + "\"";

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"dumpwallet\",\"params\":[" + this.filename + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    *//**
     * Locking wallet with a password on RecordsKeeper Blockchain.<br>
     * lockWallet() function is used to verify transaction's information by passing transaction id and sender's address to the function.
     * <p><code>  lockWallet(password); <br>
     * result = lockWallet(password);  <br>
     * System.out.println(result);                    //prints result</code></p>
     * You have to pass password as an argument to the lockWallet function call:
     * @param password password to lock the wallet
     * @return It will return the the response of the lock wallet function. This encrypts the node’s wallet for the first time, using passphrase as the password for unlocking. Once encryption is complete, the wallet’s private keys can no longer be retrieved directly from the wallet.dat file on disk, and chain will stop and need to be restarted. Use with caution – once a wallet has been encrypted it cannot be permanently unencrypted, and must be unlocked for signing transactions with the unlockwallet function.
     *//*

    public String lockWallet(String password) throws IOException, JSONException {

        this.password = "\"" + password + "\"";

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"dumpwallet\",\"params\":[" + this.password + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    *//**
     * Unlocking wallet with the password on RecordsKeeper Blockchain.<br>
     * unlockWallet() function is used to verify transaction's information by passing transaction id and sender's address to the function.
     * <p><code>unlockWallet(password, unlock_time); <br>
     * result = unlockWallet(password, unlock_time); <br>
     * System.out.println(result);                    //prints result</code></p>
     * You have to pass these two arguments to the unlockWallet function call:
     * @param password password to unlock the wallet
     * @param unlock_time seconds for which wallet remains unlock
     * @return It will return the response of the unlock wallet function. This uses passphrase to unlock the node’s wallet for signing transactions for the next timeout seconds. This will also need to be called before the node can connect to other nodes or sign blocks that it has mined.
     *//*

    public String unlockWallet(String password, int unlock_time) throws IOException, JSONException {

        this.password = "\"" + password + "\"";
        this.unlock_time = unlock_time;

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"walletpassphrase\",\"params\":[" + this.password + "," + this.unlock_time + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    *//**
     * Change wallet's password. <br>
     * changeWalletPassword() function is used to change wallet's password and set new password.
     * <p><code>changeWalletPassword(old_password, new_password); <br>
     * result = changeWalletPassword(password, new_password); <br>
     * System.out.println(result);                    //prints result</code></p>
     * You have to pass these two arguments to the changeWalletPassword function call:
     * @param old_password old password of the wallet
     * @param new_password new password of the wallet
     * @return This changes the wallet’s password from old-password to new-password.
     *//*

    public String changeWalletPassword(String old_password, String new_password) throws IOException, JSONException {

        this.old_password = "\"" + old_password + "\"";
        this.new_password = "\"" + new_password + "\"";

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"walletpassphrasechange\",\"params\":[" + this.old_password + "," + this.new_password + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
*/
    /**
     * Sign Message on RecordsKeeper Blockchain.<br>
     * signMessage() function is used to change wallet's password and set new password.
     * <p><code>signMessage(private_key, message);  <br>
     * signedMessage = signMessage(priavte_key, message);    <br>
     * System.out.println(signedMessage);                 //prints signed message</code></p>
     * You have to pass these two arguments to the signMessage function call:
     * @param private_key private key of the sender's wallet address
     * @param message message to send
     * @return It will return the signed message.
     */

    public String signMessage(String private_key, String message) throws IOException, JSONException {

        this.private_key = "\"" + private_key + "\"";
        this.message = "\"" + message + "\"";

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"signmessage\",\"params\":[" + this.private_key + "," + this.message + "],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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

    /**
     * Verify Message on RecordsKeeper Blockchain.<br>
     * verifyMessage() function is used to change wallet's password and set new password.
     * <p><code>verifyMessage(address, signedMessage, message);
     * validity = verifyMessage(address, signedMessage, message);
     * System.out.println(validity);                 //prints validity of the message</code></p>
     * You have to pass these three arguments to the verifyMessage function call:
     * @param address address to be verified
     * @param signedMessage signed message
     * @param message message to send
     * @return It will return the validity of the message.
     */

    public String verifyMessage(String address, String signedMessage, String message) throws IOException, JSONException {

        this.address = "\"" +address+ "\"";
        this.signedMessage = "\"" +signedMessage+ "\"";
        this.message = "\"" +message+ "\"";

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"verifymessage\",\"params\":[" + this.address+ "," + this.signedMessage + ","+this.message+"],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
