package com.example.controller

import com.example.model.studentsModel.StudentCredentials
import com.example.repository.StudentRepository
import com.example.repository.StudentRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent
import org.koin.ktor.ext.inject

fun Application.studentsRouting(){
    val repo by inject<StudentRepositoryImpl>()
    routing {
        route("/student") {
            post("/register") {
                var params = call.receive<StudentCredentials>()
                var response = repo.registerStudent(params)
                call.respond(response)
            }
            authenticate {
                post("/login") {
                    val principal = call.principal<JWTPrincipal>()
                    val email = principal!!.payload.getClaim("email").asString()
                    val password = principal.payload.getClaim("password").asString()
                    val expiration = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    var response = repo.loginStudent(email,password)
                    call.respond(response)
                }
            }
        }
    }
}