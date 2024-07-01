package com.grnt.musictestjava.model

import java.io.Serializable

data class Song(
    val id: String,
    val albumid: String,
    val name: String,
    val artist: String,
    val streamurl: String
):Serializable
