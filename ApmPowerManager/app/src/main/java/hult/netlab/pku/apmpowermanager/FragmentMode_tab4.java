package hult.netlab.pku.apmpowermanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;


public class FragmentMode_tab4 extends Fragment {
    private ListView list;
    private SharedPreferences preferences;
    private Editor editor;
    private String[] str;
    private modemanager mm;
    private MyAdapter simpleAdapter;
    private List<Map<String, Object>> listItems;
    private Dialog editDialog;
    private Dialog addDialog;
    private EditText dialog_mode_name;
    ViewGroup contain;
    LayoutInflater inflaters;
    static final String ACTION_UPDATE = "hult.netlab.pku.apmpowermanager.UPDATE";

    public FragmentMode_tab4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(final LayoutInflater inflater,  final ViewGroup container,
                             Bundle savedInstanceState) {

        LocalBroadcastManager mBroadcastManager = LocalBroadcastManager.getInstance(container.getContext());
        BroadcastReceiver mReceiver;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(ACTION_UPDATE)){
                    refresh();
                }
            }
        };
        mBroadcastManager.registerReceiver(mReceiver, filter);


        final View view = inflater.inflate(R.layout.fragment_fragment_mode, container, false);
        contain = container;
        inflaters = inflater;
        list = (ListView) view.findViewById(R.id.mode_list);
        preferences = getActivity().getSharedPreferences("apm_mode", Activity.MODE_PRIVATE);
        editor = preferences.edit();
        int mode_num = preferences.getInt("mode_num", 0);
        mm = new modemanager(getActivity());
        if (mode_num == 0) {
            editor.putString("mode0", "initial");
            try {
                editor.putInt("initialbrightness", mm.getBrightness());
            } catch (SettingNotFoundException e) {
                e.printStackTrace();
            }
            try {
                editor.putInt("initialtimeout", mm.getTimeout());
            } catch (SettingNotFoundException e) {
                e.printStackTrace();
            }
            editor.putBoolean("initialdata", mm.isDataEnabled());
            editor.putBoolean("initialwifi", mm.isWifiEnabled());
            editor.putBoolean("initialbluetooth", mm.isBluetoothEnabled());
            editor.putBoolean("initialsilence", mm.isSilence());
            editor.putBoolean("initialvibrate", mm.isVibrate());

            editor.putString("mode1", "Super Saving");

            editor.putInt("Super Savingbrightness", 100);
            editor.putInt("Super Savingtimeout", 15000);
            editor.putBoolean("Super Savingdata", false);
            editor.putBoolean("Super Savingwifi", false);
            editor.putBoolean("Super Savingbluetooth", false);
            editor.putBoolean("Super Savingsilence", true);
            editor.putBoolean("Super Savingvibrate", false);
            editor.putInt("mode_num", 2);
            editor.commit();
            mode_num = 2;
        }

        str = new String[mode_num];

        for (int i = 0; i < mode_num; i++) {
            str[i] = preferences.getString("mode" + String.valueOf(i), null);
        }

        listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mode_num; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("mode_name", str[i]);
            listItems.add(listItem);
        }

        simpleAdapter = new MyAdapter(this.getActivity().getApplicationContext());
        list.setAdapter(simpleAdapter);

        ImageView v = (ImageView) view.findViewById(R.id.add_mode);
        addDialog = new Dialog(getActivity());

        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                View layout = inflater.inflate(R.layout.dialog_add, container, false);
                dialog_mode_name = (EditText) layout.findViewById(R.id.dialog_mode_name);

                Log.d("name",dialog_mode_name.getText().toString());
                TextView dialog_mode_next = (TextView)layout.findViewById(R.id.dialog_mode_next);
                dialog_mode_next.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        String add_mode_name = dialog_mode_name.getText().toString();
                        editor.putString("OperatingMode", add_mode_name);
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(container.getWindowToken(), 0);
                        Log.d("name",add_mode_name);
                        editor.commit();
                        Intent wordIntent = new Intent(getActivity(), ModeEdit.class);
                        wordIntent.putExtra("mode_add",true);
                        getActivity().startActivityForResult(wordIntent, 1);
                        addDialog.dismiss();
                    }
                });

                TextView dialog_mode_cancel = (TextView) layout.findViewById(R.id.dialog_mode_cancel);
                dialog_mode_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        addDialog.dismiss();
                    }
                });

                addDialog.setContentView(layout);
                addDialog.setTitle("New Mode");
                addDialog.show();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(dialog_mode_name.getWindowToken(),0);
            }
        });


        return view;
    }



    //刷新
    public void refresh() {
        int mode_num = preferences.getInt("mode_num", 0);
        str = new String[mode_num];
        for (int i = 0; i < mode_num; i++) {
            str[i] = preferences.getString("mode" + String.valueOf(i), null);
        }
        listItems.clear();
        for (int i = 0; i < mode_num; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("mode_name", str[i]);
            listItems.add(listItem);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //内部类，方便操作
    class ViewHolder {
        RadioButton radiobutton_mode_name;
        TextView text_edit_mode;
    }

    //内部类，方便操作
    class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private int temp = -1;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return listItems.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        //获取ListView每一行
        public View getView(int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = null;
            final int position1 = position;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.mode_list_detail, null);
                holder.radiobutton_mode_name = (RadioButton) convertView.findViewById(R.id.modecheckbutton);
                holder.text_edit_mode = (TextView) convertView.findViewById(R.id.modename);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.radiobutton_mode_name.setId(position);
            holder.text_edit_mode.setText((String) listItems.get(position).get("mode_name"));

            holder.radiobutton_mode_name.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (temp != -1) {
                            RadioButton tempButton = (RadioButton) getActivity().findViewById(temp);
                            if (tempButton != null)
                                tempButton.setChecked(false);
                        }
                        temp = buttonView.getId();
                        String s = preferences.getString("mode" + String.valueOf(position1), null);
                        int bright = preferences.getInt(s + "brightness", 0);
                        int time = preferences.getInt(s + "timeout", 0);
                        boolean data = preferences.getBoolean(s + "data", true);
                        Log.d("data", String.valueOf(data));
                        boolean wifi = preferences.getBoolean(s + "wifi", false);
                        Log.d("wifi", String.valueOf(wifi));
                        boolean blue = preferences.getBoolean(s + "bluetooth", false);
                        Log.d("data", String.valueOf(data));
                        boolean silence = preferences.getBoolean(s + "silence", false);
                        Log.d("silence", String.valueOf(silence));
                        boolean vibrate = preferences.getBoolean(s + "vibrate", false);
                        Log.d("vibrate", String.valueOf(vibrate));
                        mm.setAll(bright, time, data, wifi, blue, silence, vibrate);
                    }
                }
            });


            editDialog = new Dialog(getActivity());

            holder.text_edit_mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.brightness_seekbar, parent, false);

                    TextView dialog_mode_edit = (TextView) layout.findViewById(R.id.dialog_mode_edit);
                    dialog_mode_edit.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            editor.putString("OperatingMode", str[position1]);
                            editor.commit();
                            Intent wordIntent = new Intent(getActivity(), ModeEdit.class);
                            getActivity().startActivityForResult(wordIntent, 1);
                            editDialog.dismiss();
                        }
                    });


                    TextView dialog_mode_delete = (TextView) layout.findViewById(R.id.dialog_mode_delete);
                    dialog_mode_delete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            int mode_num = preferences.getInt("mode_num", 0);
                            String strtemp;
                            int position = 0;
                            for(position = 0; position < mode_num; position++){
                                if(str[position1] == preferences.getString("mode"+String.valueOf(position), null))
                                    break;
                            }
                            for(int i = position; i < mode_num-1; i++){
                                strtemp = preferences.getString("mode"+String.valueOf(i+1), null);
                                editor.putString("mode"+String.valueOf(i), strtemp);
                            }
                            editor.putInt("mode_num", mode_num-1);
                            editor.commit();
                            LocalBroadcastManager mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
                            Intent intent = new Intent(ACTION_UPDATE);
                            mBroadcastManager.sendBroadcast(intent);
                            editDialog.dismiss();
                        }
                    });

                    editDialog.setContentView(layout);
                    editDialog.setTitle(str[position1]);
                    editDialog.show();
                }
            });

            return convertView;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("hide",""+hidden);
        refresh();
    }


}




