package com.keskheu.api

import android.os.Parcel
import android.os.Parcelable

data class RetourApi(var retour:Int) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        retour = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(retour)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RetourApi> {
        override fun createFromParcel(parcel: Parcel): RetourApi {
            return RetourApi(parcel)
        }

        override fun newArray(size: Int): Array<RetourApi?> {
            return arrayOfNulls(size)
        }
    }
}