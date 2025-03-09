package com.auto_lab.auto_hub.home_page

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.LocationTask.LocationHelper
import com.auto_lab.auto_hub.R
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.data_processing.weatherDataModel
import com.auto_lab.auto_hub.navigationController.Screens
import com.auto_lab.auto_hub.navigationController.navBar
import com.auto_lab.auto_hub.timetable_feature.Module
import com.auto_lab.auto_hub.ui.theme.BackGroundTheme
import com.auto_lab.auto_hub.ui.theme.CardBackGroundColor
import com.auto_lab.auto_hub.ui.theme.HeaderTextColor
import java.time.LocalDate


//before displaying this screen read user data from local storage
@OptIn(ExperimentalMaterial3Api::class)
@Composable //= arrayListOf<Module>(Module("comp201","Lecture","10:35-12:20","MSB-G24"),Module("comp204","Tutorial","12:20-13:30","MSB-G24"),Module("math243","Lecture","13:30-14:20","MSB-G24"),Module("Stat140","Practical","14:35 -17:20","MSB-G24"),Module("comp201","Lecture","10:35-12:20","MSB-G24"))
fun Home_pageScreen(navController: NavController,weatherDataModel: weatherDataModel, userName:String,moduleSummary: MutableList<Module>, context: Context, userData: UserData){
    val dayOfWeek = ""+LocalDate.now().dayOfWeek
    //get instance of focus to clear keyboard before loging in or changing screens
    val focusManager = LocalFocusManager.current

    //create local variables for weather
    var weather by remember { mutableStateOf("") }
    var temperatureRecord by remember { mutableDoubleStateOf(0.0) }

    //weather data model
    val isLoading = weatherDataModel.ApiResult.collectAsState()

    //for checking if internet connection is there
    var showDialog by remember { mutableStateOf(false) }


    //read todays timetable and weather data
    LaunchedEffect(Unit) {
        //clear focus to close keyboard
        focusManager.clearFocus()

        //show loading bar
        weatherDataModel.setApiResult(true)

        //get city name and check user location if enabled
        var city =""
        if(LocationHelper.isLocationEnabled(context))
            city = LocationHelper.getLocation(context)
        else{
            showDialog = true
        }

        //call api for weather
        if(weather == "" || temperatureRecord == 0.0){
            if(city == "Unknown City" || city  == ""){
                weatherDataModel.setApiResult(false)
                weather = ""
                temperatureRecord = 0.0
            }else {
                weatherDataModel.getWeatherData(city){
                        weatherData, isSuccessful ->
                    if(isSuccessful){

                        temperatureRecord = try{
                            weatherData?.current?.temp_c.toString().toDouble()
                        }catch (e: Exception){
                            0.0
                        }
                        Log.i("weatherError",temperatureRecord.toString())
                        if(temperatureRecord > 0.0 && temperatureRecord < 20.0){
                            weather = "cold"
                        }else if (temperatureRecord > 20.0 && temperatureRecord < 25.0){
                            weather = "cloudy sunny"
                        }else if (temperatureRecord >25.0 && temperatureRecord < 30.0){
                            weather = "sunny"
                        }else if(temperatureRecord > 30.0){
                            weather = "hot"
                        }
                    }
                    else{
                        weather = ""
                        temperatureRecord = 0.0
                    }
                }
            }
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        Row {
                            IconButton(
                                onClick = {
                                    //change remmeber me to false
                                    userData.writeData(R.string.remember_me.toString(),context,"false")

                                    //send user to login page
                                    navController.navigate(Screens.Login.route){
                                        popUpTo(navController.graph.id){
                                            inclusive = true
                                        }
                                    }
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.logout),
                                    contentDescription = "logout image",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .alpha(0.6f),
                                    tint = Color.White,

                                    )
                            }
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Logout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable(
                                    onClick = {
                                        //change remmeber me to false
                                        userData.writeData(R.string.remember_me.toString(),context,"false")

                                        navController.navigate(Screens.Login.route){
                                            popUpTo(navController.graph.id){
                                                inclusive = true
                                            }
                                        }
                                    }
                                )
                                    .padding(top = 8.dp)
                            )
                        }

                        Text(
                            text = "AUTOHUB",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
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
    )
    {
        innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            //add a background theme from theme file
            BackGroundTheme()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()

            ){

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    userScrollEnabled = true
                ) {
                    item(
                        content = {
                            Text(
                                text = "Hey, $userName",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = HeaderTextColor,
                                modifier = Modifier.padding(start=8.dp,top = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your today's summary and weather are below :)",
                                modifier = Modifier.padding(start=8.dp).alpha(0.4f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(CardBackGroundColor)
                                    .align(Alignment.CenterHorizontally),
                            ){
                                Column (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Text(
                                        text = dayOfWeek.uppercase(),
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .height(100.dp)
                                            .width(250.dp)
                                            .align(Alignment.CenterHorizontally)
                                            .border(width = 2.dp, color = Color.White   , shape = RoundedCornerShape(16.dp))
                                            .background(Color.White)
                                            .alpha(0.6f),

                                        ){
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if(isLoading.value){
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(50.dp),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }else {
                                                if (temperatureRecord > 0.0 || weather.isNotEmpty()) {
                                                    if (weather.contentEquals("cold") || weather.contentEquals(
                                                            "raining"
                                                        )
                                                    ) {
                                                        Text(
                                                            text = "$temperatureRecord°C",
                                                            fontSize = 32.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = CardBackGroundColor,
                                                        )

                                                        Image(
                                                            painter = painterResource(id = R.drawable.rainy_day),
                                                            contentDescription = "cloud image",
                                                            modifier = Modifier.size(50.dp)
                                                        )
                                                    } else if (weather.contentEquals("cloudy sunny")) {
                                                        Text(
                                                            text = "$temperatureRecord°C",
                                                            fontSize = 32.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = CardBackGroundColor,
                                                        )

                                                        Image(
                                                            painter = painterResource(id = R.drawable.cloudy_image),
                                                            contentDescription = "cloudy sunny image",
                                                            modifier = Modifier.size(35.dp)
                                                        )
                                                    } else if (weather.contentEquals("sunny")) {
                                                        Text(
                                                            text = "$temperatureRecord°C",
                                                            fontSize = 32.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = CardBackGroundColor,
                                                        )

                                                        Image(
                                                            painter = painterResource(id = R.drawable.cloudy_sunny),
                                                            contentDescription = "sun image",
                                                            modifier = Modifier.size(35.dp)
                                                        )
                                                    } else {
                                                        Text(
                                                            text = "$temperatureRecord°C",
                                                            fontSize = 32.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = CardBackGroundColor,
                                                        )

                                                        Image(
                                                            painter = painterResource(id = R.drawable.sun),
                                                            contentDescription = "sun image",
                                                            modifier = Modifier.size(35.dp)
                                                        )
                                                    }
                                                }
                                                else{
                                                    Text("error loading weather",color = Color.Black, modifier = Modifier.alpha(0.6f))
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Today's Summary",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        textDecoration = TextDecoration.Underline,
                                        color = Color.White
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))


                                    if(moduleSummary.isNotEmpty()){
                                        val sessionSize = moduleSummary.size
                                        for (i in 0..< sessionSize){
                                            if(moduleSummary[i].moduleCode == "weekend"){
                                                Text(text = "Its a weekend! Hope you enjoy it", color =  Color.White)
                                                break
                                            }
                                            if(moduleSummary[i].moduleCode != "nA"){
                                                DisplayingModuleText(moduleSummary[i].moduleCode,moduleSummary[i].sessionTime)
                                            }
                                        }
                                    }else{
                                        TextButton(
                                            onClick = {
                                                //navigate to creation screen
                                                navController.navigate("CampusDetails_Screen")
                                            }
                                        ) {
                                            Text(
                                                text = "No TimeTable! Create now"
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),

                                ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Box (
                                        modifier = Modifier
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .weight(1f)
                                            .background(CardBackGroundColor)

                                    ){
                                        IconButton(
                                            onClick = {
                                                //redirect user to their timetable screen
                                                //also check before if they have done their timetable if not suggest them they do a timetable
                                                //read todays data
                                                val currentDate = LocalDate.now()
                                                val dayOfWeek = currentDate.dayOfWeek.toString().uppercase()
                                                var moduleSummary = ""
                                                if(dayOfWeek =="MONDAY"){
                                                    moduleSummary = userData.readData(R.string.timetable_monday.toString(),context).dropLast(1)
                                                }else if(dayOfWeek =="TUESDAY"){
                                                    moduleSummary = userData.readData(R.string.timetable_tuesday.toString(),context).dropLast(1)
                                                }else if(dayOfWeek== "WEDNESDAY"){
                                                    moduleSummary = userData.readData(R.string.timetable_wednesday.toString(),context).dropLast(1)
                                                }else if(dayOfWeek == "THURSDAY"){
                                                    moduleSummary = userData.readData(R.string.timetable_thursday.toString(), context).dropLast(1)
                                                }else if (dayOfWeek == "FRIDAY"){
                                                    moduleSummary = userData.readData(R.string.timetable_friday.toString(),context).dropLast(1)
                                                }

                                                // val moduleSummary = Module("comp201","Lecture","10:35-12:20","MSB-G24").toString()+"&&"+Module("comp204","Tutorial","12:20-13:30","MSB-G24").toString()+"&&"+Module("math243","Lecture","13:30-14:20","MSB-G24").toString()
                                                navController.navigate("CreateTimetable_Screen/$moduleSummary/$dayOfWeek")
                                            },
                                            modifier = Modifier.fillMaxSize()

                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.timetable),
                                                    contentDescription = "lecture timetable image",
                                                    tint = Color.White,
                                                    modifier = Modifier.weight(1f).padding(top = 5.dp)
                                                )
                                                Text(
                                                    text = "Lecture timetable",
                                                    color = Color.White,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .weight(0.5f)
                                                        .fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Box (
                                        modifier = Modifier
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .weight(1f)
                                            .background(CardBackGroundColor)

                                    ){
                                        IconButton(
                                            onClick = {
                                                //for now since the attendance tracker backend logic has not been implemented show upcoming screen
                                                val section = "Attendance Tracker"
                                                navController.navigate("UpcomingFeatures_Screen/$section")
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.register),
                                                    contentDescription = "Attendance Tracker",
                                                    tint = Color.White,
                                                    modifier = Modifier.weight(1f).padding(top = 5.dp)
                                                )
                                                Text(
                                                    text = "Attendance Tracker",
                                                    color = Color.White,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .weight(0.5f)
                                                        .fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Box (
                                        modifier = Modifier
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .weight(1f)
                                            .background(CardBackGroundColor)

                                    ){
                                        IconButton(
                                            onClick = {
                                                //for now since the Exam timetable backend logic has not been implemented show upcoming screen
                                                val section = "Exam timetable"
                                                navController.navigate(Screens.examScreen.route)
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.exam),
                                                    contentDescription = "exam timetable image",
                                                    tint = Color.White,
                                                    modifier = Modifier.weight(1f).padding(top = 5.dp)
                                                )
                                                Text(
                                                    text = "Exam timetable",
                                                    color = Color.White,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .weight(0.5f)
                                                        .fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Box (
                                        modifier = Modifier
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .weight(1f)
                                            .background(CardBackGroundColor)

                                    ){
                                        IconButton(
                                            onClick = {
                                                //for now since the Study timetable backend logic has not been implemented show upcoming screen
                                                val section = "Study timetable"
                                                navController.navigate("UpcomingFeatures_Screen/$section")
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.studying),
                                                    contentDescription = "study timetable image",
                                                    tint = Color.White,
                                                    modifier = Modifier.weight(1f).padding(top = 5.dp)
                                                )
                                                Text(
                                                    text = "Study timetable",
                                                    color = Color.White,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .weight(0.5f)
                                                        .fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .weight(1f)
                                            .background(CardBackGroundColor)

                                    ) {
                                        IconButton(
                                            onClick = {
                                                //create a assignment tracker screen for adding and removing modules
                                                val section = "assignment"
                                                navController.navigate("UpcomingFeatures_Screen/$section")
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.checklist),
                                                    contentDescription = "Assignment image",
                                                    tint = Color.White,
                                                    modifier = Modifier.weight(1f).padding(top = 5.dp)
                                                )
                                                Text(
                                                    text = "Assignment tracker",
                                                    color = Color.White,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .weight(0.5f)
                                                        .fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Box(
                                        modifier = Modifier
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .weight(1f)
                                            .background(CardBackGroundColor)

                                    ) {
                                        IconButton(
                                            onClick = {
                                                //for now since the Chat room backend logic has not been implemented show upcoming screen
                                                val section = "Chat room"
                                                navController.navigate("UpcomingFeatures_Screen/$section")
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.account),
                                                    contentDescription = "chat room image",
                                                    tint = Color.White,
                                                    modifier = Modifier.weight(1f).padding(top = 5.dp)
                                                )
                                                Text(
                                                    text = "Chat room",
                                                    color = Color.White,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier
                                                        .weight(0.5f)
                                                        .fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Dialog popup for sending user to enable location
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
                                text = "Please turn on ur location for better user experience"
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
                                LocationHelper.openLocationSettings(context)
                                showDialog = false
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Open Settings")
                        }
                    }
                )
            }
        }
    }
}

//for displaying module text
@Composable
fun DisplayingModuleText(moduleCode:String, sessionTime:String){
    val moduleTime = if(sessionTime.split("-").size > 2){
        "${sessionTime.split("-")[0]}-${sessionTime.split("-")[sessionTime.split("-").size-1]}"
    }else{
        sessionTime
    }
    Text(
        text = "$moduleTime - $moduleCode".uppercase(),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
        textAlign = TextAlign.Center,
        color = Color.White
    )
}
