package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class appinfoActivity extends Activity {
    ImageView iconView = null;
    TextView textView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.LowBatteryRed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        LinearLayout frameView = (LinearLayout)findViewById(R.id.frame);
        frameView.addView(new LineChart().execute(appinfoActivity.this));
        Intent intent = getIntent();
        textView = (TextView)findViewById(R.id.appname);
        int position = (int)intent.getExtras().get("id");
        iconView = (ImageView)findViewById(R.id.image);
        iconView.setImageDrawable(InstalledAppList.packageInfoLists.get(position).applicationInfo.loadIcon(InstalledAppList.pm));
        textView.setText(InstalledAppList.packageInfoLists.get(position).applicationInfo.loadLabel(InstalledAppList.pm));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appinfo, menu);
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
