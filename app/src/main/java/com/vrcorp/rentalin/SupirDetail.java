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
import com.vrcorp.rentalin.R;
import com.vrcorp.rentalin.app.AppController;
import com.vrcorp.rentalin.server.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SupirDetail extends AppCompatActivity {
    ProgressDialog pDialog;
    private String url = Url.URL + "tambahsupir.php";
    private static final String TAG = SupirDetail.class.getSimpleName();
    SharedPreferences sharedpreferences;
    int success;
    EditText namaSupir, nohp,harga,alamat;
    TextView idLama;
    String tnamaSupir, tnohp,tharga,talamat, jenis, id, string_id;
    String ttnamaSupir, ttnohp,ttharga,ttalamat, tid;
    Button btn_simpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supir_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        namaSupir=findViewById(R.id.namaSupir);
        nohp=findViewById(R.id.no_hp);
        idLama=findViewById(R.id.idLama);
        harga=findViewById(R.id.harga);
        alamat=findViewById(R.id.alamat);
        btn_simpan=findViewById(R.id.btn_simpan);
        sharedpreferences = getSharedPreferences("rentalinPartner", Context.MODE_PRIVATE);
        string_id = sharedpreferences.getString("id", null);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        jenis=b.getString("jenis");
        if(jenis.equals("update")){
            id=b.getString("id");
            tnamaSupir=b.getString("kodeMobil");
            tnohp=b.getString("merekMobil");
            tharga=b.getString("harga");
            talamat=b.getString("alamat");
            namaSupir.setText(tnamaSupir);
            nohp.setText(tnohp);
            alamat.setText(talamat);
            idLama.setText(id);
            harga.setText(tharga);
        }else{
            namaSupir.setText("");
            nohp.setText("");
            alamat.setText("");
            idLama.setText("");
            harga.setText("");
        }
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttnamaSupir = namaSupir.getText().toString();
                ttnohp = nohp.getText().toString();
                ttharga = harga.getText().toString();
                ttalamat= alamat.getText().toString();
                tid= idLama.getText().toString();
                insertData(ttnamaSupir,ttnohp,ttharga,ttalamat, tid, string_id);
            }
        });
    }
    private void insertData(final String xkodeMobil, final String xmerekMobil, final String xharga,
                            final String xalamat, final String xid, final String idP) {
        pDialog = new ProgressDialog(SupirDetail.this);
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
                        Toast.makeText(SupirDetail.this,
                                "Proses Berhasil", Toast.LENGTH_LONG).show();
                        namaSupir.setText("");
                        nohp.setText("");
                        alamat.setText("");
                        idLama.setText("");
                        harga.setText("");
                    } else {
                        Toast.makeText(SupirDetail.this,
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
                Log.e(TAG, "Login Error: "+xmerekMobil+xharga+xid+idP + error.getMessage());
                Toast.makeText(SupirDetail.this,
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
                params.put("alamat", xalamat);
                params.put("id", xid);
                params.put("idPartner", idP);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }
}
