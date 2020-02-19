package com.vrcorp.rentalin.layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrcorp.rentalin.R;


public class OrderanFragment extends Fragment {

    public OrderanFragment() {
        // Required empty public constructor
    }

    public static OrderanFragment newInstance(String param1, String param2) {
        OrderanFragment fragment = new OrderanFragment();
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
        return inflater.inflate(R.layout.fragment_orderan, container, false);
    }

}
