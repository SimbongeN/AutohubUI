package com.auto_lab.auto_hub.data_processing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auto_lab.auto_hub.data_processing.api.RetrofitInstance
import com.auto_lab.auto_hub.data_processing.api.TimetableApi
import com.auto_lab.auto_hub.data_processing.api.timetableData.campusModules
import com.auto_lab.auto_hub.data_processing.api.timetableData.examData
import com.auto_lab.auto_hub.data_processing.api.timetableData.urlModel
import com.auto_lab.auto_hub.data_processing.api.timetableData.userDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class TimeTableData: ViewModel() {
    private lateinit var timetableAPI: TimetableApi
    private val campusApi = RetrofitInstance.withoutAuthApiTimetable

    private val _ApiResult = MutableStateFlow(false)
    val ApiResult : StateFlow<Boolean> = _ApiResult

    fun getURLS(email: String,password: String, moduleCodes: String, campus: String, callback: (String, String) -> Unit){
        timetableAPI = RetrofitInstance.getTimetableApi(email,password)

        viewModelScope.launch{
            _ApiResult.value = true
            try {
                val response = timetableAPI.getUserModuleUrls(moduleCodes,campus)
                _ApiResult.value = false
                if (response.isSuccessful){
                    val urls: urlModel ?= response.body()
                    urls?.let {
                        callback(urls.userUrls ,"successful")
                    }
                }else{
                    callback(response.toString(),"failed")
                }
            }catch (e: Exception){
                Log.i("userUrls", "errorMessage",e)
                _ApiResult.value = false
                callback("error","network error")
            }
        }
    }

    fun getTimetableData(email: String,password: String,modulesUrls: String,callback: (userDataModel?, String) -> Unit){
        timetableAPI = RetrofitInstance.getTimetableApi(email,password)
        viewModelScope.launch{
            _ApiResult.value = true
            try {
                val response: Response<userDataModel> = timetableAPI.getTimetableDetails(modulesUrls)
                _ApiResult.value = false
                if (response.isSuccessful){
                    val userData: userDataModel ?=response.body()
                    callback(userData,"successful")
                }else{
                    callback(null,"failed")
                }
            }catch (e: Exception){
                Log.i("userModules","error",e)
                _ApiResult.value = false
                callback(null,"network error")
            }
        }
    }

    fun getCampusData(campus: String,callback: (String, Boolean) -> Unit){
        viewModelScope.launch{
            _ApiResult.value = true
            try{
                val response = campusApi.getCampusModules(campus)
                _ApiResult.value = false
                if(response.isSuccessful){
                    val sendResponse : campusModules  ?= response.body()
                    callback(sendResponse?.campusModule.toString(),true)
                }else{
                    callback(response.body().toString(),false)
                }
            }catch (e: Exception){
                _ApiResult.value = false
                callback("error",false)
            }
        }
    }

    //implement full logic before end of feb
    fun getExamTimeTableInfo(email:String,password: String,campus: String, moduleCodes: String,callback: (Boolean, examData?) -> Unit){
        timetableAPI = RetrofitInstance.getTimetableApi(email,password)
        viewModelScope.launch{
            _ApiResult.value = true
            try{
                val response = timetableAPI.getExamDetails(moduleCodes,campus)
                _ApiResult.value = false
                if(response.isSuccessful){

                    val examResponse: examData ?= response.body()
                    callback(true,examResponse)
                }else
                {
                    val examResponse = examData()
                    callback(false,examResponse)
                }
            }catch (e: Exception){
                _ApiResult.value = false
                val examResponse = examData()
                callback(false,examResponse)
            }
        }
    }

    //save timetable data
    //implement full logic before end of feb
    fun saveTimeTableData(email:String,password: String, lectureTimeTableData: String,callback: (Boolean, String?) -> Unit){
        timetableAPI = RetrofitInstance.getTimetableApi(email,password)
        viewModelScope.launch{
            _ApiResult.value = true
            try{
                val response = timetableAPI.saveLectureTimeTable(email,lectureTimeTableData)
                _ApiResult.value = false
                if(response.isSuccessful){
                    val examResponse: String ?= response.body()
                    callback(true,examResponse)
                }else
                {
                    val examResponse = ""
                    callback(false,examResponse)
                }
            }catch (e: Exception){
                _ApiResult.value = false
                val examResponse = ""
                callback(false,examResponse)
            }
        }
    }
}