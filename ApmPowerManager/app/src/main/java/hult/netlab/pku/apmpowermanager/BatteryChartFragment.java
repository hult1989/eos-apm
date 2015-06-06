package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
 * {@link BatteryChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BatteryChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BatteryChartFragment extends Fragment {

    private ListView listView;
    private ArrayList<Item> items;
    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TextView indicator1;
    private TextView indicator2;
    private TextView indicator3;


    public BatteryChartFragment() {
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
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.activity_battery_chart,container,false);

        indicator1 = (TextView)rootview.findViewById(R.id.indicator1);
        indicator2 = (TextView)rootview.findViewById(R.id.indicator2);
        indicator3 = (TextView)rootview.findViewById(R.id.indicator3);

        mPager = (ViewPager)rootview.findViewById(R.id.battery_paper);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(2);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0 :
                        indicator1.setBackgroundColor(getResources().getColor(android.R.color.white));
                        indicator2.setBackgroundColor(getResources().getColor(R.color.google_teal));
                        indicator3.setBackgroundColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 1 :
                        indicator1.setBackgroundColor(getResources().getColor(R.color.google_teal));
                        indicator2.setBackgroundColor(getResources().getColor(android.R.color.white));
                        indicator3.setBackgroundColor(getResources().getColor(R.color.google_teal));
                        break;
                    case 2 :
                        indicator1.setBackgroundColor(getResources().getColor(R.color.google_teal));
                        indicator2.setBackgroundColor(getResources().getColor(R.color.google_teal));
                        indicator3.setBackgroundColor(getResources().getColor(android.R.color.white));
                        break;
                    default:
                        indicator1.setBackgroundColor(getResources().getColor(R.color.google_teal));
                        indicator2.setBackgroundColor(getResources().getColor(R.color.google_teal));
                        indicator3.setBackgroundColor(getResources().getColor(android.R.color.white));
                        break;
                }
            }
        });

        listView = (ListView)rootview.findViewById(R.id.remain_list);
        items = getData();
        itemAdapter adapter = new itemAdapter(container.getContext());
        listView.setAdapter(adapter);

        return rootview;
    }

    private ArrayList<Item> getData() {
        ArrayList<Item> list = new ArrayList<Item>();
        Item item1 = new Item(R.drawable.ic_book_open_grey600_48dp, "Reading", "10h23m");
        list.add(item1);
        Item item2 = new Item(R.drawable.ic_movie_grey600_48dp, "Watching movies", "5h29m");
        list.add(item2);
        Item item3 = new Item(R.drawable.ic_phone_grey600_48dp, "Phoneing", "5h24m");
        list.add(item3);
        Item item4 = new Item(R.drawable.ic_camera_grey600_48dp, "Taking pictures", "6h23m");
        list.add(item4);
        Item item5 = new Item(R.drawable.ic_wifi_grey600_48dp, "Wifi", "8h12m");
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



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public Fragment getItem(int position) {

            return BatterySlidePageFragment.create(position);
        }


        public int getCount() {
            return NUM_PAGES;
        }
    }





}
