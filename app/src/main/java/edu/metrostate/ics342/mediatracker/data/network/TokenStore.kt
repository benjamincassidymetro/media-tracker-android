package edu.metrostate.ics342.mediatracker.data.network

object TokenStore {
    var accessToken: String? = null
    var refreshToken: String? = null

    fun clear() {
        accessToken = null
        refreshToken = null
    }
}