package com.example.TeamApp.data
import java.util.UUID


data class Event(
    val participants: List<Any> = ArrayList(),
    val creatorID:String? =null,
    val iconResId: String = "",
    val date: String = "",
    val activityName: String = "",
    val currentParticipants: Int = 0,
    val maxParticipants: Int = 0,
    val location: String = "",
    val description: String = "",
    val locationID: Map<String, Coordinates>? = null,
    val id: String = UUID.randomUUID().toString()
) {
    constructor() : this(emptyList(),null,"", "", "", 0, 0, "", "", null, UUID.randomUUID().toString())


    companion object {
        val sportIcons: Map<String, String> = mapOf(
            "Badminton" to "figma_badminton_icon",
            "Bilard" to "figma_pool_icon",
            "Bieganie" to "figma_running_icon",
            "Boks" to "figma_boxing_icon",
            "Jazda na deskorolce" to "figma_skate_icon",
            "Jazda na rolkach" to "figma_rollerskates_icon",
            "Kajakarstwo" to "figma_kayak_icon",
            "Kolarstwo" to "figma_cycling_icon",
            "Koszykówka" to "figma_basketball_icon",
            "Kręgle" to "figma_bowling_icon",
            "Kulturystyka" to "figma_calistenics_icon",
            "Łyżwiarstwo" to "figma_iceskate_icon",
            "Piłka nożna" to "figma_soccer_icon",
            "Pingpong" to "figma_pingpong_icon",
            "Pływanie" to "figma_swimming_icon",
            "Rzutki" to "figma_dart_icon",
            "Siatkówka" to "figma_volleyball_icon",
            "Siłownia" to "figma_gym_icon",
            "Szermierka" to "figma_fencing_icon",
            "Tenis" to "figma_tennis_icon",
            "Wędkarstwo" to "figma_fising_icon"
        )
    }




    fun isParticipant(participant: User): Boolean {
        return participants.contains(participant)
    }
}