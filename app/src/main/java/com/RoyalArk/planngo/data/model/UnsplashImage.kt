package com.RoyalArk.planngo.data.model

data class UnsplashImage(
    val id: String,
    val description: String?,
    val urls: Urls,
    val alt_description: String?
)

data class Urls(
    val regular: String
)

data class UnsplashResponse(
    val results: List<UnsplashImage>
)

