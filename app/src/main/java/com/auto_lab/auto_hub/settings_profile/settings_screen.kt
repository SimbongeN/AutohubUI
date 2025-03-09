package com.auto_lab.auto_hub.settings_profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.navigationController.Screens
import com.auto_lab.auto_hub.navigationController.navBar
import com.auto_lab.auto_hub.ui.theme.CardBackGroundColor
import com.auto_lab.auto_hub.ui.theme.HeaderTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
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
            Text(
                text = "Personal Area",
                fontSize = 28.sp,
                color = HeaderTextColor,
                modifier = Modifier.padding(top = 16.dp)
            )
            SettingItem(navController,"Change Password")
        }
    }
}

@Composable
fun SettingItem(navController: NavController,text: String) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        TextButton(
            onClick = {
                //send user to recover screen
                if(text == "Change Password"){
                    showDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = text,
                color =  Color.Black,
                modifier = Modifier.alpha(0.8f)
            )
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
                        text = "You will be logged out and sent to recovery screen to change your password"
                    )

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
                        //move user to recover screen
                        navController.navigate(Screens.accountRecover.route){
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }
                        showDialog = false
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Continue")
                }
            }
        )
    }
}
