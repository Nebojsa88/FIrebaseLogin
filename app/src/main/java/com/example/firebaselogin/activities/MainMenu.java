package com.example.firebaselogin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebaselogin.MainActivity;
import com.example.firebaselogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainMenu extends AppCompatActivity {

    private Button signOut;
    private ImageView profileImage;
    private Button btnUpload;
    private ProgressBar progressBar;
    Uri imageUri;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        storageReference = FirebaseStorage.getInstance().getReference(user.getEmail());

        btnUpload = findViewById(R.id.buttonUpload);
        progressBar = findViewById(R.id.progressBarPhoto);
        signOut = findViewById(R.id.btn_sign_out);
        profileImage = findViewById(R.id.imageViewProfile);
        progressBar.setVisibility(View.INVISIBLE);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainMenu.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void fileChooser(){
        Intent i  = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){

            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profileImage);
        }
    }
    public void uploadPhoto(){

        btnUpload.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);

        StorageReference reference = storageReference.child("user");
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainMenu.this, "Upload Successful !", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainMenu.this, "Sorry, there is a problem !", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int)progress);
            }
        });

        }


    }
