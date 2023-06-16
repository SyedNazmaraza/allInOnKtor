package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*

class JwtConfig private constructor(private var secret : String){
    private var algorithm = Algorithm.HMAC256(secret)
    companion object{
        private const val AUDIENCE = "http://0.0.0.0:8080/"
        private const val ISSURE: String = "http://0.0.0.0:8080/register"
        private const val CLAIM:String="email"
        lateinit var instance : JwtConfig
            private set
        fun initializer(secret: String){
            synchronized(this){
                if (!this::instance.isInitialized){
                    instance= JwtConfig(secret)
                }
            }
        }
    }
    val verifier: JWTVerifier =JWT.require(algorithm).withAudience(AUDIENCE).withIssuer(ISSURE).build()
    fun createToken(studentEmail:String,studentPass:String): String =JWT.create()
            .withAudience(AUDIENCE)
            .withIssuer(ISSURE)
            .withClaim(CLAIM,studentEmail)
            .withClaim("password",studentPass)
            .sign(algorithm)

}