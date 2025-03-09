package com.auto_lab.auto_hub.settings_profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.R
import com.auto_lab.auto_hub.data_processing.AuthViewModel
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.login_and_reg.CircularLoadingDialog
import com.auto_lab.auto_hub.navigationController.Screens
import com.auto_lab.auto_hub.navigationController.navBar
import com.auto_lab.auto_hub.ui.theme.CardBackGroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController,userData: UserData,context: Context,authViewModel: AuthViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardBackGroundColor, // Background color
                    titleContentColor = Color.White, // Title text color
                    actionIconContentColor = Color.White // Action icons color
                )
            )
        },
        bottomBar = {
            navBar(navController)
        }
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = com.auto_lab.auto_hub.R.drawable.user), // Replace with your vector asset
                contentDescription = "Profile Picture",
                modifier = Modifier.size(128.dp).padding(top = 32.dp)
            )
            ProfileOption(navController,userData,context,authViewModel,"Create Lecture TimeTable")
            ProfileOption(navController,userData,context,authViewModel,"Delete Current Timetable")
            ProfileOption(navController,userData,context,authViewModel,"Delete Exam TimeTable")
            ProfileOption(navController,userData,context,authViewModel,"Delete Account")
        }
    }
}

@Composable
fun ProfileOption(navController: NavController,userData: UserData,context: Context,authViewModel: AuthViewModel,text: String) {
    var showDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val isLoading = authViewModel.AuthResult.collectAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        TextButton(
            onClick = {
                if(text == "Create Lecture TimeTable"){
                    navController.navigate(Screens.CampusDetails.route)
                }
                if(text == "Delete Current Timetable"){
                    //clear current timetable from local storage
                    userData.writeData(R.string.timetable_monday.toString(),context,"")
                    userData.writeData(R.string.timetable_tuesday.toString(),context,"")
                    userData.writeData(R.string.timetable_wednesday.toString(),context,"")
                    userData.writeData(R.string.timetable_thursday.toString(),context,"")
                    userData.writeData(R.string.timetable_friday.toString(),context,"")
                    Toast.makeText(context, "Lecture timetable deleted successfully", Toast.LENGTH_LONG).show()
                }

                if(text == "Delete Account"){
                    //delete user account and send user back to login screen
                    showDialog = true
                }

                if(text=="Delete Exam TimeTable"){
                    userData.writeData(R.string.ExamData.toString(),context,"")
                    Toast.makeText(context, "Exam timetable deleted successfully", Toast.LENGTH_LONG).show()
                }


            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = text,
            )
        }
    }

    // Dialog popup
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Enter account details") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Display feedback message
                    feedbackMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = it,
                            color = if (it.startsWith("Success")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val readUserData = userData.readData(R.string.user_information.toString(),context)
                        val email = if(readUserData != "failed" || !readUserData.isEmpty()){
                            readUserData.split(":")[1]
                        }else{
                            ""
                        }
                        val password = if(readUserData != "failed" || !readUserData.isEmpty()){
                            readUserData.split(":")[2]
                        }else{
                            ""
                        }
                        if(email.isNotEmpty() || password.isNotEmpty()){
                            authViewModel.deleteAccount(email,password){
                                isSuccessful, message ->
                                if(isSuccessful){
                                    Toast.makeText(context, "Account Deleted successfuly", Toast.LENGTH_SHORT).show()
                                    userData.writeData(R.string.user_information.toString(),context,"")
                                    userData.writeData(R.string.remember_me.toString(),context,"false")
                                    navController.navigate(Screens.Login.route){
                                        popUpTo(navController.graph.id){
                                            inclusive = true
                                        }
                                    }
                                }else{
                                    feedbackMessage = "unable to delete user pls try again later"
                                }
                            }
                        }
                        showDialog = false
                              },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Delete")
                }
            }
        )
    }

    if (isLoading.value) {
        CircularLoadingDialog(isLoading.value)
    }
}


