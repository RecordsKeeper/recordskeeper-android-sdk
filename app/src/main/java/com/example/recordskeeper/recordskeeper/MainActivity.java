package com.example.recordskeeper.recordskeeper;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recordskeeper.address.Address;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    String add;

    private Button createxrkwallet;
    private Button creatmultisigwallet;
    private Button restorexrkwallet;
    private Button submit;
    private EditText addr;

    Address address = new Address();

    public MainActivity() throws IOException{
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createxrkwallet = (Button) findViewById(R.id.buttoncreatexrkwallet);
        creatmultisigwallet = (Button) findViewById(R.id.buttoncreatemultisigwallet);
        restorexrkwallet = (Button) findViewById(R.id.buttonrestorexrkwallet);
        submit = (Button) findViewById(R.id.buttonsubmit);
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
                        AlertDialog.Builder sbuilder = new AlertDialog.Builder(MainActivity.this);
                        View sview = getLayoutInflater().inflate(R.layout.details, null);
                        Button close1 = sview.findViewById(R.id.buttonclose1);
                        TextView xrkwalletadd = sview.findViewById(R.id.xrkwalletaddress);

                            String walletadd = address.getAddress().toString();
                            xrkwalletadd.setText(walletadd);

                        close1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });

                        sbuilder.setView(sview);
                        AlertDialog alertDialog = sbuilder.create();
                        alertDialog.show();

                    }
                });

                mbuilder.setView(mview);
                AlertDialog dialog = mbuilder.create();
                dialog.show();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        });

        creatmultisigwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder nbuilder = new AlertDialog.Builder(MainActivity.this);
                View nview = getLayoutInflater().inflate(R.layout.createmultisigwallet, null);
                nbuilder.setView(nview);
                AlertDialog dialog1 = nbuilder.create();
                dialog1.show();
            }
        });

        restorexrkwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder pbuilder = new AlertDialog.Builder(MainActivity.this);
                View pview = getLayoutInflater().inflate(R.layout.restorexrkwallet, null);
                pbuilder.setView(pview);
                AlertDialog dialog2 = pbuilder.create();
                dialog2.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!addr.getText().toString().isEmpty()){
                    startActivity(new Intent(MainActivity.this, Submit.class));
                }
            }
        });

    }
}
