package com.itible.bike.util;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.itible.bike.R;
import com.itible.bike.dao.TrainingDao;
import com.itible.bike.entity.Training;

import java.util.ArrayList;
import java.util.Date;

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
        String htmlString = "<a href='" + emp.getTrainingUrl() + "'>mapa</a>";         // string inside html markup objects
        Spanned spanned = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY);
        viewHolder.txt_map.setText(spanned);
//        viewHolder.txt_distance.setOnClickListener(v -> {
//            showEditTrainingDialog(emp);
//        });

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
//    private void showEditTrainingDialog(Training exercise) {
//        LayoutInflater factory = LayoutInflater.from(context);
//        final View textEntryView = factory.inflate(R.layout.edit_exercise_layout, null);
//        final EditText edtDate = textEntryView.findViewById(R.id.edt_date);
//        final EditText edtMax = textEntryView.findViewById(R.id.edt_max_rep);
//        final EditText edtSum = textEntryView.findViewById(R.id.edt_max_sum);
//        final EditText edtReps = textEntryView.findViewById(R.id.edt_reps);
//
//        edtDate.setText(Util.sdf.format(exercise.getDate()), TextView.BufferType.EDITABLE);
//        edtMax.setText(String.valueOf(exercise.getMax()), TextView.BufferType.EDITABLE);
//        edtSum.setText(String.valueOf(exercise.getSum()), TextView.BufferType.EDITABLE);
//        edtReps.setText(String.valueOf(exercise.getReps()), TextView.BufferType.EDITABLE);
//        loadStatistics(exercise.getName());
//
//        Dialog editTrainingDialog = new AlertDialog.Builder(context)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Edit Values:").setView(textEntryView)
//                .setPositiveButton("Save", (dialog, which) -> {
//                    if (!Util.insertedValuesCheck(edtMax.getText().toString(), edtSum.getText().toString(), edtReps.getText().toString())) {
//                        Toast.makeText(context, "Zadane hodnoty nie su v poriadku", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    try {
//                        hashMap.put("date", Util.sdf.parse(edtDate.getText().toString()).getTime());
//                        hashMap.put("max", Integer.valueOf(edtMax.getText().toString()));
//                        hashMap.put("sum", Integer.valueOf(edtSum.getText().toString()));
//                        hashMap.put("reps", String.valueOf(edtReps.getText()));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    exerciseDao.update(exercise.getKey(), hashMap)
//                            .addOnSuccessListener(suc -> Toast.makeText(context, "Record is updated", Toast.LENGTH_SHORT).show()).addOnFailureListener(er -> Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show());
//
//                    boolean updateStatistics = false;
//                    HashMap<String, Object> hashMap1 = new HashMap<>();
//                    hashMap1.put("name", statistics.getName());
//                    if (Integer.parseInt(edtSum.getText().toString()) > statistics.getMaxSum()) {// if sum record is made
//                        hashMap1.put("maxSum", Integer.parseInt(edtSum.getText().toString()));
//                        updateStatistics = true;
//                    }
//                    if (Integer.parseInt(edtMax.getText().toString()) > statistics.getMaxReps()) {  // if max record is made
//                        hashMap1.put("maxReps", Integer.parseInt(edtMax.getText().toString()));
//                        updateStatistics = true;
//                    }
//                    if (updateStatistics) {
//                        statisticsDao.update(statistics.getKey(), hashMap1)
//                                .addOnSuccessListener(suc -> Toast.makeText(context, "Statistics is updated", Toast.LENGTH_SHORT).show()).addOnFailureListener(er -> Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show());
//                    }
//                    Intent intent = new Intent(context, MainActivity.class);
//                    context.startActivity(intent);
//                })
//                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
//                .create();
//
//        editTrainingDialog.setCanceledOnTouchOutside(false);
//        editTrainingDialog.show();
//    }
}