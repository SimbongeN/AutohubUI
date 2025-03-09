package com.auto_lab.auto_hub.data_processing.api

import android.content.Context
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class localStorage {

    fun saveData(context: Context, filename: String, content: String): String{
        var response = "failed"
        try {
            val fos: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            fos.write(content.toByteArray())
            fos.flush()
            fos.close()
            response = "successful"
        }catch (e: IOException){
            return response
        }
        return response
    }


    fun readData(context: Context,filename: String): String{
        var response = "failed"
        try {
            val fin: FileInputStream = context.openFileInput(filename)
            var a: Int
            val temp = StringBuilder()
            while (fin.read().also { a = it } != -1) {
                temp.append(a.toChar())
            }

            response = temp.toString()
            fin.close()
        } catch (e: IOException) {
            return response
        }
        return response
    }
}