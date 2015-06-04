package hult.netlab.pku.apmpowermanager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
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
          //      appInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
                appInfoList = pm.getInstalledApplications(0);
                for (ApplicationInfo applicationInfo : appInfoList) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("pkgName",applicationInfo.packageName);
                    item.put("image", pm.getApplicationIcon(applicationInfo));
                    item.put("name", pm.getApplicationLabel(applicationInfo));
                    mData.add(item);
                }
                applistview = (ListView) rootview.findViewById(R.id.applistview);
            }
        });

        applistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String pkgName = (String)mData.get(position).get("pkgName");
                Intent intent = new Intent(container.getContext(), appinfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("pkgName", pkgName);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        SimpleAdapter adapter = new SimpleAdapter(container.getContext(), mData, R.layout.listlayout, new String[]{"image", "name"},
                new int[]{R.id.image, R.id.title});
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

