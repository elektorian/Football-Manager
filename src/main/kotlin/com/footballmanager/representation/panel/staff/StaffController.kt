package com.footballmanager.representation.panel.staff

import com.footballmanager.representation.panel.staff.dto.StaffInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/staff")
class StaffController(
    private val staffViewService: StaffViewService,
) {
    @GetMapping
    fun getStaff(@RequestParam team: UUID): StaffInfo = staffViewService.getStaff(team)
}