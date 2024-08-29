package com.example.TeamApp.data
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


data class Event(
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

    companion object {
        val sportIcons: Map<String, String> = mapOf(
            "Badminton" to "figma_badminton_icon",
            "Koszykówka" to "figma_basketball_icon",
            "Kręgle" to "figma_bowling_icon",
            "Boks" to "figma_boxing_icon",
            "Kulturystyka" to "figma_calistenics_icon",
            "Kolarstwo" to "figma_cycling_icon",
            "Rzutki" to "figma_dart_icon",
            "Szermierka" to "figma_fencing_icon",
            "Wędkarstwo" to "figma_fising_icon",
            "Siłownia" to "figma_gym_icon",
            "Łyżwiarstwo" to "figma_iceskate_icon",
            "Kajakarstwo" to "figma_kayak_icon",
            "Pingpong" to "figma_pingpong_icon",
            "Bilard" to "figma_pool_icon",
            "Jazda na rolkach" to "figma_rollerskates_icon",
            "Bieganie" to "figma_running_icon",
            "Jazda na deskorolce" to "figma_skate_icon",
            "Piłka nożna" to "figma_soccer_icon",
            "Pływanie" to "figma_swimming_icon",
            "Tenis" to "figma_tennis_icon",
            "Siatkówka" to "figma_volleyball_icon"
        )
    }



    fun isParticipant(participant: User): Boolean {
        return participants.contains(participant)
    }
}