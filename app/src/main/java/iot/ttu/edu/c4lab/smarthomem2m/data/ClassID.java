package iot.ttu.edu.c4lab.smarthomem2m.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2014/11/5.
 */
public class ClassID extends Entity implements Parcelable {
    private long id;
    private String name;


    public ClassID() {
    }

    public ClassID(long id, String name) {
        this.id = id;
        this.name = name ;
    }


    public ClassID(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
    }

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

