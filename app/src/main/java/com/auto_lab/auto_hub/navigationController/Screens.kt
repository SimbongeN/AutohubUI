package com.auto_lab.auto_hub.navigationController

sealed class Screens(val route: String) {

    //splash screen
    object splashScreen: Screens(route = "splashScreen")

    //register Screens and login screens
    object Login: Screens(route = "login_Screen") //login
    object Register: Screens(route = "Register_Screen")
    object RegisterResult: Screens(route = "RegisterResult_Screen/{result}")
    object Verification: Screens(route = "Verification_Screen/{email}")

    //acount recovery
    object accountRecover: Screens(route = "accountRecovery")
    object cornfirmVerificationCode: Screens(route = "confirmVerificationCode/{code}/{email}")
    object newUserDetailScreen: Screens(route = "userDetail/{email}")

    //homepage
    object HomePage: Screens(route = "home_Screen")
    object UpcomingFeatures: Screens(route = "UpcomingFeatures_Screen/{typeOfFeature}")


    //timetable operation
    object CreateTimetable: Screens(route = "CreateTimetable_Screen/{listOfModules}/{dayOfWeek}")
    object DownloadModule: Screens(route = "downloadModules/{moduleNames}/{dropdownOptionsList}/{campus}")
    object ModuleInformation: Screens(route = "ModuleInformation_Screen/{initialFieldCount}/{campus}")
    object CampusDetails: Screens(route = "CampusDetails_Screen")
    object examScreen : Screens(route = "examScreen")

    //setting and profile screens
    object settings: Screens(route = "settings")
    object profile: Screens(route = "profile")

    //Update and maintenance screens
    object update: Screens(route = "update")
    object maintenance: Screens(route = "maintenance")
}