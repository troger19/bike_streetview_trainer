package com.itible.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itible.bike.R;
import com.itible.bike.dao.TrainingDao;
import com.itible.bike.dto.TrainingDto;

import java.util.HashMap;

public class SaveTrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        final EditText edit_name = findViewById(R.id.edit_name);
        final EditText edit_position = findViewById(R.id.edit_position);
        Button btn = findViewById(R.id.btn_submit);
        TrainingDao dao = new TrainingDao();
        TrainingDto emp_edit = (TrainingDto) getIntent().getSerializableExtra("EDIT");
        if (emp_edit != null) {
            btn.setText("UPDATE");
            edit_name.setText(emp_edit.getName());
            edit_position.setText(emp_edit.getPosition());
        } else {
            btn.setText("SUBMIT");
        }
        btn.setOnClickListener(v ->
        {
            TrainingDto emp = new TrainingDto(edit_name.getText().toString(), edit_position.getText().toString());
            if (emp_edit == null) {
                dao.add(emp)
                        .addOnSuccessListener(suc -> {
                            Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SaveTrainingActivity.this, LoadTrainingActivity.class);
                            startActivity(intent);
                        })

                        .addOnFailureListener(er ->
                                Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", edit_name.getText().toString());
                hashMap.put("position", edit_position.getText().toString());
                dao.update(emp_edit.getKey(), hashMap).addOnSuccessListener(suc -> {
                    Toast.makeText(this, "Record is updated", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(SaveTrainingActivity.this, LoadTrainingActivity.class);
                    startActivity(intent);
                }).addOnFailureListener(er ->
                        Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}