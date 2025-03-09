package com.auto_lab.auto_hub.data_processing

import android.content.Context
import androidx.lifecycle.ViewModel
import com.auto_lab.auto_hub.data_processing.api.localStorage

class UserData: ViewModel()  {
    private val LocalStorage = localStorage()

    fun writeData(filename: String, context: Context, content: String): String{
        return LocalStorage.saveData(context,filename,content)
    }

    fun readData(filename: String, context: Context): String{
        return LocalStorage.readData(context, filename)
    }
}