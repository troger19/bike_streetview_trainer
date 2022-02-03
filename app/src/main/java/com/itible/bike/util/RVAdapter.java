package com.itible.bike.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.itible.bike.R;
import com.itible.bike.activity.MainActivity;
import com.itible.bike.dao.TrainingDao;
import com.itible.bike.entity.Training;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final TrainingDao trainingDao;
    ArrayList<Training> list = new ArrayList<>();

    public RVAdapter(Context ctx, TrainingDao trainingDao) {
        this.context = ctx;

        this.trainingDao = trainingDao;
    }

    public void setItems(ArrayList<Training> emp) {
        list.addAll(emp);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.training_item, parent, false);
        return new EmployeeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Training e = null;
        this.onBindViewHolder(holder, position, e);
    }

    /**
     * Recycler View to show  progress arrows and records
     *
     * @param holder
     * @param position
     * @param e
     */
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, Training e) {
        EmployeeVH viewHolder = (EmployeeVH) holder;
        Training emp = e == null ? list.get(position) : e;
        viewHolder.txt_date.setText(Util.sdf.format(new Date(emp.getDate())));
        viewHolder.txt_distance.setText(String.valueOf(emp.getDistance()));
        viewHolder.txt_time.setText(Util.formatDuration(emp.getDuration()));
        viewHolder.txt_rpm.setText(String.valueOf(emp.getRpm()));
        String htmlString = "<a href='" + emp.getTrainingUrl() + "'>mapa</a>";         // string inside html markup objects
        Spanned spanned = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY);
        viewHolder.txt_map.setText(spanned);
        viewHolder.txt_distance.setOnClickListener(v -> {
            showEditTrainingDialog(emp);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Show edit training dialog a save
     *
     * @param exercise training to edit
     */
    private void showEditTrainingDialog(Training exercise) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View textEntryView = factory.inflate(R.layout.edit_exercise_layout, null);
        final EditText edtDate = textEntryView.findViewById(R.id.edt_date);
        final EditText edtDistance = textEntryView.findViewById(R.id.edt_distance);

        edtDate.setText(Util.sdf.format(exercise.getDate()), TextView.BufferType.EDITABLE);
        edtDistance.setText(String.valueOf(exercise.getDistance()), TextView.BufferType.EDITABLE);

        Dialog editTrainingDialog = new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Edit Values:").setView(textEntryView)
                .setPositiveButton("Save", (dialog, which) -> {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    try {
                        hashMap.put("date", Util.sdf.parse(edtDate.getText().toString()).getTime());
                        hashMap.put("distance", Integer.valueOf(edtDistance.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    trainingDao.update(exercise.getKey(), hashMap)
                            .addOnSuccessListener(suc -> Toast.makeText(context, "Record is updated", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(er -> Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show());

                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                })
                .setNeutralButton("Delete", (dialog, which) -> {
                    trainingDao.remove(exercise.getKey())
                            .addOnSuccessListener(suc -> Toast.makeText(context, "Record is deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(er -> Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show());

                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .create();

        editTrainingDialog.setCanceledOnTouchOutside(false);
        editTrainingDialog.show();
    }
}