package com.example.service


import com.example.model.studentsModel.Student
import com.example.model.studentsModel.StudentCredentials

interface StudentService {

    suspend fun registerStudent(params:StudentCredentials): Student?
    suspend fun findStudentByEmail(params: StudentCredentials):Student?
    suspend fun deleteStudentByEmail(params: StudentCredentials): Boolean
    suspend fun getAllStudents(params: StudentCredentials):List<Student?>?
}