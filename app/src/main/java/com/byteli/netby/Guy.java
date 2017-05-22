package com.byteli.netby;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by byte on 2/1/16.
 */
public class Guy implements Parcelable {
    public int id;
    public String name;

    public static final Parcelable.Creator<Guy> CREATOR = new Parcelable.Creator<Guy>(){
        @Override
        public Guy createFromParcel(Parcel in){
            return new Guy(in);
        }
        @Override
        public Guy[] newArray(int size){
            return new Guy[size];
        }
    };

    public Guy(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Guy(Parcel source){
        this.id = source.readInt();
        this.name = source.readString();
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
