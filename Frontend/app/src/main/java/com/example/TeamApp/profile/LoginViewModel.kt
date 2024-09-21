package com.example.TeamApp.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.TeamApp.data.User


class LoginViewModel:ViewModel(){

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init{
        _user.value = User(
            name = "Jan",
            email = "jan@example.com",
            birthDay = "01/01/1990",
            gender = "Male",
            avatar = "testavatar"
        )
    }

    // Example function to set user data
    fun setUser(userData: User) {
        _user.value = userData
    }

    fun navigateToCreateEvent(navController: NavController) {
        navController.navigate("createEvent"){
            popUpTo("profile"){inclusive = true}
        }
    }
    fun navigateToSearchThrough(navController: NavController) {
        navController.navigate("search"){
            popUpTo("profile"){inclusive = true}
        }
    }
    fun navitagateToSettings(navController: NavController) {
        navController.navigate("settings"){
            popUpTo("profile"){inclusive = false}
        }
    }

}