package com.example.gameplanet.utils

import android.content.Context

class SharedPreference(
    context : Context
){
    private val sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
    fun saveUserSharedPref(
        userName: String,
        isLogged : Boolean,
        userId : Int
    ){
        val editor = sharedPref.edit()
        editor.putString("userName", userName)
        editor.putBoolean("isLogged",isLogged)
        editor.putInt("userId", userId)
        editor.apply()
    }
    fun getUserNameSharedPref(): String? {
        return sharedPref.getString("userName", "")
    }
    fun getUserIdSharedPref(): Int{
        return sharedPref.getInt("userId", 0)
    }
    fun getisLoggedSharedPref() : Boolean{
        return sharedPref.getBoolean("isLogged",false)
    }
    fun removeUserSheredPref(){
        val editor = sharedPref.edit()
        editor.remove("userName")
        editor.remove("isLogged")
        editor.remove("userId")
        editor.apply()
    }
}