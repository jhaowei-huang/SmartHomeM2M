package iot.ttu.edu.c4lab.smarthomem2m.data;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class IoTDataNew {

    private HashMap classIdMap;
    private HashMap deviceIdMap;
    private HashMap resourceIdMap;
    private String path = "";
    private Resources resources;
    private AssetManager assetManager;
    private Properties prop;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
        assetManager = resources.getAssets();
    }

    public void initData() {
        // TODO Auto-generated method stub

        prop = new Properties();

        initclassid();
        initresourceid();
        initdeviceId();

        Log.d("tag", "新 data 測試" + getResourceID("200").getName());
        Log.d("tag", "新 data 測試" + getDeviceID("100").getName());

//	    System.out.println("測試 "+getresourceID("200").getName());
//	    System.out.println("測試 "+getdeviceID("100").getName());

    }

    private void initdeviceId() {
        deviceIdMap = new HashMap();
        Enumeration<?> deviceData = getProperties(path + "configDevice.properties");
        while (deviceData.hasMoreElements()) {
            String key = (String) deviceData.nextElement();
            String value = prop.getProperty(key);
//			System.out.println("Key : " + key + ", Value : " + value);
            int keynumber = Integer.parseInt(key);
            deviceIdMap.put(key, new TypeID(keynumber, 0, value));
        }
    }

    private void initresourceid() {
        resourceIdMap = new HashMap();
        Enumeration<?> resourceData = getProperties(path + "configResource.properties");
        while (resourceData.hasMoreElements()) {
            String key = (String) resourceData.nextElement();
            String value = prop.getProperty(key);
//			System.out.println("Key : " + key + ", Value : " + value);
            int keynumber = Integer.parseInt(key);
            if (keynumber < 200) {
                resourceIdMap.put(key, new DeviceID(keynumber, value, 0));
            } else {
                resourceIdMap.put(key, new DeviceID(keynumber, value, 1));
            }
        }
    }

    private void initclassid() {
        classIdMap = new HashMap();
        Enumeration<?> classData = getProperties(path + "configClass.properties");

        while (classData.hasMoreElements()) {
            String key = (String) classData.nextElement();
            String value = prop.getProperty(key);
            int keynumber = Integer.parseInt(key);
            classIdMap.put(key, new ClassID(keynumber, value));
        }
    }

    private Enumeration<?> getProperties(String filename) {

        InputStream input = null;

        try {
            input = assetManager.open(filename);
            if (input == null) {
//				Log.d("tag", "新 data 測試" + " Sorry, unable to find");
                return null;
            }
            BufferedReader bf = new BufferedReader(new InputStreamReader(input));
            prop.load(bf);

            Enumeration<?> e = prop.propertyNames();
            return e;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

    public TypeID getDeviceID(String key) {
        return (TypeID) deviceIdMap.get(key);
    }

    public DeviceID getResourceID(String key) {
        return (DeviceID) resourceIdMap.get(key);
    }
}
