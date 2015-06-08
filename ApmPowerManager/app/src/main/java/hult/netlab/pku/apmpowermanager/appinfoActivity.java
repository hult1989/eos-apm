package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class appinfoActivity extends Activity {
    private ImageView iconView = null;
    private TextView textView = null;
    private TextView consumeText = null;
    private TextView uploadText = null;
    private TextView downloadText = null;

    private Button uninstall_button;
    private Button close_button;
    private PackageManager pm;

    public double[] readSql(String pkgName){
//        String sqlCmd = "select * from appratio where pkgname = \""
//                + pkgName + "\" order by timestamp desc limit 0, 24;";
        String sqlCmd = "select ratio from apphistory where pkgname = \""
                + pkgName + "\" order by timestamp desc limit 0, 24;";
        Cursor cursor = MainActivity.appDatabase.rawQuery(sqlCmd, null);
        int index = 23;
        double[] result = new double[24];
        while(cursor.moveToNext() != false){
            result[index] = cursor.getDouble(0) * 100  ;
            index--;
        }
        cursor.close();
        while(index >= 0){
            result[index] = 0;
            index--;
        }
        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActionBar().setElevation(0);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        pm = getPackageManager();
        LinearLayout frameView = (LinearLayout)findViewById(R.id.frame);

        Intent intent = getIntent();
        textView = (TextView)findViewById(R.id.appname);
        consumeText = (TextView)findViewById(R.id.batteryconsumption);
        uploadText = (TextView)findViewById(R.id.upload);
        downloadText = (TextView)findViewById(R.id.download);
        String pkgName = (String)intent.getExtras().get("pkgName");
        String appConsume = (String)intent.getExtras().get("ratio");
        iconView = (ImageView)findViewById(R.id.image);
        double[] num = readSql(pkgName);
        frameView.addView(new LineChart(num).execute(appinfoActivity.this));


        try {
            iconView.setImageDrawable(pm.getApplicationIcon(pkgName));
            textView.setText(pm.getApplicationLabel(pm.getApplicationInfo(pkgName, 0)));
            int uid = pm.getPackageInfo(pkgName, 0).applicationInfo.uid;
            consumeText.setText(appConsume);
            long cellularDownload = TrafficStats.getUidRxBytes(uid) / 1024;
            long cellularUpload = TrafficStats.getUidTxBytes(uid) / 1024;
            String upload = "";
            if(cellularUpload / 1024 > 1){
                upload +=  cellularUpload / 1024 + " MB " + cellularUpload % 1024 + " KB";
            }else{
                upload +=  cellularUpload % 1024 + " KB";
            }
            uploadText.setText(upload);
            String download = "";
            if(cellularDownload / 1024 > 1){
                download += cellularDownload / 1024 + " MB " + cellularDownload % 1024 + " KB";
            }else{
                download += cellularDownload % 1024 + " KB";
            }
            downloadText.setText(download);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        final Uri uri = Uri.parse("package:"+pkgName);
        close_button = (Button)findViewById(R.id.close);
        uninstall_button = (Button)findViewById(R.id.uninstall);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,uri);
                startActivity(intent);
            }
        });

        uninstall_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DELETE,uri);
                startActivity(intent);
            }
        });
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
            startActivity(new Intent(appinfoActivity.this, aboutActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
