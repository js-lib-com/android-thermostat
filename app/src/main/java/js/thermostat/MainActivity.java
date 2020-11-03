package js.thermostat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import js.log.Log;
import js.log.LogFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final Log log = LogFactory.getLog(MainActivity.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        log.trace("onCreate");
        super.onCreate(savedInstanceState);
        Settings settings = App.instance().getSettings();
        if (!settings.hasDomain()) {
            SettingsActivity.start(this);
            return;
        }
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_logo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ThermostatActivity.start(this);
    }
}
