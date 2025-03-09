package com.auto_lab.auto_hub.login_and_reg

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.data_processing.AuthViewModel
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.navigationController.Screens
import com.auto_lab.auto_hub.ui.theme.*
import com.auto_lab.auto_hub.ui.theme.HeaderTextColor
import com.auto_lab.auto_hub.R

//@Preview(showBackground = true)
//@Preview(name = "Full Preview", showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, userData: UserData, authViewModel: AuthViewModel,context: Context){
    var name by remember { mutableStateOf("") }
    var errorName by remember {mutableStateOf(false)}

    var surname by remember { mutableStateOf("") }
    var errorSurname by remember {mutableStateOf(false)}

    var email by remember { mutableStateOf("") }
    var errorEmail by remember {mutableStateOf(false)}

    var password by remember { mutableStateOf("") }
    var errorPassword by remember {mutableStateOf(false)}

    var confirmPassword by remember { mutableStateOf("") }
    var errorConfirmPassword by remember {mutableStateOf(false)}

    // State for feedback message
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val isLoading = authViewModel.AuthResult.collectAsState()

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                )
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
                    painter = painterResource(id = R.drawable.registration_image),
                    contentDescription = "Registration image",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Register Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeaderTextColor,
                    textAlign = TextAlign.Left
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Create a new account",
                    color = informationTextColor,
                    textAlign = TextAlign.Left
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row{
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text(text ="Name")
                        },
                        isError = errorName
                    )

                    if (errorName) {
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
                        value = surname,
                        onValueChange = {
                            surname = it
                        },
                        label = {
                            Text(text ="Surname")
                        },
                        isError =  errorSurname,
                        singleLine = true
                    )

                    if (errorSurname) {
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
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text(text ="Email address")
                        },
                        isError = errorEmail,
                        singleLine = true
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
                        isError = errorPassword,
                        singleLine = true
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

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                        },
                        label = {
                            Text(text ="Confirm password")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errorConfirmPassword,
                        singleLine = true
                    )

                    if (errorConfirmPassword) {
                        Text(
                            text = "Required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
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
                    //check validation
                    errorConfirmPassword = confirmPassword.isEmpty()
                    errorPassword = password.isEmpty()
                    errorEmail = email.isEmpty()
                    errorSurname = surname.isEmpty()
                    errorName = name.isEmpty()

                    //check if all input fields are not empty
                    if(errorConfirmPassword || errorPassword || errorEmail || errorSurname || errorName){
                        feedbackMessage = "Please fill in all required fields."
                    }else if(password != confirmPassword){
                        feedbackMessage = "Passwords do not match"
                    }else if(!EmailVerifier.isValidEmail(email)){
                        errorEmail = true
                        feedbackMessage = "Please enter a valid email"
                    }
                    else {

                        //save user name to local data storage using userData View model
                        val saveUserContent = "$name:$email:$password"
                        userData.writeData(R.string.user_information.toString(),context,saveUserContent)
                        userData.writeData(R.string.timetable_monday.toString(),context,"")
                        userData.writeData(R.string.timetable_tuesday.toString(),context,"")
                        userData.writeData(R.string.timetable_wednesday.toString(),context,"")
                        userData.writeData(R.string.timetable_thursday.toString(),context,"")
                        userData.writeData(R.string.timetable_friday.toString(),context,"")

                        //set rememebr me to false
                        userData.writeData(R.string.remember_me.toString(),context,"false")

                        if(password.length < 5 || confirmPassword.length <5){
                            feedbackMessage = "password length too short at least 5 characters"
                            Toast.makeText(context, "Invalid password length", Toast.LENGTH_SHORT).show()
                        }else{
                            //call user data model and pass user name
                            authViewModel.autheticate_register(
                                name.trim().lowercase(),
                                surname.trim().lowercase(),
                                email.trim().lowercase(),
                                password.trim()
                            ){
                                    isSuccessful, message ->
                                if(isSuccessful){
                                    //implement full logic
                                    navController.navigate("Verification_Screen/$email")
                                }else{
                                    feedbackMessage = message
                                }
                            }
                        }

                    }

                }) {
                    Text(text = "Register")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Have an account?",
                    color = informationTextColor
                )
                TextButton(onClick = {
                    navController.navigate(Screens.Login.route){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                }) {
                    Text(text="Login", fontSize = 20.sp)
                }

                if(isLoading.value){
                    CircularLoadingDialog(isLoading.value)
                }
            }
        }
    }
}

