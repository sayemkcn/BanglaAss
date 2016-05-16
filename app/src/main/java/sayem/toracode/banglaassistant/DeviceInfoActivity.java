package sayem.toracode.banglaassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import sayem.toracode.banglaassistant.commons.DeviceUtilities;

public class DeviceInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView androidIdTv = (TextView) this.findViewById(R.id.androidIdTextView);
        TextView karnelVersionTv = (TextView) this.findViewById(R.id.karnelVersionTextView);
        TextView modelTv = (TextView) this.findViewById(R.id.modelTextView);
        TextView osTv = (TextView) this.findViewById(R.id.osTextView);
        TextView releaseTv = (TextView) this.findViewById(R.id.releaseTextView);
        TextView deviceTv = (TextView) this.findViewById(R.id.deviceTextView);
        TextView brandModelTv = (TextView) this.findViewById(R.id.brandModelTextView);
        TextView productTv = (TextView) this.findViewById(R.id.productTextView);
        TextView displayTv = (TextView) this.findViewById(R.id.displayTextView);
        TextView unknownTv = (TextView) this.findViewById(R.id.unknownTextView);
        TextView hardwareTv = (TextView) this.findViewById(R.id.hardwareTextView);
        TextView deviceIdTv = (TextView) this.findViewById(R.id.deviceId);
        TextView manufacturerSerialTv = (TextView) this.findViewById(R.id.manufacturerSerialTextView);
        TextView userTv = (TextView) this.findViewById(R.id.userTextView);
        TextView hostTv = (TextView) this.findViewById(R.id.hostTextView);

        androidIdTv.setText(DeviceUtilities.getAndroidId(this));
        if (!DeviceUtilities.readKernelVersion().contains("ERROR"))
            karnelVersionTv.setText(DeviceUtilities.readKernelVersion());
        modelTv.setText(DeviceUtilities.getDeviceModelNumber());
        osTv.setText(DeviceUtilities.OSNAME + " " + DeviceUtilities.OSVERSION);
        releaseTv.setText(DeviceUtilities.RELEASE);
        deviceTv.setText(DeviceUtilities.DEVICE);
        brandModelTv.setText(DeviceUtilities.BRAND + "/" + DeviceUtilities.MODEL);
        productTv.setText(DeviceUtilities.PRODUCT);
        displayTv.setText(DeviceUtilities.DISPLAY);
        unknownTv.setText(DeviceUtilities.UNKNOWN);
        hardwareTv.setText(DeviceUtilities.HARDWARE);
        deviceIdTv.setText(DeviceUtilities.ID);
        manufacturerSerialTv.setText(DeviceUtilities.MANUFACTURER);
        userTv.setText(DeviceUtilities.USER);
        hostTv.setText(DeviceUtilities.HOST);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return true;
    }
}
