package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
    private Button uninstall_button;
    private Button close_button;
    private PackageManager pm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.LowBatteryRed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        pm = getPackageManager();
        LinearLayout frameView = (LinearLayout)findViewById(R.id.frame);
        frameView.addView(new LineChart().execute(appinfoActivity.this));
        Intent intent = getIntent();
        textView = (TextView)findViewById(R.id.appname);
        String pkgName = (String)intent.getExtras().get("pkgName");
        iconView = (ImageView)findViewById(R.id.image);
        try {
            iconView.setImageDrawable(pm.getApplicationIcon(pkgName));
            textView.setText(pm.getApplicationLabel(pm.getApplicationInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES)));
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
