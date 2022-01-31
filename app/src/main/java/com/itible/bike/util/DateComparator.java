package com.itible.bike.util;

import com.itible.bike.entity.Training;

import java.util.Comparator;

public class DateComparator implements Comparator<Training> {

    @Override
    public int compare(Training exercise1, Training exercise2) {
        Long max1 = exercise1.getDate();
        Long max2 = exercise2.getDate();
        return max1.compareTo(max2);
    }

}
