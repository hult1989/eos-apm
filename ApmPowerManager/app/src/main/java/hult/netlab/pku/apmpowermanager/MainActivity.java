package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private static final int PAGENUM = 4;
    private ViewPager mPager;
    private PagerAdapter mainPagerAdapter;
    private RelativeLayout bottom_tab1;
    private RelativeLayout bottom_tab2;
    private RelativeLayout bottom_tab3;
    private RelativeLayout bottom_tab4;
    private TextView save_tab;
    private TextView drain_tab;
    private TextView rank_tab;
    private TextView mode_tab;
    public static SQLiteDatabase appDatabase;
    public static final long SERVICE_INTERVAL_IN_SECONDS = 5;

    public void sqliteInit(){
        String createAppDatabase = "create table appinfo (id integer primary key autoincrement, " +
                "pkgname text, pid integer, proctime integer, runningtime integer, timestamp integer);";
        String createBatteryDatabase = "create table batteryinfo (id integer primary key autoincrement, quantity integer, timestamp integer);";
        String createAppRatioHistory = "create table appratio (id integer primary key autoincrement, pkgname text,  ratio integer, timestamp integer);";
        String createAppRatioCMD = "create table apphistory (id integer primary key autoincrement, pkgname text,  ratio integer, timestamp integer);";

        try {
            appDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir().toString()+"/appdb.db3", null);
            Log.e("file location", getFilesDir().toString());
        //    appDatabase.execSQL(createAppDatabase);
        //    appDatabase.execSQL(createBatteryDatabase);
        //    appDatabase.execSQL(createAppRatioCMD);
        }catch (Exception e){
            e.printStackTrace();
            appDatabase.execSQL("drop table appinfo");
            appDatabase.execSQL("drop table batteryinfo");
            appDatabase.execSQL("drop table apphistory");
            appDatabase.execSQL(createAppDatabase);
            appDatabase.execSQL(createBatteryDatabase);
            appDatabase.execSQL(createAppRatioCMD);
        }
    }

    public void startMyService(){
       /*
        Intent intent = new Intent();
        intent.setAction("MyService");
        intent.setPackage(getPackageName());
        startService(intent);
        */
        AlarmManager alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, MyService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, SERVICE_INTERVAL_IN_SECONDS * 1000, pendingIntent);
    }


    static final String ACTION_UPDATE = "hult.netlab.pku.apmpowermanager.UPDATE";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActionBar().setElevation(0);
        }
        getActionBar().hide();
     //   getActionBar().setElevation(0);
        sqliteInit();
        startMyService();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_tab1 = (RelativeLayout) findViewById(R.id.bottom_tab1);
        bottom_tab2 = (RelativeLayout) findViewById(R.id.bottom_tab2);
        bottom_tab3 = (RelativeLayout) findViewById(R.id.bottom_tab3);
        bottom_tab4 = (RelativeLayout) findViewById(R.id.bottom_tab4);
        save_tab = (TextView) findViewById(R.id.tab_save_text);
        drain_tab = (TextView) findViewById(R.id.tab_drain_text);
        rank_tab = (TextView) findViewById(R.id.tab_rank_text);
        mode_tab = (TextView) findViewById(R.id.tab_mode_text);

        mPager = (ViewPager) findViewById(R.id.main_pager);
        mPager.setOffscreenPageLimit(3);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mainPagerAdapter);
        mPager.setCurrentItem(0);


        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        save_tab.setTextColor(Color.WHITE);
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 1:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(Color.WHITE);
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 2:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(Color.WHITE);
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 3:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(Color.WHITE);
                        break;
                    default:
                        save_tab.setTextColor(Color.WHITE);
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                }
            }
        });

        bottom_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });
        bottom_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(1);
            }
        });
        bottom_tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(2);
            }
        });
        bottom_tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(3);
            }
        });



        BroadcastReceiver myBroadcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = (int) (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                        / (float) intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100) * 100);
                Log.e("battery level", level + "");

                switch (intent.getIntExtra(BatteryManager.EXTRA_STATUS, 1)) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 1) == BatteryManager.BATTERY_PLUGGED_AC)
                            Log.e("battery ", "on ac");
                        else
                            Log.e("battery ", "on battery");
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                            Log.e("battery ", "dis discharging");
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        Log.e("battery ", "full");
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        Log.e("battery ", "not charging");
                        break;
                }
                switch (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 1)) {
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        Log.e("battery ", "not charging");
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        Log.e("battery ", "health good");
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        Log.e("battery ", "over voltage");
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        Log.e("battery ", "over heat");
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        Log.e("battery ", "unknown");
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        Log.e("battery ", "specified failer");
                        break;
                }
                Log.e("voltage ", intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 1) + "mv");
                Log.e("temperature", intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 1)/10.0 + "°C");
                Log.e("technology", intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
            }
        };
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {
        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new BatteryRateFragment();
                case 1:
                    return new BatteryChartFragment();
                case 2:
                    return new FragmentRank();
                case 3:
                    return new FragmentMode_tab4();
                default:
                    return new BatteryRateFragment();
            }
        }

        @Override
        public int getCount() {
            return PAGENUM;
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.about) {
            startActivity(new Intent(MainActivity.this, aboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
        if (requestCode == 1 && (resultCode == ModeEdit.RESULT_OK)) {
            LocalBroadcastManager mBroadcastManager = LocalBroadcastManager.getInstance(this);
            Intent intent = new Intent(ACTION_UPDATE);
            mBroadcastManager.sendBroadcast(intent);
        }
    }
}
