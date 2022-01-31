package com.itible.bike.dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.itible.bike.entity.Training;

import java.util.HashMap;

public class TrainingDao {
    private final DatabaseReference databaseReference;

    public TrainingDao() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        db.setPersistenceEnabled(true);
        databaseReference = db.getReference(Training.class.getSimpleName());
    }

    public Task<Void> add(Training training) {
        return databaseReference.push().setValue(training);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key) {
        return databaseReference.child(key).removeValue();
    }

    public Query getByUser(String username) {
        return databaseReference.orderByChild("name").equalTo(username);
    }


    public Query getAll(String username) {
        return databaseReference.orderByChild("name").equalTo(username);
    }
}