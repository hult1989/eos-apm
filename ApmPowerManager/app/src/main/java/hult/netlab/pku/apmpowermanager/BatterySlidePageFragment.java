package hult.netlab.pku.apmpowermanager;


/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class BatterySlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    public BatterySlidePageFragment() {
    }

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static BatterySlidePageFragment create(int pageNumber) {
        BatterySlidePageFragment fragment = new BatterySlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.abttery_slide_page, container, false);
        String sqlCmd = "select level from batteryinfo order by timestamp desc limit 0, 72;";
        Cursor cursor = StartActivity.appDatabase.rawQuery(sqlCmd, null);
        double[] history = new double[72];
        int index = 0;
        if(cursor.getCount() < 72){
            for(index = 0; index < 72 - cursor.getCount(); index++)
                history[index] = 0;
        }
        index = 71;
        while(cursor.moveToNext()){
            history[index] = cursor.getInt(0);
            index--;
        }
        cursor.close();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CardView chartLayout = (CardView) rootView.findViewById(R.id.chartview);
            chartLayout.setElevation(6);
            if (mPageNumber == 0) {
                double[] dayBeforeYesterday = new double[24];
                for (int i = 0; i < 24; i++) {
                    dayBeforeYesterday[i] = history[i];
                }
                View view = new LineChart(dayBeforeYesterday, 1).execute(container.getContext());
                chartLayout.addView(view);
            } else if (mPageNumber == 1) {
                double[] yesterday = new double[24];
                for (int i = 0; i < 24; i++) {
                    yesterday[i] = history[i+24];
                }
                View view = new LineChart(yesterday, 1).execute(container.getContext());
                chartLayout.addView(view);
            } else {
                double[] today = new double[24];
                for (int i = 0; i < 24; i++) {
                    today[i] = history[i+48];
                }
                View view = new LineChart(today, 1).execute(container.getContext());
                chartLayout.addView(view);
            }

        } else {
            LinearLayout chartLayout = (LinearLayout) rootView.findViewById(R.id.chartview);
            if (mPageNumber == 0) {
                double[] dayBeforeYesterday = new double[24];
                for (int i = 0; i < 24; i++) {
                    dayBeforeYesterday[i] = history[i];
                }
                View view = new LineChart(dayBeforeYesterday, 1).execute(container.getContext());
                chartLayout.addView(view);
            } else if (mPageNumber == 1) {
                double[] yesterday = new double[24];
                for (int i = 0; i < 24; i++) {
                    yesterday[i] = history[i+24];
                }
                View view = new LineChart(yesterday, 1).execute(container.getContext());
                chartLayout.addView(view);
            } else {
                double[] today = new double[24];
                for (int i = 0; i < 24; i++) {
                    today[i] = history[i + 48];
                }
                View view = new LineChart(today, 1).execute(container.getContext());
                chartLayout.addView(view);
            }
        }

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
