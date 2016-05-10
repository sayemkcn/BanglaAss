package sayem.picosoft.banglaassistant.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sayem.picosoft.banglaassistant.R;
import sayem.picosoft.banglaassistant.commons.Pref;

/**
 * Created by sayemkcn on 4/1/16.
 */
public class PageOperationHelper {
    private Activity context = null;
    private View rootView = null;
    private ButtonListener listener = new ButtonListener();

    private ToggleButton bluetoothButton;
    private ToggleButton wifiButton;
    private ToggleButton celNetworkButton;
    private ToggleButton autoSyncButton;
    private ToggleButton locationButton;
    private ToggleButton autoRotateScreenButton;
    private Button touchSettingsButton;
    private ToggleButton airplaneModeButton;
    private Button adjustBrightnessButton;
    private Button sleepScreenButton;
    private Button volumeSettingButton;
    private Button phoneRingtoneButton;

    // For alert Dialog | brightness dialog
    View dialogView;
    CheckBox checkBox;
    SeekBar seekBar;
    boolean isBrightnessModeAutomatic = false;

    int mScreenTimeout = 0;

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
        this.autoSyncButton = (ToggleButton) rootView.findViewById(R.id.autoSyncButton);
        this.locationButton = (ToggleButton) rootView.findViewById(R.id.locationButton);
        this.autoRotateScreenButton = (ToggleButton) rootView.findViewById(R.id.autoRotateScreenButton);
        this.touchSettingsButton = (Button) rootView.findViewById(R.id.touchSettingsButton);
        this.airplaneModeButton = (ToggleButton) rootView.findViewById(R.id.airplaneModeButton);
        this.adjustBrightnessButton = (Button) rootView.findViewById(R.id.adjustBrightnessButton);
        this.sleepScreenButton = (Button) rootView.findViewById(R.id.screenSleepTimeButton);
        this.volumeSettingButton = (Button) rootView.findViewById(R.id.volumeSettingButton);
        this.phoneRingtoneButton = (Button) rootView.findViewById(R.id.phoneRingtoneButton);


        // SET DEFAULTS FOR BUTTONS
        // bluetooth
        this.bluetoothButton.setChecked(BluetoothAdapter.getDefaultAdapter().isEnabled());
        // wifi
        this.setWiFiButtonDefaultState();
        // mobile data
        this.celNetworkButton.setChecked(this.isMobileDataEnabled());
        // auto sync
        this.autoSyncButton.setChecked(ContentResolver.getMasterSyncAutomatically());
        // location
        this.locationButton.setChecked(((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER));
        // auto rotate screen
        if (android.provider.Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1)
            this.autoRotateScreenButton.setChecked(true);
        else
            this.autoRotateScreenButton.setChecked(false);
        // airplane mode
        this.airplaneModeButton.setChecked(Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 1) != 0);


