package com.boxingregistration.app

import com.boxingregistration.app.domain.Member
import com.boxingregistration.app.domain.YearCategory
import com.boxingregistration.app.domain.getAllClubs
import com.boxingregistration.app.domain.getAllYearCategories
import com.boxingregistration.app.persistence.MemberRepository
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional

class UpdateClubCommand(val club: String, val members: List<Member>)

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


    @GetMapping("/club/{club}")
    fun club(@PathVariable club: String): List<Member>
    {
        return repository.findByClub(club)
    }


    @PostMapping("/register")
    fun register(@RequestBody registerCommand: RegisterCommand): List<Member>
    {
        return repository.findByClub(club)
    }


    @Transactional
    @PostMapping("/members/update")
    fun update(@RequestBody updateCommand: UpdateClubCommand)
    {
        repository.deleteByClub(updateCommand.club)
        repository.saveAll(updateCommand.members)
    }
}