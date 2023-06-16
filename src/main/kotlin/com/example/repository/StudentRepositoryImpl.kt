package com.example.repository

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.studentsModel.StudentCredentials
import com.example.security.JwtConfig
import com.example.security.hash
import com.example.service.StudentService
import com.example.service.StudentServiceImp
import com.example.utils.BaseResponse
import io.ktor.server.config.*
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class StudentRepositoryImpl(var service: StudentService) : StudentRepository {
//    val secret =config.property("jwt.secret").getString()
//    val issuer =config.property("jwt.issuer").getString()
//    val audience =config.property("jwt.audience").getString()
//    val myRealm = config.property("jwt.realm").getString()

    override suspend fun registerStudent(params: StudentCredentials): BaseResponse<Any> {
        if (findStudentByEmail(params)) {
            var student = service.registerStudent(params)
            if (student != null) {
                val token = JwtConfig.instance.createToken(params.email,params.password)
                return BaseResponse.SuccessResponse(data = token, mes = "Success")
            }
            return BaseResponse.ErrorResponse(mes = "Failure")
        }
        return BaseResponse.ErrorResponse(mes = "Email Already Exists")
    }

    override suspend fun loginStudent(email: String, password: String) : BaseResponse<Any>{
        val student= service.findStudentByEmail(StudentCredentials("c",email,password))
        if(student?.email.equals(email) && student?.password.equals(hash(password))){
            return BaseResponse.SuccessResponse(mes = "Success")
        }
        else{
            return BaseResponse.ErrorResponse(mes="Invalid Credentials")
        }

    }

    override suspend fun findStudentByEmail(params: StudentCredentials): Boolean {
        var student = service.findStudentByEmail(params)
        if(student!=null){
            return false
        }
        return true
    }
}