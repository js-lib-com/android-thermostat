package js.thermostat;

import android.app.Application;

import js.log.Log;
import js.log.LogFactory;
import js.log.LogLevel;

public class App extends Application implements Thread.UncaughtExceptionHandler {
    private static Log log;

    /**
     * Application single instance. System guarantee there is only one single Application instance, per process.
     */
    private static App instance;

    public static App instance() {
        return instance;
    }

    private Settings settings;
    private String thermostatURL;

    @Override
    public void onCreate() {
        LogManager.activateInAppLogging(this, LogLevel.TRACE, true);
        log = LogFactory.getLog(App.class);
        log.trace("onCreate()");

        Thread.setDefaultUncaughtExceptionHandler(this);

        instance = this;
        super.onCreate();
        settings = new Settings(getApplicationContext());
    }

    public Settings getSettings() {
        return settings;
    }

    public String getThermostatURL() {
        return thermostatURL;
    }

    public void setThermostatURL(String thermostatURL) {
        this.thermostatURL = thermostatURL;
    }

    @Override
    public void uncaughtException(Thread t, Throwable throwable) {
        log.dump("Uncaught exception on: ", throwable);
    }
}