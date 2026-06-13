package com.footballmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FootballManagerApplication

fun main(args: Array<String>) {
    runApplication<FootballManagerApplication>(*args)
}
