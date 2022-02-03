package com.itible.bike.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itible.bike.R;
import com.itible.bike.dao.TrainingDao;
import com.itible.bike.entity.Training;

import java.util.Calendar;

public class SaveTrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        final EditText edit_position = findViewById(R.id.edit_distance);
        Button btnSubmit = findViewById(R.id.btn_submit);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String user = sharedPref.getString(MyPreferencesActivity.USER_PREF, "jano");
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String trainingFinalUrl = b.getString(MonitoringScreen.FINAL_URL);
        long duration = b.getLong(MonitoringScreen.FINAL_DURATION);
        int movements = b.getInt(MonitoringScreen.FINAL_MOVEMENTS);


        btnSubmit.setOnClickListener(v -> {
            if (edit_position.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "You must enter the distance!", Toast.LENGTH_SHORT).show();
            } else {
                TrainingDao trainingDao = new TrainingDao();
                Training training = new Training();
                training.setName(user);
                training.setTrainingUrl(trainingFinalUrl);
                training.setDistance(Integer.parseInt(edit_position.getText().toString()));
                long date = Calendar.getInstance().getTime().getTime();
                training.setDate(date);
                training.setDuration(Math.toIntExact(duration));
                training.setRpm(Math.round((float) movements / ((float) duration / 60)));

                trainingDao.add(training)
                        .addOnSuccessListener(suc -> {
                            Toast.makeText(getApplicationContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent1);
                        })
                        .addOnFailureListener(er ->
                                Toast.makeText(getApplicationContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}