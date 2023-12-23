package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText edusername, edpw, edconfirm;
    Button btregis, btcancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        edusername = findViewById(R.id.in_username);
        edpw = findViewById(R.id.in_password);
        edconfirm = findViewById(R.id.in_confirmpass);
        btregis = (Button) findViewById(R.id.btregis);
        btcancel = (Button) findViewById(R.id.btcancel);
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });
        btregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edusername.getText().toString();
                String password = edpw.getText().toString();
                String confirm = edconfirm.getText().toString();
                if (!password.equals(confirm)) {
                    Toast.makeText(Register.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit(); //tạo đối tượng để thực hiện ghi
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.commit();
                    Toast.makeText(Register.this, "Register successfully!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Register.this, Login.class);
                    startActivity(i);
                }
            }
        });
    }
}