package com.example.base;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * 此代码仿照“http://www.eoeandroid.com/thread-171911-1-1.html”
 * javaapk.com提供测试
 * @author yand
 * 
 */
public class MyDatePickerDialog extends DatePickerDialog {

	public MyDatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);

	}

	public MyDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		setButton("确定", this);
	}
}