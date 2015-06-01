package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class appinfoActivity extends Activity {
    CardView cardView = null;
    CardView infoView = null;
    CardView frameView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LowBatteryRed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        cardView = (CardView)findViewById(R.id.cardview);
        infoView = (CardView)findViewById(R.id.info);
        frameView = (CardView)findViewById(R.id.frame);
        cardView.setElevation(6);
        infoView.setElevation(6);
        frameView.setElevation(6);
//        linearLayout = (LinearLayout)findViewById(R.id.info);
  //      linearLayout.setElevation(16);
        frameView.addView(new LineChart().execute(appinfoActivity.this));

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
