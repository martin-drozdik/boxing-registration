package com.boxingregistration.app

import com.boxingregistration.app.domain.*
import com.boxingregistration.app.persistence.MemberRepository
import com.boxingregistration.app.persistence.TournamentRepository
import com.boxingregistration.app.persistence.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.transaction.Transactional


class UpdateClubCommand(val members: MutableList<Member>)

class RegisterCommand(val name: String, val email: String, val password: String, val club: String)

class NewTournamentCommand(val name: String)

@RestController
@RequestMapping("/api")
class MemberController
(
    val memberRepository: MemberRepository,
    val userRepository: UserRepository,
    val tournamentRepository: TournamentRepository,
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


    @GetMapping("/tournament")
    fun tournament(): String
    {
        val tournament = tournamentRepository.findByIsCurrent(true)
        if (tournament.isEmpty())
        {
            return ""
        }
        require(tournament.size == 1)
        return tournament.first().name
    }


    @GetMapping("/members")
    fun members(@AuthenticationPrincipal user: RegisteredUser): List<Member>
    {
        val tournament = tournament()
        if (tournament.isEmpty())
        {
            return emptyList()
        }
        return memberRepository.findByClub(user.club).filter { it.tournament_name == tournament()}
    }


    @GetMapping("/all")
    fun all(@AuthenticationPrincipal user: RegisteredUser): List<Member>
    {
        require(user.isAdmin)
        val tournament = tournament()
        if (tournament.isEmpty())
        {
            return emptyList()
        }
        return memberRepository.findAll().filter { it.tournament_name == tournament()}
    }


    @Transactional
    @PostMapping("/newtournament")
    fun newtournament
    (
        @AuthenticationPrincipal user: RegisteredUser,
        @RequestBody newTournamentCommand: NewTournamentCommand

    ): String
    {
        require(newTournamentCommand.name.isNotEmpty())
        val currentTournaments = tournamentRepository.findByIsCurrent(true)
        require(currentTournaments.size <= 1)
        if (currentTournaments.size == 1)
        {
            val currentTournament = currentTournaments.first()
            currentTournament.isCurrent = false
            tournamentRepository.save(currentTournament)
        }
        val tournament = Tournament(newTournamentCommand.name, true)
        tournamentRepository.save(tournament)

        return tournament.name
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

        val coach = RegisteredUser(registerCommand.name, registerCommand.email, encoder.encode(registerCommand.password), registerCommand.club, false)

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
    fun update(@RequestBody updateCommand: UpdateClubCommand, @AuthenticationPrincipal user: RegisteredUser)
    {
        require(!user.isAdmin)

        val tournament = tournament()
        require(tournament.isNotEmpty())
        updateCommand.members.forEach {
            it.coach = user.email
            it.tournament_name = tournament
            it.club = user.club
        }
        memberRepository.saveAll(updateCommand.members)
    }
}