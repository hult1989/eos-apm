package hult.netlab.pku.powerprofile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Context;

import com.android.internal.os.PowerProfile;


public class MainActivity extends Activity {
    PowerProfile mprofile;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mprofile = new PowerProfile(this);
        textView = (TextView)findViewById(R.id.tx);
        String str = "";
        str +="screen on:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_SCREEN_ON) + " ma\n";
        str +="bluetooth active:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_BLUETOOTH_ACTIVE) + " ma\n";
        str +="bluetooth on:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_BLUETOOTH_ON) + " ma\n";
        str +="bluetooth at CMD:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_BLUETOOTH_AT_CMD) + " ma\n";
        str +="screen full:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_SCREEN_FULL) + " ma\n";
        str +="cpu idle:\t\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_CPU_IDLE) + " ma\n";
        str +="wifi on:\t\t\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_WIFI_ON) + " ma\n";
        str +="wifi active:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_WIFI_ACTIVE) + " ma\n";
        str +="wifi scan:\t\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_WIFI_SCAN) + " ma\n";
        str +="dsp audio:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_AUDIO) + " ma\n";
        str +="dsp video:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_VIDEO) + " ma\n";
        str +="3g active:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_RADIO_ACTIVE) + " ma\n";
        str +="gps on:\t\t\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_GPS_ON) + " ma\n";
        str +="cpu active:\t\t\t\t" + mprofile.getAveragePower(mprofile.POWER_CPU_ACTIVE) + " ma\n";
        str +="cpu awake:\t\t\t" + mprofile.getAveragePower(mprofile.POWER_CPU_AWAKE) + " ma\n";
        str +="battery capacity:" + mprofile.getBatteryCapacity() + " ma\n";
        textView.setTextSize(20);
        textView.setText(str);
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
