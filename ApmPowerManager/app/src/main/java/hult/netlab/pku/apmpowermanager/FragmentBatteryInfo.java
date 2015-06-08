package hult.netlab.pku.apmpowermanager;


import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentBatteryInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBatteryInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBatteryInfo extends Fragment {

    private ListView listView;
    private ArrayList<Item> items;

    public FragmentBatteryInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_fragment_battery_info,container,false);
        listView = (ListView)rootview.findViewById(R.id.battery_info_list);
        items = getData();
        final itemAdapter adapter = new itemAdapter(container.getContext());
        listView.setAdapter(adapter);

        LocalBroadcastManager mBroadcastManager = LocalBroadcastManager.getInstance(container.getContext());
        BroadcastReceiver mReceiver;
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.ACTION_BATTERYINFO_CHANGE);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(MainActivity.ACTION_BATTERYINFO_CHANGE)){
                    getData();
                    adapter.notifyDataSetChanged();
                }
            }
        };
        mBroadcastManager.registerReceiver(mReceiver, filter);

        return rootview;
    }

    private ArrayList<Item> getData() {
        ArrayList<Item> list = new ArrayList<Item>();
        Item item0 = new Item(R.drawable.ic_cellphone_android_grey600_48dp, "Standby Time", MainActivity.tc.getStandByTime());
        list.add(item0);
        Item item5 = new Item(R.drawable.ic_settings_grey600_48dp, "Battery type", MainActivity.batteryPreference.getString("technology", null));
        list.add(item5);
        Item item1 = new Item(R.drawable.ic_calendar_plus_grey600_48dp, "Health status", MainActivity.batteryPreference.getString("health", null));
        list.add(item1);
        Item item4 = new Item(R.drawable.ic_flash_grey600_48dp, "Battery voltage", MainActivity.batteryPreference.getString("voltage", null));

        list.add(item4);
        Item item3 = new Item(R.drawable.ic_thermometer_grey600_48dp, "Battery temperature", MainActivity.batteryPreference.getString("temperature", null));
        list.add(item3);
        return list;
    }

    public class itemAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public itemAdapter(Context context) {
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHloder holder = null;
            if (convertView == null) {
                holder = new ViewHloder();
                convertView = layoutInflater.inflate(R.layout.remain_list_detail, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
                holder.name = (TextView) convertView.findViewById(R.id.item_name);
                holder.time = (TextView) convertView.findViewById(R.id.item_tiem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHloder) convertView.getTag();
            }
            holder.icon.setImageResource(items.get(position).icon);
            holder.name.setText(items.get(position).name);
            holder.time.setText(items.get(position).time);

            return convertView;
        }

    }

    class Item {
        int icon;
        String name;
        String time;

        public Item(int icon, String name, String time) {
            this.icon = icon;
            this.name = name;
            this.time = time;
        }
    }

    public final class ViewHloder {
        public ImageView icon;
        public TextView name;
        public TextView time;
    }


}
