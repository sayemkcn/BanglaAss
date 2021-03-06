package sayem.toracode.banglaassistant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import sayem.toracode.banglaassistant.helper.PageProcessHelper;

public class TransparentActivity extends Activity {
    private static final String KILL_ACTION = "kill_process";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (getIntent().getAction()) {
            case KILL_ACTION:
                try {
                    this.killBackgroundProcess(TransparentActivity.this);
                } catch (InterruptedException e) {
                    Log.e("KILL PROCESS", e.toString());
                }

                break;
        }

    }

    private void killBackgroundProcess(TransparentActivity transparentActivity) throws InterruptedException {
//        final ProgressDialog progressDialog = new ProgressDialog(TransparentActivity.this);
//        progressDialog.setTitle("Please wait..");
//        progressDialog.setCancelable(true);
//        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new PageProcessHelper(TransparentActivity.this).killBackgroundProcesses();
                Log.d("BOSTED", true + "");
                finish();
            }
        }).start();
//        synchronized (this){
//           wait();
//        }
        Toast.makeText(getApplicationContext(), "Your phone has been boosted!", Toast.LENGTH_SHORT).show();
//        if (progressDialog.isShowing()){
//            progressDialog.dismiss();
//        }
    }

}
