package js.thermostat;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.lang.reflect.Type;

import js.hera.dev.Thermostat;
import js.lang.Callback;
import js.log.Log;
import js.log.LogFactory;
import js.net.client.HttpRmiTransaction;

public class ThermostatDevice {
    private static final Log log = LogFactory.getLog(ThermostatDevice.class);

    public static final String DEVICE_NAME = "thermostat";

    private final String thermostatURL;

    public ThermostatDevice() {
        Settings settings = App.instance().getSettings();
        this.thermostatURL = ThermostatDevice.buildURL(settings.getDomain(), settings.getPort());
    }

    public void setSetpoint(@NonNull Double setpoint) {
        exec("setSetpoint", void.class, setpoint.toString(), null);
    }

    public void getSetpoint(Callback<Double> callback) {
        exec("getSetpoint", double.class, null, callback);
    }

    public void getState(Callback<Thermostat.State> callback) {
        exec("getState", Thermostat.State.class, null, callback);
    }

    public void update() {
        exec("update", void.class, null, null);
    }

    private void exec(String methodName, Type returnType, String parameter, Callback<?> callback) {
        log.trace("exec(String,Type,String,Callback<?>)");

        if (callback == null) {
            callback = (Object value) -> {
            };
        }

        HttpRmiTransaction rmi = HttpRmiTransaction.getInstance(thermostatURL);
        rmi.setMethod("js.hera.dev.HostSystem", "invoke");
        rmi.setReturnType(returnType);

        if (parameter != null) {
            rmi.setArguments(DEVICE_NAME, methodName, parameter);
        } else {
            rmi.setArguments(DEVICE_NAME, methodName);
        }

        try {
            rmi.exec(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("DefaultLocale")
    public static String buildURL(String host, int port) {
        return String.format("http://%s:%d/", host, port);
    }
}
