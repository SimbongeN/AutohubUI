package com.auto_lab.auto_hub.login_and_reg

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.auto_lab.auto_hub.LocationTask.LocationHelper
import com.auto_lab.auto_hub.data_processing.AuthViewModel
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.navigationController.Screens
import com.auto_lab.auto_hub.ui.theme.*
import com.auto_lab.auto_hub.ui.theme.HeaderTextColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.regex.Pattern
import com.auto_lab.auto_hub.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel, userData: UserData,context: Context){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false)}
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val isLoading = authViewModel.AuthResult.collectAsState()

    val scrollState = rememberScrollState()

    var selectedUserType by remember { mutableStateOf(false) }

    var numberOfLoginTries by remember { mutableIntStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }

    val permissions = listOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
    var showDialog2 by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        if(!isInternetAvailable(context)){
            showDialog = true
        }


        when{
            !permissionState.allPermissionsGranted ->{
                showDialog2 = true
            }
        }
    }


    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AUTOHUB",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HeaderTextColor, // Background color
                    titleContentColor = Color.White, // Title text color
                ),
            )
        },


    ){
            innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            //adding background theme
            BackGroundTheme()

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_image),
                    contentDescription = "login image",
                    modifier = Modifier.size(200.dp)
                )

                Text(
                    text = "Welcome back : )",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeaderTextColor,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Login to your account",
                    color = informationTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            //isValid = android.util.Patterns.EMAIL_ADDRESS.matcher().matches()
                        },
                        label = {
                            Text(text ="Email address")
                        },
                        singleLine = true,
                        isError = errorEmail
                    )
                    if (errorEmail) {
                        Text(
                            text = "Required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = {
                            Text(text ="Password")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        isError = errorPassword
                    )
                    if (errorPassword) {
                        Text(
                            text = "Required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = (selectedUserType),
                        onClick = { selectedUserType = true }
                    )
                    Text(text = "Stay logged in")
                }

                // Display feedback message
                feedbackMessage?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = it,
                        color = if (it.startsWith("Success")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    //check all fields
                    errorEmail = email.isEmpty()
                    errorPassword = password.isEmpty()

                    if(errorEmail||errorPassword){
                        feedbackMessage= "Please fill in all required fields."
                    }
                    else if(!EmailVerifier.isValidEmail(email)){
                        errorEmail = true
                        feedbackMessage = "Please enter a valid email"
                    }
                    else {
                        if(numberOfLoginTries < 3){
                            authViewModel.autheticate_Login(
                                email.trim().lowercase(),
                                password.trim()
                            ) { isSuccess, message ->
                                if (isSuccess) {
                                    //perform user authentication on email and password
                                    //load user name from local data base via userData view model  if user doesnt exist but exist in sever means they deleted the app they must contact us
                                    var username = ""
                                    val getUserInformation = userData.readData(R.string.user_information.toString(),context)
                                    username = getUserInformation.toString().split(":")[0]

                                    if(selectedUserType){
                                        userData.writeData(R.string.remember_me.toString(),context,"true")
                                    }

                                    if(username.isEmpty() || username.isBlank() || username == "failed"){
                                        userData.writeData(R.string.user_information.toString(), context,"${message.split(":")[1]}:${email}:$password")
                                        navController.navigate(Screens.HomePage.route){
                                            popUpTo(navController.graph.id){
                                                inclusive = true
                                            }
                                        }
                                    }else{
                                        navController.navigate(Screens.HomePage.route){
                                            popUpTo(navController.graph.id){
                                                inclusive = true
                                            }
                                        }
                                    }
                                } else {
                                    numberOfLoginTries += 1
                                    feedbackMessage = message
                                }
                            }
                        }else{
                            feedbackMessage = "forgot password? Press on forgot password button"
                            Toast.makeText(context, "Login attempts exceeded", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text(text = "Login")
                }
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = {
                    //change screens to passwordRecovery screen
                    navController.navigate(Screens.accountRecover.route)
                }) {
                    Text(text="Forgot Password")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Don't have an account?",
                    color = informationTextColor
                )

                TextButton(onClick = {
                    //change screens to register screen
                    navController.navigate("Register_Screen")
                }) {
                    Text(text="Create an account",fontSize = 15.sp)
                }

                if (isLoading.value) {
                    CircularLoadingDialog(isLoading.value)
                }
            }
        }
    }

    // Dialog popup
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Notice",color = HeaderTextColor) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Text(
                        text = "Your data is turned off :("
                    )

                }
            },
            confirmButton = {

                Button(
                    onClick = {
                        //move user to recover screen
                        LocationHelper.openDataSettings(context)
                        showDialog = false
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Open settings")
                }
            }
        )
    }

    //show dialog for permissions
    if (showDialog2)
    {
        var showBtn by remember { mutableStateOf(false) }
        if(permissionState.allPermissionsGranted){
            showBtn = true
        }
        AlertDialog(
            onDismissRequest = { showDialog2 = false },
            title = {
                if(showBtn == true){
                    Text("Welcome to Autohub :)")
                }else{
                    Text(text = "Permissions Required")
                }
                    },
            text = {
                when {
                    permissionState.allPermissionsGranted ->{
                        Text("Hey I'm Autohub, Your own personal student assistance shall we get started on our journey")
                    }
                    permissionState.shouldShowRationale -> {
                        Text("This app requires permissions to function properly. Please grant them.")
                    }
                    else -> {
                        Text("Permissions were denied. You can enable them in the app settings.")
                    }
                }
            },
            confirmButton = {
                if(showBtn == true){
                    Button(onClick = {
                        showDialog2 = false
                    }) {
                        Text("Start Journey")
                    }
                }else{
                    Button(onClick = {
                        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Intent(Settings.ACTION_APPLICATION_SETTINGS)
                        } else {
                            Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS)
                        }
                        context.startActivity(intent)
                        showDialog2 = false
                    }) {
                        Text("Grant Permissions")
                    }
                }
            },
            dismissButton = {
                if(showBtn != true){
                    Button(onClick = { showDialog2 = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}

//email verification method
object EmailVerifier{
    private const val email_pattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}"

    fun isValidEmail(email : String): Boolean{
        val patten = Pattern.compile(email_pattern, Pattern.CASE_INSENSITIVE)
        val matcher = patten.matcher(email)
        return matcher.find()
    }
}

@Composable
fun CircularLoadingDialog(show: Boolean,onDismiss: (() -> Unit)? = null) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss?.invoke() }) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

