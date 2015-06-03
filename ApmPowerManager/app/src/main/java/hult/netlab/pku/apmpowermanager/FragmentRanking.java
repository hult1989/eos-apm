package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRanking.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRanking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRanking extends Fragment {
    ListView applistview;
    ImageView imageView = null;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    public  static PackageManager pm ;
    List<ApplicationInfo> appInfoList = null;
    ApplicationInfo appInfo;

    public static List<PackageInfo> packageInfoLists;
    CardView cardView;


    public  static  List<PackageInfo> getPackageInfoLists(){
        return packageInfoLists;
    }

    public FragmentRanking() {
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

                packageInfoLists = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
                appInfoList = new ArrayList<ApplicationInfo>();
                for (PackageInfo packageInfo : packageInfoLists) {
                    appInfo = new ApplicationInfo();
                    appInfoList.add(appInfo);
                    Map<String, Object> item = new HashMap<String, Object>();
                    int icon = R.id.image;
                    item.put("image", packageInfo.applicationInfo.loadIcon(pm));
                    item.put("name", packageInfo.applicationInfo.loadLabel(pm));
                    mData.add(item);
                }
                applistview = (ListView) rootview.findViewById(R.id.applistview);
            }
        });

        applistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Toast.makeText(getActivity(), " id:" + position + ", " + packageInfoLists.get(position).applicationInfo.loadLabel(pm), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), appinfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("id", position);
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
