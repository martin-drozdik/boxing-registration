package com.boxingregistration.app

import com.boxingregistration.app.domain.RegisteredUser
import com.boxingregistration.app.persistence.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


@Component
class Admins(userRepository: UserRepository, passwordEncoder: PasswordEncoder)
{
    init
    {
        val admin1email = "martin.drozdik@centrum.sk"
        val admin1 = RegisteredUser("Martin Drozdik", admin1email, passwordEncoder.encode("kokot"), "", true)
        if (userRepository.findByEmail(admin1email) == null)
        {
            userRepository.save(admin1)
        }
    }
}