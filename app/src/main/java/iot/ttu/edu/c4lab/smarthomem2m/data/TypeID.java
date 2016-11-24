package iot.ttu.edu.c4lab.smarthomem2m.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2014/11/5.
 */
public class TypeID extends Entity implements Parcelable {
    private long id;
    private long classID;
    private String name;


    public TypeID() {
    }

    public TypeID(long id,long classID , String name) {
        this.id = id;
        this.classID = classID;
        this.name = name ;
    }


    public TypeID(Parcel in) {
        this.id = in.readLong();
        this.classID = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<TypeID> CREATOR = new Creator<TypeID>() {
        @Override
        public TypeID createFromParcel(Parcel in) {
            return new TypeID(in);
        }

        @Override
        public TypeID[] newArray(int size) {
            return new TypeID[size];
        }
    };

    @Override
    public String toString() {
        return "Ip{" +
                "id=" + id +
                ", classID='" + classID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public long getClassID() {
        return classID;
    }

    public void setClassID(long classID) {
        this.classID = classID;
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
        parcel.writeLong(classID);
        parcel.writeString(name);
    }

}

