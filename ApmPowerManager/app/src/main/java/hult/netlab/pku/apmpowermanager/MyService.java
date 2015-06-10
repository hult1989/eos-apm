package hult.netlab.pku.apmpowermanager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyService extends Service {
    String result;
    ActivityManager mActivityManager;
    List<ActivityManager.RunningAppProcessInfo> mRunningProcess;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void onCreate(){
        Log.d("in thread service", "create");
        mActivityManager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
            PackageManager pm = getPackageManager();
            List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        /*
            for (PackageInfo packageInfo : packageInfos) {
                String pkgName = packageInfo.packageName.toString();
                String initAppHistory = "insert into apphistory (pkgname, ratio, timestamp) values("
                        + "\"" + pkgName + "\", " + 0.01 + ", " + System.currentTimeMillis() + ");";
                String initAppInfo = "insert into appinfo (pkgname , pid , proctime, runningtime, timestamp) "
                        + "values ( \"" + pkgName + "\", 1024, 10, 10, " + System.currentTimeMillis() + ");";
                try {
                    MainActivity.appDatabase.execSQL(initAppHistory, new Object[]{});
                    MainActivity.appDatabase.execSQL(initAppInfo, new Object[]{});
                }catch (Exception e){
                    String createAppDatabase = "create table appinfo (id integer primary key autoincrement, " +
                            "pkgname text, pid integer, proctime integer, runningtime integer, timestamp integer);";
                    String createAppRatioCMD = "create table apphistory (id integer primary key autoincrement, pkgname text,  ratio integer, timestamp integer);";
                    try{
                        MainActivity.appDatabase.execSQL(createAppDatabase, new Object[]{});
                        MainActivity.appDatabase.execSQL(createAppRatioCMD, new Object[]{});

                        MainActivity.appDatabase.execSQL(initAppHistory, new Object[]{});
                        MainActivity.appDatabase.execSQL(initAppInfo, new Object[]{});
                    }catch (Exception ne){
                        ne.printStackTrace();
                    }
                }
            }
            */
    }


    class fakeConsumption implements Runnable{
        public void run(){
            double totalRunningTime = 0;
            mRunningProcess = mActivityManager.getRunningAppProcesses();
            HashMap<String, Object> tempItem = new HashMap<>();
            for (ActivityManager.RunningAppProcessInfo amProcess: mRunningProcess) {
                try {
                    PackageManager pm = getPackageManager();
                    int num = amProcess.pkgList.length;
                    for(int i = 0;i < num; i++){
                        PackageInfo packageInfo = pm.getPackageInfo(amProcess.pkgList[i], 0);
                    long appProcTime = getAppProcessTime(amProcess.pid);
                    String pkgName = amProcess.pkgList[0];
                    Long timestamp = System.currentTimeMillis();
                    //这里会被执行但calcrunningtime会出问题，
                    Long runningTime = calcRunningTime(pkgName, amProcess.pid);
                    totalRunningTime += runningTime;
                    tempItem.put(pkgName, runningTime);
                    String SQLcommand = "insert into appinfo (pkgname, pid, proctime, runningtime, timestamp) "
                            + "values ( \"" + pkgName + "\", " + amProcess.pid + ", "
                            + appProcTime + ", " + runningTime + ", " + System.currentTimeMillis() + ");";
                    try{
                        MainActivity.appDatabase.execSQL(SQLcommand, new Object[]{});
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            for (ActivityManager.RunningAppProcessInfo processInfo: mRunningProcess){
                PackageManager pm = getPackageManager();
                try {
                    int num = processInfo.pkgList.length;
                    for(int i = 0;i < num; i++) {
                        PackageInfo packageInfo = pm.getPackageInfo(processInfo.pkgList[i], 0);
                        String pkgName = processInfo.pkgList[0];
                        float runningtime = Float.parseFloat(tempItem.get(pkgName).toString());
                        String insertCMD = "insert into apphistory (pkgname, ratio, timestamp) values" +
                                " (\"" + pkgName + "\", " + runningtime / totalRunningTime + ", "
                                + System.currentTimeMillis() + ");";
                        MainActivity.appDatabase.execSQL(insertCMD, new Object[]{});
                    }
                }catch (Exception e){};
            }
        }
    }

    public int onStartCommand(Intent intent, int flags, int startID){
        new Thread(new fakeConsumption()).start();
        return START_STICKY;

    }

    public long calcRunningTime (String pkgName, int pid) {
        String cmd = "select pid, proctime from appinfo where pkgname = \"" + pkgName + "\" order by timestamp desc limit 0, 1";
        Cursor cursor = MainActivity.appDatabase.rawQuery(cmd, null);
  //      这里什么也读不到
        cursor.moveToNext();
        int count = cursor.getCount();
        long pidInSql = cursor.getLong(0);

        long runningTimeInSql = cursor.getLong(1);
        cursor.close();
        Long appConsumptionTime = getAppProcessTime(pid);
        if (count == 0)
            return appConsumptionTime;
        else if(pid == pidInSql) {
            Long result = appConsumptionTime - runningTimeInSql;
            if(result > 0) {
                return result;
            }else
                return appConsumptionTime;
        }else {
            if(getAppProcessTime(pid) > MainActivity.SERVICE_INTERVAL_IN_SECONDS * 100){
                Long result = appConsumptionTime - runningTimeInSql;
                if(result > 0) {
                    return result;
                }else
                    return appConsumptionTime;
            }
            else
                return appConsumptionTime;
        }
    }


    String second2hour(long second){
        long min = second / 60;
        long h = second / 3600;
        long day = second / 86400;
        long sec = second % 60;
        String result = "";
        if(day > 0)
            result += day + " day";
        if(h > 0)
            result += h + " h";
        if(min > 0)
            result += min + " min";
        result += sec + "sec";
        return result;
    }

    public boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    private long getAppProcessTime(int pid) {

        FileInputStream in = null;
        String ret = null;
        try {
            in = new FileInputStream("/proc/" + pid + "/stat");
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            ret = os.toString();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (ret == null) {
            return 0;
        }

        String[] s = ret.split(" ");
        if (s == null || s.length < 17) {
            return 0;
        }

        final long utime = string2Long(s[13]);
        final long stime = string2Long(s[14]);
        final long cutime = string2Long(s[15]);
        final long cstime = string2Long(s[16]);

        return utime + stime + cutime + cstime;
    }

    private long string2Long(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        return 0;
    }

}
