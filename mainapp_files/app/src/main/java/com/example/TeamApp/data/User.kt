package com.example.TeamApp.data

import java.util.UUID

class User {
    // Getters and Setters
    var name: String? = null
    var email: String? = null
    var birthDay: String? = null
    var gender: String? = null;
    var avatar: String? = null;
    var userID: String = UUID.randomUUID().toString()
    var attendedEvents: MutableList<Any> = ArrayList()
    //age do dodania
    constructor()

    constructor(name: String?, email: String?) {
        this.name = name
        this.email = email
    }

    constructor(name: String?, email: String?, birthDay: String?, gender: String?, avatar: String?) {
        this.name = name
        this.email = email
        this.birthDay = birthDay
        this.gender = gender
        this.avatar = avatar
    }

    override fun toString(): String {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userID='" + userID + '\'' +
                ", avatarUrl" + avatar + '\'' + // Dodajemy userID do toString()
                '}'
    }

}