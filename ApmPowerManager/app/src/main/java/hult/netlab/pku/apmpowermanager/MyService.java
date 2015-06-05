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
        mRunningProcess = mActivityManager.getRunningAppProcesses();
        new Thread(new sqlOpRunnable()).start();
    }

    class sqlOpRunnable implements Runnable {
        public void run(){
            try{
                refreshAppList();
                getAppComsumption();
            }catch (Exception e){
//                MainActivity.appDatabase.execSQL("drop table appinfo");
                e.printStackTrace();
            }finally {
//                MainActivity.appDatabase.execSQL("drop table appinfo");
            }
            Collections.sort(MainActivity.appConsumptionArrayList, new SortByConsume());
            /*
            for(Map<String, Object> app: MainActivity.appConsumptionArrayList){
                Log.d(app.get("pkgName").toString(), app.get("consume").toString());
            }
             */
        }
    }

    class fakeConsumption implements Runnable{
        public void run(){
            for (ActivityManager.RunningAppProcessInfo amProcess: mRunningProcess) {
                try {
                    PackageManager pm = getPackageManager();
                    PackageInfo packageInfo = pm.getPackageInfo(amProcess.pkgList[0], 0);
                    String SQLcommand = "insert into appinfo (pkgname, quantity, time) values (\"" + amProcess.pkgList[0] + "\", "
                                        + (int)(Math.random() * 50 % 60) + ", " + System.currentTimeMillis() + ");";
                    MainActivity.appDatabase.execSQL(SQLcommand);
               //     Log.d("runnable in service", SQLcommand);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    class SortByConsume implements Comparator{
        public int compare(Object o1, Object o2) {
            Map<String, Object> s1 = ( Map<String, Object>) o1;
            Map<String, Object> s2 = ( Map<String, Object>) o2;
            if (Integer.parseInt(s1.get("consume").toString()) > Integer.parseInt(s2.get("consume").toString()))
                return -1;
            else if (Integer.parseInt(s1.get("consume").toString()) == Integer.parseInt(s2.get("consume").toString())) {
                return 0;
            }
            return 1;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startID){
        new Thread(new fakeConsumption()).start();
        Log.d("time: ", System.currentTimeMillis() + " ");
        return START_STICKY;

    }


    public void getAppComsumption(){
        Iterator iterator = MainActivity.appList.entrySet().iterator();
        String pkgName = "";
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            pkgName = (String) entry.getKey();
            AppConsumption appConsumption = (AppConsumption) entry.getValue();
            Map<String, Object> app = new HashMap<String, Object>();
            app.put("pkgName", pkgName);
            app.put("label", appConsumption.getLabel());
            app.put("consume", appConsumption.getCpuConsumption().get(23));
            MainActivity.appConsumptionArrayList.add(app);
                String SQLcommand = "insert into appinfo (pkgname, quantity, time) values (\"" + pkgName + "\", "
                        + appConsumption.getCpuConsumption().get(23) + ", " + System.currentTimeMillis() + ");";
//            String SQLcommand = "insert into appinfo (pkgname, quantity, time) values (\"" + pkgName + "\", "
//                    + (int)(Math.random() * 60) + ", " + System.currentTimeMillis() + ");";
            try {
                MainActivity.appDatabase.execSQL(SQLcommand);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void refreshAppList(){
        PackageManager pm = getPackageManager();
        for (ActivityManager.RunningAppProcessInfo amProcess: mRunningProcess) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(amProcess.pkgList[0], 0);
           //     if(isSystemApp(packageInfo))
           //         continue;
           //     for(int i = 0; i < amProcess.pkgList.length; i++) {
                    String pkgName = amProcess.pkgList[0];
                    MainActivity.appList.put(pkgName, new AppConsumption());
                    MainActivity.appList.get(pkgName).addCpuComsumption(getAppProcessTime(amProcess.pid));
                    String label = pm.getApplicationLabel(pm.getApplicationInfo(pkgName, 0)).toString();

                    MainActivity.appList.get(pkgName).addLabel(label);
            //    }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
