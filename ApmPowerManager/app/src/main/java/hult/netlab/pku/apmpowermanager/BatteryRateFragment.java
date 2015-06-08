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
    private Timer timer;
    private DonutProgress donutProgress;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    static final String ACTION_UPDATE = "hult.netlab.pku.apmpowermanager.UPDATE";

    private static class freshThread extends Thread {
        public void run() {
            Looper.prepare();
            MainActivity.freshBatteryInfoHandler = new Handler() {
                public void handleMessage(Message msg){
                    if(msg.what == MainActivity.REFRESH){
                        Log.e("new thread", "works!");
                    }
                }
            };
            Looper.loop();
        }
    }

    public BatteryRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.mBatteryBroadcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("got broadcast", "!@!");
                int level = (int) (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                        / (float) intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100) * 100);
                switch (intent.getIntExtra(BatteryManager.EXTRA_STATUS, 1)) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 1) == BatteryManager.BATTERY_PLUGGED_AC)
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        //         Log.e("battery ", "dis discharging");
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        //          Log.e("battery ", "full");
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        //        Log.e("battery ", "not charging");
                        break;
                }
                switch (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 1)) {
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        //        Log.e("battery ", "UNKNOWN");
                        break;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.activity_batteryinfomain,container,false);

        donutProgress = (DonutProgress)rootview.findViewById(R.id.donut_progress);
        donutProgress.setText(String.valueOf(MainActivity.batteryPreference.getInt("batterylevel", 1024)));
        donutProgress.setTextSize(160);
        donutProgress.setInnerBottomText("Percent Battery");
        donutProgress.setInnerBottomTextColor(Color.WHITE);
        donutProgress.setInnerBottomTextSize(30);
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
