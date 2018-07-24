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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <h1>Assets Class Usage</h1>
 * Assets class is used to call assets related functions like create
 assets and list assets functions which are used on the RecordsKeeeper Blockchain.
 * <p>Library to work with RecordsKeeper assets.</p>
 * You can create new assets and list all assets by using Assets class. You just have to pass parameters to invoke the pre-defined functions.
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

public class Assets{

    private String address;
    private String asset_name;
    private int asset_qty;
    private String issue_id;
    private int issue_qty;
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

    public Assets() throws IOException {

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
     * Create Assets on the RecordsKeeper Blockchain.<br>
     * createAsset() function is used to create or issue an asset.
     * <p><code>createAsset(); <br>
     * txid = createAsset();          //createAsset() function call<br>
     * System.out.println(txid);                   //prints tx id of the issued asset </code></p>
     * You have to pass these two arguments to the createAsset function call:<br>
     * @param address to create asset for that address
     * @param asset_name assigning name to the asset
     * @param asset_qty amount of the asset to be created
     * @return It will return the transaction id of the issued asset.
     */

    public String createAsset(String address, String asset_name, int asset_qty) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

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
        String txid = "";
        if(jsonObject.isNull("result"))
            txid = "Asset or stream with this name already exists.";
        else
            txid = jsonObject.getString("result");

        return txid;
    }

    /**
     * List all assets on the RecordsKeeper Blockchain.<br>
     * retrieveAssets() function is used to list all assets, no of assets, issued quantity and issued transaction id of all the assets on RecordsKeeper Blockchain.
     * <p><code>retrieveAssets();  <br>
     * asset_name, issue_id, issue_qty, asset_count = retrieveAssets()       //retrieveAssets() function call<br>
     * System.out.print(asset_name);       //prints all the addresses of the wallet<br>
     * System.out.print(asset_count);     //prints the address count<br>
     * System.out.print(issue_qty);         //prints assets issued quantity<br>
     * System.out.print(issue_id );         //prints assets issued transaction id</code></p>
     * @return It will return all the assets, the count of the assets, issued quantity of assets and issued transaction id of the asset on the RecordsKeeper Blockchain.
     */

    public JSONObject retrieveAssets() throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

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
        JSONObject item = new JSONObject();
        int asset_count = array.length();
        String output = "";
        for (int i = 0; i<asset_count; i++) {
            JSONObject object = array.getJSONObject(i);
            asset_name = object.getString("name");
            issue_id = object.getString("issuetxid");
            issue_qty = object.getInt("issueraw");
            item.put("asset_name", asset_name);
            item.put("issue_id", issue_id);
            item.put("issue_qty", issue_qty);
            item.put("asset_count", asset_count);
            output = output + item + ",";
        }

        String out = "{\"Output\":["+output+"]}";
        JSONObject object1 = new JSONObject(out);

        return object1;
    }

    /**
     * Send Assets on the RecordsKeeper Blockchain.<br>
     * sendasset() function is used to send an asset.
     * <p><code>sendasset(); <br>
     * txid = sendasset();          //sendasset() function call <br>
     * System.out.println(txid);                   //prints tx id of the sent asset </code></p>
     * You have to pass these two arguments to the sendasset function call:<br>
     * @param address to which the asset is to be sent
     * @param asset_name name for the asset
     * @param asset_qty amount to be sent
     * @return It will return the transaction id of the issued asset.
     */

    public String sendAssets(String address, String asset_name, int asset_qty) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String credential = Credentials.basic(rkuser, passwd);

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
