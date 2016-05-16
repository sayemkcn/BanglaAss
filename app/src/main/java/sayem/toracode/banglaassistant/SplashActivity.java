package sayem.toracode.banglaassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;


public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        setSupportActionBar(null);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ifFirstRun())
                        Thread.sleep(5000);
                    else
                        Thread.sleep(2000);
                    finish();
                } catch (InterruptedException e) {
                    finish();
                }
            }
        }).start();
//        Toast.makeText(getApplicationContext(), "OnPostCreate", Toast.LENGTH_SHORT).show();
    }

    public boolean ifFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
            return true;
        }
        return false;
    }
}
