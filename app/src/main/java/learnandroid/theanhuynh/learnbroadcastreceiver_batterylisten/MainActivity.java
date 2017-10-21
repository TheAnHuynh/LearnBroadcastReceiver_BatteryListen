package learnandroid.theanhuynh.learnbroadcastreceiver_batterylisten;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtBatteryStatus, txtScale, txtChargeStatus;
    ConstraintLayout loBatteryListener;
    Button btnThoat;
    BroadcastReceiver batteryStatusListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO: Hiển thị % Pin
            int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = batteryLevel / (float)scale;


            txtBatteryStatus.setText(batteryLevel + "%");
            txtScale.setText("Scale: " + scale + " (battertyPct:" + batteryPct + ")");
            // TODO: Hiển thị trạng thái sạc pin
            String batteryChargeStatus = "Charge Status:";
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            // How are we charging?
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            if(isCharging){
                loBatteryListener.setBackgroundColor(Color.rgb(0,191,255));
                batteryChargeStatus += "Charging";
                if (usbCharge){
                    batteryChargeStatus += " USB";
                }else{
                    if (acCharge){
                        batteryChargeStatus += " AC";
                    }
                }

                if(status == BatteryManager.BATTERY_STATUS_FULL){
                    batteryChargeStatus += " (FULL)";
                }
            }
            else{
                loBatteryListener.setBackgroundColor(Color.rgb(173,255,47));
                batteryChargeStatus += "not charge";
            }

            if (batteryLevel < 30) {
                loBatteryListener.setBackgroundColor(Color.RED);
            }
            txtChargeStatus.setText(batteryChargeStatus);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtBatteryStatus = (TextView) findViewById(R.id.txtBatteryStatus);
        txtScale = (TextView) findViewById(R.id.txtScale);
        txtChargeStatus = (TextView) findViewById(R.id.txtCharge);
        loBatteryListener = (ConstraintLayout) findViewById(R.id.loBatteryListener);
        btnThoat = (Button) findViewById(R.id.btnThoat);
    }

    private void addEvents() {
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        registerReceiver(batteryStatusListener, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(batteryStatusListener);
    }
}
