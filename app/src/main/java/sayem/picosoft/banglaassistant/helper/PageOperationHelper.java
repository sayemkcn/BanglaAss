package sayem.picosoft.banglaassistant.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sayem.picosoft.banglaassistant.R;

/**
 * Created by sayemkcn on 4/1/16.
 */
public class PageOperationHelper {
    private Activity context = null;
    private View rootView = null;
    private ButtonListener listener = new ButtonListener();

    ToggleButton bluetoothButton;
    ToggleButton wifiButton;
    ToggleButton celNetworkButton;

    private PageOperationHelper() {
    }

    public static PageOperationHelper newInstance(Activity context, View rootView) {
        PageOperationHelper helper = new PageOperationHelper();
        helper.context = context;
        helper.rootView = rootView;
        return helper;
    }

    public void initAndSetOperationToolsPage() {
        this.bluetoothButton = (ToggleButton) rootView.findViewById(R.id.bluetoothButton);
        this.wifiButton = (ToggleButton) rootView.findViewById(R.id.wifiButton);
        this.celNetworkButton = (ToggleButton) rootView.findViewById(R.id.cellNetworkButton);

        // set defaults for buttons
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            bluetoothButton.setChecked(true);
        } else {
            bluetoothButton.setChecked(false);
        }

        this.setWiFiButtonDefaultState();

        if (this.isMobileDataEnabled()) {
            celNetworkButton.setChecked(true);
        } else {
            celNetworkButton.setChecked(false);
        }

        // set button actions
        this.bluetoothButton.setOnCheckedChangeListener(listener);
        this.wifiButton.setOnCheckedChangeListener(listener);
        this.celNetworkButton.setOnCheckedChangeListener(listener);
    }

    class ButtonListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        @Override
        public void onClick(View v) {
            int which = v.getId();
            switch (which) {

            }

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.bluetoothButton:
                    this.toggleBluetooth();
                    break;
                case R.id.wifiButton:
                    if (!this.isHotspotEnabled()) {
                        this.toggleWiFi();
                    } else {
                        setWiFiButtonDefaultState();
                        showToast("Wi-Fi hotspot enabled. can not complete operation.");
                    }
                    break;
                case R.id.cellNetworkButton:
                    context.startActivityForResult(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS), 0);

                    break;
            }
        }


        private boolean isHotspotEnabled() {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            try {
                int apState = (Integer) wifiManager.getClass().getDeclaredMethod("getWifiApState").invoke(wifiManager, null);
                if (apState == 13) {
                    return true;
                } else if (apState == 11) {
                    return false;
                }
            } catch (Exception e) {
                Log.d("WIFI-HOTSPOT", e.toString());
            }

            return false;
        }

        private void toggleWiFi() {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
                showToast("WiFi turned off!");
            } else if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
                showToast("WiFi turned on!");
            }
        }

        private void toggleBluetooth() {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
                bluetoothButton.setChecked(false);
                showToast("Blutooth turned off!");
            } else if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
                bluetoothButton.setChecked(true);
                showToast("Bluetooth turned on!");
            }
        }
    }

    private boolean isMobileDataEnabled() {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            Log.d("ENABLING_MOBILE_DATA", e.toString());
        }
        return mobileDataEnabled;
    }

    private void setWiFiButtonDefaultState() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiButton.setChecked(true);
        } else if (!wifiManager.isWifiEnabled()) {
            wifiButton.setChecked(false);
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
