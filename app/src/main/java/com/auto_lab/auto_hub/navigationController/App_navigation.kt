package com.auto_lab.auto_hub.navigationController

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.auto_lab.auto_hub.R
import com.auto_lab.auto_hub.data_processing.AuthViewModel
import com.auto_lab.auto_hub.data_processing.TimeTableData
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.data_processing.update_maintenance
import com.auto_lab.auto_hub.data_processing.weatherDataModel
import com.auto_lab.auto_hub.home_page.Home_pageScreen
import com.auto_lab.auto_hub.home_page.UpcomingFeatures
import com.auto_lab.auto_hub.login_and_reg.LoginScreen
import com.auto_lab.auto_hub.login_and_reg.RegisterScreen
import com.auto_lab.auto_hub.login_and_reg.Registration_result
import com.auto_lab.auto_hub.login_and_reg.VerificationScreen
import com.auto_lab.auto_hub.login_and_reg.animatedSplashScreen
import com.auto_lab.auto_hub.login_and_reg.recoverScreen
import com.auto_lab.auto_hub.login_and_reg.updateUserInfor
import com.auto_lab.auto_hub.login_and_reg.verifyCode
import com.auto_lab.auto_hub.navigationController.Screens.CampusDetails
import com.auto_lab.auto_hub.navigationController.Screens.CreateTimetable
import com.auto_lab.auto_hub.navigationController.Screens.DownloadModule
import com.auto_lab.auto_hub.navigationController.Screens.HomePage
import com.auto_lab.auto_hub.navigationController.Screens.Login
import com.auto_lab.auto_hub.navigationController.Screens.ModuleInformation
import com.auto_lab.auto_hub.navigationController.Screens.Register
import com.auto_lab.auto_hub.navigationController.Screens.RegisterResult
import com.auto_lab.auto_hub.navigationController.Screens.UpcomingFeatures
import com.auto_lab.auto_hub.navigationController.Screens.Verification
import com.auto_lab.auto_hub.navigationController.Screens.accountRecover
import com.auto_lab.auto_hub.navigationController.Screens.cornfirmVerificationCode
import com.auto_lab.auto_hub.navigationController.Screens.examScreen
import com.auto_lab.auto_hub.navigationController.Screens.newUserDetailScreen
import com.auto_lab.auto_hub.navigationController.Screens.profile
import com.auto_lab.auto_hub.navigationController.Screens.settings
import com.auto_lab.auto_hub.settings_profile.ProfileScreen
import com.auto_lab.auto_hub.settings_profile.SettingsScreen
import com.auto_lab.auto_hub.timetable_feature.Module
import com.auto_lab.auto_hub.timetable_feature.ModuleInputScreen
import com.auto_lab.auto_hub.timetable_feature.Timetable_creationScreen
import com.auto_lab.auto_hub.timetable_feature.UserTimetable_informationScreen
import com.auto_lab.auto_hub.timetable_feature.examTimeTable
import com.auto_lab.auto_hub.ui.theme.CardBackGroundColor
import java.time.LocalDate
import kotlin.toString


