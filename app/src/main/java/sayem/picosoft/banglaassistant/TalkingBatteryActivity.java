package sayem.picosoft.banglaassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class TalkingBatteryActivity extends AppCompatActivity {

    private InterstitialAd interstitial;

    private Spinner languageSpinner;
    private Spinner accentSpinner;
    private CheckBox voiceCheckBox;
    private CheckBox batteryLevelShowCheckBox;
    private CheckBox usbConnectedCheckBox;
    private CheckBox chargerConnectedCheckBox;
    private CheckBox batteryLowCheckBox;
    private SeekBar volumeSeekBar;
    private boolean isFirstRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talking_battery);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getPreferenceData("language")==null){
            isFirstRun = true;
        }

        languageSpinner = (Spinner) this.findViewById(R.id.languageSpinner);
        accentSpinner = (Spinner) this.findViewById(R.id.accentSpinner);
        voiceCheckBox = (CheckBox) this.findViewById(R.id.voiceCheckBox);
        batteryLevelShowCheckBox = (CheckBox) this.findViewById(R.id.statusBarNotificationCheckBox);
        usbConnectedCheckBox = (CheckBox) this.findViewById(R.id.usbConnectedCheckBox);
        chargerConnectedCheckBox = (CheckBox) this.findViewById(R.id.chargerConnectedCheckBox);
        batteryLowCheckBox = (CheckBox) this.findViewById(R.id.batteryLowCheckBox);
        volumeSeekBar = (SeekBar) this.findViewById(R.id.seekBar);

        // Load Ads
        AdView mAdView = (AdView) this.findViewById(R.id.homeAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getResources().getString(R.string.deviceId))
                .build();
        mAdView.loadAd(adRequest);
        // End Ad Loading

        // Interstitial ad load

        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.interstitialAdUnitId));

        // Create ad request.
        AdRequest intAdRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getResources().getString(R.string.deviceId))
                .build();

        // Begin loading your interstitial.
        interstitial.loadAd(intAdRequest);

        // End Loading Interstitial

        // set language spinner array adapter.
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        // set language spinner preferences
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savePreference("language", parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                savePreference("language", "Bangla");
            }
        });

        // set accent spinner array adapter
        ArrayAdapter<CharSequence> accentAdapter = ArrayAdapter.createFromResource(this, R.array.accent, android.R.layout.simple_spinner_dropdown_item);
        accentSpinner.setAdapter(accentAdapter);
        // set accennt spinner preferences
        accentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savePreference("accent", parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                savePreference("accent", "Male");
            }
        });

        // set checkbox preferences.


        voiceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    savePreference("voice", "yes");
                } else {
                    savePreference("voice", "no");
                }
                displayInterstitial();
            }
        });
        batteryLevelShowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    savePreference("battery_show", "yes");
                } else {
                    savePreference("battery_show", "no");
                }
                displayInterstitial();
            }
        });
        usbConnectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    savePreference("usb_connected", "yes");
                } else {
                    savePreference("usb_connected", "no");
                }
                displayInterstitial();
            }
        });
        chargerConnectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    savePreference("charger_connected", "yes");
                } else {
                    savePreference("charger_connected", "no");
                }
                displayInterstitial();
            }
        });
        batteryLowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    savePreference("battery_low", "yes");
                } else {
                    savePreference("battery_low", "no");
                }
                displayInterstitial();
            }
        });

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Toast.makeText(getApplicationContext(), progress + "", Toast.LENGTH_SHORT).show();
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress / 6, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });


        // set previous preferences on ui when creating activity.
        if (getPreferenceData("language").equals("English")) {
            languageSpinner.setSelection(1);
        }
        if (getPreferenceData("accent").equals("Male")) {
            accentSpinner.setSelection(1);
        }else if(getPreferenceData("accent").equals("Chittagong Native")){
            accentSpinner.setSelection(2);
        }
        if (getPreferenceData("voice").equals("no")) {
            voiceCheckBox.setChecked(false);
        } else if (voiceCheckBox.isChecked()) {
            savePreference("voice", "yes");
        }
        if (getPreferenceData("battery_show").equals("no")) {
            batteryLevelShowCheckBox.setChecked(false);
        } else if (batteryLevelShowCheckBox.isChecked()) {
            savePreference("battery_low", "yes");
        }
        if (getPreferenceData("usb_connected").equals("no")) {
            usbConnectedCheckBox.setChecked(false);
        } else if (usbConnectedCheckBox.isChecked()) {
            savePreference("usb_connected", "yes");
        }
        if (getPreferenceData("charger_connected").equals("no")) {
            chargerConnectedCheckBox.setChecked(false);
        } else if (chargerConnectedCheckBox.isChecked()) {
            savePreference("charger_connected", "yes");
        }
        if (getPreferenceData("battery_low").equals("no")) {
            batteryLowCheckBox.setChecked(false);
        }
        AudioManager systemAudioVolume = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = systemAudioVolume.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setProgress(currentVolume * 6);

        // finish this activity if first run
        if (isFirstRun){
            this.finish();
        }
    }

    private void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void savePreference(String key, String value) {
        SharedPreferences sharedPref = getSharedPreferences("BTBatteryForAssistant", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();

        prefEditor.putString(key, value);
        prefEditor.apply();

    }

    private String getPreferenceData(String key) {
        SharedPreferences sharedPref = getSharedPreferences("BTBatteryForAssistant", Context.MODE_PRIVATE);

        String data = sharedPref.getString(key, "");
        return data;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome Bangla Talking Battery Application http://goo.gl/6Vwh0v");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share this App"));

        } else if (id == R.id.action_about) {
            new MaterialDialog.Builder(TalkingBatteryActivity.this)
                    .iconRes(R.mipmap.ic_launcher)
                    .title(R.string.app_name)
                    .content("Developer:\nSayem Hossain\n\nDeveloper site:\nhttp://www.ekushay.com\n\nEmail:\nsayem@ekushay.com\n\nSpecial Credits(vocal):\nRahnuma Nishat(Standard Female)\nSifat Shaharear(Standard Male)\nArif Rafsan(Chittagong Native),")
                    .positiveText("Okay")
                    .negativeText("Contact Developer")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dev_url)));
                            startActivity(browserIntent);
                        }
                    })
                    .show();
        } else if (id == R.id.action_like_page) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.fb_page_link)));
            startActivity(browserIntent);
        } else if (id == R.id.action_rate) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.app_link)));
            startActivity(browserIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
