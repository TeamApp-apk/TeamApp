package com.example.TeamApp.data
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class Event(
    val iconResId: Int = 0,
    val date: String = "",
    val activityName: String = "",
    val currentParticipants: Int = 0,
    val maxParticipants: Int = 0,
    val location: String = ""
) {
    constructor() : this(0, "", "", 0, 0, "")

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
//    init {
//        this.date = LocalDate.parse(date) // Correct parsing
//        this.endDate = LocalDate.parse(endDate)
//        val daysBetween = ChronoUnit.DAYS.between(this.date, this.endDate)
//        require(daysBetween <= 7) { "Twoje wydarzenie musi zakończyć się w ciągu tygodnia od rozpoczęcia" }
//    }
//
//    fun setDate(date: String?) {
//        this.date = LocalDate.parse(date)
//    }
//
//    fun addParticipant(participant: User): Boolean {
//        if (currentParticipants < maxParticipants && !participants.contains(participant)) {
//            participants.add(participant)
//            currentParticipants++
//            return true
//        }
//        return false
//    }
//
//    fun removeParticipant(participant: User): Boolean {
//        if (participants.remove(participant)) {
//            currentParticipants--
//            return true
//        }
//        return false
//    }

    fun isParticipant(participant: User): Boolean {
        return participants.contains(participant)
    }

    val participantCount: Int
        get() = participants.size
}