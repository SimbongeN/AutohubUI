package com.auto_lab.auto_hub.data_processing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auto_lab.auto_hub.data_processing.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class update_maintenance:  ViewModel() {
    private val updateApi = RetrofitInstance.withoutAuthApi
    private val _Result = MutableStateFlow(false)
    val Result : StateFlow<Boolean> = _Result

    fun checkforUpdate(callback: (Boolean, String) -> Unit){
        viewModelScope.launch{
            _Result.value = true
            try {
                val response = updateApi.checkNewRelease()
                _Result.value = false
                if(response.isSuccessful){
                    if(response.body().toString() == "true")
                        callback(true,"new release is out")
                    else
                        callback(false,"release not out")
                }else{
                    callback(false,"release not out")
                }
            }catch (e: Exception){
                _Result.value = false
                callback(false,"network error")
            }
        }
    }

    fun checkforMaintenance(callback: (Boolean, String) -> Unit){
        viewModelScope.launch{
            _Result.value = true
            try {
                val response = updateApi.checkMaintence()
                _Result.value = false
                if(response.isSuccessful){
                    if (response.body().toString() == "true")
                        callback(true,"theres maintenance")
                    else
                        callback(false,"no maintenance")
                }else{
                    callback(false,"no maintenance")
                }
            }catch (e: Exception){
                _Result.value = false
                callback(false,"network error")
            }
        }
    }

}