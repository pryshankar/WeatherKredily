package com.android.weatherkredily.data.local

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


object Converter {

    @TypeConverter
    fun fromImageToByteArray(img: Bitmap) =  getImageAsByteArray(img)

    @TypeConverter
    fun fromByteArrayToImage(byteArray: ByteArray) = getByteArrayAsImage(byteArray)


    private fun getImageAsByteArray(bitmap : Bitmap) : ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    private fun getByteArrayAsImage(byteArray : ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

}