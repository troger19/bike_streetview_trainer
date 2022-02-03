package com.itible.bike.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.itible.bike.R;
import com.itible.bike.dao.TrainingDao;
import com.itible.bike.entity.Training;
import com.itible.bike.util.DateComparator;
import com.itible.bike.util.RVAdapter;

import java.util.ArrayList;
//import com.itible.bike.util.RVAdapter;


public class LoadTrainingActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter adapter;
    TrainingDao trainingDao;
    boolean isLoading = false;
    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_training);
        swipeRefreshLayout = findViewById(R.id.swip);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String user = sharedPref.getString(MyPreferencesActivity.USER_PREF, "jano");

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        trainingDao = new TrainingDao();
        adapter = new RVAdapter(this, trainingDao);
        recyclerView.setAdapter(adapter);
        loadData(user);
    }

    private void loadData(String username) {
        swipeRefreshLayout.setRefreshing(true);
        trainingDao.getByUser(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Training> emps = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Training emp = data.getValue(Training.class);
                    emp.setKey(data.getKey());
                    emps.add(emp);
                    key = data.getKey();
                }
                emps.sort(new DateComparator());
                adapter.setItems(emps);
                adapter.notifyDataSetChanged();
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}