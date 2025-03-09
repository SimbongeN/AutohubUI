package com.auto_lab.auto_hub.home_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.R
import com.auto_lab.auto_hub.navigationController.navBar
import com.auto_lab.auto_hub.ui.theme.*
import com.auto_lab.auto_hub.ui.theme.BackGroundTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingFeatures (navController: NavController,typeOfFeature: String){
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
                    containerColor = CardBackGroundColor, // Background color
                    titleContentColor = Color.White, // Title text color
                )
            )
        },
        bottomBar = {
            navBar(navController)
        }
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
                        .clip(RoundedCornerShape(16.dp))
                        .padding(16.dp)


                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.work_in_progress),
                        contentDescription = "construction picture",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                Text(
                    text = "This Feature $typeOfFeature is currently being developed and will be available soon",
                    modifier = Modifier
                        .padding(16.dp)
                        .alpha(0.7f)
                )
            }

        }
    }
}