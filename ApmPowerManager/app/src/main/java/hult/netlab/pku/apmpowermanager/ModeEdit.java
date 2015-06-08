package hult.netlab.pku.apmpowermanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class ModeEdit extends Activity {
    private Button button_save;
    private Button button_exit;
    private TextView text_brightness;
    private TextView text_timeout;
    private Switch button_data;
    private Switch button_wifi;
    private Switch button_bluetooth;
    private Switch button_silence;
    private Switch button_vibrate;
    private modemanager mm = new modemanager(this);

    SeekBar brightness_seekbar;
    SeekBar timeout_seekbar;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private int brightness;
    private int timeout;
    private boolean data;
    private boolean wifi;
    private boolean bluetooth;
    private boolean silence;
    private boolean vibrate;
    private String str;   //mode name
    private boolean add; //for add mode


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_edit);

        Intent wordIntent = getIntent();
        add = wordIntent.getBooleanExtra("mode_add",false);

        text_brightness = (TextView) findViewById(R.id.text_brightness);
        text_timeout = (TextView) findViewById(R.id.text_timeout);
        button_data = (Switch) findViewById(R.id.button_data);
        button_wifi = (Switch) findViewById(R.id.button_wifi);
        button_bluetooth = (Switch) findViewById(R.id.button_bluetooth);
        button_silence = (Switch) findViewById(R.id.button_silence);
        button_vibrate = (Switch) findViewById(R.id.button_vibrate);
        button_save = (Button) findViewById(R.id.button_save);
        button_exit = (Button) findViewById(R.id.button_exit);

        preferences = getSharedPreferences("apm_mode", MODE_PRIVATE);
        editor = preferences.edit();

        getActionBar().setTitle(preferences.getString("OperatingMode", null));
        Log.d("name",preferences.getString("OperatingMode",null));

        str = preferences.getString("OperatingMode", null);

        brightness_seekbar = (SeekBar)findViewById(R.id.brightness_seekbar);
        timeout_seekbar = (SeekBar)findViewById(R.id.timeout_seekbar);

        brightness = preferences.getInt(str + "brightness", 0);
        if (brightness == -1) {
            text_brightness.setText("Auto");
         //   brightness_seekbar.setProgress(-1);
        }
        else {
            text_brightness.setText(String.valueOf((brightness * 100 / 255) + "%"));
            brightness_seekbar.setProgress((brightness * 100 / 255));
        }

        timeout = preferences.getInt(str + "timeout", 15000);
        if (timeout <= 60000) {
            int range=0; // for timeout seekbar
            text_timeout.setText(String.valueOf((timeout / 1000)) + "s");
            switch (timeout){
                case 15000 : range = 1; break;
                case 30000 : range = 2; break;
                case 60000 : range = 3; break;
            }
            timeout_seekbar.setProgress(range*100/6);
        }
        else {
            int range=0; // for timeout seekbar
            text_timeout.setText(String.valueOf(timeout / 60000) + "mins");
            switch (timeout){
                case 300000 : range = 4; break;
                case 600000 : range = 5; break;
                case 1800000 : range = 6; break;
            }
            timeout_seekbar.setProgress(range*100/6);
        }

        data = preferences.getBoolean(str + "data", false);
        button_data.setChecked(data);
        wifi = preferences.getBoolean(str + "wifi", false);
        button_wifi.setChecked(wifi);
        bluetooth = preferences.getBoolean(str + "bluetooth", false);
        button_bluetooth.setChecked(bluetooth);
        silence = preferences.getBoolean(str + "silence", false);
        button_silence.setChecked(silence);
        vibrate = preferences.getBoolean(str + "vibrate", false);
        button_vibrate.setChecked(vibrate);

        brightness_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                brightness = (progress * 255) / 100;
                text_brightness.setText(String.valueOf(progress) + "%");
                mm.setBrightness(brightness);
            }
        });

        timeout_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                 if(progress<=100/6){
                     timeout = 15000; text_timeout.setText("15s");
                 }else if(progress>100/6 && progress<=200/6){
                     timeout = 30000; text_timeout.setText("30s");
                 }else if(progress>200/6 && progress<=300/6){
                     timeout = 60000; text_timeout.setText("60s");
                 }else if(progress>300/6 && progress<=400/6){
                     timeout = 300000; text_timeout.setText("5mins");
                 }else if(progress>400/6 && progress<=500/6){
                     timeout = 600000; text_timeout.setText("10mins");
                 }else{
                     timeout = 1800000; text_timeout.setText("30mins");
                 }
            }
        });

        button_data.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                button_data.setChecked(isChecked);
                data = isChecked;
            }
        });
        button_wifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                button_wifi.setChecked(isChecked);
                wifi = isChecked;
            }
        });
        button_bluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                button_bluetooth.setChecked(isChecked);
                bluetooth = isChecked;
            }
        });
        button_silence.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                button_silence.setChecked(isChecked);
                silence = isChecked;
            }
        });
        button_vibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                button_vibrate.setChecked(isChecked);
                vibrate = isChecked;
            }
        });
        button_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt(str + "brightness", brightness);
                editor.putInt(str + "timeout", timeout);
                editor.putBoolean(str + "data", data);
                editor.putBoolean(str + "wifi", wifi);
                editor.putBoolean(str + "bluetooth", bluetooth);
                editor.putBoolean(str + "silence", silence);
                editor.putBoolean(str + "vibrate", vibrate);
                if(add) {
                    int temp = preferences.getInt("mode_num", 0);
                    editor.putString("mode" + String.valueOf(temp), str);
                    editor.putInt("mode_num", temp + 1);
                    editor.putBoolean(str+"check",false);
                }
                editor.commit();
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        button_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
