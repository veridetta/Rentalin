package com.vrcorp.rentalin.layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vrcorp.rentalin.LoginActivity;
import com.vrcorp.rentalin.MainActivity;
import com.vrcorp.rentalin.MobilDetail;
import com.vrcorp.rentalin.R;
import com.vrcorp.rentalin.RegisterActivity;
import com.vrcorp.rentalin.SaldoDetail;
import com.vrcorp.rentalin.SupirDetail;
import com.vrcorp.rentalin.server.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class homeFragment extends Fragment {
    ProgressDialog pDialog;
    private String url = Url.URL + "ubahprofil.php";
    private String urlGet = Url.URL + "getprofil.php";
    SharedPreferences sharedpreferences;
    int success;
    String namaPartner, idPartner, jumlahMobil, jumlahSupir, totalSaldo, string_id;
    TextView tnamaPartner, tidPartner, tjumlahMobil, tjumlahSupir, ttotalSaldo;
    LinearLayout ly_kendaraan,ly_supir, ly_kd, ly_sp, ly_saldo;
    public homeFragment() {
        // Required empty public constructor
    }


    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ly_kendaraan = view.findViewById(R.id.ly_kendaraan);
        ly_supir = view.findViewById(R.id.ly_supir);
        ly_supir = view.findViewById(R.id.ly_supir);
        tnamaPartner = view.findViewById(R.id.tNamaPartner);
        tidPartner = view.findViewById(R.id.tIdPartner);
        tjumlahMobil = view.findViewById(R.id.jMobil);
        tjumlahSupir = view.findViewById(R.id.jSupir);
        ttotalSaldo = view.findViewById(R.id.totalSaldo);
        ly_kd = view.findViewById(R.id.jumlah_mobil);
        ly_sp = view.findViewById(R.id.jumlah_supir);
        ly_saldo= view.findViewById(R.id.jumlah_saldo);
        sharedpreferences = getActivity().getSharedPreferences("rentalinPartner", Context.MODE_PRIVATE);
        string_id = sharedpreferences.getString("id", null);
        getData(string_id);
        ly_supir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SupirDetail.class);
                intent.putExtra("jenis","baru");
                startActivity(intent);
            }
        });
        ly_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SupirFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        ly_kendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MobilDetail.class);
                intent.putExtra("jenis","baru");
                startActivity(intent);
            }
        });
        ly_kd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MobilFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        ly_saldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SaldoDetail.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private void getData(final String xid){
        final String urll = Url.URL + "getdata.php?id="+xid;
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat data ....");
        pDialog.show();
        Log.wtf("URL Called", urll + "");
        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                urll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(MainActivity.class.getSimpleName(), "Auth Response: " +urll+ response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jArray = jsonObject.getJSONArray("content");
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject1=jArray.getJSONObject(i);
                        namaPartner=jsonObject1.getString("namaPartner");
                        idPartner ="PART00"+jsonObject1.getString("idPartner");
                        jumlahMobil= jsonObject1.getString("jumlahMobil");
                        jumlahSupir= jsonObject1.getString("jumlahSupir");
                        totalSaldo =jsonObject1.getString("totalSaldo");
                    }
                    //format harga
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setGroupingSeparator('.');
                    symbols.setDecimalSeparator(',');

                    DecimalFormat decimalFormat = new DecimalFormat("Rp #,###", symbols);
                    String prezzo = decimalFormat.format(Integer.parseInt(totalSaldo));
                    tnamaPartner.setText(namaPartner);
                    tidPartner.setText(idPartner);
                    tjumlahMobil.setText(jumlahMobil);
                    tjumlahSupir.setText(jumlahSupir);
                    ttotalSaldo.setText(prezzo);
                    if(pDialog.isShowing()){
                        pDialog.dismiss();
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
                Toast.makeText(getActivity(), "Silahkan coba lagi", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

}
