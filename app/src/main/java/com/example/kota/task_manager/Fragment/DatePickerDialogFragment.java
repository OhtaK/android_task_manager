package com.example.kota.task_manager.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by keisuke-ota on 2019/06/04.
 */

//DatePickerの表示用
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public EditText showDateView;//日付を選択した時にそれを表示するView

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year, month, dayOfMonth);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //日付が選択されたときの処理
        String str = String.format(Locale.US, "%d-%d-%d",year, month+1, day);
        showDateView.setText(str);
    }
}