package com.example.imageuploader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int PICk_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage, mButtonUploadImage;
    private TextView mTextViewShowUploads;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private EditText mEditText;
    private Uri mImageUri;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonChooseImage = findViewById(R.id.choose_image);
        mImageView = findViewById(R.id.image_view);
        mButtonUploadImage = findViewById(R.id.upload_image);
        mEditText = findViewById(R.id.image_edit_text);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(view -> openImageChooser());
        mButtonUploadImage.setOnClickListener(view -> uploadFile());
    }

    public void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICk_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICk_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileRef.putFile(mImageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    Log.d("UPLOADTask", downloadUri.toString());
                } else {
                    Toast.makeText(MainActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "No image chosen", Toast.LENGTH_SHORT).show();
        }
    }
}