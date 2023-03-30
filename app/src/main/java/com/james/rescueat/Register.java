package com.james.rescueat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Vector;

public class Register extends AppCompatActivity {
    public static Vector<String> emails = new Vector<String>();
    public static Vector<String> pnums = new Vector<String>();
    EditText nameInput, emailInput, phoneInput, pwdInput, rpwdInput;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameInput = findViewById(R.id.nameTxtField);
        emailInput = findViewById(R.id.emailTxtField);
        pwdInput = findViewById(R.id.passwordTxtField);
        rpwdInput = findViewById(R.id.rpasswordTxtField);
        phoneInput = findViewById(R.id.phoneInputField);
    }
    public boolean checkPasswordMatch(){
        boolean match = true;
        if(pwdInput.getText().toString().equals(rpwdInput.getText().toString())){
            match = true;
        }
        else{
            match = false;
            Toast.makeText(getApplicationContext(),"Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
        return match;
    }
    public boolean checkNull(){
        boolean found = false;
        if(nameInput.getText().toString().isEmpty() || emailInput.getText().toString().isEmpty() || phoneInput.getText().toString().isEmpty() ||
                pwdInput.getText().toString().isEmpty()|| rpwdInput.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"All fields must be filled in!",Toast.LENGTH_SHORT).show();
            found = true;
        }
        else{
            found = false;
        }
        return found;
    }
    public void retrieveData(){

        Query query = db.collection("users");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                emails.clear();
                pnums.clear();
                for(DocumentSnapshot document : queryDocumentSnapshots){
                    emails.add(document.getString("email"));
                    pnums.add(document.getString("phone"));
                }
            }
        });
    }
    public boolean checkEmail(){
        boolean found = false;
        if(emails.contains(emailInput.getText().toString())){
            found = true;
            Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
        }
        else{
            found = false;
        }
        return found;
    }
    public boolean checkEmailPattern(){
        boolean valid = false;
        String e = emailInput.getText().toString().trim();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!e.matches(pattern)){
            valid = false;
            Toast.makeText(this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();
        }
        else{
            valid = true;
        }
        return valid;
    }
    public boolean checkPhone(){
        boolean found = false;
        if(pnums.contains(phoneInput.getText().toString())){
            found = true;
            Toast.makeText(this, "Phone number already exists!", Toast.LENGTH_SHORT).show();
        }
        else{
            found = false;
        }
        return found;
    }
    public boolean checkPhonePattern(){
        boolean valid = false;
        String p = phoneInput.getText().toString();
        String pattern = "^(\\+??01)[0-46-9]-*[0-9]{7,8}$";
        if(!p.matches(pattern)){
            valid = false;
            Toast.makeText(this, "Enter a valid phone number!", Toast.LENGTH_SHORT).show();
        }
        else{
            valid = true;
        }
        return valid;
    }
    public boolean checkPasswordPattern(){
        boolean valid = false;
        String pwd = pwdInput.getText().toString();
        String pattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        if(!pwd.matches(pattern)){
            valid = false;
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Password Is Not Strong!");
            ad.setMessage("Password must contain 1 Uppercase, 1 Lowercase and 1 Special character and must exceed 8 Characters ");
            ad.show();
        }
        else{
            valid = true;
        }
        return valid;
    }
    public void registerButton(View view) {
        retrieveData();
        Log.d("emails", String.valueOf(emails));
        Log.d("phone", String.valueOf(pnums));
        if(checkPasswordPattern() && checkPhonePattern() && checkEmailPattern() && checkPasswordMatch() && !checkNull() && !checkEmail() && !checkPhone()){
            HashMap<String,String> details = new HashMap<>();
            details.put("email",emailInput.getText().toString());
            details.put("name",nameInput.getText().toString());
            details.put("phone",phoneInput.getText().toString());
            details.put("password",pwdInput.getText().toString());
            db.collection("users").add(details);
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(login);
        }
    }

    public void toLogin(View view) {
        Intent login = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(login);
    }
}