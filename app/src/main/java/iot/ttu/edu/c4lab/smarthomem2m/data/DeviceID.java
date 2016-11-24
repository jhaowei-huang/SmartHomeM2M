package iot.ttu.edu.c4lab.smarthomem2m.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2014/11/5.
 */
public class DeviceID extends Entity implements Parcelable {
    private long id;
    private String name;
    private int type; //actuator = 1,sensor = 0

    public DeviceID() {
    }

    public DeviceID(long id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }


    public DeviceID(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<DeviceID> CREATOR = new Creator<DeviceID>() {
        @Override
        public DeviceID createFromParcel(Parcel in) {
            return new DeviceID(in);
        }

        @Override
        public DeviceID[] newArray(int size) {
            return new DeviceID[size];
        }
    };

    @Override
    public String toString() {
        return "Ip{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        Log.i(MainActivity.LOGTAG, "writeToParcel");
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeInt(type);
    }

}

