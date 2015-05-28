package hult.netlab.pku.apmpowermanager;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class listviewDemo extends ListActivity {

    ImageView imageView = null;
    ListView listView = null;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    ImageButton btn = null;
    PackageManager pm ;
    List<ApplicationInfo> appInfoList = null;
    ApplicationInfo appInfo;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pm = getPackageManager();
        List<PackageInfo> packageInfoLists = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        appInfoList = new ArrayList<ApplicationInfo>();
        for(PackageInfo packageInfo: packageInfoLists){
            appInfo = new ApplicationInfo();
            appInfoList.add(appInfo);
            Map<String, Object> item = new HashMap<String , Object>();
            int icon = R.id.image;
            item.put("image", packageInfo.applicationInfo.loadIcon(pm));
            item.put("name",  packageInfo.applicationInfo.loadLabel(pm));
            mData.add(item);
        }

        listView = getListView();
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(listviewDemo.this,  " id:" + l, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(listviewDemo.this, appinfoActivity.class));

            }
        });

        SimpleAdapter adapter = new SimpleAdapter(this, mData, R.layout.listlayout, new String[]{"image", "name"},
                new int[]{R.id.image, R.id.title});
        setListAdapter(adapter);

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


    }

    public boolean filterApp(ApplicationInfo info){
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
             return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;

        }
        return false;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

