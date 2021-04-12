package com.example.gamenews.model

import java.io.Serializable

data class News(
    val title: String?,
    val type: String?,
    val img: String?,
    val click_url: String?,
    val time: String?,
    val top: String?
) : Serializable
