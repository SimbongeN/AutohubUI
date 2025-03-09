package com.auto_lab.auto_hub.login_and_reg

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.navigationController.Screens
import kotlinx.coroutines.delay
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.collectAsState
import com.auto_lab.auto_hub.R
import com.auto_lab.auto_hub.data_processing.update_maintenance

@Composable
fun animatedSplashScreen(navController: NavController,userData: UserData,context: Context,updateMaintenance: update_maintenance){

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnimate = animateFloatAsState(
        targetValue = if(startAnimation) 1f else 0.4f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    val isLoading = updateMaintenance.Result.collectAsState()

    //to insure that user passes the update screen  make sure to check if the current sdk version is the same as the one thats released if not force user to update the app
    LaunchedEffect(key1 = true) {
        val skipLogin = userData.readData(R.string.remember_me.toString(),context)
        startAnimation = true
        delay(3000)
        navController.popBackStack()
        //check if notifications are not enabled send user to notification settings
        if(!isInternetAvailable(context) ){
            navController.navigate(Screens.Login.route)
        }
        else if(skipLogin == "true"){
            //before moving to homepage
            startAnimation = isLoading.value
            updateMaintenance.checkforUpdate {
                state, message ->
                startAnimation = !isLoading.value
                if(state){
                    //move to to update screen
                    navController.navigate(Screens.update.route)
                }else{
                    startAnimation = !isLoading.value
                    updateMaintenance.checkforMaintenance {
                        state2, message2 ->
                        startAnimation = !isLoading.value
                        if(state2){
                            //navigate to maintenance screen
                            navController.navigate(Screens.maintenance.route)
                        }else{
                            navController.navigate(Screens.HomePage.route)
                        }
                    }
                }
            }

        }else{
            navController.navigate(Screens.Login.route)
        }

    }
    splashScreen(alpha = alphaAnimate.value)

}

@Composable
fun splashScreen(alpha : Float){
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier.size(500.dp)
        ){
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxSize()
                    .alpha(alpha),
                painter = painterResource(com.auto_lab.auto_hub.R.drawable.autohub_splash),
                contentDescription = "autohub logo",
                tint = Color.Black
            )
        }
    }
}

@SuppressLint("ServiceCast")
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}
