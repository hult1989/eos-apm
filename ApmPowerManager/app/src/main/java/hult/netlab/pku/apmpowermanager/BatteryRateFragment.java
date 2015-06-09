package hult.netlab.pku.apmpowermanager;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Timer;
import java.util.TimerTask;



public class BatteryRateFragment extends Fragment {
    private static final int NUM_PAGES = 2;
   // private Timer timer;
    private DonutProgress donutProgress;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    static final String ACTION_UPDATE = "hult.netlab.pku.apmpowermanager.UPDATE";
    int level;
    String innerBottomText;

    public BatteryRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateBatteryInfo();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.activity_batteryinfomain,container,false);
        donutProgress = (DonutProgress)rootview.findViewById(R.id.donut_progress);
        donutProgress.setTextSize(140);
        donutProgress.setSuffixTextSize(60);
        donutProgress.setInnerBottomTextColor(Color.WHITE);
        donutProgress.setInnerBottomTextSize(30);

        updateBatteryInfo();

        mPager = (ViewPager)rootview.findViewById(R.id.sub_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(1);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }
        });

        LocalBroadcastManager mBroadcastManager = LocalBroadcastManager.getInstance(container.getContext());
        BroadcastReceiver mReceiver;
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.ACTION_BATTERYINFO_CHANGE);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(MainActivity.ACTION_BATTERYINFO_CHANGE)){
                       updateBatteryInfo();
                       }
            }
        };
        mBroadcastManager.registerReceiver(mReceiver, filter);

        return rootview;
    }


    private void updateBatteryInfo(){
        level = MainActivity.batteryPreference.getInt("batterylevel",0);
        innerBottomText = MainActivity.tc.getRemainTime();

        donutProgress.setText(level+"");
        donutProgress.setInnerBottomText(innerBottomText);
        donutProgress.setPrefixText(MainActivity.batteryPreference.getString("charging"," "));
       // multiple thread cause conflict when define global variable
       final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(donutProgress.getProgress()>level){
                            donutProgress.setProgress(0);
                        }
                        donutProgress.setProgress(donutProgress.getProgress() + 1);
                        if (donutProgress.getProgress() == level) {
                            Log.e("stop","must stop");
                            timer.cancel();
                        }
                    }
                });
            }
        }, 10, 30);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。

        if (requestCode == 1 && (resultCode == ModeEdit.RESULT_OK)) {
            LocalBroadcastManager mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
            Intent intent = new Intent(ACTION_UPDATE);
            mBroadcastManager.sendBroadcast(intent);
        }
    }


}
