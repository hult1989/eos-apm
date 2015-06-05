package hult.netlab.pku.apmpowermanager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kingway on 15-6-3.
 */
public class FragmentRank extends Fragment {
    private ListView applistview;
    private ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private PackageManager pm ;
    private List<ApplicationInfo> appInfoList = null;

    public FragmentRank() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                /*
                appInfoList = pm.getInstalledApplications(0);
                for (ApplicationInfo applicationInfo : appInfoList) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    String pkgName = applicationInfo.packageName;
                    item.put("pkgName",applicationInfo.packageName);
                    item.put("image", pm.getApplicationIcon(applicationInfo));
                    item.put("name", pm.getApplicationLabel(applicationInfo));
                    item.put("consume", MainActivity.appList.get(pkgName).getCpuConsumption().get(23));
                    mData.add(item);
                }
                 */
                for(Map<String, Object> app: MainActivity.appConsumptionArrayList){
                    Map<String, Object> item = new HashMap<String, Object>();
                    try {
                        item.put("label", app.get("label").toString());
                        item.put("image", pm.getApplicationIcon(app.get("pkgName").toString()));
                        item.put("consume", app.get("consume").toString());
                        item.put("pkgName", app.get("pkgName").toString());
                        mData.add(item);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                applistview = (ListView) rootview.findViewById(R.id.applistview);
            }
        });

        applistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String pkgName = (String)mData.get(position).get("pkgName");
                String appConsume = mData.get(position).get("consume").toString();
                Intent intent = new Intent(container.getContext(), appinfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("pkgName", pkgName);
                mBundle.putString("consume", appConsume);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        SimpleAdapter adapter = new SimpleAdapter(container.getContext(), mData, R.layout.listlayout, new String[]{"image", "label", "consume"},
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

