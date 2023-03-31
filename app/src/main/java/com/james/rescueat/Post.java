package com.james.rescueat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Post extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private static final String TAG = "MainActivity3";
    private static final String KEY_TITLE = "TITLE";
    private final int CAMERA_REQ_CODE = 100;
    ImageView imgCam;
    ImageButton selectImg;
    Button upload;
    Uri uri;
    EditText textType, textDesc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef, imageRef;
    UploadTask uploadTask;
    String title, caption, documentId, newDocumentId, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.add);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent i = new Intent(getApplicationContext(), MainMenu.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        i.putExtras(bundle);
                        startActivity(i);
                        return true;
                }
                return false;
            }
        });

        storageRef = storage.getReference();

        upload = findViewById(R.id.upload);
        selectImg = findViewById(R.id.select);
        textType = findViewById(R.id.txtType);
        textDesc = findViewById(R.id.txtDesc);

        //Upload function
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = textType.getText().toString();
                caption = textDesc.getText().toString();

                // To check if an image has been selected
                if (uri != null) {
                    // To upload image to Firebase Storage and get its URL
                    imageRef = storageRef.child("images/" + uri.getLastPathSegment());
                    uploadTask = imageRef.putFile(uri);

                    uploadTask.addOnSuccessListener(new OnSuccessListener < UploadTask.TaskSnapshot > () {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener < Uri > () {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();

                                            // Upload text and image URL to DB
                                            send_to_db(title, caption, imageUrl);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });
                    Intent i = new Intent(getApplicationContext(), MainMenu.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    i.putExtras(bundle);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Successfully uploaded!", Toast.LENGTH_SHORT).show();
                } else {
                    // To upload text only to DB
                    send_to_db(title, caption, null);
                    Intent i = new Intent(getApplicationContext(), MainMenu.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    i.putExtras(bundle);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Successfully uploaded!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Button to select images
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    //To get images from cam
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQ_CODE) {
                Bitmap img = (Bitmap)(data.getExtras().get("data"));
                selectImg.setImageBitmap(img);
                uri = null;
            } else if (requestCode == 1) {
                uri = data.getData();
                selectImg.setImageURI(uri);
            }
        }
    }

    //To upload both Text and Image into the DB
    public void send_to_db(String title, String caption, @Nullable String imageUrl) {
        Map < String, Object > note = new HashMap < > ();
        note.put("imageName", title);
        note.put("imageCaption", caption);
        if (imageUrl != null) {
            note.put("imageURL", imageUrl);
        }

        db.collection("images").add(note);
    }
}