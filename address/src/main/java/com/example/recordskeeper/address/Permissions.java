package com.example.recordskeeper.address;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <h1>Permissions Class Usage</h1>
 * Permissions class is used to call permissions related functions like grant and revoke permissions for an address functions which are used on the RecordsKeeeper Blockchain.
 * <p>Library to work with RecordsKeeper permissions.</p>
 * You can grant and revoke permissions like connect, send, receive, create, issue, mine, activate, admin by using Assets class. You just have to pass parameters to invoke the pre-defined functions.
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

public class Permissions {

    private String address;
    private String permissions;
    private String resp;
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

    public Permissions() throws IOException {
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
     * Grant Permissions to an address on the RecordsKeeper Blockchain.<br>
     * grantPermission() function is used to grant permissions like connect, send, receive, create, issue, mine, activate, admin to an address on RecordsKeeper Blockchain.
     * <p><code>grantPermission(address, permissions); <br>
     * result = grantPermission(address, permissions);          //grantPermission() function call<br>
     * System.out.println(txid);                  //prints response of the grant permision transaction</code></p>
     * You have to pass these two arguments to the grantPermission function call:
     * @param address to which you have to grant permission
     * @param permissions list of comma-seperated permissions passed as a string
     * @return It will return the transaction id of the permission transaction.
     */

    public String grantPermission(String address, String permissions) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

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

    /**
     * Revoke Permissions to an address on the RecordsKeeper Blockchain.<br>
     * revokePermission() function is used to revoke permissions like connect, send, receive, create, issue, mine, activate, admin to an address on RecordsKeeper Blockchain.
     * <p><code>revokePermission(address, permissions); <br>
     * result = revokePermission(address, permissions);       //revokePermission() function call <br>
     * System.out.println(result);                //prints response of the revoke permision transaction</code></p>
     * You have to pass these two arguments to the revokePermission function call:
     * @param address to which you have to grant permission
     * @param permissions list of comma-seperated permissions passed as a string
     * @return It will return the transaction id of the permission transaction.
     */

    public String revokePermission(String address, String permissions) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

        this.address = "\"" +address+ "\"";
        this.permissions = "\"" +permissions+ "\"";

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"revoke\",\"params\":[" + this.address + "," + this.permissions + "],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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