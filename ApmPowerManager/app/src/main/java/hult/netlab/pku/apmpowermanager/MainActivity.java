package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity {
    private static final int PAGENUM = 5;
    private ViewPager mPager;
    private PagerAdapter mainPagerAdapter;
    private RelativeLayout bottom_tab1;
    private RelativeLayout bottom_tab2;
    private RelativeLayout bottom_tab3;
    private RelativeLayout bottom_tab4;
    private RelativeLayout bottom_tab5;
    private TextView save_tab;
    private TextView drain_tab;
    private TextView rank_tab;
    private TextView mode_tab;
    private TextView about_tab;

    public static final long SERVICE_INTERVAL_IN_SECONDS = 3600;
    public static SharedPreferences batteryPreference;

    static final String ACTION_UPDATE = "hult.netlab.pku.apmpowermanager.UPDATE";
    public static final String ACTION_BATTERYINFO_CHANGE = "hult.netlab.pku.apmpowermanager.BATTERYINFO_CHANGE";

    public static  BroadcastReceiver mBatteryBroadcastReciver;
    public static modemanager mmanager;
    public static timeCalculator tc;

    class timeCalculator {
        double screenOnCurrent;
        double screenFullCurrent;
        double wifiOnCurrent;
        double wifiActiveCurrent;
        double audioCurrent;
        double videoCurrent;
        double radioOnCurrent;
        double radioActiveCurrent;
        double cpuAwakeCurrent;
        double cpuIdleCurrent;
        double cpuActiveCurrent;
        double bluetoothCurrent;
        double leftBattery;
        double batteryCapacity;
        int cpuSteps;

        public timeCalculator(Context context) {
            mmanager = new modemanager(context);
            try {
                Class clazz = null;
                Constructor con = null;
                clazz = Class.forName("com.android.internal.os.PowerProfile");
                con = clazz.getConstructor(Context.class);
                Object instance = con.newInstance(getApplicationContext());
                Method methodAVG = clazz.getDeclaredMethod("getAveragePower", String.class);
                Method methodNUM = clazz.getDeclaredMethod("getNumSpeedSteps");
                this.cpuSteps = (Integer) methodNUM.invoke(instance);
                this.batteryCapacity = (Double) methodAVG.invoke(instance, "battery.capacity") + 600;
                this.screenOnCurrent = (Double) methodAVG.invoke(instance, "screen.on");
                this.screenFullCurrent = (Double) methodAVG.invoke(instance, "screen.full");
                this.wifiOnCurrent = (Double) methodAVG.invoke(instance, "wifi.on");
                this.wifiActiveCurrent = (Double) methodAVG.invoke(instance, "wifi.active");
                this.audioCurrent = (Double) methodAVG.invoke(instance, "dsp.audio");
                this.videoCurrent = (Double) methodAVG.invoke(instance, "video.audio");
                this.radioOnCurrent = (Double) methodAVG.invoke(instance, "radio.on");
                this.radioActiveCurrent = (Double) methodAVG.invoke(instance, "radio.active");
                this.cpuAwakeCurrent = (Double) methodAVG.invoke(instance, "cpu.awake");
                this.cpuIdleCurrent = (Double) methodAVG.invoke(instance, "cpu.idle");
                this.cpuActiveCurrent = (Double) methodAVG.invoke(instance, "cpu.active");
                this.bluetoothCurrent = (Double) methodAVG.invoke(instance, "bluetooth.on");
                this.leftBattery = (MainActivity.batteryPreference.getInt("batterylevel", 1024) * batteryCapacity / 100);
            } catch (Exception ex) {
            }
        }

        public String getStandByTime() {
            int lifeInMinute = 0;
            try {
                double totalCurrent = this.cpuActiveCurrent;
                if (mmanager.isBluetoothEnabled())
                    totalCurrent += this.bluetoothCurrent;
                if (mmanager.isDataEnabled())
                    totalCurrent += this.radioOnCurrent;
                if (mmanager.isWifiEnabled())
                    totalCurrent += this.wifiOnCurrent;
                lifeInMinute = (int) (this.leftBattery / totalCurrent * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            result = lifeInMinute / 60 + " h " + lifeInMinute % 60 + " m";
            return result;
        }

        public String getRemainTime(){
            int lifeInMinute = 0;
            try {
                double totalCurrent = this.cpuActiveCurrent;
                if(mmanager.isBluetoothEnabled())
                    totalCurrent += this.bluetoothCurrent;
                if(mmanager.isDataEnabled())
                    totalCurrent += this.radioOnCurrent;
                if(mmanager.isWifiEnabled())
                    totalCurrent += this.wifiOnCurrent;
                totalCurrent += (this.screenOnCurrent + (this.screenFullCurrent-this.screenOnCurrent) * mmanager.getBrightness() / 100)/3;
                lifeInMinute = (int)(this.leftBattery / totalCurrent * 60);
            }catch (Exception e){
                e.printStackTrace();
            }
            String result = "";
            result = lifeInMinute / 60 +  " hours " + lifeInMinute % 60 + " minutes";
            return result;
        }

        public String getWifiTime(){
            int lifeInMinute = 0;
            try {
                double totalCurrent = this.cpuActiveCurrent;
                if (mmanager.isBluetoothEnabled())
                    totalCurrent += this.bluetoothCurrent;
                if (mmanager.isDataEnabled())
                    totalCurrent += this.radioOnCurrent;
                if (mmanager.isWifiEnabled())
                    totalCurrent += this.wifiActiveCurrent;
                totalCurrent += this.screenOnCurrent + (this.screenFullCurrent - this.screenOnCurrent) * mmanager.getBrightness() / 100;
                lifeInMinute = (int) (this.leftBattery / totalCurrent * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            result = lifeInMinute / 60 + " hour " + lifeInMinute % 60 + " minutes";
            return result;
        }

        public String getMovieTime() {
            int lifeInMinute = 0;
            try {
                double totalCurrent = this.cpuActiveCurrent;
                if (mmanager.isBluetoothEnabled())
                    totalCurrent += this.bluetoothCurrent;
                if (mmanager.isDataEnabled())
                    totalCurrent += this.radioOnCurrent;
                if (mmanager.isWifiEnabled())
                    totalCurrent += this.wifiOnCurrent;
                totalCurrent += this.audioCurrent + this.videoCurrent;
                totalCurrent += this.screenOnCurrent + (this.screenFullCurrent - this.screenOnCurrent) * mmanager.getBrightness() / 100;
                lifeInMinute = (int) (this.leftBattery / totalCurrent * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            result = lifeInMinute / 60 + " hour " + lifeInMinute % 60 + " minutes";
            return result;
        }

        public String getCellularTime() {
            int lifeInMinute = 0;
            try {
                double totalCurrent = this.cpuActiveCurrent;
                if (mmanager.isBluetoothEnabled())
                    totalCurrent += this.bluetoothCurrent;
                if (mmanager.isDataEnabled())
                    totalCurrent += this.radioActiveCurrent;
                if (mmanager.isWifiEnabled())
                    totalCurrent += this.wifiOnCurrent;
                totalCurrent += this.screenOnCurrent + (this.screenFullCurrent - this.screenOnCurrent) * mmanager.getBrightness() / 100;
                lifeInMinute = (int) (this.leftBattery / totalCurrent * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            result = lifeInMinute / 60 + " hour " + lifeInMinute % 60 + " minutes";
            return result;
        }

        public String getMusicTime() {
            int lifeInMinute = 0;
            try {
                double totalCurrent = this.cpuActiveCurrent;
                if (mmanager.isBluetoothEnabled())
                    totalCurrent += this.bluetoothCurrent;
                if (mmanager.isDataEnabled())
                    totalCurrent += this.radioOnCurrent;
                if (mmanager.isWifiEnabled())
                    totalCurrent += this.wifiOnCurrent;
                totalCurrent += this.audioCurrent;
                lifeInMinute = (int) (this.leftBattery / totalCurrent * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            result = lifeInMinute / 60 + " hour " + lifeInMinute % 60 + " minutes";
            return result;
        }

        public String getGamingTime() {
            int lifeInMinute = 0;
            try {
                double totalCurrent = this.cpuActiveCurrent;
                if (mmanager.isBluetoothEnabled())
                    totalCurrent += this.bluetoothCurrent;
                if (mmanager.isDataEnabled())
                    totalCurrent += this.radioOnCurrent;
                if (mmanager.isWifiEnabled())
                    totalCurrent += this.wifiOnCurrent;
                totalCurrent += this.screenOnCurrent + (this.screenFullCurrent - this.screenOnCurrent) * mmanager.getBrightness() / 100;
                totalCurrent += this.audioCurrent += this.videoCurrent;
                lifeInMinute = (int) (this.leftBattery / totalCurrent * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            result = lifeInMinute / 60 + " hour " + lifeInMinute % 60 + " minutes";
            return result;
        }

        public String getPhoneTime() {
            int lifeInMinute = 0;
            try {
                double totalCurrent = this.cpuAwakeCurrent;
                if (mmanager.isBluetoothEnabled())
                    totalCurrent += this.bluetoothCurrent;
                if (mmanager.isDataEnabled())
                    totalCurrent += this.radioActiveCurrent;
                if (mmanager.isWifiEnabled())
                    totalCurrent += this.wifiOnCurrent;
                lifeInMinute = (int) (this.leftBattery / totalCurrent * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            result = lifeInMinute / 60 + " hour " + lifeInMinute % 60 + " minutes";
            return result;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        batteryPreference = getSharedPreferences("batteryinfo", MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActionBar().setElevation(0);
        }
        getActionBar().hide();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //sqliteInit();
                startMyService();
            }
        }).start();
        setContentView(R.layout.activity_main);
        layoutInit();
        mBatteryBroadcastReciver = getBatteryBroadcastReceiver();
        registerReceiver(mBatteryBroadcastReciver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        tc = new timeCalculator(this);
    }

    protected void onDestroy(){
        this.unregisterReceiver(mBatteryBroadcastReciver);
        super.onDestroy();
    }


    void layoutInit() {

        bottom_tab1 = (RelativeLayout) findViewById(R.id.bottom_tab1);
        bottom_tab2 = (RelativeLayout) findViewById(R.id.bottom_tab2);
        bottom_tab3 = (RelativeLayout) findViewById(R.id.bottom_tab3);
        bottom_tab4 = (RelativeLayout) findViewById(R.id.bottom_tab4);
        bottom_tab5 = (RelativeLayout) findViewById(R.id.bottom_tab5);

        save_tab = (TextView) findViewById(R.id.tab_save_text);
        drain_tab = (TextView) findViewById(R.id.tab_drain_text);
        rank_tab = (TextView) findViewById(R.id.tab_rank_text);
        mode_tab = (TextView) findViewById(R.id.tab_mode_text);
        about_tab = (TextView) findViewById(R.id.tab_mode_about);

        mPager = (ViewPager) findViewById(R.id.main_pager);
        mPager.setOffscreenPageLimit(4);
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
                        about_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 1:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(Color.WHITE);
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        about_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 2:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(Color.WHITE);
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        about_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 3:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(Color.WHITE);
                        about_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 4:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        about_tab.setTextColor(Color.WHITE);
                        break;
                    default:
                        save_tab.setTextColor(Color.WHITE);
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        about_tab.setTextColor(getResources().getColor(R.color.google_teal));
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
        bottom_tab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(4);
            }
        });

    }



    public void startMyService() {

       /*
        Intent intent = new Intent();
        intent.setAction("MyService");
        intent.setPackage(getPackageName());
        startService(intent);
        */
        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, MyService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, SERVICE_INTERVAL_IN_SECONDS * 1000, pendingIntent);
    }


    public BroadcastReceiver getBatteryBroadcastReceiver() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            SharedPreferences.Editor editor = MainActivity.batteryPreference.edit();
            int lastLevel = batteryPreference.getInt("batterylevel", 10);
            long lastStamp = (batteryPreference.getLong("timestamp", 0));
            int  batteryState = BatteryManager.BATTERY_STATUS_NOT_CHARGING;
            public void onReceive(Context context, Intent intent) {
                int level = (int) (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                        / (float) intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100) * 100);
                if ((System.currentTimeMillis() - lastStamp) / 1000 > SERVICE_INTERVAL_IN_SECONDS) {
                    String sqlCmd = "insert into batteryinfo (level, timestamp) values (" + level + ", " + System.currentTimeMillis() + ");";
                    try {
                        Log.e("broadcast", "insert!");
                        StartActivity.appDatabase.execSQL(sqlCmd, new Object[]{});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editor.putInt("batterylevel", level);

                    switch (intent.getIntExtra(BatteryManager.EXTRA_STATUS, 1)) {
                        case BatteryManager.BATTERY_STATUS_CHARGING:
                            editor.putString("charging", "Charging");
                            break;
                        case BatteryManager.BATTERY_STATUS_DISCHARGING:
                            break;
                        case BatteryManager.BATTERY_STATUS_FULL:
                            break;
                        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                            break;
                    }
                    switch (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 1)) {
                        case BatteryManager.BATTERY_HEALTH_DEAD:
                            editor.putString("health", "DEAD");
                            break;
                        case BatteryManager.BATTERY_HEALTH_GOOD:
                            editor.putString("health", "GOOD");
                            break;
                        case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                            editor.putString("health", "OVER VOLTAGE");
                            break;
                        case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                            editor.putString("health", "OVER HEAD");
                            break;
                        case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                            break;
                    }
                    editor.putLong("timestamp", System.currentTimeMillis());
                    editor.putString("temperature", intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 1) / 10.0 + " °C");
                    editor.putString("technology", intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
                    editor.putString("voltage", new DecimalFormat("0.0").format(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 1) / 1000) + " V");
                    editor.putLong("timestamp", System.currentTimeMillis());
                    editor.commit();
                }else{
                    if(Math.abs(level-lastLevel)>=1 || intent.getIntExtra(BatteryManager.EXTRA_STATUS, 1) != batteryState){
                        batteryState = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 1);
                        if(batteryState == BatteryManager.BATTERY_STATUS_CHARGING){
                            editor.putString("charging", "Charging");
                        }else{
                            editor.putString("charging"," ");
                        }
                        LocalBroadcastManager mBroadcastManager = LocalBroadcastManager.getInstance(context);
                        Intent batteryChangeIntent = new Intent(ACTION_BATTERYINFO_CHANGE);
                        mBroadcastManager.sendBroadcast(batteryChangeIntent);
                    }
                    editor.putLong("timestamp", System.currentTimeMillis());
                    editor.putInt("batterylevel", level);
                    editor.commit();
                }
            }
        };
        return broadcastReceiver;
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
                case 4:
                    return new FragmentAbout();
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
