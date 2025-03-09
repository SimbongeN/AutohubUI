package com.auto_lab.auto_hub.timetable_feature

import android.content.Context
import android.util.Log
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auto_lab.auto_hub.R
import com.auto_lab.auto_hub.data_processing.TimeTableData
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.navigationController.navBar
import com.auto_lab.auto_hub.ui.theme.BackGroundTheme
import com.auto_lab.auto_hub.ui.theme.CardBackGroundColor
import com.auto_lab.auto_hub.ui.theme.cardTextColor
import java.util.Base64
import kotlin.text.isEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun examTimeTable (navController: NavController,timeTableData: TimeTableData,context: Context){
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var moduleCodes by remember { mutableStateOf("") }
    var ErrorModuleCodes by remember { mutableStateOf(false) }
    var campus by remember { mutableStateOf("") }
    var ErrorCampus by remember { mutableStateOf(false) }

    var ListOfExamDetails = remember { mutableListOf<ExamDetails>()}
    var showDialog by remember { mutableStateOf(false) }
    var isNotOut by remember { mutableStateOf(false) }

    val isLoading = timeTableData.ApiResult.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        val readData  = UserData()
        var response = readData.readData(R.string.ExamData.toString(),context)

        //get user data email and password
        val userInformation = readData.readData(R.string.user_information.toString(),context)
        var email = ""
        var password = ""
        if(userInformation.contains(":")){
            if(userInformation.split(":").size > 2){
                email = userInformation.split(":")[1].trim()
                password = userInformation.split(":")[2].trim()
            }
        }


        if(response == "failed" || response==""){
            showDialog = true
        }else if(response.contains(",")){

            isNotOut = true
            val getCampus = response.split("=")[0]
            val getModuleCodes = response.split("=")[1]
            val encodedUserModules = Base64.getEncoder().encodeToString(getModuleCodes.uppercase().trim().toByteArray())
            val encodedCampus = Base64.getEncoder().encodeToString(getCampus.uppercase().trim().toByteArray())
            timeTableData.getExamTimeTableInfo(email,password,encodedCampus,encodedUserModules){
                    isSuccessful, userInfor ->
                if(isSuccessful){
                    val saveData = UserData()
                    if (userInfor != null) {
                        // f"{module},{module_descripton},{paper},{date},{time},{duration},{venue}"
                        if(userInfor.size == 1){
                            if(userInfor[0].contains("released")){
                                saveData.writeData(R.string.ExamData.toString(),context,"${getCampus}=${getModuleCodes}")
                                isNotOut = true
                            }
                        }else{
                            var save = ""
                            for(moduleInfor in userInfor){
                                val splitToken = moduleInfor.split(",")
                                if(splitToken.size == 7){
                                    val moduleInfo = ExamDetails(splitToken[0],splitToken[1],splitToken[2],splitToken[3],splitToken[4],splitToken[5],splitToken[6])
                                    save += "${moduleInfo.toString()}&&"
                                    ListOfExamDetails.add(moduleInfo)
                                }
                            }
                            save = save.dropLast(2)
                            if(save.isEmpty())
                                saveData.writeData(R.string.ExamData.toString(),context,save)
                            isNotOut = false
                        }
                    }
                }
            }
        }
        else{
            val makeObject = response.split("&&")
            for(examData in makeObject){
                val create = examData.split(";")
                if(create.size==7){
                    val exam = ExamDetails(create[0],create[1],create[2],create[3],create[4],create[5],create[6])
                    ListOfExamDetails.add(exam)
                }
            }
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "EXAM TIMETABLE",
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
            modifier = Modifier.padding(innerPadding).verticalScroll(scrollState).imePadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.padding(top = 16.dp)
                ){
                    if(isLoading.value){
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    else{
                        if(isNotOut){
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
                                        "The June 2025 Final Exam Timetables and the Supplementary Exam timetables will be released on 4 April 2025.",
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }else{
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ListOfExamDetails.forEachIndexed { index, item ->
                                    DisplayModuleDetails(item)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
    // Dialog popup
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "Enter Details") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        OutlinedTextField(
                            value = campus,
                            onValueChange = { campus = it },
                            label = { Text("Campus") },
                            singleLine = true,
                            isError = ErrorCampus,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (ErrorCampus){
                            Text(
                                text = "Required",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        text = "Please enter module codes separated by comma (,) eg Comp102,math140",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().alpha(0.7f)
                    )
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        OutlinedTextField(
                            value = moduleCodes,
                            onValueChange = { moduleCodes = it },
                            label = { Text("ModulesCodes") },
                            singleLine = true,
                            isError = ErrorModuleCodes,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (ErrorModuleCodes){
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
                        Spacer(modifier = Modifier.height(8.dp))
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
                        ErrorCampus = campus.isEmpty()
                        ErrorModuleCodes = moduleCodes.isEmpty()
                        if(ErrorCampus || ErrorModuleCodes){
                            feedbackMessage = "Please provide valid details"
                        }else{
                            if(!moduleCodes.contains(",")){
                                feedbackMessage = "Please separate module code by ','"
                                ErrorModuleCodes = true
                            }else{
                                val localStorage = UserData()
                                //get user data email and password
                                val userInformation = localStorage.readData(R.string.user_information.toString(),context)
                                var email = ""
                                var password = ""
                                if(userInformation.contains(":")){
                                    if(userInformation.split(":").size > 2){
                                        email = userInformation.split(":")[1].trim()
                                        password = userInformation.split(":")[2].trim()

                                    }
                                }
                                val encodedUserModules = Base64.getEncoder().encodeToString(moduleCodes.uppercase().trim().toByteArray())
                                val encodedCampus = Base64.getEncoder().encodeToString(campus.uppercase().trim().toByteArray())
                                timeTableData.getExamTimeTableInfo(email,password,encodedCampus,encodedUserModules){
                                    isSuccessful, userInfor ->

                                        if(isSuccessful){
                                            val saveData = UserData()
                                            if (userInfor != null) {
                                                // f"{module},{module_descripton},{paper},{date},{time},{duration},{venue}"
                                                if(userInfor.size == 1){

                                                    if(userInfor[0].contains("released")){
                                                        saveData.writeData(R.string.ExamData.toString(),context,"${campus}=${moduleCodes}")
                                                        isNotOut = true
                                                    }
                                                }else{
                                                    var save = ""
                                                    for(moduleInfor in userInfor){
                                                        val splitToken = moduleInfor.split(",")
                                                        if(splitToken.size == 7){
                                                            val moduleInfo = ExamDetails(splitToken[0],splitToken[1],splitToken[2],splitToken[3],splitToken[4],splitToken[5],splitToken[6])
                                                            save += "${moduleInfo.toString()}&&"
                                                            ListOfExamDetails.add(moduleInfo)
                                                        }
                                                    }
                                                    save = save.dropLast(2)
                                                    saveData.writeData(R.string.ExamData.toString(),context,save)
                                                    isNotOut = false
                                                }
                                            }
                                        }
                                }

                                showDialog = false
                            }
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Continue")
                }
            }
        )
    }
}

@Composable
fun DisplayModuleDetails(moduleInfo: ExamDetails){

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackGroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,

        ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = moduleInfo.moduleCode.uppercase(),
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
                    text = "Date:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = cardTextColor,
                )
                Text(
                    text = moduleInfo.date.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = cardTextColor,
                )
            }
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
                Text(
                    text = moduleInfo.time,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = cardTextColor,
                )
            }

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp,bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Duration:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor
                )

                Text(
                    text = moduleInfo.duration,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor
                )

            }

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp,bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Paper:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor
                )
                Text(
                    text = moduleInfo.paper.uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor
                )
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

                Text(
                    text = moduleInfo.venue.uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardTextColor
                )

            }
        }
    }
}