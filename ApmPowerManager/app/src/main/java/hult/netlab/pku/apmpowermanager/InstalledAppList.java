package hult.netlab.pku.apmpowermanager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.app.ToolbarActionBar;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InstalledAppList extends Activity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
 //       setTheme(R.style.appt);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_app_list);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pm = getPackageManager();

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
                applistview = (ListView) findViewById(R.id.applistview);
            }
        });

        applistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Toast.makeText(InstalledAppList.this, " id:" + position + ", " + packageInfoLists.get(position).applicationInfo.loadLabel(pm), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InstalledAppList.this, appinfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("id", position);
                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });

        SimpleAdapter adapter = new SimpleAdapter(this, mData, R.layout.listlayout_backup, new String[]{"image", "name"},
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_installed_app_list, menu);
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
