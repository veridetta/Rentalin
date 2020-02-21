package com.vrcorp.rentalin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import com.vrcorp.rentalin.adapter.HistoryAdapter;
import com.vrcorp.rentalin.model.ModelUtama;
import com.vrcorp.rentalin.server.Url;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class SaldoDetail extends AppCompatActivity {

    Button btn_cairkan;
    TextView saldo;
    ProgressDialog pDialog;
    private static final String TAG = MainActivity.class.getSimpleName();
    SharedPreferences sharedpreferences;
    int success;
    String namaMobil, idOrderan, status, cekin, cekout, supir, pendapatan, string_id, totalSaldo;
    List<ModelUtama> dbList;
    List<ModelUtama> modelList = new ArrayList<ModelUtama>();
    private HistoryAdapter adapter;
    LinearLayout data, noData;
    RecyclerView order_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_detail);
        saldo = findViewById(R.id.saldo);
        btn_cairkan = findViewById(R.id.btn_cairkan);
        sharedpreferences = SaldoDetail.this.getSharedPreferences("rentalinPartner", Context.MODE_PRIVATE);
        string_id = sharedpreferences.getString("id", null);
        getOrderan(string_id,"0");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        btn_cairkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new LovelyTextInputDialog(SaldoDetail.this, R.style.TintTheme)
                        .setTopColorRes(R.color.blue_400)
                        .setTitle("Konfirmasi Penarikan Min. 100000")
                        .setMessage("Masukkan Jumlah Uang (hanya angka)")
                        .setIcon(R.drawable.formuser)
                        .setInputFilter("Masukkan", new LovelyTextInputDialog.TextFilter() {
                            @Override
                            public boolean check(String text) {
                                if(Integer.parseInt(text)<100000){
                                    Toast.makeText(SaldoDetail.this, "Minimal 100000", Toast.LENGTH_SHORT).show();
                                }else{
                                    getOrderan(string_id,text);
                                }
                                return text.matches("\\w+");
                            }
                        })
                        .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                //Toast.makeText(SaldoDetail.this, text, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

            }
        });
    }
    private void getOrderan(final String xid, final String jumlah){
        final String urll = Url.URL + "gethistory.php?id="+xid+"&&jumlah="+jumlah;
        RequestQueue requestQueue= Volley.newRequestQueue(SaldoDetail.this);
        pDialog = new ProgressDialog(SaldoDetail.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat data ....");
        pDialog.show();
        Log.wtf("URL Called", urll + "");
        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                urll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(MainActivity.class.getSimpleName(), "Auth Response: " +urll+ response);
                dbList= new ArrayList<ModelUtama>();
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jArray = jsonObject.getJSONArray("content");
                    totalSaldo = jsonObject.getString("saldo");
                    if(jsonObject.getInt("total")>0){
                        for(int i=0;i<jArray.length();i++){
                            JSONObject jsonObject1=jArray.getJSONObject(i);
                            idOrderan=jsonObject1.getString("idOrderan");
                            namaMobil =jsonObject1.getString("namaMobil");
                            cekin= jsonObject1.getString("cekin");
                            cekout= jsonObject1.getString("cekout");
                            pendapatan=jsonObject1.getString("pendapatan");
                            status=jsonObject1.getString("status");
                            supir=jsonObject1.getString("supir");
                            ModelUtama model = new ModelUtama();
                            model.setIdOrderan(idOrderan);
                            model.setNamamobil(namaMobil);
                            model.setCekin(cekin);
                            model.setCekout(cekout);
                            model.setBiaya(pendapatan);
                            model.setSupir(supir);
                            model.setStatus(status);
                            modelList.add(model);
                        }

                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                        //format harga
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                        symbols.setGroupingSeparator('.');
                        symbols.setDecimalSeparator(',');

                        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###", symbols);
                        String prezzo = decimalFormat.format(Integer.parseInt(totalSaldo));
                        saldo.setText(prezzo);
                        dbList = modelList;
                        order_list =  findViewById(R.id.rc_riwayat);
                        adapter = new HistoryAdapter(SaldoDetail.this, dbList);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SaldoDetail.this,1);
                        order_list.setLayoutManager(mLayoutManager);
                        //konjugasi_list.setItemAnimator(new DefaultItemAnimator());
                        //DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
                        //konjugasi_list.addItemDecoration(decoration);
                        order_list.setAdapter(adapter);
                    }else{
                        if(pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                    }
                }catch (JSONException e){e.printStackTrace(); }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }
                Toast.makeText(SaldoDetail.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}
