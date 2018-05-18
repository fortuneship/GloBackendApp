//package com.tunex.mightyglobackend.utilities;
//
//import android.app.TimePickerDialog;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TimePicker;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
///**
// * Created by MIGHTY5 on 5/18/2018.
// */
//
//public class SetTime implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {
//
//    private EditText mEditText;
//    private Calendar mCalendar;
//    private SimpleDateFormat mFormat;
//
//    public SetTime(EditText editText){
//        this.mEditText = editText;
//        mEditText.setOnFocusChangeListener(this);
//        mEditText.setOnClickListener(this);
//    }
//
//    @Override
//    public void onFocusChange(View view, boolean hasFocus) {
//        if (hasFocus){
//            showPicker(view);
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        showPicker(view);
//    }
//
//    private void showPicker(View view) {
//        if (mCalendar == null)
//            mCalendar = Calendar.getInstance();
//
//        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
//        int minute = mCalendar.get(Calendar.MINUTE);
//
//
//
//        new TimePickerDialog(view.getContext(), this, hour, minute, true).show();
//    }
//
//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        mCalendar.set(Calendar.MINUTE, minute);
//
//        if (mFormat == null)
//            mFormat = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
//
//        this.mEditText.setText(mFormat.format(mCalendar.getTime()));
//    }
//
//}
