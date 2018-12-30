package com.boxingregistration.app

import com.boxingregistration.app.domain.Member
import com.boxingregistration.app.domain.YearCategory
import com.boxingregistration.app.domain.getAllClubs
import com.boxingregistration.app.domain.getAllYearCategories
import com.boxingregistration.app.persistence.MemberRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(val repository: MemberRepository)
{
    @GetMapping("/categories")
    fun categories(): List<YearCategory>
    {
        return getAllYearCategories()
    }

    @GetMapping("/clubs")
    fun clubs(): List<String>
    {
        return getAllClubs()
    }

    @GetMapping("/members")
    fun all(): List<Member>
    {
        return repository.findAll()
    }

    @PostMapping("/members")
    fun update(@RequestBody newMembers: List<Member>): List<Member>
    {
        return repository.saveAll(newMembers)
    }
}