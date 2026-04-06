package com.pulsemind

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PulseMindApplication

fun main(args: Array<String>) {
    runApplication<PulseMindApplication>(*args)
}

