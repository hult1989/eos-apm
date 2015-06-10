package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;


public class StartActivity extends Activity {
    public  static SQLiteDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getActionBar().hide();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sqliteInit();
            }
        }).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                StartActivity.this.finish();
            }
        }, 1000);


    }
    public void sqliteInit() {
        appDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir().toString() + "/appdb.db3", null);
        SharedPreferences preferences = getSharedPreferences("starttimes", MODE_PRIVATE);
        int time = preferences.getInt("isfirststart", 0);
        if(time == 0) {
            String createAppDatabase = "create table appinfo (id integer primary key autoincrement, " +
                    "pkgname text, pid integer, proctime integer, runningtime integer, timestamp integer);";
            String createAppRatioCMD = "create table apphistory (id integer primary key autoincrement, pkgname text,  ratio integer, timestamp integer);";
            String createBatteryDatabase = "create table batteryinfo (id integer primary key autoincrement, level integer, timestamp integer);";
            Log.e("Mainactivity", "sqliteInit first time");
            appDatabase.execSQL(createBatteryDatabase, new Object[]{});
            appDatabase.execSQL(createAppDatabase);
            appDatabase.execSQL(createAppRatioCMD);

            ActivityManager mActivityManager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
            PackageManager pm = getPackageManager();
            List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
            for (PackageInfo packageInfo : packageInfos) {
                String pkgName = packageInfo.packageName.toString();
                String initAppHistory = "insert into apphistory (pkgname, ratio, timestamp) values("
                        + "\"" + pkgName + "\", " + 1 + ", " + System.currentTimeMillis() + ");";
                String initAppInfo = "insert into appinfo (pkgname , pid , proctime, runningtime, timestamp) "
                        + "values ( \"" + pkgName + "\", 1024, 10, 10, " + System.currentTimeMillis() + ");";
                try {
                    appDatabase.execSQL(initAppHistory, new Object[]{});
                    appDatabase.execSQL(initAppInfo, new Object[]{});
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            String insertBatteryInfo = "insert into batteryinfo (level, timestamp) values (" + 50 + ", " + System.currentTimeMillis() + ");";
            appDatabase.execSQL(insertBatteryInfo, new Object[]{});
            Log.e("batteryinfo init", "in StartActivity");
        }
        Log.e("start time", ": " + time );
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("isfirststart", ++time);
        editor.commit();
    }

}
