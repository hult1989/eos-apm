package hult.netlab.pku.apmpowermanager;

import android.content.Context;
import android.media.MediaDescription;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class batteryinfomain extends ActionBarActivity {
    private Timer timer;
    private ArcProgress arcProgress;
    private ArrayList<Mode> modeList;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batteryinfomain);
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arcProgress.setProgress(arcProgress.getProgress() + 1);
                    }
                });
            }
        }, 1000, 100);
        listView = (ListView)findViewById(R.id.mode_list);
        modeList = getData();
        modeAdapter adapter =new modeAdapter(this);
        listView.setAdapter(adapter);
    }

    private ArrayList<Mode> getData(){
        ArrayList<Mode> list = new ArrayList<Mode>();
        for(int i=1;i<10;i++) {
            Mode mode = new Mode();
            mode.check = Math.random()>0.5 ? true : false;
            mode.name = "mode"+i;
            mode.description = "mode"+i+" description!";
            list.add(mode);
        }
        return list;
    }

    public class modeAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public modeAdapter(Context context){
            this.layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return modeList.size();
        }

        @Override
        public Object getItem(int position) {
            return modeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHloder holder = null;
            if(convertView == null){
                holder = new ViewHloder();
                convertView = layoutInflater.inflate(R.layout.mode_list_detail,null);
                holder.checkbutton = (RadioButton)convertView.findViewById(R.id.modecheckbutton);
                holder.name = (TextView)convertView.findViewById(R.id.modename);
                holder.description = (TextView)convertView.findViewById(R.id.modedescription);
                convertView.setTag(holder);
            }else{
                holder = (ViewHloder)convertView.getTag();
            }
            holder.checkbutton.setChecked(modeList.get(position).check);
            holder.name.setText(modeList.get(position).name);
            holder.description.setText(modeList.get(position).description);

            return convertView;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_batteryinfomain, menu);
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

    class Mode {
        boolean check;
        String name;
        String description;
    }

    public final class ViewHloder{
        public RadioButton checkbutton;
        public TextView name;
        public TextView description;
    }
}
