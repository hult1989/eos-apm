package grace.pku.apm;

import com.newer.t4.demo.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DemoActivity extends Activity {

	/** Called when the activity is first created. */
	private ImageView image;// 电池状态图片
	private TextView textCD;// 电池充电状态
	private TextView textRL;// 电池剩余容量
	private TextView textZT;// 电池状态
	private TextView textDY;// 电池电压mV
	private TextView textWD;// 电池温度
	private TextView textLX;// 电池类型
	private BroadcastReceiver myBroadcastReciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 电池剩余容量
			int level = (int) (intent
					.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
					/ (float) intent.getIntExtra(BatteryManager.EXTRA_SCALE,
							100) * 100);
			textRL.setText(level + "%");

			// 电池当前使用状态
			image.setImageResource(intent.getIntExtra(
					BatteryManager.EXTRA_ICON_SMALL, 0));
			switch (intent.getIntExtra(BatteryManager.EXTRA_STATUS, 1)) {
			case BatteryManager.BATTERY_STATUS_CHARGING:
				if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 1) == BatteryManager.BATTERY_PLUGGED_AC)
					textCD.setText("使用充电器充电中");
				else
					textCD.setText("使用USB充电中");
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				textCD.setText("放电中");
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				textCD.setText("已充满");
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				textCD.setText("未充满");
				break;
			}
			// 电池状态
			switch (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 1)) {
			case BatteryManager.BATTERY_HEALTH_DEAD:
				textZT.setText("电池已损坏！");
				textZT.setTextColor(Color.RED);
				break;
			case BatteryManager.BATTERY_HEALTH_GOOD:
				textZT.setText("健康");
				break;
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				textZT.setText("电压过高");
				break;
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				textZT.setText("温度过高");
				break;
			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
				textZT.setText("未知");
				break;
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
				textZT.setText("未知故障");
				break;
			}
			// 电池电压
			textDY.setText(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 1)+"mV");
			// 电池温度
			textWD.setText((intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 1)/10.0)+"℃");
			// 电池类型
			textLX.setText(intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textCD = (TextView) findViewById(R.id.textCD);
		textRL = (TextView) findViewById(R.id.textRL);
		textZT = (TextView) findViewById(R.id.textZT);
		textDY = (TextView) findViewById(R.id.textDY);
		textWD = (TextView) findViewById(R.id.textWD);
		textLX = (TextView) findViewById(R.id.textLX);
		image = (ImageView) findViewById(R.id.imageView1);
		registerReceiver(myBroadcastReciver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myBroadcastReciver);
	}
}