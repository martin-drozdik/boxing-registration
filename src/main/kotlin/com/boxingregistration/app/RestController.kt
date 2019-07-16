package com.boxingregistration.app

import com.boxingregistration.app.domain.*
import com.boxingregistration.app.persistence.MemberRepository
import com.boxingregistration.app.persistence.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException




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
    fun register(@RequestBody registerCommand: RegisterCommand): ResponseEntity<RegisteredUser>
    {
        if (userRepository.findByEmail(registerCommand.email) != null)
        {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The email address ${registerCommand.email} is already in use.")
        }

        if (userRepository.findByClub(registerCommand.club).isNotEmpty())
        {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "There is already somebody registered as the manager of club '${registerCommand.club}'.")
        }

        val coach = RegisteredUser(registerCommand.name, registerCommand.email, encoder.encode(registerCommand.password), registerCommand.club)

        val successfullySavedUser = userRepository.save(coach)
        val message = SimpleMailMessage()
        message.setTo(registerCommand.email)
        message.setSubject("Vitaj v AIBA")
        message.setText("Vitaj v AIBA")
        try
        {
            emailSender.send(message)
        }
        catch (error: Throwable)
        {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is a problem with our mail server. Please contact the administrator of this webpage.")
        }

        return ResponseEntity.ok(successfullySavedUser)
    }


    @Transactional
    @PostMapping("/members/update")
    fun update(@RequestBody updateCommand: UpdateClubCommand)
    {
        memberRepository.saveAll(updateCommand.members)
    }
}