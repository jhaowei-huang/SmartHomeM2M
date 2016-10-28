package iot.ttu.edu.c4lab.smarthomem2m.wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by pcuser on 2015/9/16.
 */
public class WifiConnector {

    private Context mContext;
    private WifiManager mWifiManager;
    private int mNetworkID = -1;

    // 網路加密模式
    public enum SecurityMode {
        OPEN, WEP, WPA, WPA2
    }

    public WifiConnector(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    public boolean onConnect(String ssid, String password, SecurityMode mode) {
        // 增加新的網路設置
        WifiConfiguration cfg = new WifiConfiguration();
        cfg.SSID = "\"" + ssid + "\"";
        if (password != null && !"".equals(password)) {
            // 若為WEP加密模式，需要將密碼放到cfg.wepKeys[0]
            if (mode == SecurityMode.WEP) {
                cfg.wepKeys[0] = "\"" + password + "\"";
                cfg.wepTxKeyIndex = 0;
            } else {
                cfg.preSharedKey = "\"" + password + "\"";
            }
        }

        cfg.status = WifiConfiguration.Status.ENABLED;
        mNetworkID = mWifiManager.addNetwork(cfg);
        Log.d("tag", "mNetworkID  =" + mNetworkID);
//        Log.d("tag","NetworkID  =" + mWifiManager.getConnectionInfo().getNetworkId());
//        mWifiManager.disableNetwork(mWifiManager.getConnectionInfo().getNetworkId());
//        mWifiManager.removeNetwork(mWifiManager.getConnectionInfo().getNetworkId());
        boolean value = mWifiManager.enableNetwork(mNetworkID, true);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ee) {
            value = false;
        }

        return value;
    }
}
