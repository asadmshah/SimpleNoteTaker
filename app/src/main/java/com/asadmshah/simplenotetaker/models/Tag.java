package com.asadmshah.simplenotetaker.models;

import android.os.Parcelable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Tag implements Parcelable {

    public abstract long id();
    public abstract String label();
    public abstract int color();

    public static Tag create(long id, String label, int color) {
        return new AutoParcel_Tag(id, label, color);
    }

}
