package edu.metrostate.ics342.mediatracker.data.model

data class TokenRequest(
    val grantType: String,
    val email: String,
    val password: String,
    val clientId: String,
    val clientSecret: String
)