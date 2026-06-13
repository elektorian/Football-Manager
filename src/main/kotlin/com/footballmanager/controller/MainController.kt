package com.footballmanager.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    @GetMapping("/")
    fun index(): String {
        return "Welcome to Football Manager! This is a blank page."
    }
}
