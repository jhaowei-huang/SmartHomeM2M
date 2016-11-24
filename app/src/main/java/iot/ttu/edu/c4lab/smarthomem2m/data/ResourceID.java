package iot.ttu.edu.c4lab.smarthomem2m.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2014/11/5.
 */
public class ResourceID extends Entity implements Parcelable {
    private long id;
    private long classID;
    private long typeId;
    private String name;
    private int type; //actuator = 1,sensor = 0

    public ResourceID() {
    }

    public ResourceID(long id,long typeId ,long classID, String name,int type) {
        this.id = id;
        this.typeId = typeId;
        this.classID = classID;
        this.name = name;
        this.type = type;
    }


    public ResourceID(Parcel in) {
        this.id = in.readLong();
        this.typeId = in.readLong();
        this.classID = in.readLong();
        this.name = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<ResourceID> CREATOR = new Creator<ResourceID>() {
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
        return "Ip{" +
                "id=" + id +
                ", typeId='" + typeId + '\'' +
                ", classID='" + classID + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public long getClassID() {
        return classID;
    }

    public void setClassID(long classID) {
        this.classID = classID;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
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
        parcel.writeLong(typeId);
        parcel.writeLong(classID);
        parcel.writeString(name);
        parcel.writeInt(type);
    }

}

