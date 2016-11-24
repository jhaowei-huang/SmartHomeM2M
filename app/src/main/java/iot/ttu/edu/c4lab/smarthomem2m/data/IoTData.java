package iot.ttu.edu.c4lab.smarthomem2m.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jhaowei on 2016-07-25.
 */

public class IoTData {

    List<TypeID> typeIDs = new ArrayList<>();
    List<ClassID> classIDs = new ArrayList<>();
    List<ResourceID> resourceIDs = new ArrayList<>();

    private HashMap deviceIdMap;

    public IoTData() {
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub

        initclassid();
        inittypeid();
        initresourceid();

        initdeviceId();
    }

    private void initdeviceId() {
        deviceIdMap = new HashMap();

        deviceIdMap.put("1",new NewResourceID(1,"溫度",0));
        deviceIdMap.put("2",new NewResourceID(2,"濕度",0));
        deviceIdMap.put("3",new NewResourceID(3,"光感計",0));
        deviceIdMap.put("4",new NewResourceID(4,"磁力",0));
        deviceIdMap.put("5",new NewResourceID(5,"顏色",0));
        deviceIdMap.put("6",new NewResourceID(6,"土壤濕度",0));
        deviceIdMap.put("7",new NewResourceID(7,"震動",0));
        deviceIdMap.put("8",new NewResourceID(8,"傾斜",0));
        deviceIdMap.put("9",new NewResourceID(9,"大氣壓力",0));
        deviceIdMap.put("10",new NewResourceID(10,"灰塵",0));
        deviceIdMap.put("11",new NewResourceID(11,"火焰",0));
        deviceIdMap.put("12",new NewResourceID(12,"風力",0));
        deviceIdMap.put("14",new NewResourceID(14,"天氣",0));
        deviceIdMap.put("17",new NewResourceID(17,"聲音",0));
        deviceIdMap.put("18",new NewResourceID(18,"超音波",0));
        deviceIdMap.put("101",new NewResourceID(101,"水位",0));
        deviceIdMap.put("45",new NewResourceID(45,"電壓",0));
        deviceIdMap.put("46",new NewResourceID(46,"電流",0));
        deviceIdMap.put("47",new NewResourceID(47,"功率",0));
        deviceIdMap.put("48",new NewResourceID(48,"累積用電量",0));
        deviceIdMap.put("49",new NewResourceID(49,"功率因素",0));

        deviceIdMap.put("20",new NewResourceID(20,"可燃氣體",0));
        deviceIdMap.put("21",new NewResourceID(21,"一氧化碳",0));
        deviceIdMap.put("22",new NewResourceID(22,"CO2",0));

        deviceIdMap.put("25",new NewResourceID(25,"煙霧",0));
        deviceIdMap.put("29",new NewResourceID(29,"空氣清淨度值",0));

        deviceIdMap.put("30",new NewResourceID(30,"黑色線追蹤",0));
        deviceIdMap.put("31",new NewResourceID(31,"避障",0));
        deviceIdMap.put("32",new NewResourceID(32,"紅外線IR",0));
        deviceIdMap.put("33",new NewResourceID(33,"PIR",0));

        deviceIdMap.put("50",new NewResourceID(50,"心跳",0));
        deviceIdMap.put("51",new NewResourceID(51,"體溫",0));
        deviceIdMap.put("52",new NewResourceID(52,"腦波",0));
        deviceIdMap.put("53",new NewResourceID(53,"postDetection",0));
        deviceIdMap.put("54",new NewResourceID(54,"語音指令",0));
        deviceIdMap.put("55",new NewResourceID(55,"手勢",0));
        deviceIdMap.put("56",new NewResourceID(56,"觸摸",0));
        deviceIdMap.put("59",new NewResourceID(59,"人數",0));
        deviceIdMap.put("70",new NewResourceID(70,"時間",0));
        deviceIdMap.put("71",new NewResourceID(53,"剩餘時間",0));
        deviceIdMap.put("102",new NewResourceID(102,"地點",0));
        deviceIdMap.put("104",new NewResourceID(104,"狀態",0));
        deviceIdMap.put("105",new NewResourceID(105,"水量",0));
//////////////////////////////////////////////////////////////////
        deviceIdMap.put("200",new NewResourceID(200,"開關",1));
        deviceIdMap.put("201",new NewResourceID(201,"LED",1));
        deviceIdMap.put("202",new NewResourceID(202,"蜂鳴器",1));
        deviceIdMap.put("203",new NewResourceID(203,"啟動",1));
        deviceIdMap.put("210",new NewResourceID(210,"數量",2));

        deviceIdMap.put("211",new NewResourceID(211,"時間",1));
        deviceIdMap.put("212",new NewResourceID(212,"定時",2));
        deviceIdMap.put("213",new NewResourceID(213,"模式切換",1));
        deviceIdMap.put("214",new NewResourceID(214,"脈衝",1));
        deviceIdMap.put("215",new NewResourceID(215,"設定",1));
        deviceIdMap.put("216",new NewResourceID(216,"次數",1));
        deviceIdMap.put("217",new NewResourceID(217,"行程",1));

        deviceIdMap.put("218",new NewResourceID(218,"段數",1));
        deviceIdMap.put("219",new NewResourceID(219,"音量準位",1));
        deviceIdMap.put("220",new NewResourceID(220,"類型",1));
        deviceIdMap.put("221",new NewResourceID(221,"狀態",1));

    }

