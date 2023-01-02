package com.example.imageuploader;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {

    static CollectionReference getCollectionReference() {
        return FirebaseFirestore.getInstance().collection("uploads");
    }
}
