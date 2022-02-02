package com.itible.bike.util;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itible.bike.R;

public class EmployeeVH extends RecyclerView.ViewHolder {
    public TextView txt_date, txt_distance, txt_map, txt_time, txt_rpm;

    public EmployeeVH(@NonNull View itemView) {
        super(itemView);
        txt_date = itemView.findViewById(R.id.txt_date);
        txt_distance = itemView.findViewById(R.id.txt_distance);
        txt_time = itemView.findViewById(R.id.txt_time);
        txt_rpm = itemView.findViewById(R.id.txt_rpm);
        txt_map = itemView.findViewById(R.id.txt_map);
        txt_map.setMovementMethod(LinkMovementMethod.getInstance());
        txt_map.setLinkTextColor(Color.BLUE);
    }
}