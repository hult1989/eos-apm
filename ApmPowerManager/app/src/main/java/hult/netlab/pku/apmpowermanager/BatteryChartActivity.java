package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;

import java.util.ArrayList;


public class BatteryChartActivity extends Activity {
    private LinearLayout chartLayout;
    private View view;
    private ListView listView;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_chart);
        chartLayout = (LinearLayout) findViewById(R.id.chartview);
        view = new LineChart().execute(BatteryChartActivity.this);
        chartLayout.addView(view);
        listView = (ListView) findViewById(R.id.remain_list);

        items = getData();
        itemAdapter adapter = new itemAdapter(this);
        listView.setAdapter(adapter);
    }

    private ArrayList<Item> getData() {
        ArrayList<Item> list = new ArrayList<Item>();
        Item item1 = new Item(R.drawable.ic_picture_as_pdf_white_48dp, "Reading", "10h23m");
        list.add(item1);
        Item item2 = new Item(R.drawable.ic_local_movies_white_48dp, "Watching movies", "5h29m");
        list.add(item2);
        Item item3 = new Item(R.drawable.ic_local_phone_white_48dp, "Phoneing", "5h24m");
        list.add(item3);
        Item item4 = new Item(R.drawable.ic_photo_camera_white_72dp, "Taking pictures", "6h23m");
        list.add(item4);
        Item item5 = new Item(R.drawable.ic_wifi_white_48dp, "Wifi", "8h12m");
        list.add(item5);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_battery_chart, menu);
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
