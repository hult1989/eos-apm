package hult.netlab.pku.apmpowermanager;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;



public class BatteryRateFragment extends Fragment {

    private static final int NUM_PAGES = 2;
    private Timer timer;
    private DonutProgress donutProgress;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    public BatteryRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.activity_batteryinfomain,container,false);

        donutProgress = (DonutProgress)rootview.findViewById(R.id.donut_progress);
        donutProgress.setText("80");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(donutProgress.getProgress()>80){
                            donutProgress.setProgress(0);
                        }
                        donutProgress.setProgress(donutProgress.getProgress() + 1);
                        if (donutProgress.getProgress() == 80) {
                            timer.cancel();
                        }
                    }
                });
            }

        }, 100, 30);

        mPager = (ViewPager)rootview.findViewById(R.id.sub_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(1);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }
        });
        return rootview;
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }



        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new FragmentBatteryInfo();
                case 1:
                    return new SubFragmentMode();
                default: return new FragmentMode();
            }
        }


        public int getCount() {
            return NUM_PAGES;
        }
    }
}
