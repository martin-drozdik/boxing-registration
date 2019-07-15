package com.boxingregistration.app

import com.boxingregistration.app.domain.*
import com.boxingregistration.app.persistence.MemberRepository
import com.boxingregistration.app.persistence.UserRepository
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional


class UpdateClubCommand(val members: List<Member>)

class RegisterCommand(val name: String, val email: String, val password: String, val club: String)

@RestController
@RequestMapping("/api")
class MemberController
(
    val memberRepository: MemberRepository,
    val userRepository: UserRepository,
    val emailSender: JavaMailSender,
    private val encoder: PasswordEncoder
)
{
    @GetMapping("/login")
    fun login(@AuthenticationPrincipal user: RegisteredUser): RegisteredUser
    {
        return user
    }

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
    fun all(@AuthenticationPrincipal user: RegisteredUser): List<Member>
    {
        return memberRepository.findByClub(user.club)
    }


    @GetMapping("/club/{club}")
    fun club(@PathVariable club: String): List<Member>
    {
        return memberRepository.findByClub(club)
    }


    @PostMapping("/register")
    fun register(@RequestBody registerCommand: RegisterCommand): RegisteredUser
    {
        val coach = RegisteredUser(registerCommand.name, registerCommand.email, encoder.encode(registerCommand.password), registerCommand.club)

        val successfullySavedUser = userRepository.save(coach)

        val message = SimpleMailMessage()
        message.setTo(registerCommand.email)
        message.setSubject("Vitaj v AIBA")
        message.setText("Vitaj v AIBA")
        emailSender.send(message)

        return successfullySavedUser
    }


    @Transactional
    @PostMapping("/members/update")
    fun update(@RequestBody updateCommand: UpdateClubCommand)
    {
        memberRepository.saveAll(updateCommand.members)
    }
}