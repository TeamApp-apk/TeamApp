package com.example.TeamApp.data

class User {
    // Getters and Setters
    var name: String? = null
    var email: String? = null
    //age do dodania
    constructor()

    constructor(name: String?, email: String?) {
        this.name = name
        this.email = email
    }

    override fun toString(): String {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\''
    }
}