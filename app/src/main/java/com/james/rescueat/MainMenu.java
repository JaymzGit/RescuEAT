package com.james.rescueat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.james.rescueat.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference dbaccess1 = db.collection("images").document("kteJgCZdLABVJAYFnIn1");
    private DocumentReference dbaccess2 = db.collection("images").document("F0ffdaoFxY7z3wvMSVQd");
    private DocumentReference dbaccess3 = db.collection("images").document("bu4cTs4aC78Ey3q6dO96");
    private static final String key_name = "imageName";
    private static final String key_url = "imageURL";

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        Intent i = new Intent(getApplicationContext(), Post.class);
                        startActivity(i);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        super.onStart();
    }

    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        super.onRestart();
    }
}