    public NewResourceID getDevice(String index) {
        Object device = deviceIdMap.get(index);
        NewResourceID deviceIDData = null;
        if (device != null) {
            deviceIDData = (NewResourceID) device;
        }
        return deviceIDData;
    }

    public int getNewResourceIDtype(String index) {
        Object device = deviceIdMap.get(index);
        NewResourceID deviceIDData = null;
        int type = -1;
        if (device != null) {
            deviceIDData = (NewResourceID) device;
            type = deviceIDData.getType();
        }
        return type;
    }

    public String getNewResourceIDName(String index) {
        Object device = deviceIdMap.get(index);
        NewResourceID deviceIDData = null;
        String name = null;
        if (device != null) {
            deviceIDData = (NewResourceID) device;
            name = deviceIDData.getName();
        }
        return name;
    }

    private void initresourceid() {
        // TODO Auto-generated method stub

        // 空氣清淨機 resource
        resourceIDs.add(new ResourceID(1, 8, 0, "開關", 1));
        resourceIDs.add(new ResourceID(2, 8, 0, "風量", 2));
        resourceIDs.add(new ResourceID(3, 8, 0, "負離子", 1));
        resourceIDs.add(new ResourceID(4, 8, 0, "定時", 2));

        // 電風扇
        resourceIDs.add(new ResourceID(1, 15, 0, "開關", 1));
        resourceIDs.add(new ResourceID(2, 15, 0, "風量", 2));
        resourceIDs.add(new ResourceID(3, 15, 0, "旋轉", 1));

        // 智慧插座
        resourceIDs.add(new ResourceID(1, 100, 0, "開關1", 1));
        resourceIDs.add(new ResourceID(2, 100, 0, "開關2", 1));
        resourceIDs.add(new ResourceID(3, 100, 0, "開關3", 1));
        resourceIDs.add(new ResourceID(4, 100, 0, "開關4", 1));

        // 智慧插座
        resourceIDs.add(new ResourceID(3, 101, 0, "中", 1));
        resourceIDs.add(new ResourceID(4, 101, 0, "強", 1));

        // Kinect/vedioCam
        resourceIDs.add(new ResourceID(1, 102, 0, "人數", 0));
        resourceIDs.add(new ResourceID(2, 102, 0, "姿勢", 0));
        resourceIDs.add(new ResourceID(3, 102, 0, "聲音", 0));
        resourceIDs.add(new ResourceID(4, 102, 0, "左手", 0));
        resourceIDs.add(new ResourceID(5, 102, 0, "右手", 0));

        resourceIDs.add(new ResourceID(6, 102, 0, "溫度", 0));
        resourceIDs.add(new ResourceID(7, 102, 0, "濕度", 0));
        resourceIDs.add(new ResourceID(8, 102, 0, "大氣壓力", 0));
        resourceIDs.add(new ResourceID(9, 102, 0, "天氣", 0));
        resourceIDs.add(new ResourceID(10, 102, 0, "地點", 0));

        // 掃地機
        resourceIDs.add(new ResourceID(1, 104, 0, "開/關", 1));
        resourceIDs.add(new ResourceID(2, 104, 0, "模式", 2));

        // 咖啡機
        resourceIDs.add(new ResourceID(1, 105, 0, "開/關", 1));
        resourceIDs.add(new ResourceID(2, 105, 0, "啟動", 1));
        resourceIDs.add(new ResourceID(3, 105, 0, "杯數/濃度", 1));
        resourceIDs.add(new ResourceID(4, 105, 0, "數量(-)", 1));

        // 除濕機
        resourceIDs.add(new ResourceID(1, 107, 0, "開/關", 1));
        resourceIDs.add(new ResourceID(2, 107, 0, "水位", 0));

        // 感測器+致動器
        resourceIDs.add(new ResourceID(1, 120, 0, "溫度", 0));
        resourceIDs.add(new ResourceID(2, 120, 0, "濕度", 0));
        resourceIDs.add(new ResourceID(3, 120, 0, "光感計", 0));
        resourceIDs.add(new ResourceID(4, 120, 0, "磁力", 0));
        resourceIDs.add(new ResourceID(5, 120, 0, "顏色", 0));
        resourceIDs.add(new ResourceID(6, 120, 0, "土壤濕度", 0));
        resourceIDs.add(new ResourceID(7, 120, 0, "震動", 0));
        resourceIDs.add(new ResourceID(8, 120, 0, "傾斜", 0));
        resourceIDs.add(new ResourceID(9, 120, 0, "風力", 0));
        resourceIDs.add(new ResourceID(10, 120, 0, "煙霧", 0));
        resourceIDs.add(new ResourceID(11, 120, 0, "灰塵", 0));
        resourceIDs.add(new ResourceID(12, 120, 0, "火焰", 0));
        resourceIDs.add(new ResourceID(13, 120, 0, "可燃性氣體", 0));
        resourceIDs.add(new ResourceID(14, 120, 0, "一氧化碳", 0));
        resourceIDs.add(new ResourceID(20, 120, 0, "黑色線追蹤", 0));
        resourceIDs.add(new ResourceID(21, 120, 0, "避障", 0));
        resourceIDs.add(new ResourceID(30, 120, 0, "紅外線IR", 0));
        resourceIDs.add(new ResourceID(31, 120, 0, "聲音", 0));
        resourceIDs.add(new ResourceID(32, 120, 0, "超音波", 0));
        resourceIDs.add(new ResourceID(33, 120, 0, "PIR", 0));
        resourceIDs.add(new ResourceID(40, 120, 0, "心跳", 0));
        resourceIDs.add(new ResourceID(41, 120, 0, "體溫", 0));
        resourceIDs.add(new ResourceID(42, 120, 0, "腦波", 0));
        resourceIDs.add(new ResourceID(50, 120, 0, "觸摸", 0));
        resourceIDs.add(new ResourceID(51, 120, 0, "按鍵", 0));
        resourceIDs.add(new ResourceID(200, 120, 0, "LED", 1));
        resourceIDs.add(new ResourceID(201, 120, 0, "兩色LED", 1));
        resourceIDs.add(new ResourceID(202, 120, 0, "三色LED", 1));
        resourceIDs.add(new ResourceID(203, 120, 0, "IR（send）", 1));
        resourceIDs.add(new ResourceID(210, 120, 0, "繼電器Relay", 1));
        resourceIDs.add(new ResourceID(220, 120, 0, "蜂鳴器", 1));

        // 感測器
        resourceIDs.add(new ResourceID(1, 121, 0, "溫度", 0));
        resourceIDs.add(new ResourceID(2, 121, 0, "濕度", 0));
        resourceIDs.add(new ResourceID(3, 121, 0, "光感計", 0));
        resourceIDs.add(new ResourceID(4, 121, 0, "磁力", 0));
        resourceIDs.add(new ResourceID(5, 121, 0, "顏色", 0));
        resourceIDs.add(new ResourceID(6, 121, 0, "土壤濕度", 0));
        resourceIDs.add(new ResourceID(7, 121, 0, "震動", 0));
        resourceIDs.add(new ResourceID(8, 121, 0, "傾斜", 0));
        resourceIDs.add(new ResourceID(9, 121, 0, "風力", 0));
        resourceIDs.add(new ResourceID(10, 121, 0, "煙霧", 0));
        resourceIDs.add(new ResourceID(11, 121, 0, "灰塵", 0));
        resourceIDs.add(new ResourceID(12, 121, 0, "火焰", 0));
        resourceIDs.add(new ResourceID(13, 121, 0, "可燃性氣體", 0));
        resourceIDs.add(new ResourceID(14, 121, 0, "一氧化碳", 0));
        resourceIDs.add(new ResourceID(20, 121, 0, "黑色線追蹤", 0));
        resourceIDs.add(new ResourceID(21, 121, 0, "避障", 0));
        resourceIDs.add(new ResourceID(30, 121, 0, "紅外線IR", 0));
        resourceIDs.add(new ResourceID(31, 121, 0, "聲音", 0));
        resourceIDs.add(new ResourceID(32, 121, 0, "超音波", 0));
        resourceIDs.add(new ResourceID(33, 121, 0, "PIR", 0));
        resourceIDs.add(new ResourceID(40, 121, 0, "心跳", 0));
        resourceIDs.add(new ResourceID(41, 121, 0, "體溫", 0));
        resourceIDs.add(new ResourceID(42, 121, 0, "腦波", 0));
        resourceIDs.add(new ResourceID(50, 121, 0, "觸摸", 0));
        resourceIDs.add(new ResourceID(51, 121, 0, "按鍵", 0));

        // 致動器
        resourceIDs.add(new ResourceID(200, 122, 0, "LED", 1));
        resourceIDs.add(new ResourceID(201, 122, 0, "兩色LED", 1));
        resourceIDs.add(new ResourceID(202, 122, 0, "三色LED", 1));
        resourceIDs.add(new ResourceID(203, 122, 0, "IR（send）", 1));
        resourceIDs.add(new ResourceID(210, 122, 0, "繼電器Relay", 1));
        resourceIDs.add(new ResourceID(220, 122, 0, "蜂鳴器", 1));

        // 檯燈
        resourceIDs.add(new ResourceID(1, 200, 0, "開關", 1));
        resourceIDs.add(new ResourceID(2, 200, 0, "亮度", 2));
        resourceIDs.add(new ResourceID(3, 200, 0, "顏色", 2));

        // 捕蚊燈
        resourceIDs.add(new ResourceID(1, 201, 0, "開關", 1));

        // 電腦
        resourceIDs.add(new ResourceID(1, 202, 0, "電腦", 1));

        // 顯示器
        resourceIDs.add(new ResourceID(1, 203, 0, "顯示器", 1));

        // 顯示器aaaa
        resourceIDs.add(new ResourceID(1, 204, 0, "MAC", 0));
        resourceIDs.add(new ResourceID(2, 204, 0, "語音指令", 0));

        // 暖氣機
        resourceIDs.add(new ResourceID(1, 106, 0, "開關", 1));
        resourceIDs.add(new ResourceID(2, 106, 0, "送風/暖氣/溫度", 1));
        resourceIDs.add(new ResourceID(3, 106, 0, "溫度+", 1));
        resourceIDs.add(new ResourceID(4, 106, 0, "溫度-", 1));
        resourceIDs.add(new ResourceID(5, 106, 0, "旋轉", 1));

        // 遙控車
        resourceIDs.add(new ResourceID(1, 123, 0, "前", 1));
        resourceIDs.add(new ResourceID(2, 123, 0, "後", 1));
        resourceIDs.add(new ResourceID(3, 123, 0, "左", 1));
        resourceIDs.add(new ResourceID(4, 123, 0, "右", 1));
        resourceIDs.add(new ResourceID(5, 123, 0, "速度", 2));
        resourceIDs.add(new ResourceID(6, 123, 0, "x軸", 0));
        resourceIDs.add(new ResourceID(7, 123, 0, "y軸", 0));
        resourceIDs.add(new ResourceID(8, 123, 0, "z軸", 0));

    }

