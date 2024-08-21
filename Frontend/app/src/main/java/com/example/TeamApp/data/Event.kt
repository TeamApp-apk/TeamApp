package com.example.TeamApp.data
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class Event(
    val iconResId: String = "",
    val date: String = "",
    val activityName: String = "",
    val currentParticipants: Int = 0,
    val maxParticipants: Int = 0,
    val location: String = ""
) {
    constructor() : this("", "", "", 0, 0, "")

//    private val endDate: LocalDate
    private val participants: MutableList<User> = ArrayList()

    enum class SportDiscipline {
        FOOTBALL,
        TENNIS,
        BASKETBALL,
        VOLLEYBALL,
        CYCLING,
        SWIMMING,
        RUNNING,
        GOLF,
        YOGA,
        BILLIARD,
        BADMINTON,
        GYM
    }


    fun isParticipant(participant: User): Boolean {
        return participants.contains(participant)
    }
}