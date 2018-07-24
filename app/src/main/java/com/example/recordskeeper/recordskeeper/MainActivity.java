package com.example.recordskeeper.recordskeeper;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.print.PrintManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recordskeeper.address.Address;
import com.example.recordskeeper.address.Transactions;
import com.example.recordskeeper.address.Wallet;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity{

    public Button createxrkwallet;
    public Button creatmultisigwallet;
    public Button restorexrkwallet;
    public Button submit;
    public EditText addr;
    public String xrkwltaddress;
    public String xrkwltpubkey;
    public String xrkwltprivkey;
    public String res;
    public String txid;
    public Button createmultisigbutton;
    public TextView xrkaddress;
    public TextView multisigaddress;
    public View nview;
    int count = 3;
    int time = 0;
    double amount = 0;
    public Double fee;
    public Button close;
    public Button closemultisig;
    public String multisigornot;
    public EditText pubkey;
    public EditText pubkey2;
    public String valid;
    public String addressvalid;
    public int k;
    public TextView pk;
    public EditText pubkey1;
    public LinearLayout linearLayout;
    public LinearLayout newlayout;
    public String abc;
    public String[] items;

    Wallet wallet = new Wallet();
    Address address = new Address();
    Transactions transactions = new Transactions();

    public MainActivity() throws IOException{
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.rgb(84, 178, 206));

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        toolbar1.setBackgroundColor(Color.rgb(84, 178, 206));
        Switch myswitch = (Switch) findViewById(R.id.switch1);
        TextView testmain = (TextView) findViewById(R.id.testmain);
        String test = "Test Network";

        if (!myswitch.isChecked()) {
            testmain.setText(test);
        }

        testmain.setTextColor(Color.WHITE);

        createxrkwallet = (Button) findViewById(R.id.buttoncreatexrkwallet);
        creatmultisigwallet = (Button) findViewById(R.id.buttoncreatemultisigwallet);
        restorexrkwallet = (Button) findViewById(R.id.buttonrestorexrkwallet);
        submit = (Button) findViewById(R.id.buttonsubmits);
        addr = (EditText) findViewById(R.id.addresstext);

        createxrkwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
                View mview = getLayoutInflater().inflate(R.layout.createxrkwallet, null);
                final EditText password = mview.findViewById(R.id.password);
                final EditText confirmpassword = mview.findViewById(R.id.confirmpassword);
                Button createxrkwallet1 = mview.findViewById(R.id.buttoncreatexrkwallet1);
                close = mview.findViewById(R.id.buttonclose);

                createxrkwallet1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder sbuilder = new AlertDialog.Builder(MainActivity.this);
                        final View sview = getLayoutInflater().inflate(R.layout.details, null);
                        Button close1 = sview.findViewById(R.id.buttonclose1);
                        xrkaddress = sview.findViewById(R.id.xrkaddress);
                        TextView xrkpublickey = sview.findViewById(R.id.publickey);
                        TextView xrkprivkey = sview.findViewById(R.id.privkey);
                        ImageView xrkwltaddimg = sview.findViewById(R.id.image);
                        ImageView xrkwltpubkeyimg = sview.findViewById(R.id.image1);
                        ImageView xrkwltprivkeyimg = sview.findViewById(R.id.image2);
                        TextView print = sview.findViewById(R.id.print);

                        try {
                            JSONObject object = new JSONObject(String.valueOf(wallet.createWallet()));
                            xrkwltaddress = object.getString("public_address");
                            xrkwltpubkey = object.getString("public_key");
                            xrkwltprivkey = object.getString("private_key");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        xrkaddress.setText(xrkwltaddress);
                        xrkpublickey.setText(xrkwltpubkey);
                        xrkprivkey.setText(xrkwltprivkey);

                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(xrkwltaddress), BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            xrkwltaddimg.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        MultiFormatWriter multiFormatWriter1 = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix1 = multiFormatWriter1.encode(String.valueOf(xrkwltpubkey), BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder1 = new BarcodeEncoder();
                            Bitmap bitmap1 = barcodeEncoder1.createBitmap(bitMatrix1);
                            xrkwltpubkeyimg.setImageBitmap(bitmap1);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        MultiFormatWriter multiFormatWriter2 = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix2 = multiFormatWriter2.encode(String.valueOf(xrkwltprivkey), BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder2 = new BarcodeEncoder();
                            Bitmap bitmap2 = barcodeEncoder2.createBitmap(bitMatrix2);
                            xrkwltprivkeyimg.setImageBitmap(bitmap2);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                       /* PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);

                        // Set job name, which will be displayed in the print queue
                        String jobName = getActivity().getString(R.string.app_name) + " Document";

                        // Start a print job, passing in a PrintDocumentAdapter implementation
                        // to handle the generation of a print document
                        printManager.print(jobName, new MyPrintDocumentAdapter(getActivity()),
                                null); */

                        sbuilder.setView(sview);
                        final AlertDialog alertDialog = sbuilder.create();
                        alertDialog.show();

                        close1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                mbuilder.setView(mview);
                final AlertDialog dialog = mbuilder.create();
                dialog.show();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        addr.setText(xrkwltaddress);
                    }
                });
            }
        });

        creatmultisigwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder nbuilder = new AlertDialog.Builder(MainActivity.this);
                nview = getLayoutInflater().inflate(R.layout.createmultisigwallet, null);
                closemultisig = nview.findViewById(R.id.buttonclosecreatemultisig);
                FloatingActionButton add = nview.findViewById(R.id.add);
                FloatingActionButton delete = nview.findViewById(R.id.delete);
                createmultisigbutton = nview.findViewById(R.id.buttoncreatemultisig);
                pubkey = (EditText) findViewById(R.id.pubkey);
                pubkey2 = (EditText) findViewById(R.id.pubkey2);
                EditText pubkey = nview.findViewById(R.id.pubkey);
                EditText pubkey1 = nview.findViewById(R.id.pubkey2);

                String pk = pubkey.getText().toString();
                String pk1 = pubkey1.getText().toString();
                abc = pk+","+pk1;

                final int[] put = {0};

                items = new String[]{"1", "2", "3"};
                final Spinner spinner = nview.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        put[0] = spinner.getSelectedItemPosition();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                linearLayout = nview.findViewById(R.id.vertical);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newlayout = new LinearLayout(MainActivity.this);
                        newlayout.setOrientation(LinearLayout.HORIZONTAL);

                        newlayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                        linearLayout.addView(newlayout);

                        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        TextView tv = new TextView(MainActivity.this);
                        tv.setText("Public Key " + count);
                        tv.setId(v.generateViewId());
                        tv.setTextColor(Color.DKGRAY);
                        tv.setTextSize(14);
                        tv.setPadding(20, 0, 20, 0);
                        newlayout.addView(tv, lpView);

                        EditText pubk = new EditText(MainActivity.this);
                        pubk.setHint("Enter Public Key " + count);
                        pubk.setHintTextColor(Color.GRAY);
                        newlayout.addView(pubk, lpView);

                        count++;
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newlayout.setVisibility(View.GONE);
                        count--;
                    }
                });

                createmultisigbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder gbuilder = new AlertDialog.Builder(MainActivity.this);
                        View gview = getLayoutInflater().inflate(R.layout.clickcreatemulti, null);
                        Button closemultisigbox = gview.findViewById(R.id.closemultisigbox);
                        ImageView view1 = gview.findViewById(R.id.imagemultisig);
                        multisigaddress = gview.findViewById(R.id.multisigaddress);

                        try {
                            res = address.getMultisigWalletAddress(put[0], abc);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        multisigaddress.setText(res);

                        MultiFormatWriter multiFormatWriter3 = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix3 = multiFormatWriter3.encode(String.valueOf(res), BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder3 = new BarcodeEncoder();
                            Bitmap bitmap3 = barcodeEncoder3.createBitmap(bitMatrix3);
                            view1.setImageBitmap(bitmap3);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        gbuilder.setView(gview);
                        final AlertDialog dialogcreate = gbuilder.create();
                        dialogcreate.show();

                        closemultisigbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogcreate.dismiss();
                            }
                        });
                    }
                });


                nbuilder.setView(nview);
                final AlertDialog dialog1 = nbuilder.create();
                dialog1.show();

                closemultisig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                        addr.setText(res);
                    }
                });
            }
        });

        restorexrkwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder pbuilder = new AlertDialog.Builder(MainActivity.this);
                View pview = getLayoutInflater().inflate(R.layout.restorexrkwallet, null);
                Button closeres = pview.findViewById(R.id.buttoncloserestore);
                Button restorexrkwallet1 = pview.findViewById(R.id.buttonres);

                restorexrkwallet1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                pbuilder.setView(pview);
                final AlertDialog dialog2 = pbuilder.create();
                dialog2.show();

                closeres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String addo = addr.getText().toString();

                    try {
                        valid = address.checkifValid(addo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    addressvalid = "The Address is Valid";

                    if(valid == addressvalid) {

                        AlertDialog.Builder subbuilder = new AlertDialog.Builder(MainActivity.this);
                        View subview = getLayoutInflater().inflate(R.layout.submit, null);
                        TextView balance = subview.findViewById(R.id.balance);
                        TextView pubadd = subview.findViewById(R.id.pubadd);
                        TextView sendersadd = subview.findViewById(R.id.sendersadd);
                        TextView nohistory = subview.findViewById(R.id.nohistory);
                        TextView transactionid = subview.findViewById(R.id.transactionid);
                        TextView transactiontime = subview.findViewById(R.id.transactiontime);
                        TextView transactionamount = subview.findViewById(R.id.transactionamount);
                        Button sendtransaction = subview.findViewById(R.id.buttonsendtransaction);

                        TabHost tabHost = subview.findViewById(R.id.tabhost);
                        tabHost.setup();

                        TabHost.TabSpec spec = tabHost.newTabSpec("Transaction");

                        spec.setIndicator("Transaction");
                        spec.setContent(R.id.tab1);
                        tabHost.addTab(spec);

                        spec = tabHost.newTabSpec("Receive");
                        spec.setContent(R.id.tab2);
                        spec.setIndicator("Receive");
                        tabHost.addTab(spec);

                        spec = tabHost.newTabSpec("Send");
                        spec.setIndicator("Send");
                        spec.setContent(R.id.tab3);
                        tabHost.addTab(spec);

                        subbuilder.setView(subview);
                        final AlertDialog dialogsub = subbuilder.create();
                        dialogsub.show();

                        try {
                            JSONObject object = new JSONObject(String.valueOf(transactions.listaddresstransaction(addo)));
                            JSONArray array = object.getJSONArray("result");
                            if (array.isNull(0)) {
                                String hist = "No Transaction Yet.";
                                nohistory.setText(hist);
                            } else {
                                String r1 = "Transaction ID";
                                transactionid.setText(r1);
                                String r2 = "Time";
                                transactiontime.setText(r2);
                                String r3 = "Transaction Amount";
                                transactionamount.setText(r3);

                                TableLayout tableLayout = subview.findViewById(R.id.layout);

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    JSONObject balanc = object1.getJSONObject("balance");
                                    txid = object1.getString("txid");
                                    time = object1.getInt("time");
                                    amount = balanc.getDouble("amount");
                                    int days = time / (24 * 3600);
                                    int hours = time / 3600;
                                    int mins = (time % 3600) / 60;
                                    TextView id = new TextView(MainActivity.this);
                                    TextView tim = new TextView(MainActivity.this);
                                    TextView am = new TextView(MainActivity.this);
                                    id.setText(txid);
                                    tim.setText(days + " days" + hours + " hours " + mins + " minutes");
                                    am.setText(amount + " XRK");
                                    TableRow row = new TableRow(MainActivity.this);
                                    row.addView(id);
                                    row.addView(tim);
                                    row.addView(am);
                                    tableLayout.addView(row);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        double bal = 0;

                        try {
                            bal = address.checkBalance(addo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        balance.setText(bal + " XRK");

                        pubadd.setText(addo);
                        sendersadd.setText(addo);

                        try {
                            multisigornot = address.listaddresses(addo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (multisigornot != null) {

                            LinearLayout ll = subview.findViewById(R.id.ll);
                            ll.setVisibility(subview.GONE);

                            sendtransaction.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder sendmultibuilder = new AlertDialog.Builder(MainActivity.this);
                                    View sendmultiview = getLayoutInflater().inflate(R.layout.sendtransactionmulti, null);
                                    EditText senderprivatekey = sendmultiview.findViewById(R.id.enterprivatekey);
                                    TextView getfee = sendmultiview.findViewById(R.id.getfee);
                                    Button sign = sendmultiview.findViewById(R.id.sign);
                                    Button cancel = sendmultiview.findViewById(R.id.cancel_action);

                                    try {
                                        JSONObject object = new JSONObject(String.valueOf(transactions.getFee(addo, txid)));
                                        double balance_amount = object.getDouble("balance");
                                        double sent_amount = object.getDouble("amount");
                                        fee = (abs(balance_amount) - sent_amount);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    getfee.setText(fee + " XRK");

                                    sign.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Uri.Builder builder = new Uri.Builder();
                                            builder.scheme("http")
                                                    .authority("wallet.recordskeeper.co")
                                                    .appendPath("signer.php")
                                                    .appendQueryParameter("multisig", txid)
                                                    .appendQueryParameter("redeemScript", txid)
                                                    .appendQueryParameter("txid", txid)
                                                    .appendQueryParameter("vout", txid)
                                                    .appendQueryParameter("getRawTransactionResp", txid);
                                        }
                                    });

                                    sendmultibuilder.setView(sendmultiview);
                                    final AlertDialog dialogsendmulti = sendmultibuilder.create();
                                    dialogsendmulti.show();

                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogsendmulti.dismiss();
                                        }
                                    });
                                }
                            });

                        }else{

                            sendtransaction.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder sendbuilder = new AlertDialog.Builder(MainActivity.this);
                                    View sendview = getLayoutInflater().inflate(R.layout.sendtransactionnorm, null);
                                    TextView senderaddress = sendview.findViewById(R.id.addressforprivkey);
                                    EditText senderprivatekey = sendview.findViewById(R.id.enterprivkey);
                                    TextView getfee = sendview.findViewById(R.id.getfee);
                                    Button sendamount = sendview.findViewById(R.id.sendamount);
                                    Button cancel = sendview.findViewById(R.id.cancel);

                                    senderaddress.setText(addo);

                                    try {
                                        JSONObject object = new JSONObject(String.valueOf(transactions.getFee(addo, txid)));
                                        double balance_amount = object.getDouble("balance");
                                        double sent_amount = object.getDouble("amount");
                                        fee = (abs(balance_amount) - sent_amount);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    getfee.setText(fee + " XRK");

                                    sendamount.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder ssbuilder = new AlertDialog.Builder(MainActivity.this);
                                            View ssview = getLayoutInflater().inflate(R.layout.transactionsent, null);
                                            TextView status = ssview.findViewById(R.id.status);
                                            Button ok = ssview.findViewById(R.id.ok);

                                            ssbuilder.setView(ssview);
                                            final AlertDialog dialogss = ssbuilder.create();
                                            dialogss.show();

                                            ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogss.dismiss();
                                                }
                                            });
                                        }
                                    });

                                    sendbuilder.setView(sendview);
                                    final AlertDialog dialogsend = sendbuilder.create();
                                    dialogsend.show();

                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogsend.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"The Address is not Valid",Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }
}