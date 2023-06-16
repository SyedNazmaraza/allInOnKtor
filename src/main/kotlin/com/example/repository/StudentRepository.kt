package com.example.repository

import com.example.model.studentsModel.StudentCredentials
import com.example.utils.BaseResponse

interface StudentRepository {
    suspend fun registerStudent(params:StudentCredentials): BaseResponse<Any>

    suspend fun loginStudent(email:String,password:String):  BaseResponse<Any>
    suspend fun findStudentByEmail(params: StudentCredentials): Boolean


}