    private void inittypeid() {
        // TODO Auto-generated method stub

        typeIDs.add(new TypeID(1,0,"冷氣機"));
        typeIDs.add(new TypeID(2,0,"電冰箱"));
        typeIDs.add(new TypeID(3,0,"洗衣機"));
        typeIDs.add(new TypeID(4,0,"除濕機"));
        typeIDs.add(new TypeID(5,0,"電視機"));
        typeIDs.add(new TypeID(6,0,"烘衣機"));
        typeIDs.add(new TypeID(7,0,"熱水器"));
        typeIDs.add(new TypeID(8,0,"空氣清淨機"));
        typeIDs.add(new TypeID(9,0,"電子鍋"));
        typeIDs.add(new TypeID(10,0,"開飲機"));
        typeIDs.add(new TypeID(11,0,"電磁爐"));
        typeIDs.add(new TypeID(12,0,"烘碗機"));
        typeIDs.add(new TypeID(13,0,"微波爐"));
        typeIDs.add(new TypeID(14,0,"全熱交換器"));
        typeIDs.add(new TypeID(15,0,"電扇"));
        typeIDs.add(new TypeID(100,0,"智慧插座"));
        typeIDs.add(new TypeID(101,0,"果汁機"));
        typeIDs.add(new TypeID(102,0,"Kinect/vedioCam/webservice"));
        typeIDs.add(new TypeID(104,0,"掃地機"));
        typeIDs.add(new TypeID(105,0,"咖啡機"));
        typeIDs.add(new TypeID(106,0,"暖氣機"));
        typeIDs.add(new TypeID(110,0,"檯燈"));
        typeIDs.add(new TypeID(111,0,"捕蚊燈"));
        typeIDs.add(new TypeID(140,0,"充電器"));
        typeIDs.add(new TypeID(141,0,"遙控器"));
        typeIDs.add(new TypeID(220,0,"遙控車"));
        typeIDs.add(new TypeID(202,0,"電腦"));
        typeIDs.add(new TypeID(203,0,"顯示器"));
        typeIDs.add(new TypeID(204,0,"手機/平板"));
        typeIDs.add(new TypeID(120,0,"感測器+致動器"));
        typeIDs.add(new TypeID(121,0,"感測器"));
        typeIDs.add(new TypeID(122,0,"致動器"));
        typeIDs.add(new TypeID(124,0,"溫溼度計"));
        typeIDs.add(new TypeID(125,0,"風力計"));
        typeIDs.add(new TypeID(126,0,"勾表"));
        typeIDs.add(new TypeID(127,0,"水滴偵測"));
        typeIDs.add(new TypeID(128,0,"警報器"));

    }

