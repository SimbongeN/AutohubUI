package com.auto_lab.auto_hub.timetable_feature


import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.data_processing.TimeTableData
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.login_and_reg.CircularLoadingDialog
import com.auto_lab.auto_hub.navigationController.Screens
import com.auto_lab.auto_hub.navigationController.navBar
import com.auto_lab.auto_hub.ui.theme.*
import com.auto_lab.auto_hub.ui.theme.HeaderTextColor
import com.auto_lab.auto_hub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable //arrayListOf<Module>(Module("comp201","Lecture","10:35-12:20","MSB-G24"),Module("comp204","Tutorial","12:20-13:30","MSB-G24"),Module("math243","Lecture","13:30-14:20","MSB-G24"),Module("Stat140","Practical","14:35-17:20","MSB-G24"),Module("comp201","Lecture","10:35-12:20","MSB-G24"))
fun Timetable_creationScreen(navController: NavController,listOfModules: ArrayList<Module>,dayOfWeek: String, userData: UserData,context: Context){//ListOfModules: ArrayList<Module> = null, Day: String
    val dayOfTheWeek = dayOfWeek.uppercase()
    val scrollState = rememberScrollState()
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
                            text = "TIME TABLE",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
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
            modifier = Modifier.padding(innerPadding)
        ) {
            //add a background theme from theme file
            BackGroundTheme()

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly

                ){
                    TextButton(
                        onClick = {
                            //read mondays sessions
                            val moduleSummary = userData.readData(R.string.timetable_monday.toString(),context)
                            navController.navigate("CreateTimetable_Screen/$moduleSummary/Monday")
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(28.dp))
                            .background(CardBackGroundColor)
                    ) {
                        if(dayOfTheWeek.uppercase() =="MONDAY")
                            Text(text="Mon", color = Color.Green)
                        else
                            Text(text="Mon", color = Color.White)
                    }

                    TextButton(
                        onClick = {
                            val moduleSummary = userData.readData(R.string.timetable_tuesday.toString(),context)
                            navController.navigate("CreateTimetable_Screen/$moduleSummary/Tuesday")
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(28.dp))
                            .background(CardBackGroundColor)
                    ) {
                        if(dayOfTheWeek.uppercase() == "TUESDAY")
                            Text(text="Tues", color = Color.Green)
                        else
                            Text(text="Tues", color = Color.White)
                    }

                    TextButton(
                        onClick = {

                            val moduleSummary = userData.readData(R.string.timetable_wednesday.toString(),context)
                            navController.navigate("CreateTimetable_Screen/$moduleSummary/wednesday")
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(28.dp))
                            .background(CardBackGroundColor)
                    ) {
                        if(dayOfTheWeek.uppercase() =="WEDNESDAY")
                            Text(text="Wed", color = Color.Green)
                        else
                            Text(text="Wed", color = Color.White)

                    }
                    TextButton(
                        onClick = {
                            val moduleSummary = userData.readData(R.string.timetable_thursday.toString(),context)
                            navController.navigate("CreateTimetable_Screen/$moduleSummary/thursday")
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(28.dp))
                            .background(CardBackGroundColor)
                    ) {
                        if(dayOfTheWeek.uppercase() == "THURSDAY")
                            Text(text="Thurs", color = Color.Green)
                        else
                            Text(text="Thurs", color = Color.White)

                    }
                    TextButton(
                        onClick = {
                            val moduleSummary = userData.readData(R.string.timetable_friday.toString(),context)
                            navController.navigate("CreateTimetable_Screen/$moduleSummary/Friday")
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(28.dp))
                            .background(CardBackGroundColor)
                    ) {
                        if(dayOfTheWeek.uppercase() =="FRIDAY")
                            Text(text="Fri", color = Color.Green)
                        else
                            Text(text="Fri", color = Color.White)

                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                if (listOfModules.isNotEmpty()){
                    Column (
                        //userScrollEnabled = true
                        modifier = Modifier.verticalScroll(scrollState).imePadding(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        listOfModules.forEachIndexed{ index:Int, item:Module ->
                            if(item.moduleCode != "nA"){
                                DisplayModuleDetails(navController,moduleInfo = item,dayOfTheWeek,listOfModules,context)
                            }
                        }
                    }
                }

                else if(dayOfTheWeek.uppercase() == "SATURDAY" || dayOfTheWeek == "SUNDAY"){
                    Text(
                        text = "Weekend"
                    )
                }
                else{
                    val timetableExist = if(userData.readData(R.string.timetable_monday.toString(),context).length > 10){
                        true
                    }else if(userData.readData(R.string.timetable_tuesday.toString(),context).length > 10){
                        true
                    }else if(userData.readData(R.string.timetable_wednesday.toString(),context).length > 10){
                        true
                    }else if (userData.readData(R.string.timetable_thursday.toString(),context).length > 10){
                        true
                    }else if(userData.readData(R.string.timetable_friday.toString(),context).length > 10){
                        true
                    }else{
                        false
                    }

                    if(timetableExist){
                        Text(
                            text = "No lectures on $dayOfTheWeek"
                        )
                    }else{
                        TextButton(
                            onClick = {
                                //navigate to create timetable screen
                                navController.navigate("CampusDetails_Screen")
                            }
                        ) {
                            Text(
                                text = "No TimeTable! Create now"
                            )
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun DisplayModuleDetails(navController: NavController,moduleInfo: Module,day: String,listOfModules: ArrayList<Module>,context: Context){
    val moduleTime = if(moduleInfo.sessionTime.split("-").size > 2){
       "${moduleInfo.sessionTime.split("-")[0]}-${moduleInfo.sessionTime.split("-")[moduleInfo.sessionTime.split("-").size-1]}"
    }else{
        moduleInfo.sessionTime
    }

    var editMode by remember { mutableStateOf(false) }
    var sessionTime by remember { mutableStateOf(moduleTime) }
    var venue by remember { mutableStateOf(moduleInfo.sessionVenue) }
    var moduleCode by remember { mutableStateOf(moduleInfo.moduleCode) }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackGroundColor)
            .padding(16.dp)
            .clickable(enabled = true,
                onClick = {
                    editMode = true
                }),
        verticalAlignment = Alignment.CenterVertically,

    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = moduleInfo.sessionType.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = cardTextColor,
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Time:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = cardTextColor,
                )

                if(editMode){
                    OutlinedTextField(
                        value = sessionTime,
                        onValueChange = {sessionTime = it },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.weight(1f) // Main text field takes up remaining width
                    )
                }else{
                    Text(
                        text = sessionTime,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = cardTextColor,
                    )
                }
            }

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp,bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Module:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor
                )

                if(editMode){
                    OutlinedTextField(
                        value = moduleCode.uppercase(),
                        onValueChange = {moduleCode = it.uppercase()},
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.weight(1f) // Main text field takes up remaining width
                    )
                }else{
                    Text(
                        text = moduleCode.uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = cardTextColor
                    )
                }

            }

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp,bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Venue:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor
                )
                if(editMode){
                    OutlinedTextField(
                        value = venue.uppercase(),
                        onValueChange = {venue = it.uppercase()},
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.weight(1f) // Main text field takes up remaining width
                    )
                }else{
                    Text(
                        text = venue.uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = cardTextColor
                    )
                }
            }

            if(editMode){
                Button(onClick = {
                    listOfModules.remove(moduleInfo)
                    moduleInfo.sessionTime = sessionTime
                    moduleInfo.moduleCode = moduleCode
                    moduleInfo.sessionVenue = venue
                    listOfModules.add(moduleInfo)

                    //save new user edited details
                    val userData = UserData()
                    var Newcontent = ""
                    for(i in listOfModules){
                        Newcontent += "${i.toString()}&&"
                    }
                    var dayFile = ""

                    if(day.uppercase() == "MONDAY"){
                        dayFile = R.string.timetable_monday.toString()
                    }
                    if(day.uppercase() == "TUESDAY"){
                        dayFile = R.string.timetable_tuesday.toString()
                    }
                    if(day.uppercase() == "WEDNESDAY"){
                        dayFile = R.string.timetable_wednesday.toString()
                    }
                    if(day.uppercase() == "THURSDAY"){
                        dayFile = R.string.timetable_thursday.toString()
                    }
                    if(day.uppercase() == "FRIDAY"){
                        dayFile = R.string.timetable_friday.toString()
                    }
                    Newcontent = Newcontent.dropLast(2)
                    userData.writeData(dayFile,context,Newcontent)
                    editMode = false
                    navController.popBackStack()
                    navController.navigate("CreateTimetable_Screen/$Newcontent/$day")
                }) {
                    Text("Save")
                }
            }
        }
        IconButton(
            onClick ={
                val userData = UserData()
                listOfModules.remove(moduleInfo)
                var Newcontent = ""
                for(i in listOfModules){
                    Newcontent += "${i.toString()}&&"
                }
                var dayFile = ""
                if(day.uppercase() == "MONDAY"){
                    dayFile = R.string.timetable_monday.toString()
                }
                if(day.uppercase() == "TUESDAY"){
                    dayFile = R.string.timetable_tuesday.toString()
                }
                if(day.uppercase() == "WEDNESDAY"){
                    dayFile = R.string.timetable_wednesday.toString()
                }
                if(day.uppercase() == "THURSDAY"){
                    dayFile = R.string.timetable_thursday.toString()
                }
                if(day.uppercase() == "FRIDAY"){
                    dayFile = R.string.timetable_friday.toString()
                }
                Newcontent = Newcontent.dropLast(2)
                userData.writeData(dayFile,context,Newcontent)
                navController.popBackStack()
                navController.navigate("CreateTimetable_Screen/$Newcontent/$day")
            }
        ) {
            Icon(
                painter = painterResource(id =R.drawable.delete),
                contentDescription = "delete",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleInputScreen(navController: NavController, initialFieldCount: Int, campus:String,context: Context,userData: UserData, timeTableData: TimeTableData){
    // State to hold the content of each main text field
    val textFieldValues = remember {
        mutableStateListOf<TextFieldValue>().apply {
            repeat(initialFieldCount) { add(TextFieldValue()) }
        }
    }

    val mainFieldErrors = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(initialFieldCount) { add(false) } // No errors initially
        }
    }

    // State for feedback message
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val isLoading = timeTableData.ApiResult.collectAsState()

    //scrollable functionality
    val scrollState = rememberScrollState()

    var showDialog by remember { mutableStateOf(false) }

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
            //add a background theme from theme file
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
                    text= "Module Details",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeaderTextColor
                )

                Text(
                    text= "Please enter module codes",
                    color = informationTextColor
                )

                textFieldValues.forEachIndexed { index, value ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 32.dp, end = 32.dp, bottom = 0  .dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Main TextField
                        // for module codes
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newValue -> textFieldValues[index] = newValue },
                            label = { Text("Module code ${index + 1}") },
                            isError = mainFieldErrors[index],
                            singleLine = true,
                            modifier = Modifier.weight(1f) // Main text field takes up remaining width
                        )

                        //check if fields are not empty for module code
                        if (mainFieldErrors[index]) {
                            Text(
                                text = "Required",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display feedback message
                feedbackMessage?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = it,
                        color = if (it.startsWith("Success")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Button to extract all text field values
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){

                    Button(onClick = {
                        //navigate back to previous screen
                        navController.navigate("CampusDetails_Screen")
                    }) {
                        Text("Previous")
                    }

                    Button(
                        onClick = {
                            for (i in textFieldValues.indices) {
                                mainFieldErrors[i] = textFieldValues[i].text.isEmpty()
                            }

                            val hasErrors = mainFieldErrors.any { it }
                            if (hasErrors) {
                                feedbackMessage = "please fill in all required field"
                            }else{
                                var userModule = ""
                                for(i in textFieldValues.indices){
                                    userModule +="${textFieldValues[i].text.uppercase().trim()},"
                                }
                                userModule = userModule.dropLast(1)
                                var listOfTb = mutableListOf<List<String>>()

                                timeTableData.getCampusData(campus){
                                        response , successful ->
                                    if(successful){
                                        //check if module exist in code base
                                        val userModuleList = userModule.split(",")
                                        var allModulesExist = true
                                        for(module in userModuleList)
                                            if(!response.contains(module)) {
                                                allModulesExist = false
                                                break
                                            }

                                        if(allModulesExist){
                                            val listOfAllModules = response.split("\n( PDF version )\n")
                                            for(m in userModule.split(",")){

                                                var listTb = mutableListOf<String>()

                                                //there an error when searching for PHYS110 it doesnt show up in the response ive tried everything
                                                //but still wont show up NOTE it crashes the app everytime its included as one of the modules
                                                //phys110 has one timetable block which is E therefore i have hard coded phys110
                                                //NOTE ALL OTHER MODULE SEEM TO WORK FINE AND THEY ARE ABLE TO APPEAR
                                                if(m.uppercase() == "PHYS110"){
                                                    listTb.add("(E)")
                                                    listOfTb.add(listTb)
                                                    continue
                                                }

                                                //search for user timetable blocks
                                                for(i in 0..< listOfAllModules.size){
                                                    val line  = listOfAllModules[i].split(",")

                                                    if(line.size == 3){
                                                        val tbElement = line[2].trim().split(" ")

                                                        if(tbElement.contains(m)){
                                                            var timetableBlockFlag = false
                                                            for(x in 0..<tbElement.size-1){
                                                                if('(' in tbElement[x] && ')' in tbElement[x]){
                                                                    if('(' in tbElement[x+1] && ')' in tbElement[x+1]){
                                                                        if(!listTb.contains(tbElement[x])) {
                                                                            listTb.add("${tbElement[x]}-${tbElement[x + 1]}")
                                                                            timetableBlockFlag = true
                                                                        }
                                                                        break
                                                                    }else{
                                                                        if(!listTb.contains(tbElement[x])) {
                                                                            listTb.add(tbElement[x])
                                                                            timetableBlockFlag = true
                                                                        }
                                                                        break
                                                                    }
                                                                }
                                                            }
                                                            if(!timetableBlockFlag){
                                                                timetableBlockFlag = false
                                                                listTb.add("Na")
                                                            }
                                                        }
                                                    }
                                                }
                                                listOfTb.add(listTb)
                                            }
                                            var dropdownOptionsList = ""
                                            for(i in listOfTb){

                                                for(m in i){
                                                    dropdownOptionsList += "$m;"
                                                }
                                                dropdownOptionsList = "${dropdownOptionsList.dropLast(1)}!"
                                            }
                                            Log.i("timtable response", dropdownOptionsList)
                                            navController.navigate("downloadModules/${userModule}/${dropdownOptionsList.dropLast(1)}/${campus}")
                                        }
                                        else{
                                            feedbackMessage = "Please provide valid module codes, use format eg Math140"
                                        }
                                    }
                                    else if(response == "error"){
                                        feedbackMessage = "Something went wrong check internet connection"
                                    }else{
                                        feedbackMessage = "Something went wrong Please try again"
                                    }
                                }
                            }
                        },
                    ) {
                        Text("Next")
                    }
                }
            }
            if (isLoading.value) {
                CircularLoadingDialog(isLoading.value)
            }

            //show alert user to check properly all the module code they have entered to avoid crushing the app
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
                                text = "Please make sure you have written your module codes correctly for the system to generate you an accurate timetable"
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTimetable_informationScreen(navController: NavController){
    // State for feedback message
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var campusName by remember { mutableStateOf("") }
    var numberOfModules by remember { mutableStateOf("") }
    var errorCampusName by remember { mutableStateOf(false) }
    var errorNumModule by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

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
            //adding background image
            BackGroundTheme()

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Student Details",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeaderTextColor
                )

                Text(
                    text = "Please enter campus and module details",
                    color = informationTextColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = campusName,
                    onValueChange = {
                        campusName = it
                    },
                    label = {
                        Text(text ="Campus")
                    },
                    singleLine = true,
                    isError = errorCampusName
                )
                //check if fields are not empty for time table blocks
                if (errorCampusName) {
                    Text(
                        text = "Required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = numberOfModules,
                    onValueChange = {
                        numberOfModules =it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = {
                        Text(text ="No. Modules")
                    },
                    singleLine = true,
                    isError = errorNumModule
                )
                //check if fields are not empty for time table blocks
                if (errorNumModule) {
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

                Spacer(modifier = Modifier.height(32.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Button(onClick = {
                        //send user back to home screen
                        navController.navigate(Screens.HomePage.route){
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }

                    }) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {
                            errorCampusName = campusName.isEmpty()
                            errorNumModule = numberOfModules.isEmpty()
                            feedbackMessage = if (errorNumModule||errorCampusName){
                                "please fill in all required fields"
                            }else
                                null

                            if(feedbackMessage == null) {
                                campusName= campusName.trim()
                                if(campusName.uppercase() == "Pietermaritzburg".uppercase() || campusName.uppercase() == "PMB")
                                    campusName = "PMB"
                                else if(campusName.uppercase() == "westville".uppercase())
                                    campusName = campusName.uppercase()
                                else if(campusName.uppercase() == "howard".uppercase())
                                    campusName = campusName.uppercase()
                                else if(campusName.uppercase() == "edgewood".uppercase())
                                    campusName = campusName.uppercase()
                                else
                                    errorCampusName  = true

                                if(numberOfModules.toInt() <= 0)
                                    errorNumModule = true
                                else
                                    errorNumModule = false

                                if(!errorNumModule && !errorCampusName)
                                    navController.navigate("ModuleInformation_Screen/$numberOfModules/${campusName.uppercase()}")
                            }
                        }
                    ) {
                        Text(text = "Continue")
                    }
                }
            }
        }
    }
}
