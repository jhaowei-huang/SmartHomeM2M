package iot.ttu.edu.c4lab.smarthomem2m;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import java.util.HashMap;

import iot.ttu.edu.c4lab.smarthomem2m.data.DataTable;
import iot.ttu.edu.c4lab.smarthomem2m.data.Device;
import iot.ttu.edu.c4lab.smarthomem2m.data.Event;
import iot.ttu.edu.c4lab.smarthomem2m.data.Rule;

public class M2MCoapClient {
    public static CoapClient client = new CoapClient();
    // default gateway ip : coap://192.168.6.254:5683
    // demo room gateway ip : coap://140.129.33.157:5683
    private static String IP = "coap://192.168.6.254:5683";
    private static String COMMAND_DISCOVER = "/.well-known/core";
    private static HashMap<String, ObserveSensorsHandler> observeSensorsHandlerMap = new HashMap<>();
    private static HashMap<String, Event> eventMap = new HashMap<>();
    public static HashMap<String, Rule> ruleMap = new HashMap<>();

    private static String DEVECIEREGEX = "[<]/[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}:[0-9]{1,5}/[0-9]{1,3}[>]";
    private static String RESOURCEREGEX = "[<]/[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}:[0-9]{1,5}/[0-9]{1,3}/[0-9]{1,4}[>]";
    private static String RESOURCEREGEX_NEW = "[<]/[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}:[0-9]{1,5}/[0-9]+/[0-9]+/[0-9]+[>]";
    private static String NAMEREGEX = "[(].*[)]";

    public static HashMap<String, Device> deviceMap = new HashMap<>();
    public static String[] deviceStringArray;

    public static HashMap<String, Device> zumoMap = new HashMap<>();

    public static CheckRulesThread checkRulesThread = new M2MCoapClient.CheckRulesThread();

    public static void connect(String ip) {
        client.setURI("coap://" + ip + COMMAND_DISCOVER);
        client.setTimeout(15000);
    }

    public static void connect() {
        client.setURI(IP + COMMAND_DISCOVER);
        client.setTimeout(15000);
    }

    public static CoapResponse get() {
        return client.get();
    }

    public static void parsingNewDataModel(String[] array) {
        // 得到.well-known/core的回傳字串，用逗號區分，得到一陣列
        for (String element : array) {
            // 檢查是否有符合<[IP]:[PORT]/[DEVICEID]]>，如：</192.168.6.154:5683/124>
            if (element.matches(DEVECIEREGEX)) {
                // 去除角括號，並加入至deviceMap
                String str = element.replace("</", "").replace(">", "");
                String[] strArray = str.split("/");
                // 以[IP]:[PORT]/[DEVICEID]當作是key，設定device ip 與 id
                if (DataTable.getDeviceName(strArray[1]) != null) {
                    if (strArray[1].equals("220")) {
                        Device device = new Device(strArray[0], DataTable.getDeviceName(strArray[1]));
                        device.setDeviceIp(strArray[0]);
                        device.setDeviceId(strArray[1]);
                        zumoMap.put(strArray[0] + "/" + strArray[1], device);
                    } else if (strArray[1].equals("200")) {
                        // end device更新用，忽略typeId = 200
                    } else {
                        Device device = new Device(strArray[0], DataTable.getDeviceName(strArray[1]));
                        device.setDeviceIp(strArray[0]);
                        device.setDeviceId(strArray[1]);
                        deviceMap.put(strArray[0] + "/" + strArray[1], device);
                    }
                }
            }
            // 檢查是否有符合<[IP]:[PORT]/[DEVICEID]/[INDEX]/[RESOURCEID]]>，如：</192.168.6.154:5683/124/0/1>
            else if (element.matches(RESOURCEREGEX_NEW)) {
                // 去除角括號，並從deviceMap中取的方才加入的裝置
                String str = element.replace("</", "").replace(">", "");
                String[] strArray = str.split("/");

                if (strArray[1].equals("220")) {
                    // 查詢該項resource是屬於sensor類還是actuator類
                    int type = DataTable.getNewResourceType(strArray[3]);
                    Device device = zumoMap.get(strArray[0] + "/" + strArray[1]);
                    // type = 0 表示為sensor，device加入一個sensorL
                    device.addactuator(strArray[2] + "/" + strArray[3]);
                } else if (strArray[1].equals("200")) {
                    // end device更新用，忽略typeId = 200
                } else {
                    // 查詢該項resource是屬於sensor類還是actuator類
                    int type = DataTable.getNewResourceType(strArray[3]);
                    Device device = deviceMap.get(strArray[0] + "/" + strArray[1]);
                    // type = 0 表示為sensor，device加入一個sensor
                    if (type == 0) {
                        device.addsensor(strArray[2] + "/" + strArray[3]);
                    }
                    // type = 1 表示為actuator，device加入一個actuator
                    else if (type == 1) {
                        device.addactuator(strArray[2] + "/" + strArray[3]);
                    }
                }
            }
        }
        // 將deviceMap轉換成String陣列，用於在下拉是選單中的顯示文字
        toDeviceStringArray();
    }

    public static void toDeviceStringArray() {
        // 建立一個跟deviceMap一樣大小的陣列
        deviceStringArray = new String[deviceMap.size()];

        int i = 0;
        for (Device device : deviceMap.values()) {
            // 依序放入陣列，並加上該項裝置的中文名稱，如：192.168.6.154:5683/124(溫溼度計)
            deviceStringArray[i] = device.getIp() + "/" + device.getDeviceId() + "(" + device.getName() + ")";
            i++;
        }
    }

