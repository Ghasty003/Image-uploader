package com.example.imageuploader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends FirestoreRecyclerAdapter<Uploads, ImageAdapter.ImageViewHolder> {

    Context context;

    public ImageAdapter(@NonNull FirestoreRecyclerOptions<Uploads> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull Uploads uploads) {
        holder.textView.setText(uploads.getName());
        Picasso.with(context).load(uploads.getImageUrl()).into(holder.imageView);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return new ImageViewHolder(view);
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
