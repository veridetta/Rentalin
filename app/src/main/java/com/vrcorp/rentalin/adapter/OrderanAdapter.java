package com.vrcorp.rentalin.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vrcorp.rentalin.MainActivity;
import com.vrcorp.rentalin.R;
import com.vrcorp.rentalin.model.ModelUtama;
import com.vrcorp.rentalin.server.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class OrderanAdapter extends RecyclerView.Adapter<OrderanAdapter.MyViewHolder> {

    private Context context;
    List<ModelUtama> notesList, konjugasi;
    ProgressDialog pDialog;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView idOrderan, status, cekin, cekout, supir, pendapatan, namaMobil;
        public CardView ly_menu;
        TextView terima, tolak;
        LinearLayout ly_button;

        public MyViewHolder(View view) {
            super(view);
            idOrderan = view.findViewById(R.id.id_orderan);
            status = view.findViewById(R.id.status);
            cekin = view.findViewById(R.id.cekin);
            cekout = view.findViewById(R.id.cekout);
            supir = view.findViewById(R.id.supir);
            pendapatan = view.findViewById(R.id.pendapatan);
            namaMobil= view.findViewById(R.id.namaMobil);
            ly_menu = view.findViewById(R.id.card_menu);
            ly_button = view.findViewById(R.id.ly_button);
            terima=view.findViewById(R.id.btn_terima);
            tolak=view.findViewById(R.id.btn_tolak);
        }
    }


    public OrderanAdapter(Context context, List<ModelUtama> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.konjugasi = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderan_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ModelUtama student = notesList.get(position);
        holder.idOrderan.setText("#Ord"+student.getIdOrderan()+"CRB");
        holder.status.setText(student.getStatus());
        holder.cekin.setText("Cek in: "+student.getCekin());
        holder.cekout.setText("Cek out: "+student.getCekout());
        holder.supir.setText("Supir: "+student.getSupir());
        holder.namaMobil.setText(student.getNamamobil());
        if(student.getStatus().equals("Menunggu Konfirmasi Rental")){
            holder.ly_button.setVisibility(View.VISIBLE);
        }else{
            holder.ly_button.setVisibility(View.GONE);
        }
        holder.terima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAction(student.getIdOrderan(), "terima");
            }
        });
        //format harga
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###", symbols);
        String prezzo = decimalFormat.format(Integer.parseInt(student.getBiaya()));
        holder.pendapatan.setText(prezzo);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
    private void postAction(final String xid, final String action){
        final String urll = Url.URL + "actoinorderan.php?id="+xid+"&&action="+action;
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memproses data ....");
        pDialog.show();
        Log.wtf("URL Called", urll + "");
        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                urll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    int sukses = jsonObject.getInt("success");
                    if(sukses > 0){
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                        Toast.makeText(context, "Proses berhasil", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        Log.e(MainActivity.class.getSimpleName(), "Auth Response: " +response);
                    }else{
                        if(pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(context, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
                //finish();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

}