        // set button actions
        this.bluetoothButton.setOnCheckedChangeListener(listener);
        this.wifiButton.setOnCheckedChangeListener(listener);
        this.celNetworkButton.setOnCheckedChangeListener(listener);
        this.autoSyncButton.setOnCheckedChangeListener(listener);
        this.locationButton.setOnCheckedChangeListener(listener);
        this.autoRotateScreenButton.setOnCheckedChangeListener(listener);
        this.touchSettingsButton.setOnClickListener(listener);
        this.airplaneModeButton.setOnCheckedChangeListener(listener);
        this.adjustBrightnessButton.setOnClickListener(listener);
        this.sleepScreenButton.setOnClickListener(listener);
        this.volumeSettingButton.setOnClickListener(listener);
        this.phoneRingtoneButton.setOnClickListener(listener);
    }

    // Listener object for All of the tools button
    class ButtonListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,SeekBar.OnSeekBarChangeListener {

        private View volumeView;
        private SeekBar ringerVolumeSeekbar;
        private SeekBar notificationVolumeSeekbar;
        private SeekBar mediaVolumeSeekbar;
        private SeekBar alarmVolumeSeekbar;
        private SeekBar voiceCallVolumeSeekbar;
        private SeekBar systemVolumeSeekbar;
        private TextView ringerVolumeTextView;
        private TextView notificationVolumeTextView;
        private TextView mediaVolumeTextView;
        private TextView alarmVolumeTextView;
        private TextView voiceCallVolumeTextView;
        private TextView systemVolumeTextView;
        AudioManager audioManager;

        @Override
        public void onClick(View v) {
            int which = v.getId();
            switch (which) {
                case R.id.touchSettingsButton:
                    if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP))
                        showTouchSettingDialog();
                    break;
                case R.id.adjustBrightnessButton:
                    try {
                        showAdjustBrightnessWindow();
                    } catch (Settings.SettingNotFoundException e) {
                        Log.d("SETTINGS_EX", e.toString());
                    }
                    break;
                case R.id.screenSleepTimeButton:
                    showSleepTimeDialog();
                    break;
                case R.id.volumeSettingButton:
                    showVolumeDialog();
                    break;
                case R.id.phoneRingtoneButton:
                    break;
            }

        }

        private void showVolumeDialog() {

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            View volumeView = LayoutInflater.from(context).inflate(R.layout.volume_settings_layout, null);
            this.ringerVolumeSeekbar = (SeekBar) volumeView.findViewById(R.id.ringerVolumeSeekbar);
            this.notificationVolumeSeekbar = (SeekBar) volumeView.findViewById(R.id.notificationVolumeSeekbar);
            this.mediaVolumeSeekbar = (SeekBar) volumeView.findViewById(R.id.mediaVolumeSeekbar);
            this.alarmVolumeSeekbar = (SeekBar) volumeView.findViewById(R.id.alarmVolumeSeekbar);
            this.voiceCallVolumeSeekbar = (SeekBar) volumeView.findViewById(R.id.voiceCallVolumeSeekbar);
            this.systemVolumeSeekbar = (SeekBar) volumeView.findViewById(R.id.systemVolumeSeekbar);

            this.ringerVolumeTextView = (TextView) volumeView.findViewById(R.id.ringerVolumeTextView);
            this.notificationVolumeTextView = (TextView) volumeView.findViewById(R.id.notificationVolumeTextView);
            this.mediaVolumeTextView = (TextView) volumeView.findViewById(R.id.mediaVolumeTextView);
            this.alarmVolumeTextView = (TextView) volumeView.findViewById(R.id.alarmVolumeTextView);
            this.voiceCallVolumeTextView = (TextView) volumeView.findViewById(R.id.voiceCallVolumeTextView);
            this.systemVolumeTextView = (TextView) volumeView.findViewById(R.id.systemVolumeTextView);

            this.ringerVolumeSeekbar.setOnSeekBarChangeListener(listener);
            this.notificationVolumeSeekbar.setOnSeekBarChangeListener(listener);
            this.mediaVolumeSeekbar.setOnSeekBarChangeListener(listener);
            this.alarmVolumeSeekbar.setOnSeekBarChangeListener(listener);
            this.voiceCallVolumeSeekbar.setOnSeekBarChangeListener(listener);
            this.systemVolumeSeekbar.setOnSeekBarChangeListener(listener);

            ringerVolumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING)*14);
            notificationVolumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)*14);
            mediaVolumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)*7);
            alarmVolumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_ALARM)*14);
            voiceCallVolumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)*20);
            systemVolumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)*14);

            new AlertDialog.Builder(context)
                    .setTitle("Volume Settings")
                    .setView(volumeView)
                    .setPositiveButton("Ok", null)
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int id = seekBar.getId();
            int volume = progress/14;
            switch (id){
                case R.id.ringerVolumeSeekbar:
                    audioManager.setStreamVolume(AudioManager.STREAM_RING,volume,AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                    Log.d("RINGER_VOLUME",volume+"");
                    break;
                case R.id.notificationVolumeSeekbar:
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,volume,0);
                    Log.d("NOTIFICATION_VOLUME",progress/14+"");
                    break;
                case R.id.mediaVolumeSeekbar:
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume*2,0);
                    Log.d("MEDIA_VOLUME",(progress*2)/14+"");
                    break;
                case R.id.alarmVolumeSeekbar:
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM,volume,0);
                    Log.d("ALARM_VOLUME",progress/14+"");
                    break;
                case R.id.voiceCallVolumeSeekbar:
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,volume,0);
                    Log.d("VOICE_CALL_VOLUME",progress/5+"");
                    break;
                case R.id.systemVolumeSeekbar:
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,volume,0);
                    Log.d("SYSTEM_VOLUME",progress/14+"");
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }


        private void showSleepTimeDialog() {
            final CharSequence[] items = {"6 seconds", "15 seconds", "30 seconds", "1 minute", "2 minutes", "5 minutes", "10 minutes", "15 minutes", "30 minutes", "Never Timeout"};
            int selectedIndex = 0;
            String indexString = Pref.getPreferenceData(context, "screen_timeout_selected_index");
            if (indexString != null && !indexString.equals("")) {
                selectedIndex = Integer.parseInt(indexString);
            }
            new AlertDialog.Builder(context)
                    .setTitle("Sleep")
                    .setSingleChoiceItems(items, selectedIndex, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            int timeout = 0;
                            switch (which) {
                                case 0:
                                    mScreenTimeout = 6000;
                                    break;
                                case 1:
                                    mScreenTimeout = 15000;
                                    break;
                                case 2:
                                    mScreenTimeout = 30000;
                                    break;
                                case 3:
                                    mScreenTimeout = 60000;
                                    break;
                                case 4:
                                    mScreenTimeout = 120000;
                                    break;
                                case 5:
                                    mScreenTimeout = 300000;
                                    break;
                                case 6:
                                    mScreenTimeout = 600000;
                                    break;
                                case 7:
                                    mScreenTimeout = 900000;
                                    break;
                                case 8:
                                    mScreenTimeout = 1800000;
                                    break;
                                case 9:
                                    mScreenTimeout = -1;
                                    break;
                            }
                            dialog.dismiss();
                            Pref.savePreference(context, "screen_timeout_selected_index", which + "");
                            Toast.makeText(context, "Screen sleep time has been set to " + items[which], Toast.LENGTH_SHORT).show();
                            android.provider.Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, mScreenTimeout);
                        }
                    })
                    .show();

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
                        showToast("You have turn of Wi-Fi hotspot first!!");
                    }
                    break;
                case R.id.cellNetworkButton:
                    context.startActivityForResult(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS), 0);
                    break;

                case R.id.autoSyncButton:
                    boolean sync = ContentResolver.getMasterSyncAutomatically();
                    ContentResolver.setMasterSyncAutomatically(!sync);
                    break;
                case R.id.locationButton:
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                    break;
                case R.id.autoRotateScreenButton:
                    setAutoOrientationEnabled(context, !(android.provider.Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1));
                    break;
                case R.id.airplaneModeButton:
                    Intent airplaneModeSetting = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    airplaneModeSetting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(airplaneModeSetting);
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
            wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
            if (wifiManager.isWifiEnabled())
                showToast("WiFi turned off!");
            else
                showToast("WiFi turned on!");
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

    public static void setAutoOrientationEnabled(Context context, boolean enabled) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }

    private void showTouchSettingDialog() {
        // dial pad touch tone
        boolean isDtmfToneEnabled = Settings.System.getInt(context.getContentResolver(),
                Settings.System.DTMF_TONE_WHEN_DIALING, 1) != 0;
        // touch sound
        boolean isTouchSoundsEnabled = Settings.System.getInt(context.getContentResolver(),
                Settings.System.SOUND_EFFECTS_ENABLED, 1) != 0;
        // Lock screen sound
        boolean isLockScreenSoundsEnabled = Settings.System.getInt(context.getContentResolver(),
                "lockscreen_sounds_enabled", 1) != 0;
        // haptic feedback / vibrate on keypress
        boolean isVibrateOnTouchEnabled = Settings.System.getInt(context.getContentResolver(),
                Settings.System.HAPTIC_FEEDBACK_ENABLED, 1) != 0;

        boolean[] enabledSetting = new boolean[]{isDtmfToneEnabled, isTouchSoundsEnabled, isLockScreenSoundsEnabled, isVibrateOnTouchEnabled};
        List<Integer> enabledIndexList = new ArrayList<>(enabledSetting.length);
        // populate list with setting index that is enabled
        // use it on items callback to set default selected items
        for (int i = 0; i < enabledSetting.length; i++) {
            if (enabledSetting[i]) {
                enabledIndexList.add(i);
            }
        }

        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.title("Touch Settings");
        dialog.items(R.array.touchSettingsNameArray);
        dialog.itemsCallbackMultiChoice(convertIntegers(enabledIndexList), new MaterialDialog.ListCallbackMultiChoice() {
            @Override
            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                // enable settings that are selected
                boolean[] enableDisableSetting = new boolean[]{false, false, false, false};
                // covert integer array to boolean array
                // integer number is boolean index, false other than those indexes
                for (int i = 0; i < which.length; i++) {
                    enableDisableSetting[which[i]] = true;
                }
                // enable settings for true indexes
                for (int i = 0; i < enableDisableSetting.length; i++) {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.DTMF_TONE_TYPE_WHEN_DIALING, enableDisableSetting[0] ? 1 : 0);
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, enableDisableSetting[1] ? 1 : 0);
                    Settings.System.putInt(context.getContentResolver(), "lockscreen_sounds_enabled", enableDisableSetting[2] ? 1 : 0);
                    Settings.System.putInt(context.getContentResolver(), Settings.System.HAPTIC_FEEDBACK_ENABLED, enableDisableSetting[3] ? 1 : 0);
                }

                return true;
            }
        });
        dialog.positiveText("OK");
        dialog.show();
    }

    // convert List to Integer array
    public static Integer[] convertIntegers(List<Integer> integers) {
        Integer[] ret = new Integer[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private int boolToInt(boolean bool) {
        if (bool) {
            return 1;
        } else {
            return 0;
        }
    }

    private void showAdjustBrightnessWindow() throws Settings.SettingNotFoundException {
        this.dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null);
        this.checkBox = (CheckBox) dialogView.findViewById(R.id.autoBrightnessCheckBox);
        this.seekBar = (SeekBar) dialogView.findViewById(R.id.brightnessSeekbar);

        // check if already set to automatic
        this.isBrightnessModeAutomatic = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        this.checkBox.setChecked(isBrightnessModeAutomatic);
        // seekbar default when opening view
        this.seekBar.setEnabled(!isBrightnessModeAutomatic);
        this.seekBar.setProgress(Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) / 2);
//        Log.d("BRIGHTNESS LEVEL", Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) + "");


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                seekBar.setEnabled(!isChecked);
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, isChecked ? 1 : 0);
                isBrightnessModeAutomatic = isChecked;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.System.putInt(context.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, progress * 2);
//                Log.d("SEEKBAR_VALUE", progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Adjust screen brightness");
        dialog.setView(dialogView);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
}
