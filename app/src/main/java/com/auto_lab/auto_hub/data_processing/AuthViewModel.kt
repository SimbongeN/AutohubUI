package com.auto_lab.auto_hub.data_processing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auto_lab.auto_hub.data_processing.api.AuthApi
import com.auto_lab.auto_hub.data_processing.api.RetrofitInstance
import com.auto_lab.auto_hub.data_processing.api.authModel.authModel
import com.auto_lab.auto_hub.data_processing.api.authModel.recoverModel
import com.auto_lab.auto_hub.data_processing.api.authModel.registrationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private lateinit var authApi_login: AuthApi
    private val authApi_Reg = RetrofitInstance.withoutAuthApi
    private val authApi_emailVerification = RetrofitInstance.withoutAuthApi

    private val _AuthResult = MutableStateFlow(false)
    val AuthResult : StateFlow<Boolean> = _AuthResult

    // implement authentication methods
    fun autheticate_Login(email:String, password:String,callback: (Boolean, String) -> Unit) {
        authApi_login = RetrofitInstance.getAuth(email, password)

        viewModelScope.launch {
            _AuthResult.value = true
            try {
                val response = authApi_login.autheticateUser(email)
                val userInformation: authModel ?= response.body()
                _AuthResult.value = false
                if (response.isSuccessful) {
                    callback(true,"successful:${userInformation?.name}")
                } else {
                    callback(false,"Invalid email or password")
                }
            } catch (e: Exception) {
                _AuthResult.value = false
                callback(false,"Network error")
            }
        }
    }

    fun autheticate_register(name:String, surname: String, email: String, password: String,callback: (Boolean, String) -> Unit){
        val createUser = registrationModel(name,surname,email,password)
        viewModelScope.launch{
            _AuthResult.value = true
            try {
                val response = authApi_Reg.createUser(createUser.name,createUser.surname,createUser.email,createUser.password)
                _AuthResult.value = false
                if(response.isSuccessful){
                    callback(true,"successful")
                }else{
                    callback(false,"user already exist")
                }
            }catch (e: Exception){
                _AuthResult.value = false
                callback(false,"Network error")
            }
        }
    }

    fun verification(email:String,callback: (Boolean, String) -> Unit){
        viewModelScope.launch{
            _AuthResult.value = true
            try{
                val response = authApi_emailVerification.checkEmailVerification(email)
                _AuthResult.value = false
                if (response.isSuccessful){
                    val body = response.body()
                    callback(body == true,"successful")
                }else{
                    callback(false,"Email not found")
                }
            }catch (e: Exception){
                _AuthResult.value = false
                Log.i("api response2", "errer",e)
                callback(false, "Network error")
            }
        }
    }

    fun recoverAccount(email:String,callback: (Boolean, String) -> Unit){
        viewModelScope.launch{
            _AuthResult.value = true
            try {
                val response = authApi_emailVerification.recoverAccount(email)
                _AuthResult.value = false
                if(response.isSuccessful){
                    val Result: recoverModel ?= response.body()
                    callback(true,Result?.status.toString())
                }else{
                    callback(false, "Account doesn't exist")
                }
            }catch (e: Exception){
                _AuthResult.value = false
                callback(false, "Network issue")
            }
        }
    }

    fun updateUser(email: String,password: String,callback: (Boolean, String) -> Unit){
        viewModelScope.launch{
            _AuthResult.value = true
            try {
                val response = authApi_Reg.updateUser(email,password)
                _AuthResult.value = false
                if(response.isSuccessful){
                    callback(true, "successful")
                }else{
                    callback(false, "incomplete action")
                }
            }catch (e: Exception){
                _AuthResult.value = false
                callback(false, "Network issue")
            }
        }
    }

    fun deleteAccount(email: String, password: String, callback: (Boolean, String) -> Unit){
        authApi_login = RetrofitInstance.getAuth(email,password)
        viewModelScope.launch{
            _AuthResult.value = true
            try{
                val response = authApi_login.deleteAccount(email)
                _AuthResult.value = false
                if(response.isSuccessful){
                    callback(true,"Account deleted successfully")
                }else{
                    callback(false,"failed to delete account")
                }
            }catch (e: Exception){
                _AuthResult .value = false
                callback(false,"network error")
            }
        }
    }

}