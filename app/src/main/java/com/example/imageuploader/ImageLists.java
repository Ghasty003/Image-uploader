package com.example.imageuploader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ImageLists extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_lists);

        recyclerView = findViewById(R.id.recyclerView);


        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        Query query = Utility.getCollectionReference().orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Uploads> options = new FirestoreRecyclerOptions.Builder<Uploads>().setQuery(query, Uploads.class).build();

        imageAdapter = new ImageAdapter(options, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        imageAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        imageAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageAdapter.notifyDataSetChanged();
    }
}