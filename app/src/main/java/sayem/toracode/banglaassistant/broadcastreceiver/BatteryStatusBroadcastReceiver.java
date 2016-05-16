package sayem.toracode.banglaassistant.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.BatteryManager;

import sayem.toracode.banglaassistant.R;

/**
 * Created by sayemkcn on 5/10/16.
 */

public class BatteryStatusBroadcastReceiver extends BroadcastReceiver {
    public BatteryStatusBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Check Battery Full Status
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isBatteryFull = status == BatteryManager.BATTERY_STATUS_FULL;
        // How are we get charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        // Action from intent filter defined on manifest
        String action = intent.getAction();

        String languagePref = getPreference(context, "language");
        String accentPref = getPreference(context, "accent");
        String usbConnectedPref = getPreference(context, "usb_connected");
        String chargerConnectedPref = getPreference(context, "charger_connected");
        String voiceEnabledPref = getPreference(context, "voice");


        switch (accentPref) {
            case "Male":

                if (languagePref.equals("Bangla")) {

                    if (isBatteryFull) {
                        if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Full! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_bn_standard_battery_full).start();
                        }
                    } else if (usbCharge) {
                        if (usbConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "USB Charger Connected! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_bn_standard_usb_connected).start();
                        }
                    } else if (acCharge) {
                        if (chargerConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "AC Charger Connected! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_bn_standard_charger_connected).start();
                        }
                    } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                        if ((usbConnectedPref.equals("yes") || chargerConnectedPref.equals("yes")) && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Charger Disonnected! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_bn_standard_charger_disconnected).start();
                        }
                    }

                    if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                        if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Low! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_bn_standard_battery_low).start();
                        }
                    }


                } else if (languagePref.equals("English")) {

                    if (isBatteryFull) {
                        if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Full! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_en_standard_battery_full).start();
                        }
                    } else if (usbCharge) {
                        if (usbConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "USB Charger Connected! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_en_standard_usb_connected).start();
                        }
                    } else if (acCharge) {
                        if (chargerConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "AC Charger Connected! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_en_standard_charger_connected).start();
                        }
                    } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                        if ((usbConnectedPref.equals("yes") || chargerConnectedPref.equals("yes")) && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Charger Disonnected! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_en_standard_charger_disconnected).start();
                        }
                    }

                    if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                        if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Low! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.male_en_standard_battery_low).start();
                        }
                    }

                }
                break;
            case "Female":
                if (languagePref.equals("Bangla")) {

                    if (isBatteryFull) {
                        if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Full! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_bettery_full_bn).start();
                        }
                    } else if (usbCharge) {
                        if (usbConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "USB Charger Connected! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_usb_connected_bn).start();
                        }
                    } else if (acCharge) {
                        if (chargerConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "AC Charger Connected! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_charger_connected_bn).start();
                        }
                    } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                        if ((usbConnectedPref.equals("yes") || chargerConnectedPref.equals("yes")) && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Charger Disonnected! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_charger_disconnected_bn).start();
                        }
                    }

                    if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                        if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Low! Bangla", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_bettery_low_bn).start();
                        }
                    }


                } else if (languagePref.equals("English")) {

                    if (isBatteryFull) {
                        if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Full! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_bettery_full_en).start();
                        }
                    } else if (usbCharge) {
                        if (usbConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "USB Charger Connected! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_usb_connected_en).start();
                        }
                    } else if (acCharge) {
                        if (chargerConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "AC Charger Connected! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_charger_connected_en).start();
                        }
                    } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                        if ((usbConnectedPref.equals("yes") || chargerConnectedPref.equals("yes")) && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Charger Disonnected! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_charger_disconnected_en).start();
                        }
                    }

                    if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                        if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Low! English", Toast.LENGTH_SHORT).show();
                            MediaPlayer.create(context, R.raw.female_battery_low_en).start();
                        }
                    }

                }
                break;
            case "Chittagong Native":
                if (isBatteryFull) {
                    if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Full! Bangla", Toast.LENGTH_SHORT).show();
                        MediaPlayer.create(context, R.raw.male_chitt_battery_full).start();
                    }
                } else if (usbCharge) {
                    if (usbConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "USB Charger Connected! Bangla", Toast.LENGTH_SHORT).show();
                        MediaPlayer.create(context, R.raw.male_chitt_usb_connected).start();
                    }
                } else if (acCharge) {
                    if (chargerConnectedPref.equals("yes") && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "AC Charger Connected! Bangla", Toast.LENGTH_SHORT).show();
                        MediaPlayer.create(context, R.raw.male_chitt_charger_connected).start();
                    }
                } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                    if ((usbConnectedPref.equals("yes") || chargerConnectedPref.equals("yes")) && voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Charger Disonnected! Bangla", Toast.LENGTH_SHORT).show();
                        MediaPlayer.create(context, R.raw.male_chitt_charger_disconnected).start();
                    }
                }

                if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                    if (voiceEnabledPref.equals("yes")) {
//                    Toast.makeText(context, "Battery Low! Bangla", Toast.LENGTH_SHORT).show();
                        MediaPlayer.create(context, R.raw.male_chitt_battery_low).start();
                    }
                }
                break;
            default:
                break;
        }


//        if (chargerConnectedPref.equals("yes")) {
//
//            if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
//                if (languagePref.equals("Bangla")) {
//                    Toast.makeText(context, "Charger Connected! Bangla", Toast.LENGTH_SHORT).show();
//                } else if (languagePref.equals("English")) {
//                    Toast.makeText(context, "Charger Connected! English", Toast.LENGTH_SHORT).show();
//                }
//
//            } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
//
//                if (languagePref.equals("Bangla")) {
//                    Toast.makeText(context, "Charger Disconnected! Bangla", Toast.LENGTH_SHORT).show();
//                } else if (languagePref.equals("English")) {
//                    Toast.makeText(context, "Charger Disconnected! English", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }


    }

    private String getPreference(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("BTBatteryForAssistant", Context.MODE_PRIVATE);

        String pref = sharedPref.getString(key, "");
        return pref;
    }
}
