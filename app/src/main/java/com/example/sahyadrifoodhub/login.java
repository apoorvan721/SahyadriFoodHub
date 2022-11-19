package com.example.sahyadrifoodhub;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    TextView textsignup;
    Button login;
    EditText username;
    EditText password;
    TextView forget_password;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        textsignup = findViewById(R.id.signuptextview);
        login = findViewById(R.id.btnlogin);
        forget_password = findViewById(R.id.forgotPassword);
        reset_alert=new AlertDialog.Builder(this);
        inflater =this.getLayoutInflater();

        textsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, register.class));

            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v =inflater.inflate(R.layout.reset_pop,null);

                reset_alert.setTitle("Reset Forget password?")
                        .setMessage("Enter your Email to get password Reset link")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //validate the email adddress
                                EditText popupemail=v.findViewById(R.id.popemail);
                                if(popupemail.getText().toString().isEmpty())
                                {
                                    popupemail.setError("Required field");
                                    return;
                                }
                                //send the resent link
                                firebaseAuth.sendPasswordResetEmail(popupemail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(login.this,"Reset email sent",Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(login.this,e.getMessage() ,Toast.LENGTH_SHORT).show();

                                    }
                                });



                            }
                        }).setNegativeButton("cancel",null)
                        .setView(v)
                        .create().show();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extract and validate
                if(username.getText().toString().isEmpty())
                {
                    username.setError("Email is missing");
                    return;
                }

                if(password.getText().toString().isEmpty())
                {
                    password.setError("Password is missing");
                    return;
                }
                //data is valid
                //login user
                firebaseAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(),Category_home.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Category_home.class));
            finish();
        }
    }
}