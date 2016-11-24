package iot.ttu.edu.c4lab.smarthomem2m.data;

import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DataTable {
    private static IoTDataNew data = new IoTDataNew();

    public static void init(Resources res) {
        data.setResources(res);
        data.initData();
    }

//    public static JSONObject getTable() throws JSONException {
//        List<TypeID> typeids = data.getTypeIDs();
//
//        table = new JSONObject();
//        JSONArray deviceArray = new JSONArray();
//
//        for (TypeID t : typeids) {
//            JSONArray array = new JSONArray();
//            List<ResourceID> resourceids = data.getResourceData("" + t.getId());
//            for (ResourceID r : resourceids) {
//                JSONObject resourceObj = new JSONObject();
//
//                resourceObj.put("id", r.getId());
//                resourceObj.put("name", r.getName());
//                array.put(resourceObj);
//            }
//
//            JSONObject obj = new JSONObject();
//            obj.put("resource", array);
//            obj.put("name", t.getName());
//            obj.put("deviceid", "" + t.getId());
//
//            deviceArray.put(obj);
//        }
//
//        table.put("device", deviceArray);
//        // System.out.println("\n" + table);
//        return table;
//    }

    public static String getDeviceName(String deviceid) {
        try {
            return data.getDeviceID(deviceid).getName();
        } catch (Exception e) {
            return null;
        }
    }

//    public static String getResourceName(String deviceid, String resourceid) {
//        try {
//            return data.getResourceID(resourceid).getName();
//        } catch (Exception e) {
//            return null;
//        }
//    }

//    public static String getResourceName(int deviceid, String resourceid) {
//        try {
//            return data.getResourceData(deviceid + "", resourceid).getName();
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static int getNewResourceType(String index) {
        try {
            // sensor = 0, actuator = 1
            return data.getResourceID(index).getType();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getNewResourceName(String index) {
        try {
            // sensor = 0, actuator = 1
            return data.getResourceID(index).getName();
        } catch (Exception e) {
            return "-";
        }
    }
}
