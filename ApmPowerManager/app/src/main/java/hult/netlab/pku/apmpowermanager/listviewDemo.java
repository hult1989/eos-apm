package hult.netlab.pku.apmpowermanager;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class listviewDemo extends ListActivity {

    ImageView imageView = null;
    private String[] appName = {"Chrome", "Chrome", "Chrome"};
    private String[] appIntro = {"A web browser from Google", "A web browser from Google", "A web browser from Google"};
    ListView listView = null;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    ImageButton btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listView = getListView();
        btn = (ImageButton)findViewById(R.id.button);

        for(int i = 0; i < appIntro.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", R.mipmap.chrome);
            item.put("name", appName[i]);
            //item.put("intro", R.id.bar);
            mData.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, mData, R.layout.listlayout, new String[]{"image", "name"},
                new int[]{R.id.image, R.id.title});
        setListAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(listviewDemo.this, appName[i] + " id:" + l, Toast.LENGTH_SHORT).show();

            }
        });
        super.onCreate(savedInstanceState);
/*
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, appName[0], Toast.LENGTH_SHORT).show();

            }
        });
*/


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

