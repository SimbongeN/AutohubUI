package com.auto_lab.auto_hub.navigationController

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auto_lab.auto_hub.ui.theme.BackGroundTheme
import com.auto_lab.auto_hub.ui.theme.CardBackGroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updateScreen(context: Context){
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "UPDATE",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
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
            modifier = Modifier.padding(innerPadding),
        ) {
            BackGroundTheme()
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackGroundColor)
                        .padding(16.dp),

                    ) {
                    Column {
                        Text(
                            "Notice",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 28.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            "Theres a new update for Autohub please update the app for better user experience",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Button(
                    onClick = {
                        val urlIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.auto_lab.auto_hub")
                        )
                        context.startActivity(urlIntent)
                    }
                ) {
                    Text("Update Now")
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun maintenance(){
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "UPDATE",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
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
            modifier = Modifier.padding(innerPadding),
        ) {
            BackGroundTheme()
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackGroundColor)
                        .padding(16.dp),

                    ) {
                    Column {
                        Text(
                            "Notice",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 28.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            "Currently the server is down for maintenance, thank you for your patience",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

        }
    }
}