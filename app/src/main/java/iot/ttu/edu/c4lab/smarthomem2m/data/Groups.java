package iot.ttu.edu.c4lab.smarthomem2m.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2014/11/5.
 */
public class Groups extends Entity implements Parcelable {
    private long id;
    private String name;


    public Groups() {
    }


    public Groups(long id, String name) {
        this.id = id;
        this.name = name ;
    }


    public Groups(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<Groups> CREATOR = new Creator<Groups>() {
        @Override
        public Groups createFromParcel(Parcel in) {
            return new Groups(in);
        }

        @Override
        public Groups[] newArray(int size) {
            return new Groups[size];
        }
    };

    @Override
    public String toString() {
        return "Ip{" +
                "id=" + id +
                ", name='" + name + '\'' +
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        Log.i(MainActivity.LOGTAG, "writeToParcel");
        parcel.writeLong(id);

        parcel.writeString(name);
    }

}

