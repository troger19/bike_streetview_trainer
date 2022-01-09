package com.itible.bike.dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.itible.bike.dto.TrainingDto;

import java.util.HashMap;

public class TrainingDao {
    private final DatabaseReference databaseReference;

    public TrainingDao() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(TrainingDto.class.getSimpleName());
    }

    public Task<Void> add(TrainingDto emp) {
        return databaseReference.push().setValue(emp);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key) {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key) {
        if (key == null) {
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
    }
}