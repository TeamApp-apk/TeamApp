package com.example.TeamApp.data
import java.util.UUID


data class Event(
    var participants: MutableList<Any> = ArrayList(),
    val creatorID:String? =null,
    val iconResId: String = "",
    val pinIconResId: String = "",
    val date: String = "",
    val activityName: String = "",
    val eventName: String? = null,
    val price: String? = null,
    val skillLevel: String? = null,
    val currentParticipants: Int = 0,
    val maxParticipants: Int = 0,
    val location: String = "",
    val description: String = "",
    val locationID: Map<String, Coordinates>? = null,
    val id: String = "",
    val lastMessage: Map<String, Any>? = null
) {
    constructor() : this(mutableListOf(),null,"",
        "", "", "","","","", 0, 0,
        "", "", null, "")


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
            "Kalistenika" to "figma_calistenics_icon",
            "Łyżwiarstwo" to "figma_iceskate_icon",
            "Piłka nożna" to "figma_soccer_icon",
            "Pingpong" to "figma_pingpong_icon",
            "Pływanie" to "figma_swimming_icon",
            "Rzutki" to "figma_dart_icon",
            "Siatkówka" to "figma_volleyball_icon",
            "Siłownia" to "figma_gym_icon",
            "Szermierka" to "figma_fencing_icon",
            "Tenis" to "figma_tennis_icon",
            "Wędkarstwo" to "figma_fishing_icon"
        )
        val sportPinIcons: Map<String,String> = mapOf(
            "Badminton" to "figma_badminton_pin",
            "Biliard" to "figma_pool_pin",
            "Bieganie" to "figma_running_pin",
            "Boks" to "figma_boxing_pin",
            "Jazda na deskorolce" to "figma_skate_pin",
            "Jazda na rolkach" to "figma_rollerskates_pin",
            "Kajakarstwo" to "figma_kayak_pin",
            "Kolarstwo" to "figma_cycling_pin",
            "Koszykówka" to "figma_basketball_pin",
            "Kręgle" to "figma_bowling_pin",
            "Kalistenika" to "figma_calistenics_pin",
            "Łyżwiarstwo" to "figma_iceskate_pin",
            "Piłka nożna" to "figma_soccer_pin",
            "Pingpong" to "figma_pingpong_pin",
            "Pływanie" to "figma_swimming_pin",
            "Rzutki" to "figma_dart_pin",
            "Siatkówka" to "figma_volleyball_pin",
            "Siłownia" to "figma_gym_pin",
            "Szermierka" to "figma_fencing_pin",
            "Tenis" to "figma_tennis_pin",
            "Wędkarstwo" to "figma_fishing_pin"
        )

    }





    fun isParticipant(participant: User): Boolean {
        return participants.contains(participant)
    }
    override fun toString(): String {
        return "Event(id='$id', " +
                "creatorID=$creatorID, " +
                "iconResId='$iconResId', " +
                "pinIconResId='$pinIconResId', " +
                "date='$date', " +
                "activityName='$activityName', " +
                "currentParticipants=$currentParticipants, " +
                "maxParticipants=$maxParticipants, " +
                "location='$location', " +
                "description='$description', " +
                "locationID=$locationID, " +
                "participants=$participants, " +
                "lastMessage=$lastMessage)"
    }
}