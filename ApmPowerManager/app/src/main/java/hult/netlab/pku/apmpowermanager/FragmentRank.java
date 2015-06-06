package hult.netlab.pku.apmpowermanager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by kingway on 15-6-3.
 */
public class FragmentRank extends Fragment {
    private ListView applistview;
    private ArrayList<Map<String, Object>> mData ;
    private PackageManager pm ;
    private List<ApplicationInfo> appInfoList = null;
    public static long allAppTotalRunningTime;
    private ProgressBar progressBar;

    public FragmentRank() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class sortMap implements Comparator {
        public int compare(Object o1, Object o2) {
            Map<String, Object> s1 = (Map<String, Object>) o1;
            Map<String, Object> s2 = (Map<String, Object>) o2;
            if(Long.parseLong(s1.get("runningtime").toString()) > Long.parseLong(s2.get("runningtime").toString()))
                return -1;
            else if(Long.parseLong(s1.get("runningtime").toString()) == Long.parseLong(s2.get("runningtime").toString()))
                return 0;
            else
                return 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.activity_installed_app_list, container, false);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pm = getActivity().getPackageManager();
                mData = new ArrayList<Map<String, Object>>();

                String selectSqlCmd = "select pkgname, proctime from appinfo group by pkgname order by proctime desc";
                Cursor cursor = MainActivity.appDatabase.rawQuery(selectSqlCmd, null);
                long sumCpuTime = 0;
                while(cursor.moveToNext()){
                    sumCpuTime += cursor.getLong(1);
                }
                cursor.moveToFirst();
                while(cursor.moveToNext()){
                    try {
                        Map<String, Object>item = new HashMap<>();
                        String pkgName = cursor.getString(0);
                        item.put("image", pm.getApplicationIcon(pkgName));
                        long proctime = cursor.getLong(1);
                        float ratio = (float)proctime / sumCpuTime;
                        item.put("ratiotext", new DecimalFormat("0.0%").format(ratio));
                        item.put("ratio", ratio);
                        item.put("pkgname", pkgName);
                        item.put("label", pm.getApplicationLabel(pm.getApplicationInfo(pkgName, 0)));
                        mData.add(item);
                    }catch (Exception e){};
                }

/*
                allAppTotalRunningTime = 0;
                List<PackageInfo> packageInfos = pm.getInstalledPackages(pm.GET_UNINSTALLED_PACKAGES);
                for(PackageInfo packageInfo: packageInfos){
                    String pkgName = packageInfo.packageName;
                    String selectCMD = "select pid, runningtime from appinfo where pkgname = \"" +
                            pkgName + "\" order by timestamp desc limit 0, 24 ;";
                    Cursor cursor = MainActivity.appDatabase.rawQuery(selectCMD, null);
                    long appTotalTime = 0;
                    while(cursor.moveToNext()){
                        appTotalTime += cursor.getLong(1);
                    }
                    allAppTotalRunningTime += appTotalTime;
                    Map<String, Object> item = new HashMap<String, Object>();
                    try{
                        item.put("label", pm.getApplicationLabel(pm.getApplicationInfo(pkgName, 0)));
                        item.put("runningtime", appTotalTime);
                        item.put("pkgname", pkgName);
                        item.put("image", pm.getApplicationIcon(pkgName));
                        mData.add(item);
                    }catch (Exception e){};
                }
                Collections.sort(mData, new sortMap());
                for(Map<String, Object> data: mData) {
                    String ratio =  new DecimalFormat("0.0%").format(Float.parseFloat(data.get("runningtime").toString()) / allAppTotalRunningTime);
            //        Log.e("ratio", ratio);
                    data.put("ratio", ratio);
                }
 */
                applistview = (ListView) rootview.findViewById(R.id.applistview);
            }
        });




        applistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String pkgName = (String) mData.get(position).get("pkgname");
                String appConsume = mData.get(position).get("ratiotext").toString();
                Intent intent = new Intent(container.getContext(), appinfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("pkgName", pkgName);
                mBundle.putString("ratio", appConsume);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        SimpleAdapter adapter = new SimpleAdapter(container.getContext(), mData, R.layout.listlayout, new String[]{"image", "label", "ratiotext"},
                new int[]{R.id.image, R.id.title, R.id.appconsume});
        applistview.setAdapter(adapter);

        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Drawable){
                    ImageView iv = (ImageView)view;
                    iv.setImageDrawable((Drawable)data);
                    return true;
                }else{
                    return false;
                }
            }

        });

        return rootview;
    }


}

