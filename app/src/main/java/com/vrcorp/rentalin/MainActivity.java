package com.vrcorp.rentalin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vrcorp.rentalin.layout.AkunFragment;
import com.vrcorp.rentalin.layout.MobilFragment;
import com.vrcorp.rentalin.layout.OrderanFragment;
import com.vrcorp.rentalin.layout.SupirFragment;
import com.vrcorp.rentalin.layout.homeFragment;

public class MainActivity extends AppCompatActivity {
    private ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //toolbar.setTitle("Home");
        toolbar.hide();
        loadFragment(new homeFragment());
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    //toolbar.setTitle("Home");
                    toolbar.hide();
                    fragment = new homeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_orderan:
                    fragment = new OrderanFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_car:
                    fragment = new MobilFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_supir:
                    fragment = new SupirFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_profile:
                    fragment = new AkunFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
