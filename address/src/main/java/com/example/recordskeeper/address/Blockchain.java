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

/**
 * <h1>Blockchain Class Usage</h1>
 * Blockchain class is used to call blockchain related functions like retrieving blockchain parameters, retrieving node's
 information, retrieving mempool's information, retrieving node's permissions and check node's balance functions which are used on the RecordsKeeeper Blockchain.
 * <p>Library to work with Blockchain in RecordsKeeper Blockchain.</p>
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

public class Blockchain {

    private String permission;
    private String resp;

    Config cfg = new Config();

    String url = cfg.getProperty("url");
    String rkuser = cfg.getProperty("rkuser");
    String passwd = cfg.getProperty("passwd");
    String chain = cfg.getProperty("chain");

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String credential = Credentials.basic(rkuser, passwd);

    /**
     * Default Constructor Class
     */

    public Blockchain() throws IOException {}

    /**
     * Retrieve Blockchain parameters of RecordsKeeper Blockchain.<br>
     * getChainInfo() function is used to retrieve Blockchain parameters.
     * <p><code>getChainInfo(); <br>
     * chain_protocol, chain_description, root_stream, max_blocksize, default_networkport, default_rpcport, mining_diversity, chain_name = getChainInfo() //getChainInfo() function call <br>
     * System.out.println(chain_protocol); //prints blockchain's protocol <br>
     * System.out.println(chain_description); //prints blockchain's description <br>
     * System.out.println(root_stream); //prints blockchain's root stream <br>
     * System.out.println(max_blocksize); //prints blockchain's maximum block size <br>
     * System.out.println(default_networkport); //prints blockchain's default network port <br>
     * System.out.println(default_rpcport); //prints blockchain's default rpc port <br>
     * System.out.println(mining_diversity); //prints blockchain's mining diversity <br>
     * System.out.println(chain_name); //prints blockchain's name</code></p>
     * @return It will return the information about RecordsKeeper blockchain's parameters.
     */

    public JSONObject getChainInfo() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getblockchainparams\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        String chain_protocol = object.getString("chain-protocol");
        String chain_name = object.getString("chain-name");
        String chain_description = object.getString("chain-description");
        String root_stream = object.getString("root-stream-name");
        int max_blocksize = object.getInt("maximum-block-size");
        int default_networkport = object.getInt("default-network-port");
        int default_rpcport = object.getInt("default-rpc-port");
        int mining_diversity =  object.getInt("mining-diversity");

        JSONObject item = new JSONObject();
        item.put("chain_name", chain_name);
        item.put("chain_protocol", chain_protocol);
        item.put("chain_description", chain_description);
        item.put("root_stream_name", root_stream);
        item.put("max_block_size", max_blocksize);
        item.put("default_networkport", default_networkport);
        item.put("default_rpcport", default_rpcport);
        item.put("mining_diversity", mining_diversity);

        return item;
    }

    /**
     * Retrieve node's information on RecordsKeeper Blockchain. <br>
     * getNodeInfo() function is used to retrieve node's information on RecordsKeeper Blockchain.
     * <p><code>getNodeInfo(); <br>
     * node_balance, synced_blocks, node_address, difficulty = getNodeInfo();       #getNodeInfo() function call <br>
     * System.out.println(node_balance); //prints balance of the node<br>
     * System.out.println(synced_blocks); //prints no of synced blocks<br>
     * System.out.println(node_address); //prints node's address<br>
     * System.out.println(difficulty); //prints node's difficulty </code></p>
     * @return It will return node's balance, no of synced blocks, node's address and node's difficulty.
     */

    public JSONObject getNodeInfo() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getinfo\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .header("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        resp = response.body().string();
        System.out.println(resp);
        JSONObject jsonObject = new JSONObject(resp);
        JSONObject object = jsonObject.getJSONObject("result");
        int node_balance = object.getInt("balance");
        int synced_blocks = object.getInt("blocks");
        String node_address = object.getString("nodeaddress");
        double difficulty = object.getDouble("difficulty");

        JSONObject item = new JSONObject();
        item.put("balance", node_balance);
        item.put("blocks", synced_blocks);
        item.put("node_address", node_address);
        item.put("difficulty", difficulty);

        return item;
    }

    /**
     * Retrieve permissions given to the node on RecordsKeeper Blockchain.<br>
     * permissions() function is used to retrieve node's permissions.
     * <p><code> permissions();<br>
     * allowed_permissions = permissions();                //permissions() function call<br>
     * System.out.print(allowed_permissions);      //prints permissions available to the node</code></p>
     * @return It will return the permissions available to the node.
     */

    public String permissions() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"listpermissions\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        int pms_count = array.length();
        String output = "";
        for (int i = 0; i<pms_count; i++) {
            permission = array.getJSONObject(i).getString("type");
            if(i != pms_count - 1)
                output += permission +",";
            else output += permission;
        }
        return output;
    }

    /**
     * Retrieve pending transaction's information on RecordsKeeper Blockchain. <br>
     * getpendingTransactions() function is used to retrieve pending transaction's information like no of pending transactions and the pending transactions.
     * <p><code>getpendingTransactions();<br>
     * pendingtx, pendingtxcount = getpendingTransactions(address);   //getpendingTransactions() function call<br>
     * System.out.println(pendingtx);             //prints pending transactions<br>
     * System.out.println(pendingtxcount);        //prints pending transaction count</code></p>
     * @return It will return the information of pending transactions on Recordskeeper Blockchain.
     */

    public JSONObject getpendingTransactions() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getmempoolinfo\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        double tx_count = object.getDouble("size");

        String tx = "";

        if(tx_count>0) {

            RequestBody body1 = RequestBody.create(mediaType, "{\"method\":\"getrawmempool\",\"params\":[],\"id\":1,\"chain_name\":\"" + chain + "\"}\n");
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
            JSONArray array = jsonObject1.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                tx = String.valueOf(jsonObject1.get("result"));
            }
        }

            JSONObject item = new JSONObject();
            item.put("tx_count", tx_count);
            item.put("tx", tx);

            return item;
    }

    /**
     * Check node's total balance. <br>
     * checkNodeBalance() function is used to check the total balance of the node.
     * <p><code>checkNodeBalance();<br>
     * node_balance = checkNodeBalance();     //checkNodeBalance() function call <br>
     * System.out.println(node_balance);    //prints total balance of the node</code></p>
     * @return It will return the total balance of the node on RecordsKeeper Blockchain.
     */

    public double checkNodeBalance() throws IOException, JSONException {

        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getmultibalances\",\"params\":[],\"id\":1,\"chain_name\":\""+chain+"\"}\n");
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
        JSONArray object1 = object.getJSONArray("total");
        JSONObject object2 = object1.getJSONObject(0);
        double qty = object2.getDouble("qty");

        return qty;
    }
}
