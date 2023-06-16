package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.database.DatabaseFactory
import com.example.model.customerModel.customerReq
import io.ktor.server.application.*
import com.example.controller.*
import com.example.repository.StudentRepository
import com.example.repository.StudentRepositoryImpl
import com.example.security.configureSecurity
import com.example.service.StudentService
import com.example.service.StudentServiceImp
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.core.scope.get
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.koin
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val appModule = org.koin.dsl.module {
        single<StudentService> { StudentServiceImp() }
        single { StudentRepositoryImpl(get())}
    }
    DatabaseFactory.init(environment.config)
    install(ContentNegotiation){
        gson()
    }
    configureSecurity()
    koin {
        modules(appModule)
    }
    studentsRouting()
}

fun Application.start(){
    install(ContentNegotiation) {
        gson()
    }
    install(RequestValidation) {
        validate<customerReq> { it ->
            if (it.name.length < 5) {
                ValidationResult.Invalid("Name Should Have Min 5 Characters")
            } else {
                ValidationResult.Valid
            }
        }
    }
    install(RateLimit){
        register(RateLimitName("public")){
            rateLimiter(limit = 2, refillPeriod = 60.seconds)
        }
    }
    install(StatusPages){
        status(HttpStatusCode.TooManyRequests){
            call,status -> val retryAfter = call.response.headers["Retry-After"]
            call.respondText(text = "429: Too many requests. Wait for $retryAfter seconds.", status = status)
        }
        exception<RequestValidationException>{
                call, cause -> call.respondText(cause.reasons.toString()) }
    }
    configureRouting()
}

//fun main() = runBlocking{
//    var job = launch (Dispatchers.Default){
//        println("in 8080 ${Thread.currentThread().name}")
//        embeddedServer(Netty, port = 8080, host = "localHost", module = Application::module).start(wait = true)
//    }
//    var job1 = launch(Dispatchers.Default) {
//        println("in 8081 ${Thread.currentThread().name}")
//        embeddedServer(Netty, port = 8081, host = "localHost", module = Application::start).start(wait = true)
//    }
//    job.join()
//    job1.join()
//}


