package com.itible.bike.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itible.bike.R;
import com.itible.bike.activity.SaveTrainingActivity;
import com.itible.bike.dao.TrainingDao;
import com.itible.bike.dto.TrainingDto;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<TrainingDto> list = new ArrayList<>();
    private final Context context;

    public RVAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<TrainingDto> emp) {
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
        TrainingDto e = null;
        this.onBindViewHolder(holder, position, e);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, TrainingDto e) {
        EmployeeVH vh = (EmployeeVH) holder;
        TrainingDto emp = e == null ? list.get(position) : e;
        vh.txt_name.setText(emp.getName());
        vh.txt_position.setText(emp.getPosition());
        vh.txt_option.setOnClickListener(v ->
        {
            PopupMenu popupMenu = new PopupMenu(context, vh.txt_option);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item ->
            {
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        Intent intent = new Intent(context, SaveTrainingActivity.class);
                        intent.putExtra("EDIT", emp);
                        context.startActivity(intent);
                        break;
                    case R.id.menu_remove:
                        TrainingDao dao = new TrainingDao();
                        dao.remove(emp.getKey()).addOnSuccessListener(suc ->
                        {
                            Toast.makeText(context, "Record is removed", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                            list.remove(emp);
                        }).addOnFailureListener(er ->
                        {
                            Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}