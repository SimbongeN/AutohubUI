package com.auto_lab.auto_hub.login_and_reg

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.data_processing.AuthViewModel
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.navigationController.Screens
import com.auto_lab.auto_hub.ui.theme.BackGroundTheme
import com.auto_lab.auto_hub.ui.theme.CardBackGroundColor
import com.auto_lab.auto_hub.ui.theme.HeaderTextColor

//implement logic for recovering account

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun recoverScreen(navController: NavController,authViewModel: AuthViewModel){
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    val isLoading = authViewModel.AuthResult.collectAsState()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "RECOVERY",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardBackGroundColor, // Background color
                    titleContentColor = Color.White, // Title text color
                )
            )
        },
    ){
            innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding).fillMaxWidth(),
        ) {
            //adding background image
            BackGroundTheme()

            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp).verticalScroll(scrollState).imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ){
                Text(
                    text = "Account Recovery",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    color = HeaderTextColor,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Please enter your email",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().alpha(0.6f)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = {
                        Text("Email")
                    },
                    singleLine = true,
                    isError = errorEmail
                )
                if(errorEmail){

                    Text(
                        text = "Required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
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

                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    Button(
                        //send user back to login screen
                        onClick = {
                            navController.navigate(Screens.Login.route){
                                popUpTo(navController.graph.id){
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Back"
                        )
                    }

                    Button(
                        //verify user email
                        onClick = {
                            errorEmail = email.isEmpty()
                            if(errorEmail){
                                feedbackMessage = "Please provide email"
                            }else{
                                //verify userEmail
                                authViewModel.recoverAccount(email.trim()){
                                    isSuccessful, message ->
                                    if(isSuccessful){
                                        navController.navigate("confirmVerificationCode/${message.trim()}/${email.trim()}")
                                    }else{
                                        feedbackMessage = message
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "submit"
                        )
                    }

                }

                if (isLoading.value) {
                    CircularLoadingDialog(isLoading.value)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun verifyCode(navController: NavController,email: String,ConfirmVerificationCode: String){
    val scrollState = rememberScrollState()
    var verificationCode by remember { mutableStateOf("") }
    var errorVerificationCode by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }


    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "RECOVERY",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardBackGroundColor, // Background color
                    titleContentColor = Color.White, // Title text color
                )
            )
        },
    ){
            innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding).fillMaxWidth()
        ) {
            //adding background image
            BackGroundTheme()

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).imePadding().padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                Text(
                    text = "Account Recovery",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    color = HeaderTextColor,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Verification code has been sent to $email",
                    modifier = Modifier.alpha(0.6f)
                )

                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = {
                        verificationCode = it
                    },
                    label = {
                        Text("Verification code")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = errorVerificationCode
                )
                if(errorVerificationCode){

                    Text(
                        text = "Required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Display feedback message
                feedbackMessage?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = it,
                        color = if (it.isEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    Button(
                        //send user back to login screen
                        onClick = {
                            navController.navigate(Screens.accountRecover.route)
                        }
                    ) {
                        Text(
                            text = "Back"
                        )
                    }
                    Button(
                        //verify user email
                        onClick = {
                            errorVerificationCode = verificationCode.isEmpty()
                            if(errorVerificationCode){
                                feedbackMessage = "Please provide verification code"
                            }else{
                                //send user to password screen
                                if(verificationCode.trim() == ConfirmVerificationCode.trim()){
                                    navController.navigate("userDetail/$email")
                                }else{
                                    feedbackMessage = "invalid verification code"
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "submit"
                        )
                    }

                }
            }
        }
    }
}


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updateUserInfor(navController: NavController,authViewModel: AuthViewModel,userData: UserData,context: Context,email: String){
    val scrollState = rememberScrollState()
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf(false) }
    var errorConfirmPassword by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    val isLoading = authViewModel.AuthResult.collectAsState()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "RECOVERY",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardBackGroundColor, // Background color
                    titleContentColor = Color.White, // Title text color
                )
            )
        },
    ){
            innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding).fillMaxWidth()
        ) {
            //adding background image
            BackGroundTheme()

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).imePadding().padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                Text(
                    text = "Account Recovery",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    color = HeaderTextColor,
                    modifier = Modifier.fillMaxWidth()
                )

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = {
                            Text("Password")
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errorPassword
                    )
                    if(errorPassword){

                        Text(
                            text = "Required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                        },
                        label = {
                            Text("Confirm Password")
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errorConfirmPassword
                    )
                    if(errorConfirmPassword){

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    Button(
                        //send user back to login screen
                        onClick = {
                            navController.navigate(Screens.accountRecover.route)
                        }
                    ) {
                        Text(
                            text = "Back"
                        )
                    }

                    Button(
                        //verify user email
                        onClick = {
                            errorPassword = password.isEmpty()
                            errorConfirmPassword = confirmPassword.isEmpty()
                            if(errorPassword || errorConfirmPassword){
                                feedbackMessage = "Please fill all required items"
                            }else{
                                if(password != confirmPassword){
                                    feedbackMessage = "Passwords do not match"
                                }else{
                                    //save password to localStorage
                                    val currentInfor  = userData.readData(com.auto_lab.auto_hub.R.string.user_information.toString(),context)
                                    var arrayOfInfor = mutableListOf<String>()
                                    if(currentInfor.contains(":")){
                                        arrayOfInfor = currentInfor.split(":").toMutableList()
                                        arrayOfInfor.removeAt(arrayOfInfor.size-1)
                                        arrayOfInfor.addLast(password)
                                    }
                                    var save = ""
                                    for(i in arrayOfInfor){
                                        save += "$i:"
                                    }
                                    save = save.dropLast(1)

                                    userData.writeData(com.auto_lab.auto_hub.R.string.user_information.toString(),context,save)

                                    //update user information and send them back to login screen
                                    authViewModel.updateUser(email,password){
                                            isSuccessful, message ->
                                        if(isSuccessful){
                                            Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                            navController.navigate(Screens.Login.route)
                                        }else{
                                            feedbackMessage = "Something went wrong Please try again"
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "submit"
                        )
                    }

                }

                if (isLoading.value) {
                    CircularLoadingDialog(isLoading.value)
                }
            }
        }
    }
}