package com.example.security

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity(){
    var env = environment.config
    JwtConfig.initializer("secret")
    install(Authentication){
        jwt {
            verifier(JwtConfig.instance.verifier)
            validate {
                if(it.payload.getClaim("email").asString()!="" && it.payload.getClaim("password").asString()!=""){
                    JWTPrincipal(it.payload)
                }
                else{
                    null
                }
            }
            challenge{ _, _ ->
                call.respond(HttpStatusCode.Unauthorized,"Token is not valid or expired")
            }
        }

    }
}