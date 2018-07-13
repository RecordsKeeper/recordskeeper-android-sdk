package com.example.recordskeeper.recordskeeper;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.TestLooperManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.recordskeeper.address.Address;
import com.example.recordskeeper.address.Transactions;
import com.example.recordskeeper.address.Wallet;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    public Button createxrkwallet;
    public Button creatmultisigwallet;
    public Button restorexrkwallet;
    public Button submit;
    public EditText addr;
    public String xrkwltaddress;
    public String xrkwltpubkey;
    public String xrkwltprivkey;
    public String txid;
    public int time;
    public int amount;
    public JSONObject object;
    public Button createmultisigbutton;

    Wallet wallet = new Wallet();
    Address address = new Address();
    Transactions transactions = new Transactions();

    public MainActivity() throws IOException{
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Button close = mview.findViewById(R.id.buttonclose);

                createxrkwallet1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder sbuilder = new AlertDialog.Builder(MainActivity.this);
                        final View sview = getLayoutInflater().inflate(R.layout.details, null);
                        Button close1 = sview.findViewById(R.id.buttonclose1);
                        TextView xrkaddress = sview.findViewById(R.id.xrkaddress);
                        TextView xrkpublickey = sview.findViewById(R.id.publickey);
                        TextView xrkprivkey = sview.findViewById(R.id.privkey);
                        ImageView xrkwltaddimg = sview.findViewById(R.id.image);
                        ImageView xrkwltpubkeyimg = sview.findViewById(R.id.image1);
                        ImageView xrkwltprivkeyimg = sview.findViewById(R.id.image2);

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
                            BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(xrkaddress), BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            xrkwltaddimg.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        MultiFormatWriter multiFormatWriter1 = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix1 = multiFormatWriter1.encode(String.valueOf(xrkpublickey), BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder1 = new BarcodeEncoder();
                            Bitmap bitmap1 = barcodeEncoder1.createBitmap(bitMatrix1);
                            xrkwltpubkeyimg.setImageBitmap(bitmap1);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        MultiFormatWriter multiFormatWriter2 = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix2 = multiFormatWriter2.encode(String.valueOf(xrkprivkey), BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder2 = new BarcodeEncoder();
                            Bitmap bitmap2 = barcodeEncoder2.createBitmap(bitMatrix2);
                            xrkwltprivkeyimg.setImageBitmap(bitmap2);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

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
                    }
                });
            }
        });

        creatmultisigwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder nbuilder = new AlertDialog.Builder(MainActivity.this);
                View nview = getLayoutInflater().inflate(R.layout.createmultisigwallet, null);
                Button closemultisig = nview.findViewById(R.id.buttonclosecreatemultisig);
                FloatingActionButton add = nview.findViewById(R.id.add);
                createmultisigbutton = nview.findViewById(R.id.buttoncreatemultisig);

                /*Spinner spinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.numbers, android.R.layout.simple_list_item_1);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
*/

                createmultisigbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder gbuilder = new AlertDialog.Builder(MainActivity.this);
                        View gview = getLayoutInflater().inflate(R.layout.clickcreatemulti, null);
                        Button closemultisigbox = gview.findViewById(R.id.closemultisigbox);
                        ImageView view1 = gview.findViewById(R.id.imagemultisig);
                        TextView multisigaddress = gview.findViewById(R.id.multisigaddress);

                        String res = "";
                        res = address.getMultisigWalletAddress();

                        multisigaddress.setText(res);

                        MultiFormatWriter multiFormatWriter3 = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix3 = multiFormatWriter3.encode(String.valueOf(multisigaddress), BarcodeFormat.QR_CODE, 200, 200);
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

        addr.setText(xrkwltaddress);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder subbuilder = new AlertDialog.Builder(MainActivity.this);
                View subview = getLayoutInflater().inflate(R.layout.submit, null);
                TextView balance = subview.findViewById(R.id.balance);
                TextView pubadd = subview.findViewById(R.id.pubadd);
                TextView sendersadd = subview.findViewById(R.id.sendersadd);
                Button sendtransaction = subview.findViewById(R.id.buttonsendtransaction);
                TextView nohistory = subview.findViewById(R.id.nohistory);
                TextView id = subview.findViewById(R.id.id);
                TextView tim = subview.findViewById(R.id.time);
                TextView am = subview.findViewById(R.id.amount);

                try {
                    if(transactions.listaddresstransaction("xrkwltaddress") == null) {
                        String put = "No Transaction yet.";
                        nohistory.setText(put);
                    }else {
                        object = new JSONObject(String.valueOf(transactions.listaddresstransaction("mpC8A8Fob9ADZQA7iLrctKtwzyWTx118Q9")));
                        txid = object.getString("txid");
                        time = object.getInt("time");
                        amount = object.getInt("amount");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                id.setText(txid);
                tim.setText(time);
                am.setText(amount);

                double bal = 0;

                try {
                    bal = address.checkBalance(xrkwltaddress);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                balance.setText(bal + " XRK");

                pubadd.setText(xrkwltaddress);
                sendersadd.setText(xrkwltaddress);

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

            }
        });
    }
}
