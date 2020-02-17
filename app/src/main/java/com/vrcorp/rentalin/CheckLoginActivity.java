package com.vrcorp.rentalin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class CheckLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_login);
        Intent belumLogin = new Intent(CheckLoginActivity.this, LoginActivity.class);
        belumLogin.putExtra("CEK_LOGIN", "tidak");
        Toast.makeText(CheckLoginActivity.this, "Harap Login Dahulu!",
                Toast.LENGTH_LONG).show();
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        //editor.putBoolean("session_status", true);
        startActivity(belumLogin);
    }
}
