package com.example.sahyadrifoodhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Category_home extends AppCompatActivity {
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_home);

        logout=findViewById(R.id.button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        findViewById(R.id.categorybreakfast).setOnClickListener(view -> {
            Intent i = new Intent(Category_home.this, Breakfast_menu.class);
            i.putExtra("Category","Breakfast");
            startActivity(i);

        });

        findViewById(R.id.categoryjuice).setOnClickListener(view -> {
            Intent i = new Intent(Category_home.this, Breakfast_menu.class);
            i.putExtra("Category","Juice");
            startActivity(i);

        });

        findViewById(R.id.categorymeal).setOnClickListener(view -> {
            Intent i = new Intent(Category_home.this, Breakfast_menu.class);
            i.putExtra("Category","Meals");
            startActivity(i);

        });
        findViewById(R.id.categorychats).setOnClickListener(view -> {
            Intent i = new Intent(Category_home.this, Breakfast_menu.class);
            i.putExtra("Category", "Chats");
            startActivity(i);
        });

    }
}