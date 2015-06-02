package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Button;

import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager mPager;
    PagerAdapter mainPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.LowBatteryRed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



/*
        ActivityManager am = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo info : runningApps) {
           // Log.e("processName", info.processName + "pid:" + info.pid + "uid: "+ info.uid );
                for(int i = 0; i < info.pkgList.length; i++)
                    if(info.pkgList[i].contains("system") == false)
                        Log.e(info.processName, info.pkgList[0]);

        }
        */
        mPager = (ViewPager)findViewById(R.id.main_pager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mainPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
               // invalidateOptionsMenu();
            }
        });
    }


    private class  MainPagerAdapter extends  FragmentStatePagerAdapter{
        public MainPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch(position) {
                case 0:
                    return new BatteryRateFragment();
                case 1:
                    return new BatteryChartFragment();
                default: return new BatteryChartFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
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
        if (id == R.id.about) {
            startActivity(new Intent(MainActivity.this, aboutActivity.class));
            return true;
        }
        if(id==R.id.installedapp){
            startActivity(new Intent(MainActivity.this, InstalledAppList.class));

        }

        return super.onOptionsItemSelected(item);
    }
}
