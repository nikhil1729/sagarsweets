package com.sagarsweets.in;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;


public class MyProfileFragment extends Fragment {

    TextInputEditText etDob;
    TextView tvAge;

    public MyProfileFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initView(view);
        createDatePickerForDOB();

        // Inflate the layout for this fragment
        return view;
    }

    private void createDatePickerForDOB() {


        etDob.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        // Set DOB
                        String dob = selectedDay + "/"
                                + (selectedMonth + 1) + "/"
                                + selectedYear;

                        etDob.setText(dob);

                        // Calculate Age
                        Calendar dobCalendar = Calendar.getInstance();
                        dobCalendar.set(selectedYear, selectedMonth, selectedDay);

                        Calendar today = Calendar.getInstance();

                        int years = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
                        int months = today.get(Calendar.MONTH) - dobCalendar.get(Calendar.MONTH);
                        int days = today.get(Calendar.DAY_OF_MONTH) - dobCalendar.get(Calendar.DAY_OF_MONTH);

                        // Adjust negative days
                        if (days < 0) {
                            months--;

                            Calendar temp = (Calendar) today.clone();
                            temp.add(Calendar.MONTH, -1);

                            days += temp.getActualMaximum(Calendar.DAY_OF_MONTH);
                        }

                        // Adjust negative months
                        if (months < 0) {
                            years--;
                            months += 12;
                        }

                        // Show age
                        tvAge.setText(years + " Years "
                                + months + " Months "
                                + days + " Days");
                    },
                    year, month, day
            );

            // Minimum age 14 years
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, -14);

            datePickerDialog.getDatePicker()
                    .setMaxDate(maxDate.getTimeInMillis());

            datePickerDialog.show();
        });
    }

    private void initView(View view) {
        etDob = view.findViewById(R.id.etDob);
        tvAge = view.findViewById(R.id.tvAge);
    }
}