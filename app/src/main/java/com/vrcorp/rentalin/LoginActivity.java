package com.vrcorp.rentalin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        btnLogin=findViewById(R.id.btn_login);
        btnReg=findViewById(R.id.btn_register);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent belumLogin = new Intent(LoginActivity.this, RegisterActivity.class);
                belumLogin.putExtra("CEK_LOGIN", "tidak");
                Toast.makeText(LoginActivity.this, "Harap Login Dahulu!",
                        Toast.LENGTH_LONG).show();
                //SharedPreferences.Editor editor = sharedpreferences.edit();
                //editor.putBoolean("session_status", true);
                startActivity(belumLogin);
            }
        });
    }
}
