package com.vrcorp.rentalin.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
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


    public HistoryAdapter(Context context, List<ModelUtama> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.konjugasi = notesList;
    }

    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.riwayat_list, parent, false);

        return new HistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {
        final ModelUtama student = notesList.get(position);
        holder.idOrderan.setText("#Ord"+student.getIdOrderan()+"CRB");
        holder.cekin.setText(student.getCekin());
        holder.status.setText(student.getStatus());
        //format harga
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###", symbols);
        String prezzo;
        if(student.getStatus().equals("keluar")){
            holder.pendapatan.setTextColor(Color.RED);
            prezzo = "-"+decimalFormat.format(Integer.parseInt(student.getBiaya()));
            holder.pendapatan.setText(prezzo);
        }else{
            holder.pendapatan.setTextColor(Color.GREEN);
            prezzo = "+"+decimalFormat.format(Integer.parseInt(student.getBiaya()));
            holder.pendapatan.setText(prezzo);
        }



    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