    private void initclassid() {
        // TODO Auto-generated method stub
        classIDs.add(new ClassID(0, "家電類"));
    }

    public ResourceID getResourceData(String typeid, String resourceid) {
        ResourceID resourceData = null;

        for (ResourceID data : resourceIDs) {
            String data_typeid = "" + data.getTypeId();
            String data_resourceid = "" + data.getId();
            if (data_typeid.equals(typeid) && data_resourceid.equals(resourceid)) {
                resourceData = data;
                break;
            }

        }

        return resourceData;
    }

    public List<ResourceID> getResourceData(String typeid) {
        List<ResourceID> resourceData = new ArrayList<>();

        for (ResourceID data : resourceIDs) {
            String data_typeid = "" + data.getTypeId();
            if (data_typeid.equals(typeid)) {
                resourceData.add(data);
            }

        }

        return resourceData;
    }

    public List<TypeID> getTypeIDs() {
        return typeIDs;
    }

    public TypeID getTypeData(String typeid) {
        TypeID typeData = null;

        for (TypeID data : typeIDs) {
            String data_typeid = "" + data.getId();
            // System.out.println("test "+typeid +","+data_typeid );
            if (data_typeid.equals(typeid)) {
                typeData = data;
                break;
            }

        }

        return typeData;
    }
}

