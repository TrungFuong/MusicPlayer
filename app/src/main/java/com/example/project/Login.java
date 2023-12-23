package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText edusername, edpass;
    Button btlogin, btregis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btlogin = (Button) findViewById(R.id.btlogin);
        btregis = (Button) findViewById(R.id.btregis);
        edusername = (EditText) findViewById(R.id.in_username);
        edpass = (EditText) findViewById(R.id.in_password);
        btregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences s = getSharedPreferences("USER", MODE_PRIVATE);
                String username = s.getString("username", "");
                String password = s.getString("password", "");
                User user = new User(username, password);
                if (user == null) {
                    Toast.makeText(Login.this, "We can not find a matching username! Please try again or register!", Toast.LENGTH_LONG).show();
                    return;
                }
                String loginusername = edusername.getText().toString();
                String loginpassword = edpass.getText().toString();
            if (loginusername.equals(user.getUsername()) && loginpassword.equals(user.getPassword())){
                    Toast.makeText(Login.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
            }else{
                    Toast.makeText(Login.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}