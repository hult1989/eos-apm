package hult.netlab.pku.apmpowermanager;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.RelativeLayout;

import android.widget.TextView;



public class MainActivity extends FragmentActivity {

    private static final int PAGENUM = 4;
    private ViewPager mPager;
    private PagerAdapter mainPagerAdapter;
    private RelativeLayout bottom_tab1;
    private RelativeLayout bottom_tab2;
    private RelativeLayout bottom_tab3;
    private RelativeLayout bottom_tab4;
    private TextView save_tab;
    private TextView drain_tab;
    private TextView rank_tab;
    private TextView mode_tab;
    static final String ACTION_UPDATE = "hult.netlab.pku.apmpowermanager.UPDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   getActionBar().setElevation(0);
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
        bottom_tab1 = (RelativeLayout) findViewById(R.id.bottom_tab1);
        bottom_tab2 = (RelativeLayout) findViewById(R.id.bottom_tab2);
        bottom_tab3 = (RelativeLayout) findViewById(R.id.bottom_tab3);
        bottom_tab4 = (RelativeLayout) findViewById(R.id.bottom_tab4);
        save_tab = (TextView) findViewById(R.id.tab_save_text);
        drain_tab = (TextView) findViewById(R.id.tab_drain_text);
        rank_tab = (TextView) findViewById(R.id.tab_rank_text);
        mode_tab = (TextView) findViewById(R.id.tab_mode_text);

        mPager = (ViewPager) findViewById(R.id.main_pager);

        mPager.setOffscreenPageLimit(3);

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mainPagerAdapter);
        mPager.setCurrentItem(0);
      // actually no need to initialize save_tab's textcolor as we set it in xml.
      //  save_tab.setTextColor(Color.WHITE);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        save_tab.setTextColor(Color.WHITE);
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 1:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(Color.WHITE);
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 2:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(Color.WHITE);
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 3:
                        save_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(Color.WHITE);
                        break;
                    default:
                        save_tab.setTextColor(Color.WHITE);
                        drain_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        rank_tab.setTextColor(getResources().getColor(R.color.google_teal));
                        mode_tab.setTextColor(getResources().getColor(R.color.google_teal));
                }
            }
        });

        bottom_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });
        bottom_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(1);
            }
        });
        bottom_tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(2);
            }
        });
        bottom_tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(3);
            }
        });
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {
        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new BatteryRateFragment();
                case 1:
                    return new BatteryChartFragment();
                case 2:
                    return new FragmentRank();
                case 3:
                    return new FragmentMode_tab4();
                default:
                    return new BatteryRateFragment();
            }
        }

        @Override
        public int getCount() {
            return PAGENUM;
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

        if (id == R.id.about) {
            startActivity(new Intent(MainActivity.this, aboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
        if (requestCode == 1 && (resultCode == ModeEdit.RESULT_OK)) {
            LocalBroadcastManager mBroadcastManager = LocalBroadcastManager.getInstance(this);
            Intent intent = new Intent(ACTION_UPDATE);
            mBroadcastManager.sendBroadcast(intent);
        }
    }
}
