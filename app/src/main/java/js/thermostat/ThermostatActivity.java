package js.thermostat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import js.hera.dev.Thermostat;
import js.log.Log;
import js.log.LogFactory;

public class ThermostatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final Log log = LogFactory.getLog(ThermostatActivity.class);

    public static void start(Activity activity) {
        log.trace("start(Activity)");
        Intent intent = new Intent(activity, ThermostatActivity.class);
        activity.startActivity(intent);
    }

    private TextView captionView;
    private TextView temperatureView;
    private TextView setpointView;

    private ThermostatDevice thermostatDevice;
    private double setpointValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);

        thermostatDevice = new ThermostatDevice();

        captionView = findViewById(R.id.thermostat_caption);
        temperatureView = findViewById(R.id.thermostat_temperature);
        setpointView = findViewById(R.id.thermostat_setpoint);

        findViewById(R.id.thermostat_set_up).setOnClickListener(this);
        findViewById(R.id.thermostat_set_down).setOnClickListener(this);
        findViewById(R.id.thermostat_settings).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        thermostatDevice.getState((Thermostat.State state) -> runOnUiThread(() -> {
            if (state != null) {
                captionView.setText(state.isRunning() ? R.string.central_heating_on : R.string.central_heating_off);
                temperatureView.setText(value((double) state.getTemperature()));
                setpointValue = state.getSetpoint();
                setpointView.setText(value(setpointValue));
            }
        }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        thermostatDevice.update();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.thermostat_set_up) {
            thermostatDevice.setSetpoint(setpointValue + 0.5);
            updateSetpoint();
        } else if (v.getId() == R.id.thermostat_set_down) {
            thermostatDevice.setSetpoint(setpointValue - 0.5);
            updateSetpoint();
        } else if (v.getId() == R.id.thermostat_settings) {
            SettingsActivity.start(this);
        }
    }

    private void updateSetpoint() {
        thermostatDevice.getSetpoint((Double value) -> runOnUiThread(() -> {
            setpointValue = value;
            setpointView.setText(value(value));
        }));
    }

    private static String value(Double value) {
        return value != null ? String.format(Locale.getDefault(), "%02.1f", value) : "";
    }
}
