package js.thermostat;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import js.lang.AsyncTask;
import js.lang.Callback;
import js.log.Log;
import js.log.LogFactory;

/**
 * Thermostat service discovery using MDNS.
 *
 * @author Iulian Rotaru
 */
public final class DeviceDiscovery {
    private static final Log log = LogFactory.getLog(DeviceDiscovery.class);

    private static final String THERMOSTAT_SERVICE = "_thermostat._tcp";

    private final NsdManager nsdManager;
    private final DiscoveryListener discoveryListener;

    public DeviceDiscovery(Context context) {
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        discoveryListener = new DiscoveryListener();
    }

    public void search(Callback<String> callback) {
        log.trace("search(Callback<String>)");
        discoveryListener.setCallback(callback);
        nsdManager.discoverServices(THERMOSTAT_SERVICE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public void stop() {
        log.trace("stop");
        AsyncTask<Void> task = new AsyncTask<Void>() {
            @Override
            protected Void execute() {
                nsdManager.stopServiceDiscovery(discoveryListener);
                return null;
            }
        };
        task.start();
    }

    private class DiscoveryListener extends AbstractDiscoveryListener {
        private Callback<String> callback;

        public void setCallback(Callback<String> callback) {
            this.callback = callback;
        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            super.onServiceFound(serviceInfo);
            nsdManager.resolveService(serviceInfo, new AbstractResolveListener() {
                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    log.trace("onServiceResolved: %s", serviceInfo);
                    callback.handle(ThermostatDevice.buildURL(serviceInfo.getHost().getHostAddress(), serviceInfo.getPort()));
                }
            });
        }
    }

    private abstract class AbstractDiscoveryListener implements NsdManager.DiscoveryListener {
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            log.trace("onStartDiscoveryFailed: %s: %d", serviceType, errorCode);
            nsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            log.trace("onStopDiscoveryFailed: %s: %d", serviceType, errorCode);
            nsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onDiscoveryStarted(String serviceType) {
            log.trace("onDiscoveryStarted: %s", serviceType);
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            log.trace("onDiscoveryStopped: %s", serviceType);
        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            log.trace("onServiceFound: %s", serviceInfo);
            nsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            log.trace("onServiceLost: %s", serviceInfo);
            nsdManager.stopServiceDiscovery(this);
        }
    }

    private static abstract class AbstractResolveListener implements NsdManager.ResolveListener {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            log.trace("onResolveFailed");
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            log.trace("onServiceResolved");
        }
    }
}
