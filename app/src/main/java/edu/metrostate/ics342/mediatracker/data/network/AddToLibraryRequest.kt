package edu.metrostate.ics342.mediatracker.data.network

data class AddToLibraryRequest(
    val mediaId: Int,
    val status: String
)