    public static String[] getDeviceStringArray(boolean isSensor) {
        // 建立一個跟deviceMap一樣大小的陣列
        deviceStringArray = new String[deviceMap.size()];

        int i = 0;
        if (isSensor) {
            for (Device device : deviceMap.values()) {
                if (device.getsensor().size() != 0) {
                    deviceStringArray[i] = device.getIp() + "/" + device.getDeviceId() + "(" + device.getName() + ")";
                    i++;
                }
            }
        } else {
            for (Device device : deviceMap.values()) {
                if (device.getactuator().size() != 0) {
                    deviceStringArray[i] = device.getIp() + "/" + device.getDeviceId() + "(" + device.getName() + ")";
                    i++;
                }
            }
        }

        return deviceStringArray;
    }

    public static String[] getResourceStringArray(String key, boolean isSensor) {
        // 新版
        key = key.replaceAll(NAMEREGEX, "");

        Device device = deviceMap.get(key);
        if (device == null)
            return null;

        // 選擇不同的裝置，會對應到不同的sensor類resourceid，並加上中文名稱
        // 如：0/1(溫度)
        if (isSensor) {
            String[] resourceArray = new String[device.getsensor().size()];
            for (int i = 0; i < device.getsensor().size(); i++) {
                String[] sensorStringArray = device.getsensor().get(i).split("/");
                resourceArray[i] = device.getsensor().get(i) + "(" +
                        DataTable.getNewResourceName(sensorStringArray[1]) + ")";
            }

            return resourceArray;
        }
        // 選擇不同的裝置，會對應到不同的actuator類resourceid，並加上中文名稱
        // 如：0/200(開關)
        else {
            String[] resourceArray = new String[device.getactuator().size()];
            for (int i = 0; i < device.getactuator().size(); i++) {
                String[] actuatorStringArray = device.getactuator().get(i).split("/");
                resourceArray[i] = device.getactuator().get(i) + "(" +
                        DataTable.getNewResourceName(actuatorStringArray[1]) + ")";
            }

            return resourceArray;
        }
    }

    public static void observeSensors() {
        if (ruleMap == null)
            return;

        for (Rule m : ruleMap.values()) {
            for (Rule.Condition condition : m.getConditions()) {
                M2MCoapClient.observeSensor(deviceMap.get(condition.sensorId), condition.resourceId);
            }
        }
    }

    public static void observeSensor(Device device, String resourceid) {
        if (observeSensorsHandlerMap.get(device.getDeviceId() + "/" + resourceid) == null) {
            ObserveSensorsHandler handler = new ObserveSensorsHandler(device, resourceid);
            observeSensorsHandlerMap.put(device.getDeviceId() + "/" + resourceid, handler);
            client.setURI(IP + "/" + device.getIp() + "/" + device.getDeviceId() + "/" + resourceid);
            client.observe(handler);
        }
    }

    public static class ObserveSensorsHandler implements CoapHandler {
        private Device device = null;
        private String resourceid = null;

        public ObserveSensorsHandler(Device device, String resourceid) {
            this.device = device;
            this.resourceid = resourceid;
        }

        @Override
        public void onError() {

        }

        @Override
        public void onLoad(CoapResponse response) {
            String key = device.getDeviceIp() + "/" + device.getDeviceId() + "/" + resourceid;
            // System.out.println("event key = " + key);
            Event e = eventMap.get(key);
            if (e == null) {
                eventMap.put(key, new Event(device, resourceid, response.getResponseText()));
            } else {
                e.setValue(response.getResponseText());
            }
        }
    }

    public static void observeActuator(Device device, String resourceid) {
        if (observeSensorsHandlerMap.get(device.getDeviceId() + "/" + resourceid) == null) {
            ObserveSensorsHandler handler = new ObserveSensorsHandler(device, resourceid);
            observeSensorsHandlerMap.put(device.getDeviceId() + "/" + resourceid, handler);
            client.setURI(IP + "/" + device.getIp() + "/" + device.getDeviceId() + "/" + resourceid);
            client.observe(handler);
        }
    }

    public static class CheckRulesThread extends Thread {
        private volatile boolean isContinue = true;

        public void stopIt() {
            isContinue = false;
        }

        private void loop() {
            if (ruleMap == null)
                return;

            for (Rule item : ruleMap.values()) {
                boolean flag = false;
                boolean isFirst = true;
                for (Rule.Condition condition : item.getConditions()) {
                    // System.out.println("rule key = " + rule.sensorId + "/" + rule.resourceId);
                    Event event = eventMap.get(condition.sensorId + "/" + condition.resourceId);
                    if (event == null)
                        return;

                    String value = event.getValue();
                    // System.out.println("current = " + value);
                    if (condition.run(value)) {
                        if (isFirst) {
                            flag = true;
                            isFirst = false;
                        } else if (condition.logic.equals("AND"))
                            flag = flag & condition.enable;
                        else if (condition.logic.equals("OR"))
                            flag = flag | condition.enable;
                    } else {
                        flag = false;

                        if (isFirst) {
                            isFirst = false;
                        }
                    }
                }

                if (flag) {
                    for (Rule.Action action : item.getActions()) {
                        String uri = IP + "/" + action.actuatorId + "/" + action.resourceId;
                        client.setURI(uri);
                        client.put(action.value, 0);
                    }
                }
            }
        }

        @Override
        public void run() {
            while (isContinue) {
                try {
                    loop();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
