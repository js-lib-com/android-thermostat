package js.thermostat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    private static final String DOMAIN = "settings.domain";
    private static final String PORT = "settings.port";

    private final Context context;

    public Settings(Context context) {
        this.context = context;
    }

    public String getDomain() {
        return store().getString(DOMAIN, "");
    }

    public void setDomain(String domain) {
        editor().putString(DOMAIN, domain).commit();
    }

    public boolean hasDomain() {
        String domain = getDomain();
        return domain != null && !domain.isEmpty();
    }

    public int getPort() {
        return store().getInt(PORT, 8080);
    }

    public void setPort(int port) {
        editor().putInt(PORT, port).commit();
    }

    private SharedPreferences store() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private SharedPreferences.Editor editor() {
        return store().edit();
    }
}
