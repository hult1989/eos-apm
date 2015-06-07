package hult.netlab.pku.apmpowermanager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.content.*;

public class modemanager{
	private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter;
    private AudioManager audioManager;
    private Context context;
    
    public modemanager(Context context){
    	this.context = context;
    }
    
    public void setAll(int bright, int timeout, boolean data, boolean wifi, boolean bluetooth, 
    			boolean silence, boolean vibrate){
    	setBrightness(bright);
    	setTimeout(timeout);
    	setData(data);
    	setWifi(wifi);
    	setBluetooth(bluetooth);
    	if(vibrate)
    		setVibrate();
    	if(silence)
    		setSilence();
    }
    //Brightness
    public int getBrightness() throws SettingNotFoundException{
    	return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
    }
    public void setBrightness(int brightness){
    	Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 
    			brightness);
    	Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 
        		Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }
    public void setBrightnessAuto(){
    	Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 
        		Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }
    //Timeout
    public int getTimeout() throws SettingNotFoundException{
    	return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
    }
    public void setTimeout(int time){
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, time*1000);
        return;
    }
    //Data-3g
    public boolean isDataEnabled(){
    	return true;
    }
    public void setData(boolean judge){
    	final String TAG = "setMobileDataEnabled";
    	final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
   	 	Class conmanClass;
   	 	try {
   	 		conmanClass = Class.forName(conman.getClass().getName());
   	 		final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
   	 		iConnectivityManagerField.setAccessible(true);
   	 		final Object iConnectivityManager = iConnectivityManagerField.get(conman);
   	 		final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
   	 		final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
   	 		setMobileDataEnabledMethod.setAccessible(true);
   	 		setMobileDataEnabledMethod.invoke(iConnectivityManager, judge);
   	 		
   	 	} catch (ClassNotFoundException e) {
    	 // TODO Auto-generated catch block
   	 		Log.d(TAG, "ClassNotFoundException");
    	 } catch (NoSuchFieldException e) {
    		 Log.d(TAG, "NoSuchFieldException");
    	 } catch (IllegalArgumentException e) {
    		 Log.d(TAG, "IllegalArgumentException");
    	 } catch (IllegalAccessException e) {
    		 Log.d(TAG, "IllegalAccessException");
    	 } catch (NoSuchMethodException e) {
    		 Log.d(TAG, "NoSuchMethodException");
    	 } catch (InvocationTargetException e) {
    		 Log.d(TAG, "InvocationTargetException");
    	 }finally{}
    }
    //Wifi
    public boolean isWifiEnabled(){
    	wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	if(wifiManager == null)
    		return false;
    	return wifiManager.isWifiEnabled();
    }
    public void setWifi(boolean judge){
    	wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	if(wifiManager == null)
    		return;
    	if(judge)
    		wifiManager.setWifiEnabled(true);
    	else
    		wifiManager.setWifiEnabled(false);
    }
    //Bluetooth
    public boolean isBluetoothEnabled(){
    	bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if(bluetoothAdapter == null)
    		return false;
    	return bluetoothAdapter.isEnabled();
    }
    public void setBluetooth(boolean judge){
    	bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
     	if(bluetoothAdapter == null)
     		return;
    	if(judge)
       		bluetoothAdapter.enable();
       	else
       		bluetoothAdapter.disable();
       	return;
	}
    //silence
    public boolean isSilence(){
    	audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    	return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }
    public void setSilence(){
    	audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    	audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }
    //vibration
    public boolean isVibrate(){
    	audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    	return audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
    }
    public void setVibrate(){
    	audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    	audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
}

/*
<uses-permission android:name="android.permission.READ_SETTINGS" />
<uses-permission android:name="android.permission.WRITE_SETTINGS" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" /> 
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.VIBRATE"/> 
<uses-permission android:name="android.permission.INTERNET" />
*/
