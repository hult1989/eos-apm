package com.example.flowstatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.base.Adapterforimage;

/**
 * ´Ë´úÂë·ÂÕÕ¡°http://www.eoeandroid.com/thread-171911-1-1.html¡±
 * 
 * @author yand
 * 
 */
public class DataShowActivity extends Activity {
	private ListView showListview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_show);
		showListview = (ListView) findViewById(R.id.apps_data_listview);
		show_data_onlistviw();
	}

	private void show_data_onlistviw() {
		PackageManager pckMan = getPackageManager();
		List<PackageInfo> packs = pckMan.getInstalledPackages(0);
		ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();
		for (PackageInfo p : packs) {
			if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
					&& (p.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
				int appid = p.applicationInfo.uid;
				long rxdata = TrafficStats.getUidRxBytes(appid);
				rxdata = rxdata / 1024;
				long txdata = TrafficStats.getUidTxBytes(appid);
				txdata = txdata / 1024;
				long data_total = rxdata + txdata;
				HashMap<String, Object> items = new HashMap<String, Object>();
				Drawable drawable = p.applicationInfo
						.loadIcon(getPackageManager());
				Log.i("TAG", "" + drawable);
				items.put("appsimage",
						p.applicationInfo.loadIcon(getPackageManager()));
				items.put("appsname",
						p.applicationInfo.loadLabel(getPackageManager())
								.toString());
				items.put("rxdata", rxdata + "");
				items.put("txdata", txdata + "");
				items.put("alldata", data_total + "");
				item.add(items);
			}
		}
		Adapterforimage adapter = new Adapterforimage(this, item);

		showListview.setAdapter(adapter);
	}
}
