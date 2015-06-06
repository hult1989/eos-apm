package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private static final int PAGENUM = 3;
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

    public void sqliteInit(){
        String createAppDatabase = "create table appinfo (id integer primary key autoincrement, " +
                "pkgname text, pid integer, proctime integer, runningtime integer, timestamp integer);";
        String createBatteryDatabase = "create table batteryinfo (id integer primary key autoincrement, quantity integer, timestamp integer);";
        String createAppRatioHistory = "create table appratio (id integer primary key autoincrement, pkgname text,  ratio integer, timestamp integer);";
        String createAppRatioCMD = "create table apphistory (id integer primary key autoincrement, pkgname text,  ratio integer, timestamp integer);";

        try {
            appDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir().toString()+"/appdb.db3", null);
            Log.e("file location", getFilesDir().toString());
      //      appDatabase.execSQL(createAppDatabase);
      //      appDatabase.execSQL(createBatteryDatabase);
      //      appDatabase.execSQL(createAppRatioCMD);
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
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 5000, pendingIntent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActionBar().setElevation(0);
        getActionBar().hide();
     //   getActionBar().setElevation(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sqliteInit();
                startMyService();
            }
        }).start();
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
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mainPagerAdapter);
        mPager.setCurrentItem(0);
        save_tab.setTextColor(Color.WHITE);

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
}
