package hult.netlab.pku.apmpowermanager;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
                appInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
                for (ApplicationInfo applicationInfo : appInfoList) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("pkgName",applicationInfo.packageName);
                    item.put("image", pm.getApplicationIcon(applicationInfo));
                    item.put("name", pm.getApplicationLabel(applicationInfo));
                    item.put("progress",(int)(Math.random()*100));
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

        appinfoAdapter adapter = new appinfoAdapter(container.getContext());
        applistview.setAdapter(adapter);

     /*
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
       */


        return rootview;
    }


    public class appinfoAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public appinfoAdapter(Context context) {
            this.layoutInflater = LayoutInflater.from(context);
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
                holder.rate = (TextView) convertView.findViewById(R.id.batteryconsumption);
                holder.label = (TextView)convertView.findViewById(R.id.title);
                holder.bar = (ProgressBar) convertView.findViewById(R.id.bar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHloder) convertView.getTag();
            }
            holder.icon.setImageDrawable((Drawable) mData.get(position).get("image"));
            holder.label.setText((String)mData.get(position).get("name"));
            holder.rate.setText(mData.get(position).get("progress")+"%");
            holder.bar.setProgress((int)mData.get(position).get("progress"));

            return convertView;
        }

    }


    class ViewHloder {
        public ImageView icon;
        public TextView label;
        public TextView rate;
        public ProgressBar bar;

    }

}

