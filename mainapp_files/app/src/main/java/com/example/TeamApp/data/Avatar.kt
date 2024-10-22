package com.example.TeamApp.data

data class Avatar(
    var faceUrl: String,
    var avatarUrl: String
) {
    override fun toString(): String {
        return "Avatar(faceUrl='$faceUrl', avatarUrl='$avatarUrl')"
    }
}
