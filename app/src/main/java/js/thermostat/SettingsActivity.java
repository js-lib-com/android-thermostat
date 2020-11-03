package js.thermostat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import js.log.Log;
import js.log.LogFactory;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final Log log = LogFactory.getLog(SettingsActivity.class);

    public static void start(Activity activity) {
        log.trace("start(Activity)");
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    private Settings settings;
    private EditText domainEdit;
    private EditText portEdit;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        log.trace("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        domainEdit = findViewById(R.id.settings_domain);
        portEdit = findViewById(R.id.settings_port);

        settings = App.instance().getSettings();
        domainEdit.setText(settings.getDomain());
        portEdit.setText(Integer.toString(settings.getPort()));

        findViewById(R.id.settings_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.settings_save) {
            settings.setDomain(domainEdit.getText().toString());
            settings.setPort(Integer.parseInt(portEdit.getText().toString()));
        }
        finish();
    }
}
