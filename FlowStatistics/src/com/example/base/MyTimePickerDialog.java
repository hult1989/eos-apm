package com.example.base;

import android.app.TimePickerDialog;
import android.content.Context;

/**
 * 此代码仿照“http://www.eoeandroid.com/thread-171911-1-1.html”
 * javaapk.com提供测试
 * @author yand
 * 
 */
public class MyTimePickerDialog extends TimePickerDialog {

	public MyTimePickerDialog(Context context, int theme,
			OnTimeSetListener callBack, int hourOfDay, int minute,boolean is24HourView) {
		super(context, theme, callBack, hourOfDay,minute, is24HourView);

	}

	public MyTimePickerDialog(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute,
			 boolean is24HourView) {
		super(context, callBack,  hourOfDay,minute, is24HourView);
		setButton("确定", this);
	}
}