package com.vrcorp.rentalin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vrcorp.rentalin.app.AppController;
import com.vrcorp.rentalin.server.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MobilDetail extends AppCompatActivity {
    ProgressDialog pDialog;
    private String url = Url.URL + "tambahmobil.php";
    private static final String TAG = MobilDetail.class.getSimpleName();
    SharedPreferences sharedpreferences;
    int success;
    EditText kodeMobil, namaMobil,harga;
    TextView idLama;
    String tkodeMobil, tnamaMobil,tharga, jenis, id, string_id;
    String ttkodeMobil, ttnamaMobil,ttharga, tid;
    Button btn_simpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobil_detail);
        kodeMobil=findViewById(R.id.kodeMobil);
        namaMobil=findViewById(R.id.namaMobil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        harga=findViewById(R.id.harga);
        idLama=findViewById(R.id.idLama);
        btn_simpan=findViewById(R.id.btn_simpan);
        sharedpreferences = getSharedPreferences("rentalinPartner", Context.MODE_PRIVATE);
        string_id = sharedpreferences.getString("id", null);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        jenis=b.getString("jenis");
        if(jenis.equals("update")){
            id=b.getString("id");
            tkodeMobil=b.getString("kodeMobil");
            tnamaMobil=b.getString("merekMobil");
            tharga=b.getString("harga");
            kodeMobil.setText(tkodeMobil);
            namaMobil.setText(tnamaMobil);
            idLama.setText(id);
            harga.setText(tharga);
        }else{
            kodeMobil.setText("");
            namaMobil.setText("");
            harga.setText("");
        }
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttkodeMobil = kodeMobil.getText().toString();
                ttnamaMobil = namaMobil.getText().toString();
                ttharga = harga.getText().toString();
                tid= idLama.getText().toString();
                insertData(ttkodeMobil,ttnamaMobil,ttharga,tid, string_id);
            }
        });

    }
    private void insertData(final String xkodeMobil, final String xmerekMobil, final String xharga,
                            final String xid, final String idP) {
        pDialog = new ProgressDialog(MobilDetail.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mengubah ...");
        pDialog.show();
        Log.e(TAG, "Register Response: "+url);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response string: " + response.toString());
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    // Check for error node in json
                    success = jObj.getInt("success");
                    if (success > 0) {
                        String id = jObj.getString("id");
                        //Log.e("Successfully Register!", jObj.toString());
                        Toast.makeText(MobilDetail.this,
                                "Proses Berhasil", Toast.LENGTH_LONG).show();
                        kodeMobil.setText("");
                        namaMobil.setText("");
                        harga.setText("");
                        idLama.setText("");
                    } else {
                        Toast.makeText(MobilDetail.this,
                                jObj.getString("message"), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.wtf(TAG, e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: "+kodeMobil+xmerekMobil+xharga+xid+idP + error.getMessage());
                Toast.makeText(MobilDetail.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("kodeMobil", xkodeMobil);
                params.put("merekMobil", xmerekMobil);
                params.put("harga", xharga);
                params.put("id", xid);
                params.put("idPartner", idP);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }
}
