package com.vrcorp.rentalin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vrcorp.rentalin.LoginActivity;
import com.vrcorp.rentalin.MainActivity;
import com.vrcorp.rentalin.MobilDetail;
import com.vrcorp.rentalin.R;
import com.vrcorp.rentalin.SupirDetail;
import com.vrcorp.rentalin.model.ModelUtama;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class MobilAdapter extends RecyclerView.Adapter<MobilAdapter.MyViewHolder> {
    private Context context;
    List<ModelUtama> notesList, konjugasi;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView idmobil, kodeMobil, merekMobil, alamat, harga;
        public CardView ly_menu;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            idmobil = view.findViewById(R.id.id_mobil);
            kodeMobil = view.findViewById(R.id.kodeMobil);
            merekMobil = view.findViewById(R.id.namaMobil);
            alamat = view.findViewById(R.id.alamat);
            harga = view.findViewById(R.id.pendapatan);
            ly_menu = view.findViewById(R.id.card_menu);
            img = view.findViewById(R.id.img_left);
        }
    }


    public MobilAdapter(Context context, List<ModelUtama> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.konjugasi = notesList;
    }

    @Override
    public MobilAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kendaraan_list, parent, false);

        return new MobilAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MobilAdapter.MyViewHolder holder, int position) {
        final ModelUtama student = notesList.get(position);
        holder.idmobil.setText("ID: "+student.getIdOrderan());
        holder.kodeMobil.setText(student.getKodemobil());
        holder.merekMobil.setText(student.getNamamobil());
        holder.alamat.setText(student.getAlamat());
        holder.harga.setText(student.getBiaya());
        //format harga
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###", symbols);
        String prezzo = decimalFormat.format(Integer.parseInt(student.getBiaya()));
        holder.harga.setText(prezzo);
        if(student.getTujuan().equals("supir")){
            holder.img.setImageResource(R.drawable.driver);
        }
        holder.ly_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student.getTujuan().equals("mobil")){
                    Intent intent = new Intent(v.getContext(), MobilDetail.class);
                    intent.putExtra("jenis","update");
                    intent.putExtra("id",student.getIdOrderan());
                    intent.putExtra("kodeMobil",student.getKodemobil());
                    intent.putExtra("merekMobil",student.getNamamobil());
                    intent.putExtra("alamat",student.getAlamat());
                    intent.putExtra("harga",student.getBiaya());
                    v.getContext().startActivity(intent);
                }else if(student.getTujuan().equals("supir")){
                    Intent intent = new Intent(v.getContext(), SupirDetail.class);
                    intent.putExtra("jenis","update");
                    intent.putExtra("id",student.getIdOrderan());
                    intent.putExtra("kodeMobil",student.getKodemobil());
                    intent.putExtra("merekMobil",student.getNamamobil());
                    intent.putExtra("alamat",student.getAlamat());
                    intent.putExtra("harga",student.getBiaya());
                    v.getContext().startActivity(intent);
                }
            }
        });
        if(student.getAlamat().isEmpty()){
            holder.alamat.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
