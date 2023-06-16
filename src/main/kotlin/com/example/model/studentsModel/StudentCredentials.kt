package com.example.model.studentsModel

import kotlinx.serialization.Serializable

@Serializable
data class StudentCredentials(
    val userName:String , val email:String, val password:String
)