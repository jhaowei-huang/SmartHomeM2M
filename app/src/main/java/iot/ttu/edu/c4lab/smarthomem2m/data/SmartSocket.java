package iot.ttu.edu.c4lab.smarthomem2m.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pcuser on 2016/4/11.
 */
public class SmartSocket implements Parcelable {
    private String mac;
    private String ip;

    public SmartSocket() {
    }

    public  SmartSocket(String mac,String ip) {
        this.mac =mac;
        this.ip = ip;
    }


    public SmartSocket(Parcel in) {
        this.mac = in.readString();
        this.ip = in.readString();
    }

    public static final Parcelable.Creator<ResourceID> CREATOR = new Parcelable.Creator<ResourceID>() {
        @Override
        public ResourceID createFromParcel(Parcel in) {
            return new ResourceID(in);
        }

        @Override
        public ResourceID[] newArray(int size) {
            return new ResourceID[size];
        }
    };

    @Override
    public String toString() {
        return "SmartSocket{" +
                "mac=" + mac +
                ", id='" + ip + '\'' +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String Mac) {
        this.mac = mac;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        Log.i(MainActivity.LOGTAG, "writeToParcel");
        parcel.writeString(mac);
        parcel.writeString(ip);
    }
}
