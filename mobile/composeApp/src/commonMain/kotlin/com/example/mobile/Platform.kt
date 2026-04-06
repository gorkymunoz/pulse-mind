package com.example.mobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform