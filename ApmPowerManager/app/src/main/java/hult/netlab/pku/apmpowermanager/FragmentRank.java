package hult.netlab.pku.apmpowermanager;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class FragmentRank extends Fragment {
    private ListView applistview;
    private ArrayList<HashMap<String, Object>> mData;
    private PackageManager pm;
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
            if (Long.parseLong(s1.get("runningtime").toString()) > Long.parseLong(s2.get("runningtime").toString()))
                return -1;
            else if (Long.parseLong(s1.get("runningtime").toString()) == Long.parseLong(s2.get("runningtime").toString()))
                return 0;
            else
                return 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.activity_installed_app_list, container, false);
        applistview = (ListView) rootview.findViewById(R.id.applistview);
        pm = getActivity().getPackageManager();
        mData = new ArrayList<HashMap<String, Object>>();

        String selectSqlCmd = "select pkgname, proctime from appinfo group by pkgname order by proctime desc";
        Cursor cursor = MainActivity.appDatabase.rawQuery(selectSqlCmd, null);
        long sumCpuTime = 0;
        Log.d("curso",cursor.getCount()+"");

        while (cursor.moveToNext()) {
            sumCpuTime += cursor.getLong(1);
        }
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            try {
                HashMap<String, Object> item = new HashMap<>();
                String pkgName = cursor.getString(0);
                long proctime = cursor.getLong(1);
                float ratio = (float) proctime / sumCpuTime;
                item.put("image", pm.getApplicationIcon(pkgName));
                item.put("ratiotext", new DecimalFormat("0.0%").format(ratio));
                item.put("ratio", (int)(ratio * 100));
                item.put("pkgname", pkgName);
                item.put("label", pm.getApplicationLabel(pm.getApplicationInfo(pkgName, 0)));
                mData.add(item);
            } catch (Exception e) {
            }
            ;
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
<<<<<<< HEAD
                    try{
                        item.put("label", pm.getApplicationLabel(pm.getApplicationInfo(pkgName, 0)));
                        item.put("runningtime", appTotalTime);
                        item.put("pkgname", pkgName);
                        item.put("image", pm.getApplicationIcon(pkgName));
                        mData.add(item);
                    }catch (Exception e){};
=======
                    item.put("pkgName",applicationInfo.packageName);
                    item.put("image", pm.getApplicationIcon(applicationInfo));
                    item.put("name", pm.getApplicationLabel(applicationInfo));
                    item.put("progress",(int)(Math.random()*100));
                    mData.add(item);
>>>>>>> de0d4c7abfa4d4c9f6957ef87da7d9c0004e28e0
                }
                Collections.sort(mData, new sortMap());
                for(Map<String, Object> data: mData) {
                    String ratio =  new DecimalFormat("0.0%").format(Float.parseFloat(data.get("runningtime").toString()) / allAppTotalRunningTime);
            //        Log.e("ratio", ratio);
                    data.put("ratio", ratio);
                }
 */


        applistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else {
                    return false;
                }
            }

        });
        applistview.setAdapter(new appinfoAdapter(container.getContext(), mData));
        return rootview;
    }
    //   appinfoAdapter adapter = new appinfoAdapter(container.getContext());
}


 class appinfoAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String, Object>> mData;

    public appinfoAdapter(Context context, ArrayList<HashMap<String, Object>> mData) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHloder holder = null;
        if (convertView == null) {
            holder = new ViewHloder();
            convertView = layoutInflater.inflate(R.layout.listlayout, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.image);
            holder.rate = (TextView) convertView.findViewById(R.id.appconsume);
            holder.label = (TextView) convertView.findViewById(R.id.title);
            Log.e("label textview", holder.label.toString());
            holder.bar = (ProgressBar) convertView.findViewById(R.id.bar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHloder) convertView.getTag();
        }

        holder.icon.setImageDrawable((Drawable) mData.get(position).get("image"));
        holder.label.setText((String) mData.get(position).get("label").toString());
        holder.rate.setText(mData.get(position).get("ratiotext").toString());
        holder.bar.setProgress((int)(mData.get(position).get("ratio")));
        return convertView;
    }
}

class ViewHloder {
    public ImageView icon;
    public TextView label;
    public TextView rate;
    public ProgressBar bar;

}


