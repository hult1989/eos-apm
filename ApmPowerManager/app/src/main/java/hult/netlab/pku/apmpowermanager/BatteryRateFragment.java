package hult.netlab.pku.apmpowermanager;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class BatteryRateFragment extends Fragment {

    private Timer timer;
    private DonutProgress donutProgress;
    private ArrayList<Mode> modeList;
    private ListView listView;

    public BatteryRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.activity_batteryinfomain,container,false);

        donutProgress = (DonutProgress)rootview.findViewById(R.id.donut_progress);
        timer = new Timer();
       final MainActivity main = new MainActivity();
        timer.schedule(new TimerTask() {
            public void run (){
            main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        donutProgress.setProgress(donutProgress.getProgress() + 1);
                    }
                });}

        }, 1000, 100);

        listView = (ListView)rootview.findViewById(R.id.mode_list);
        modeList = getData();
        modeAdapter adapter =new modeAdapter(container.getContext());
        listView.setAdapter(adapter);
        return rootview;
  
    }

    private ArrayList<Mode> getData(){
        ArrayList<Mode> list = new ArrayList<Mode>();
        for(int i=1;i<10;i++) {
            Mode mode = new Mode();
            mode.check = Math.random()>0.5 ? true : false;
            mode.name = "mode"+i;
     //       mode.description = "mode"+i+" description!";
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
                convertView.setTag(holder);
            }else{
                holder = (ViewHloder)convertView.getTag();
            }
            holder.checkbutton.setChecked(modeList.get(position).check);
            holder.name.setText(modeList.get(position).name);
 //           holder.description.setText(modeList.get(position).description);

            return convertView;
        }
    }

    class Mode {
        boolean check;
        String name;
//        String description;
    }

    public final class ViewHloder{
        public RadioButton checkbutton;
        public TextView name;
     //   public TextView description;
    }

}
