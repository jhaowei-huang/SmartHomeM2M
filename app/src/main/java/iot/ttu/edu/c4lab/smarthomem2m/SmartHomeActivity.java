package iot.ttu.edu.c4lab.smarthomem2m;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import net.sf.clipsrules.jni.Environment;

import org.eclipse.californium.core.CoapResponse;

import java.util.ArrayList;
import java.util.HashMap;

import iot.ttu.edu.c4lab.smarthomem2m.data.DataTable;
import iot.ttu.edu.c4lab.smarthomem2m.data.Device;
import iot.ttu.edu.c4lab.smarthomem2m.data.Rule;
import iot.ttu.edu.c4lab.smarthomem2m.wifi.AutoConnect;
import iot.ttu.edu.c4lab.smarthomem2m.wifi.WifiConnector;
import iot.ttu.edu.c4lab.smarthomem2m.wizard.WizardActivity;
import iot.ttu.edu.c4lab.smarthomem2m.wizard.WizardDialogBuilder;

public class SmartHomeActivity extends AppCompatActivity {
    // 權限
    private boolean permissionGranted = true;
    private final int PERMISSION_CODE = 1;
    // ui
    private ProgressDialog progressDialog = null;
    private NotificationCompat.Builder mBuilder = null;
    // 系統WiFi管理物件、WiFi ssid掃描清單、合法自動連線的WiFi ssid名單
    private BroadcastReceiver broadcastReceiver = null;
    private WifiManager wifiManager = null;
    private ArrayList<ScanResult> wifiScanList = null;
    private ArrayList<String> autoWifiSSIDList = new ArrayList<>();
    // 自動連線程式
    private boolean reconnect = true;
    private AutoConnect autoconnect = null;
    private BroadcastReceiver receiver = null;
    private static String ssid = "";
    private static String password = "";
    private static String ip = "";

    public static int NEW_RULE = 0;
    public static int EDIT_RULE = 1;

    private void requestMultiplePermissions() {
        String[] permissions = {
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_SECURE_SETTINGS};

        for (String permission : permissions) {
            int isGRANTED = ActivityCompat.checkSelfPermission(this, permission);
            if (isGRANTED != PackageManager.PERMISSION_GRANTED) {
                // 權限被拒絕
                permissionGranted = false;
            } else {
                // 允許該項權限，不做任何事情
            }
        }

        // 只要有其中一項權限不足，則請求使用者允許操作該權限。
        if (permissionGranted == false)
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 系統自動產生向使用者要求權限的畫面
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void reconnect() {
        showProgressDialog(getString(R.string.findgateway));
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                wifiScanList = (ArrayList<ScanResult>) wifiManager.getScanResults();
                wifiManager.getConnectionInfo().getSSID();
                unregisterReceiver(broadcastReceiver);
                autoWifiSSIDList.clear();

                for (Object scanResult : wifiScanList.toArray()) {
                    ScanResult result = (ScanResult) scanResult;
                    if (result.SSID.startsWith("CCC") || result.SSID.startsWith("BBB")) {
                        autoWifiSSIDList.add(result.SSID);
                    }
                }

                showProgressDialog(getString(R.string.connecting));

                if (autoWifiSSIDList.size() == 1) {
                    new AutoConnectTask(autoWifiSSIDList.get(0)).execute();
                } else if (autoWifiSSIDList.size() > 1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SmartHomeActivity.this);
                    dialog.setTitle(R.string.gatewayListTitle);
                    dialog.setItems(autoWifiSSIDList.toArray(new String[autoWifiSSIDList.size()]),
                            (dialogInterface, index) -> {
                                new AutoConnectTask(autoWifiSSIDList.get(index)).execute();
                            });
                    dialog.show();
                } else {
                    closeProgressDialog();
                    Snackbar.make(findViewById(android.R.id.content), R.string.nogateway,
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }

    public void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SmartHomeActivity.this);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        } else {
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private String getMyIp() {
        try {
            // 新增一個WifiManager物件並取得WIFI_SERVICE
            WifiManager wifi_service = (WifiManager) getSystemService(WIFI_SERVICE);
            // 取得wifi資訊
            WifiInfo wifiInfo = wifi_service.getConnectionInfo();
            // 取得IP，但這會是一個詭異的數字，還要再自己換算才行
            // 利用位移運算和AND運算計算IP
            DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
            int ipAddress = dhcpinfo.serverAddress;
            String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
            return this.ip = ip;
        } catch (Exception e) {
            return null;
        }
    }

    public void closeProgressDialog() {
        if (progressDialog == null)
            return;
        else if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public class AutoConnectTask extends AsyncTask<Void, Void, Boolean> {
        public AutoConnectTask(String ssid) {
            SmartHomeActivity.ssid = ssid;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                autoconnect = new AutoConnect(ssid);
                password = autoconnect.getPwd();
                Log.d("password", password);
            } catch (Exception e) {
                return false;
            }

            WifiConnector wifiConnector = new WifiConnector(getApplicationContext());
            if (wifiConnector.onConnect(ssid, password, WifiConnector.SecurityMode.WPA2)) {
                String gatewayIp = null;
                while (gatewayIp == null || gatewayIp.equals("0.0.0.0")) {
                    gatewayIp = getMyIp();
                }

                return true;
            } else
                return wifiConnector.onConnect(ssid, password, WifiConnector.SecurityMode.WPA2);
        }

        @Override
        protected void onPostExecute(final Boolean response) {
            new DiscoverTask(getMyIp()).execute();
        }
    }

    public class DiscoverTask extends AsyncTask<Void, Void, CoapResponse> {
        private String gatewayIp = "";

        public DiscoverTask(String gatewayIp) {
            this.gatewayIp = gatewayIp;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.searching));
        }

