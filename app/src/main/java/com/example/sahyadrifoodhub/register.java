package com.example.sahyadrifoodhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirmpassword;
    Button btn_register;
    TextView account;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.inputUsername);
        email=findViewById(R.id.inputEmail);
        password=findViewById(R.id.inputPassword);
        confirmpassword=findViewById(R.id.inputConformPassword);
        btn_register=findViewById(R.id.btnRegister);
        account=findViewById(R.id.alreadyHaveAccount);
        fAuth=FirebaseAuth.getInstance();

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this, login.class));

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rname=username.getText().toString();
                String remail=email.getText().toString();
                String rpassword=password.getText().toString();
                String rconfirmpassword=confirmpassword.getText().toString();


                if(rname.isEmpty()){
                    username.setError("Full Name is required");
                    return;
                }

                if(remail.isEmpty()){
                    email.setError("Email is required");
                    return;
                }

                if(rpassword.isEmpty()){
                    password.setError("Password is required");
                    return;
                }

                if(rconfirmpassword.isEmpty()){
                    confirmpassword.setError("Password needs to be confirmed");
                    return;
                }

                if(!rpassword.equals(rconfirmpassword))
                {
                    confirmpassword.setError("Password doesn't match");
                    return;
                }

                Toast.makeText(register.this,"Registration Succesful",Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(remail,rpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(),Category_home.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}