//don't allow user to pass this screen if they haven't been verified
//registration result
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registration_result(navController: NavController,result: String, authViewModel: AuthViewModel,context: Context){
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val isLoading = authViewModel.AuthResult.collectAsState()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
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
                )
            )
        },
    ){
            innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            //adding background theme
            BackGroundTheme()

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (result.contains("fail")){
                    Image(
                        painter = painterResource(id= R.drawable.failed_reg_image),
                        contentDescription = "Registration failed image",
                        modifier = Modifier.size(200.dp)
                    )

                    // Display feedback message
                    feedbackMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = it,
                            color = if (it.startsWith("successful")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Registration ${result.split("+")[0]} please click on the link sent to your email",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Button(onClick = {
                            //send user back to registration page
                            navController.navigate("Register_Screen")
                        }
                        ) {
                            Text(text = "Change Email")
                        }
                        Button(
                            onClick = {
                                //make sure user email is verified before proceeding to login page
                                //use the auth view model to check user has verfied after pressing the retry button
                                val userEmail = result.split("+")[1]
                                authViewModel.verification(userEmail){
                                        isVerified, message ->
                                    Log.i("isVerified", "$isVerified----$message")
                                    if(isVerified){
                                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                                        navController.navigate("login_Screen")
                                    }else{
                                        feedbackMessage = "Please verify your email"
                                    }
                                }
                            }
                        ) {
                            Text(text = "Retry")
                        }
                    }

                }else{
                    Image(
                        painter = painterResource(id= R.drawable.successfull_reg_image),
                        contentDescription = "Registration failed image",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Registration $result",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(onClick = {
                        //redirect user to login screen once email has been verified
                        //for now redirect
                        navController.navigate("login_Screen")
                    }) {
                        Text(text = "Continue")
                    }
                    if(isLoading.value){
                        CircularLoadingDialog(isLoading.value)
                    }
                }
            }
        }
    }
}


//send verification code to user
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(navController: NavController,userEmail: String, authViewModel: AuthViewModel){
    val isLoading = authViewModel.AuthResult.collectAsState()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
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
                )
            )
        },

    ){
            innerPadding ->
        Box (
            modifier = Modifier.padding(innerPadding)
        ){
            //add background theme
            BackGroundTheme()

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Email verification",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeaderTextColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "verification link has been sent to",
                    fontSize = 16.sp,
                    color = informationTextColor,
                )
                Text(
                    text = userEmail,
                    fontSize = 16.sp,
                    color = informationTextColor,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Please verify your email",
                    fontSize = 16.sp,
                    color =informationTextColor,
                )
                Text(
                    text = "press continue once verified",
                    fontSize = 16.sp,
                    color = informationTextColor,
                )
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(onClick = {
                        //send user back to registration page
                        navController.navigate("Register_Screen")
                    }
                    ) {
                        Text(text = "Change Email")
                    }
                    Button(onClick = {
                        //check the user email has been verified (use authview model to check whether user has been veriefd
                        authViewModel.verification(userEmail){
                                isVerified, message ->
                            Log.i("isVerified","$isVerified---$message")
                            if(isVerified){
                                val msgResponse =message
                                navController.navigate("RegisterResult_Screen/$msgResponse")
                            }else{
                                val msgResponse ="fail"
                                navController.navigate("RegisterResult_Screen/$msgResponse+$userEmail")
                            }
                        }

                    }) {
                        Text(text = "Continue")
                    }
                }
                if(isLoading.value){
                    CircularLoadingDialog(isLoading.value)
                }

            }
        }
    }
}