package com.asadmshah.simplenotetaker.models;

import android.os.Parcelable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Note implements Parcelable {

    public abstract long id();
    public abstract String title();
    public abstract String text();
    public abstract long createdOn();

    public static Note create(long id, String title, String text, long createdOn) {
        return new AutoParcel_Note(id, title, text, createdOn);
    }

}
