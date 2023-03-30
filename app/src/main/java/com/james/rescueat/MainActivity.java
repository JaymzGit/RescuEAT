package com.james.rescueat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText emailInput, pwdInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailInput = findViewById(R.id.IdTxtField);
        pwdInput = findViewById(R.id.PasswordTxtField);
    }
    public boolean checkNull() {
        boolean isNull = true;
        if (emailInput.getText().toString().isEmpty() || pwdInput.getText().toString().isEmpty()) {
            isNull = true;
            Toast.makeText(this, "Email or Password cannot be empty!", Toast.LENGTH_SHORT).show();
        } else {
            isNull = false;
        }
        return isNull;
    }
    public void LoginButton(View view) {
        if (!checkNull()) {
            Query query = db.collection("users").whereEqualTo("email", emailInput.getText().toString());
            query.get().addOnCompleteListener(new OnCompleteListener < QuerySnapshot > () {
                @Override
                public void onComplete(@NonNull Task < QuerySnapshot > task) {
                    if (task.isSuccessful()) {
                        query.get().addOnSuccessListener(new OnSuccessListener < QuerySnapshot > () {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String pwd;
                                String email;
                                String name;
                                for (DocumentSnapshot document: queryDocumentSnapshots) {
                                    pwd = document.get("password").toString();
                                    email = document.get("email").toString();
                                    name = document.get("name").toString();
                                    if (pwd.equals(pwdInput.getText().toString()) && email.equals(emailInput.getText().toString())) {
                                        Toast.makeText(getApplicationContext(), "Welcome, " + name + "!", Toast.LENGTH_SHORT).show();
                                        toMainMenu(view);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                    if (task.getResult().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Email does not exist!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void toMainMenu(View view) {
        Intent main = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(main);
    }
    public void toRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }
}