        @Override
        protected CoapResponse doInBackground(Void... voids) {
            M2MCoapClient.connect(gatewayIp);
            return M2MCoapClient.get();
        }

        @Override
        protected void onPostExecute(final CoapResponse response) {
            if (response != null) {
                // coap well-known core指令
                // 得到的字串用逗後區分，再進行分析轉換
                Log.d("device", response.getResponseText());

                M2MCoapClient.parsingNewDataModel(response.getResponseText().split(","));
            } else {
                // 搜尋裝置結果為空的
            }

            closeProgressDialog();
        }
    }

    private void updateListView() {
        ListView listView_ruleList = (ListView) findViewById(R.id.listView_ruleList);
        if (listView_ruleList != null) {
            RuleListAdapter ruleListAdapter = (RuleListAdapter) listView_ruleList.getAdapter();
            ruleListAdapter.notifyDataSetChanged();

            Log.d("MainService", "rule number = " + M2MCoapClient.ruleMap.size());
        } else {
            Log.d("MainService", "list is null");
        }
    }

    public NotificationCompat.Builder getBuilder() {
        return mBuilder;
    }

    private void startNotificationAction(View view) {
        // 判斷mBuilder是否為null，若為null則建立一個新的
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText(getString(R.string.running));

        // intent是向其他的元件來傳達訊息或資料的物件
        // 如：不同activity傳達訊息、啟動service、傳送推播訊息
        Intent resultIntent = new Intent(this, SmartHomeActivity.class);
        resultIntent.putExtra("reconnect", true);
        resultIntent.putExtra("permissionGranted", true);
        resultIntent.putExtra("gatewayip", ip);
        resultIntent.putExtra("notificationActivate", true);
        resultIntent.putExtra("deviceMap", M2MCoapClient.deviceMap);
        resultIntent.putExtra("ruleMap", M2MCoapClient.ruleMap);

        // 當有多個activity切換時，設定按下返回鍵的效果
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        // 按下notification bar時，會恢復原本的程式狀態
        mBuilder.setContentIntent(resultPendingIntent);

        // notification bar新增按鈕，直接產生動作，不需要切回去app畫面
        Intent stopIntent = new Intent(M2MCoapClientService.SERVICE_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);
        mBuilder.addAction(R.drawable.ic_stop, getString(R.string.stop), stopPendingIntent);

        Intent pauseIntent = new Intent(M2MCoapClientService.SERVICE_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        mBuilder.addAction(android.R.drawable.ic_media_pause, getString(R.string.pause), pausePendingIntent);

        // 更新、顯示mBuilder所建立的notification bar
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(M2MCoapClientService.serviceId, mBuilder.build());

        // 開始SmartM2M service
        Log.d("MainService", "I am going to start service");
        Intent serviceIntent = new Intent(this, M2MCoapClientService.class);
        serviceIntent.putExtra("isStop", false);
        startService(serviceIntent);
    }

    private void stopNotificationAction(View view) {
        // 關閉notification bar
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(M2MCoapClientService.serviceId);

        // 停止SmartM2M service
        Intent serviceIntent = new Intent(this, M2MCoapClientService.class);
        serviceIntent.putExtra("isStop", true);
        startService(serviceIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);

        Intent intent = this.getIntent();
        Boolean reconnect = intent.getBooleanExtra("reconnect", true);
        permissionGranted = intent.getBooleanExtra("permissionGranted", false);

        Boolean notificationActivate = intent.getBooleanExtra("notificationActivate", false);

        String ip = intent.getStringExtra("gatewayip");

        if (notificationActivate && ip.equals(getMyIp())) {
            Log.d("MainService", "notificationActive");
            HashMap<String, Rule> ruleMap = (HashMap<String, Rule>) intent.getSerializableExtra("ruleMap");
            HashMap<String, Device> deviceMap = (HashMap<String, Device>) intent.getSerializableExtra("deviceMap");

            M2MCoapClient.ruleMap = ruleMap;
            M2MCoapClient.deviceMap = deviceMap;
            M2MCoapClient.toDeviceStringArray();
            reconnect = false;
        }

        // 請求權限
        requestMultiplePermissions();
        // 讀取properties
        DataTable.init(this.getResources());

        // 點擊notification bar上的按鈕，依據所設定的動作代號時，產生相對應的工作
        IntentFilter filter = new IntentFilter();
        filter.addAction(M2MCoapClientService.SERVICE_STOP);
        filter.addAction(M2MCoapClientService.SERVICE_PAUSE);
        filter.addAction(M2MCoapClientService.SERVICE_CONTINUE);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 停止，關掉notification bar並停止SmartM2M
                if (intent.getAction().equals(M2MCoapClientService.SERVICE_STOP)) {
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(M2MCoapClientService.serviceId);

                    Intent serviceIntent = new Intent(SmartHomeActivity.this, M2MCoapClientService.class);
                    serviceIntent.putExtra("isStop", true);
                    startService(serviceIntent);
                }
                // 暫停，停止SmartM2M
                else if (intent.getAction().equals(M2MCoapClientService.SERVICE_PAUSE)) {
                    // 判斷mBuilder是否為null
                    NotificationCompat.Builder mBuilder = getBuilder();
                    if (mBuilder != null) {
                        mBuilder.setContentText("暫停...");

                        Intent continueIntent = new Intent(M2MCoapClientService.SERVICE_CONTINUE);
                        PendingIntent continuePendingIntent =
                                PendingIntent.getBroadcast(SmartHomeActivity.this, 0, continueIntent, 0);
                        // 切換暫停/繼續按鈕
                        NotificationCompat.Action action = mBuilder.mActions.get(1);
                        action.icon = android.R.drawable.ic_media_play;
                        action.actionIntent = continuePendingIntent;
                        action.title = "繼續";

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(M2MCoapClientService.serviceId, mBuilder.build());

                        Intent serviceIntent = new Intent(SmartHomeActivity.this, M2MCoapClientService.class);
                        serviceIntent.putExtra("isStop", true);
                        startService(serviceIntent);
                    }
                }
                // 繼續，繼續SmartM2M
                else if (intent.getAction().equals(M2MCoapClientService.SERVICE_CONTINUE)) {
                    // 判斷mBuilder是否為null
                    NotificationCompat.Builder mBuilder = getBuilder();
                    if (mBuilder != null) {
                        mBuilder.setContentText("正在執行中...");

                        Intent pauseIntent = new Intent(M2MCoapClientService.SERVICE_PAUSE);
                        PendingIntent pausePendingIntent =
                                PendingIntent.getBroadcast(SmartHomeActivity.this, 0, pauseIntent, 0);
                        // 切換暫停/繼續按鈕
                        NotificationCompat.Action action = mBuilder.mActions.get(1);
                        action.icon = android.R.drawable.ic_media_pause;
                        action.actionIntent = pausePendingIntent;
                        action.title = "暫停";

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(M2MCoapClientService.serviceId, mBuilder.build());

                        Intent serviceIntent = new Intent(SmartHomeActivity.this, M2MCoapClientService.class);
                        serviceIntent.putExtra("isStop", false);
                        startService(serviceIntent);
                    }
                }
            }
        };

        registerReceiver(receiver, filter);

        // 開始自動連線，並取得end device內容
        if (reconnect)
            reconnect();

        // 加入浮動按鈕至浮動選單中，並設定圖示、大小、文字
        final FloatingActionButton fab_start = new FloatingActionButton(this);
        fab_start.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_start.setLabelText(getString(R.string.ic_rule_start));
        fab_start.setImageResource(R.drawable.ic_rule_start);

        final FloatingActionButton fab_stop = new FloatingActionButton(this);
        fab_stop.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_stop.setLabelText(getString(R.string.ic_rule_stop));
        fab_stop.setImageResource(R.drawable.ic_rule_stop);

        final FloatingActionButton fab_add = new FloatingActionButton(this);
        fab_add.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_add.setLabelText(getString(R.string.ic_rule_add));
        fab_add.setImageResource(R.drawable.ic_rule_add);

        FloatingActionMenu fab_menu = (FloatingActionMenu) this.findViewById(R.id.fab_menu);
        fab_menu.removeAllMenuButtons();
        fab_menu.addMenuButton(fab_start);
        fab_menu.addMenuButton(fab_stop);
        fab_menu.addMenuButton(fab_add);

        fab_add.setOnClickListener(view -> {
            Intent wizardIntent = new Intent(this, WizardActivity.class);
            wizardIntent.putExtra("requestCode", NEW_RULE);
            startActivityForResult(wizardIntent, NEW_RULE);
        });

        fab_start.setOnClickListener(view -> {
            startNotificationAction(view);
        });

        fab_stop.setOnClickListener(view -> {
            stopNotificationAction(view);
        });

        RuleListAdapter ruleListAdapter = new RuleListAdapter(this);
        ListView listView_ruleList = (ListView) findViewById(R.id.listView_ruleList);
        listView_ruleList.setAdapter(ruleListAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        updateListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_RULE && resultCode == RESULT_OK) {
            updateListView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smarthome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_demo) {
            ArrayList<String> smartSocketStringList = new ArrayList<>();
            ArrayList<String> brightSensorStringList = new ArrayList<>();

            for (Device device : M2MCoapClient.deviceMap.values()) {
                // 找出可用的智慧插座
                if (device.getDeviceId().equals("100")) {
                    smartSocketStringList.add(device.getIp() + "/" + device.getDeviceId());
                } else {
                    for (String resourceId : device.getsensor()) {
                        // 找出可用的光感計
                        if (resourceId.equals("0/3")) {
                            brightSensorStringList.add(device.getIp() + "/" + device.getDeviceId() + "/" + resourceId);
                        }
                    }
                }
            }

            if (brightSensorStringList.size() == 0 || smartSocketStringList.size() == 0) {
                Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.dialog_demo_error_message), Snackbar.LENGTH_LONG).show();
                return true;
            }

            Rule turnOnRule = new Rule("1");
            Rule turnOffRule = new Rule("2");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_demo_title_sensor);
            builder.setSingleChoiceItems(brightSensorStringList.toArray(new String[brightSensorStringList.size()]), 0, null);
            builder.setPositiveButton(R.string.dialog_dismiss_confirm_postive, (dialogInterface, i) -> {
                try {
                    ListView lv = ((AlertDialog) dialogInterface).getListView();
                    Object checkedItem = lv.getAdapter().getItem(lv.getCheckedItemPosition());

                    Log.d("demoRule", (String) checkedItem);
                    String[] array = checkedItem.toString().split("/");
                    turnOnRule.addCondition(
                            "AND",
                            array[0] + "/" + array[1],
                            array[2] + "/" + array[3],
                            ">",
                            "600",
                            getString(R.string.dialog_demo_turnon_condition_text));

                    turnOffRule.addCondition(
                            "AND",
                            array[0] + "/" + array[1],
                            array[2] + "/" + array[3],
                            "<",
                            "400",
                            getString(R.string.dialog_demo_turnoff_condition_text));

                    builder2.show();

                } catch (Exception e) {
                    return;
                }
            });
            builder.setNegativeButton(R.string.dialog_dismiss_confirm_negative, null);

            builder2.setTitle(R.string.dialog_demo_title_actuator);
            builder2.setSingleChoiceItems(smartSocketStringList.toArray(new String[brightSensorStringList.size()]), 0, null);
            builder2.setPositiveButton(R.string.dialog_dismiss_confirm_postive, (dialogInterface, i) -> {
                try {
                    ListView lv = ((AlertDialog) dialogInterface).getListView();
                    Object checkedItem = lv.getAdapter().getItem(lv.getCheckedItemPosition());

                    Log.d("demoRule", (String) checkedItem);

                    String[] array = checkedItem.toString().split("/");
                    turnOnRule.addAction(
                            array[0] + "/" + array[1],
                            "1/200",
                            "1",
                            getString(R.string.dialog_demo_turnon_action_text));

                    turnOffRule.addAction(
                            array[0] + "/" + array[1],
                            "1/200",
                            "0",
                            getString(R.string.dialog_demo_turnoff_action_text));

                    M2MCoapClient.ruleMap.put(turnOnRule.getRuleName(), turnOnRule);
                    M2MCoapClient.ruleMap.put(turnOffRule.getRuleName(), turnOffRule);
                    updateListView();
                } catch (Exception e) {
                    return;
                }
            });
            builder2.setNegativeButton(R.string.dialog_dismiss_confirm_negative, null);

            builder.show();
            return true;
        } else if (id == R.id.action_info) {
            Snackbar snackbar =
                    Snackbar.make(findViewById(android.R.id.content),
                            "SSID = " + SmartHomeActivity.ssid + "\nPW = " + SmartHomeActivity.password,
                            Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction(getString(R.string.dialog_dismiss_confirm_neutral), view -> {
                snackbar.dismiss();
            });

            snackbar.show();
            return true;
        } else if (id == R.id.action_search) {
            new DiscoverTask(getMyIp()).execute();
            return true;
        } else if (id == R.id.action_reconnect) {
            reconnect();
            return true;
        } else if (id == R.id.action_clipstest) {
            Environment clips = new Environment();
            String str = clips.eval("(+ 1 2 3 4 5 6 7 8 9 10)").toString();
            Snackbar.make(findViewById(android.R.id.content),
                    "(+ 1 2 3 4 5 6 7 8 9 10) = " + str, Snackbar.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