@Composable
fun Navigation(authViewModel: AuthViewModel, dataModel: TimeTableData, userData: UserData,weatherDataModel: weatherDataModel,updateMaintenance: update_maintenance,context: Context){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.splashScreen.route, builder = {
        //splash screen
        composable(route = Screens.splashScreen.route){
            animatedSplashScreen(navController,userData,context,updateMaintenance)
        }

        //login screen
        composable(route = Login.route){
            LoginScreen(navController,authViewModel,userData,context)
        }

        //registration screen
        composable(route = Register.route){
            RegisterScreen(navController,userData,authViewModel,context)
        }

        //result of registration screen
        composable(
            route = RegisterResult.route,
            arguments = listOf(navArgument("result"){
                type = NavType.StringType
            })
        ){
            val result= if(it.arguments?.getString("result") != null){
                it.arguments?.getString("result")
            }else{
                ""
            }
            Registration_result(navController, result.toString(),authViewModel,context)
        }

        //verification screen
        composable(
            route = Verification.route,
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                }
            )
        ){
            val email = if(it.arguments?.getString("email") != null){
                it.arguments?.getString("email")
            }else{
                ""
            }
            VerificationScreen(navController,email.toString(), authViewModel)
        }

        //home screen
        composable(
            route = HomePage.route,

        ){

            val user = userData.readData(R.string.user_information.toString(),context)
            val userName = if(user.contains(":")){
                user.split(":")[0]
            }else{
                ""
            }

            val dayOfWeek = LocalDate.now().dayOfWeek.toString().uppercase()
            val tempModulesArray = if(dayOfWeek =="MONDAY"){
                userData.readData(R.string.timetable_monday.toString(),context)
            }else if(dayOfWeek =="TUESDAY"){
                userData.readData(R.string.timetable_tuesday.toString(),context)
            }else if(dayOfWeek== "WEDNESDAY"){
                userData.readData(R.string.timetable_wednesday.toString(),context)
            }else if(dayOfWeek == "THURSDAY"){
                userData.readData(R.string.timetable_thursday.toString(), context)
            }else if(dayOfWeek == "FRIDAY"){
                userData.readData(R.string.timetable_friday.toString(),context)
            }else{
                "weekend"
            }

            var arrayM = mutableListOf<String>() as ArrayList<String>
            if(tempModulesArray.toString().isEmpty() || tempModulesArray.toString().isBlank() || tempModulesArray.toString() == "failed")
                ""
            else if(tempModulesArray.contains("weekend")){
                arrayM.add(
                    "weekend;weekend;weekend;weekend"
                )
            }
            else
                arrayM = ArrayList(tempModulesArray.split("&&"))

            //I know it hard coded values but for now it will do the trick
            val moduleArray = mutableListOf<Module>()
            if(arrayM.isNotEmpty()){
                arrayM.forEach { item ->
                    val listModule = item.split(";")
                    if(listModule.size >2){
                        var moduleCode:String = listModule[0]
                        var sessionType: String = listModule[1]
                        var sessionTime: String = listModule[2]
                        var sessionVenue: String = if(listModule.size>3)
                            listModule[3]
                        else
                            ""
                        moduleArray.add(Module(moduleCode,sessionType,sessionTime,sessionVenue))
                    }
                }
            }

            Home_pageScreen(navController, weatherDataModel,userName.toString(),moduleArray,context,userData)
        }

        //upcoming feature screen
        composable(
            route = UpcomingFeatures.route,
            arguments = listOf(
                navArgument("typeOfFeature"){
                    type = NavType.StringType
                }
            )
        ){
            val typeOfFeature = if(it.arguments?.getString("typeOfFeature") != null){
                it.arguments?.getString("typeOfFeature")
            }else{
                ""
            }
            UpcomingFeatures(navController,typeOfFeature.toString())
        }

        //show timetable screen
        composable(
            route = CreateTimetable.route,
            arguments = listOf(
                navArgument("listOfModules"){
                    type = NavType.StringType
                }
            )
        ){

            val listOfModules= if(it.arguments?.getString("listOfModules") != null){
                it.arguments?.getString("listOfModules")
            }else{
                ""
            }

            val dayOfWeek = if(it.arguments?.getString("dayOfWeek") != null){
                it.arguments?.getString("dayOfWeek")
            }else{
                LocalDate.now().dayOfWeek.toString()
            }

            var arrayM = mutableListOf<String>() as ArrayList<String>

            if(listOfModules != null)
                if(listOfModules.isNotEmpty())
                    arrayM = ArrayList(listOfModules.split("&&"))

            if(dayOfWeek.toString().uppercase() == "FRIDAY")
                Log.i("firday error",arrayM.toString())
            var moduleArray = mutableListOf<Module>() as ArrayList<Module>

            //i know it hard coded values but for now it will do the trick
            if(arrayM.isNotEmpty()){
                arrayM.forEach { item ->
                    if(item.contains(";")){
                        val listModule = item.split(";")
                        if(listModule.size >2){
                            var moduleCode:String = listModule[0]
                            var sessionType: String = listModule[1]
                            var sessionTime: String = listModule[2]
                            var sessionVenue: String = if(listModule.size>3)
                                listModule[3]
                            else
                                ""
                            moduleArray.add(Module(moduleCode,sessionType,sessionTime,sessionVenue))
                        }
                    }
                }
            }
            Timetable_creationScreen(navController,moduleArray, dayOfWeek.toString().uppercase(),userData,context)
        }

        //get timetable information screen
        composable(
            route = ModuleInformation.route,
            arguments = listOf(
                navArgument("initialFieldCount"){
                    type = NavType.IntType
                },
                navArgument("campus"){
                    type = NavType.StringType
                }
            )
        ){
            val initialFieldCount = if(it.arguments?.getInt("initialFieldCount") != null){
                it.arguments!!.getInt("initialFieldCount")
            } else {
                0
            }
            val campus = if(it.arguments?.getString("campus")!=null){
                it.arguments?.getString("campus")
            }else{
                ""
            }
            ModuleInputScreen(navController,initialFieldCount, campus.toString(),context,userData,dataModel)
        }

        composable(
            route = DownloadModule.route,
            arguments = listOf(
                navArgument("moduleNames"){
                    type = NavType.StringType
                },
                navArgument("dropdownOptionsList"){
                    type = NavType.StringType
                },
                navArgument("campus"){
                    type = NavType.StringType
                }
            )
        ){
            val moduleCode = if(it.arguments?.getString("moduleNames")!=null){
                it.arguments?.getString("moduleNames")
            }else{
                ""
            }

            val listOfModuleCode = moduleCode.toString().split(",")

            val timetableBlocks = if(it.arguments?.getString("dropdownOptionsList")!=null){
                it.arguments?.getString("dropdownOptionsList")
            }else{
                ""
            }

            val splitBlock = timetableBlocks.toString().split("!")
            var tbBlocks = mutableListOf<List<String>>()
            for(i in 0..<splitBlock.size){
                if(splitBlock[i].contains(";")){
                    tbBlocks.add(splitBlock[i].split(";"))
                }else{
                    val  temp = mutableListOf<String>(splitBlock[i])
                    tbBlocks.add(temp)
                }
            }

            val campus = if(it.arguments?.getString("campus")!=null){
                it.arguments?.getString("campus")
            }else{
                ""
            }

            Log.i("timetable response navigation", tbBlocks[0].toString())
            ModuleInputScreen(navController,listOfModuleCode,tbBlocks,userData,context,campus.toString(),dataModel)
        }

        //get campus and number of modules screen
        composable(
            route = CampusDetails.route
        ){
            UserTimetable_informationScreen(navController)
        }

        composable(
            route = accountRecover.route
        ){
            recoverScreen(navController,authViewModel)
        }

        composable(
            route = cornfirmVerificationCode.route,
            arguments = listOf(
                navArgument("code"){
                    type = NavType.StringType
                },
                navArgument("email"){
                    type = NavType.StringType
                }
            )

        ){
            val verificationCode = if(it.arguments?.getString("code")!=null){
                it.arguments?.getString("code")
            }else{
                ""
            }

            val email = if(it.arguments?.getString("email")!=null){
                it.arguments?.getString("email")
            }else{
                ""
            }

            verifyCode(navController,email.toString(),verificationCode.toString())
        }

        composable(
            route = newUserDetailScreen.route,
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                }
            )

        ){

            val email = if(it.arguments?.getString("email")!=null){
                it.arguments?.getString("email")
            }else{
                ""
            }

            updateUserInfor(navController, authViewModel,userData,context,email.toString())
        }

        composable(
            route = settings.route
        ){
            SettingsScreen(navController)
        }

        composable(
            route = profile.route
        ){
            ProfileScreen(navController,userData,context,authViewModel)
        }

        composable(
            route = examScreen.route
        ){
            examTimeTable(navController,dataModel,context)
        }

        composable(
            route = Screens.update.route
        ){
            updateScreen(context)
        }

        composable(
            route = Screens.maintenance.route
        ){
            maintenance()
        }

    })
}

@Composable
fun navBar(navController: NavController){
    val navitems = listOf<NavItems>(
        NavItems("Home",Icons.Default.Home, HomePage.route),
        NavItems("Profile", Icons.Default.Person, profile.route),
        NavItems("Setting", Icons.Default.Settings, settings.route)
    )

    NavigationBar(
        containerColor = CardBackGroundColor
    ) {
        navitems.forEach{ navItems ->
            NavigationBarItem(
                selected = navController.currentDestination?.route == navItems.route,
                onClick = {
                    navController.navigate(navItems.route){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                },
                icon = {
                    Icon(imageVector = navItems.icon, contentDescription = "",
                        tint = if (navController.currentDestination?.route == navItems.route){
                            Color.Black
                        }
                        else{
                            Color.White
                        }
                    )
                },
                label = {
                    Text(text = navItems.label, color = Color.White)
                }
            )
        }
    }
}