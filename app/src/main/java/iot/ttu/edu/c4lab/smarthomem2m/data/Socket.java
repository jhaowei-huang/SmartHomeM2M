package iot.ttu.edu.c4lab.smarthomem2m.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by pcuser on 2016/4/11.
 */
public class Socket implements Parcelable {
    private String ip;
    private long id;
    private String name;

    public Socket() {
    }

    public  Socket(String ip,long id,String name) {
        this.ip =ip;
        this.id = id;
        this.name = name;
    }


    public Socket(Parcel in) {
        this.ip = in.readString();
        this.id = in.readLong();
        this.name = in.readString();
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
        return "Socket{" +
                "ip=" + ip +
                ", id='" + id + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        Log.i(MainActivity.LOGTAG, "writeToParcel");
        parcel.writeString(ip);
        parcel.writeLong(id);
        parcel.writeString(name);
    }
}