// package iot.ttu.edu.M2MRuleEditor.data;
//
// import java.util.ArrayList;
// import java.util.List;
//
// public class IoTData {
//
// List<TypeID> typeIDs = new ArrayList<>();
// List<ClassID> classIDs = new ArrayList<>();
// List<ResourceID> resourceIDs = new ArrayList<>();
//
// public IoTData() {
// initData();
// }
//
// private void initData() {
// initclassid();
// inittypeid();
// initresourceid();
// }
//
// private void initresourceid() {
// // 空氣清淨機 resource
// resourceIDs.add(new ResourceID(1, 8, 0, "開關", 1));
// resourceIDs.add(new ResourceID(2, 8, 0, "風量", 1));
// resourceIDs.add(new ResourceID(3, 8, 0, "負離子", 1));
// resourceIDs.add(new ResourceID(4, 8, 0, "定時", 1));
//
// // 電風扇
// resourceIDs.add(new ResourceID(1, 15, 0, "開關", 1));
// resourceIDs.add(new ResourceID(2, 15, 0, "風量", 1));
// resourceIDs.add(new ResourceID(3, 15, 0, "強", 1));
//
// // 智慧插座
// resourceIDs.add(new ResourceID(1, 100, 0, "開關1", 1));
// resourceIDs.add(new ResourceID(2, 100, 0, "開關2", 1));
// resourceIDs.add(new ResourceID(3, 100, 0, "開關3", 1));
// resourceIDs.add(new ResourceID(4, 100, 0, "開關4", 1));
//
// // 智慧插座
// resourceIDs.add(new ResourceID(3, 101, 0, "中", 1));
// resourceIDs.add(new ResourceID(4, 101, 0, "強", 1));
//
// // Kinect/vedioCam
// resourceIDs.add(new ResourceID(1, 102, 0, "人數", 0));
// resourceIDs.add(new ResourceID(2, 102, 0, "姿勢", 0));
// resourceIDs.add(new ResourceID(3, 102, 0, "聲音", 0));
// resourceIDs.add(new ResourceID(4, 102, 0, "左手", 0));
// resourceIDs.add(new ResourceID(5, 102, 0, "右手", 0));
//
// resourceIDs.add(new ResourceID(6, 102, 0, "溫度", 0));
// resourceIDs.add(new ResourceID(7, 102, 0, "濕度", 0));
// resourceIDs.add(new ResourceID(8, 102, 0, "大氣壓力", 0));
// resourceIDs.add(new ResourceID(9, 102, 0, "天氣", 0));
// resourceIDs.add(new ResourceID(10, 102, 0, "地點", 0));
//
// // 掃地機
// resourceIDs.add(new ResourceID(1, 104, 0, "開/關", 1));
// resourceIDs.add(new ResourceID(2, 104, 0, "模式", 2));
//
// // 咖啡機
// resourceIDs.add(new ResourceID(1, 105, 0, "開/關", 1));
// resourceIDs.add(new ResourceID(2, 105, 0, "啟動", 1));
// resourceIDs.add(new ResourceID(3, 105, 0, "杯數/濃度", 1));
// resourceIDs.add(new ResourceID(4, 105, 0, "數量(-)", 1));
//
// // 感測器
// resourceIDs.add(new ResourceID(1, 121, 0, "溫度", 0));
// resourceIDs.add(new ResourceID(2, 121, 0, "濕度", 0));
// resourceIDs.add(new ResourceID(3, 121, 0, "光感計", 0));
// resourceIDs.add(new ResourceID(4, 121, 0, "磁力", 0));
// resourceIDs.add(new ResourceID(5, 121, 0, "顏色", 0));
// resourceIDs.add(new ResourceID(6, 121, 0, "土壤濕度", 0));
// resourceIDs.add(new ResourceID(7, 121, 0, "震動", 0));
// resourceIDs.add(new ResourceID(8, 121, 0, "傾斜", 0));
// resourceIDs.add(new ResourceID(10, 121, 0, "煙霧", 0));
// resourceIDs.add(new ResourceID(11, 121, 0, "灰塵", 0));
// resourceIDs.add(new ResourceID(12, 121, 0, "火焰", 0));
// resourceIDs.add(new ResourceID(13, 121, 0, "可燃性氣體", 0));
// resourceIDs.add(new ResourceID(14, 121, 0, "一氧化碳", 0));
// resourceIDs.add(new ResourceID(20, 121, 0, "黑色線追蹤", 0));
// resourceIDs.add(new ResourceID(21, 121, 0, "避障", 0));
// resourceIDs.add(new ResourceID(30, 121, 0, "紅外線IR", 0));
// resourceIDs.add(new ResourceID(31, 121, 0, "聲音", 0));
// resourceIDs.add(new ResourceID(32, 121, 0, "超音波", 0));
// resourceIDs.add(new ResourceID(33, 121, 0, "PIR", 0));
// resourceIDs.add(new ResourceID(40, 121, 0, "心跳", 0));
// resourceIDs.add(new ResourceID(41, 121, 0, "體溫", 0));
// resourceIDs.add(new ResourceID(42, 121, 0, "腦波", 0));
// resourceIDs.add(new ResourceID(50, 121, 0, "觸摸", 0));
// resourceIDs.add(new ResourceID(51, 121, 0, "按鍵", 0));
//
// // 致動器
// resourceIDs.add(new ResourceID(200, 122, 0, "LED", 1));
// resourceIDs.add(new ResourceID(201, 122, 0, "兩色LED", 1));
// resourceIDs.add(new ResourceID(202, 122, 0, "三色LED", 1));
// resourceIDs.add(new ResourceID(203, 122, 0, "IR（send）", 1));
// resourceIDs.add(new ResourceID(210, 122, 0, "繼電器Relay", 1));
// resourceIDs.add(new ResourceID(220, 122, 0, "蜂鳴器", 1));
//
// // 檯燈
// resourceIDs.add(new ResourceID(1, 200, 0, "開關", 1));
// resourceIDs.add(new ResourceID(2, 200, 0, "亮度", 2));
// resourceIDs.add(new ResourceID(3, 200, 0, "顏色", 2));
//
// // 捕蚊燈
// resourceIDs.add(new ResourceID(1, 201, 0, "開關", 1));
//
// // 電腦
// resourceIDs.add(new ResourceID(1, 202, 0, "電腦", 1));
//
// // 顯示器
// resourceIDs.add(new ResourceID(1, 203, 0, "顯示器", 1));
//
// // 顯示器
// resourceIDs.add(new ResourceID(1, 204, 0, "MAC", 0));
// resourceIDs.add(new ResourceID(2, 204, 0, "語音指令", 0));
//
// // 暖氣機
// resourceIDs.add(new ResourceID(1, 106, 0, "開關", 1));
// resourceIDs.add(new ResourceID(2, 106, 0, "送風/暖氣/溫度", 1));
// resourceIDs.add(new ResourceID(3, 106, 0, "溫度+", 1));
// resourceIDs.add(new ResourceID(4, 106, 0, "溫度-", 1));
// resourceIDs.add(new ResourceID(5, 106, 0, "旋轉", 1));
// }
//
// private void inittypeid() {
// typeIDs.add(new TypeID(8, 0, "空氣清淨機"));
// typeIDs.add(new TypeID(15, 0, "電風扇"));
// typeIDs.add(new TypeID(100, 0, "智慧插座"));
// typeIDs.add(new TypeID(101, 0, "果汁機"));
// typeIDs.add(new TypeID(102, 0, "Kinect/vedioCam/webservice"));
// typeIDs.add(new TypeID(104, 0, "掃地機"));
// typeIDs.add(new TypeID(105, 0, "咖啡機"));
// typeIDs.add(new TypeID(106, 0, "暖氣機"));
// // typeIDs.add(new TypeID(107, 0, "???"));
//
// typeIDs.add(new TypeID(120, 0, "感測器+致動器"));
// typeIDs.add(new TypeID(121, 0, "感測器"));
// typeIDs.add(new TypeID(122, 0, "致動器"));
//
// typeIDs.add(new TypeID(200, 0, "檯燈"));
// typeIDs.add(new TypeID(201, 0, "捕蚊燈"));
// typeIDs.add(new TypeID(202, 0, "電腦"));
// typeIDs.add(new TypeID(203, 0, "顯示器"));
// typeIDs.add(new TypeID(204, 0, "手機"));
//
// typeIDs.add(new TypeID(300, 0, "充電器"));
// }
//
// private void initclassid() {
// classIDs.add(new ClassID(0, "家電類"));
// }
//
// public ResourceID getResourceData(String typeid, String resourceid) {
// ResourceID resourceData = null;
//
// for (ResourceID data : resourceIDs) {
// String data_typeid = "" + data.getTypeId();
// String data_resourceid = "" + data.getId();
// if (data_typeid.equals(typeid) && data_resourceid.equals(resourceid)) {
// resourceData = data;
// break;
// }
// }
//
// return resourceData;
// }
//
// public List<ResourceID> getResourceData(String typeid) {
// List<ResourceID> resourceData = new ArrayList<>();
//
// for (ResourceID data : resourceIDs) {
// String data_typeid = "" + data.getTypeId();
// if (data_typeid.equals(typeid)) {
// resourceData.add(data);
// }
//
// }
//
// return resourceData;
// }
//
// public TypeID getTypeData(String typeid) {
// TypeID typeData = null;
//
// for (TypeID data : typeIDs) {
// String data_typeid = "" + data.getId();
// if (data_typeid.equals(typeid)) {
// typeData = data;
// break;
// }
//
// }
//
// return typeData;
// }
//
// public List<TypeID> getTypeIDs() {
// return typeIDs;
// }
//
// public List<ResourceID> getResourceIDs() {
// return resourceIDs;
// }
//
// public void setResourceIDs(List<ResourceID> resourceIDs) {
// this.resourceIDs = resourceIDs;
// }
//
// public void setTypeIDs(List<TypeID> typeIDs) {
// this.typeIDs = typeIDs;
// }
//
// }

