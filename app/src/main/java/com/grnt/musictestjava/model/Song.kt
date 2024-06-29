package com.grnt.musictestjava.model

import java.io.Serializable

data class Song(
    var id: String,
    val title: String,
    val artist: String,
    val url: String
):Serializable
