package com.auto_lab.auto_hub.timetable_feature

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.R
import com.auto_lab.auto_hub.data_processing.TimeTableData
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.data_processing.api.timetableData.userDataModel
import com.auto_lab.auto_hub.login_and_reg.CircularLoadingDialog
import com.auto_lab.auto_hub.navigationController.navBar
import com.auto_lab.auto_hub.ui.theme.*
import com.auto_lab.auto_hub.ui.theme.HeaderTextColor
import java.time.LocalDate
import java.util.Base64
import kotlin.toString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleInputScreen(
    navController: NavController,
    moduleNames: List<String>, // List of strings to display as text elements
    dropdownOptionsList: List<List<String>>, // List of dropdown options for each module
    userData: UserData,
    context: Context,
    campus:String,
    timeTableData: TimeTableData
) {

    val dropdownSelections = remember {
        mutableStateListOf<String>().apply {
            repeat(moduleNames.size) { add("") }
        }
    }

    var expandedIndex by remember { mutableIntStateOf(-1) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val isLoading = timeTableData.ApiResult.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        showDialog = true
    }

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
                            text = "Time table Creation",
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
        bottomBar = {
            navBar(navController)
        }
    ){
            innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding).fillMaxWidth()
        ) {
            BackGroundTheme()

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Module Details",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeaderTextColor
                )

                Text(
                    text = "Please select the timetable blocks (TB) for each module",
                    color = informationTextColor
                )

                moduleNames.forEachIndexed { index, moduleName ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 16.dp, end = 0.dp, bottom = 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Display module name as text
                        Text(
                            text = moduleName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        // Dropdown for timetable blocks
                        Box(
                            modifier = Modifier
                                .width(65.dp)
                                .height(56.dp)
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dropdownSelections[index],
                                modifier = Modifier
                                    .clickable { expandedIndex = index }
                                    .padding(horizontal = 8.dp, vertical = 16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )

                            DropdownMenu(
                                expanded = expandedIndex == index,
                                onDismissRequest = { expandedIndex = -1 }
                            ) {
                                dropdownOptionsList[index].forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            dropdownSelections[index] = option
                                            expandedIndex = -1
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                feedbackMessage?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = it,
                        color = if (it.startsWith("Success")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { navController.navigate("CampusDetails_Screen") }) {
                        Text("Previous")
                    }

                    Button(
                        onClick = {

                            // Collect and display selected dropdown values
                            val selectedValues = dropdownSelections.joinToString(",").uppercase()


                            if(selectedValues.isEmpty() || selectedValues.split(",").size != moduleNames.size){
                                feedbackMessage = "Please select all required timetable blocks "
                            }
                            else{
                                //collect user information timetable blocks
                                val splitTb = selectedValues.split(",")
                                var combinedUserTb_Modules = ""
                                for(i in 0..<moduleNames.size){
                                    var cleanTb = if(splitTb[i].trim().contains("-")){
                                        splitTb[i].split("-")[0].replace("(","").replace(")","")
                                    }
                                    else{
                                        splitTb[i].replace("(","").replace(")","")
                                    }

                                    if(cleanTb == "Na")
                                        cleanTb = "null"
                                    combinedUserTb_Modules += "${moduleNames[i]}-$cleanTb,"
                                }

                                //call the first api get user urls
                                val getUserInformation = userData.readData(R.string.user_information.toString(),context)
                                val email = getUserInformation.toString().split(":")[1].trim()
                                val password = getUserInformation.toString().split(":")[2].trim()
                                Log.i("userDetails",password)
                                var modules = combinedUserTb_Modules.dropLast(1)


                                val encodedUserModules = Base64.getEncoder().encodeToString(modules.toByteArray())
                                timeTableData.getURLS(email,password,encodedUserModules,campus){
                                        UserUrls, message ->
                                    Log.i("userModules",UserUrls.split("&")[0])
                                    if(message == "successful"){
                                        var userUrls = UserUrls

                                        //--------------------------------------------------------get timetable data section-------------------------------------
                                        if(userUrls.isEmpty()){
                                            feedbackMessage = "something went wrong please try again"
                                        }
                                        else{
                                            userUrls = userUrls.replace("& ",",")
                                            val encodedUserModules = Base64.getEncoder().encodeToString(userUrls.toByteArray())
                                            timeTableData.getTimetableData(email,password,encodedUserModules){
                                                    userDataResponseCheck, message->

                                                val today = LocalDate.now()
                                                val todayname = today.dayOfWeek.toString().uppercase()
                                                var moduleSummary = ""

                                                val userDataResponse: userDataModel? = userDataResponseCheck
                                                if(message == "successful" && userDataResponse != null){
                                                    //save user data to local storage
                                                    val monday = userDataResponse.Monday
                                                    var mondayString=""
                                                    if(monday.Practical.isNotEmpty()|| todayname == "monday"){
                                                        for(i in 0..<monday.Practical.size){
                                                            if(!mondayString.contains("${Module(monday.Practical[i].moduleCode,"Practical",monday.Practical[i].Time,monday.Practical[i].Venue).toString()}&&"))
                                                                mondayString += "${Module(monday.Practical[i].moduleCode,"Practical",monday.Practical[i].Time,monday.Practical[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(monday.Lecture.isNotEmpty()){
                                                        for(i in 0..<monday.Lecture.size){
                                                            if(!mondayString.contains("${Module(monday.Lecture[i].moduleCode,"Lecture",monday.Lecture[i].Time,monday.Lecture[i].Venue).toString()}&&"))
                                                                mondayString += "${Module(monday.Lecture[i].moduleCode,"Lecture",monday.Lecture[i].Time,monday.Lecture[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(monday.Tutorial.isNotEmpty()){
                                                        for(i in 0..<monday.Tutorial.size){
                                                            if(!mondayString.contains("${Module(monday.Tutorial[i].moduleCode,"Tutorial",monday.Tutorial[i].Time,monday.Tutorial[i].Venue).toString()}&&"))
                                                                mondayString += "${Module(monday.Tutorial[i].moduleCode,"Tutorial",monday.Tutorial[i].Time,monday.Tutorial[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    //save mondays data to localstorage
                                                    userData.writeData(R.string.timetable_monday.toString(),context,mondayString)
                                                    if(todayname == "monday".uppercase())
                                                        moduleSummary = mondayString



                                                    //do the same thing for tuesday
                                                    val tuesday = userDataResponse.Tuesday
                                                    var tuesdayString=""
                                                    if(tuesday.Practical.isNotEmpty()){
                                                        for(i in 0..<tuesday.Practical.size){
                                                            if(!tuesdayString.contains(Module(tuesday.Practical[i].moduleCode,"Practical",tuesday.Practical[i].Time,tuesday.Practical[i].Venue).toString()))
                                                                tuesdayString += "${Module(tuesday.Practical[i].moduleCode,"Practical",tuesday.Practical[i].Time,tuesday.Practical[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(tuesday.Lecture.isNotEmpty()){
                                                        for(i in 0..<tuesday.Lecture.size){
                                                            if(!tuesdayString.contains(Module(tuesday.Lecture[i].moduleCode,"Lecture",tuesday.Lecture[i].Time,tuesday.Lecture[i].Venue).toString()))
                                                                tuesdayString += "${Module(tuesday.Lecture[i].moduleCode,"Lecture",tuesday.Lecture[i].Time,tuesday.Lecture[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(tuesday.Tutorial.isNotEmpty()){
                                                        for(i in 0..<tuesday.Tutorial.size){
                                                            if(!tuesdayString.contains(Module(tuesday.Tutorial[i].moduleCode,"Tutorial",tuesday.Tutorial[i].Time,tuesday.Tutorial[i].Venue).toString()))
                                                                tuesdayString += "${Module(tuesday.Tutorial[i].moduleCode,"Tutorial",tuesday.Tutorial[i].Time,tuesday.Tutorial[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    //save wednesday data to localstorage
                                                    userData.writeData(R.string.timetable_tuesday.toString(),context,tuesdayString)
                                                    if(todayname == "tuesday".uppercase())
                                                        moduleSummary = tuesdayString

                                                    //do the same thing for wednesday
                                                    val wednesday = userDataResponse.Wednesday
                                                    var wednesdayString=""
                                                    if(wednesday.Practical.isNotEmpty()){
                                                        for(i in 0..<wednesday.Practical.size){
                                                            if(!wednesdayString.contains(Module(wednesday.Practical[i].moduleCode,"Practical",wednesday.Practical[i].Time,wednesday.Practical[i].Venue).toString()))
                                                                wednesdayString += "${Module(wednesday.Practical[i].moduleCode,"Practical",wednesday.Practical[i].Time,wednesday.Practical[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(wednesday.Lecture.isNotEmpty()){
                                                        for(i in 0..<wednesday.Lecture.size){
                                                            if(!wednesdayString.contains(Module(wednesday.Lecture[i].moduleCode,"Lecture",wednesday.Lecture[i].Time,wednesday.Lecture[i].Venue).toString()))
                                                                wednesdayString += "${Module(wednesday.Lecture[i].moduleCode,"Lecture",wednesday.Lecture[i].Time,wednesday.Lecture[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(wednesday.Tutorial.isNotEmpty()){
                                                        for(i in 0..<wednesday.Tutorial.size){
                                                            if(!wednesdayString.contains(Module(wednesday.Tutorial[i].moduleCode,"Tutorial",wednesday.Tutorial[i].Time,wednesday.Tutorial[i].Venue).toString()))
                                                                wednesdayString += "${Module(wednesday.Tutorial[i].moduleCode,"Tutorial",wednesday.Tutorial[i].Time,wednesday.Tutorial[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    //save wednesday data to localstorage
                                                    userData.writeData(R.string.timetable_wednesday.toString(),context,wednesdayString)
                                                    if(todayname == "wednesday".uppercase())
                                                        moduleSummary = wednesdayString

                                                    //do the same thing for thursday
                                                    val thursday = userDataResponse.Thursday
                                                    var thursdayString=""
                                                    if(thursday.Practical.isNotEmpty()){
                                                        for(i in 0..<thursday.Practical.size){
                                                            if(!thursdayString.contains(Module(thursday.Practical[i].moduleCode,"Practical",thursday.Practical[i].Time,thursday.Practical[i].Venue).toString()))
                                                                thursdayString += "${Module(thursday.Practical[i].moduleCode,"Practical",thursday.Practical[i].Time,thursday.Practical[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(thursday.Lecture.isNotEmpty()){
                                                        for(i in 0..<thursday.Lecture.size){
                                                            if(!thursdayString.contains(Module(thursday.Lecture[i].moduleCode,"Lecture",thursday.Lecture[i].Time,thursday.Lecture[i].Venue).toString()))
                                                                thursdayString += "${Module(thursday.Lecture[i].moduleCode,"Lecture",thursday.Lecture[i].Time,thursday.Lecture[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(thursday.Tutorial.isNotEmpty()){
                                                        for(i in 0..<thursday.Tutorial.size){
                                                            if(!thursdayString.contains(Module(thursday.Tutorial[i].moduleCode,"Tutorial",thursday.Tutorial[i].Time,thursday.Tutorial[i].Venue).toString()))
                                                                thursdayString += "${Module(thursday.Tutorial[i].moduleCode,"Tutorial",thursday.Tutorial[i].Time,thursday.Tutorial[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    //save thursday data to localstorage
                                                    userData.writeData(R.string.timetable_thursday.toString(),context,thursdayString)
                                                    if(todayname == "thursday".uppercase())
                                                        moduleSummary = thursdayString

                                                    //do it for friday
                                                    //do the same thing for friday
                                                    val friday = userDataResponse.Friday
                                                    Log.i("friday error", friday.toString())
                                                    Log.i("see friday txt file",userData.readData(R.string.timetable_friday.toString(),context))

                                                    var fridayString=""
                                                    if(friday.Practical.isNotEmpty()){
                                                        for(i in 0..<friday.Practical.size){
                                                            if(!fridayString.contains(Module(friday.Practical[i].moduleCode,"Practical",friday.Practical[i].Time,friday.Practical[i].Venue).toString()))
                                                                fridayString += "${Module(friday.Practical[i].moduleCode,"Practical",friday.Practical[i].Time,friday.Practical[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(friday.Lecture.isNotEmpty()){
                                                        for(i in 0..<friday.Lecture.size){
                                                            if(!fridayString.contains(Module(friday.Lecture[i].moduleCode,"Lecture",friday.Lecture[i].Time,friday.Lecture[i].Venue).toString()))
                                                                fridayString += "${Module(friday.Lecture[i].moduleCode,"Lecture",friday.Lecture[i].Time,friday.Lecture[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    if(friday.Tutorial.isNotEmpty()){
                                                        for(i in 0..<friday.Tutorial.size){
                                                            if(!fridayString.contains(Module(friday.Tutorial[i].moduleCode,"Tutorial",friday.Tutorial[i].Time,friday.Tutorial[i].Venue).toString()))
                                                                fridayString += "${Module(friday.Tutorial[i].moduleCode,"Tutorial",friday.Tutorial[i].Time,friday.Tutorial[i].Venue).toString()}&&"
                                                        }
                                                    }

                                                    //save friday data to local storage
                                                    userData.writeData(R.string.timetable_friday.toString(),context,fridayString)
                                                    if(todayname == "friday".uppercase())
                                                        moduleSummary = fridayString

                                                    val timetableDataSum = "(m)${mondayString}(tu)${tuesdayString}(w)${wednesdayString}(th)${thursdayString}(f)${fridayString}"
                                                    timeTableData.saveTimeTableData(email,password,timetableDataSum){
                                                        state, message ->
                                                        if(state){
                                                            moduleSummary = moduleSummary.dropLast(2)
                                                            //moduleSummary = Module("comp201","Lecture","10:35-12:20","MSB-G24").toString()+"&&"+Module("comp204","Tutorial","12:20-13:30","MSB-G24").toString()+"&&"+Module("math243","Lecture","13:30-14:20","MSB-G24").toString()
                                                            navController.navigate("CreateTimetable_Screen/$moduleSummary/$todayname")
                                                        }else{
                                                            moduleSummary = moduleSummary.dropLast(2)
                                                            //moduleSummary = Module("comp201","Lecture","10:35-12:20","MSB-G24").toString()+"&&"+Module("comp204","Tutorial","12:20-13:30","MSB-G24").toString()+"&&"+Module("math243","Lecture","13:30-14:20","MSB-G24").toString()
                                                            navController.navigate("CreateTimetable_Screen/$moduleSummary/$todayname")
                                                        }
                                                    }
                                                }
                                                else{
                                                    Log.i("userModules",userDataResponse.toString())
                                                    feedbackMessage= "unable to get modules"
                                                }
                                            }
                                        }
                                    }else{
                                        feedbackMessage = "something went wrong please try again"
                                    }
                                }
                            }

                        }
                    ) {
                        Text("Next")
                    }
                }
            }

            if (isLoading.value) {
                CircularLoadingDialog(isLoading.value)
            }

            //show alert user that they must select corrrect timetable blocks and that nA means theres no timetable block for that module
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
                                text = "Please make sure timetable blocks are not clashing by choosing different timetable blocks for each module"
                            )
                            Text(
                                text = "Note that if 'Na' is the only option appearing it means that module has no timetable block please select 'Na' to proceed"
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                //close Dialog frame
                                showDialog = false
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("I understand")
                        }
                    }
                )
            }

        }
    }

}