package com.example.imageuploader;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int PICk_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage, mButtonUploadImage;
    private TextView mTextViewShowUploads;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private EditText mEditText;
    private Uri mImageUri;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonChooseImage = findViewById(R.id.choose_image);
        mImageView = findViewById(R.id.image_view);
        mButtonUploadImage = findViewById(R.id.upload_image);
        mEditText = findViewById(R.id.image_edit_text);
        mTextViewShowUploads = findViewById(R.id.show_uploads);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseFirestore = FirebaseFirestore.getInstance();

        mButtonChooseImage.setOnClickListener(view -> openImageChooser());
        mButtonUploadImage.setOnClickListener(view -> uploadFile());

        mTextViewShowUploads.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ImageLists.class)));

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
            Glide.with(this).load(mImageUri).into(mImageView);
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadFile() {

        String name = mEditText.getText().toString();

        Uploads uploads = new Uploads();

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

                    uploads.setImageUrl(downloadUri.toString());
                    uploads.setName(name);

                    DocumentReference documentReference;

                    documentReference = Utility.getCollectionReference().document();

                    documentReference.set(uploads).addOnCompleteListener(task1 -> {
                        if (!task1.isSuccessful()) {
                            Toast.makeText(this, task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        Log.d("UPLOADTask", downloadUri.toString());
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "No image chosen", Toast.LENGTH_SHORT).show();
        }
    }
}