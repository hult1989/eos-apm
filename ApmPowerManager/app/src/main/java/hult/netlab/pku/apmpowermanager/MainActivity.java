package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Button;

public class MainActivity extends Activity {
    Button btn0 = null;
    Button btn1 = null;
    Button btn2 = null;

    private Button button;
    private Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.LowBatteryRed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
	LinearLayout layout = (LinearLayout)findViewById(R.id.main);
        btn0 = (Button)findViewById(R.id.btn0);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InstalledAppList.class));
            }
        });

        btn1 = (Button)findViewById(R.id.button1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),BatteryInfoMain.class);
                startActivity(intent);
            }
        });

        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),BatteryChartActivity.class);
                startActivity(intent);
            }
        });

        btn2 = new Button(MainActivity.this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
        btn2.setText("appinfo");

        p.addRule(RelativeLayout.BELOW, R.id.button1);
        btn2.setLayoutParams(p);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, appinfoActivity.class));
            }
        });

        layout.addView(btn2);
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

        return super.onOptionsItemSelected(item);
    }
}
